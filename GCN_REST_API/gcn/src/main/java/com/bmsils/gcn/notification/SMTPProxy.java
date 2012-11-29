/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.notification;


import com.bmsils.gcn.managers.impl.ConfigurationManagerImpl;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.*;

import static javax.mail.Message.RecipientType;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/30/12
 * Time: 6:49 PM
 * A SMTP service which allows to send out Email Notifications
 */

public class SMTPProxy {
    private static final Logger log = LoggerFactory.getLogger(SMTPProxy.class);
    private static SMTPProxy instance;

    private Session session = null;
    private String host = null;
    private int port = 25;
    private String username = null;
    private String password = null;
    private String returnPath = null;

    private boolean debugEnabled = false;

    private EmailMessage emailMessage;

    private SMTPProxy() {

    }

    public static SMTPProxy getInstance() {
        if (instance == null) {
            instance = new SMTPProxy();
        }
        return instance;
    }


    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

    /**
     * Sets the SMTP host (eg mail.example.com). The host is null by
     * default, but must be set before gateway exports can execute.
     *
     * @param host The SMTP host.
     */
    public void setHost(String host) {
        this.host = host;
        // JavaMail Session no longer valid so set to null; it will get
        // recreated as needed.
        session = null;
    }

    /**
     * Sets the port number that will be used when connecting to the SMTP
     * server. The default is 25, the standard SMTP port number.
     *
     * @param port The SMTP port number.
     */
    public void setPort(int port) {
        this.port = port;
        // JavaMail Session no longer valid so set to null; it will get
        // recreated as needed.
        session = null;
    }

    /**
     * Sets the username that will be used when connecting to the SMTP
     * server. The default is null, or no username.
     *
     * @param username The SMTP username.
     */
    public void setUsername(String username) {
        this.username = username;
        // JavaMail Session no longer valid so set to null; it will get
        // recreated as needed.
        session = null;
    }

    /**
     * Sets the username that will be used when connecting to the SMTP
     * server. The default is null, or no username.
     *
     * @param password The SMTP password.
     */
    public void setPassword(String password) {
        this.password = password;
        // JavaMail Session no longer valid so set to null; it will get
        // recreated as needed.
        session = null;
    }

    /**
     * Sets the return path (the mail.smtp.from property) for any outgoing emails. The
     * default is null, or no return path, which means bounces will go to the
     * address specified in the reply-to or from headers.
     *
     * @param returnPath the return path
     */
    public void setReturnPath(String returnPath) {
        this.returnPath = returnPath;

        // JavaMail Session no longer valid so set to null; it will get
        // recreated as needed.
        session = null;
    }


    /**
     * Creates a new {@link MimeMessage} instance that is specified the Message-ID generation scheme of
     * the default JavaMail configuration will be used.
     *
     * @return MimeMessage a new MessageObject
     */
    public MimeMessage createMessage() {
        retrieveSession();
        return new MimeMessage(session);
    }

    protected boolean isProxyConfigured() {
        boolean configured = true;
        //Email Server related properties
        if (StringUtils.isEmpty(this.host) || StringUtils.isEmpty(String.valueOf(this.port)) || StringUtils.isEmpty(this.username) ||
                StringUtils.isEmpty(this.password)) {
            return false;
        }
        //Email Retailer related properties
        if (StringUtils.isEmpty(this.emailMessage.getSubject()) || StringUtils.isEmpty(this.emailMessage.getTextBody())
                || StringUtils.isEmpty(this.emailMessage.getSender().getEmail()) || StringUtils.isEmpty(this.emailMessage.getSender().getName())) {
            return false;
        }
        return configured;
    }

    /**
     * Send email notifications.
     *
     * @param messages The mail objects to send.
     * @throws javax.mail.MessagingException If a connection was unable to be established.
     */

    public Collection<ForgotPwdNotification> send(Collection<ForgotPwdNotification> messages) {

        // If there are no messages, do nothing.
        if (messages.size() == 0) {
            log.info("No messages to notify.");
            return Collections.emptyList();
        }
        if (!isProxyConfigured()) {
            log.error("\n\n ### Missing Email Server or Email Template configuration properties. Please configure for the email notifications. ### \n\n");
            return Collections.emptyList();
        }
        retrieveSession();
        Collection<ForgotPwdNotification> responseNotifications = new ArrayList<ForgotPwdNotification>(messages.size());

        try {
            Iterator iter = messages.iterator();
            while (iter.hasNext()) {
                ForgotPwdNotification notificationMsg = (ForgotPwdNotification) iter.next();
                log.info(" Sending an email to   " + notificationMsg.getEmail());
                responseNotifications.add(sendEmail(notificationMsg));
            }
        } finally {

            session = null;
        }
        return responseNotifications;
    }

