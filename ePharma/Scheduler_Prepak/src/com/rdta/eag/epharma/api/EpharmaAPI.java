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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.rdta.eag.epharma.commons.xml.XMLUtil;
import com.rdta.eag.epharma.commons.xml.XMLUtils;

import com.rdta.eag.epharma.commons.persistence.QueryRunner;
import com.rdta.eag.epharma.commons.persistence.QueryRunnerFactory;
import com.rdta.eag.epharma.commons.persistence.pool.TLConnectionPool;
//import com.rdta.eag.security.CreateSig;
import com.rdta.eag.security.VerifySig;
import com.rdta.eag.security.VerifySig_PE;
import com.rdta.tlapi.xql.Connection;

/**
 * @author sanumolu
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class EpharmaAPI {

    private static final String ROLE = "com.rdta.eag.epharma.api.EpharmaAPI";
    
    private static final String LOG_CONFIG = "api_log.xml";

    private static Logger log;
    
    private static final String PROPERTIES_CONFIG = "Prop.properties";
    
    static Connection conn = null;
    
    private static final QueryRunner queryRunner = QueryRunnerFactory
            .getInstance().getDefaultQueryRunner();
    
    static {
    	
    	Log4jSetup.initLog4J(LOG_CONFIG);
        log = Logger.getLogger(ROLE);
    }
    
    static String signerid = null;
    
    
    
    public void readPropertiesFile(){
		try{
			
			log.debug("********** Inside readPropertiesFile method *********** ");
			InputStream inputstream = new FileInputStream(PROPERTIES_CONFIG);
			java.util.Properties properties = new java.util.Properties();
			properties.load(inputstream);

			
			signerid = properties.getProperty("signerId");
			log.info("The signer Id from propeties file "+signerid);
			
		}catch(IOException e){
			log.error("Exception in readPropertiesFile method " + e.toString());
		}
		
		
	}
	

    /**
     * 
     * @param subCert
     * @param pin
     * @param userName
     * @param password
     * @return
     */
    public static String getSession(String subCert, String subPin, 
            		String userName, String password) {

        String outputMsg = "";

        try {
        	log.debug("********** Inside getSession method *********** ");
            
        	String query = "tlsp:GetSession('" + subCert + "','" + subPin
                    + "','" + userName + "','" + password + "')";
            log.info("Query "+query);
            List lst = queryRunner.returnExecuteQueryStrings(query);
            outputMsg = (String) lst.get(0);
           
            	
        } catch (Exception e) {
            log.error("Exception in getSession method " + e.toString());
            e.printStackTrace();
            outputMsg = "Session could not be established.";
        }
        log.info("Output Msg"+outputMsg);
        System.out.println("Output Msg"+outputMsg);
        return outputMsg;
    }

    /**
     * 
     * @param signerID
     * @param doc
     * @return
     * @throws Exception
     */
   
    public static String submitPedigreeEnvelope(
            		String sessionId, String inputXml)
            		throws Exception {

        String outputMsg = "";
        boolean isValidSign = false;
        boolean pedInResult = false;
       
        
        
        try {
        	EpharmaAPI obj = new EpharmaAPI();
			obj.readPropertiesFile();
        	log.debug("********** Inside submitPedigreeEnvelope method *********** ");
            if (inputXml != null) {
                
                Node pedNode = XMLUtil.parse(inputXml);
                isValidSign=verifySignature( pedNode.getOwnerDocument());
                log.info("isValidSign value"+isValidSign);
                if(isValidSign){
                	StringBufferInputStream sbIns = new StringBufferInputStream(inputXml);
                	conn = TLConnectionPool.getTLConnectionPool().borrowConnection();
                	//String query = "tlsp:InsertPedigreeInRP('"+sessionId+"', " + inputXml + ")";
                	String query = "tlsp:InsertPedigreeInRP('"+sessionId+"', $1)";
                	outputMsg = queryRunner.returnExecuteQueryStreamAsStringNew(query,conn, sbIns);
                	log.info("Query for InsertPedigreeInRP"+query);
                	//outputMsg = queryRunner.returnExecuteQueryStringsAsString(query);
                	log.info("Result of InsertPedigreeInRP query  "+outputMsg);
                	
                	if(outputMsg == null || outputMsg.equals("")) 
                		outputMsg = "Call: submitPedigreeEnvelope could not be completed.";
                	String pedEnvId = XMLUtil.getValue(pedNode,
                        "/pedigreeEnvelope/serialNumber");
                	log.info("The SignerID from properties file ::"+signerid);
                	String query1 = "tlsp:CreateReceivedPedigreeForPedigrees_EPharmaAPI('"+signerid+"', '"+pedEnvId +"')";
                	log.info("Query for CreateReceivedPedigreeForPedigrees_EPharmaAPI"+query);
                	List recvPedIds =new ArrayList();
                	recvPedIds = queryRunner.returnExecuteQueryStrings(query1);
                
                	for(int i=0; i<recvPedIds.size();i++){
                		log.info("The receivePedigreeId >> PED-IN >>"+recvPedIds.get(i).toString()); 
                		pedInResult=pedIn(recvPedIds.get(i).toString()); 
                		if(pedInResult){
                			outputMsg="Transaction has been Successful";
                		}else{
                			outputMsg="Data could not be loaded to SQL Database";
                		}
                		
         
                	}
                }else 
                {
                	outputMsg = "Signature is Invalid.";
                }
               
           }

	        
        }catch(Exception e) {
            log.error("Exception in submitPedigreeEnvelope method " + e.toString());
            e.printStackTrace();
            outputMsg = "Call:submitPedigreeEnvelope could not be completed.";
        }finally {
			try {
				if(conn != null) 
					TLConnectionPool.getTLConnectionPool().returnConnection(conn);
				
			}catch(Exception ex){}
		}
        log.info("Output Msg"+outputMsg);
        return outputMsg;
    }


    
    
    /**
     * 
     * @param pedigreeId
     * @return
     * @throws Exception
     */
    public static boolean pedIn(String pedigreeId) throws Exception {

        String outputMsg = "";
        boolean pedInResult = true;

        try {
        	log.debug("********** Inside pedIn method *********** ");
        	
        	String query1 = "tlsp:GetPedInXMLData('"+pedigreeId+"')";
        	String xmlString = queryRunner.returnExecuteQueryStringsAsString(query1);
        	
        	log.info("The result of GetPedInXml Stored Procedure "+xmlString);
            StringBufferInputStream sbIns = new StringBufferInputStream(xmlString);
        	String query = "tlsp:Ped-InPrePak($1)";
            log.info("Query for Ped-In"+query);
            queryRunner.returnExecuteQueryStreamAsStringNew(query,conn, sbIns);
	        outputMsg = "Call: PedIn completed successfully";
	        
        }catch(Exception e) {
            log.error("Exception in getSession method " + e.toString());
            e.printStackTrace();
            outputMsg = "Data could not be loaded to SQL Database";
            pedInResult=false;
        }
        return pedInResult;
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
        	log.debug("********** Inside createSignature method *********** ");
          // String result = CreateSig.signXMLDocument(xml, keyStoreFile,
          //       keyStoreFilePassword, keyAlias);
            
	        outputMsg = "Call:createSignature completed successfully";
	        
        }catch(Exception e) {
            log.error("Exception in createSignature method " + e.toString());
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
    public static boolean verifySignature(Document doc) throws Exception {
        
        String outputMsg = "";
        boolean isValid=false;
        try {
        	log.debug("********** Inside verifySignature method *********** ");
        	 outputMsg = "Call:verifySignature completed successfully.";
        	
        	        
            boolean verify =VerifySig_PE.verifyPedigreeEnvelopeString(XMLUtils.convertXMLDocumentToString(doc)); 
                  
            if(verify) {
                outputMsg = "Call:verifySignature completed successfully.";
                isValid=true;
            } else {
                outputMsg = "Call:verifySignature could not be completed.";
                isValid=false;
            }
	        
        }catch(Exception e) {
            System.out.println("Exception in verifySignature method " + e.toString());
            e.printStackTrace();
            outputMsg = "Call:verifySignature could not be completed.";
        }
        log.info("Output Msg"+outputMsg);
        return isValid; 
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
        
        
        String outputMsg = "";
        try {
        	log.debug("********** Inside subscribeTradingPartner method *********** ");
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
            e.printStackTrace();
            outputMsg = "Call:subscribeTradingPartner could not be completed.";
        }
        
        log.info("Output Msg"+outputMsg);
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
            outputMsg = queryRunner.returnExecuteQueryStringsAsString(query);
            log.info("Result of query: InsertSubscriberKeys "+outputMsg);
            if(outputMsg == null || outputMsg.equals("")) 
                outputMsg = "Call: submitPublicKey could not be completed.";
            else
                outputMsg = "Call: submitPublicKey completed successfully."; 

        }catch(Exception e) {
            log.error("Exception in submitPublicKey method " + e.toString());
            e.printStackTrace();
            outputMsg = "Call:submitPublicKey could not be completed.";
        }
       
        log.info("Output Msg"+outputMsg);
        return outputMsg;
	}
    
    /**
     * @param sessionId
     * @return
     */
    public String logOut(String sessionId) {
        String outputMsg = "";

        try {
        		log.debug("********** Inside logOut method *********** ");
                String query = "tlsp:LogOut('"+sessionId+"')";
                outputMsg = queryRunner.returnExecuteQueryStringsAsString(query);
                
                if(outputMsg == null || outputMsg.equals("")) 
                    outputMsg = "Call: logOut could not be completed.";
                else
                    outputMsg = "Call: logOut completed successfully."; 

        }catch(Exception e) {
            log.error("Exception in logOut method " + e.toString());
            e.printStackTrace();
            outputMsg = "Call: logOut could not be completed.";
        }
        log.info("Output Msg"+outputMsg);
        return outputMsg;
    }
    
    
 public String getOutgoingPedigreeEnvelope(String session,String invoiceNumber,String NDC){
    	
    	try {
            
    		
    		

    }catch(Exception e) {
        log.error("Exception in getOutgoingPedigreeEnvelope method " + e.toString());
        return  "Call: getOutgoingPedigreeEnvelope method could not be completed.";
    }
    	
    	return "Success in getOutgoingPedigreeEnvelope";
    }
    
    
    public static void main(String[] agrs) throws Exception{
    	
    	EpharmaAPI obj = new EpharmaAPI();
  
    	obj.readPropertiesFile();
    	
    	      	
        //Create Subscribers into EPharma System.
    	
    	/*obj.subscribeTradingPartner("Southwood","12345", "", "Distributor", "asdJohn Doe",
                "Manager", "123 Main St. San Jose, CA 95123", "408-123-1234",
                "jdoe@test.com", "comments comments comments");
          */
    	
    	//Submission of the Public key used for verification of signatures
    	
    	/*obj.submitPublicKey("1362143605","12345","key","keyType");
    	*/
    	
    	//Generating Session for the subscriber
    	/*String sessionId =obj.getSession("1362143605","12345","dloy","dloy");
    	System.out.println("SessionID"+sessionId);
    	*/
    	
    	
    	//Submit PedigreeEnvelope
    	/*
    	File f = new File("c:/signatureFile1.xml");

		Document doc = XMLUtils.createDocument("c:/signatureFile1.xml");
		String pedigreeXml =XMLUtils.convertXMLDocumentToString(doc);
    	String response = obj.submitPedigreeEnvelope(sessionId,pedigreeXml);
    	*/
    	
    	//LogOUT
    	
    	/*String result =  obj.logOut("ce0e92f7f18839fe:27fb8d2:11173133082:-80001174477960890");
   		System.out.println("Session Logout - result--- "+result);
    	*/
    	
    	
    	
    	//Obtain Session and SubmitPedigree Envelope
    	
    	String sessionId =obj.getSession("106978968","12345","dloy","dloy");
    	System.out.println("SessionID"+sessionId);
    	File f = new File("c:/signatureFile1.xml");

		Document doc = XMLUtils.createDocument("c:/signatureFile1.xml");
		String pedigreeXml =XMLUtils.convertXMLDocumentToString(doc);
    	String response = obj.submitPedigreeEnvelope(sessionId,pedigreeXml);
    	
    	
    	
    	
    	
    	
    }
}