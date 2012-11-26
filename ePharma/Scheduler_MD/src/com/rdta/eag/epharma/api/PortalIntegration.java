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
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

import org.apache.log4j.Logger;

import com.rdta.eag.epharma.commons.persistence.PersistanceException;
import com.rdta.eag.epharma.commons.persistence.QueryRunner;
import com.rdta.eag.epharma.commons.persistence.QueryRunnerFactory;
import com.rdta.eag.epharma.commons.xml.XMLUtil;
import com.rdta.eag.epharma.util.Log4jSetup;


import com.rdta.dhforms.CreatePDF;
import com.rdta.dhforms.SendDHForm;
 


public class PortalIntegration {

  //  private static final String ROLE = "com.rdta.eag.api.PortalIntegration";

 
    private static final QueryRunner queryRunner = QueryRunnerFactory
            .getInstance().getDefaultQueryRunner();
    
    static String  getClassesFolderPath(Class clsObject){
    	String fullPath = PortalIntegration.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    	
    	String className = clsObject.getName();
    	String classNameChanged = replace( className , "." , "/" );
    	int pos;
    	if( (pos = fullPath.indexOf(classNameChanged)) > 0  ){
    		fullPath = fullPath.substring( 0, pos );
    	}
    	
    	return new File(fullPath).getParentFile().getAbsolutePath();
    }
    
    static String replace(String org,String find,String repl){
    	StringBuffer sb = new StringBuffer(org);
    	int pos;
    	while( (pos =  sb.indexOf(find)) >= 0  ){
    		sb.replace(pos,pos+find.length(),repl);
    	}
    	
    	return sb.toString();
    }
    static{
    	try{
    		String path = getClassesFolderPath(PortalIntegration.class);
    		System.out.println("Set the DH Path as 124 " + path);
    		System.setProperty("com.rdta.dhforms.path", path );
    	}catch(Exception e){
    		System.out.println("Exception while setting the DH Path");
    	}
    }
    /**
     * 
     * @param subscriberID
     * @param invoiceID
     * @param pedigreeEnvelopID
     * @param pedigreeID
     * @return
     * @throws Exception 
     */
    public String getPedigreeDocument(String subscriberID, String invoiceID , String pedigreeEnvelopID, String pedigreeID) throws Exception
    {	
    	
    	if(checkForSubscriberID(subscriberID)){
    		
    		if(searchPedigree(pedigreeID,pedigreeEnvelopID,invoiceID)){
    	    			 
    			String stringXML =  createDHForm(pedigreeID);
    			
    	   			
    			System.out.println("\n "+stringXML);
    			return stringXML;
    		}
    		else{
    			
    			System.out.println("Pedigree not found");	
    			return "no ped";
    		}
    		
    	}
    	else{
    		System.out.println("Subscriber ID not found");
    		return "no ped";
    	}

    	
    }
    
    
   private boolean searchPedigree(String pedigreeID, String pedigreeEnvelopID, String invoiceID) throws PersistanceException {
	   
	   StringBuffer buff=new StringBuffer("tlsp:CheckForPedigreeExistsForPortal('"+pedigreeEnvelopID+"','"+pedigreeID+"','"+invoiceID+"')");
	    System.out.println(buff.toString());
		List result = queryRunner.executeQuery(buff.toString());
		if(result.size()>0)
		{
			return true;
		}
		else{
			return false;
		}
		
	}


private boolean checkForSubscriberID(String id) throws PersistanceException {
	   StringBuffer buff=new StringBuffer("tlsp:CheckForSubscriberForPortal('"+id+"')");
	    System.out.println(buff.toString());
		List result = queryRunner.executeQuery(buff.toString());
		if(result.size()>0){
			return true;	
		}
		else
		{
			return false;
		}
	}


public String  createDHForm(String pedigreeID) throws IOException, PersistanceException{
	   
	 //  SendDHForm sdForm = new SendDHForm();
	CreatePDF sdForm = new CreatePDF();
	   String result[]=sdForm.createPDF(pedigreeID);
	   StringBuffer b= new StringBuffer("<pedigreeForm>");
	   		 
	   for(int i=0;i<result.length;i++){
		   if(result[i]!=null){
		  String data=convertTobinary(result[i]);
		   b.append("<pedigree>"+data);
	       b.append("<mimeType>document/pdf</mimeType><encoding>base-64</encoding></pedigree>");
		   }   	   
	   }
	   b.append("</pedigreeForm>");
	   return b.toString();
   }


private String convertTobinary(String stringFilepath) throws IOException, PersistanceException {
	
    StringBuffer buff=new StringBuffer("tlsp:GetBinaryDataForFile('file:///"+stringFilepath+"')");
    System.out.println("GetBinary: " + buff.toString());
    
	String data = queryRunner.returnExecuteQueryStringsAsString(buff.toString());
	return data;
	 
}



    
     
    public static void main(String args[]){
    	PortalIntegration po= new PortalIntegration();
    	try {
    		//String subscriberID, String invoiceID , String pedigreeEnvelopID, String pedigreeID
			//String xml=po.getPedigreeDocument("1679335201","172020","fff36a9f-731b-1ec0-c001-8da0839f4f15","fff36a9f-731b-1d80-c001-8da0839f4f15");
    		String xml=po.getPedigreeDocument("1679335201","172020","fff36aa5-32e0-1a42-c001-6b1603d94e35","fff36aa5-32e0-1100-c001-6b1603d94e35");
			System.out.println(xml);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
  }
