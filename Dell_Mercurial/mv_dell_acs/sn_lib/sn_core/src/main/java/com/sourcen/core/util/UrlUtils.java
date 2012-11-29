/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util;

import java.io.UnsupportedEncodingException;
import java.net.*;

/**
 * Created by IntelliJ IDEA.
 * User: Chethan
 * Date: 2/17/12
 * Time: 6:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class UrlUtils {

    public static String decode(String value) throws UnsupportedEncodingException {
        value = URLDecoder.decode(value, "UTF-8");
        return value;
    }
    public static String encode(String value) throws UnsupportedEncodingException{
        value = URLEncoder.encode(value,"UTF-8");
        return value;
    }

    public static URI buildUri(String url) throws URISyntaxException, MalformedURLException {
        URL srcUrl = new URL(url);
        return new URI(srcUrl.getProtocol(), null, srcUrl.getHost(),srcUrl.getPort(),srcUrl.getPath(), srcUrl.getQuery(), null);
    }

}
