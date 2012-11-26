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
import java.io.StringBufferInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;

import com.rdta.eag.epharma.commons.persistence.PersistanceException;
import com.rdta.eag.epharma.commons.persistence.QueryRunner;
import com.rdta.eag.epharma.commons.persistence.QueryRunnerFactory;
import com.rdta.eag.epharma.commons.persistence.pool.TLConnectionPool;

import com.rdta.eag.epharma.pedexchange.PedEnvTransmissionHandler;
import com.rdta.eag.epharma.job.Job;
import com.rdta.eag.epharma.commons.xml.XMLUtil;

import com.rdta.eag.epharma.util.SendDHFormEmail;
import com.rdta.tlapi.xql.Connection;

public class DeleteAndInsertPedigree implements Job{

	private String filePath;
	private Map sourceHeaderMap = new LinkedHashMap();
	private static BufferedReader buffReader;
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
	static String fileName = null;
	static String deleteUsecaseError=null;
	static String deleteUsecaseGenericError=null;
	static String errorFileFolder = null;

	static String flagUrl = null;

	static String currentLine =null;
	static List linesForAp= new ArrayList();
	static DeleteAndInsertPedigree instance = new DeleteAndInsertPedigree();

	private static final String PROPS_CONFIG = "Props.properties";

	StringBuffer ptpExceptionValues = new StringBuffer("The lines with Customerdea's having following Invoices doesnt match with PTP : ");

	final static SimpleDateFormat sdf = new SimpleDateFormat("MMddyy");



	private static String invoice = "start";
	private boolean isReaderStatusClosed = false;
	private static boolean ptpStatus = false;
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	private static Log log = LogFactory.getLog(DeleteAndInsertPedigree.class);
	private String newline = System.getProperty("line.separator");
	//No Arguement constructor.
	public DeleteAndInsertPedigree(){

	}

	//Parameterized Constructor.
	public DeleteAndInsertPedigree(String filePath) throws Exception {
		this.filePath = filePath;
		buffReader = new BufferedReader(new FileReader(filePath));

	}
	// Constructor which takes the file path as an argument
	public DeleteAndInsertPedigree(String filePath,int lineNum ) throws Exception {
		this(filePath);
		skipNumberOfLines(lineNum);
	}

	public String getName() {
		return "MonitorActivityJobPedRcv";
	}

	//Method to skip the no.of lines
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

	//Method to decrement the line count.
	public void LineDecrement() throws Exception {
		lineCount=lineCount-1;
	}

	//Method to prepare header for Map.
	private Map prepareHeaderMap() throws Exception {


		String temp="";
		String line = getNextLine();
		log.debug(line);
		if(line != null) {
			String[] result = line.split("\\|");
			colCount = result.length - 1;
			String token = null;
			for(int i = 0; i<result.length; i++) {
				token = result[i];
				log.debug("Header Name: "+i + token);
				sourceHeaderMap.put(new Integer(i), token);

			}
		}
		return sourceHeaderMap;

	}

