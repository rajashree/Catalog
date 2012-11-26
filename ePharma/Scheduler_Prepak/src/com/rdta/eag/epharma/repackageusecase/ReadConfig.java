package com.rdta.eag.epharma.repackageusecase;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class ReadConfig {
	
	private static final String PROPS_CONFIG = "Props.properties";
	static String emailFrom = null;
	static String emailTo = null;
	static String emailSubject = null;
	static String emailSMTP = null;
	static String emailUserName = null;
	static String emailPassword = null;
	static String fileUrl = null;
	static String errorUrl = null;
	static String processedUrl = null;
	static String signerid = null;
	static String deaNumber = null;
	static String sourceRoutingCode = null;
	
	private static Log log = LogFactory.getLog(ReadConfig.class);
	
	private String readPropertiesFile(){
		StringBuffer sb = new StringBuffer("");
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

			log.debug(properties.getProperty("prepackUsecase.fileUrl"));
			fileUrl = properties.getProperty("prepackUsecase.fileUrl");

			log.debug(properties.getProperty("prepackUsecase.errorUrl"));
			errorUrl = properties.getProperty("prepackUsecase.errorUrl");

			log.debug(properties.getProperty("prepackUsecase.processedUrl"));
			processedUrl = properties.getProperty("prepackUsecase.processedUrl");

			log.debug(properties.getProperty("signerId"));
			signerid = properties.getProperty("signerId");

			log.debug(properties.getProperty("deaNumber"));
			deaNumber = properties.getProperty("deaNumber");

			log.debug(properties.getProperty("sourceRoutingCode"));
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
            
		}catch(IOException e){
			log.error("Exception is :"+e);
		}
		return sb.toString();
	}
	
	public String ReadConfigFile(){
		
		String urlXmlString = readPropertiesFile();
		System.out.println("Result : ReadConfig "+urlXmlString);
		return urlXmlString;
		
	}
	
	public static void main(String arg[]){
		
		ReadConfig obj = new ReadConfig();
		obj.ReadConfigFile();
		
	}
}
