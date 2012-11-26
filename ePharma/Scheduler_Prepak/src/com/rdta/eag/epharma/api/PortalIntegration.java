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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.pdfbox.examples.persistence.AppendDoc;
import org.pdfbox.examples.persistence.CopyDoc;
import org.pdfbox.exceptions.COSVisitorException;

import com.rdta.eag.epharma.commons.persistence.PersistanceException;
import com.rdta.eag.epharma.commons.persistence.QueryRunner;
import com.rdta.eag.epharma.commons.persistence.QueryRunnerFactory;
import com.rdta.eag.epharma.commons.xml.XMLUtil;
import com.rdta.eag.epharma.util.Log4jSetup;


import com.rdta.dhforms.CreatePDF;
import com.rdta.dhforms.SendDHForm;
import com.rdta.dhforms.SendDHFormEmail;
 


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
   
   /* public String getPedigreeEnvelopeDocument(String subscriberID, String invoiceID,String pedigreeEnvelopeID) throws Exception{
    	
    	String xmlString=null;
    	if(checkForSubscriberID(subscriberID)){
    		
    		StringBuffer query=new StringBuffer("tlsp:GetPedigreeIDFromPE('"+pedigreeEnvelopeID+"')");
    		System.out.println(query.toString());
    	
    		List result = queryRunner.returnExecuteQueryStrings(query.toString());
    		
       		xmlString=createDHForm_PedigreesInPE(result);
    			
    		
    		System.out.println("MY XML STRING");
    
			System.out.println(xmlString);
    		return xmlString;
    	
    	}	
    	else{
    		System.out.println("Subscriber ID not found");
    		return "no SubscriberID";
    		
    	}

    }*///Commented for the sake of handling Kitting
    
    
    
    /**
	 * 
	 * @param subscriberID
	 * @param poNumber
	 * @param deaNumber
	 * @param ndcNumber
	 * @param invoiceNumber
	 * @return
	 * @throws Exception
	 */
	public  String getPedigreeGivenInvoiceNumberNDC(String subscriberID,
			String deaNumber, String ndcNumber,
			String invoiceNumber) throws Exception {
			
		
			String pedigreeID ;
		try{	
			if (checkForSubscriberID(subscriberID)) {
			
			pedigreeID = searchPedigrees(deaNumber, ndcNumber,invoiceNumber);
				if(pedigreeID != null)
				{
					String xmlString =  createDHForm(pedigreeID);
					System.out.println("\n "+xmlString);
    			
					return xmlString;
				}
				else{
    			 
					System.out.println("Pedigree not found");	
					return "No pedigree found for the request parameters";
				}
    		
			}
			else{
				System.out.println("Subscriber ID not found");
				return "Subscriber ID not found";
			}
    	} catch(Exception e) {
    		 
    		e.printStackTrace();
    		return "No pedigree found for the request parameters";
    	}
    	
    }
    
	
	/**
	 * 
	 * @param subscriberID
	 * @param poNumber
	 * @param deaNumber
	 * @param ndcNumber
	 * @param invoiceNumber
	 * @return
	 * @throws Exception
	 */
	
	
	public  String getPedigreeGivenInvoiceNumber(String subscriberID,
			String deaNumber, String invoiceNumber) throws Exception {
			
		
		List pedigreeIDs ;
		try{	
			if (checkForSubscriberID(subscriberID)) {
			
			pedigreeIDs = searchPedigrees(deaNumber,invoiceNumber);
			String xmlString=createDHForm_Pedigrees(pedigreeIDs);
    			
    		
    		System.out.println("MY XML STRING");
    
			System.out.println(xmlString);
    		return xmlString;
    	
    	}	
    	else{
    		System.out.println("Subscriber ID not found");
    		return "no SubscriberID";
    		
    	}

    }catch(Exception e) {
		 
		e.printStackTrace();
		return "No pedigree found for the request parameters";
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
    public String getPedigreeDocument(
    		String subscriberID, String invoiceID , 
    		String pedigreeEnvelopID, String pedigreeID)  {	
    	
    	try {
    	if(checkForSubscriberID(subscriberID)){
    		
    		if(searchPedigree(pedigreeID,pedigreeEnvelopID,invoiceID)){
    	    			 
    			String stringXML =  createDHForm(pedigreeID);
    			
    	   			
    			System.out.println("\n "+stringXML);
    			
    			return stringXML;
    		}
    		else{
    			 
    			System.out.println("Pedigree not found");	
    			return "No pedigree found for the request parameters";
    		}
    		
    	}
    	else{
    		System.out.println("Subscriber ID not found");
    		return "Subscriber ID not found";
    	}
    	} catch(Exception e) {
    		 
    		e.printStackTrace();
    		return "No pedigree found for the request parameters";
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

   
private String searchPedigrees(String deaNumber,String ndcNumber,String invoiceNumber) throws PersistanceException {
	   
	   	StringBuffer buff=new StringBuffer("tlsp:getPedigreeDocs_InvoiceAndNDC('"+deaNumber+"','"+ndcNumber+"','"+invoiceNumber+"')");
	    System.out.println(buff.toString());
	    String result = queryRunner.returnExecuteQueryStringsAsString(buff.toString());
		if(result != null)
		{
			System.out.println("Pedigree exists");
			return result;
		}
		else{
			return null;
		}
		
	}


private List searchPedigrees(String deaNumber,String invoiceNumber) throws PersistanceException {
	   
	   	StringBuffer buff=new StringBuffer("tlsp:getPedigreeDocs_Invoice('"+deaNumber+"','"+invoiceNumber+"')");
	    System.out.println(buff.toString());
	    List result = queryRunner.returnExecuteQueryStrings(buff.toString());
	    if (result.size() > 0) {
		
			System.out.println("Pedigrees exists");
			return result;
		}
		else{
			return result;
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



public String mergePDFs(List pdfFilePaths) throws COSVisitorException, IOException{
	AppendDoc merge = new AppendDoc(); 
	CopyDoc copyFile =new CopyDoc();
	CreatePDF form = new CreatePDF();
	File baseDir =form.getBaseDirectory();
	File outDir = new File(baseDir, "out");            
    outDir.mkdirs();
    File outputFile = new File(outDir, "Out.pdf"); 
    outputFile.createNewFile();
    String fileout = outputFile.getAbsolutePath();

	ListIterator it=pdfFilePaths.listIterator();
	 
	if(pdfFilePaths.size()== 1 ){
		copyFile.doIt(it.next().toString(),outputFile.getAbsolutePath());
		
	}else{
      while(!it.hasPrevious()){
    	
    	  String x =it.next().toString();
    	  System.out.println(x);
    	  merge.doIt(x,it.next().toString(),fileout);    	  
    	  	  
      }
      while(it.hasNext()){
    	  
    	  
    	  String file1= it.next().toString();
  
           	merge.doIt(fileout,file1,fileout);
      }
	}
	System.out.println("PDF merged...");
	return fileout;

}

/*
public String  createDHForm_PedigreesInPE(List pedigreeIDs) throws IOException, PersistanceException{
	   
	
	ArrayList result=new ArrayList();
	String resultantString=null;
	Iterator it=pedigreeIDs.iterator();
	System.out.println("Pedigrees "+pedigreeIDs.size());
	StringBuffer buf=null;
	
	while(it.hasNext()){
		String pedigreeID=(String) it.next();
		CreatePDF sdForm = new CreatePDF();
		
		String[] filepaths=sdForm.createPDF(pedigreeID);
		for(int i=0;i<filepaths.length;i++){
			   if(filepaths[i]!=null){
				   buf=new StringBuffer(filepaths[i]);
			   }
		}
		result.add(buf);
	
	}
	try {
			System.out.println("Size of the no of filepaths "+result.size());
		
			resultantString=mergePDFs(result);
		} catch (COSVisitorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	  StringBuffer b= new StringBuffer("<pedigreeForm>");
	  String data=convertTobinary(resultantString);
	  b.append("<pedigree>"+data);
	  b.append("<mimeType>document/pdf</mimeType><encoding>base-64</encoding></pedigree>");
	  b.append("</pedigreeForm>");
	  return b.toString();
	
}

*///Commented for the sake of handling Kitting


public String  createDHForm(String pedigreeID) throws Exception{
	   

	
	CreatePDF sdForm = new CreatePDF();
	   String result=sdForm.createPDF(pedigreeID);
	   StringBuffer b= new StringBuffer("<pedigreeForm>");
	   		 
	   
		   System.out.println("Result of createDHForm is:::: "+0+" ::::::"+result);
		   if(result!=null && !result.equals("")){
			   System.out.println("file path zz"+result);
		  String data=convertTobinary(result);
		   b.append("<pedigree>"+data);
	       b.append("<mimeType>document/pdf</mimeType><encoding>base-64</encoding></pedigree>");
		     
		   
	   }
		   else{
			   System.out.println("No pedigree Found");
			   return "No Pedigree";
		   }
	   b.append("</pedigreeForm>");
	   return b.toString();
   }


public String  createDHForm_Pedigrees(List pedigreeIDs) throws IOException, PersistanceException{
	String resultantString=null;
	CreatePDF sdForm = new CreatePDF();
	try {
		ArrayList result=new ArrayList();
		
		Iterator it=pedigreeIDs.iterator();
		System.out.println("Pedigrees "+pedigreeIDs.size());
		StringBuffer buf=null;
	
		while(it.hasNext()){
			String pedigreeID=(String) it.next();
			
		
			String filepath=sdForm.createPDF(pedigreeID);
		
			if(filepath!=null){
			   buf=new StringBuffer(filepath);
			}
			result.add(buf);
		}
		
		System.out.println("Size of the no of filepaths "+result.size());
	
		resultantString=mergePDFs(result);
	} catch (COSVisitorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	StringBuffer b= new StringBuffer("<pedigreeForm>");
	String data=convertTobinary(resultantString);
	b.append("<pedigree>"+data);
	b.append("<mimeType>document/pdf</mimeType><encoding>base-64</encoding></pedigree>");
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
    		//REPACK
    		/*//String subscriberID, String invoiceID , String pedigreeEnvelopID, String pedigreeID
    		
    		
    		//Pdf Generation for one Pedigree
    		
    		String xml=po.getPedigreeDocument("288581223","141","fff36ad9-6808-1740-c001-9b625c14376b","fff36ad9-680b-1c80-c001-9b625c14376b");
			System.out.println("Completed");
			
			
			//Pdf Generation for the Pedigrees in the PedigreeEnvelope
    		String pedigreeID = "fff36d48-079f-1ec0-c001-547269091795";
			String pedigreeEnvelopID = "fff36d48-079f-1800-c001-547269091795";
			String invoiceID = "141";
			//For Normal
		//	String xml=po.getPedigreeDocument("288581223","17588","urn:uuid:fff36ed7-ec23-1182-c001-ca17b0d55709","fff36ed7-ec23-1183-c001-ca17b0d55709");
		//  For Repackage
		  String xml=po.getPedigreeDocument("288581223","9479","urn:uuid:fff36f2a-728f-1381-c001-e60f3929b44e", "urn:uuid:fff36f2a-728f-1382-c001-e60f3929b44e");
		//	String xml=po.getPedigreeEnvelopeDocument("288581223","141","fff36d48-079f-1800-c001-547269091795");
			System.out.println(xml);
			*/
    		
    		
    		
    		
    		/*//New Based on NDC, INVOICE
    		//getPedigreeGivenInvoiceNumberNDC(String subscriberID,String deaNumber, String ndcNumber,String invoiceNumber)
    		String xml = "";
    		
    		String subscriberID = "288581223";
    		String deaNumber = "RU0326240";
    		String ndcNumber = "00409-1152-78";
    		String invoiceNumber = "376"; 
    		
    		xml = po.getPedigreeGivenInvoiceNumberNDC(subscriberID,deaNumber,ndcNumber,invoiceNumber);
    		System.out.println("----------------------------------------------------------------------");
    		System.out.println("Result Here !!!!"+xml);
    		System.out.println("----------------------------------------------------------------------");
    		
    		*/
    		
//    		New Based on INVOICE
    		//getPedigreeGivenInvoiceNumber(String subscriberID,String deaNumber, String ndcNumber,String invoiceNumber)
    		String xml = "";
    		
    		/*String subscriberID = "631648173";
			String deaNumber = "RU0326240";
			String ndcNumber ="55513-267-01";
			String invoiceNumber ="CO00001";*/

    		String subscriberID = "631648173";
			String deaNumber = "BG9541548";
			String ndcNumber ="58016-0495-02";
			String invoiceNumber ="173476";
    		
    		/*String ndcNumber = "58016-0495-02";
    		String invoiceNumber = "9479"; */
    		
    		xml = po.getPedigreeGivenInvoiceNumberNDC(subscriberID,deaNumber,ndcNumber,invoiceNumber);
    		System.out.println("----------------------------------------------------------------------");
    		System.out.println("Result Here !!!!"+xml);
    		System.out.println("----------------------------------------------------------------------");
		
    	
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	
  }