	// Preparing the headers for Batch file values.
	private Map prepareHeaderMapForDeleteAndInsert() throws Exception {

		Map Ab=  new LinkedHashMap();
		Ab.put(new Integer(0),"RecordIdentifier");
		Ab.put(new Integer(1),"PONo");
		Ab.put(new Integer(2),"PODateYYYYMMDD");
		Ab.put(new Integer(3),"RecDateYYYYMMDD");
		Ab.put(new Integer(4),"NDC");
		Ab.put(new Integer(5),"MFGName");
		Ab.put(new Integer(6),"DrugLabel");
		Ab.put(new Integer(7),"Form");
		Ab.put(new Integer(8),"Strength");
		Ab.put(new Integer(9),"ContainerSize");
		Ab.put(new Integer(10),"LotNo");
		Ab.put(new Integer(11),"ExpYYYYMMDD");
		Ab.put(new Integer(12),"QuantityPulled");
		Ab.put(new Integer(13),"MFGAddressLine1");
		Ab.put(new Integer(14),"MFGAddressLine2");
		Ab.put(new Integer(15),"MFGCity");
		Ab.put(new Integer(16),"State");
		Ab.put(new Integer(17),"MFGZip");
		Ab.put(new Integer(18),"Country");
		Ab.put(new Integer(19),"MFGContactName");
		Ab.put(new Integer(20),"MFGContactTelephone");
		Ab.put(new Integer(21),"MFGContactEMail");
		Ab.put(new Integer(22),"MFGDea");
		Ab.put(new Integer(23),"InvoiceNo");
		Ab.put(new Integer(24),"InvoiceDateYYYYMMDD");
		Ab.put(new Integer(25),"PONumber");
		Ab.put(new Integer(26),"CustomerName");
		Ab.put(new Integer(27),"CustomerAddressLine1");
		Ab.put(new Integer(28),"CustomerAddressLine2");
		Ab.put(new Integer(29),"CustomerCity");
		Ab.put(new Integer(30),"CustomerState");
		Ab.put(new Integer(31),"CustomerZip");
		Ab.put(new Integer(32),"CustomerCountry");
		Ab.put(new Integer(33),"ShipToCustomerName");
		Ab.put(new Integer(34),"ShipToCustomerAddressLine1");
		Ab.put(new Integer(35),"ShipToCustomerAddressLine2");
		Ab.put(new Integer(36),"ShipToCustomerCity");
		Ab.put(new Integer(37),"ShipToState");
		Ab.put(new Integer(38),"ShipToCustomerZip");
		Ab.put(new Integer(39),"ShipToCountry");
		Ab.put(new Integer(40),"UniqueBoxID");
		Ab.put(new Integer(41),"CustomerDea");
		Ab.put(new Integer(42),"CaseSerialNo");

		return Ab;

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

	public void finalize() {
		try {

			if(buffReader != null) {
				buffReader.close();
			}

		}catch(Exception e) {}
	}

	//Method to prepare Error xml.
	public String addError(String RecId,int lineNo, String colName) throws Exception{
		String er="<Error><RecordIdentifier>"+RecId+"</RecordIdentifier><Message>"+colName+" at line "+lineNo+" is null</Message></Error>";
		return  er;
	}

	//Method to Create records for the Batch file.
	public List createRecordsForFile() throws Exception{
		String t ="<root>";
		StringBuffer errors=new StringBuffer("<Errors>");
		int errorcount=0;

		String line=getNextLine();

		while(line !=null)
		{
			String res[]=line.split("\\|");

			if(res[0].equalsIgnoreCase("DELETE_PEDIGREE"))
			{
				t=t+"<pedship>";

				Map mp = prepareHeaderMapForDeleteAndInsert();
				log.debug("The Map value is : "+mp);
				for (Iterator it=mp.keySet().iterator(); it.hasNext(); ) {
					Object key = it.next();
					Object value = mp.get(key);
					int i = Integer.parseInt(key.toString());

					if(res[i].equalsIgnoreCase("")|| res[i]==null)
					{
						errorcount=errorcount+1;
						errors.append(addError("DELETE_PEDIGREE",getLineCount(),(String)value));
					}
					if(i<res.length && res[i]!=null){
						t =t +"<"+value+">"+res[i]+"</"+value+">";

					}
					else{
						t =t +"<"+value+"/>" ;

					}

				}

				t =t +"</pedship>";
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

	//Method to create the XML for the RecordIdentifier.
	public String createXMLForAP(Map m,String subrootname,String line) throws Exception{
		String t ="";

		t=t+"<"+subrootname+">";
		String res[]=line.split("\\|");

		Map mp = m;
		for (Iterator it=mp.keySet().iterator(); it.hasNext(); ) {
			Object key = it.next();
			Object value = mp.get(key);
			int i = Integer.parseInt(key.toString());

			if(i<res.length && res[i]!=null){
				t =t +"<"+value+">"+res[i]+"</"+value+">";
			}
			else{
				t =t +"<"+value+"/>" ;
			}

		}
		t =t +"</"+subrootname+">";

		return (t.toString()).replaceAll("&","&amp;") ;
	}

	//Method to check for DELETE_PEDIGREE RecordIdentifier.
	private String checkforDELETE_PEDIGREE(String line) throws Exception {
		// TODO Auto-generated method stub
		if(line!=null)
		{
			String res[]=line.split("\\|");

			if( res[0]!=null && res[0].equalsIgnoreCase("DELETE_PEDIGREE"))
			{
				return "DELETE_PEDIGREE";
			}

			if( res[0]!=null && res[0].equalsIgnoreCase("RecordIdentifier"))
			{
				return "RecordIdentifier";
			}

		}
		return "NA";

	}


	//Method to check the RecordIdentifier.
	private boolean checkforRecordIdentifier(String line) throws Exception {
		// TODO Auto-generated method stub
		if(line!=null)
		{
			String res[]=line.split("\\|");

			if( res[0]!=null && res[0].equalsIgnoreCase("RecordIdentifier"))
			{
				return true;
			}
			return false;
		}
		return false;

	}


	/**
	 * @param lineCount The lineCount to set.
	 */
	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}

	//A method that creates and returns a flag file on a daily basis
	private static String createflagfile() {

		TimeZone local = TimeZone.getDefault();

		String todayDate = sdf.format(new Date());
		log.debug("***********Today's date is :"+todayDate);

		String flagFile = "EPED"+todayDate;
		return flagFile;
	}


	//Method to copy files from one directory to the other.
	private static void copyfile(String source,String destination) throws Exception{

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
			log.debug("Filename incorrect");

			return;
		}
		catch(FileNotFoundException e)
		{
			log.debug(e.getMessage());
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

	//Method to read Properties file to get required values.
	private void readPropertiesFile(){

		try{

			log.debug("Inside properties reading block *******");

			InputStream inputstream = getClass().getResourceAsStream(PROPS_CONFIG);
			java.util.Properties properties = new java.util.Properties();
			properties.load(inputstream);

			log.debug(properties.getProperty("eMail.From"));
			emailFrom = properties.getProperty("eMail.From");

			log.debug(properties.getProperty("eMail.To"));
			emailTo = properties.getProperty("eMail.To");

			log.debug(properties.getProperty("eMail.Subject.DeleteAndInsertUsecaseError"));
			deleteUsecaseError=properties.getProperty("eMail.Subject.DeleteAndInsertUsecaseError");

			log.debug(properties.getProperty("eMail.Subject.DeleteAndInsertUseCaseGenericError"));
			deleteUsecaseGenericError = properties.getProperty("eMail.Subject.DeleteAndInsertUseCaseGenericError");



			log.debug(properties.getProperty("eMail.Subject"));
			emailSubject = properties.getProperty("eMail.Subject");


			log.debug(properties.getProperty("eMail.SMTP"));
			emailSMTP = properties.getProperty("eMail.SMTP");

			log.debug(properties.getProperty("eMail.UserName"));
			emailUserName = properties.getProperty("eMail.UserName");

			log.debug(properties.getProperty("eMail.Password"));
			emailPassword = properties.getProperty("eMail.Password");

			log.debug(properties.getProperty("deleteAndInsertPedigreeUsecase.fileUrl"));
			fileUrl = properties.getProperty("deleteAndInsertPedigreeUsecase.fileUrl");

			log.debug(properties.getProperty("deleteAndInsertPedigreeUsecase.errorUrl"));
			errorUrl = properties.getProperty("deleteAndInsertPedigreeUsecase.errorUrl");

			errorFileFolder = properties.getProperty("deleteAndInsertPedigreeUsecase.errorUrl");

			log.debug(properties.getProperty("deleteAndInsertPedigreeUsecase.processedUrl"));
			processedUrl = properties.getProperty("deleteAndInsertPedigreeUsecase.processedUrl");

			log.debug(properties.getProperty("signerId"));
			signerid = properties.getProperty("signerId");

			log.debug(properties.getProperty("deaNumber"));
			deaNumber = properties.getProperty("deaNumber");

			log.debug(properties.getProperty("sourceRoutingCode"));
			sourceRoutingCode = properties.getProperty("sourceRoutingCode");

		}catch(IOException e){
			log.debug("Exception is :"+e);

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
			String fileName = "C:/Re_Process_DeleteAndInsert/" +CreatedDate+"_ReProcess.txt";
			log.debug("Reprocess file : "+fileName);
			fos = new FileOutputStream("C:/Re_Process_DeleteAndInsert/" +CreatedDate+"_ReProcess.txt");
			dos = new DataOutputStream(fos);
			String line= currentLine;
			log.debug("lineNumber  : "+lineCount);
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
			String fileName1 = "C:/Re_Process_DeleteAndInsert/" +CreatedDate+"_ReProcess.txt";
			log.debug("Reprocess file : "+fileName1);
			fos = new FileOutputStream("C:/Re_Process_DeleteAndInsert/" +CreatedDate+"_ReProcess.txt");
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


	public void perform()     {

		Connection conn = null;

		try{
System.out.println("yaha 11111111111");

			String fname = createflagfile();
System.out.println("yaha 21111111111");
			conn = TLConnectionPool.getTLConnectionPool().borrowConnection();
			conn.setAutoCommit(false);
System.out.println("yaha 31111111111");
			String flagFile = fname+ "PM.FLG";
			String textFile1 = fname +"AM.TXT";
			String textFile2 = fname +"PM.TXT";
			List envIds = new ArrayList();
			String flagErrorUrl="";
			String flagProcessedUrl="";

			readPropertiesFile();
System.out.println("yaha 41111111111");
			boolean exists = false;
			File dir = new File(fileUrl);
			log.debug(fileUrl);
			if (dir.isDirectory()) {
				String[] children = dir.list();
				log.debug("NO of Files"+ children.length);
				 int size=children.length;
				if(size == 1)
				{
					if(children[0].endsWith(".FLG")){
						log.debug("Only flag file was seen in the Delete-Insert dir-Please put in data file to rectify the problem: sending error email");
						SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
								emailSubject ,"Only flag file was seen in the dir-Please put in data file to rectify the problem", emailUserName, emailPassword );
					}
					else
					{
						log.debug("Flag file is not present in case of Delete-Insert,please put in flag file");
						SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
								emailSubject ,"Flag file is not present in Delete-Insert case,please put in flag file", emailUserName, emailPassword );
					}

				}
				if(size == 2)
				{
				for (int i=0; i<children.length; i++) {
					if(children[i].endsWith(".FLG")){
						log.debug("Flag File is"+children[i]);
						exists=true;
						flagUrl= fileUrl+children[i];
						log.debug("Flagurl File is"+flagUrl);
						flagErrorUrl=flagErrorUrl+children[i];
						flagProcessedUrl=flagProcessedUrl+children[i];
						File flagFl = new File(flagUrl);
						flagUrl=flagUrl+"_";
						File newfile = new File(flagUrl);
						flagFl.renameTo(newfile);
					}
					if(children[i].endsWith(".txt")||children[i].endsWith(".TXT")){
						log.debug("Text File is"+children[i]);
						textFile1=children[i];
						fileName = textFile1;
					}

				}
			}
				else if(size>2)
				{

					SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
							emailSubject,"Number of files exceeds allowed limit of two in case of Delete-Insert.Please remove additional files.", emailUserName, emailPassword );
					log.debug("NO TEXT FILE NOT > 2: error email sent");
					//cannot let control to process the files
					exists=false;
				}
			}else {
				log.debug("NO TEXT FILE");
			}


			if (exists) {
				log.debug(" File or directory exists");


				String f1=fileUrl + textFile1;
				boolean existsFile1= (new File(f1).exists());

				if(existsFile1){
					fileUrl=f1;
					errorUrl=errorUrl+"/"+textFile1;
					processedUrl=processedUrl+"/"+textFile1;
				}

				FindErrorsInTextFile fetf= new FindErrorsInTextFile(fileUrl,errorFileFolder);
				List list= fetf.createRecordsForFile();

				int errorcount=Integer.parseInt(list.get(2).toString());
				if(errorcount>0 ){
					String errorxml=list.get(0).toString();
					log.debug("Null values  in the file");

					log.debug(errorxml);
					copyfile(fileUrl,errorUrl);

					File f = new File(fileUrl);
					f.delete();
					File flag = new File(flagUrl);
					flag.delete();

					SendDHFormEmail.sendMailToSupport(emailFrom, "manish@rainnigdata.com" , emailSMTP,
							emailSubject,errorxml, emailUserName, emailPassword );

				}
				else
				{
					DeleteAndInsertPedigree cfc= new DeleteAndInsertPedigree(fileUrl);
					String line=cfc.getNextLine();


					if(line == null || line.trim().equals("")){
						log.info("The text file is empty");
						SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
						emailSubject ,"The text file is empty in case of delete insert", emailUserName, emailPassword );
						copyfile(fileUrl,errorUrl);
						File f = new File(fileUrl);
						f.delete();
						File flag = new File(flagUrl);
						flag.delete();
					}
					else
					{
					StringBuffer temp = new StringBuffer("");
					invoice="start";
					if(!cfc.checkforDELETE_PEDIGREE(line).equalsIgnoreCase("DELETE_PEDIGREE")){
						log.debug("Wrong Pick type");
						SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
								emailSubject,"The Pick Type should be DELETE_PEDIGREE", emailUserName, emailPassword );
						
					}else{
					while(line!=null){

						if((cfc.checkforDELETE_PEDIGREE(line).equalsIgnoreCase("DELETE_PEDIGREE"))){
							log.debug("DELETE_PEDIGREE Line No: "+cfc.getLineCount());
							Map ap= cfc.prepareHeaderMapForDeleteAndInsert();
							String xmlStr = cfc.createXMLForAP(ap,"pedship",line);
							log.debug("Line Contents is "+line);
							//log.debug("DELETE_PEDIGREE");
							String res[]=line.split("\\|");

							if(invoice.equals("start")||res[23].equals(invoice))
							{
								if(invoice.equals("start"))
								{
									invoice = res[23];
								}
								log.debug("Invoice here "+res[23]);
								temp.append(xmlStr);
								linesForAp.add(line);

							}
							else{

								StringBuffer resxml=new StringBuffer("<root><pedshipData>");
								resxml.append(temp);
								resxml.append("</pedshipData></root>");
								log.debug("PEDSHIPDATA FOR EACH INVOICE"+resxml.toString());
								String xquery = "tlsp:Delete_IfMatchingPEFound($1)";

								StringBufferInputStream sbIns = new StringBufferInputStream(resxml.toString());
								String deleteQueryResult = queryRunner.returnExecuteQueryStreamAsString(xquery,sbIns);

								log.debug("*********"+deleteQueryResult);

								//String deleteQueryResult = queryRunner.returnExecuteQueryStringsAsStringNew(xquery,conn);
								log.debug("Line Count "+currentLine);
								if(deleteQueryResult.equalsIgnoreCase("true")){
									log.debug("The deletion of Matching Pedigree Envelope is successful  LineCount value "+currentLine);
									temp= new StringBuffer("");
									temp.append(xmlStr);
									log.debug("Invoice here : "+res[23]);
									invoice = res[23];

									String query = " tlsp:PTPExists($1) ";

									String resStr = resxml.toString().replaceAll("[\\{\\}]"," ");
									resStr = resStr.replaceAll("'","&apos;");

									StringBufferInputStream sbIns2 = new StringBufferInputStream(resStr);
									String ptpexists = queryRunner.returnExecuteQueryStreamAsString(query,sbIns2);

									log.debug("The ptpexists value is : "+ptpexists);
									String inputStr = resStr;//.replaceAll("&","&amp;");
									inputStr = inputStr.replaceAll("'","&apos;");
									log.debug("Output "+inputStr);
									String envId = InsertDocToDB.CreateDeleteAndInsertPedigreeEnvelopeForMD(inputStr,signerid,deaNumber,sourceRoutingCode,fileName,conn);
										envIds.add(envId);

								}
								else{

									emailSubject = "No Matching Pedigree Envelope is found";
									SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
											emailSubject,"No Matching Pedigree Envelope is found for the following Pedship"+newline+newline+resxml.toString(), emailUserName, emailPassword );
									log.debug("No Matching Pedigree Envelope is found");
									//log.debug("Invoice here : "+newline+newline+res[23]);

									temp= new StringBuffer("");
									temp.append(xmlStr);
									invoice = res[23];
									linesForAp.clear();
									linesForAp.add(line);
								}


							}

						}
						line=cfc.getNextLine();
					}

					StringBuffer resxml=new StringBuffer("<root><pedshipData>");
					resxml.append(temp);
					resxml.append("</pedshipData></root>");
					log.debug("Invoice here also : "+invoice);
					String xquery = "tlsp:Delete_IfMatchingPEFound($1)";

					log.debug("Input to SP is  : "+resxml.toString());

					StringBufferInputStream sbIns = new StringBufferInputStream(resxml.toString());
					String deleteQueryResult = queryRunner.returnExecuteQueryStreamAsString(xquery,sbIns);

					//String xquery = "tlsp:Delete_IfMatchingPEFound("+resxml.toString()+")";

					log.debug("Delete query "+deleteQueryResult);


					if(deleteQueryResult.equalsIgnoreCase("true"))
					{
						String res1 = resxml.toString().replaceAll("[\\{\\}]"," ");
						res1 = res1.replaceAll("'","&apos;");

						String query = " tlsp:PTPExists($1) ";

						StringBufferInputStream sbIns1 = new StringBufferInputStream(res1);
						String ptpexists = queryRunner.returnExecuteQueryStreamAsString(query,sbIns1);

						//String query = " tlsp:PTPExists("+res1+") ";
						//String ptpexists = queryRunner.returnExecuteQueryStringsAsStringNew(query,conn);
						//log.debug("The ptpexists value is : "+ptpexists);

						String inputStr = res1;//.replaceAll("&","&amp;");
						inputStr = inputStr.replaceAll("'","&apos;");
						String envId = InsertDocToDB.CreateDeleteAndInsertPedigreeEnvelopeForMD(inputStr,signerid,deaNumber,sourceRoutingCode,fileName,conn);
							envIds.add(envId);
							linesForAp.clear();
							linesForAp.add(line);

						copyfile(fileUrl,processedUrl);
						File f = new File(fileUrl);
						f.delete();
						File flag = new File(flagUrl);
						flag.delete();

						log.debug("No of envIds created : "+envIds);
						StringBuffer buf =  new StringBuffer();
						buf.append("<EnvelopeIds>");
						for(int i=0; i<envIds.size();i++){
							buf.append("<envelopeId>");
							log.debug(envIds.get(i).toString());
							buf.append(envIds.get(i).toString());
							buf.append("</envelopeId>");
						}
						buf.append("</EnvelopeIds>");
						conn.commit();
						log.debug("EnvelopeIds string : "+buf.toString());
						String query2 = "tlsp:InsertProcessedEnvelope("+buf.toString()+",'"+fileName+"')";
						log.debug("Insert query is : "+query2);
						queryRunner.returnExecuteQueryStringsAsStringNew(query2,conn);

						//check for unsigned envelopes
						String unsignedEnvId =null;

						String resignQuery ="tlsp:FindUnSignedEnvelope_MD()";
						unsignedEnvId = queryRunner.returnExecuteQueryStringsAsStringNew(resignQuery,conn);
						//System.out.println("resignQuery "+resignQuery);
						//System.out.println("resignQueryResult "+unsignedEnvId );
						log.info("resignQueryResult these envelope ids did not get signed  Delete-Insert:"+unsignedEnvId );

						if(unsignedEnvId != null && !unsignedEnvId.equals(""))
						{
							String unsignedMessage = "These are the unsigned envelopes, subsequently a procedure will be run to correct them  : "+ unsignedEnvId;
							log.info("Alert: Undsinged envelopes are : "+unsignedEnvId);

							SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
							"Alert: Undsinged envelopes Delete-Insert",unsignedEnvId, emailUserName, emailPassword );

							resignQuery ="tlsp:CreateSignatureToUnsignedEnvelope_MD()";
							String result = queryRunner.returnExecuteQueryStringsAsStringNew(resignQuery,conn);
							log.debug("result of CreateSignatureToUnsignedEnvelope_MD :"+result);
							//System.out.println("resignQuery "+resignQuery);
							//System.out.println("resignQueryResult "+resignQuery);

						}
						//check for unsigned envelopes ends

						PedEnvTransmissionHandler.receivePedigreeEnvelopes(envIds);
						conn.commit();

					}else{
						copyfile(fileUrl,errorUrl);
						File f = new File(fileUrl);
						f.delete();
						File flag = new File(flagUrl);
						flag.delete();
						emailSubject = "No Matching Pedigree Envelope is found";
						SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
								emailSubject,"No Matching Pedigree Envelope is found for the following Pedship   "+newline+newline+resxml.toString(), emailUserName, emailPassword );
						log.debug("No Matching Pedigree Envelope is found");
					}


					//TLConnectionPool.getTLConnectionPool().returnConnection(conn);
					//log.debug("ptpExceptionValues is :"	+ ptpExceptionValues.toString());


			}//Check for PickType
			}
			}//end of if line == null
			}//end og if exists
			else {
				log.debug(" File or directory does not exist");
				//SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
				//		emailSubject,"File or directory does not exist", emailUserName, emailPassword );

			}

		}catch(PersistanceException ex){
			try{
				conn.rollback();
				instance.createReProcessFile(linesForAp);

				log.error("Error in DeleteAndInsertPedigree class :"+ex);
				SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
						deleteUsecaseError,ex.getMessage(), emailUserName, emailPassword );
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){

			try {
				conn.rollback();
				instance.createReProcessFile(linesForAp);
				SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
						deleteUsecaseGenericError ,e.getMessage(), emailUserName, emailPassword );
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		finally{
			try {
				ptpExceptionValues = new StringBuffer("The lines with Customerdea's having following Invoices doesnt match with PTP : ");
				log.debug("returning the connection to pool in finally");
				TLConnectionPool.getTLConnectionPool().returnConnection(conn);

		}catch(Exception e)
		{
			log.error("error in returning cvonnection to pool "+e);
		}
		}

	}

	public static void main(String args[])
	{

    DeleteAndInsertPedigree m = new DeleteAndInsertPedigree ();
    m.perform();
	}
}