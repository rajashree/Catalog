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

package com.rdta.eag.epharma.activity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.text.ParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.rdta.eag.epharma.commons.persistence.PersistanceException;
import com.rdta.eag.epharma.commons.persistence.QueryRunner;
import com.rdta.eag.epharma.commons.persistence.QueryRunnerFactory;
import com.rdta.eag.epharma.commons.persistence.pool.TLConnectionPool;

import com.rdta.eag.epharma.commons.xml.XMLUtil;
import com.rdta.eag.epharma.util.SendDHFormEmail;
import com.rdta.eag.epharma.pedexchange.PedEnvTransmissionHandler;

import com.rdta.eag.epharma.job.Job;
import com.rdta.tlapi.xql.Connection;

public class DropShip implements Job {
	private static final String PROPS_CONFIG = "Props.properties";

	private String filePath;
	private static BufferedReader buffReader;
	static final SimpleDateFormat sdf = new SimpleDateFormat("MMddyy");
	private static String poNumber = "start";
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	private static Log log = LogFactory.getLog(DropShip.class);
	private boolean isReaderStatusClosed = false;
	private int lineCount = 0;
	private int colCount = 0;

	static String emailFrom = null;
	static String emailTo = null;
	static String emailSubject = null;
	static String emailSMTP = null;
	static String emailUserName = null;
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
	static String errorSubject = null;
	static String flagFile = null;
	static String fileName = null;

	static String currentLine =null;
	static Connection conn = null;
	static List linesForAp= new ArrayList();
	static DropShip instance = new DropShip();

	public DropShip(){

	}

	public String getName() {
		return "MonitorActivityJobPedRcv";
	}

	// A constructor which takes the file path as an argument
	public DropShip(String filePath) throws Exception {
		this.filePath = filePath;
		buffReader = new BufferedReader(new FileReader(filePath));
	}

	// A method that creates and returns a flag file on a daily basis
	private static String createflagfile() {

		TimeZone local = TimeZone.getDefault();

		String todayDate = sdf.format(new Date());
		log.info("***********Today's date is :"+todayDate);
		log.info("***********Today's date is :"+todayDate);

		String flagFile = "EPED"+todayDate;
		return flagFile;
	}

	// Preparing the headers
	private Map prepareHeaderMapForDropShip() throws Exception {

		 Map ab=  new LinkedHashMap();
	 	 ab.put(new Integer(0),"PickType");
		 ab.put(new Integer(1),"PONumber");
		 ab.put(new Integer(2),"PODate");
		 ab.put(new Integer(3),"ProductCode");
		 ab.put(new Integer(4),"ManufacturerName");
		 ab.put(new Integer(5),"DrugName");
		 ab.put(new Integer(6),"Dosage");
		 ab.put(new Integer(7),"Strength");
		 ab.put(new Integer(8),"ContainerSize");
		 ab.put(new Integer(9),"LotNo");
		 ab.put(new Integer(10),"ExpDate");
		 ab.put(new Integer(11),"Quantity");
		 ab.put(new Integer(12),"ManuAdd1");
		 ab.put(new Integer(13),"ManuAdd2");
		 ab.put(new Integer(14),"ManuCity");
		 ab.put(new Integer(15),"ManuState");
		 ab.put(new Integer(16),"ManuZip");
		 ab.put(new Integer(17),"ManuCountry");
		 ab.put(new Integer(18),"ManuDEA");
		 ab.put(new Integer(19),"ManContactName");
		 ab.put(new Integer(20),"ManContactEmail");
		 ab.put(new Integer(21),"ManContactPhone");
		 ab.put(new Integer(22),"CustPONumber");
		 ab.put(new Integer(23),"CustPODate");
		 ab.put(new Integer(24),"CustContactname");
		 ab.put(new Integer(25),"Custname");
		 ab.put(new Integer(26),"CustshipAdd1");
		 ab.put(new Integer(27),"CustShipAdd2");
		 ab.put(new Integer(28),"CustShiAddCity");
		 ab.put(new Integer(29),"CustShipAddState");
		 ab.put(new Integer(30),"CustShipzip");
		 ab.put(new Integer(31),"CustShipcountry");
		 ab.put(new Integer(32),"CustDEA");
		 ab.put(new Integer(33),"CustName");
		 ab.put(new Integer(34),"CustBillAdd1");
		 ab.put(new Integer(35),"CustBillAdd2");
		 ab.put(new Integer(36),"CustBillAddCity");
		 ab.put(new Integer(37),"CustBillState");
		 ab.put(new Integer(38),"CustBillZip");
		 ab.put(new Integer(39),"CustBillCountry");


		 return ab;
	}

