package com.rdta.eag.epharma.dropship;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rdta.eag.epharma.activity.DropShip;

public class ReadConfig {
	
	private static final String PROPS_CONFIG = "Props.properties";
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
	private static Log log = LogFactory.getLog(ReadConfig.class);
	
	public String readPropertiesFile()throws Exception{
		StringBuffer sb = new StringBuffer("");
		InputStream inputstream = null;
		try{
			log.info("Inside properties reading block *******");

			inputstream = getClass().getResourceAsStream(PROPS_CONFIG);

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
           
            sb.append("<properties>");
            sb.append("<emailFrom>"+emailFrom+"</emailFrom>");
            sb.append("<emailTo>"+emailTo+"</emailTo>");
            sb.append("<emailSubject>"+emailSubject+"</emailSubject>");
            sb.append("<emailSMTP>"+emailSMTP+"</emailSMTP>");
            sb.append("<emailUserName>"+emailUserName+"</emailUserName>");
            sb.append("<emailPassword>"+emailPassword+"</emailPassword>");
            sb.append("<fileUrl>"+fileUrl+"</fileUrl>");
            sb.append("<errorUrl>"+errorUrl+"</errorUrl>");
            sb.append("<processedUrl>"+processedUrl+"</processedUrl>");
            sb.append("<signerid>"+signerid+"</signerid>");
            sb.append("<deaNumber>"+deaNumber+"</deaNumber>");
            sb.append("<sourceRoutingCode>"+sourceRoutingCode+"</sourceRoutingCode>");
            sb.append("</properties>");
		}
		catch(IOException e)
		{
			log.error("Exception is :"+e);
		}
		finally
		{
			inputstream.close();
		}
		return sb.toString();
	}
	
	public String ReadConfigFile()throws Exception{
		
		String urlXmlString = readPropertiesFile();
		return urlXmlString;
		
	}
	
}
