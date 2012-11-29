/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.notification;

import com.sourcen.core.App;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/29/12
 * Time: 1:18 PM
 * A Proxy interface for the SMTP service which allows to send out Email Notifications
 */
public class GenericSMTPProxy {

    private static final Logger log = LoggerFactory.getLogger(GenericSMTPProxy.class);
    private static GenericSMTPProxy instance;

    private Session session = null;
    private String host = null;
    private int port = 25;
    private String username = null;
    private String password = null;
    private String returnPath = null;

    private boolean debugEnabled = false;

    private EmailMessage emailMessage;

    private GenericSMTPProxy() {

    }

    //TODO: Later, we might have to take RetailerID for returning a retailer specific  SMTPProxy server
    public static GenericSMTPProxy getInstance() {
        if (instance == null) {
            instance = new GenericSMTPProxy();
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
        if (StringUtils.isEmpty(this.emailMessage.getSubject()) || StringUtils.isEmpty(this.emailMessage.getTextBody()) || StringUtils.isEmpty(this.emailMessage.getReplyTo().getEmail())
                || StringUtils.isEmpty(this.emailMessage.getReplyTo().getName()) || StringUtils.isEmpty(this.emailMessage.getSender().getEmail()) || StringUtils.isEmpty(this.emailMessage.getSender().getName())) {
            return false;
        }
        return configured;
    }

    /**
     * Send email notifications.
     *
     * @param messages The mail objects to send.
     *
     * @throws javax.mail.MessagingException If a connection was unable to be established.
     */
    public Collection<EmailAddressNotification> send(Collection<EmailAddressNotification> messages) {

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
        Collection<EmailAddressNotification> responseNotifications = new ArrayList<EmailAddressNotification>(messages.size());

        try {
            Iterator iter = messages.iterator();
            while (iter.hasNext()) {
                EmailAddressNotification notificationMsg = (EmailAddressNotification) iter.next();
                log.info(" Sending an email to   " + notificationMsg.getEmail());
                responseNotifications.add(sendEmail(notificationMsg));
            }
        } finally {

            session = null;
        }
        return responseNotifications;
    }

    //TODO: Need to add retailer based configuration in the future. Where we might have a specific Email properties

    /**
     * Initialize the Email Server related configuration from the specific App Profile: Development or UAT or Production
     */
    protected void initConfiguration() {
        ConfigurationService config = App.getService(ConfigurationService.class);
        emailMessage = new EmailMessage();
        emailMessage.setSender(config.getProperty("coupon.email.sender.name"), config.getProperty("coupon.email.sender.emailID"));
        emailMessage.setReplyTo(config.getProperty("coupon.email.replyTo.name"), config.getProperty("coupon.email.replyTo.emailID"));
        emailMessage.setSubject(config.getProperty("coupon.email.subject"));
        emailMessage.setTextBody(config.getProperty("coupon.email.textBody"));
        emailMessage.setHtmlBody(config.getProperty("coupon.email.htmlBody"));
        this.setDebugEnabled(config.getBooleanProperty("mail.debug"));
        this.setHost(config.getProperty("mail.smtp.host"));
        this.setPort(config.getIntegerProperty("mail.smtp.port", 25));
        this.setUsername(config.getProperty("mail.smtp.username"));
        this.setPassword(config.getProperty("mail.smtp.password"));
        this.setReturnPath(config.getProperty("mail.smtp.from"));
    }

    /**
     * Send an EmailNotification to the end user.
     *
     * @param emailAddressNotification the EmailAddressNotification object which contain the coupon n email address.
     *
     * @return
     */
    protected EmailAddressNotification sendEmail(EmailAddressNotification emailAddressNotification) {
        MimeMessage mimeMessage = new MimeMessage(this.session);

        try {


            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailMessage.getSender().getEmail(), emailMessage.getSender().getName()));
            InternetAddress[] addresses = new InternetAddress[]{new InternetAddress(emailMessage.getReplyTo().getEmail(), emailMessage.getReplyTo().getName())};
            message.setReplyTo(addresses);
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(emailAddressNotification.getEmail(), emailAddressNotification.getEmail()));
            message.setSubject(emailMessage.getSubject());

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            String textBody = String.format(emailMessage.getTextBody(), emailAddressNotification.getCoupon());
            messageBodyPart.setText(textBody);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);


            // Put parts in message
            message.setContent(multipart);
            //Send the message
            Transport.send(message);
            emailAddressNotification.setStatus("1");

        } catch (MessagingException e) {
            e.printStackTrace();
            emailAddressNotification.setStatus("-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            emailAddressNotification.setStatus("-1");
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
            // mailProps.put("mail.smtp.auth", "true");
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
