package com.sourcen.microsite.action.admin;

import javassist.NotFoundException;

import com.sourcen.microsite.model.EmailMessage;
import com.sourcen.microsite.model.Property;
import com.sourcen.microsite.model.EmailMessage.EmailAddress;
import com.sourcen.util.mail.SmtpMailer;

/*
 * Revision: 1.0
 * Date: October 25, 2008
 *
 * Copyright (C) 2005 - 2008 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 *
 * By : Chandra Shekher
 *
 */

public class EmailSettingsAction extends AdminAction {

	private String host;
	private String username;
	private String password;
	private int port;
	private String from;
	private String to;
	private String subject="Test Email Subject";
	private String body="Test Email Body";
	private boolean restart;

	public String execute() {
		this.actionIndex = 2;
		Property property = new Property();
		property.setKey(SmtpMailer.HOST);
		property.setValue(host);
		this.applicationManager.saveProperty(property);

		property.setKey(SmtpMailer.PORT);
		property.setValue(String.valueOf(port));

		this.applicationManager.saveProperty(property);
		property.setKey(SmtpMailer.USERNAME);
		property.setValue(username);

		this.applicationManager.saveProperty(property);
		property.setKey(SmtpMailer.PASSWORD);
		property.setValue(password);

		this.applicationManager.saveProperty(property);
		if(restart)
			this.emailManager.restart();
		return SUCCESS;

	}

	public String input() {
		this.actionIndex = 2;
		loadSettings();

		return INPUT;

	}

	public String testMailInput() {
		this.actionIndex = 2;
		from = this.getUser().getEmail();
		to = this.getUser().getEmail();
		try {
			host = this.applicationManager.getMailHost();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		return "testinput";

	}

	public String testMailSettings() {
		this.actionIndex = 2;

		EmailMessage message = new EmailMessage();
		message.setHtmlBody(body);
		message.setSender(this.getUser().getUsername(), from);
		message.setSubject(subject);
		message.addRecipient(new EmailAddress("testMail", to));
		this.emailManager.send(message);

		// return ERROR;
		return SUCCESS;

	}

	public void loadSettings() {
		try {
			host = this.applicationManager.getMailHost();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			port = this.applicationManager.getMailPort();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			username = this.applicationManager.getMailUser();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			password = this.applicationManager.getMailPassword();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public boolean isRestart() {
		return restart;
	}

	public void setRestart(boolean restart) {
		this.restart = restart;
	}

}
