/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.notification;


import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/30/12
 * Time: 12:48 AM
 * Test case for sending an email with Gmail SMTP configuration
 */
public class GmailTest {

    @Test
    public void testEmailWithGmailSettings() {
        // try {
//            Properties props = System.getProperties();
//            //props.put("mail.smtp.starttls.enable", "true");
//            props.put("mail.smtp.host", "smtp.gmail.com");
//            props.put("mail.smtp.auth", "true");
//            props.put("mail.smtp.port", "465"); // smtp port
//            props.put("mail.smtp.socketFactory.port", "465");
//            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//            props.put("mail.smtp.socketFactory.fallback", "false");
//            logger.info("#########################");
//            Authenticator auth = new Authenticator() {
//
//                @Override
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication("test@sourcen.com", "$niplpa55");
//                }
//            };
//            logger.info("$$$$$$$$$$$$$$$$$$$$");
//            Session session = Session.getDefaultInstance(props, auth);
//            MimeMessage msg = new MimeMessage(session);
//            msg.setFrom(new InternetAddress("username-gmail@gmail.com"));
//            msg.setSubject("Try attachment gmail");
//            msg.setRecipient(Message.RecipientType.TO, new InternetAddress("vivek@sourcen.com"));
//            msg.setText("Hello how are you doing today? \n\n Cheers!");
//            //add atleast simple body
//            MimeBodyPart body = new MimeBodyPart();
//            body.setText("Try attachment");
        //do attachment
        /* MimeBodyPart attachMent = new MimeBodyPart();
    FileDataSource dataSource = new FileDataSource(new File("file-sent.txt"));
    attachMent.setDataHandler(new DataHandler(dataSource));
    attachMent.setFileName("file-sent.txt");
    attachMent.setDisposition(MimeBodyPart.ATTACHMENT);
    Multipart multipart = new MimeMultipart();
    multipart.addBodyPart(body);
    multipart.addBodyPart(attachMent);
      msg.setContent(multipart);  */
        //   Transport.send(msg);
//        } catch (AddressException ex) {
//            ex.printStackTrace();
//            //Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (MessagingException ex) {
//            ex.printStackTrace();
//            //s Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
//        }


    }


}
