/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util;

import com.sourcen.core.SourcenRuntimeException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 3520 $, $Date:: 2012-06-22 10:55:07#$
 * @since 1.0
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {

    private static final char[] USERNAME_DISALLOWED_CHARS = {'/',';','#',',',':','$','*','&','^','!','@','(',')','{','}','~','>','<','?','+','='};

    public static final String CDN_RESOURCE_FILE_PATH_SEPARATOR = "/";

    private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);

    public static String toString(Object object) {
        return ToStringBuilder.reflectionToString(object);
    }

    /**
     * Allowed chars in a username are word chars (a-zA-Z0-9_) and dash, @, and dot.
     *
     * @param username A username
     * @return true if the username is supported, false otherwise
     */
    public static boolean isUsernameValid(String username) {
        return username != null
                && StringUtils.indexOfAny(username, USERNAME_DISALLOWED_CHARS) == -1
                && (username.length() == username.replaceAll("\\s","").length());
    }

    public static String getSimpleString(String str) {
        return str.replaceAll("[^a-zA-Z0-9\\s\\.\\/\\_]+", "").replaceAll("[\\s\\.]+", "_");
    }

    public static List<Long> asLongList(String values) {
        List<Long> longList = new ArrayList<Long>();
        String[] valArray = StringUtils.split(values, ",");
        if (valArray.length > 0) {
            for (String value : valArray) {
                if (StringUtils.isNotEmpty(value)) {
                    longList.add(Long.parseLong(value));
                }
            }
        }
        return longList;
    }

    public static String MD5Hash(final String plainText, String characterEncoding) {
        try {
            return DigestUtils.md5DigestAsHex(plainText.getBytes(characterEncoding));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
            throw new SourcenRuntimeException(e);
        }
    }

    public static String MD5Hash(final String plainText) {
        return MD5Hash(plainText, "UTF-8");
    }

    public static String toHex(final byte[] bytes) {
        return new String(Hex.encodeHex(bytes));
    }

    public static byte[] fromHex(final String hexString) {
        try {
            return Hex.decodeHex(hexString.toCharArray());
        } catch (final DecoderException e) {
            logger.warn("unable to decode hexString :=" + hexString + " into bytes", e);
        }
        return null;
    }

    /**
     * trim the trialing slashes from a String.
     *
     * @param str of type String
     *
     * @return String
     */
    public static String trimTrialingSlashes(final String str) {
        final char[] value = str.toCharArray();
        final int startIndex = 0;
        int endIndex = value.length;
        for (int i = endIndex - 1; i > startIndex; i--) {
            if (value[i] == '/') {
                endIndex--;
            } else {
                break;
            }
        }
        return str.substring(startIndex, endIndex);
    }

    public static String convertUnderscoreNameToPropertyName(String name) {
        StringBuilder result = new StringBuilder();
        boolean nextIsUpper = false;
        if (name != null && name.length() > 0) {
            if (name.length() > 1 && name.substring(1, 2).equals("_")) {
                result.append(name.substring(0, 1).toUpperCase());
            } else {
                result.append(name.substring(0, 1).toLowerCase());
            }
            for (int i = 1; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                if (s.equals("_")) {
                    nextIsUpper = true;
                } else {
                    if (nextIsUpper) {
                        result.append(s.toUpperCase());
                        nextIsUpper = false;
                    } else {
                        result.append(s.toLowerCase());
                    }
                }
            }
        }
        return result.toString();
    }

    public static String convertCamelCaseToConstant(String str) {
        String[] parts = str.split("(?=\\p{Upper})");
        String finalStr = "";
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
            if (parts[i].length() > 0) {
                finalStr += parts[i].toUpperCase();
                if (i < parts.length - 1) {
                    finalStr += "_";
                }
            }
        }
        return finalStr;
    }

    public static String formatMessage(String text, Object... replacements) {
        return formatMessage(text, Locale.getDefault(), replacements);
    }

    public static String formatMessage(String text, Locale locale, Object... replacements) {
        if (replacements == null || replacements.length == 0) {
            return text;
        }
        if (replacements.length == 1 && replacements[0].getClass().isArray()) {
            replacements = (Object[]) replacements[1];
        }
        MessageFormat messageFormat = new MessageFormat(text, locale);
        return messageFormat.format(replacements);
    }

    public static boolean startsWith(String haystack, String prefix) {
        return startsWith(haystack, prefix, false);
    }

    public static boolean startsWith(String haystack, String prefix, Boolean trimLeadingSpaces) {
        if (trimLeadingSpaces) {
            haystack = trimStart(haystack);
        }
        return haystack.startsWith(prefix);
    }

    public static String trimStart(String str) {
        int counter = 0;
        while (str.indexOf(" ", counter) == counter) {
            counter++;
        }
        return str.substring(counter);
    }

    public static String trimBothEnds(String str) {
        if(str != null){
            return trimStart(str).trim();
        }
        return null;
    }

    public static boolean endsWith(String haystack, String suffix) {
        return haystack.endsWith(suffix);
    }

    public static Boolean endsWith(String haystack, Collection<String> suffixes) {
        for (String suffix : suffixes) {
            if (haystack.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }

    public static Collection<String> asCollection(String str, String regexExp) {
        return asCollection(str.split(regexExp));
    }

    public static Collection<String> asCollection(String str, String regexExp, boolean trimValues) {
        return asCollection(str.split(regexExp), trimValues);
    }

    public static Collection<String> asCollection(String[] str) {
        return asCollection(str, true);
    }

    public static Collection<String> asCollection(String[] str, boolean trimValues) {
        return asCollection(str, trimValues, false);
    }

    public static Collection<String> asCollection(String[] str, boolean trimValues, boolean useIntern) {
        ArrayList<String> list = new ArrayList<String>(str.length);
        for (int i = 0; i < str.length; i++) {
            if (str[i] != null) {
                String finalStr = null;
                if (trimValues) {
                    finalStr = new String(str[i].trim()); // construct new string as .trim only changes the .length val
                } else {
                    finalStr = str[i];
                }
                if (useIntern) {
                    finalStr = finalStr.intern();
                }
                list.add(i, finalStr);
            } else {
                list.add(i, null);
            }
        }
        return list;
    }

    public static String replaceTokens(String text, Map replacements) {
        if (replacements == null || replacements.isEmpty()) {
            return text;
        }
        Pattern pattern = Pattern.compile("\\{(.+?)\\}");
        Matcher matcher = pattern.matcher(text);
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (matcher.find()) {
            Object replacementObj = replacements.get(matcher.group(1));
            String replacement = (replacementObj == null) ? null : replacementObj.toString();
            builder.append(text.substring(i, matcher.start()));
            if (replacement == null) {
                builder.append(matcher.group(0));
            } else {
                builder.append(replacement);
            }
            i = matcher.end();
        }
        builder.append(text.substring(i, text.length()));
        return builder.toString();
    }

    public static int indexOf(final String[] haystack, final String needle) {
        String item;
        for (int i = haystack.length - 1; i > -1; i--) {
            item = haystack[i];
            if (item == needle || item.hashCode() == needle.hashCode() || item.equals(needle)) {
                return i;
            }
        }
        return -1;
    }

    public static String splitCamelCase(String string) {
        return splitCamelCase(string, " ");
    }

    public static String splitCamelCase(String string, String delimiter) {
        return string.replaceAll(String.format("%s|%s|%s",
                "(?<=[A-Z])(?=[A-Z][a-z])",
                "(?<=[^A-Z])(?=[A-Z])",
                "(?<=[A-Za-z])(?=[^A-Za-z])"), delimiter);
    }

    public static <T> T[] splitAndParse(final String string, String delimiter, final Class<T> dataType) {
        Assert.notNull(string);
        Assert.notNull(delimiter);
        Assert.notNull(dataType);

        String[] parts = split(string, delimiter);
        Object[] finalResult = null;

        if (dataType.equals(Long.class)) {
            Long[] result = new Long[parts.length];
            for (int i = 0; i < parts.length; i++) {
                result[i] = Long.parseLong(parts[i]);
            }
            finalResult = result;
        } else if (dataType.equals(Integer.class)) {
            Long[] result = new Long[parts.length];
            for (int i = 0; i < parts.length; i++) {
                result[i] = Long.parseLong(parts[i]);
            }
            finalResult = result;
        } else {
            throw new UnsupportedOperationException("unknown dataType:=" + dataType + " we only support Long and Integer");
        }

        return (T[]) finalResult;
    }

      /**
     * Get random UUID
     * @param escape - true or false
     * a) true: Escapes the '-' in the generated UUID sequence. UUID will have 32 characters.
     * b) false: The un-escaped sequence will have 36 characters.
     * @return - UUID
     */
    public static String getUUID(boolean escape){
        if(escape)
            return replace(UUID.randomUUID().toString(), "-","");
        else
            return UUID.randomUUID().toString();
    }
	
	// Extracted from Google GWT library - SafeHtmlUtils.java
    // http://code.google.com/p/google-web-toolkit/source/browse/trunk/user/src/com/google/gwt/safehtml/shared/SafeHtmlUtils.java?r=9979
    // Method used to avoid the double escaping of special characters if the string is already escaped
    public static String safeHtmlEscape(String text) {
        StringBuilder escaped = new StringBuilder();
        final String HTML_ENTITY_REGEX = "[a-z]+|#[0-9]+|#x[0-9a-fA-F]+";
        boolean firstSegment = true;
        for (String segment : text.split("&", -1)) {
            if (firstSegment) {
                /*
                * The first segment is never part of an entity reference, so we always
                * escape it.
                * Note that if the input starts with an ampersand, we will get an empty
                * segment before that.
                */
                firstSegment = false;
                escaped.append(StringEscapeUtils.escapeHtml(segment));
                continue;
            }

            int entityEnd = segment.indexOf(';');
            if (entityEnd > 0
                    && segment.substring(0, entityEnd).matches(HTML_ENTITY_REGEX)) {
                // Append the entity without escaping.
                escaped.append("&").append(segment.substring(0, entityEnd + 1));

                // Append the rest of the segment, escaped.
                escaped.append(StringEscapeUtils.escapeHtml(segment.substring(entityEnd + 1)));
            } else {
                // The segment did not start with an entity reference, so escape the
                // whole segment.
                escaped.append("&amp;").append(StringEscapeUtils.escapeHtml(segment));
            }
        }

        return escaped.toString();
    }
}
