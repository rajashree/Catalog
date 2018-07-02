/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/11/12
 * Time: 3:03 PM
 * MessageUtil is an utility class to test Android and iPhone Push Notification as a stand alone program
 */

public class MessageUtil {
    private final static String AUTH = "authentication";
    private static final String UPDATE_CLIENT_AUTH = "Update-Client-Auth";
    public static final String PARAM_REGISTRATION_ID = "registration_id";
    public static final String PARAM_DELAY_WHILE_IDLE = "delay_while_idle";
    public static final String PARAM_COLLAPSE_KEY = "collapse_key";
    private static final String UTF8 = "UTF-8";

    public static int sendMessage(String auth_token, String registrationId,
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
        out.close();
        int responseCode = conn.getResponseCode();
        return responseCode;
    }

    private static class CustomizedHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public static String getToken()
            throws IOException {
        String auth_key="DQAAAMAAAAD53m8uijmoln8PFYmfRN1p-0t01b1MXcuXZPEyzpSuMHv_7MhoNPt1XcGBLPu8ewHWAkKNuwWa728V0V2bvYGOgarHqefCzaCQS_H47uV8BflSV3XFl2lwLBegoMf6ZL9zFTawxX8_Zi9LFsU6FYApdo3gWWvHM4823hcTI7sBzJEUxERUV4uSJUWfQUUcmZV9pp2vv07w89zQNez6x9oqRUOs3hh2frXMlbToTG8GOuK1qs3iEGpk3GtNh50_MhU";
        return auth_key;
    }

    public static String getRegistrationId()
            throws IOException {
        //Device
        //return "APA91bHbnab1JJ9xvmkog0WttpIlhyWaff5NyczCqRYr0_cyIBQBd0H9062HwESRjkawkEK4zyi_uEu6yixsna0etSGoGICtNg-d_jGjcc0n-yj8goDjhRFWt7t5dweVIZahQw6LxwwTquh-aFzeeVFVWt_GHig9nA";
        //Emulator
        return "APA91bEhcT_Lhk8K7q-Iz4C-i2Ba3uzfWTEFL-K2YcjEmJg_rIonxtYXn0oHZ3o0Cmt6VL3cgp33P4MQtHaoCczBeLY8y7jm9QJO5kRrizdL8E3awzaOj8BhEEQdteozCiQ-41opICTGMZWQZeJZNYDpSS_prMG4mg";
    }


    public static void main(String args[]){
       /* try{
            System.out.println(new MessageUtil().sendMessage(MessageUtil.getToken(),MessageUtil.getRegistrationId(),"----To BMSILS ----"));
        }catch(Exception e){
            e.printStackTrace();
        }*/

        ApnsService service =
                APNS.newService()
                        .withCert("server.p12", "gcn12345")
                        .withSandboxDestination()
                        .build();
        String payload = APNS.newPayload().alertBody("Can't be simpler than this!").build();
        String token = "f6064fab37dfbc30abe60bef663b84c7fc3afcabcdae02931ec8052416365ddf";
        service.push(token, payload);
    }

}