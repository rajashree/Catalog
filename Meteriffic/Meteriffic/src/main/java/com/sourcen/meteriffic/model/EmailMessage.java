/*
 * Copyright (C) 2007-2009 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 * 
 */
package com.sourcen.meteriffic.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Eclipse.
 * User : rajashreem
 * Date : Jun 9, 2009
 * Time : 3:31:20 PM
 */

public class EmailMessage implements Serializable{
	
	 private EmailAddress sender;
	 private String textBody;
	 private String htmlBody;
	 private String textBodyProperty;
	 private String subject;
	 private Collection<EmailAddress> recipients = new ArrayList<EmailAddress>();
	 private Collection<EmailAddress> bccRecipients = new ArrayList<EmailAddress>();
	 
	 
	
	 public static class EmailAddress implements Serializable{
		 
		private static final long serialVersionUID = -2283726716468804311L;
		private String name;
		 private String email;
		 
		 public EmailAddress(String name, String email){
			 this.email = email;
			 this.name = name;
		 }

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the email
		 */
		public String getEmail() {
			return email;
		}
		
	 }



	/**
	 * @return the sender
	 */
	public EmailAddress getSender() {
		return sender;
	}



	/**
	 * @param sender the sender to set
	 */
	public void setSender(EmailAddress sender) {
		this.sender = sender;
	}



	/**
	 * @return the textBody
	 */
	public String getTextBody() {
		return textBody;
	}



	/**
	 * @param textBody the textBody to set
	 */
	public void setTextBody(String textBody) {
		this.textBody = textBody;
	}



	/**
	 * @return the htmlBody
	 */
	public String getHtmlBody() {
		return htmlBody;
	}



	/**
	 * @param htmlBody the htmlBody to set
	 */
	public void setHtmlBody(String htmlBody) {
		this.htmlBody = htmlBody;
	}



	/**
	 * @return the textBodyProperty
	 */
	public String getTextBodyProperty() {
		return textBodyProperty;
	}



	/**
	 * @param textBodyProperty the textBodyProperty to set
	 */
	public void setTextBodyProperty(String textBodyProperty) {
		this.textBodyProperty = textBodyProperty;
	}



	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}



	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}



	/**
	 * @return the recipients
	 */
	public Collection<EmailAddress> getRecipients() {
		return recipients;
	}



	/**
	 * @param recipients the recipients to set
	 */
	public void setRecipients(Collection<EmailAddress> recipients) {
		this.recipients = recipients;
	}



	/**
	 * @return the bccRecipients
	 */
	public Collection<EmailAddress> getBccRecipients() {
		return bccRecipients;
	}



	/**
	 * @param bccRecipients the bccRecipients to set
	 */
	public void setBccRecipients(Collection<EmailAddress> bccRecipients) {
		this.bccRecipients = bccRecipients;
	}

	
	public void addRecipient(EmailAddress emailAddress) {
		recipients.add(emailAddress);
	}

	public void addBccRecipient(EmailAddress emailAddress) {
		bccRecipients.add(emailAddress);
	}
	
}
