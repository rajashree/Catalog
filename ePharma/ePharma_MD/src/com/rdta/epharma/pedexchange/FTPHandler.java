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

package com.rdta.epharma.pedexchange;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.eag.epharma.util.SendDHFormEmail;



/**
 * @author Santosh Subramanya
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class FTPHandler {
	
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	private static Log log = LogFactory.getLog(FTPHandler.class);
	private static final String PROPS_CONFIG = "Ftp.properties";
	
	static final SimpleDateFormat sdf = new SimpleDateFormat("MMddyy");
	
	static String emailFrom = null;
	static String emailTo = null;
	static String emailSubject = null;
	static String emailSMTP = null;
	static String emailPassword = null; 

//	 FTP handler method that handles the operations of ftp connection and pedigree transmission	
	public void ftpHandler(String envId, String notifyURI, String username, String password) throws IOException{
			
		log.info("Inside ftpHandler() method of the FTPHandler class....................");
		
		FTPClient ftpClient = null;
		FTPHandler handler = new FTPHandler();
		
		try{
			
			log.info("Trying to connect to the ftp.......");
			
	        int reply;
	        
	        //calling the readPropertiesFile() method to get the required mail information from the properties file
	        handler.readPropertiesFile();
	        
	        ftpClient = new FTPClient();	        
			ftpClient.connect(notifyURI);			
			ftpClient.login(username, password);			
			
			System.out.println("Reply from FTP is :"+ftpClient.getReplyString());
			log.info("Reply from FTP is :"+ftpClient.getReplyString());
			
			reply = ftpClient.getReplyCode();			
			log.info("FTP reply code is :"+reply);
			
			if(!FTPReply.isPositiveCompletion(reply)) {
		        ftpClient.disconnect();		        
		        log.error("FTP server refused connection.");
		        System.exit(1);
		    }			
			
			log.info("Connection to the ftp successful..........");
			
			StringBuffer buffer2 = new StringBuffer();
			buffer2.append("for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope where $i/*:serialNumber = '"+envId+"'");
			buffer2.append("return $i");
			
			List pedEnvDoc = queryRunner.executeQuery(buffer2.toString());		
			InputStream stream = (InputStream)pedEnvDoc.get(0);			
			
			String todayDate = sdf.format(new Date());			
			log.info("***********Today's date is :"+todayDate);
			
			log.info("Uploading the file to the ftp.....");
			
			log.info("Creating a new directory on the FTP Server....");
			ftpClient.makeDirectory(todayDate);
			
			log.info("Changing the working directory on the server to "+todayDate);
			ftpClient.changeWorkingDirectory(todayDate);			
			
			ftpClient.storeUniqueFile(stream);
			
			log.info("Successfully Uploaded the file to the ftp.....");
			
			//creating a log entry in the DeliveryLogInfo collection
			
			StringBuffer buffer3 = new StringBuffer();
			buffer3.append("tig:insert-document('tig:///ePharma_MD/DeliveryLogInfo', <DeliveryLog><pedigreeEnvelopeId>"+envId+"</pedigreeEnvelopeId><status>delivered</status></DeliveryLog>)");
			queryRunner.returnExecuteQueryStringsAsString(buffer3.toString());
			
			log.info("Log entry created in DeliveryLogInfo collection......");
			
		}catch(Exception e){			
			log.error(e.getMessage());
			try{
				SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
						emailSubject ,e.getMessage(), emailFrom, emailPassword );
			}catch(Exception e1){				
				e1.printStackTrace();
				log.error(e.getMessage());
			}
		}finally{
			log.info("Inside the finally block of FTPHandler Class......");
			try{
				log.info("Trying to close the FTP connection......");
				ftpClient.disconnect();	
			}catch(Exception e){
				try{
					SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
							emailSubject ,e.getMessage(), emailFrom, emailPassword );
				}catch(Exception e1){				
					e1.printStackTrace();
					log.error(e.getMessage());
				}
			}
						
			log.info("FTP connection successfully closed......");
		}
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
			
			log.info(properties.getProperty("eMail.Password"));
			emailPassword = properties.getProperty("eMail.Password");					
			
		}catch(IOException e){
			System.out.println("Exception is :"+e);
			log.error("Exception is :"+e);
		}
	}
}
