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

public class RepackUseCaseForPrePack implements Job{

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

	static String flagUrl = null;

	static String currentLine =null;
	static Connection conn = null;
	static List linesForAp= new ArrayList();
	static RepackUseCaseForPrePack instance = new RepackUseCaseForPrePack();

	private static final String PROPS_CONFIG = "Props.properties";

	StringBuffer ptpExceptionValues = new StringBuffer("The lines with Customerdea's having following Invoices doesnt match with PTP : ");

	static final SimpleDateFormat sdf = new SimpleDateFormat("MMddyy");

	 String invoice = "";

	private boolean isReaderStatusClosed = false;
	private static boolean ptpStatus = false;
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	private static Log log = LogFactory.getLog(RepackUseCaseForPrePack.class);

	//No Arguement constructor.
	public RepackUseCaseForPrePack(){


	}

	//Parameterized Constructor.
	public RepackUseCaseForPrePack(String filePath) throws Exception {
		this.filePath = filePath;
		buffReader = new BufferedReader(new FileReader(filePath));

	}
	// Constructor which takes the file path as an argument
	public RepackUseCaseForPrePack(String filePath,int lineNum ) throws Exception {
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

	// Method to read lines from the text file
	/*public String getNextLine() throws Exception {
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
	}*/


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
	private Map prepareHeaderMapForPrepack() throws Exception {

		Map Ab=  new LinkedHashMap();
		Ab.put(new Integer(0),"PickType");
		Ab.put(new Integer(1),"Invoice");
		Ab.put(new Integer(2),"PODateYYYYMMDD");
		Ab.put(new Integer(3),"RecDateYYYYMMDD");
		Ab.put(new Integer(4),"PedigreeId");
		Ab.put(new Integer(5),"NDC");
		Ab.put(new Integer(6),"MFGName");
		Ab.put(new Integer(7),"LegendDrugName");
		Ab.put(new Integer(8),"Dosage");
		Ab.put(new Integer(9),"Strength");
		Ab.put(new Integer(10),"ContainerSize");
		Ab.put(new Integer(11),"LotNo");
		Ab.put(new Integer(12),"ExpYYYYMMDD");
		Ab.put(new Integer(13),"QuantityPulled");
		Ab.put(new Integer(14),"MfgStreet1");
		Ab.put(new Integer(15),"MfgStreet2");
		Ab.put(new Integer(16),"MfgCity");
		Ab.put(new Integer(17),"MfgState");
		Ab.put(new Integer(18),"MfgPostalCode");
		Ab.put(new Integer(19),"MfgCountry");
		Ab.put(new Integer(20),"MfgContactName");
		Ab.put(new Integer(21),"MfgContactTitle");
		Ab.put(new Integer(22),"MfgContactEMail");
		Ab.put(new Integer(23),"MfgDEANumber");
		Ab.put(new Integer(24),"LotNumberInitial");
		Ab.put(new Integer(25),"ExpiryDate");
		Ab.put(new Integer(26),"Quantity");
		Ab.put(new Integer(27),"CustomerName");
		Ab.put(new Integer(28),"CustomerAddressStreet1");
		Ab.put(new Integer(29),"CustomerAddressStreet2");
		Ab.put(new Integer(30),"CustomerCity");
		Ab.put(new Integer(31),"CustomerState");
		Ab.put(new Integer(32),"CustomerPostalCode");
		Ab.put(new Integer(33),"CustomerCountry");
		//Ab.put(new Integer(34),"ShipToCustomerName");
		Ab.put(new Integer(34),"ShipToCustomerAddressLine1");
		Ab.put(new Integer(35),"ShipToCustomerAddressLine2");
		Ab.put(new Integer(36),"ShipToCustomerCity");
		Ab.put(new Integer(37),"ShipToState");
		Ab.put(new Integer(38),"ShipToCustomerZip");
		Ab.put(new Integer(39),"ShipToCountry");
		Ab.put(new Integer(40),"CustomerDea");
		Ab.put(new Integer(41),"BatchNumber");


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

	public void finalzie() {
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

			if(res[0].equalsIgnoreCase("MP"))
			{
				t=t+"<pedship>";

				Map mp = prepareHeaderMapForPrepack();
				log.debug("The Map value is : "+mp);
				for (Iterator it=mp.keySet().iterator(); it.hasNext(); ) {
					Object key = it.next();
					Object value = mp.get(key);
					int i = Integer.parseInt(key.toString());

					if(res[i].equalsIgnoreCase("")|| res[i]==null)
					{
						errorcount=errorcount+1;
						errors.append(addError("MP",getLineCount(),(String)value));
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

	//Method to check for MP RecordIdentifier.
	private String checkforRepack(String line) throws Exception {
		// TODO Auto-generated method stub
		if(line!=null)
		{
			String res[]=line.split("\\|");

			if( res[0]!=null && res[0].equalsIgnoreCase("RepackProd"))
			{
				return "RepackProd";
			}
			if( res[0]!=null && res[0].equalsIgnoreCase("PrevProd"))
			{
				return "PrevProd";
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

			log.debug("Inside properties reading block ");

			InputStream inputstream = getClass().getResourceAsStream(PROPS_CONFIG);
			java.util.Properties properties = new java.util.Properties();
			properties.load(inputstream);

			log.debug(properties.getProperty("eMail.From"));
			emailFrom = properties.getProperty("eMail.From");

			log.debug(properties.getProperty("eMail.To"));
			emailTo = properties.getProperty("eMail.To");

			log.debug(properties.getProperty("eMail.Subject"));
			emailSubject = properties.getProperty("eMail.Subject");

			log.debug(properties.getProperty("eMail.SMTP"));
			emailSMTP = properties.getProperty("eMail.SMTP");

			log.debug(properties.getProperty("eMail.UserName"));
			emailUserName = properties.getProperty("eMail.UserName");

			log.debug(properties.getProperty("eMail.Password"));
			emailPassword = properties.getProperty("eMail.Password");

			log.debug(properties.getProperty("repackUsecase.fileUrl"));
			fileUrl = properties.getProperty("repackUsecase.fileUrl");

			log.debug(properties.getProperty("repackUsecase.errorUrl"));
			errorUrl = properties.getProperty("repackUsecase.errorUrl");

			log.debug(properties.getProperty("repackUsecase.processedUrl"));
			processedUrl = properties.getProperty("repackUsecase.processedUrl");

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
			String fileName1 = "C:/Re_Process_MP/" +CreatedDate+"_ReProcess.txt";
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

		try{
			log.info("**** inside Repackage usecase perform method ****");
			invoice = "start";
			String fname = createflagfile();

			conn = TLConnectionPool.getTLConnectionPool().borrowConnection();
			conn.setAutoCommit(false);

			String flagFile = fname+ "PM.FLG";
			String textFile1 = fname +"AM.TXT";
			String textFile2 = fname +"PM.TXT";
			List envIds = new ArrayList();
			List envIds1=new ArrayList();
			String flagErrorUrl="";
			String flagProcessedUrl="";

			RepackUseCaseForPrePack msp = new RepackUseCaseForPrePack();
			msp.readPropertiesFile();

			//boolean exists = (new File(fileUrl+flagFile).exists());

			boolean exists = false;
			File dir = new File(fileUrl);
			if (dir.isDirectory()) {
				String[] children = dir.list();
				log.debug("NO of Files"+ children.length);

				//check if there are not more than 2 files in dir

				if(children.length == 2)
				{
				for (int i=0; i<children.length; i++) {
					if(children[i].endsWith(".FLG")){
						log.debug("Flag File is"+children[i]);
						exists=true;
						flagUrl= fileUrl+children[i];
						log.debug("Flagurl File is"+flagUrl);
						flagErrorUrl=flagErrorUrl+children[i];
						flagProcessedUrl=flagProcessedUrl+children[i];
						//rename flag file so it cant be picked up again
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
			 } else log.debug("NO TEXT FILE NOT EQUAL TO 2");
			} else {
				log.debug("NO TEXT FILE");
			}


			if (exists) {
				log.debug("***** File or directory exists *****");


				String f1=fileUrl + textFile1;
				//String f2=fileUrl + textFile2;
				boolean existsFile1= (new File(f1).exists());
				//boolean existsFile2= (new File(f2).exists());
				if(existsFile1){
					fileUrl=f1;
					errorUrl=errorUrl+"/"+textFile1;
					processedUrl=processedUrl+"/"+textFile1;
				}
//				if(existsFile2){
//				fileUrl=f2;
//				errorUrl=errorUrl+"/"+textFile2;
//				processedUrl=processedUrl+"/"+textFile2;
//				}
				FindErrorsInTextFile fetf= new FindErrorsInTextFile(fileUrl);
				List list= fetf.createRecordsForFile();

				int errorcount=Integer.parseInt(list.get(2).toString());
				log.info("*** error count *** :"+errorcount);
				if(errorcount>0 ){
					String errorxml=list.get(0).toString();
					log.debug("Null values  in the file");

					log.debug(errorxml);
					copyfile(fileUrl,errorUrl);

					File f = new File(fileUrl);
					f.delete();
					File flag = new File(flagUrl);
					flag.delete();

					SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
							emailSubject,errorxml, emailUserName, emailPassword );
					System.exit(0);

				}
				else
				{
					RepackUseCaseForPrePack cfc= new RepackUseCaseForPrePack(fileUrl);
					String line=cfc.getNextLine();
					StringBuffer temp = new StringBuffer("<result>");
					Map ap= cfc.prepareHeaderMapForPrepack();
					String xmlStr =null;
					int i=0;
					log.info("*** line before while loop *** :"+line);
					while(line!=null){

						if((cfc.checkforRepack(line).equalsIgnoreCase("RepackProd"))){

							log.debug("RepackProd"+cfc.getLineCount());
						  System.out.println("RepackProd"+cfc.getLineCount());
							xmlStr = cfc.createXMLForAP(ap,"pedship",line);
							log.debug("RepackProd");
							String res[]=line.split("\\|");
							System.out.println("INvoice"+res[1]);

							System.out.println("** iNvoice **"+invoice);
							if(invoice.equals("start"))
							{

								invoice=res[1];
								temp.append("<root>");
								temp.append("<PE>");
								temp.append("<repack>");
								temp.append(xmlStr);
							}
							else if(res[1].equals(invoice)){

								temp.append("</repack><repack>");
								temp.append(xmlStr);
							}
							else if(!res[1].equals(invoice) && !invoice.equals("start")){
								invoice =res[1];
								temp.append("</repack></PE></root>");
								temp.append(" <root><PE><repack>");
								temp.append(xmlStr);
							}


						}
						if((cfc.checkforRepack(line).equalsIgnoreCase("PrevProd"))){
							log.debug("PrevProd"+cfc.getLineCount());
							  System.out.println("PrevProd"+cfc.getLineCount());
							xmlStr = cfc.createXMLForAP(ap,"pedship",line);
							log.debug("PrevProd");
							temp.append(xmlStr);
                  		}
						line=cfc.getNextLine();
					}
					temp.append("</repack></PE></root>");
					StringBuffer resxml=temp.append("</result>");
					System.out.println(temp);
					String inputStr = resxml.toString();//.replaceAll("&","&amp;");
					inputStr = inputStr.replaceAll("'","&apos;");
					List envId = InsertDocToDB.CreateRepackManualPedigreeEnvelopeForPP(inputStr,signerid,deaNumber,sourceRoutingCode,fileName,conn);
					String kk=envId.toString();
					StringBuffer buf =null;
					StringBuffer buf1 =new StringBuffer();
					for(int m=0;m<envId.size();m++)
					{
						String data=envId.get(m).toString();

						System.out.println("Data is *****"+data);
						if(data.equalsIgnoreCase("NOPTPExists")){
						// buf1.toString();
							String query1 = " tlsp:GetInvoiceNo("+(inputStr)+") ";
							String invoiceNo = queryRunner.returnExecuteQueryStringsAsStringNew(query1,conn);
							log.debug("Invoive No. is :"	+ invoiceNo);
							ptpExceptionValues.append(invoiceNo+";");
							SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
									emailSubject ,ptpExceptionValues.toString(), emailUserName, emailPassword );
							copyfile(fileUrl,errorUrl);
							File f = new File(fileUrl);
							f.delete();
							File flag = new File(flagUrl);
							flag.delete();


						}
						else{
								envIds=envId;
								envIds1.add(data);
								linesForAp.clear();
								linesForAp.add(line);
								copyfile(fileUrl,processedUrl);
								File f = new File(fileUrl);
								f.delete();
								File flag = new File(flagUrl);
								flag.delete();
								log.debug("No of envIds created : "+envIds);
								buf =  new StringBuffer();
								buf.append("<EnvelopeIds>");
								for(int x=0; x<envIds1.size();x++){
									buf.append("<envelopeId>");
									buf.append(envIds1.get(x).toString());
									buf.append("</envelopeId>");
								}
								buf1=buf.append("</EnvelopeIds>");
								ptpStatus=true;
							}
					}
				if(ptpStatus)
				{
				 String query2 = "tlsp:InsertProcessedEnvelope("+buf1.toString()+",'"+fileName+"')";
			     log.debug("Insert query is : "+query2);
			     queryRunner.returnExecuteQueryStringsAsStringNew(query2,conn);
		    	 PedEnvTransmissionHandler.receivePedigreeEnvelopes(envIds1);
		     	 conn.commit();
			     //TLConnectionPool.getTLConnectionPool().returnConnection(conn);
				}
				}


			}
			else {
				log.debug(" File or directory does not exist");
				//SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
				//		emailSubject,"File or directory does not exist", emailUserName, emailPassword );

			}

		}catch(PersistanceException ex){
			try{
				conn.rollback();
				instance.createReProcessFile(linesForAp);

				log.error("Error in RepackUseCaseForPrepack class :"+ex);
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

    RepackUseCaseForPrePack m = new RepackUseCaseForPrePack ();
    m.perform();
	}
}

