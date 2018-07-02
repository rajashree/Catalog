/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.web.controller;

import com.bmsils.gcn.managers.UserManager;
import com.bmsils.gcn.web.beans.RandomContainer;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/3/12
 * Time: 6:28 PM
 * GenericController contains REST API calls which are generic
 */
@Controller
@RequestMapping("/api/v1/rest")

public class GenericController extends BaseController{

    /**
     * Method used to obtain the available GCNs
     * @param key
     * @return MV object with list of available GCNs
     */
    @RequestMapping(value = "generateAvailableGCNs", method = RequestMethod.GET)
    public ModelAndView generateAvailableGCNs(@RequestParam(required=true) String key) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Set listOfIds = new HashSet(10);
        if (key != null && key.length() == 3 && !containsSpecial(key)) {
            listOfIds = getString(0, 4, key, 10);
            modelMap.put("status", "success");
            modelMap.put("data", listOfIds);
            modelMap.put("message", getMessageText("generic.action.generateAvailableGCN.success",null));

        }else {
            modelMap.put("status", "failure");
            modelMap.put("message", getMessageText("generic.action.generateAvailableGCN.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }


    private boolean containsSpecial(String inputStr) {

        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(inputStr);
        boolean b = m.find();
        return b;
    }

    private Set<String> getString(int n, int length, String inputStr,int resultCount) {
        String str1 = "0123456789abcdefghijklmnopqrstuvwxyz";
        List<String> registeredGCNs = userManager.getRegisteredGCNs();
        HashSet<String> result = new HashSet<String>();
        StringBuilder sb = new StringBuilder(length);
        Random r = new Random();

        for (int i = 0; result.size() < 10; i++) {
            for (int j = 0; j < length; j++) {
                sb.append(str1.charAt(r.nextInt(str1.length())));
            }
            sb.insert(r.nextInt(length), inputStr);
            if(registeredGCNs != null){
                if(!registeredGCNs.contains(sb.toString())){
                    result.add(sb.toString());
                }
            }else{
                result.add(sb.toString());
            }
            sb.delete(0, 7);
        }
        return result;
    }

    /**
     * Dependency Injection of various Spring beans follows
     */
    @Autowired
    private UserManager userManager;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }




}
