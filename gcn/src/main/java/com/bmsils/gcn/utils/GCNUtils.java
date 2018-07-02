/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.utils;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/11/12
 * Time: 3:03 PM
 * GCNUtils is a utility class - extension of WebUtils
 */

public class GCNUtils extends org.springframework.web.util.WebUtils {

    public static final Md5PasswordEncoder md5Encoder = new Md5PasswordEncoder();
    public static final String encoderSalt = "gcn";

    /**
     * Utility method to check for null or empty string
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        if (null == str || "".equals(str) || "null".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * Utility method to check for null or 0 Long value
     * @param l
     * @return
     */
    public static boolean isNullOrEmpty(Long l) {
        if (null == l || l.longValue() == 0L) {
            return true;
        }
        return false;
    }
    
}
