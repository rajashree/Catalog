/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.controller.api.v1;

import com.dell.acs.managers.AdPublisherManager;
import com.dell.acs.persistence.domain.AdPublisher;
import com.dell.acs.persistence.domain.User;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2247 $, $Date:: 2012-05-10 14:01:03#$
 */
@Controller
@RequestMapping("/api/v1/UserService")
@Deprecated
public class UserService extends BaseAPIController {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    /**
     * It will create the response in JSOn format for remote request.
     *
     * @param adPublisherWebsite , this will store the website coming from remote requset.
     *
     * @return will return the ResponseEntity.
     */
    @RequestMapping(value = "getPublisherDetails.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<String> getPublisherDetails(@RequestParam(value = "adPublisherWebsite", required = true) String adPublisherWebsite) {

        LOG.info("Website from remote request: " + adPublisherWebsite);
        User user = getUser();
        String remoteRespone = null;
        HttpHeaders responseHeaders = new HttpHeaders();
        JSONObject result = new JSONObject();
        if (user != null && !isAnonymous()) {
            AdPublisher adPublisher = adPublisherManager.getAdPublisher(user, adPublisherWebsite);
            if (adPublisher != null) {
                result.accumulate("publisher_id", user.getId());
                result.accumulate("apiKey", adPublisher.getApiKey());
                result.accumulate("status", "success");
                remoteRespone = adPublisher.getApiKey();
                LOG.info("adPublisher.getApiKey()");
            } else {
                result.accumulate("error", "Unable to find the publisher");
                remoteRespone = "error, Unable to find the publisher";
            }
        } else {
            result.accumulate("error", "Unable to authenticate user");
        }
        responseHeaders.setContentType(MediaType.TEXT_PLAIN);
        /* return remoteRespone;*/
        /*return getJSONResult(result.toString());*/
        return new ResponseEntity<String>(remoteRespone, responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "getPlacementJson.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<String> getPlacementJSON(@RequestParam(value = "adPublisherWebsite", required = true) String adPublisherWebsite) {

        LOG.error("Website from remote request: " + adPublisherWebsite);
        User user = getUser();
        String remoteRespone = null;
        HttpHeaders responseHeaders = new HttpHeaders();
        JSONObject result = new JSONObject();
        if (user != null && !isAnonymous()) {
            AdPublisher adPublisher = adPublisherManager.getAdPublisher(user, adPublisherWebsite);
            if (adPublisher != null) {
                /*result.accumulate("JSON String", adPublisher.getWordPressJSON());*/
                remoteRespone = adPublisher.getProperties().getProperty("wp_ad_json");
                LOG.info("JSON String: "+adPublisher.getProperties().getProperty("wp_ad_json"));
            } else {
                result.accumulate("error", "Unable to find the publisher");
                remoteRespone = "error, Unable to find the publisher";
            }
        } else {
            result.accumulate("error", "Unable to authenticate user");
        }
        responseHeaders.setContentType(MediaType.TEXT_PLAIN);
        /* return remoteRespone;*/
        /*return getJSONResult(result.toString());*/
        return new ResponseEntity<String>(remoteRespone, responseHeaders, HttpStatus.OK);
    }

    /**
     * reference for AdPublisherManager.
     */
    @Autowired
    AdPublisherManager adPublisherManager;

    /**
     * Initialize the adPublisherManager instance.
     *
     * @param adPublisherManager , store the informatio injected from bean
     */
    public void setAdPublisherManager(AdPublisherManager adPublisherManager) {
        this.adPublisherManager = adPublisherManager;
    }
}

