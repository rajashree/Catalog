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

package com.rdta.pedigreebank;
 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;



import java.text.SimpleDateFormat;
 
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import java.util.TimeZone;
 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
 
import org.w3c.dom.Document;
import org.w3c.dom.Node;


import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;

import com.rdta.eag.epharma.util.SendDHFormEmail;


public class AutomaticCaseForMD  implements Job{

	private String filePath;
	private Map sourceHeaderMap = new LinkedHashMap(); 
	private BufferedReader buffReader;
	private int lineCount = 0;
	private int colCount = 0;
	private static String invoice = "start";
	static final SimpleDateFormat sdf = new SimpleDateFormat("MMddyy");
	static String emailFrom = null;
	static String emailTo = null;
	static String emailSubject = null;
	static String emailSMTP = null;
	static String emailPassword = null; 
	static String signerName = null;
	static String signerTitle = null;
	static String signerTel = null;
	static String signerEmail = null;
	static String fileUrl = null;
	static String errorUrl = null;
	static String processedUrl = null;
	static String sourceRoutingCode = null;
	static String destinationRoutingCode = null;
	static String signerid = null;
	static String deaNumber = null;

	
	private boolean isReaderStatusClosed = false;
	
	 
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	
	
	
	public AutomaticCaseForMD(String filePath) throws Exception {
	this.filePath = filePath;
	buffReader = new BufferedReader(new FileReader(filePath));
	}
	
	public AutomaticCaseForMD(String filePath,int lineNum ) throws Exception {
		this(filePath);
		skipNumberOfLines(lineNum);
	}
	
	
	public AutomaticCaseForMD() {
		// TODO Auto-generated constructor stub
	}

	private void skipNumberOfLines(int lineNum) throws Exception {
		
		while(lineCount < lineNum) {
			getNextLine();
		}
	}
	
	public String getNextLine() throws Exception {
		String line  = null;
		if(!isReaderStatusClosed){
			line = buffReader.readLine();
			if(line != null) {
				lineCount = lineCount + 1;
			} else {
				isReaderStatusClosed = true;
				closeStream();
			}
		}
		
		return line;
	}
	public void LineDecrement() throws Exception {
		lineCount=lineCount-1;
	}
	
	private Map prepareHeaderMap() throws Exception {
	 
		String line = getNextLine();
		System.out.println(line);
		if(line != null) {
			String[] result = line.split("\\|");
			colCount = result.length - 1;
			String token = null;
			for(int i = 0; i<result.length; i++) {
				token = result[i];
				System.out.println("Header Name: "+i + token);
				sourceHeaderMap.put(new Integer(i), token);
			     
			} 
		} 
		 return sourceHeaderMap;
		 
	}
	private Map prepareHeaderMapForAB() throws Exception {
		
		 Map Ab=  new LinkedHashMap();
	 	 Ab.put(new Integer(0),"RecordIdentifier");
		 Ab.put(new Integer(1),"PONo");
		 Ab.put(new Integer(2),"PODateMMDDYY");
		 Ab.put(new Integer(3),"RecDateMMDDYY");
		 Ab.put(new Integer(4),"BankTimeMMDDYYhhmmss");
		 Ab.put(new Integer(5),"NDC");
		 Ab.put(new Integer(6),"MFGName");
		 Ab.put(new Integer(7),"DrugLabel");
		 Ab.put(new Integer(8),"Form");
		 Ab.put(new Integer(9),"Strength");
		 Ab.put(new Integer(10),"ContainerSize");
		 Ab.put(new Integer(11),"LotNo");
		 Ab.put(new Integer(12),"ExpMMYY");
		 Ab.put(new Integer(13),"BinLocation");
		 Ab.put(new Integer(14),"Quantity");
		 Ab.put(new Integer(15),"MFGAddressLine1");
		 Ab.put(new Integer(16),"MFGAddressLine2");
		 Ab.put(new Integer(17),"MFGCity");
		 Ab.put(new Integer(18),"State");
		 Ab.put(new Integer(19),"MFGZip");
		 Ab.put(new Integer(20),"Country");
		 Ab.put(new Integer(21),"MFGContactName");
		 Ab.put(new Integer(22),"MFGContactTelephone");
		 Ab.put(new Integer(23),"MFGContactEMail");
		 Ab.put(new Integer(24),"MFGDea");
		 Ab.put(new Integer(25),"CaseSerialNo");
		 return Ab;
		 
	}
	
	
	private Map prepareMapForAP() throws Exception {
		
	      
	    Map ApMap=  new LinkedHashMap();
	    ApMap.put(new Integer(0), "RecordIdentifier");
	    ApMap.put(new Integer(1), "PullTimeMMDDYYhhmmss");
	    ApMap.put(new Integer(2), "QtyPulled");
	    ApMap.put(new Integer(3), "NDC");
	    ApMap.put(new Integer(4), "InvoiceNo");
	    ApMap.put(new Integer(5), "InvoiceDate");
	    ApMap.put(new Integer(6), "PONumber");
	    ApMap.put(new Integer(7), "CustomerName");
	    ApMap.put(new Integer(8), "CustomerAddressLine1");
	    ApMap.put(new Integer(9), "CustomerAddressLine2");
	    ApMap.put(new Integer(10), "CustomerCity");
	    ApMap.put(new Integer(11), "State");
	    ApMap.put(new Integer(12), "CustomerZip");
	    ApMap.put(new Integer(13), "Country");
	    ApMap.put(new Integer(14), "ShipToCustomerName");
	    ApMap.put(new Integer(15), "ShipToCustomerAddressLine1");
	    ApMap.put(new Integer(16), "ShipToCustomerAddressLine2");
	    ApMap.put(new Integer(17), "ShipToCustomerCity");
	    ApMap.put(new Integer(18), "ShipToState");
	    ApMap.put(new Integer(19), "ShipToCustomerZip");
	    ApMap.put(new Integer(20), "ShipToCountry");
	    ApMap.put(new Integer(21), "UniqueBoxID");
	    ApMap.put(new Integer(22), "BinLocation");
	    ApMap.put(new Integer(23), "CustomerDea");
	    	 
		 return ApMap;
		 
	}
	
