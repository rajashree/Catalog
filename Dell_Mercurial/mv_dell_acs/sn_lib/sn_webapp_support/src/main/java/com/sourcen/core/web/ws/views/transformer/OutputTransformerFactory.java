/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.ws.views.transformer;

import com.sourcen.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2806 $, $Date:: 2012-06-01 10:40:50#$
 */
public class OutputTransformerFactory {

    private static final Logger logger = LoggerFactory.getLogger(OutputTransformerFactory.class);


    public static JsonOutputTransformer getJsonOutputTransformer(final HttpServletRequest request) {
        String outputFormat = getOutputFormat(request);
        JsonOutputTransformer transformer = null;
       if (outputFormat == null) {
            transformer = new StandardJsonOutputTransformer();
        } else if (!outputFormat.equalsIgnoreCase("standard")) {
            transformer = getOutputTransformer(request, outputFormat, "Json");
        }
        if (transformer == null) {
            transformer = new StandardJsonOutputTransformer();
        }
        transformer.setHttpServletRequest(request);
        return transformer;
    }

    public static XmlOutputTransformer getXmlOutputTransformer(final HttpServletRequest request) {
        String outputFormat = getOutputFormat(request);
        XmlOutputTransformer transformer = null;
        if (outputFormat == null) {
            transformer = new StandardXmlOutputTransformer();
        } else if (!outputFormat.equalsIgnoreCase("default")) {
            transformer = getOutputTransformer(request, outputFormat, "Xml");
        }
        if (transformer == null) {
            transformer = new StandardXmlOutputTransformer();
        }
        return transformer;
    }


    private static <T extends OutputTransformer> T getOutputTransformer(final HttpServletRequest request, final String outputFormat, String outputType) {
        Assert.notNull(outputFormat);
        String transformerName = StringUtils.capitalize(outputFormat);
        if (!transformerName.isEmpty()) {
            String transformerClassName = getDefaultTransformerPackagePrefix(request) + outputType + "OutputTransformer";
            logger.debug("trying to load transformer :=" + transformerClassName);
            try {
                // return the specific class.
                Class transformerClass = ClassUtils.forName(transformerClassName, ClassUtils.getDefaultClassLoader());
                return (T) transformerClass.newInstance();
            } catch (ClassNotFoundException e) {
                logger.warn("unable to find transformer for outputFormat:=" + transformerName + ", and resolved transformerClassName:=" + transformerClassName);
            } catch (Exception e) {
                logger.warn("unable to create transformer for outputFormat:=" + transformerName + ", and resolved transformerClassName:=" + transformerClassName, e);
            }
        }
        return null;
    }

    public static String getOutputFormat(final HttpServletRequest request) {

        String outputFormat = null;
        if (request.getAttribute("outputFormat") != null) {
            outputFormat = request.getAttribute("outputFormat").toString();
        } else if (request.getParameter("outputFormat") != null) {
            outputFormat = request.getParameter("outputFormat");
        }
        if (outputFormat == null) {
            if (request.getRequestURI().indexOf("v1") != -1) {
                outputFormat = "Dell";
            } else if (request.getRequestURI().indexOf("v2") != -1) {
                outputFormat = "standard";
            }
        }
        return outputFormat;
    }

    public Map<String, String> getTransformPrefix() {
        if (transformPrefix.isEmpty()) {
            transformPrefix.put("/api/v1/rest/", "com.dell.acs.web.ws.v1.views.transformer.retailers.Dell");
            transformPrefix.put("/api/v2/rest/", "com.sourcen.core.web.ws.views.transformer.Standard");
        }
        return transformPrefix;
    }

    public static String getDefaultTransformerPackagePrefix(final HttpServletRequest request) {
        if (request.getRequestURI().indexOf("v1") != -1) {
            return transformPrefix.get("/api/v1/rest/");
        } else if (request.getRequestURI().indexOf("v2") != -1) {
            return transformPrefix.get("/api/v2/rest/");
        }
        //Control should never reach here. Default case v2
        return transformPrefix.get("/api/v2/rest/");
    }

    private static Map<String, String> transformPrefix = Collections.emptyMap();

    public void setTransformPrefix(Map<String, String> transformPrefix) {
        OutputTransformerFactory.transformPrefix = transformPrefix;
    }
}
