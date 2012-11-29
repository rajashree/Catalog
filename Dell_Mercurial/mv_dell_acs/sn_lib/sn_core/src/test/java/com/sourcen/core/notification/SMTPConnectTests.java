/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.notification;

import com.sourcen.core.test.SimpleTestCase;
import org.junit.Test;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/29/12
 * Time: 1:59 PM
 * Generic test cases for various Email Server connection
 */
public class SMTPConnectTests extends SimpleTestCase {

    /*private String host = "smtp.1and1.com";
    private String username = "test@snipl.com";
    private String password = "$niplpa55";*/

    private int securePort = 465;
    //private int port = 25;


    private String host = "smtp.gmail.com";
    private String username = "test@sourcen.com";
    private String password = "$niplpa55";
    private Properties props = new Properties();

    @Test
    public void testSendEmail() throws MessagingException, UnsupportedEncodingException {
        Authenticator auth = new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        Session session = Session.getInstance(getMailProps(), auth);
        // Transport transport = connectToSmtpServer();
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username, "Coupons"));
        message.addRecipient(Message.RecipientType.TO,
                new InternetAddress("vivek@sourcen.com", "Vivek Kondur"));
        message.setSubject("Thanks! for sharing.");

        // Create the message part
        BodyPart messageBodyPart = new MimeBodyPart();

        // Fill the message
        messageBodyPart.setText("Here is your coupon::: ABC123. Thanks for sharing.");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);


        // Put parts in message
        message.setContent(multipart);
        Transport.send(message);

        // Send the message
        // transport.send(message);
    }

    @Test
    public void testConnectToSmtpServer() throws MessagingException {
        URLName url = new URLName("smtp", host, securePort, "", username, password);
        Session session = Session.getInstance(getMailProps(), null);
        Transport trans = new com.sun.mail.smtp.SMTPTransport(session, url);
        trans.connect(host, securePort, username, password);
        logger.info(" THe transport layer " + trans);

    }

    public Transport connectToSmtpServer() throws MessagingException {
        URLName url = new URLName("smtp", host, securePort, "", username, password);
        Session session = Session.getInstance(getMailProps(), null);
        Transport trans = new com.sun.mail.smtp.SMTPTransport(session, url);
        trans.connect(host, securePort, username, password);
        return trans;
    }

    public Properties getMailProps() {
        //props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.username", username);
        props.put("mail.smtp.username", password);
        props.put("mail.debug", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        return props;
    }

}
