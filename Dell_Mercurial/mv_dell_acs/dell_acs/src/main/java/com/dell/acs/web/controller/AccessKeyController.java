package com.dell.acs.web.controller;

import com.dell.acs.auth.AuthUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: vivek
 * Date: 7/17/12
 * Time: 12:24 PM
 */
@Controller
public class AccessKeyController extends BaseDellController {

    private static final Logger logger = Logger.getLogger(AccessKeyController.class);


    @RequestMapping(value = "/testAPIKey", method = RequestMethod.GET)
    public ModelAndView verifyAPIKey() {
        logger.info("  REST Endpoint Access Key verification ");
        return new ModelAndView();
    }
    @RequestMapping(value = "/verifyAPIKey", method = RequestMethod.POST)
    public ModelAndView verifyAPIKey(@RequestParam String secretKey,
                                     @RequestParam String method,
                                     @RequestParam String accessKey,
                                     @RequestParam String requestURL, HttpServletRequest request, HttpServletResponse response) {
        logger.info("  REST Endpoint Access Key verification ");
        logger.info("AccessKey          " + accessKey);
        logger.info("SecretKey          " + secretKey);
        logger.info("requestURL          " + requestURL);
        ModelAndView mv = new ModelAndView();
        String signedData = AuthUtil.generateHMAC(requestURL, secretKey);
        logger.info("Singed Data  "+signedData);
        call(method, accessKey, requestURL, mv, signedData);
        return mv;
    }

    @RequestMapping(value = "/generateAuthHeader.json", method = RequestMethod.POST)
    public ModelAndView generateAuthHeader(@RequestParam String secretKey, @RequestParam String accessKey, @RequestParam String requestURL, HttpServletRequest request, HttpServletResponse response) {
        logger.info("AccessKey          " + accessKey);
        logger.info("SecretKey          " + secretKey);
        logger.info("requestURL          " + requestURL);
        ModelAndView mv = new ModelAndView();
        String signedData = AuthUtil.generateHMAC(requestURL, secretKey);
        String authorizationKey = accessKey + ":" + signedData;
        mv.addObject("data", authorizationKey);
        return mv;
    }

    private ModelAndView call(String methodType, String accessKey, String requestURL, ModelAndView mv, String signedData) {
        String wsResponse;
        HttpClient client = new HttpClient();
        HttpMethod method = null;
        logger.info("Requested method type - " + methodType);
        if(methodType.equalsIgnoreCase("POST")){
            method = new PostMethod(requestURL);
        }else if(methodType.equalsIgnoreCase("GET")){
            method = new GetMethod(requestURL);
        }
        method.addRequestHeader("Authorization", accessKey + ":" + signedData);

        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);

            if (statusCode == HttpStatus.SC_NOT_FOUND) {
                wsResponse = "Please verify the REST endpoint url.";
                //wsResponse =  method.getStatusLine().getReasonPhrase();
                logger.info("Method failed: " +method.getStatusLine().toString());
                mv.addObject("Error", wsResponse);
            }else if (statusCode != HttpStatus.SC_OK) {
                wsResponse = method.getResponseHeader("WWW-Authenticate").getValue();
                //wsResponse =  method.getStatusLine().getReasonPhrase();
                logger.info("Method failed: " +method.getStatusLine().toString());
                mv.addObject("Error", wsResponse);

            } else {

                // Read the response body.
                byte[] responseBody = method.getResponseBody();
                // Deal with the response.
                // Use caution: ensure correct character encoding and is not binary data
                mv.addObject("Success", new String(responseBody));
            }
            mv.addObject("RequestURL", requestURL);
            mv.addObject("HttpStatus", statusCode);
            mv.addObject("SignedData",signedData);


        } catch (HttpException e) {
            logger.error("Fatal protocol violation: " + e.getMessage());
        } catch (IOException e) {
            logger.error("Fatal transport error: " + e.getMessage());
        } finally {
            // Release the connection.
            method.releaseConnection();
        }

        return mv;
    }

}