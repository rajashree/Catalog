/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

/**
 *
 */

package com.sourcen.core.util;

import com.sourcen.core.util.collections.MapBackedPropertiesProvider;
import com.sourcen.core.util.collections.PriorityBasedPropertiesProvider;
import com.sourcen.core.util.collections.PropertiesProvider;
import com.sourcen.core.util.collections.RefreshableMapBackedPropertiesProvider;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 A simple utility that can convert paths to URL Parts for menu systems and navigation also providing methods for managing
 requests.

 @author Navin Raj Kumar G.S.
 @author $LastChangedBy: mahalaxmi $
 @version $Revision: 3198 $, $Date:: 2012-06-15 08:52:16#$ */
public class WebUtils extends org.springframework.web.util.WebUtils {

    public static final String LINE_BREAK = System.getProperty("line.separator");

    private static final Logger logger = Logger.getLogger(WebUtils.class);

    public static boolean isPost(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase("POST");
    }

    // //////////// methods related to URL paths.
    public static String getRequestPath(HttpServletRequest request) {
        // / /EECentral/WEB-INF/ui/views/accounts/user-management/users.jsp
        String uri = request.getRequestURI();
        if (uri.indexOf("/WEB-INF/ui/views/") >= 0) {
            uri = uri.substring(uri.indexOf("/WEB-INF/ui/views/") + 17).replaceFirst(".jsp", ".do");
        } else {
            uri = uri.replaceFirst(request.getContextPath(), "");
        }
        return uri;
    }

    public static String[] getUrlParts(HttpServletRequest request) {
        String url = getRequestPath(request);

        if (url.startsWith("/")) {
            url = url.replaceFirst("/", "");
        }
        if (url.endsWith(".do")) {
            url = url.substring(0, url.lastIndexOf(".do"));
        }

        String[] urlParts = url.split("/");
        return urlParts;
    }

    public static String getPart(HttpServletRequest request, Integer index) {
        String[] parts = getUrlParts(request);
        if (parts.length < index) {
            return "";
        }
        return parts[index];
    }

    public static Object isPathActive(HttpServletRequest request, String item, Object result) {
        String path = getRequestPath(request);
        if (path.startsWith(item)) {
            return result;
        }
        return "";
    }

    public static Boolean isActive(HttpServletRequest request, String item, Integer index) {
        String[] parts = getUrlParts(request);
        if (parts.length < index) {
            return false;
        }
        return parts[index].equalsIgnoreCase(item);
    }

    public static Object isActive(HttpServletRequest request, String item, Integer index, Object result) {
        if (isActive(request, item, index)) {
            return result;
        }
        return "";
    }

    public static String getAbsoluteApplicationPath(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        try {
            return new File(WebUtils.class.getResource("/").toURI().getPath() + "../.." + path).toURI().getPath();
        } catch (Exception e) {
            logger.warn("Unable to retrive filePath for location:=" + path, e);
        }
        return null;
    }

    public static File getAbsoluteApplicationFile(String path) {
        return new File(getAbsoluteApplicationPath(path));
    }


    public static void getScriptFiles(File srcFile, Collection<File> list, Boolean recursive) {
        if (srcFile.exists()) {
            File[] files = srcFile.listFiles();
            for (File file : files) {
                if (file.exists()) {
                    if (file.isDirectory() && recursive) {
                        getScriptFiles(file.getAbsoluteFile(), list, recursive);
                    } else {
                        list.add(file);
                    }

                }
            }
        } else {
            logger.warn("Unable to getScriptFiles for :" + srcFile.getAbsolutePath());
        }
    }

    /**
     check if a request has debugEnabled or not.

     @param request
     @return
     */
    public static boolean isDebug(HttpServletRequest request) {
        boolean isDebug = getParameter(request, "debug", false);

        if (!isDebug) {
            // check cookie.
            Cookie cookie = WebUtils.getCookie(request, "eec.debug");
            if (cookie != null) {
                isDebug = cookie.getValue().equalsIgnoreCase("true");
            }
        }
        return isDebug;
    }

    public static boolean hasParameter(HttpServletRequest request, String paramName) {
        return request.getParameterMap().containsKey(paramName);
    }