	// method to read lines from the text file
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
	/*
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
*/
	// Method to close the stream
	public void closeStream()  {
		try {
			isReaderStatusClosed = true;
			if(buffReader != null) {
				buffReader.close();
			}
		} catch(Exception e) { }
	}

	/**
	 * Returns number of lines read as of now from the input stream.
	 *
	 * @return int
	 */
	public int getLineCount() {

		return lineCount;
	}

	public String addError(String RecId,int lineNo, String colName,String msg) throws Exception{
		String er="<Error><RecordIdentifier>"+RecId+"</RecordIdentifier><Message>"+colName+" at line "+lineNo+" is "+msg+"</Message></Error>";
		return  er;
	}
	public String addError(String RecId,int lineNo, String colName) throws Exception{
		String er="<Error><RecordIdentifier>"+RecId+"</RecordIdentifier><Message>"+colName+" at line "+lineNo+" is null</Message></Error>";
		return  er;
	}

	public static Document createDomDocument(){
	    try {
	        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	        Document doc = builder.newDocument();
	        return doc;
	    } catch (ParserConfigurationException e) {
	    }
	    return null;
	}

	public List createRecordsForFile() throws Exception{
	    String t ="<root>";
	    StringBuffer errors=new StringBuffer("<Errors>");
	    int errorcount=0;
	    String line=getNextLine();
	    String pedShipXML="<root></root>";
      	Node pedShiprootNode=XMLUtil.parse(pedShipXML);
      	Document doc = createDomDocument();
       	Element element = doc.createElement("root");
        doc.appendChild(element);
         while(line !=null){
	    	String res[]=line.split("\\|");

	    	if(res[0].equalsIgnoreCase("DROP SHIP")){
	    		log.info("DROPSHIP");

	    		if(res.length!=40){
	    	   		errorcount=errorcount+1;
	            	errors.append(addError("DS",getLineCount(),"Number of Columns","less"));


	         	}
	           else{
	    		Map mp = prepareHeaderMapForDropShip();
	    		for (Iterator it=mp.keySet().iterator(); it.hasNext(); ) {
	    			Object key = it.next();
	    			Object value = mp.get(key);
	    			int i = Integer.parseInt(key.toString());
	    			 res[i]=res[i].trim();
	    			 if(i==11)
	    		        {
	    		        	try{


	    			        	int Quantity = Integer.parseInt(res[i]);


	    			        	if(Quantity<0)
	    			        	{
	    			        		errorcount=errorcount+1;
	    				        	errors.append(addError("DS",getLineCount(),(String)value,"not integer"));
	    			        	}

	    			        	}
	    			        	catch (NumberFormatException e) {
	    			    			// TODO Auto-generated catch block
	    			    			errorcount=errorcount+1;
	    			    			errors.append(addError("DS",getLineCount(),(String)value,"not Integer"));
	    			    		}
	    		        }
	    			 if(i==2||i==10 ||i==23){

	    		        	String date = res[i];

	    		    		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	    		    		try {
	    		    			if (date.length() == 10) {
	    		    				format.setLenient(false);

	    		    				format.applyPattern("yyyy-MM-dd");
	    		    				Date d = format.parse(date);
	    		    				log.info("Correct DATE ");
	    		    			}

	    		    			else {
	    		    				log.info("Wrong DATE ");
	    		    				String s ="Wrong Date Format";
	    		    				errorcount=errorcount+1;
	    		    	        	errors.append(addError("DS",getLineCount(),(String)value,"Wrong Date Format(Correct Format is YYYY-MM-DD)"));
	    		    			}
	    		    		} catch (ParseException e) {
	    		    			// TODO Auto-generated catch block
	    		    			errorcount=errorcount+1;
	    		    			errors.append(addError("DS",getLineCount(),(String)value,"Wrong Date Format(Correct Format is YYYY-MM-DD)"));

	    		    		}
	    		        }


	    			if(res[i].equalsIgnoreCase("")|| res[i]==null){
	    				errorcount=errorcount+1;
	    				errors.append(addError("DS",getLineCount(),(String)value));
	    			}
	    		}
	    	}
	    	}
	    	line= getNextLine();
	    }
	    errors.append("</Errors>");
	    String E=errors.toString();
	    t=t+"</root>" ;
	    List list = new ArrayList();
	    list.add(E);
	    list.add(t);
	    list.add(""+errorcount);
	    return list;
	}

