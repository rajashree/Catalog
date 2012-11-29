package com.sourcen.space.service.impl;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import java.security.Security;
import java.util.*;

public class SmtpProxy {

	private Session session = null;
	private String host = null;
	private int port = 25;
	private String username = null;
	private String password = null;

	private boolean SSLEnabled = false;
	private boolean debugEnabled = false;

	
	private static final String SSL_FACTORY = "";

	/**
	 * Create a new instance.
	 */
	public SmtpProxy() {
	}

	
	public void setHost(String host) {
		this.host = host;
		session = null;
	}

	
	public void setPort(int port) {
		this.port = port;
		session = null;
	}

	
	public void setUsername(String username) {
		this.username = username;
		session = null;
	}

	
	public void setPassword(String password) {
		this.password = password;
		session = null;
	}


	public void setDebugEnabled(boolean debugEnabled) {
		this.debugEnabled = debugEnabled;
		session = null;
	}

	
	public void setSSLEnabled(boolean SSLEnabled) {
		this.SSLEnabled = SSLEnabled;

		session = null;
	}

	
	public MimeMessage createMessage() {
		retrieveSession();
		return new MimeMessage(session);
	}

	
	public void send(List messages) throws MessagingException {
		// If there are no messages, do nothing.
		if (messages.size() == 0) {
			return;
		}
		retrieveSession();
		Transport transport = null;

		try {
			transport = connectToSmtpServer();
			MimeMessage message;

			Iterator iter = messages.iterator();
			while (iter.hasNext()) {
				message = (MimeMessage) iter.next();
				
				try {
					transport.sendMessage(message, message.getAllRecipients());
				} catch (AddressException ae) {
					
				} catch (SendFailedException sfe) {
					
				}
			}
		} finally {
			if (transport != null) {
				try {
					disconnectFromSmtpServer(transport);
				} catch (MessagingException e) { /* ignore */
				}
			}
			session = null;
		}
	}

	private void retrieveSession() {
		
		if (SSLEnabled) {
			Security.setProperty("ssl.SocketFactory.provider", SSL_FACTORY);
		}

	
		if (session == null) {
			Properties mailProps = new Properties();
			mailProps.setProperty("mail.smtp.host", host);
			mailProps.setProperty("mail.smtp.port", String.valueOf(port));
			mailProps.setProperty("mail.debug", String.valueOf(debugEnabled));

			if (SSLEnabled) {
				mailProps.setProperty("mail.smtp.socketFactory.class",
						SSL_FACTORY);
				mailProps.setProperty("mail.smtp.socketFactory.fallback",
						"true");
			}
			
			if (username != null) {
				mailProps.put("mail.smtp.auth", "true");
			}
			session = Session.getInstance(mailProps, null);
		}
	}

	private Transport connectToSmtpServer() throws MessagingException {
		URLName url = new URLName("smtp", host, port, "", username, password);
		Transport trans = new com.sun.mail.smtp.SMTPTransport(session, url);
		trans.connect(host, port, username, password);
		return trans;
	}

	private void disconnectFromSmtpServer(Transport transport)
			throws MessagingException {
		transport.close();
	}
}
