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

package com.rdta.eag.epharma.api;

import java.io.File;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.w3c.dom.Document;

import com.rdta.eag.epharma.commons.xml.XMLUtils;

import javax.xml.namespace.QName;

/**
 * 
 * @author sanumolu
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class EpharmaAPIClient {
    //64.147.6.52         192.168.100.34
	/*static String endPoint = "http://64.147.6.52:8080/axis/services/PortalIntegration?wsdl";
	static String endPointUrl = "http://64.147.6.52:8080/axis/services/PortalIntegration";
	*/
	
	static String endPoint ="http://localhost:8081/axis/services/EpharmaAPIInterface?wsdl";
	static String endPointUrl ="http://localhost:8081/axis/services/EpharmaAPIInterface";
    

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

    public static String submitPedigreeEnvelope(String sessionId, String pedigreeXml) {

        try {

            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(endPoint));

            call.setOperationName(new QName(endPointUrl,
                    "submitPedigreeEnvelope"));

            String response = (call.invoke(new Object[] { sessionId,
                    pedigreeXml })).toString();

            System.out.println("WS Response::::::    " + response);
            return response;
        }catch (AxisFault e) {
            System.err.println(e.getFaultString());
            System.err.println(e.dumpToString());
            System.err.println(e.getMessage());
            e.printStackTrace();
            return e.dumpToString();
        } 
        catch (Exception e) {
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
            System.out.println("Ahelllo");
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
    
    public static String portalIntegration() {
        
        try {
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(endPoint));
            call.setOperationName(new QName(endPointUrl, "getPedigreeDocument"));

            String response = (call.invoke(
                    	new Object[] { "402448514","182753","fff36db8-90a5-1100-c001-4ef128d06b7c",
                    	        "fff36db8-90ab-1f80-c001-4ef128d06b7c" })).toString();
            /*String response = (call.invoke(
                	new Object[] { "402448514","174469","fff36b69-0337-1f00-c001-eff841af394a",
                	        "fff36b69-0339-13c0-c001-eff841af394a" })).toString();
            String response = (call.invoke(
                	new Object[] { "402448514","174757","fff36b98-2f23-1080-c001-eff841af394a",
                	        "fff36b98-2f26-16c0-c001-eff841af394a" })).toString(); */
            
            System.out.println("WS Response::::::    " + response);
            return response;
        } catch (Exception e) {
            System.err.println(e.toString());
            return e.toString();
        }
    }
    
    public static void main(String[] args) {
        try {
        	
        	/* String subscribeResponse = subscribeTradingPartner("Sub12345","12345", "", "Distributor", "John Doe",
                     "Manager", "123 Main St. San Jose, CA 95123", "408-123-1234",
                     "jdoe@test.com", "comments comments comments");
        	
           
            String pedigree = new String("<PedigreeEnvelope><version>1</version><serialNumber>PE0004</serialNumber><date>2006-04-07T10:27:35.406Z</date><pedigree><shippedPedigree id='ID0009'><documentInfo><serialNumber>ID0009</serialNumber><version>1</version></documentInfo><repackagedPedigree><previousProducts><previousProductInfo><manufacturer>Watson Pharmaceuticals</manufacturer><productCode>00591-5818-01</productCode></previousProductInfo><itemInfo><lot>WAT1001</lot><quantity>100</quantity></itemInfo><contactInfo><name>watson user1</name></contactInfo></previousProducts><productInfo><drugName>Naproxen</drugName><manufacturer>Watson</manufacturer><productCode>58016-289-00</productCode><dosageForm>Oral</dosageForm><strength>500mg</strength><containerSize>bottle</containerSize></productInfo><itemInfo><lot>SWD001</lot><quantity>50</quantity></itemInfo></repackagedPedigree><transactionInfo><senderInfo><businessAddress><businessName>Southwood Pharmaceuticals</businessName><street1>60 Empire Drive</street1><street2/><city>Lake Forest</city><stateOrRegion>CA</stateOrRegion><postalCode>92630</postalCode><country>USA</country></businessAddress><shippingAddress><businessName>Southwood Pharmaceuticals</businessName><street1>60 Empire Drive</street1><street2/><city>Lake Forest</city><stateOrRegion>CA</stateOrRegion><postalCode>92630</postalCode><country>USA</country></shippingAddress><licenseNumber state='FL' agency=''>SWD-198453</licenseNumber><contactInfo><name>swd user1</name></contactInfo></senderInfo><recipientInfo><businessAddress><businessName>St. John's Hospital</businessName><street1>5001 St. Johns Avenue</street1><street2/><city>Palatka</city><stateOrRegion>FL</stateOrRegion><postalCode>32177</postalCode><country>USA</country></businessAddress><shippingAddress><businessName>St. John's Hospital</businessName><street1>5001 St. Johns Avenue</street1><street2/><city>Palatka</city><stateOrRegion>FL</stateOrRegion><postalCode>32177</postalCode><country>USA</country></shippingAddress><licenseNumber state='FL' agency=''>SWD-198453</licenseNumber><contactInfo><name>swd user1</name></contactInfo></recipientInfo><transactionIdentifier><identifier>C28785100776</identifier><identifierType>DespatchAdvice</identifierType></transactionIdentifier><transactionType>Sale</transactionType></transactionInfo><signatureInfo><signerInfo><name>Mike</name></signerInfo><signatureDate>2006-04-06T10:27:35.406Z</signatureDate><signatureMeaning>Certified</signatureMeaning></signatureInfo></shippedPedigree></pedigree></PedigreeEnvelope>");
            String response = submitPedigreeEnvelope(sessionId,pedigree);
            
            String subscribeResponse = subscribeTradingPartner("Sub12345","12345", "", "Distributor", "John Doe",
                    "Manager", "123 Main St. San Jose, CA 95123", "408-123-1234",
                    "jdoe@test.com", "comments comments comments");
            String keyResponse = submitPublicKey("Sub1234","12345","key","keyType");
           String logOutResponse = logOut(sessionId);
            String response = portalIntegration();*/
        	
        	 
        	/*	String subscribeResponse = subscribeTradingPartner("Southwood","12345", "", "Distributor", "John Doe",
                    "Manager", "123 Main St. San Jose, CA 95123", "408-123-1234",
                    "jdoe@test.com", "comments comments comments");
        	System.out.println("The Response in the main class"+subscribeResponse);
        	String keyResponse = submitPublicKey("106978968","12345","key","keyType");
        	System.out.println("The Response for the Submit Public Key"+keyResponse);	
        	*//*String sessionId = getSession("106978968","1234","test","test");
        	System.out.println("The session id "+sessionId);
        
      
        	File f = new File("c:/signatureFile1.xml");

    		Document doc = XMLUtils.createDocument("c:/signatureFile1.xml");
    		String pedigreeXml =XMLUtils.convertXMLDocumentToString(doc);
        	String response = submitPedigreeEnvelope("ce0e92f7f18839fe:27fb8d2:110e7ae685e:-80001172138933671",pedigreeXml);
        	  */
		//String logOutResponse = logOut("b8f65819f8509503:27ff9ee:10eeb64a847:-80001164015505856");
        
        	String sessionId = getSession("106978968","1234","test","test");
        	File f = new File("c:/signatureFile1.xml");

    		Document doc = XMLUtils.createDocument("c:/signatureFile1.xml");
    		String pedigreeXml =XMLUtils.convertXMLDocumentToString(doc);
        	String response = submitPedigreeEnvelope(sessionId,pedigreeXml);
        	
        
        	
        	    } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

}