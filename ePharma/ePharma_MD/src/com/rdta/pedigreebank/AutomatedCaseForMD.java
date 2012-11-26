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
import com.rdta.epharma.pedexchange.PedEnvTransmissionHandler;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
 
import java.util.ArrayList;
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
 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.persistence.pool.TLConnectionPool;
import com.rdta.commons.xml.XMLUtil;

import com.rdta.eag.epharma.util.SendDHFormEmail;
import com.rdta.tlapi.xql.Connection;
import com.rdta.util.io.BufferedWriter;


public class AutomatedCaseForMD {

	private String filePath;
	private Map sourceHeaderMap = new LinkedHashMap(); 
	private static BufferedReader buffReader;
	private int lineCount = 0;
	private int colCount = 0;
	private static String invoice = "start";
	static final SimpleDateFormat sdf = new SimpleDateFormat("MMddyy");
	static String emailFrom = null;
	static String emailTo = null;
	static String emailSubject = null;
	static String emailSMTP = null;
	static String emailPassword = null; 
	static String emailUserName = null;
	static String fileUrl = null;
	static String errorUrl = null;
	static String processedUrl = null;
	static String sourceRoutingCode = null;
	static String destinationRoutingCode = null;
	static String signerid = null;
	static String deaNumber = null;
    static String fileName = null;
	static String flagUrl = null;
	static String currentLine =null;
	static Connection conn = null;
	static List linesForAp= new ArrayList();
	private boolean isReaderStatusClosed = false;
	
	 
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	static InsertDocToDB ins = new InsertDocToDB();
	static AutomatedCaseForMD instance = new AutomatedCaseForMD();
	private static Log log=LogFactory.getLog(AutomatedCaseForMD.class);
	
	public AutomatedCaseForMD(String filePath) throws Exception {
	this.filePath = filePath;
	buffReader = new BufferedReader(new FileReader(filePath));
	}
	
