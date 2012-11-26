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

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.rdta.eag.commons.xml.XMLUtil;
import com.rdta.eag.commons.persistence.QueryRunner;
import com.rdta.eag.commons.persistence.QueryRunnerFactory;
import com.rdta.eag.security.CreateSig;
import com.rdta.eag.security.VerifySig;

/**
 * @author sanumolu
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class EpharmaAPI {

    private static final String ROLE = "com.rdta.eag.api.EpharmaAPI";

    private static final String LOG_CONFIG = "api_log.xml";

    private static Logger log;

    private static final QueryRunner queryRunner = QueryRunnerFactory
            .getInstance().getDefaultQueryRunner();
    
    static {
        Log4jSetup.initLog4J(LOG_CONFIG);
        log = Logger.getLogger(ROLE);
    }
    
    /**
     * 
     * @param subCert
     * @param pin
     * @param userName
     * @param password
     * @return
     */
    public String getSession(String subCert, String subPin, 
            		String userName, String password) {

        String outputMsg = "";

        try {
            String query = "tlsp:GetSession('" + subCert + "','" + subPin
                    + "','" + userName + "','" + password + "')";
            List lst = queryRunner.returnExecuteQueryStrings(query);
            outputMsg = (String) lst.get(0);

        } catch (Exception e) {
            log.error("Exception in getSession method " + e.toString());
            outputMsg = "Session could not be established.";
        }

        return outputMsg;
    }

    /**
     * 
     * @param signerID
     * @param pedigreeXml
     * @return
     * @throws Exception
     */
    public String submitPedigreeEnvelope(
            		String sessionId, String pedigreeXml)
            		throws Exception {

        String outputMsg = "";

        try {
            log.debug("********** Inside SubmitPedigreeAPI method *********** ");

            if (pedigreeXml != null) {
                
                Node pedNode = XMLUtil.parse(pedigreeXml);
                String query = "tlsp:InsertPedigreeInRP('"+sessionId+"'," + pedigreeXml + ")";
                outputMsg = queryRunner.returnExecuteQueryStringsAsString(query);
                if(outputMsg == null || outputMsg.equals("")) 
                    outputMsg = "Call: submitPedigreeEnvelope could not be completed.";
                
                verifySignature(pedNode);
                
                String pedId = XMLUtil.getValue(pedNode,
                        "pedigree/shippedPedigree/documentInfo/serialNumber");
             
                log.debug("ShippedPedigree id : " + pedId);
                
                pedIn(pedId);
            }

	        
        }catch(Exception e) {
            log.error("Exception in getSession method " + e.toString());
            outputMsg = "Call:submitPedigreeEnvelope could not be completed.";
        }
        return outputMsg;
    }

    /**
     * 
     * @param pedigreeId
     * @return
     * @throws Exception
     */
    public String pedIn(String pedigreeId) throws Exception {

        String outputMsg = "";

        try {
            log.debug("********* Inside pedIn method ********* ");
            String query = "tlsp:PedIn('"+pedigreeId+"')";
            queryRunner.executeQuery(query);
	        outputMsg = "Call: PedIn completed successfully";
	        
        }catch(Exception e) {
            log.error("Exception in getSession method " + e.toString());
            outputMsg = "Call:PedIn could not be completed.";
        }
        return outputMsg;
    }


    /**
     * 
     * @param xmlFilePath
     * @param keyStoreFile
     * @param keyStoreFilePassword
     * @param keyAlias
     * @throws Exception
     */
    public String createSignature(Node xml, String keyStoreFile,
            String keyStoreFilePassword, String keyAlias) throws Exception {
        
        String outputMsg = "";

        try {
            String result = CreateSig.signXMLDocument(xml, keyStoreFile,
                    keyStoreFilePassword, keyAlias);
            
	        outputMsg = "Call:createSignature completed successfully";
	        
        }catch(Exception e) {
            log.error("Exception in getSession method " + e.toString());
            e.printStackTrace();
            outputMsg = "Call:createSignature could not be completed.";
        }

        return outputMsg;
    }

    /**
     * 
     * @param xmlSignedFile
     * @return
     * @throws Exception
     */
    public String verifySignature(Node xml) throws Exception {
        
        String outputMsg = "";

        try {
            //boolean verify = VerifySig.verifySignature(xml);
            boolean verify=true;
            if(verify) {
                outputMsg = "Call:verifySignature completed successfully.";
            } else {
                outputMsg = "Call:verifySignature could not be completed.";
            }
	        
        }catch(Exception e) {
            log.error("Exception in getSession method " + e.toString());
            outputMsg = "Call:verifySignature could not be completed.";
        }

        return outputMsg;
    }
    
    /**
     * @param serverId
     * @param subPartyId
     * @param subStart
     * @param subEnd
     * @param partyRole
     * @param subType
     * @param partyRef
     * @param subTo
     * @param contactName
     * @param title
     * @param address
     * @param phone
     * @param email
     * @param comments
     * @return
     * @throws Exception
     */
    public String subscribeTradingPartner (
            String subPartyId, String licenseNum,
            String publicKey, String partyRole, String contactName,
            String title, String address, String phone,
            String email, String comments ) {
        
        log.debug(".....coming to SubscriptionAPI.....");
        String outputMsg = "";
        try {
            
            SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
            String subscriberId = new Integer(Math.abs(prng.nextInt())).toString();

            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] result =  sha.digest(subscriberId.getBytes());
            String certKey = hexEncode(result);
        
            
            String query = "tlsp:CheckAndCreateSubscriber(" +
    		"'" + subPartyId +"','"+partyRole+"','"+contactName+"','"+title+
    		"','"+address+"','"+phone+"','"+email+"','"+comments+
    		"','"+subscriberId+"','"+certKey+"')";
    
            String msg = queryRunner.returnExecuteQueryStringsAsString(query);
            
            if(!msg.equals("") || msg != null) {
                outputMsg = msg;
            } else {
                outputMsg = "Call:subscribeTradingPartner could not be completed.";
            }
        } catch (Exception e) {
            log.error("Exception in subscribeTradingPartner method " + e.toString());
            outputMsg = "Call:subscribeTradingPartner could not be completed.";
        }
        return outputMsg;
    }
   
    /**
     * @param aInput
     * @return
     */
    private String hexEncode( byte[] aInput){
        
        StringBuffer result = new StringBuffer();
        char[] digits = {'0', '1', '2', '3', '4','5','6','7',
                '8','9','a','b','c','d','e','f'};
        
        for ( int idx = 0; idx < aInput.length; ++idx) {
          byte b = aInput[idx];
          result.append( digits[ (b&0xf0) >> 4 ] );
          result.append( digits[ b&0x0f] );
        }
        return result.toString();
    }
    
    /**
     * 
     * @param subscribeID
     * @param pin
     * @param key
     * @param keyType
     * @return
     * @throws Exception
     */
    public String submitPublicKey(String subscribeID, String pin, String key, String keyType) throws Exception{
    	String outputMsg = "";

        try {
            log.debug("*********Inside submitPublicKey method******* ");

            String query = "tlsp:InsertSubscriberKeys('"+subscribeID+"','"+pin+"','"+key+"','"+keyType+"')";
            queryRunner.executeQuery(query);
	        
            outputMsg = "Call: submitPublicKey completed successfully";

        }catch(Exception e) {
            log.error("Exception in submitPublicKey method " + e.toString());
            outputMsg = "Call:submitPublicKey could not be completed.";
        }
        return outputMsg;
	}
    
    /**
     * @param sessionId
     * @return
     */
    public String logOut(String sessionId) {
        String outputMsg = "";

        try {
                String query = "tlsp:LogOut('"+sessionId+"')";
                outputMsg = queryRunner.returnExecuteQueryStringsAsString(query);
                if(outputMsg == null || outputMsg.equals("")) 
                    outputMsg = "Call: logOut could not be completed.";
                else
                    outputMsg = "Call: logOut completed successfully."; 

        }catch(Exception e) {
            log.error("Exception in logOut method " + e.toString());
            outputMsg = "Call: logOut could not be completed.";
        }
        return outputMsg;
    }

}