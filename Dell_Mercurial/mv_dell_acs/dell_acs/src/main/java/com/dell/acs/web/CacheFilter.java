package com.dell.acs.web;

import com.sourcen.core.cache.Cache;
import com.sourcen.core.cache.CacheProvider;
import com.sourcen.core.cache.ehcache.EhCacheServiceImpl;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.config.ConfigurationServiceImpl;
import com.sourcen.core.util.FileSystem;
import com.sourcen.core.util.FileUtils;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.DigestUtils;
import org.springframework.util.SerializationUtils;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

/** @author Navin Raj Kumar G.S. */
public class CacheFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(CacheFilter.class);

    private FilterConfig filterConfig;

    private static final ConfigurationService configurationService = ConfigurationServiceImpl.getInstance();

    private static final CacheProvider cacheProvider = EhCacheServiceImpl.getInstance().getSystemCacheManager();

    private static final Collection<String> patternsToCache = new HashSet<String>();

    private static final AntPathMatcher patternMatcher = new AntPathMatcher();

    private static final Map<String, Cache> cachedUrls = new ConcurrentHashMap<String, Cache>();

    private static final Cache NO_PATTERN_PATCHED = cacheProvider.getCache("CacheFilter.patternsIgnored");

    public static FileSystem fileSystem = null;

    private static File respCacheDir = null;

    // loads the files from /META-INF/mime.types, the default list does not have image/png
    private static MimetypesFileTypeMap typeMap = new MimetypesFileTypeMap();

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        try {
            fileSystem = configurationService.getFileSystem();
            respCacheDir = configurationService.getFileSystem().getDirectory("/cdn/caches/responseCaches", true, true);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        resetCache();
    }

    @Override
    public void doFilter(final ServletRequest _request, final ServletResponse _response, final FilterChain filterChain)
            throws IOException, ServletException {

        if (_request instanceof HttpServletRequest && _response instanceof HttpServletResponse) {
            HttpServletRequest request = (HttpServletRequest) _request;
            HttpServletResponse response = (HttpServletResponse) _response;

            String url = getStrippedUrl(request);

            Cache cacheToSearch = cachedUrls.get(url);

            if (cacheToSearch == null) {
                String uri = WebUtils.getRequestPath(request);
                for (String pattern : patternsToCache) {
                    if (patternMatcher.match(pattern, uri)) {
                        cacheToSearch = cacheProvider.getCache(pattern);
                        cachedUrls.put(uri, cacheToSearch);
                        break;
                    }
                }
                if (cacheToSearch == null) {
                    cachedUrls.put(uri, NO_PATTERN_PATCHED);
                }
            }

            boolean isCacheEnabled;

            if (configurationService.isDevMode()) {
                isCacheEnabled = configurationService.getBooleanProperty(CacheFilter.class, "isEnabled", false);
            } else {
                isCacheEnabled = true;
            }

            // finally it can be overridden in the request parameter.
            isCacheEnabled = WebUtils.getBooleanParameter(request, "cached", isCacheEnabled);

            if (!isCacheEnabled || cacheToSearch == null || cacheToSearch == NO_PATTERN_PATCHED) {
                filterChain.doFilter(_request, _response);
            } else {
                Object value = cacheToSearch.getValue(url);
                byte[] body = null;
                if (value != null) {
                    body = (byte[]) value;
                } else {
                    // check if we have it in the directory
                    String requestPath = StringUtils.MD5Hash(url) + ".cache";
                    File cacheFile = new File(respCacheDir, requestPath);
                    if (cacheFile.exists() && cacheFile.canRead() && cacheFile.length() > 0) {
                        body = FileUtils.readFileToByteArray(cacheFile);
                    } else {
                        // generate cache
                        ShadowResponseWrapper responseWrapper = new ShadowResponseWrapper(response);

                        // this is to disable compression so that we can get the actual content.
                        request.setAttribute("__forceCompression", false);

                        filterChain.doFilter(request, responseWrapper);
                        byte[] content = responseWrapper.toByteArray();
                        int statusCode = responseWrapper.getStatusCode();
                        if (statusCode == 200) {
                            // save the cache file.

                            if (!cacheFile.exists()) {
                                cacheFile.createNewFile();
                            } else {
                                cacheFile.delete();
                            }

                            if (cacheFile.canRead()) {
                                // if we cannot read, then some other thread set this to readable=false.
                                // just ignore it.
                                cacheFile.setReadable(false);
                                // now save the content.

                                // write headers first.
                                byte[] serializedHeader = SerializationUtils.serialize(responseWrapper.getHeaders());

                                ByteBuffer buffer = ByteBuffer.allocate(4 + serializedHeader.length + content.length);
                                buffer.putInt(serializedHeader.length);
                                buffer.put(serializedHeader);
                                buffer.put(content);
                                body = buffer.array();

                                FileOutputStream stream = new FileOutputStream(cacheFile);
                                stream.write(body);
                                stream.flush();
                                stream.close();
                                cacheToSearch.put(url, body);
                                cacheFile.setReadable(true);

                                writeBody(request, response, responseWrapper.getHeaders(), content);
                                return;
                            }
                        } else {
                            if(content.length>0){
                                response.getOutputStream().write(content);
                            }
                            response.setStatus(statusCode);
                        }
                    }
                }

                // we must have body here.
                if (body != null) {
                    ByteBuffer buffer = ByteBuffer.allocate(body.length).put(body);
                    int headerSerializedSize = buffer.getInt(0);
                    byte[] headerBytes = new byte[headerSerializedSize];
                    buffer.position(4);
                    buffer.get(headerBytes);
                    buffer.position(4 + headerSerializedSize);
                    byte[] content = new byte[body.length - 4 - headerSerializedSize];
                    buffer.get(content);

                    Map<String, List<String>> headers = (Map<String, List<String>>) SerializationUtils
                            .deserialize(headerBytes);
                    writeBody(request, response, headers, content);
                } else {
                    // ignore.
                }
            }

        } else {
            filterChain.doFilter(_request, _response);
        }

    }

    private void writeBody(HttpServletRequest request, HttpServletResponse response,
            final Map<String, List<String>> headers, final byte[] content) throws IOException {
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            for (String headerValue : entry.getValue()) {
                response.addHeader(entry.getKey(), headerValue);
            }
        }
        // set other caching headers.
        response.setHeader("Cache-Control", "max-age=120, must-revalidate");
        response.setDateHeader("Expires", Calendar.getInstance().getTime().getTime() + 120000);
        response.setDateHeader("Last-Modified", Calendar.getInstance().getTime().getTime() + 120000);
        response.getOutputStream().write(content);
        response.setContentLength(content.length);
    }

    protected String getEtag(byte[] bytes) {
        StringBuilder builder = new StringBuilder("\"0");
        DigestUtils.appendMd5DigestAsHex(bytes, builder);
        builder.append('"');
        return builder.toString();
    }

    public static String getStrippedUrl(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        String qs = request.getQueryString();
        if (qs != null) {
            // check if we have to remove any parameters in the query parameters.
            return url + "?" + qs;
        }
        return url;
    }

    public static void resetCache() {
        synchronized (patternsToCache) {
            if (patternsToCache.size() > 0) {
                for (String pattern : patternsToCache) {
                    if (cacheProvider.containsCache(pattern)) {
                        cacheProvider.removeCache(pattern);
                    }
                }
            }

            patternsToCache.clear();

            // clear the cache directory.
            try {
                FileUtils.cleanDirectory(respCacheDir);
            } catch (IOException e) {
                throw new RuntimeException("Unable to proceed further, as we are not able to clear the " +
                        "/cdn/caches/responseCache due to a file permission error.", e);
            }

            String stringPatterns = configurationService.getProperty(CacheFilter.class, "patternsToCache");
            if (stringPatterns != null && stringPatterns.isEmpty()) {
                StringTokenizer tokenizer = new StringTokenizer(stringPatterns, ",\n", false);
                while (tokenizer.hasMoreElements()) {
                    String token = tokenizer.nextToken();

                    if (patternMatcher.isPattern(token)) {
                        logger.info("token :='{}' is added to the cache filter.", token);
                        patternsToCache.add(token);
                    } else {
                        logger.warn("com.dell.acs.web.CacheFilter.patternsToCache values contains a invalid pattern." +
                                "Please verify if token :='{}' is correct", token);
                    }
                }
            }
            if (patternsToCache.isEmpty()) {
                patternsToCache.add("/api/v?/rest/**/get**");
            }
        }
    }

    @Override
    public void destroy() {
    }

    protected String getContentType(final String fileName) {
        if (fileName.endsWith(".js") || fileName.endsWith(".json") || fileName.endsWith(".jsonp")) {
            return "text/javascript";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else {
            return typeMap.getContentType(fileName);
        }
    }

    private static class ShadowResponseWrapper extends HttpServletResponseWrapper {

        private final ByteArrayOutputStream content = new ByteArrayOutputStream();

        private final ServletOutputStream outputStream = new ResponseServletOutputStream();

        private PrintWriter writer;

        private int statusCode = HttpServletResponse.SC_OK;

        final Map<String, List<String>> headers = new HashMap<String, List<String>>();

        private ShadowResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public void setHeader(String name, String value) {
            List<String> values = new ArrayList<String>();
            values.add(value);
            headers.put(name, values);
            super.setHeader(name, value);
        }

        @Override
        public void addHeader(String name, String value) {
            List<String> values = headers.get(name);
            if (values == null) {
                values = new ArrayList<String>();
                headers.put(name, values);
            }
            values.add(value);
            super.addHeader(name, value);
        }

        @Override
        public void setDateHeader(final String name, final long date) {
            super.setDateHeader(name, date);
            setHeader(name, String.valueOf(date));
        }

        @Override
        public void addDateHeader(final String name, final long date) {
            super.addDateHeader(name, date);
            addHeader(name, String.valueOf(date));
        }

        @Override
        public void setIntHeader(final String name, final int value) {
            setHeader(name, String.valueOf(value));
        }

        @Override
        public void addIntHeader(final String name, final int value) {
            addHeader(name, String.valueOf(value));
        }

        @Override
        public void setContentType(final String type) {
            addHeader("Content-Type", type);
        }

        @Override
        public String getContentType() {
            List<String> values = headers.get("Content-Type");
            return (values == null || !values.isEmpty()) ? null : values.get(0);
        }

        @Override
        public boolean containsHeader(final String name) {
            return headers.containsKey(name);
        }

        public Map<String, List<String>> getHeaders() {
            return headers;
        }

        @Override
        public void setStatus(int sc) {
            super.setStatus(sc);
            this.statusCode = sc;
        }

        @Override
        public void setStatus(int sc, String sm) {
            super.setStatus(sc, sm);
            this.statusCode = sc;
        }

        @Override
        public void sendError(int sc) throws IOException {
            super.sendError(sc);
            this.statusCode = sc;
        }

        @Override
        public void sendError(int sc, String msg) throws IOException {
            super.sendError(sc, msg);
            this.statusCode = sc;
        }

        @Override
        public void setContentLength(int len) {
        }

        @Override
        public ServletOutputStream getOutputStream() {
            return this.outputStream;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            if (this.writer == null) {
                String characterEncoding = getCharacterEncoding();
                this.writer = (characterEncoding != null ? new ResponsePrintWriter(
                        characterEncoding) : new ResponsePrintWriter(
                        org.springframework.web.util.WebUtils.DEFAULT_CHARACTER_ENCODING));
            }
            return this.writer;
        }

        @Override
        public void resetBuffer() {
            this.content.reset();
        }

        @Override
        public void reset() {
            super.reset();
            resetBuffer();
        }

        private int getStatusCode() {
            return statusCode;
        }

        private byte[] toByteArray() {
            return this.content.toByteArray();
        }

        private class ResponseServletOutputStream extends ServletOutputStream {

            @Override
            public void write(int b) throws IOException {
                content.write(b);
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                content.write(b, off, len);
            }
        }

        private class ResponsePrintWriter extends PrintWriter {

            private ResponsePrintWriter(String characterEncoding) throws UnsupportedEncodingException {
                super(new OutputStreamWriter(content, characterEncoding));
            }

            @Override
            public void write(char buf[], int off, int len) {
                super.write(buf, off, len);
                super.flush();
            }

            @Override
            public void write(String s, int off, int len) {
                super.write(s, off, len);
                super.flush();
            }

            @Override
            public void write(int c) {
                super.write(c);
                super.flush();
            }
        }
    }

}
