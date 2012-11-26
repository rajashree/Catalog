/*
 * Raining Data Corp.
 *
 * Copyright (c) Raining Data Corp. All Rights Reserved.
 *
 * This software is confidential and proprietary information belonging to
 * Raining Data Corp. It is the property of Raining Data Corp. and is protected
 * under the Copyright Laws of the United States of America. No part of this
 * software may be copied or used in any form without the prior
 * written permission of Raining Data Corp.
 *
 */

package com.rdta.eag.api;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import javax.xml.namespace.QName;

/**
 * 
 * @author sanumolu
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class EpharmaAPIClient {

    static String endPoint = "http://192.168.100.34:8080/axis/services/EpharmaAPIInterface?wsdl";

    static String endPointUrl = "http://192.168.100.34:8080/axis/services/EpharmaAPIInterface";

    public static String getSession(String subCert, String subPin,
            String userName, String password) {
        try {
            /*
             * String subCert = "true"; String subPin = "322"; String userName =
             * "test"; String password = "test";
             */

            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(endPoint));
            call.setOperationName(new QName(endPointUrl, "getSession"));

            String sessionId = (call.invoke(new Object[] { subCert, subPin,
                    userName, password })).toString();

            System.out.println("WS Response::::::    " + sessionId);
            return sessionId;
        } catch (Exception e) {
            System.err.println(e.toString());
            return e.toString();
        }
    }

    public static String submitPedigreeEnvelope(String sessionId, String pedigreeEnv) {

        try {

            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(endPoint));

            call.setOperationName(new QName(endPointUrl,
                    "submitPedigreeEnvelope"));

            String response = (call.invoke(new Object[] { sessionId,
                    pedigreeEnv })).toString();

            System.out.println("WS Response::::::    " + response);
            return response;
        } catch (Exception e) {
            System.err.println(e.toString());
            return e.toString();
        }
    }

    public static String logOut(String sessionId) {
        
        try {
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(endPoint));
            call.setOperationName(new QName(endPointUrl, "logOut"));

            String response = (call.invoke(
                    	new Object[] { sessionId })).toString();

            System.out.println("WS Response::::::    " + response);
            return response;
        } catch (Exception e) {
            System.err.println(e.toString());
            return e.toString();
        }
    }
    
    public static String subscribeTradingPartner( String subPartyId, String licenseNum,
            String publicKey, String partyRole, String contactName,
            String title, String address, String phone,
            String email, String comments ) {
        
        try {
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(endPoint));
            call.setOperationName(new QName(endPointUrl, "subscribeTradingPartner"));

            String response = (call.invoke(
                    	new Object[] { subPartyId, licenseNum,
                                publicKey, partyRole, contactName,
                                title, address, phone,
                                email, comments })).toString();

            System.out.println("WS Response::::::    " + response);
            return response;
        } catch (Exception e) {
            System.err.println(e.toString());
            return e.toString();
        }
    }

    public static String submitPublicKey(String subscribeID, String pin, String key, String keyType) {
        
        try {
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(endPoint));
            call.setOperationName(new QName(endPointUrl, "submitPublicKey"));

            String response = (call.invoke(
                    	new Object[] { subscribeID, pin, key, keyType })).toString();

            System.out.println("WS Response::::::    " + response);
            return response;
        } catch (Exception e) {
            System.err.println(e.toString());
            return e.toString();
        }
    }
    
    public static void main(String[] args) {
        try {
            String sessionId = getSession("Sub123","1234","bob","bob");
            String pedigree = new String("<PedigreeEnvelope><version>1</version><serialNumber>PE0004</serialNumber><date>2006-04-07T10:27:35.406Z</date><pedigree><shippedPedigree id='ID0009'><documentInfo><serialNumber>ID0009</serialNumber><version>1</version></documentInfo><repackagedPedigree><previousProducts><previousProductInfo><manufacturer>Watson Pharmaceuticals</manufacturer><productCode>00591-5818-01</productCode></previousProductInfo><itemInfo><lot>WAT1001</lot><quantity>100</quantity></itemInfo><contactInfo><name>watson user1</name></contactInfo></previousProducts><productInfo><drugName>Naproxen</drugName><manufacturer>Watson</manufacturer><productCode>58016-289-00</productCode><dosageForm>Oral</dosageForm><strength>500mg</strength><containerSize>bottle</containerSize></productInfo><itemInfo><lot>SWD001</lot><quantity>50</quantity></itemInfo></repackagedPedigree><transactionInfo><senderInfo><businessAddress><businessName>Southwood Pharmaceuticals</businessName><street1>60 Empire Drive</street1><street2/><city>Lake Forest</city><stateOrRegion>CA</stateOrRegion><postalCode>92630</postalCode><country>USA</country></businessAddress><shippingAddress><businessName>Southwood Pharmaceuticals</businessName><street1>60 Empire Drive</street1><street2/><city>Lake Forest</city><stateOrRegion>CA</stateOrRegion><postalCode>92630</postalCode><country>USA</country></shippingAddress><licenseNumber state='FL' agency=''>SWD-198453</licenseNumber><contactInfo><name>swd user1</name></contactInfo></senderInfo><recipientInfo><businessAddress><businessName>St. John's Hospital</businessName><street1>5001 St. Johns Avenue</street1><street2/><city>Palatka</city><stateOrRegion>FL</stateOrRegion><postalCode>32177</postalCode><country>USA</country></businessAddress><shippingAddress><businessName>St. John's Hospital</businessName><street1>5001 St. Johns Avenue</street1><street2/><city>Palatka</city><stateOrRegion>FL</stateOrRegion><postalCode>32177</postalCode><country>USA</country></shippingAddress><licenseNumber state='FL' agency=''>SWD-198453</licenseNumber><contactInfo><name>swd user1</name></contactInfo></recipientInfo><transactionIdentifier><identifier>C28785100776</identifier><identifierType>DespatchAdvice</identifierType></transactionIdentifier><transactionType>Sale</transactionType></transactionInfo><signatureInfo><signerInfo><name>Mike</name></signerInfo><signatureDate>2006-04-06T10:27:35.406Z</signatureDate><signatureMeaning>Certified</signatureMeaning></signatureInfo></shippedPedigree></pedigree></PedigreeEnvelope>");
            String response = submitPedigreeEnvelope(sessionId,pedigree);
            
            String subscribeResponse = subscribeTradingPartner("Sub12345","12345", "", "Distributor", "John Doe",
                    "Manager", "123 Main St. San Jose, CA 95123", "408-123-1234",
                    "jdoe@test.com", "comments comments comments");
            String keyResponse = submitPublicKey("Sub1234","12345","key","keyType");
            String logOutResponse = logOut(sessionId);
            	
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

}