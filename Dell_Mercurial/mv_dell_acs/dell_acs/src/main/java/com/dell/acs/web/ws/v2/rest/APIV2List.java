package com.dell.acs.web.ws.v2.rest;

import com.sourcen.core.web.ws.WebService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Lists all the available Web Services for Version 2 (/api/v2/rest/**)
 *
 * @author Vivek Kondur
 * @version 1.0
 */
@Controller
public class APIV2List extends com.sourcen.core.web.ws.APIList {

    private static final Collection<Class<? extends WebService>> classes = new LinkedList<Class<? extends WebService>>();

    public APIV2List() {
        super();
        classes.add(AdPublisherServiceImpl.class);
        classes.add(CurationServiceImpl.class);
        classes.add(CurationSourceServiceImpl.class);
        classes.add(CampaignServiceImpl.class);
        classes.add(LibraryServiceImpl.class);
        classes.add(ProductServiceImpl.class);
        classes.add(RetailerServiceImpl.class);
        classes.add(UserServiceImpl.class);
        reloadAPIList(classes);
    }

    @RequestMapping("/api/v2/rest/list")
    public ModelAndView get_list() {
        return super.get_list();
    }

    @RequestMapping("/api/v2/rest/lib")
    public ModelAndView getJsLib(@RequestParam(value = "type", required = false) String type) {

        if(type == null || type.isEmpty()){
            type = "javascript";
        } else{
            type = type.toLowerCase();
        }
        ModelAndView mv = super.get_list();
        if(type.equals("javascript") || type.equals("js") || type.equals("json")){
            mv.setViewName("/api/v2/rest/lib-js");
        } else if(type.equals("php")){
            mv.setViewName("/api/v2/rest/lib-php");
        }else{
            mv.setViewName("/api/v2/rest/lib-error");
        }
        return mv;
    }

}
