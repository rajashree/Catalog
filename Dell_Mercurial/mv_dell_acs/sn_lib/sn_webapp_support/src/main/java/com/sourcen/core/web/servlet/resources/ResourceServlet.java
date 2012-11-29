/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */


package com.sourcen.core.web.servlet.resources;

import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.WebUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

/**
 @author Navin Raj Kumar G.S.
 @author $LastChangedBy: ashish $
 @version $Revision: 3497 $, $Date:: 2012-06-21 15:01:35#$
 @since 1.0 */

public abstract class ResourceServlet extends HttpServlet {

    private static final long serialVersionUID = 7865538451758928130L;

    protected final Logger log = LoggerFactory.getLogger(getClass());


    public static final Collection<String> allowedFileExtensions =
            // Included additional file types which can be served by this servlet
            Arrays.asList("jpg", "gif", "png", "jpeg", "bmp", "html", "js", "css", "pdf", "csv", "doc", "docx", "swf");

    // loads the files from /META-INF/mime.types, the default list does not have image/png
    private static MimetypesFileTypeMap typeMap = new MimetypesFileTypeMap();

    protected void serveFile(final HttpServletRequest request, final HttpServletResponse response, final File file) {
        if (file.exists() && file.canRead()) {
            InputStream inputStream = null;
            ServletOutputStream servletOutputStream = null;
            try {
                if (isModified(request, file) || isCacheDisabled(request, file)) {
                    // set the required headers.
                    response.setDateHeader("Last-Modified", file.lastModified());
                    response.setHeader("Etag", getEtag(file));
                    response.setContentType(getContentType(file));

                    // load the file into the stream
                    inputStream = new FileInputStream(file);
                    servletOutputStream = response.getOutputStream();
                    final byte[] buffer = new byte[100 * 1024];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        servletOutputStream.write(buffer, 0, len);
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                }
            } catch (final IOException ioe) {
                log.error("Unable to send the requested file " + file.getAbsolutePath() + ": " + ioe.getMessage());
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (final Exception ignored) { /* ignored */
                }
                try {
                    if (servletOutputStream != null) {
                        servletOutputStream.close();
                    }
                } catch (final Exception ignored) { /* ignored */
                }
            }
        } else {
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } catch (final IOException e) {
                // we cannot do much here now.
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    protected String getContentType(final File file) {
        final String fileName = file.getName();
        if (fileName.endsWith(".js")) {
            return "text/javascript";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else if (fileName.endsWith(".pdf")) {
            return "application/pdf";
        } else {
            return ResourceServlet.typeMap.getContentType(fileName);
        }
    }

    protected boolean isModified(final HttpServletRequest request, final File file) throws IOException {
        if (request.getHeader("If-Modified-Since") != null && request.getHeader("If-None-Match") != null) {
            final String latestEtag = getEtag(file);
            final String browserEtag = request.getHeader("If-None-Match");
            if (!browserEtag.equalsIgnoreCase(latestEtag)) {
                return true;
            }
            final long browserModifiedTime = request.getDateHeader("If-Modified-Since");
            final long fileModifiedTime = file.lastModified();
            // we are always dividing by 1000, as the browserModifiedTime doesn't store milliseconds.
            // example: browserModifiedTime = 1243625825000 and the fileModifiedTime=1243625825781 for the same file.
            if (browserModifiedTime / 1000 != fileModifiedTime / 1000) {
                return true;
            }
            return false;
        } else {
            return true;
        }
    }

    protected boolean isCacheDisabled(final HttpServletRequest request, final File file) {
        return WebUtils.getParameter(request, "disableCache", false)
                || WebUtils.getParameter(request, "noCache", false);
    }

    protected String getEtag(final File file) throws IOException {
        return StringUtils.MD5Hash(file.getCanonicalPath() + " - " + file.length());
    }

    //
    // HTTP METHODS.
    //

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        String requestPath = request.getPathInfo();
        if (allowedFileExtensions.contains(FilenameUtils.getExtension(requestPath).toLowerCase())) {
            File file = getFile(request, requestPath);
            if (file != null) {
                serveFile(request, response, file);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }
    }

    protected abstract File getFile(final HttpServletRequest request, final String requestPath);

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }


}
