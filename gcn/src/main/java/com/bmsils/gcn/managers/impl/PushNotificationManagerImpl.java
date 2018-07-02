/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.managers.impl;

import com.bmsils.gcn.ApplicationConstants;
import com.bmsils.gcn.beans.Notification;
import com.bmsils.gcn.managers.PushNotificationManager;
import com.bmsils.gcn.utils.GCNUtils;
import com.bmsils.gcn.utils.PushNotificationUtil;
import com.notnoop.apns.ApnsService;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/24/12
 * Time: 12:30 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class PushNotificationManagerImpl implements PushNotificationManager{



    
    public void pushNotifications(List<Notification> notifications) {
        PushNotificationUtil pushNotificationUtil = new PushNotificationUtil();
        String androidAuthToken = pushNotificationUtil.getAndroidAuthenticationToken();
        ApnsService service = null;
        try {
            service = pushNotificationUtil.createAPNSService();
        } catch (IOException e) {
            e.printStackTrace();

        }
        String payload="";
        String[] arr;
        for(Notification notification : notifications){
            if(notification.getInviteCount() > 0  && notification.getMessageCount() >0 ){
                arr = new String[]{String.valueOf(notification.getMessageCount()), String.valueOf(notification.getInviteCount())};
                payload = ((ReloadableResourceBundleMessageSource)applicationContext.getBean("i18nService")).getMessage("pn.action.pushNotifications.payload.both", arr, Locale.getDefault());
            }else if(notification.getInviteCount() >0){
                arr = new String[]{String.valueOf(notification.getInviteCount())};
                payload = ((ReloadableResourceBundleMessageSource)applicationContext.getBean("i18nService")).getMessage("pn.action.pushNotifications.payload.invites", arr, Locale.getDefault());
            }else if(notification.getMessageCount() >0){
                arr = new String[]{String.valueOf(notification.getMessageCount())};
                payload = ((ReloadableResourceBundleMessageSource)applicationContext.getBean("i18nService")).getMessage("pn.action.pushNotifications.payload.messages", arr, Locale.getDefault());
            }

            switch(ApplicationConstants.DeviceType.getByValue(notification.getDeviceType())){
                case ANDROID:
                    if(!GCNUtils.isNullOrEmpty(androidAuthToken)){
                        try {
                            pushNotificationUtil.sendAndroidNotification(androidAuthToken, notification.getDeviceToken(),payload);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case IPHONE:
                    if(service != null)
                        pushNotificationUtil.sendIPhoneNotification(notification,service,payload);
                    break;
                case BLACKBERRY:
                    break;
            }


        }

    }


}
