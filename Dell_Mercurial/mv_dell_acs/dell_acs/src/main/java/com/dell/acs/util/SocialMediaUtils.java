/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.util;

import com.restfb.exception.FacebookGraphException;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * A Util class for all the Social Media stuff.
 *
 * Initial version to fetch data from Facebook using Graph APIs -
 *          https://developers.facebook.com/tools/explorer/?method=GET&path=dell
 * @author Vivek
 * @version 1.0 , 4 Sep 2012
 */

public class SocialMediaUtils {

    private static final Logger logger = Logger.getLogger(SocialMediaUtils.class);

    private static final String FB_GRAPH_END_POINT = "https://graph.facebook.com/";
    private static HttpClient client = new HttpClient();

    /**
     * Returns the facebook ID for a valid Facebook Page or Facebook UserName
     * Ex: 1) Facebook Page  https://www.facebook.com/dell
     *     2) Facebook Page  https://www.facebook.com/techcrunch
     *     3) Facebook User  https://www.facebook.com/zuck
     * @param name
     * @return
     */
    public static String getFacebookID(String name){
        logger.debug("  FB Name   " + name);
        String response = null;
        HttpMethod method = new GetMethod(FB_GRAPH_END_POINT+name.trim());
        String fbID = null;
        try {

            int httpStatus = client.executeMethod(method);
            if(httpStatus == HttpStatus.SC_OK){
                response =  new String(method.getResponseBody());
                //logger.debug(" Response  :::   " + response);
                JSONObject jsonObject = JSONObject.fromObject(response);
                if(jsonObject != null)
                {
                    fbID = (String)jsonObject.get("id");
                }
            } else {
                //https://jira.marketvine.com/browse/PAX-362 - Handling invalid Facebook Page Names.
                JSONObject jsonObject = JSONObject.fromObject(method.getResponseBodyAsString()).getJSONObject("error");
                logger.error("FacebookGraphException  :::  "+jsonObject.toString());
                throw new FacebookGraphException((String)jsonObject.get("type"),(String)jsonObject.get("message"));
            }


        } catch (IOException e) {
            logger.error(" Exception occurred while using FB Graph API " + e.getMessage());
        }
        return fbID;
    }

}
