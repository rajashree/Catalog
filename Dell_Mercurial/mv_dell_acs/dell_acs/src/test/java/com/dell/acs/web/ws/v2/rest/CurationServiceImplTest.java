/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v2.rest;

import com.dell.acs.auth.AuthUtil;
import com.dell.acs.web.ws.ServiceTest;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author Samee K.S
 * @author : sameeks $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */

public class CurationServiceImplTest extends ServiceTest {

    private static Logger logger = Logger.getLogger(CurationServiceImplTest.class);

    private static final String baseURL = "http://127.0.0.1:9090/dell_acs/api/v2/rest/CurationService/";

    private static final String accessKey = "f6deda9b4223487dbb834b291cd0f09e";
    private static final String secretKey = "6ccb2656e80f4e809e5850370dd783f2cfbf1ce851224ed781518842d0ae58d7";


    @Test
    public void testCreateCurationService() {
        String requestURL = baseURL + "createCuration.json?name=Target_World1&desc=Target_WORLD_DESC1&retailerSiteName=target.com";
        logger.info(requestURL);
        PostMethod method = new PostMethod(requestURL);
        BufferedReader br = null;
        HttpClient client = new HttpClient();
        method.addRequestHeader("Authorization", generateAuthHeader(requestURL));
        try {
            int returnCode = 0;
            returnCode = client.executeMethod(method);
            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                logger.info("Failed to invoke the service");
            } else {

                if (returnCode == HttpStatus.SC_OK){
                    br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                    String readLine = null;
                    String temp = null;
                    while ((temp = br.readLine()) != null) {
                        readLine = temp;
                    }
                    logger.info(readLine);
                    JSONObject jsonObject = JSONObject.fromObject(readLine);
                    logger.info(jsonObject.get("success"));
                    logger.info(jsonObject.get("data"));
                }else{
                    logger.info("Failed to invoke the service - " + returnCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testGetCategoriesService() {
        String requestURL = baseURL + "getCategories.json?curationID=2&hiearchical=true";
        logger.info(requestURL);
        PostMethod method = new PostMethod(requestURL);
        BufferedReader br = null;
        HttpClient client = new HttpClient();
        method.addRequestHeader("Authorization", generateAuthHeader(requestURL));
        try {
            int returnCode = 0;
            returnCode = client.executeMethod(method);
            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                logger.info("Failed to invoke the service");
            } else {

                if (returnCode == HttpStatus.SC_OK){
                    br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                    String readLine = null;
                    String temp = null;
                    while ((temp = br.readLine()) != null) {
                        readLine = temp;
                    }
                    logger.info(readLine);
                    JSONObject jsonObject = JSONObject.fromObject(readLine);
                    logger.info(jsonObject.get("success"));
                    logger.info(jsonObject.get("data"));
                }else{
                    logger.info("Failed to invoke the service - " + returnCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String generateAuthHeader(String url ){
        String signedData = AuthUtil.generateHMAC(url, secretKey);
        return(accessKey + ":" + signedData);
    }


    public void testCreateCurationCategory() {
        Long curationID = -1L;
        String categoryName = "";
        Integer position = 1;

        PostMethod method = new PostMethod(baseURL + "/getCampaignById.json?campaignId=23&trackerID=246902");
        BufferedReader br = null;
        try {
            if (super.testConnection(method)) {
                br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                String readLine = null;
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    readLine = temp;
                }
                JSONObject jsonObject = JSONObject.fromObject(readLine);
                logger.info(jsonObject.get("success"));
                logger.info(jsonObject.get("data"));
            }
        } catch (Exception ex) {

        }

    }

    void printResult(String testResult) {
        logger.info("Result :: \n" + testResult);
    }
}