	/**
	 * Returns number of columns found in input file. 
	 * 
	 * @return
	 */
	public int getColumnCount() {
		return colCount;
	}
	
	/**
	 * Returns number of lines read as of now from the input stream.
	 * 
	 * @return int
	 */
	public int getLineCount() {
		
		return lineCount;
	}
	
	/**
	 * Returns Header information.
	 * Which contains Column Index as Key and Value has DataElementNode
	 * 
	 * @return
	 */
	public Map getHeaderMap() {
	
		return sourceHeaderMap;
	}
	
	
	public boolean isStreamClosed() {
		return isReaderStatusClosed;
	}
	
	
	/**
	 * Close the open file stream
	 *
	 */
	public void closeStream()  {
		try {
			isReaderStatusClosed = true;
			if(buffReader != null) {
				buffReader.close();
			}
		} catch(Exception e) { }
	}
	
	
	public void finalzie() {
		try {
			
			if(buffReader != null) {
				buffReader.close();
			}
			
		}catch(Exception e) {}
	}
	
		
	public String addError(String RecId,int lineNo, String colName) throws Exception{
		String er="<Error><RecordIdentifier>"+RecId+"</RecordIdentifier><Message>"+colName+" at line "+lineNo+" is null</Message></Error>";
		return  er;
	}
	
	
	public String createXMLForAP(Map m,String subrootname,String line) throws Exception{
	    StringBuffer  t =new StringBuffer("");
	    
	    
	    t.append("<"+subrootname+">");   
	    String res[]=line.split("\\|");
	    
	    Map mp = m;
	    for (Iterator it=mp.keySet().iterator(); it.hasNext(); ) {
	        Object key = it.next();
	        Object value = mp.get(key);
	        int i = Integer.parseInt(key.toString());
	         
	        if(i<res.length && res[i]!=null){
	            t.append("<"+value+">"+res[i].trim()+"</"+value+">");
	        }
	        else{
	            t.append("<"+value+"/>");
	        }
	        
	        
	    }
	    t.append("</"+subrootname+">");
	     
	  
	    
	    return (t.toString()).replaceAll("&","&amp;");
	}
 
	public String createXML(Map m,String line) throws Exception{
		StringBuffer  t =new StringBuffer("");
	    
	    if(line !=null)
	    {
	    	t.append("<PedRcv>");   
	    String res[]=line.split("\\|");
	    
	    Map mp = m;
	    for (Iterator it=mp.keySet().iterator(); it.hasNext(); ) {
	        Object key = it.next();
	        Object value = mp.get(key);
	        int i = Integer.parseInt(key.toString());
	         
	        if(i<res.length && res[i]!=null){
	        	t.append("<"+value+">"+res[i].trim()+"</"+value+">");
	        }
	        else{
	        	t.append("<"+value+"/>") ;
	        }
	        
	        
	    }
	    t.append("</PedRcv>");
	     
	    }
	    return (t.toString()).replaceAll("&","&amp;") ;
	}

