/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.controller.webservice;

import com.dell.acs.managers.RetailerManager;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.web.ws.v1.beans.WSRetailer;
import com.dell.acs.web.ws.v1.beans.WSRetailerSite;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Chethan
 * Date: 2/22/12
 * Time: 5:39 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@Deprecated
public class AdvertiserController {
    /**
     * Autowired RetailerManager
     */
    @Autowired
    private RetailerManager retailerManager;

    /**
     * Controller to get Advertiser List as XML.
     *
     * @return Advertiser XML Output
     */
    @RequestMapping(value = "/advertiser.do", method = RequestMethod.GET)
    public ResponseEntity<String> getProductRecommendations() {
        HttpHeaders responseHeaders = new HttpHeaders();
        //String result = contentServerService.getRetailers();

        List<Retailer> retailerlist = retailerManager.getRetailers();
        XStream xStream = new XStream(new DomDriver());

        xStream.alias("Advertisers", List.class);
        xStream.alias("Advertiser", WSRetailer.class);
        xStream.alias("Site", WSRetailerSite.class);
        xStream.aliasField("Sites", WSRetailer.class, "RetailerSites");
        xStream.omitField(WSRetailer.class, "CreatedById");
        xStream.omitField(WSRetailer.class, "ModifiedById");
        xStream.omitField(WSRetailer.class, "CreatedDate");
        xStream.omitField(WSRetailer.class, "ModifiedDate");
        xStream.omitField(WSRetailerSite.class, "CreatedById");
        xStream.omitField(WSRetailerSite.class, "ModifiedById");
        xStream.omitField(WSRetailerSite.class, "CreatedDate");
        xStream.omitField(WSRetailerSite.class, "ModifiedDate");
        String namespace = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
        String result = namespace.concat(xStream.toXML(retailerlist));

        responseHeaders.setContentType(MediaType.TEXT_XML);
        return new ResponseEntity<String>(result, responseHeaders, HttpStatus.OK);
    }
}