	private String checkforDS(String line) throws Exception {
		// TODO Auto-generated method stub
		if(line!=null){
			String res[]=line.split("\\|");

			if( res[0]!=null && res[0].equalsIgnoreCase("DROP SHIP")){
				return "DROP SHIP";
			}
		}
		return "NA";

	}

	public String createXMLDS(Map m, String line) throws Exception{

		String t="";
		if(line !=null){
			t=t+"<DS>";
			String res[]=line.split("\\|");

			Map mp = m;
			for (Iterator it = mp.keySet().iterator(); it.hasNext(); ) {
				Object key = it.next();
				Object value = mp.get(key);
				int i = Integer.parseInt(key.toString());

				if(i<res.length && res[i]!=null){
					t =t +"<"+value+">"+res[i]+"</"+value+">";
				}else{
					t =t +"<"+value+"/>" ;
				}
			}
			t =t +"</DS>";
		}
		return t ;
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
				log.info("Filename incorrect");
				log.info("Filename incorrect");

				return;
			}
			catch(FileNotFoundException e)
			{
				log.info(e.getMessage());
				log.info(e.getMessage());
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


	private void excuteDS(String temp) throws Exception {

		List envIds = new ArrayList();
		StringBuffer resxml=new StringBuffer("<DropShip>");

		resxml.append(temp);
		resxml.append("</DropShip>");
		log.info("DS FOR EACH PO for only one PONumber****"+temp.toString());

		//StringBuffer buff = new StringBuffer();
		String inputStr = resxml.toString().replaceAll("&","&amp;");
		inputStr = inputStr.replaceAll("'","&apos;");

		String[] email = {emailFrom,emailTo,emailSMTP,emailUserName,emailPassword};

		String envId = InsertDocToDB.CreatePedigreeEnvelopeForDropShip(inputStr, signerid, deaNumber, sourceRoutingCode, email, fileName, conn);
		log.info("Envelope Id after inserting is :"+envId);
		envIds.add(envId);
		PedEnvTransmissionHandler.receivePedigreeEnvelopes(envIds);

	}


	private void readPropertiesFile(){

		try{
			log.info("Inside properties reading block *******");

			InputStream inputstream = getClass().getResourceAsStream(PROPS_CONFIG);

			java.util.Properties properties = new java.util.Properties();
			properties.load(inputstream);

			log.info(properties.getProperty("eMail.From"));
			emailFrom = properties.getProperty("eMail.From");

			log.info(properties.getProperty("eMail.To"));
			emailTo = properties.getProperty("eMail.To");

			log.info(properties.getProperty("eMail.Subject"));
			emailSubject = properties.getProperty("eMail.Subject");

			log.info(properties.getProperty("eMail.SMTP"));
			emailSMTP = properties.getProperty("eMail.SMTP");

			log.info(properties.getProperty("eMail.UserName"));
			emailUserName = properties.getProperty("eMail.UserName");

			log.info(properties.getProperty("eMail.Password"));
			emailPassword = properties.getProperty("eMail.Password");

			log.info(properties.getProperty("dropShip.fileUrl"));
			fileUrl = properties.getProperty("dropShip.fileUrl");

			log.info(properties.getProperty("dropShip.errorUrl"));
			errorUrl = properties.getProperty("dropShip.errorUrl");

			log.info(properties.getProperty("dropShip.processedUrl"));
			processedUrl = properties.getProperty("dropShip.processedUrl");

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

	private void createReProcessFile(){

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
			String fileName = "C:/Re_Process_DS/" +CreatedDate+"_ReProcess.txt";
			log.info("Reprocess file : "+fileName);
			fos = new FileOutputStream("C:/Re_Process_DS/" +CreatedDate+"_ReProcess.txt");
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
			log.error("Exception : "+ex);
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
			String fileName1 = "C:/Re_Process_DS/" +CreatedDate+"_ReProcess.txt";
			log.info("Reprocess file : "+fileName1);
			fos = new FileOutputStream("C:/Re_Process_DS/" +CreatedDate+"_ReProcess.txt");
			dos = new DataOutputStream(fos);
			String line = currentLine;

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
		{
			try{
				if(fos != null)
					fos.close();
				if(dos != null)
					dos.close();
			}catch(Exception ex){
				log.error("Exception : "+ex);
			}
		}
	}


	// The main method
	public void perform(){

		try{
		//	String fname = createflagfile();

			conn = TLConnectionPool.getTLConnectionPool().borrowConnection();
			conn.setAutoCommit(false);

			String textFile1 = "";
			//String textFile2 = fname +"PM.TXT";

			DropShip dropShip1 = new DropShip();
			dropShip1.readPropertiesFile();

			String flagErrorUrl="";
		    String flagProcessedUrl="";
			/*errorUrl = "C:/EPED_DropShip_Error_Files";
			processedUrl = "C:/EPED_DropShip_Processed_Files";*/

			boolean exists = false;
		     File dir = new File(fileUrl);
		      if (dir.isDirectory()) {
		         String[] children = dir.list();
		         log.info("NO of Files"+ children.length);
		         for (int i=0; i<children.length; i++) {
		              if(children[i].endsWith(".FLG")){
		                   log.info("Flag File is"+children[i]);
		                   exists=true;
		                   flagFile=fileUrl+children[i];
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
		        log.info("File or directory exists");
		        String f1=fileUrl + textFile1;
		    //	String f2=fileUrl + textFile2;
		    	boolean existsFile1= (new File(f1).exists());
		    //	boolean existsFile2= (new File(f2).exists());
		    	if(existsFile1){
		    		fileUrl=f1;
		    		errorUrl=errorUrl+"/"+textFile1;
		    		processedUrl=processedUrl+"/"+textFile1;
		    	}
		    	/*if(existsFile2){
		    		fileUrl=f2;
		    		errorUrl=errorUrl+"/"+textFile2;
		    		processedUrl=processedUrl+"/"+textFile2;

		    	}
		         */
		    	DropShip ds = new DropShip(fileUrl);
		    	List list = ds.createRecordsForFile();
		    	log.info("Errorxml"+list.get(0).toString());
		    	log.info("rootxml"+list.get(1).toString());

				String errorxml=list.get(0).toString();
				String rootxml=list.get(1).toString();

				int errorcount=Integer.parseInt(list.get(2).toString());
				//int errorcount=0;

				if(errorcount>0){

					log.info("errorcount"+errorcount);
					log.info("Flag URl"+flagFile);
					ds.copyfile(fileUrl,errorUrl);
					ds.copyfile(flagFile,flagErrorUrl);;
					File f= new File(fileUrl);
					File flag = new File(flagFile);
					f.delete();
					flag.delete();

					SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
							emailSubject, errorxml, emailUserName, emailPassword);
				}else{

					DropShip dropShip = new DropShip(fileUrl);
					String line = dropShip.getNextLine();
					StringBuffer temp = new StringBuffer("");

					//Node resultantNode = null;
					while(line!=null){

						String res[]=line.split("\\|");
						if((dropShip.checkforDS(line).equalsIgnoreCase("DROP SHIP"))){
							Map ap = dropShip.prepareHeaderMapForDropShip();
							String xmlStr = dropShip.createXMLDS(ap,line);
							log.info("DS : "+xmlStr);

							if(poNumber.equals("start")||res[22].equals(poNumber)){
								if(poNumber.equals("start")){
									poNumber = res[22];
								}
								log.info("poNumber"+res[22]);
								temp.append(xmlStr);
								linesForAp.add(line);

							}else{

								ds.excuteDS(temp.toString());
								temp= new StringBuffer("");
								temp.append(xmlStr);
								linesForAp.clear();
					      		linesForAp.add(line);
								log.info("poNumber"+res[22]);
								poNumber = res[22];
							}
						}

			     		line = dropShip.getNextLine();
					}
					ds.excuteDS(temp.toString());
					conn.commit();
				    TLConnectionPool.getTLConnectionPool().returnConnection(conn);
				    ds.copyfile(fileUrl,processedUrl);
				    ds.copyfile(flagFile,flagProcessedUrl);
				    File f = new File(fileUrl);
				    File ff = new File(flagFile);
				    f.delete();
				    ff.delete();
				}

				}else{
					log.info("File or directory does not exist");
				}

		}catch(PersistanceException ex){
			try{
				 conn.rollback();
				 instance.createReProcessFile(linesForAp);

				 log.error("Error in Manualshippedpedigree class :"+ex);
				 SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
							"TL Exception",ex.getMessage(), emailUserName, emailPassword );
				 }catch(Exception e){
					 e.printStackTrace();
				 }
		}catch(Exception e){

			try {
				conn.rollback();
				instance.createReProcessFile(linesForAp);
				SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
						emailSubject ,e.getMessage(), emailUserName, emailPassword );
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		finally{
			try {
			log.error("returning the connection to pool in finally");
				TLConnectionPool.getTLConnectionPool().returnConnection(conn);

		}catch(Exception e)
		{
			log.error("error in returning cvonnection to pool "+e);
		}
		}
	}

	public static void main(String[] args){
		DropShip drop = new DropShip();
		drop.perform();
	}

}