	private String checkforABorAP(String line) throws Exception {
		// TODO Auto-generated method stub
		if(line!=null)
		{
		 String res[]=line.split("\\|");
		 
		 if( res[0]!=null && res[0].equalsIgnoreCase("AB"))
	            {
			 
			 return "AB";
	            }
		 if( res[0]!=null && res[0].equalsIgnoreCase("AP"))
         {
		 
		 return "AP";
         }
		 if( res[0]!=null && res[0].equalsIgnoreCase("RecordIdentifier"))
         {
		 
		 return "RecordIdentifier";
         }
	          
		}
		return "NA"; 
		
	}
	
	

	
	/**
	 * @param lineCount The lineCount to set.
	 */
	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}
	
	
  

private void updatePedigreeBankForAB(String xmlStr) {
	
	
	try {
		Node n = XMLUtil.parse(xmlStr);
		String ndc=XMLUtil.getValue(n,"NDC");
		String binno=XMLUtil.getValue(n,"BinLocation");
		System.out.println("BINLOC"+binno);
		
		String query="tlsp:BinNumberExists_MD('"+ndc.trim()+"','"+binno.trim()+"')";
		System.out.println("check if NDC exists"+query);
		String result=queryRunner.returnExecuteQueryStringsAsString(query);
		if(result.equalsIgnoreCase("true"))
		{    String lotinfo=createLotInfo(xmlStr);
			 String q="tlsp:insertLotInfoForSameBin_MD('"+ndc.trim()+"','"+binno.trim()+"',"+lotinfo+")";
			 queryRunner.returnExecuteQueryStringsAsString(q); 
		} 
		else{
			String bininfo=createBinInfo(xmlStr);
			 String q="tlsp:insertBinInfoNodetoPedigreeBank_MD('"+ndc.trim()+"',"+bininfo+")";
			 queryRunner.returnExecuteQueryStringsAsString(q); 
		}
	} catch (PersistanceException e) {
	 
		e.printStackTrace();
	}
	 
}

private static void readPropertiesFile(String url){		
	
	try{
		System.out.println("Inside properties reading block *******");		
         
		FileInputStream fileinputstream = new FileInputStream(url+"/Props.properties");
		java.util.Properties properties = new java.util.Properties();
		properties.load(fileinputstream);
		
		System.out.println(properties.getProperty("eMail.From"));
		emailFrom = properties.getProperty("eMail.From");
		
		System.out.println(properties.getProperty("eMail.To"));
		emailTo = properties.getProperty("eMail.To");
		
		System.out.println(properties.getProperty("eMail.Subject"));
		emailSubject = properties.getProperty("eMail.Subject");
		
		System.out.println(properties.getProperty("eMail.SMTP"));
		emailSMTP = properties.getProperty("eMail.SMTP");
		
		System.out.println(properties.getProperty("eMail.Password"));
		emailPassword = properties.getProperty("eMail.Password");
		
		System.out.println(properties.getProperty("automaticUsecase.fileUrl"));
		fileUrl = properties.getProperty("automaticUsecase.fileUrl");
		
		System.out.println(properties.getProperty("automaticUsecase.errorUrl"));
		errorUrl = properties.getProperty("automaticUsecase.errorUrl");
		
		System.out.println(properties.getProperty("automaticUsecase.processedUrl"));
		processedUrl = properties.getProperty("automaticUsecase.processedUrl");
		
		System.out.println(properties.getProperty("signerId"));
		signerid = properties.getProperty("signerId");
		
		System.out.println(properties.getProperty("deaNumber"));
		deaNumber = properties.getProperty("deaNumber");	
		
		System.out.println(properties.getProperty("sourceRoutingCode"));
		sourceRoutingCode = properties.getProperty("sourceRoutingCode");
		
	}catch(IOException e){
		System.out.println("Exception is :"+e);
	}
	
}

private String createBinInfo(String xmlStr) throws PersistanceException {
	
	 
	String query="tlsp:createBinInfoForAB("+xmlStr+")";
	System.out.println("create bininfo"+query);
	String binInfoNode=queryRunner.returnExecuteQueryStringsAsString(query);
	// TODO Auto-generated method stub
	return binInfoNode;
}

private String createLotInfo(String xmlStr) throws PersistanceException {
	
	String lotInfoNode="";
	String query="tlsp:createLotInfoForAB("+xmlStr+")";
	System.out.println("create bininfo"+query);
	lotInfoNode=queryRunner.returnExecuteQueryStringsAsString(query);
 
	return lotInfoNode;
}

