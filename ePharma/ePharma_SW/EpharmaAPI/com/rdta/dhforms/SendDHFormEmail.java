
/********************************************************************************

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

*********************************************************************************/

 package com.rdta.dhforms;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



import sun.net.smtp.SmtpClient;

public class SendDHFormEmail {

	
	private static  String SMTP_AUTH_USER; 
	private static  String SMTP_AUTH_PWD;
	private static  String SMTP_SERVER;
	private static Log log=LogFactory.getLog(SendDHFormEmail.class);
	
	
	public static String sendDHFormEmail(String emailFrom, String emailTo, String smtpServer, String emailSubject, String emailMessage,String test)throws Exception {

	     String fromemail="tl-download@rainingdata.com";

	     try{
	         SmtpClient client = new SmtpClient(smtpServer); //"smtp.rainingdata.com"
	         client.from(emailFrom);
	         client.to(emailTo);
	         PrintStream message = client.startMessage();
	         message.println("From: " + fromemail);
	         message.println("To: " + emailTo);
	         message.println("Subject: "+emailSubject);
	         message.println();
	         message.println(emailMessage);
	         message.println();
	         client.closeServer();
	         //return true;
	         return "Success";
	      }
	        catch (java.io.IOException e){
	             System.out.println("ERROR SENDING EMAIL:"+e);    //return false;
	             return "Exception " + e.getMessage();
	             
	        }
	    }
	
	public static String sendDHFormEmailAttachement(
            String emailFrom, String emailTo, 
            String smtpServer,  String emailSubject, 
            String emailMessage, String uName, String pwd,String filepath) throws Exception{

        String fromemail="tl-download@rainingdata.com";
        SMTP_SERVER = smtpServer;
        
        // here we have to provide username and pwd
        
        SMTP_AUTH_USER = uName; 
        SMTP_AUTH_PWD = pwd;
        
        //String att = attachement;
        //log.info("attach: "+att);

        try{
            Properties props = new Properties();
            props.setProperty("mail.smtp.host",smtpServer);
            props.put("mail.debug", "true");
            // To authenticate user.
            props.put("mail.smtp.auth", "true");
            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getDefaultInstance(props, auth);
//          Define message
            
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(emailFrom));
            InternetAddress[] address = {new InternetAddress(emailTo)};
            
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(emailSubject);
            msg.setSentDate(new Date());

//             Create the message part 
                
            BodyPart messageBodyPart = new MimeBodyPart();

//          Fill the message
            messageBodyPart.setText(emailMessage);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

//          Part two is attachment
            messageBodyPart = new MimeBodyPart();
            
            File file = new File(filepath);       
            
            /*FileOutputStream fos = new FileOutputStream(file);
            fos.write(attachement.getBytes());
            fos.close();*/
         
            DataSource source = new FileDataSource(file);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(file.getName());
            multipart.addBodyPart(messageBodyPart);
                 
//          Put parts in message
            msg.setContent(multipart);

//          Send the message
            Transport.send(msg);
            
         
            System.out.println("before true");
           // file.delete();
            return "Success";
          
         }
           catch (Exception e){
                System.out.println("ERROR SENDING EMAIL WITH ATTACHEMENT:"+e);                
                StringBuffer sb = new StringBuffer();
                StackTraceElement trace[] = e.getStackTrace();
                for( int i =0;i<trace.length;i++){
                	sb.append(trace[i].toString()).append("....");
                }

                return "Exception" + sb.toString();
           }
       }
	public static String sendDHFormEmailMutipleAttachement(
            String emailFrom, String emailTo, 
            String smtpServer,  String emailSubject, 
            String emailMessage, String uName, String pwd,String[] filepath) throws Exception{

        String fromemail="tl-download@rainingdata.com";
        SMTP_SERVER = smtpServer;
        
        // here we have to provide username and pwd
        
        SMTP_AUTH_USER = uName; 
        SMTP_AUTH_PWD = pwd;
        
        //String att = attachement;
        //log.info("attach: "+att);

        try{
            Properties props = new Properties();
            props.setProperty("mail.smtp.host",smtpServer);
            props.put("mail.debug", "true");
            // To authenticate user.
            props.put("mail.smtp.auth", "true");
            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getDefaultInstance(props, auth);
//          Define message
            
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(emailFrom));
            InternetAddress[] address = {new InternetAddress(emailTo)};
            
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(emailSubject);
            msg.setSentDate(new Date());

//             Create the message part 
                
            BodyPart messageBodyPart = new MimeBodyPart();

//          Fill the message
            messageBodyPart.setText(emailMessage);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

//          Part two is attachment
            
            for (int i =0;i<filepath.length;i++){
            	messageBodyPart = new MimeBodyPart();
            System.out.println("Inside Loop : "+filepath[i]);
            File file = new File(filepath[i]);       
            
            /*FileOutputStream fos = new FileOutputStream(file);
            fos.write(attachement.getBytes());
            fos.close();*/
         
            DataSource source = new FileDataSource(file);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(file.getName());
            multipart.addBodyPart(messageBodyPart);
            
//          Put parts in message
            msg.setContent(multipart);
            
            }

//          Send the message
            Transport.send(msg);
            
         
            System.out.println("before true");
           // file.delete();
            return "Success";
          
         }
           catch (Exception e){
                System.out.println("ERROR SENDING EMAIL WITH ATTACHEMENT:"+e);                
                StringBuffer sb = new StringBuffer();
                StackTraceElement trace[] = e.getStackTrace();
                for( int i =0;i<trace.length;i++){
                	sb.append(trace[i].toString()).append("....");
                }

                return "Exception" + sb.toString();
           }
       }

   private static class SMTPAuthenticator extends Authenticator{

		  public PasswordAuthentication getPasswordAuthentication(){
		   String username = SMTP_AUTH_USER;
		   String password = SMTP_AUTH_PWD;
		   return new PasswordAuthentication(username, password);
		  }
   }
   
   public static void sendMailToSupport(String emailFrom, String emailTo, 
		   								 String smtpServer,  String emailSubject, 
		   								 String emailMessage, String uName, String pwd) throws Exception{
	   
	   SMTP_SERVER = smtpServer;      
       SMTP_AUTH_USER = uName; 
       SMTP_AUTH_PWD = pwd;
       try{
           Properties props = new Properties();
           props.setProperty("mail.smtp.host",smtpServer);
           props.put("mail.debug", "true");
           // To authenticate user.
           props.put("mail.smtp.auth", "true");
           Authenticator auth = new SMTPAuthenticator();
           Session session = Session.getDefaultInstance(props, auth);           
           Message msg = new MimeMessage(session);
           msg.setFrom(new InternetAddress(emailFrom));
           InternetAddress[] address = {new InternetAddress(emailTo)};           
           msg.setRecipients(Message.RecipientType.TO, address);
           msg.setSubject(emailSubject);
           msg.setSentDate(new Date());
//            Create the message part 
           BodyPart messageBodyPart = new MimeBodyPart();
//         Fill the message
           messageBodyPart.setText(emailMessage);
           Multipart multipart = new MimeMultipart();
           multipart.addBodyPart(messageBodyPart);
//        
//         Put parts in message
           msg.setContent(multipart);

//         Send the message
           Transport.send(msg);        	  
	  
       } catch (Exception e){
    	   System.out.println("ERROR SENDING EMAIL WITH ATTACHEMENT:"+e.getMessage());          
      }
       
   }	   
	
}