    public static Date getParameter(HttpServletRequest request, String paramName) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        if (request != null && request.getParameterMap().containsKey(paramName)) {
            String paramValue = request.getParameter(paramName);
            if (!WebUtils.isNullOrEmpty(paramValue)) {
                try {
                    return dateFormat.parse(paramValue.toString());
                } catch (ParseException e) {
                    logger.info("Error: While parsing the date - " + paramValue.toString() + "\n " + e.getMessage());
                    return null;
                }
            }
        }
        return null;
    }

    public static String getParameter(HttpServletRequest request, String paramName, String defaultValue) {
        if (request != null && request.getParameterMap().containsKey(paramName)) {
            String paramValue = request.getParameter(paramName);
            if (!WebUtils.isNullOrEmpty(paramValue)) {
                return paramValue.toString();
            }
        }
        return defaultValue;
    }

    public static Long getParameter(HttpServletRequest request, String paramName, Long defaultValue) {
        if (request != null && request.getParameterMap().containsKey(paramName)) {
            String paramValue = request.getParameter(paramName);
            if (!WebUtils.isNullOrEmpty(paramValue)) {
                try {
                    return Long.parseLong(paramValue);
                } catch (Exception e) {
                    logger.warn(
                            " error while parsing parameter : " + paramName + "=" + paramValue + ", " + e.getMessage());
                }
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static Double getParameter(HttpServletRequest request, String paramName, Double defaultValue) {
        if (request != null && request.getParameterMap().containsKey(paramName)) {
            String paramValue = request.getParameter(paramName);
            if (!WebUtils.isNullOrEmpty(paramValue)) {
                try {
                    return Double.parseDouble(paramValue);
                } catch (Exception e) {
                    logger.warn(
                            " error while parsing parameter : " + paramName + "=" + paramValue + ", " + e.getMessage());
                }
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static Integer getParameter(HttpServletRequest request, String paramName, Integer defaultValue) {
        if (request != null && request.getParameterMap().containsKey(paramName)) {
            String paramValue = request.getParameter(paramName);
            if (!WebUtils.isNullOrEmpty(paramValue)) {
                try {
                    return Integer.parseInt(paramValue);
                } catch (Exception e) {
                    logger.warn(
                            " error while parsing parameter : " + paramName + "=" + paramValue + ", " + e.getMessage());
                }
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static Float getParameter(HttpServletRequest request, String paramName, Float defaultValue) {
        if (request != null && request.getParameterMap().containsKey(paramName)) {
            String paramValue = request.getParameter(paramName);
            if (!WebUtils.isNullOrEmpty(paramValue)) {
                try {
                    return Float.parseFloat(paramValue);
                } catch (Exception e) {
                    logger.warn(
                            " error while parsing parameter : " + paramName + "=" + paramValue + ", " + e.getMessage());
                }
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static Boolean getParameter(HttpServletRequest request, String paramName, Boolean defaultValue) {
        if (request != null && request.getParameterMap().containsKey(paramName)) {
            String paramValue = request.getParameter(paramName);
            if (!WebUtils.isNullOrEmpty(paramValue)) {
                try {
                    return Boolean.parseBoolean(paramValue);
                } catch (Exception e) {
                    logger.warn(
                            " error while parsing parameter : " + paramName + "=" + paramValue + ", " + e.getMessage());
                }
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static List<String> getParametersList(HttpServletRequest request, String paramName) {
        if (request != null && request.getParameterMap().containsKey(paramName)) {
            String[] paramValue = request.getParameterValues(paramName);
            if (paramValue != null) {
                List<String> paramValues = new ArrayList<String>();
                try {
                    for (Object value : paramValue) {
                        paramValues.add((String) value);
                    }
                    return paramValues;
                } catch (Exception e) {
                    logger.warn(
                            " error while parsing parameter : " + paramName + "=" + paramValue + ", " + e.getMessage());
                }
                return Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }

    @Deprecated
    public static List<? extends Object> getParametersList(HttpServletRequest request, String paramName, Class<?> clazz) {
        if (request != null && request.getParameterMap().containsKey(paramName)) {
            String[] paramValue = request.getParameterValues(paramName);
            if (paramValue != null) {
                List<Object> paramValues = new ArrayList<Object>();
                try {
                    for (Object value : paramValue) {

                        paramValues.add(Class.forName(clazz.getName()).cast(value));
                    }
                    return paramValues;
                } catch (Exception e) {
                    logger.warn(
                            " error while parsing parameter : " + paramName + "=" + paramValue + ", " + e.getMessage());
                }
                // Add null check in the callee class
                return null;//Collections.emptyList();
            }
        }
        // Add null check in the callee class
        return null;
    }


    public static Date parseDate(String date) {
        throw new UnsupportedOperationException("needs to be implemented");
    }

    public static boolean isNullOrEmpty(String str) {
        if (null == str || "".equals(str) || "null".equals(str)) {
            return true;
        }
        return false;
    }

    public static boolean isStringNullOrEmpty(String str) {
        if (null == str || "".equals(str.trim())) {
            return true;
        }
        return false;
    }

    public static Set<Long> asLongList(HttpServletRequest request, String paramName) {
        String parameterValue = WebUtils.getParameter(request, paramName, "");
        if (parameterValue == null || "".equals(parameterValue)) {
            return new TreeSet<Long>();
        }

        String[] tokens = parameterValue.split(",");
        TreeSet<Long> list = new TreeSet<Long>();

        for (String t : tokens) {
            try {
                long value = Long.parseLong(t);
                list.add(value);
            } catch (NumberFormatException e) {
                // ignore has a bad token
            }
        }
        return list;
    }

    private static final PriorityBasedPropertiesProvider settings = new PriorityBasedPropertiesProvider();

    private static final PropertiesProvider defaultProperties = new MapBackedPropertiesProvider();

    private static final RefreshableMapBackedPropertiesProvider userDefinedProperties =
            new RefreshableMapBackedPropertiesProvider();

    public static final String DEFAULT_ACTION_EXTENSION_KEY = "WebUtils.defaultActionExtension";

    public static final String PUBLIC_FILE_EXTENSIONS_KEY = "WebUtils.publicFileExensions";

    static {
        defaultProperties.setProperty(DEFAULT_ACTION_EXTENSION_KEY, "do");
        defaultProperties.setProperty(PUBLIC_FILE_EXTENSIONS_KEY, "ico,jpg,gif,png,css,js,jpeg");

        // set the providers.
        settings.addProvider(0, userDefinedProperties);
        settings.addProvider(1, defaultProperties);
    }

    public static void setSettings(Map newSettings) {
        userDefinedProperties.refresh(newSettings);
    }

    public static void setSetting(String key, String value) {
        userDefinedProperties.setProperty(key, value);
    }


    public static Map getSettings() {
        return userDefinedProperties.getBackingMap();
    }

    public static String defaultActionExtension() {
        return settings.getProperty(DEFAULT_ACTION_EXTENSION_KEY);
    }


    public static Collection<String> getPublicFileExtensions() {
        return StringUtils.asCollection(settings.getProperty(PUBLIC_FILE_EXTENSIONS_KEY), ",");
    }

    public static Boolean getBooleanParameter(HttpServletRequest request, String parameterName) {
        String paramValue = request.getParameter(parameterName);
        return paramValue != null && paramValue.equalsIgnoreCase("true");
    }

    public static Boolean getBooleanParameter(HttpServletRequest request, String parameterName, boolean defaultValue) {
        String paramValue = request.getParameter(parameterName);
        if (paramValue != null) {
            return paramValue != null && paramValue.equalsIgnoreCase("true");
        }
        return defaultValue;
    }

    public static Long getLongParameter(HttpServletRequest request, String parameterName, Long defaultValue) {
        String paramValue = request.getParameter(parameterName);
        try {
            if (paramValue != null) {
                return Long.valueOf(paramValue);
            }
        } catch (Exception e) {
            // ignore return default.
        }
        return defaultValue;
    }

    public static PropertiesProvider parameters(HttpServletRequest request) {
        return new MapBackedPropertiesProvider(request.getParameterMap(), true);
    }

}