    /**
     * Initialize the Email Server related configuration from application.properties
     */
    public void initConfiguration() {
        ConfigurationManagerImpl config = ConfigurationManagerImpl.getInstance();
        emailMessage = new EmailMessage();
        emailMessage.setSender(config.getProperty("pwdRetrievalEmail.sender.name"), config.getProperty("pwdRetrievalEmail.sender.emailID"));
        emailMessage.setSubject(config.getProperty("pwdRetrievalEmail.subject"));
        emailMessage.setTextBody(config.getProperty("pwdRetrievalEmail.textBody"));
        emailMessage.setHtmlBody(config.getProperty("pwdRetrievalEmail.htmlBody"));
        this.setDebugEnabled(config.getBooleanProperty("mail.debug"));
        this.setHost(config.getProperty("mail.smtp.host"));
        this.setPort(config.getIntegerProperty("mail.smtp.port", new Integer(25)));
        this.setUsername(config.getProperty("mail.smtp.username"));
        this.setPassword(config.getProperty("mail.smtp.password"));
        this.setReturnPath(config.getProperty("mail.smtp.from"));
    }

    /**
     * method to forgot password notification email
     * @param emailAddressNotification
     * @return ForgotPwdNotification object
     */
    protected ForgotPwdNotification sendEmail(ForgotPwdNotification emailAddressNotification) {
        MimeMessage mimeMessage = new MimeMessage(this.session);
        ConfigurationManagerImpl config = ConfigurationManagerImpl.getInstance();
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailMessage.getSender().getEmail(), emailMessage.getSender().getName()));
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(emailAddressNotification.getEmail(), emailAddressNotification.getEmail()));
            message.setSubject(emailMessage.getSubject());

            // Create the message part
            MimeBodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            String pwdResetURL =String.format(config.getProperty("pwdResetURL"),emailAddressNotification.getEmail(),emailAddressNotification.getGcn(),emailAddressNotification.getPasswordToken());
            String textBody = String.format(emailMessage.getHtmlBody(),emailAddressNotification.getUsername(),pwdResetURL);
            messageBodyPart.setText(textBody,"UTF-8","html");


            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);


            // Put parts in message
            message.setContent(multipart);
            //Send the message
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log.info("Status of the email notification ---    " + emailAddressNotification.toString());

        return emailAddressNotification;
    }

    /**
     * Fetch an Authenticated session to the SMTP Server
     */
    public void retrieveSession() {

        // Create the mail session if necessary.
        if (session == null) {
            Properties mailProps = new Properties();
            mailProps.setProperty("mail.smtp.host", host);
            mailProps.setProperty("mail.smtp.port", String.valueOf(port));
            mailProps.setProperty("mail.smtp.username", username);
            mailProps.setProperty("mail.smtp.password", password);
            mailProps.setProperty("mail.debug", String.valueOf(debugEnabled));
            mailProps.put("mail.smtp.socketFactory.port", "465");
            mailProps.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            mailProps.put("mail.smtp.socketFactory.fallback", "false");


            // If a username is defined, use SMTP authentication.
            if (username != null) {
                mailProps.put("mail.smtp.auth", "true");
            }


            // return path is the address that bounces will be sent too
            if (returnPath != null && returnPath.length() > 0) {
                mailProps.put("mail.smtp.from", returnPath);
            }
            if (username != null && password != null) {
                Authenticator auth = new Authenticator() {

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                };
                session = Session.getInstance(mailProps, auth);
            } else {
                session = Session.getDefaultInstance(mailProps);
            }


        }
    }


    public Transport connectToSmtpServer() throws MessagingException {
        URLName url = new URLName("smtp", host, port, "", username, password);
        Transport trans = new com.sun.mail.smtp.SMTPTransport(session, url);
        trans.connect(host, port, username, password);
        return trans;
    }

    private void disconnectFromSmtpServer(Transport transport) throws MessagingException {
        transport.close();
    }
}
