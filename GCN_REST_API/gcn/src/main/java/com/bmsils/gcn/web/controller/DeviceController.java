/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.web.controller;

import com.bmsils.gcn.managers.DeviceManager;
import com.bmsils.gcn.persistence.domain.Device;
import com.bmsils.gcn.persistence.domain.User;
import com.bmsils.gcn.utils.EntityToBeanMapper;
import com.bmsils.gcn.utils.ValidationUtils;
import com.bmsils.gcn.web.beans.DeviceBean;
import com.bmsils.gcn.web.beans.ProfileDataBean;
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
 * Date: 4/23/12
 * Time: 6:46 PM
 * DeviceController contains REST API calls related to Devices
 */
@Controller
@RequestMapping("/api/v1/rest")
public class DeviceController extends BaseController{

    /**
     * Method used to register a Device - used for Push Notification Services
     * @param deviceId
     * @param deviceToken
     * @return MV object with "registerDevice" operation's success or failure response
     */
    @RequestMapping("registerDevice")
    public ModelAndView registerDevice(@RequestParam(required=true) String deviceId, @RequestParam(required=true) String deviceToken) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!ValidationUtils.isValidateUUID(deviceId)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.uuid.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        try{
            deviceManager.registerDevice(deviceId,deviceToken);
            modelMap.put("status", "success");
            modelMap.put("message",getMessageText("pn.action.registerDevice.success",null));
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message",getMessageText("pn.action.registerDevice.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to unregister a Device - used for Push Notification Services
     * @param deviceId
     * @param deviceToken
     * @return MV object with "unregisterDevice" operation's success or failure response
     */
    @RequestMapping("unregisterDevice")
    public ModelAndView unregisterDevice(@RequestParam(required=true) String deviceId, @RequestParam(required=true) String deviceToken) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!ValidationUtils.isValidateUUID(deviceId)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.uuid.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        try{
            deviceManager.unregisterDevice(deviceId,deviceToken);
            modelMap.put("status", "success");
            modelMap.put("message",getMessageText("pn.action.unregisterDevice.success",null));
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message",getMessageText("pn.action.unregisterDevice.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to update the device location. Used by the device while it detects the device has been blocked by the user
     * @param deviceId
     * @param location
     * @return MV object with "updateDeviceLocation" operation's success or failure response
     */
    @RequestMapping("updateDeviceLocation")
    public ModelAndView updateDeviceLocation(@RequestParam(required=true) String deviceId, @RequestParam(required=true) String location) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!ValidationUtils.isValidateUUID(deviceId)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.uuid.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        try{
            deviceManager.updateDeviceLocation(deviceId, location);
            modelMap.put("status", "success");
            modelMap.put("message",getMessageText("device.action.updateDeviceLocation.success",null));
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message",getMessageText("device.action.updateDeviceLocation.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method to obtain the device details associated with the GCN
     * @param gcn
     * @return MV object with list of Device objects
     */
    @RequestMapping("getDeviceDetails")
    public ModelAndView getDeviceDetails(@RequestParam(required=true) String gcn) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!ValidationUtils.isValidateGCN(gcn)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        try{
            List<Device> deviceList = deviceManager.getDeviceDetails(gcn);
            List<DeviceBean> devices = new ArrayList<DeviceBean>();
            for(Device device : deviceList){
                DeviceBean deviceBean = EntityToBeanMapper.getInstance().doMapping(device,DeviceBean.class);
                deviceBean.setGcn(device.getUserGCN().getUserGCN());
                devices.add(deviceBean);
            }
            
            modelMap.put("status", "success");
            modelMap.put("data",devices);
            modelMap.put("message",getMessageText("device.action.getDeviceListDetails.success",null));
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message",getMessageText("device.action.getDeviceListDetails.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }


    /**
     * Method used to blockDevice
     * @param deviceId
     * @return  MV object with "blockDevice" operation's success or failure response
     */
    @RequestMapping("blockDevice")
    public ModelAndView blockDevice(@RequestParam(required=true) String deviceId) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!ValidationUtils.isValidateUUID(deviceId)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.uuid.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        try{
            deviceManager.blockDevice(deviceId);
            modelMap.put("status", "success");
            modelMap.put("message",getMessageText("device.action.blockDevice.success",null));
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message",getMessageText("device.action.blockDevice.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to unblock the device
     * @param deviceId
     * @return MV object with "unBlockDevice" operation's success or failure response
     */
    @RequestMapping("unBlockDevice")
    public ModelAndView unBlockDevice(@RequestParam(required=true) String deviceId) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!ValidationUtils.isValidateUUID(deviceId)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.uuid.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        try{
            deviceManager.unBlockDevice(deviceId);
            modelMap.put("status", "success");
            modelMap.put("message",getMessageText("device.action.blockDevice.success",null));
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message",getMessageText("device.action.blockDevice.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to disassociate device from the account
     * @param gcn
     * @param deviceId
     * @return MV object with "disassociateDeviceFromAccount" operation's success or failure response
     */
    @RequestMapping("disassociateDeviceFromAccount")
    public ModelAndView disassociateDeviceFromAccount(@RequestParam(required=true) String gcn, @RequestParam(required=true) String deviceId) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!ValidationUtils.isValidateUUID(deviceId)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.uuid.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        if(!ValidationUtils.isValidateGCN(gcn)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }

        try{
            deviceManager.disassociateDeviceFromAccount(gcn,deviceId);
            modelMap.put("status", "success");
            modelMap.put("message",getMessageText("device.action.disassociateDeviceFromAccount.success",null));
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message",getMessageText("device.action.disassociateDeviceFromAccount.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }


    /**
     * Dependency Injection of various Spring beans follows
     */
    @Autowired
    DeviceManager deviceManager;

    public DeviceManager getDeviceManager() {
        return deviceManager;
    }

    public void setDeviceManager(DeviceManager deviceManager) {
        this.deviceManager = deviceManager;
    }
}