private boolean checkNDCExist(String xmlStr) throws PersistanceException {
	
	Node n = XMLUtil.parse(xmlStr);
	String query="tlsp:ndcExists_MD('"+XMLUtil.getValue(n,"NDC")+"')";
	System.out.println("check if NDC exists"+query);
	String result=queryRunner.returnExecuteQueryStringsAsString(query);
	if(result.equalsIgnoreCase("true"))
	{
		return true;
	}
	else{
		 return false;
	}
	
	 
}
private void copyfile(String source,String destination) throws Exception{
	
	FileInputStream  fis = null;
	 FileOutputStream fos = null;
		try
		{
			fis = new FileInputStream(source);
			fos = new FileOutputStream(destination);
			int x = fis.read();

			while(x != -1)
			{
				fos.write(x);
				x = fis.read();
			}
			fos.flush();
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			System.out.println("Filename incorrect");
			 
			return;
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e.getMessage());
			return;
		}
		finally
		{
			if(fos != null)
				fos.close();
			if(fis != null)
				fis.close();

		}	
}


public static Document createDomDocument() {
    try {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.newDocument();
        return doc;
    } catch (ParserConfigurationException e) {
    }
    return null;
}

private static void createPedigreeBankDocForAB(String xmlString) throws PersistanceException {
	// TODO Auto-generated method stub
	System.out.println("createPedigreeBankDocForAB");
	String n = mapToPedigreeBankDoc(xmlString); 
	String query="tlsp:InsertDocForAB("+n+")";
	String r=queryRunner.returnExecuteQueryStringsAsString(query);
}

private static String mapToPedigreeBankDoc(String xmlString) throws PersistanceException {
	 
	 String query="tlsp:InsertPedigreeBankDocForMD("+xmlString+")";
	 System.out.println("query for "+query);
	 String result= queryRunner.returnExecuteQueryStringsAsString(query);
	 System.out.println("result of pedbank"+result);
 
	return result;
}
private static String createflagfile() {

	TimeZone local = TimeZone.getDefault();
	
	String todayDate = sdf.format(new Date());
	System.out.println("***********Today's date is :"+todayDate); 
	
	String flagFile = "EPED"+todayDate;
	return flagFile;	
}
 
