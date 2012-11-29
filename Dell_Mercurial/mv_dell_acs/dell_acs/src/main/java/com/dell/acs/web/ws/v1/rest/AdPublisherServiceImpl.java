/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.rest;

import com.dell.acs.managers.AdPublisherManager;
import com.dell.acs.persistence.domain.AdPublisher;
import com.dell.acs.web.ws.v1.AdPublisherService;
import com.dell.acs.web.ws.v1.beans.WSAdPublisher;
import com.dell.acs.web.ws.v1.beans.WSBeanUtil;
import com.sourcen.core.ObjectNotFoundException;
import com.sourcen.core.web.ws.beans.WSProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebService;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: sameeks $
 * @version $Revision: 3859 $, $Date:: 2012-07-05 12:24:51#$
 */
@WebService
@RequestMapping("/api/v1/rest/AdPublisherService/")
public class AdPublisherServiceImpl extends WebServiceImpl implements AdPublisherService {


    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping("getPublisherByWebsite")
    public WSAdPublisher getPublisherByWebsite(@RequestParam(required = true) final String apiKey) {
        AdPublisher adPublisher = adPublisherManager.getAdPublisher(apiKey);
        return WSBeanUtil.convert(adPublisher, new WSAdPublisher());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping("getProperties")
    public Collection<WSProperty> getProperties(@RequestParam(required = true) final Long adPublisherId) {
        AdPublisher adPublisher = adPublisherManager.getAdPublisher(adPublisherId);
        return WSBeanUtil.convert(adPublisher, new WSAdPublisher()).getProperties();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping("getAdTags")
    public Map<String, String> getAdTags() {

        String imageLink95090 = "<img src=\'/resources/adtags/dell-laptop-studio-xps-13-design.jpg\' "
                + "width=\'950\' height=\'90\'/>";
        String imageLink72890 = "<img src=\'/resources/adtags/"
                + "Dell-Studio-17-17.3-Inch-Multi-Touch-Laptop-02.jpg\' width=\'728\' height=\'90\'/>";
        String imageLinkb300250 = "<img src=\'/resources/adtags/dell-inspiron-duo-tablet-laptop_1.jpg\' "
                + "width=\'300\' height=\'250\'/>";
        String imageLink300250 = "<img src=\'/resources/adtags/zdnet-dell-inspirion-one-19.jpg\'  "
                + "width=\'300\' height=\'250\'/>";
        String imageLink160600 = "<img src=\'/resources/adtags/dell-studio-hybrid-mini-desktop.jpg\'  "
                + "width=\'160\' height=\'600\'/>";

        //Map for different image key value
        Map<String, String> imageMap = new HashMap<String, String>();

        imageMap.put("b_950_90", imageLink95090);
        imageMap.put("b_728_90", imageLink72890);
        imageMap.put("b_300_250", imageLinkb300250);
        imageMap.put("bfs_300_250", imageLink300250);
        imageMap.put("b_160_600", imageLink160600);

        return imageMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping("getApiKey")
    public String getApiKey(@RequestParam(required = true) final String adPublisherWebsite) {
        if (!isAnonymous()) {
            AdPublisher adPublisher = adPublisherManager.getAdPublisher(getUser(), adPublisherWebsite);
            if (adPublisher != null) {
                return adPublisher.getApiKey();
            }
        }
        throw new ObjectNotFoundException("unable to find website for anonymous user.");
    }

    /**
     * reference of AdPublisherManager
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
