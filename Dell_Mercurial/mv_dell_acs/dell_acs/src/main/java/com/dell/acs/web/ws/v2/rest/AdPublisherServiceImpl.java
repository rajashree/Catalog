package com.dell.acs.web.ws.v2.rest;

import com.dell.acs.managers.AdPublisherManager;
import com.dell.acs.persistence.domain.AdPublisher;
import com.dell.acs.web.ws.v1.beans.WSAdPublisher;
import com.dell.acs.web.ws.v1.beans.WSBeanUtil;
import com.dell.acs.web.ws.v2.AdPublisherService;
import com.sourcen.core.ObjectNotFoundException;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.web.ws.beans.WSProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebService;
import javax.xml.ws.WebServiceException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 @author Ashish
 @author $LastChangedBy: Ashish $
 @version $Revision: 1595 $, $Date:: 7/31/12 11:20 AM#$ */
@Component(value = "AdPublisherServiceV2")
@WebService
@RequestMapping("/api/v2/rest/AdPublisherService")
public class AdPublisherServiceImpl extends WebServiceImpl implements AdPublisherService {

    public static final Logger logger = LoggerFactory.getLogger(AdPublisherServiceImpl.class);

    /**
     {@inheritDoc}
     */
    @Override
    @RequestMapping("getPublisherByWebsite")
    public AdPublisher getPublisherByWebsite(final String facebookAPIKey) {
        AdPublisher adPublisher = adPublisherManager.getAdPublisher(facebookAPIKey);
        return adPublisher;
    }

    /**
     {@inheritDoc}
     */
    @Override
    @RequestMapping("getAdTags")
    public Map<String, String> getAdTags() {

        String imageLink95090 = "<img src=\'/resources/adtags/dell-laptop-studio-xps-13-design.jpg\' "
                + "width=\'950\' height=\'90\'/>";
        String imageLink72890 = "<img src=\'/resources/adtags/"
                + "Dell-Studio-17-17.3-Inch-Multi-Touch-Laptop-02.jpg\' width=\'728\' height=\'90\'/>";
        String imageLinkBottom300250 = "<img src=\'/resources/adtags/dell-inspiron-duo-tablet-laptop_1.jpg\' "
                + "width=\'300\' height=\'250\'/>";
        String imageLink300250 = "<img src=\'/resources/adtags/zdnet-dell-inspirion-one-19.jpg\'  "
                + "width=\'300\' height=\'250\'/>";
        String imageLink160600 = "<img src=\'/resources/adtags/dell-studio-hybrid-mini-desktop.jpg\'  "
                + "width=\'160\' height=\'600\'/>";

        //Map for different image key value
        Map<String, String> imageMap = new HashMap<String, String>();

        imageMap.put("b_950_90", imageLink95090);
        imageMap.put("b_728_90", imageLink72890);
        imageMap.put("b_300_250", imageLinkBottom300250);
        imageMap.put("bfs_300_250", imageLink300250);
        imageMap.put("b_160_600", imageLink160600);

        return imageMap;
    }

    /**
     {@inheritDoc}
     */
    @Override
    @RequestMapping("getFacebookAPIKey")
    public String getFacebookAPIKey(@RequestParam(required = true)final String adPublisherWebsite) {
        String apiKey = null;
        if (!isAnonymous()) {
            AdPublisher adPublisher = adPublisherManager.getAdPublisher(getUser(), adPublisherWebsite);
            if (adPublisher != null) {
                apiKey = adPublisher.getApiKey();
            }
        }else{
            throw new WebServiceException(" Service is not available for anonymous user.");
        }

        if(StringUtils.isEmpty(apiKey)) {
            throw new WebServiceException(" API Key not defined for the Publisher Website - "+adPublisherWebsite);
        }
        return apiKey;

    }

    /**
     reference of AdPublisherManager
     */
    @Autowired
    private AdPublisherManager adPublisherManager;

    /*
       Setter
     */
    public void setAdPublisherManager(final AdPublisherManager adPublisherManager) {
        this.adPublisherManager = adPublisherManager;
    }
}
