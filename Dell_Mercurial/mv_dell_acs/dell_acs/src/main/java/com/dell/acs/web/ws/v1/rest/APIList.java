/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.rest;

import com.sourcen.core.web.ws.WebService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2788 $, $Date:: 2012-05-30 12:29:03#$
 */
@Controller
public class APIList extends com.sourcen.core.web.ws.APIList {

    private static final Collection<Class<? extends WebService>> classes = new LinkedList<Class<? extends WebService>>();

    public APIList() {
        super();
        classes.add(AdPublisherServiceImpl.class);
        classes.add(CampaignServiceImpl.class);
        classes.add(FacebookWallShareServiceImpl.class);
        classes.add(MerchantServiceImpl.class);
        classes.add(ProductServiceImpl.class);
        classes.add(RetailerServiceImpl.class);
        reloadAPIList(classes);
    }

    @RequestMapping("/api/v1/rest/list")
    public ModelAndView get_list() {
        reloadAPIList(classes);
        return super.get_list();
    }

    @RequestMapping("/api/v1/rest/lib")
    public ModelAndView getJsLib(@RequestParam(value = "type", required = false) String type) {

        if(type == null || type.isEmpty()){
            type = "javascript";
        } else{
            type = type.toLowerCase();
        }
        ModelAndView mv = super.get_list();
        if(type.equals("javascript") || type.equals("js") || type.equals("json")){
            mv.setViewName("/api/v1/rest/lib-js");
        } else if(type.equals("php")){
            mv.setViewName("/api/v1/rest/lib-php");
        }else{
            mv.setViewName("/api/v1/rest/lib-error");
        }
        return mv;
    }


}

