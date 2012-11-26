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

package com.rdta.eag.epharma.pedexchange;

import com.rdta.eag.epharma.commons.CommonUtil;
import com.rdta.eag.epharma.commons.persistence.PersistanceException;
import com.rdta.eag.epharma.commons.persistence.QueryRunner;
import com.rdta.eag.epharma.commons.persistence.QueryRunnerFactory;
import com.rdta.eag.epharma.commons.xml.XMLUtil;
import com.rdta.eag.epharma.util.SendDHFormEmail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

import org.w3c.dom.Node;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;


/**
 * @author Santosh Subramanya
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class PedEnvTransmissionHandler {

	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	private static Log log = LogFactory.getLog(PedEnvTransmissionHandler.class);
	private static final String PROPS_CONFIG = "Ftp.properties";

	static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	static final SimpleDateFormat stf = new SimpleDateFormat("HHmmss");

	static String emailFrom = null;
	static String emailTo = null;
	static String emailSubject = null;
	static String emailSMTP = null;
	static String emailPassword = null;

	private void readPropertiesFile(){

		try{
			log.info("Inside properties reading block *******");
			log.info("Inside properties reading block *******");

			InputStream inputstream = getClass().getResourceAsStream(PROPS_CONFIG);

			java.util.Properties properties = new java.util.Properties();
			properties.load(inputstream);

			log.info(properties.getProperty("eMail.From"));
			emailFrom = properties.getProperty("eMail.From");
			log.info(properties.getProperty("eMail.From"));

			log.info(properties.getProperty("eMail.To"));
			emailTo = properties.getProperty("eMail.To");

			log.info(properties.getProperty("eMail.Subject"));
			emailSubject = properties.getProperty("eMail.Subject");

			log.info(properties.getProperty("eMail.SMTP"));
			emailSMTP = properties.getProperty("eMail.SMTP");

			log.info(properties.getProperty("eMail.Password"));
			emailPassword = properties.getProperty("eMail.Password");

		}catch(IOException e){
			log.error("Error reading the properties file inside PedEnvTransmissionHandler class........");
			log.error("Exception is :"+e);
		}
	}

	// This is the method which is called from the three use cases.
	public static void receivePedigreeEnvelopes(List pedEnvs) throws PersistanceException, IOException{

		PedEnvTransmissionHandler transmissionHandler = new PedEnvTransmissionHandler();
		FileOutputStream streamOut = null;

		try{
			int length = pedEnvs.size();
			log.info("The number of PE's received is :"+length);

			transmissionHandler.readPropertiesFile();

			FTPClient client = new FTPClient();

			for(int i=0; i<length; i++){

				String envId = pedEnvs.get(i).toString();
				String envIdRes = envId.substring(9);

				//query to retrieve the DEA number from the PedigreeEnvelope
				StringBuffer buffer = new StringBuffer();
				buffer.append("for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope where $i/*:serialNumber = '"+ envId + "'");
				buffer.append("return distinct-values($i/*:pedigree/*:shippedPedigree/*:transactionInfo/*:recipientInfo/*:licenseNumber)");
				String deaNumber = queryRunner.returnExecuteQueryStringsAsString(buffer.toString());
				log.info("The query to retrieve DEA number is :"+buffer.toString());
                 log.info("deaNumberdeaNumberdeaNumberdeaNumber"+deaNumber);    
				buffer = new StringBuffer();
				buffer.append("for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope where $i/*:serialNumber = '"+ envId + "'");
				buffer.append("return distinct-values($i/*:pedigree/*:shippedPedigree/*:transactionInfo/*:transactionIdentifier/*:identifier)");
				String poNumber = queryRunner.returnExecuteQueryStringsAsString(buffer.toString());
				log.info("The query to retrieve PO number is :"+buffer.toString());
                   
				StringBuffer buffer1 = new StringBuffer();
				
               List pedTradingPartnerInfo=null;
				//Query to retrieve the information from the PedigreeTradingPartner collection
				buffer1.append("tlsp:RetrievePedTradingPartnerInfo('"+deaNumber+"')");

				//buffer1.append("tlsp:RetrievePedTradingPartnerInfo('RM0314790')");


				pedTradingPartnerInfo = queryRunner.executeQuery(buffer1.toString());
				if(pedTradingPartnerInfo!=null)
				{

				Node listNode = XMLUtil.parse((InputStream)pedTradingPartnerInfo.get(0));

				/*String notificationDescription = CommonUtil.jspDisplayValue(listNode,"notificationDescription");
				log.info("notificationDescription is :" +notificationDescription );

				String notifyURI = CommonUtil.jspDisplayValue(listNode,"notificationInfo/notifyURI");
				log.info("notifyURI is :" +notifyURI ); */


				// Extracting the folder information to be created for saving the PE's to.
				String localFolder = CommonUtil.jspDisplayValue(listNode, "localFolder");
				log.info("The local folder to create is :"+localFolder);
				log.info("The local folder to create is :"+localFolder);

				String todayDate = sdf.format(new Date());
				log.info("***********Today's date is :"+todayDate);

				Date time = Calendar.getInstance().getTime();
				String theTime = stf.format(time);

				String fileName = localFolder+"/envelopMD-"+todayDate+"-"+theTime+"-"+poNumber+"-"+envIdRes+".xml";
				log.info("File name to be created is :"+fileName);

				// Retrieving the PE to be stored in the local folder
				StringBuffer buffer2 = new StringBuffer();
				buffer2.append("for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope where $i/*:serialNumber = '"+envId+"'");
				buffer2.append("return $i");

				List pedEnvDoc = queryRunner.executeQuery(buffer2.toString());
				InputStream stream = (InputStream)pedEnvDoc.get(0);

				//Creating a directory on the local machine to copy the PE's to.
				log.info("Creating local directory.......");
				File localDir = new File(localFolder);
				localDir.mkdirs();
				log.info("Created local directory "+localFolder+".......");

				streamOut = new FileOutputStream(fileName);

				int c;
				byte[] input = new byte[1024];

				while((c = stream.read(input)) != -1){
					streamOut.write(input,0,c);
				}
              
				streamOut.close();
				System.out.println("File successfully created......");
				}
				
			 	
				
				/*String username = CommonUtil.jspDisplayValue(listNode,"notificationInfo/username");
				log.info("username is :" +username );

				String password = CommonUtil.jspDisplayValue(listNode,"notificationInfo/password");
				log.info("password is :" +password );

				if(notificationDescription.equals("ftp")){
					log.info("Calling the ftp handler class.......");
					log.info("******"+envId + notifyURI + username + password );
					FTPHandler handler = new FTPHandler();
					handler.ftpHandler(envId, notifyURI, username, password);
				}*/
			}
		}catch(Exception e){
			try{
				SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
						emailSubject ,e.getMessage(), emailFrom, emailPassword );
			}catch(Exception e1){
				e1.printStackTrace();
				log.error(e.getMessage());
			}
		}finally{
			streamOut.close();
		}
	}
}