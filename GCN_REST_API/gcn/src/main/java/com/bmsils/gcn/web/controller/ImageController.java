/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.web.controller;

import com.bmsils.gcn.managers.ImageManager;
import com.bmsils.gcn.persistence.domain.Image;
import com.bmsils.gcn.utils.EntityToBeanMapper;
import com.bmsils.gcn.utils.ValidationUtils;
import com.bmsils.gcn.web.beans.ImageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/24/12
 * Time: 6:35 PM
 * ImageController contains REST API calls related to Image
 */
@Controller
@RequestMapping("/api/v1/rest")
public class ImageController extends BaseController{

    /**
     * Method used to delete an existing image
     * @param imageId
     * @return MV object with "deleteImage" operation's success or failure response
     */
    @RequestMapping("deleteImage")
    public ModelAndView deleteImage(@RequestParam(required=true) Long imageId) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        try{
            imageManager.deleteImage(imageId);
            modelMap.put("status", "success");
            modelMap.put("message",getMessageText("image.action.deleteImage.success",null));
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message",getMessageText("image.action.deleteImage.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to obtain all the images associated with the user
     * @param gcn
     * @return MV object with list of Image objects
     */
    @RequestMapping("getImages")
    public ModelAndView getImages(@RequestParam(required=true) String gcn) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!ValidationUtils.isValidateGCN(gcn)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        try{
            List<Image> images = imageManager.getImages(gcn);
            List<ImageBean> imageList = new ArrayList<ImageBean>();
            for(Image image: images){
                imageList.add(EntityToBeanMapper.getInstance().doMapping(image,ImageBean.class));
            }
            modelMap.put("data", imageList);
            modelMap.put("status", "success");
            modelMap.put("message",getMessageText("image.action.getImages.success",null));
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message",getMessageText("image.action.getImages.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }


    /**
     * Dependency Injection of various Spring beans follows
     */
    @Autowired
    ImageManager imageManager;

    public ImageManager getImageManager() {
        return imageManager;
    }

    public void setImageManager(ImageManager imageManager) {
        this.imageManager = imageManager;
    }
}