public void perform()   {
	
	try{
	String fname = createflagfile();
	String flagFile = fname+ "PM.FLG";
	String textFile1 = fname +"AM.TXT";
	String textFile2 = fname +"PM.TXT";
	/*String fileUrl = "C:/EPED_Files/";
	String errorUrl="C:/EPED_Error_Files";
	String processedUrl="C:/EPED_Processed_Files";
	*/
	 SetPath sp= new SetPath();
	
	String propVal = System.getProperty("com.rdta.pedigreebank.path");
	readPropertiesFile(propVal);
	System.out.println("FILE PATH"+propVal);
 	boolean exists = (new File(fileUrl+flagFile).exists());
    if (exists) {
        System.out.println(" File or directory exists");
        String f1=fileUrl + textFile1; 
    	String f2=fileUrl + textFile2;
    	boolean existsFile1= (new File(f1).exists());
    	boolean existsFile2= (new File(f2).exists());
    	if(existsFile1){
    		fileUrl=f1;
    		errorUrl=errorUrl+"/"+textFile1;
    		processedUrl=processedUrl+"/"+textFile1;
    	}
    	if(existsFile2){
    		fileUrl=f2;
    		errorUrl=errorUrl+"/"+textFile2;
    		processedUrl=processedUrl+"/"+textFile2;
    		
    	}
        FindErrorsInTextFile fetf= new FindErrorsInTextFile(fileUrl);
		List list= fetf.createRecordsForFile();
		
		int errorcount=Integer.parseInt(list.get(2).toString());
         if(errorcount>0 ){
        	 String errorxml=list.get(0).toString();
        	 System.out.println("Null values  in the file");
        	 System.out.println(errorxml);
	 			
        	 SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
						"ePharma Morris & Dickson Error",errorxml, emailFrom, emailPassword );
				System.exit(0);
      	     
         }  
    	AutomaticCaseForMD cfc= new AutomaticCaseForMD(fileUrl);
    	Map m=cfc.prepareHeaderMapForAB();
    	
        String line=cfc.getNextLine();
        StringBuffer temp = new StringBuffer("");
        int count =0;
        while(line!=null){
        
         if((cfc.checkforABorAP(line).equalsIgnoreCase("AB"))){
        	
             String xmlStr = cfc.createXML(m,line);
        	   System.out.println("AB"+cfc.getLineCount() + "\n"+ xmlStr);
        	   if(cfc.checkNDCExist(xmlStr))
        	   {
        		   cfc.updatePedigreeBankForAB(xmlStr);
        		   
        	   }
        	   else{
        		   count= count+1;
        		 
        		   cfc.createPedigreeBankDocForAB(xmlStr);   
        		   
        	   }
        	   
        	   
        }
         if((cfc.checkforABorAP(line).equalsIgnoreCase("AP"))){
        	 System.out.println("AP"+cfc.getLineCount());
           Map ap= cfc.prepareMapForAP();
           String xmlStr = cfc.createXMLForAP(ap,"pedship",line);
      	   System.out.println("AP");
      	   String res[]=line.split("\\|");
      	   if(invoice.equals("start")||res[4].equals(invoice))
   		    {
      		   if(invoice.equals("start"))
      		   {
      			 invoice = res[4];	   
      		   }
      		 System.out.println("Invoice"+res[4]);
   		    temp.append(xmlStr);
   		   
   	       }
      	    else{
      		 
      		  StringBuffer resxml=new StringBuffer("<root><pedshipData>");
      		  resxml.append(temp);
      		  resxml.append("</pedshipData></root>");
      		  System.out.println("PEDSHIPDATA FOR EACH INVOICE"+resxml.toString());
      		 StringBuffer buff = new StringBuffer();
      	 
             buff.append("tlsp:PEDSHIP_MD("+resxml.toString().replaceAll("'","&apos;")+","+signerid+","+sourceRoutingCode+","+deaNumber+")");
             System.out.println(buff.toString());
             temp= new StringBuffer("");
      		 temp.append(xmlStr);
      		System.out.println("Invoice"+res[4]);
      	     invoice = res[4];
      	   List envIds = queryRunner.returnExecuteQueryStrings(buff.toString());
  		 
           
   		for(int k=0;k<envIds.size();k++){
   			String envId = envIds.get(k).toString();
   			
   			StringBuffer buff1 = new StringBuffer();
   			buff1.append("for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope[*:serialNumber = '"+ envId + "'] ");
   			buff1.append("return data($i/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber)");
   			List pedId = queryRunner.returnExecuteQueryStrings(buff1.toString());
   			
   			 
   			for (int i = 0; i < pedId.size(); i++) {
   			 
   				System.out.println("Shipped Pedigree PedigreeIDs :"	+ pedId.get(i).toString());
   				String signQuery = "tlsp:pedigreeLevelSignature('"+envId+"','"+pedId.get(i).toString()+"')";
   				System.out.println("Query for creating signature : "+signQuery);
   				queryRunner.executeQuery(signQuery);
   			
   			}
   		}
      		   
      	   }
      	   
      	   
      }
        
       line=cfc.getNextLine();
    }
      StringBuffer resxml=new StringBuffer("<root><pedshipData>");
  	  resxml.append(temp);
  	  resxml.append("</pedshipData></root>");
  	  StringBuffer buff = new StringBuffer();
      buff.append("tlsp:PEDSHIP_MD("+resxml.toString().replaceAll("'","&apos;")+")");
      System.out.println(buff.toString());
      List envIds = queryRunner.returnExecuteQueryStrings(buff.toString());
		 
      
		for(int k=0;k<envIds.size();k++){
			String envId = envIds.get(k).toString();
			
			StringBuffer buff1 = new StringBuffer();
			buff1.append("for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope[*:serialNumber = '"+ envId + "'] ");
			buff1.append("return data($i/*:pedigree/*:shippedPedigree/*:documentInfo/*:serialNumber)");
			List pedId = queryRunner.returnExecuteQueryStrings(buff1.toString());
			
			 
			for (int i = 0; i < pedId.size(); i++) {
			 
				System.out.println("Shipped Pedigree PedigreeIDs :"	+ pedId.get(i).toString());
				String signQuery = "tlsp:pedigreeLevelSignature('"+envId+"','"+pedId.get(i).toString()+"')";
				System.out.println("Query for creating signature : "+signQuery);
				queryRunner.executeQuery(signQuery);
			
			}
		}
       cfc.copyfile(fileUrl,processedUrl);
       
       
       System.out.println("No of Pedigree Bank Docs created"+count);
       File f = new File(fileUrl);
		f.delete();  
    	}
   
    
    else {
   	 System.out.println(" File or directory does not exist");
   	SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
			"ePharma Morris & Dickson Error","File or directory does not exist", emailFrom, emailPassword );
       
   }
        
	}catch(Exception e){
		
		try {
			 
			e.printStackTrace();
			SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
					"ePharma Morris & Dickson Error",e.getMessage(), emailFrom, emailPassword );
			System.exit(0);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
    }
 

public static void main(String args[])
{
    
    AutomaticCaseForMD m = new AutomaticCaseForMD ();
    m.perform();
}

}
