/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */


package com.sourcen.core.web.filter.compression;

import com.sourcen.core.config.ConfigurationServiceEvent;
import com.sourcen.core.event.EventDispatcher;
import com.sourcen.core.web.filter.AbstractFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 3030 $, $Date:: 2012-06-08 10:22:36#$
 * @since 1.0
 */
public class CompressionFilter extends AbstractFilter implements ConfigurationServiceEvent.PropertyUpdated {

    static final Logger log = LoggerFactory.getLogger(CompressionFilter.class);

    public static final String DISABLE_COMPRESSION_HEADER_KEY = "CompressionFilter.disableCompression.header.key";

    public static final String DISABLE_COMPRESSION_REQ_PARAM_KEY = "CompressionFilter.disableCompression.requestParameter.key";

    private static String defaultTypes = "text/css, text/javascript, text/html, image/jpeg, image/png," +
            " image/jpg, image/gif, application/json, text/xml, text";

    private static Set<String> supportedContentTypes = configureSupportedContentTypes(defaultTypes);

    public static Collection<String> getSupportedContentTypes() {
        return supportedContentTypes;
    }

    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
        String contentTypes = getConfigurationService().getProperty(CompressionFilter.class, "supportedContentTypes", "");
        configureSupportedContentTypes(defaultTypes + "," + contentTypes);
        EventDispatcher.addListener(this);
    }

    @Override
    public void onPropertyUpdated(ConfigurationServiceEvent event) {
        if (event.isProperty(CompressionFilter.class, "supportedContentTypes")) {
            configureSupportedContentTypes(defaultTypes + "," + event.getPropertyValue());
        }
    }

    protected static Set<String> configureSupportedContentTypes(String types) {
        String[] propValues = types.split(",");
        Set<String> supportedTypes = new HashSet<String>(propValues.length);
        for (String prop : propValues) {
            prop = prop.trim();
            if (prop.length() > 0) {
                supportedTypes.add(prop.trim());
            }
        }
        supportedContentTypes = Collections.unmodifiableSet(supportedTypes);
        return supportedContentTypes;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {

        boolean isResponseCompressed = false;

        // check if we can compress this request or not.
        if (continueCompression(request, response)) {

            // now check if we have the encoders and construct a response wrapper.
            final CompressionStreamEncoder encoder = EncodingStrategy.determineEncoder(request.getHeader(EncodingStrategy.HEADER_ACCEPT_ENCODING));
            if (encoder != null) {
                // we have a encoding format that we can use.
                // create the stream & response wrapper and apply the filter.
                final CompressionAwareResponseWrapper responseWrapper = new CompressionAwareResponseWrapper(request, response, encoder);
                responseWrapper.setCompressionThreshold(getConfigurationService().getIntegerProperty(CompressionFilter.class, "compressionThreshold", 10240));
                try {
                    isResponseCompressed = true;
                    filterChain.doFilter(request, responseWrapper);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    responseWrapper.finish();
                }
            }
        }

        // this is either if compression was disabled, or browser doesn't support compression
        if (!isResponseCompressed) {
            filterChain.doFilter(request, response);
        }
    }

    protected boolean continueCompression(final HttpServletRequest request, final HttpServletResponse response) {

        if (response.isCommitted()) {
            log.debug("Response has already been committed.");
            return false;
        }

        if (request.getParameter("__forceCompression") != null && request.getParameter("__forceCompression").equals("true")) {  // only developers use this for testing...
            return true;
        }

        if(request.getAttribute("__forceCompression") != null){
            return (Boolean)request.getAttribute("__forceCompression");
        }

        // check if devMode or if compression is enabled.
        if (!getConfigurationService().getBooleanProperty("compression.enabled", false)) {
            return false;
        }

        // check if there is any header set.
        final String DisableCompressionHeaderKey = getConfigurationService().getProperty(CompressionFilter.DISABLE_COMPRESSION_HEADER_KEY, "Disable-Compression");
        if (response.containsHeader(DisableCompressionHeaderKey)) {
            return false;
        }

        // check if there is any request parameter.
        final String disableCompressionRequestParamKey = getConfigurationService().getProperty(CompressionFilter.DISABLE_COMPRESSION_REQ_PARAM_KEY, "nocompression");
        final String reqParam = request.getParameter(disableCompressionRequestParamKey);
        if (reqParam != null && reqParam.equalsIgnoreCase("true")) {
            return false;
        }
        return true;
    }

}
