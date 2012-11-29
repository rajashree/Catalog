/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.notification;

import java.io.Serializable;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/30/12
 * Time: 6:49 PM
 * An EmailMessage is bean object for email related details
 */

public class EmailMessage implements Serializable {

    private static final long serialVersionUID = -3918232520754145235L;

    private Collection<EmailAddress> recipients = new ArrayList<EmailAddress>();
    private EmailAddress sender;
    private String subjectProperty;

    private String textBody;
    private String htmlBody;
    private String subject;
    private Locale locale;
    private boolean bccSender = false;
    private Map<String, Object> context = new HashMap<String, Object>();


    /**
     * Adds a recipient (to address) for this email message
     *
     * @param emailAddress the recipient's address
     */
    public void addRecipient(EmailAddress emailAddress) {
        recipients.add(emailAddress);
    }


    /**
     * Adds a recipient (to address)  for this email message
     *
     * @param name  the name of the recipient
     * @param email the email address of the recipient
     */
    public void addRecipient(String name, String email) {
        recipients.add(new EmailAddress(name, email));
    }


    /**
     * Sets the address the email is sent from.
     *
     * @param name  Name of the sender
     * @param email Email address of the sender
     */
    public void setSender(String name, String email) {
        this.sender = new EmailAddress(name, email);
    }

    /**
     * Sets the address the email is sent from.
     * <p/>
     * Sender only needs to be set if it is different than the system default.
     *
     * @param sender The sender's email address
     */
    public void setSender(EmailAddress sender) {
        this.sender = sender;
    }

    /**
     * If copySender is set to true, the sender will be BCCed on the message.  Default is false.
     *
     * @param bccSender Whether or not to copy the sender on the email.
     */
    public void setBccSender(boolean bccSender) {
        this.bccSender = bccSender;
    }

    /**
     * If copySender is set to true, the sender will be BCCed on the message.  Default is false.
     *
     * @return Whether or not to copy the sender on the email.
     */
    public boolean isBccSender() {
        return bccSender;
    }


    /**
     * Sets the email's subject property name
     *
     * @param subjectProperty the email's subject
     */
    public void setSubjectProperty(String subjectProperty) {
        this.subjectProperty = subjectProperty;
    }


    /**
     * Returns the recipients of the email
     *
     * @return the recipients of the email
     */
    public Collection<EmailAddress> getRecipients() {
        return recipients;
    }


    /**
     * Returns the sender of them email
     *
     * @return the sender of the email
     */
    public EmailAddress getSender() {
        return sender;
    }

    /**
     * Returns the property name for subject of the email
     *
     * @return the property name for subject of the email
     */
    public String getSubjectProperty() {
        return subjectProperty;
    }

    /**
     * Returns the subject of the email. Should be specified if subjectProperty is not specified.
     *
     * @return the subject of the email.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject of the email. Should be specified if subjectProperty is not specified.
     *
     * @param subject the subject of the email.
     */
    public void setSubject(String subject) {
        this.subject = subject;
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
     * @param textBody the text version of the body.
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
     * @param htmlBody the text version of the body.
     */
    public void setHtmlBody(String htmlBody) {
        this.htmlBody = htmlBody;
    }

    /**
     * Return the locale for the email message. This in conjunction with the body|subject property name will
     * be used to acquire the correct email content
     *
     * @return the locale for the email message
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Set the locale for the email message. This in conjunction with the body|subject property name will
     * be used to acquire the correct email content
     *
     * @param locale the locale for the email message
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * Returns a map of replacement tokens to use in the email
     *
     * @return a map of replacement tokens
     */
    public Map<String, Object> getContext() {
        return context;
    }

    /**
     * Sets the context map.
     *
     * @param context the context.
     */
    public void setContext(Map<String, Object> context) {
        this.context = context;
    }


    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailMessage)) {
            return false;
        }

        EmailMessage that = (EmailMessage) o;

        if (bccSender != that.bccSender) {
            return false;
        }

        if (context != null ? !context.equals(that.context) : that.context != null) {
            return false;
        }
        if (htmlBody != null ? !htmlBody.equals(that.htmlBody) : that.htmlBody != null) {
            return false;
        }

        if (locale != null ? !locale.equals(that.locale) : that.locale != null) {
            return false;
        }
        if (recipients != null ? !recipients.equals(that.recipients) : that.recipients != null) {
            return false;
        }
       if (sender != null ? !sender.equals(that.sender) : that.sender != null) {
            return false;
        }
        if (subject != null ? !subject.equals(that.subject) : that.subject != null) {
            return false;
        }
        if (subjectProperty != null ? !subjectProperty.equals(that.subjectProperty) : that.subjectProperty != null) {
            return false;
        }
        if (textBody != null ? !textBody.equals(that.textBody) : that.textBody != null) {
            return false;
        }
        return !(textBody != null ? !textBody.equals(that.textBody) : that.textBody != null);

    }

    public int hashCode() {
        int result = 0;
        result = 31 * result + (recipients != null ? recipients.hashCode() : 0);
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        result = 31 * result + (subjectProperty != null ? subjectProperty.hashCode() : 0);
        result = 31 * result + (textBody != null ? textBody.hashCode() : 0);
        result = 31 * result + (htmlBody != null ? htmlBody.hashCode() : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (locale != null ? locale.hashCode() : 0);
        result = 31 * result + (bccSender ? 1 : 0);
        result = 31 * result + (context != null ? context.hashCode() : 0);
        return result;
    }

    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("EmailMessage");
        sb.append("{recipients=").append(recipients);
        sb.append(", sender=").append(sender);
        sb.append(", subjectProperty='").append(subjectProperty).append('\'');

        sb.append(", textBody='").append(textBody).append('\'');
        sb.append(", htmlBody='").append(htmlBody).append('\'');
        sb.append(", subject='").append(subject).append('\'');
        sb.append(", locale=").append(locale);
        sb.append(", context=").append(context);
        sb.append(", bccSender=").append(bccSender);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Represents an email address with a name like Vivek Kondur &lt;vivek@sourcen.com&gt;
     */
    public static class EmailAddress implements Serializable {

        private static final long serialVersionUID = 2351425691786526136L;

        private String name;
        private String email;

        /**
         * Constructor for serialization use only.
         */
        public EmailAddress() {
        }

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
            return !(name != null ? !name.equals(that.name) : that.name != null);

        }

        public int hashCode() {
            int result;
            result = (name != null ? name.hashCode() : 0);
            result = 31 * result + (email != null ? email.hashCode() : 0);
            return result;
        }
    }
}
