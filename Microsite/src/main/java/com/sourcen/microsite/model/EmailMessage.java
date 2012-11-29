package com.sourcen.microsite.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EmailMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private EmailAddress sender;
	private String textBodyProperty;
	private boolean parse=true;

	private String textBody;
	private String htmlBody;
	private String subject;
	private Locale locale;
	private Collection<EmailAddress> recipients = new ArrayList<EmailAddress>();
	private Collection<EmailAddress> bccRecipients = new ArrayList<EmailAddress>();
	private Map<String, Object> context = new HashMap<String, Object>();


	public static class EmailAddress implements Serializable {

		private static final long serialVersionUID = 2351425691786526136L;

		private String name;
		private String email;

		public EmailAddress(String name, String email) {
			this.email = email;
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public String getName() {
			return name;
		}

		public String toString() {
			final StringBuffer sb = new StringBuffer();
			sb.append("EmailAddress");
			sb.append("{email='").append(email).append('\'');
			sb.append(", name='").append(name).append('\'');
			sb.append('}');
			return sb.toString();
		}
		

		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}

			EmailAddress that = (EmailAddress) o;

			if (email != null ? !email.equals(that.email) : that.email != null) {
				return false;
			}
			if (name != null ? !name.equals(that.name) : that.name != null) {
				return false;
			}

			return true;
		}

		public int hashCode() {
			int result;
			result = (name != null ? name.hashCode() : 0);
			result = 31 * result + (email != null ? email.hashCode() : 0);
			return result;
		}
	}
	 public Map<String, Object> getContext() {
	        return context;
	    }

	/**
	 * Sets the address the email is sent from.
	 * 
	 * @param name
	 *            Name of the sender
	 * @param email
	 *            Email address of the sender
	 */
	public void setSender(String name, String email) {
		this.sender = new EmailAddress(name, email);
	}

	public void addRecipient(EmailAddress emailAddress) {
		recipients.add(emailAddress);
	}

	public void addBccRecipient(EmailAddress emailAddress) {
		bccRecipients.add(emailAddress);
	}
	
	/**
	 * Sets the address the email is sent from. <p/> Sender only needs to be set
	 * if it is different than the system default.
	 * 
	 * @param sender
	 *            The sender's email address
	 */
	public void setSender(EmailAddress sender) {
		this.sender = sender;
	}

	/**
	 * Returns the subject of the email. Should be specified if subjectProperty
	 * is not specified.
	 * 
	 * @return the subject of the email.
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets the subject of the email. Should be specified if subjectProperty is
	 * not specified.
	 * 
	 * @param subject
	 *            the subject of the email.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Return the property name for the html version of the email body
	 * 
	 * @return the property name for the html version of the email body
	 */
	public String getTextBodyProperty() {
		return textBodyProperty;
	}

	/**
	 * Returns the text version of the body.
	 * 
	 * @return the text version of the body.
	 */
	public String getTextBody() {
		return textBody;
	}

	/**
	 * Sets the text version of the body.
	 * 
	 * @param textBody
	 *            the text version of the body.
	 */
	public void setTextBody(String textBody) {
		this.textBody = textBody;
	}

	/**
	 * Returns the html version of the body
	 * 
	 * @return the htmlVersion of the body.
	 */
	public String getHtmlBody() {
		return htmlBody;
	}

	/**
	 * Sets the text version of the body.
	 * 
	 * @param htmlBody
	 *            the text version of the body.
	 */
	public void setHtmlBody(String htmlBody) {
		this.htmlBody = htmlBody;
	}
	public void setContext(Map<String,Object> context) {
        this.context = context;
    }

	/**
	 * Return the locale for the email message. This in conjunction with the
	 * body|subject property name will be used to acquire the correct email
	 * content
	 * 
	 * @return the locale for the email message
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Set the locale for the email message. This in conjunction with the
	 * body|subject property name will be used to acquire the correct email
	 * content
	 * 
	 * @param locale
	 *            the locale for the email message
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public EmailAddress getSender() {
		// TODO Auto-generated method stub
		return sender;
	}

	public Collection<EmailAddress> getRecipients() {
		return recipients;
	}

	public Collection<EmailAddress> getBccRecipients() {
		return bccRecipients;
	}

	public boolean isParse() {
		return parse;
	}

	public void setParse(boolean parse) {
		this.parse = parse;
	}

	public void setRecipients(Collection<EmailAddress> recipients) {
		this.recipients = recipients;
	}

	public void setBccRecipients(Collection<EmailAddress> bccRecipients) {
		this.bccRecipients = bccRecipients;
	}

}
