/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.utils;

import com.bmsils.gcn.beans.Notification;
import com.bmsils.gcn.managers.impl.ConfigurationManagerImpl;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.ApnsServiceBuilder;
import com.notnoop.apns.PayloadBuilder;
import org.springframework.core.io.Resource;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/24/12
 * Time: 1:17 PM
 * PushNotificationUtil is an util class containing utility functions related to Android, Blackberry and iPhone Push Notifications
 */
public class PushNotificationUtil {

    ConfigurationManagerImpl configurationManager;

    public ConfigurationManagerImpl getConfigurationManager() {
        return ConfigurationManagerImpl.getInstance();
    }

    private final static String AUTH = "authentication";
    private static final String UPDATE_CLIENT_AUTH = "Update-Client-Auth";
    public static final String PARAM_REGISTRATION_ID = "registration_id";
    public static final String PARAM_DELAY_WHILE_IDLE = "delay_while_idle";
    public static final String PARAM_COLLAPSE_KEY = "collapse_key";
    private static final String UTF8 = "UTF-8";
    private static ApnsService instance = null;


    /**
     * method to obtain the Android Authentication token from the C2DM Service using the registered account
     * @return  C2DM Authentication Token
     */
    public String getAndroidAuthenticationToken(){
        String auth_key = null;

        String email = getConfigurationManager().getProperty("pushNotification.c2dm.email");
        String password = getConfigurationManager().getProperty("pushNotification.c2dm.password");
        String source = getConfigurationManager().getProperty("pushNotification.c2dm.source");

        StringBuilder builder = new StringBuilder();
        builder.append("Email=").append(email);
        builder.append("&Passwd=").append(password);
        builder.append("&accountType=GOOGLE");
        builder.append("&source=").append(source);
        builder.append("&service=ac2dm");

        try{
            byte[] data = builder.toString().getBytes();
            URL url = null;
            url = new URL("https://www.google.com/accounts/ClientLogin");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            con.setRequestProperty("Content-Length", Integer.toString(data.length));
            OutputStream output = con.getOutputStream();
            output.write(data);
            output.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Auth=")) {
                    auth_key = line.substring(5);
                }
            }
        }catch(MalformedURLException e) {
            e.printStackTrace();
            return null;
        }catch (ProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return auth_key;
    }

    /**
     * method to send the Android Push Notification
     * @param auth_token
     * @param registrationId
     * @param message
     * @return responseCode for send Push Notification job
     * @throws IOException
     */
    public int sendAndroidNotification(String auth_token, String registrationId,
                                       String message) throws IOException {

        StringBuilder postDataBuilder = new StringBuilder();
        postDataBuilder.append(PARAM_REGISTRATION_ID).append("=")
                .append(registrationId);
        postDataBuilder.append("&").append(PARAM_COLLAPSE_KEY).append("=")
                .append("0");
        postDataBuilder.append("&").append("data.payload").append("=")
                .append(URLEncoder.encode(message, UTF8));

        byte[] postData = postDataBuilder.toString().getBytes(UTF8);

        URL url = new URL("https://android.clients.google.com/c2dm/send");
        HttpsURLConnection
                .setDefaultHostnameVerifier(new CustomizedHostnameVerifier());
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded;charset=UTF-8");
        conn.setRequestProperty("Content-Length",
                Integer.toString(postData.length));
        conn.setRequestProperty("Authorization", "GoogleLogin auth="
                + auth_token);

        OutputStream out = conn.getOutputStream();
        out.write(postData);
        out.close();

        int responseCode = conn.getResponseCode();
        System.out.println(":::::::::::::::responseCode::::"+responseCode);
        return responseCode;
    }

    private static class CustomizedHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /**
     * method to obtain the APNS service, requires APNS p12 file
     * @return APNS service object that will be used to send Push Notifications
     * @throws IOException
     */
    public ApnsService createAPNSService() throws IOException {
        ApnsService service = null;
        ApnsServiceBuilder serviceBuilder = null;
         if (Boolean.valueOf(getConfigurationManager().getProperty("app.devMode"))) {
             serviceBuilder = APNS.newService()
                     .withCert(PushNotificationUtil.class.getResourceAsStream(getConfigurationManager().getProperty("pushNotification.iphone.sandbox.P12.location")),getConfigurationManager().getProperty("pushNotification.iphone.sandbox.P12.password"));
             serviceBuilder.withSandboxDestination();
         } else {
             serviceBuilder = APNS.newService()
                     .withCert(PushNotificationUtil.class.getResourceAsStream(getConfigurationManager().getProperty("pushNotification.iphone.production.P12.location")),getConfigurationManager().getProperty("pushNotification.iphone.production.P12.password"));
             serviceBuilder.withProductionDestination();
        }
        service = serviceBuilder.build();
        return service;
    }

    /**
     * method to send iPhone Push Notification
     * @param notification
     * @param service
     * @param message
     */
    public void sendIPhoneNotification(Notification notification,
                                       ApnsService service, String message) {
        PayloadBuilder payloadBuilder = APNS.newPayload();
        payloadBuilder = payloadBuilder.badge(notification.getMessageCount()+notification.getInviteCount());
        payloadBuilder = payloadBuilder.sound("beep.wav");
        payloadBuilder = payloadBuilder.alertBody(message);
        if(notification.getMessageCount() > 0 && notification.getInviteCount() > 0){
            payloadBuilder = payloadBuilder.customField("pnType","both");
        }else if(notification.getMessageCount() > 0 ){
            payloadBuilder = payloadBuilder.customField("pnType","message");
        }else if( notification.getInviteCount() > 0){
            payloadBuilder = payloadBuilder.customField("pnType","invite");
        }


        String payload = payloadBuilder.build();
        service.push(notification.getDeviceToken(), payload);
    }


}