	public AutomatedCaseForMD(String filePath,int lineNum ) throws Exception {
		this(filePath);
		skipNumberOfLines(lineNum);
	}
	
	
	public AutomatedCaseForMD() {
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
			currentLine= line;
			
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
		log.info(line);
		if(line != null) {
			String[] result = line.split("\\|");
			colCount = result.length - 1;
			String token = null;
			for(int i = 0; i<result.length; i++) {
				token = result[i];
				log.info("Header Name: "+i + token);
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
		log.info("BINLOC"+binno);
		
		String query="tlsp:BinNumberExists_MD('"+ndc.trim()+"','"+binno.trim()+"')";
		log.info("check if NDC exists"+query);
		String result=queryRunner.returnExecuteQueryStringsAsStringNew(query,conn);
		if(result.equalsIgnoreCase("true"))
		{    String lotinfo=createLotInfo(xmlStr);
			 String q="tlsp:insertLotInfoForSameBin_MD('"+ndc.trim()+"','"+binno.trim()+"',"+lotinfo+")";
			 queryRunner.returnExecuteQueryStringsAsStringNew(q,conn); 
		} 
		else{
			String bininfo=createBinInfo(xmlStr);
			 String q="tlsp:insertBinInfoNodetoPedigreeBank_MD('"+ndc.trim()+"',"+bininfo+")";
			 queryRunner.returnExecuteQueryStringsAsStringNew(q,conn); 
		}
	} catch (PersistanceException e) {
		try{
		conn.rollback();
		log.error("Error in AutomatedCaseForMD class : "+e);
		// create new File
		createReProcessFile();
		
		SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
					"TL Exception",e.getMessage(), emailUserName, emailPassword );
		}catch(Exception ex){} 
		
	} 
	 
}

private void createReProcessFile(){
	
	FileInputStream  fis = null;
	FileOutputStream fos = null;
	DataOutputStream dos = null;
		try
		{
			//fis = new FileInputStream(source);
			SimpleDateFormat df = new SimpleDateFormat();
			
			df.applyPattern("yyyy-MM-dd");
			String tmDate = df.format(new java.util.Date());
			df.applyPattern("hh:mm:ss");
			String tmTime = df.format(new java.util.Date());
			String CreatedDate = tmDate + "T" + tmTime;
			String fileName = "C:/Re_Process/" +CreatedDate+"_ReProcess.txt";
			log.info("Reprocess file : "+fileName);
			fos = new FileOutputStream("C:/Re_Process/" +CreatedDate+"_ReProcess.txt");
			dos = new DataOutputStream(fos);
			String line= currentLine;
			log.info("lineNumber  : "+lineCount);
			while(line != null)
			{
				dos.write(line.getBytes());
				dos.writeBytes("\n");
				line= getNextLine();
				
			}
			fos.flush();
		}
		 catch(Exception ex){
			 
		 }
		 
		/*finally
		{
			if(fos != null)
				fos.close();
			if(fis != null)
				fis.close();

		}	*/
}
private void createReProcessFile(List lines){
	
	FileOutputStream fos = null;
	DataOutputStream dos = null;
		try
		{
			SimpleDateFormat df = new SimpleDateFormat();
			df.applyPattern("yyyy-MM-dd");
			String tmDate = df.format(new java.util.Date());
			df.applyPattern("HHmmss");
			String tmTime = df.format(new java.util.Date());
			String CreatedDate = tmDate + "_" + tmTime;
			String fileName1 = "C:/Re_Process/" +CreatedDate+"_ReProcess.txt";
			log.info("Reprocess file : "+fileName1);
			fos = new FileOutputStream("C:/Re_Process/" +CreatedDate+"_ReProcess.txt");
			dos = new DataOutputStream(fos);
			String line= currentLine;
			
			 for(int t=0;t<lines.size();t++){
				 
					dos.write(lines.get(t).toString().getBytes());
					dos.writeBytes("\n");
			 }
			while(line != null)
			{
				dos.write(line.getBytes());
				dos.writeBytes("\n");
				line= getNextLine();
				
			}
			fos.flush();
		}
		 catch(Exception ex){
			 log.error("Exception : "+ex);
		 }
		 
		finally
		{try{
			if(fos != null)
				fos.close();
			if(dos != null)
				dos.close();
		}catch(Exception ex){
			log.error("Exception : "+ex);
		}
		}	
}


private static void readPropertiesFile(String url){		
	
	try{
		log.info("Inside properties reading block *******");		
         
		FileInputStream fileinputstream = new FileInputStream(url+"/Props.properties");
		java.util.Properties properties = new java.util.Properties();
		properties.load(fileinputstream);
		
		log.info(properties.getProperty("eMail.From"));
		emailFrom = properties.getProperty("eMail.From");
		
		log.info(properties.getProperty("eMail.To"));
		emailTo = properties.getProperty("eMail.To");
		
		log.info(properties.getProperty("eMail.Subject"));
		emailSubject = properties.getProperty("eMail.Subject");
		
		log.info(properties.getProperty("eMail.SMTP"));
		emailSMTP = properties.getProperty("eMail.SMTP");
		
		log.info(properties.getProperty("eMail.Password"));
		emailPassword = properties.getProperty("eMail.Password");
		
		log.info(properties.getProperty("eMail.UserName"));
		emailUserName = properties.getProperty("eMail.UserName");
		
		log.info(properties.getProperty("automaticUsecase.fileUrl"));
		fileUrl = properties.getProperty("automaticUsecase.fileUrl");
		
		log.info(properties.getProperty("automaticUsecase.errorUrl"));
		errorUrl = properties.getProperty("automaticUsecase.errorUrl");
		
		log.info(properties.getProperty("automaticUsecase.processedUrl"));
		processedUrl = properties.getProperty("automaticUsecase.processedUrl");
		
		log.info(properties.getProperty("signerId"));
		signerid = properties.getProperty("signerId");
		
		log.info(properties.getProperty("deaNumber"));
		deaNumber = properties.getProperty("deaNumber");	
		
		log.info(properties.getProperty("sourceRoutingCode"));
		sourceRoutingCode = properties.getProperty("sourceRoutingCode");
		
	}catch(IOException e){
		log.error("Exception is :"+e);
	}
	
}

private String createBinInfo(String xmlStr) throws PersistanceException {
	
	 
	String query="tlsp:createBinInfoForAB("+xmlStr+")";
	log.info("create bininfo"+query);
	String binInfoNode=queryRunner.returnExecuteQueryStringsAsStringNew(query,conn);
	// TODO Auto-generated method stub
	return binInfoNode;
}

private String createLotInfo(String xmlStr) throws PersistanceException {
	
	String lotInfoNode="";
	String query="tlsp:createLotInfoForAB("+xmlStr+")";
	log.info("create bininfo"+query);
	lotInfoNode=queryRunner.returnExecuteQueryStringsAsStringNew(query,conn);
 
	return lotInfoNode;
}

private boolean checkNDCExist(String xmlStr) throws PersistanceException {
	
	Node n = XMLUtil.parse(xmlStr);
	String query="tlsp:ndcExists_MD('"+XMLUtil.getValue(n,"NDC")+"')";
	log.info("check if NDC exists"+query);
	String result=queryRunner.returnExecuteQueryStringsAsStringNew(query,conn);
	if(result.equalsIgnoreCase("true"))
	{
		return true;
	}
	else{
		 return false;
	}
	
	 
}

private String pedigreeBankIdForNDC(String xmlStr) throws PersistanceException {
	
	Node n = XMLUtil.parse(xmlStr);
	String query="tlsp:ndcExists_MDNew('"+XMLUtil.getValue(n,"NDC")+"')";
	log.info("check if NDC exists"+query);
	String result=queryRunner.returnExecuteQueryStringsAsStringNew(query,conn);
	 return result;
	
	 
}
private static void  copyfile(String source,String destination) throws Exception{
	
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
			log.error("Filename incorrect");
			 
			return;
		}
		catch(FileNotFoundException e)
		{
			log.error(e.getMessage());
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

private String createPedigreeBankDocForAB(String xmlString) {
	// TODO Auto-generated method stub
	String r="";
	try{
	log.info("createPedigreeBankDocForAB");
	String n = mapToPedigreeBankDoc(xmlString); 
	
	
	String query="tlsp:InsertDocForAB("+n+")";
	 r=queryRunner.returnExecuteQueryStringsAsStringNew(query,conn);
	
	log.info("result insert"+r);
	}catch(PersistanceException e){
		try{
			conn.rollback();
			log.error("Error in AutomatedCaseForMD class : "+e);
			// create new File
			createReProcessFile();
			
			SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
						"TL Exception",e.getMessage(), emailUserName, emailPassword );
			}catch(Exception ex){} 
			
		
	}catch(Exception ex){
		
	}
	return r; 
}

private static String mapToPedigreeBankDoc(String xmlString) throws PersistanceException {
	 
	 String query="tlsp:InsertPedigreeBankDocForMD("+xmlString+")";
	 log.info("query for "+query);
	 String result= queryRunner.returnExecuteQueryStringsAsStringNew(query,conn);
	 log.info("result of pedbank"+result);
 
	return result;
}
private static String createflagfile() {

	TimeZone local = TimeZone.getDefault();
	
	String todayDate = sdf.format(new Date());
	log.info("***********Today's date is :"+todayDate); 
	
	String flagFile = "EPED"+todayDate;
	return flagFile;	
}
 
public static void main(String[] args)   {
	
	try{
	String fname = createflagfile();
	
	conn = TLConnectionPool.getTLConnectionPool().borrowConnection();
	conn.setAutoCommit(false);

	String flagFile = fname+ "PM.FLG";
	String textFile1 = fname +"AM.TXT";
	String textFile2 = fname +"PM.TXT";
	/*String fileUrl = "C:/EPED_Files/";
	String errorUrl="C:/EPED_Error_Files";
	String processedUrl="C:/EPED_Processed_Files";
	*/
	String flagErrorUrl="";
	String flagProcessedUrl="";
	List envIds = new ArrayList(); 
	
	SetPath sp= new SetPath();
	
	String propVal = System.getProperty("com.rdta.pedigreebank.path");
	String propUrl ="";
	String arry[]=propVal.split("%20");
	for(int i=0;i<arry.length;i++){
		if(i==arry.length-1)
		{
			propUrl = propUrl + arry[i];
		}
		else{
			propUrl = propUrl + arry[i]+" ";
			
		}
		
		
	}
	
	log.info(propUrl);
	readPropertiesFile(propUrl);
 	//boolean exists = (new File(fileUrl+flagFile).exists());
	boolean exists = false;
	File dir = new File(fileUrl);
	 if (dir.isDirectory()) {
         String[] children = dir.list();
         log.info("NO of Files"+ children.length);
         for (int i=0; i<children.length; i++) {
        	 if(children[i].endsWith(".FLG")){
        		 log.info("Flag File is"+children[i]);	
        		 exists=true;
        		 flagUrl= fileUrl+children[i];
        		 log.info("Flagurl File is"+flagUrl);
        		 flagErrorUrl=flagErrorUrl+children[i];
        		 flagProcessedUrl=flagProcessedUrl+children[i];
        	 }
        	 if(children[i].endsWith(".txt")||children[i].endsWith(".TXT")){
        		 log.info("Text File is"+children[i]);	
        		 textFile1=children[i];
        		 fileName = textFile1;
        	 }
        
         }
     } else {
    	 log.info("NO TEXT FILE");
     }

 	
    if (exists) {
        log.info(" File or directory exists");
        String f1=fileUrl + textFile1; 
    	//String f2=fileUrl + textFile2;
    	boolean existsFile1= (new File(f1).exists());
    	//boolean existsFile2= (new File(f2).exists());
    	if(existsFile1){
    		fileUrl=f1;
    		errorUrl=errorUrl+"/"+textFile1;
    		processedUrl=processedUrl+"/"+textFile1;
    	}
    	/*if(existsFile2){
    		fileUrl=f2;
    		errorUrl=errorUrl+"/"+textFile2;
    		processedUrl=processedUrl+"/"+textFile2;
    		
    	}*/
       /* FindErrorsInTextFile fetf= new FindErrorsInTextFile(fileUrl);
		List list= fetf.createRecordsForFile();
		
		int errorcount=Integer.parseInt(list.get(2).toString());
         if(errorcount>0 ){
        	 String errorxml=list.get(0).toString();
        	 log.info("Null values  in the file");
        	 log.info(errorxml);
	 		
		      copyfile(fileUrl,errorUrl);
		      copyfile(flagUrl,flagErrorUrl);
		      File f= new File(fileUrl);
		      f.delete();
		      
		      File flag = new File(flagUrl);
		      flag.delete();
		      SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
						"ePharma Morris & Dickson Error",errorxml, emailUserName, emailPassword );
				System.exit(0);
		      
      	     
         }  */
    	AutomatedCaseForMD cfc= new AutomatedCaseForMD(fileUrl);
    	Map m=cfc.prepareHeaderMapForAB();
    	
        String line=cfc.getNextLine();
        StringBuffer temp = new StringBuffer("");
        int count =0;
        StringBuffer pedBank = new StringBuffer();
        pedBank.append("<PedigreeBank>");
        while(line!=null){
        
         if((cfc.checkforABorAP(line).equalsIgnoreCase("AB"))){
        	
             String xmlStr = cfc.createXML(m,line);
        	   log.info("AB"+cfc.getLineCount() + "\n"+ xmlStr);
        	   String r = cfc.pedigreeBankIdForNDC(xmlStr);
        	   log.info("new result"+r);
        	   if(r != null && !r.equals(""))
        	   {
        		   cfc.updatePedigreeBankForAB(xmlStr);
        		   pedBank.append("<PedigreeBankId>");
        		   pedBank.append(r);
        		   pedBank.append("</PedigreeBankId>");
        	   }
        	   else{
        		   count= count+1;
        		 
        		 String newId= cfc.createPedigreeBankDocForAB(xmlStr);   
        		 pedBank.append("<PedigreeBankId>");
        		 pedBank.append(newId);
      		   	pedBank.append("</PedigreeBankId>");
        		   
        	   }
        	   conn.commit();
        	  
        }
         if((cfc.checkforABorAP(line).equalsIgnoreCase("AP"))){
        	 log.info("AP"+cfc.getLineCount());
           Map ap= cfc.prepareMapForAP();
           String xmlStr = cfc.createXMLForAP(ap,"pedship",line);
      	   log.info("AP");
      	   String res[]=line.split("\\|");
      	   if(invoice.equals("start")||res[4].equals(invoice))
   		    {
      		   if(invoice.equals("start"))
      		   {
      			 invoice = res[4];	   
      		   }
      		 log.info("Invoice"+res[4]);
   		    temp.append(xmlStr);
   		    linesForAp.add(line);
   	       }
      	    else{
      		 
      		  StringBuffer resxml=new StringBuffer("<root><pedshipData>");
      		  resxml.append(temp);
      		  resxml.append("</pedshipData></root>");
      		  log.info("PEDSHIPDATA FOR EACH INVOICE"+resxml.toString());
      		 StringBuffer buff = new StringBuffer();
      	     
      		 String[] email = {emailFrom,emailTo,emailSMTP,emailUserName,emailPassword};
      		 //buff.append("tlsp:PEDSHIP_MD("+resxml.toString().replaceAll("'","&apos;")+",'"+signerid+"','"+deaNumber+"','"+sourceRoutingCode+"')");
      		 String envId = InsertDocToDB.CreatePedigree(resxml.toString().replaceAll("'","&apos;"),signerid,deaNumber,sourceRoutingCode,email,fileName,conn);
      		 envIds.add(envId);
      		 linesForAp.clear();
      		 linesForAp.add(line);
             temp= new StringBuffer("");
      		 temp.append(xmlStr);
      		log.info("Invoice"+res[4]);
      	     invoice = res[4];
      	       		   
      	   }
      	  
      }
        
       line=cfc.getNextLine();
    }
      log.info("PEdigreeebank ids"+pedBank);
        
      StringBuffer resxml=new StringBuffer("<root><pedshipData>");
  	  resxml.append(temp);
  	  resxml.append("</pedshipData></root>");
  	  StringBuffer buff = new StringBuffer();
  	  
  	  String[] email = {emailFrom,emailTo,emailSMTP,emailUserName,emailPassword};
  	  String envId = InsertDocToDB.CreatePedigree(resxml.toString().replaceAll("'","&apos;"),signerid,deaNumber,sourceRoutingCode,email,fileName,conn);
      envIds.add(envId);
      log.info(buff.toString());
     
        copyfile(fileUrl,processedUrl);
        copyfile(flagUrl,flagProcessedUrl);
       
       log.info("No of Pedigree Bank Docs created"+count);
      // File f = new File(fileUrl);
		//f.delete();  
	 //	File flag = new File(flagUrl);
		//flag.delete();  
       pedBank.append("</PedigreeBank>");
       log.info("No of envIds created : "+envIds);
       StringBuffer buf =  new StringBuffer();
       buf.append("<EnvelopeIds>");
       for(int i=0; i<envIds.size();i++){
    	   buf.append("<envelopeId>");
    	   buf.append(envIds.get(i).toString());
    	   buf.append("</envelopeId>");
       }
       buf.append("</EnvelopeIds>");
       
       queryRunner.returnExecuteQueryStringsAsStringNew("tlsp:InsertPedigreeBankAudit("+pedBank.toString()+",'"+fileName+"')",conn);
       queryRunner.returnExecuteQueryStringsAsStringNew("tlsp:InsertProcessedEnvelope("+buf.toString()+",'"+fileName+"')",conn);
       //PedEnvTransmissionHandler.receivePedigreeEnvelopes(envIds);
       conn.commit();
       TLConnectionPool.getTLConnectionPool().returnConnection(conn);	
    }
   
    
    else {
   	log.info(" File or directory does not exist");
   	SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
			"ePharma Morris & Dickson Error","File or directory does not exist", emailUserName, emailPassword );
       
   }
        
	}catch(PersistanceException ex){
		try{
			 conn.rollback();
			 instance.createReProcessFile(linesForAp);
			 
			 log.error("Error in AutomatedCaseForMD class :"+ex);
			 SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
						"TL Exception",ex.getMessage(), emailUserName, emailPassword );
			 }catch(Exception e){
				 
			 }
	}
	
	catch(Exception e){
		
		try {
			conn.rollback();
			instance.createReProcessFile(linesForAp);
			log.error("Error in AutomatedCaseForMD class :"+e);
			SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
					"GenericException",e.getMessage(), emailUserName, emailPassword );
			System.exit(0);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
    }
 
}
