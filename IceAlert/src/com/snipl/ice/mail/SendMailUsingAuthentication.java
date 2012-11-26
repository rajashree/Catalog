package com.snipl.ice.mail;

/**
* @Author Sankara Rao & Kamalakar Challa
*   
*/
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

import com.snipl.ice.config.ICEEnv;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Date;

public class SendMailUsingAuthentication {

	private static final String SMTP_HOST_NAME = ICEEnv.getInstance()
			.getSmtp_host_name();

	private static final String SMTP_AUTH_USER = ICEEnv.getInstance()
			.getSmtp_auth_user();

	private static final String SMTP_AUTH_PWD = ICEEnv.getInstance()
			.getSmtp_auth_pwd();

	public boolean postMail(String recipients[], String recipientscc[],
			String recipientsbcc[], String subject, String message, String from)
			throws MessagingException {
		boolean debug = false;
		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		Authenticator auth = new SMTPAuthenticator();
		Session session = Session.getDefaultInstance(props, auth);
		session.setDebug(debug);
		Message msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);
		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			// System.out.println(recipients[i]);
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		InternetAddress[] addressCC = new InternetAddress[recipientscc.length];
		for (int i = 0; i < recipientscc.length; i++)
			addressCC[i] = new InternetAddress(recipientscc[i]);
		msg.setRecipients(Message.RecipientType.CC, addressCC);

		InternetAddress[] addressBCC = new InternetAddress[recipientsbcc.length];
		for (int i = 0; i < recipientsbcc.length; i++)
			addressBCC[i] = new InternetAddress(recipientsbcc[i]);
		msg.setRecipients(Message.RecipientType.BCC, addressBCC);

		msg.setSubject(subject);
		msg.setSentDate(new Date());

		msg.setDataHandler(new DataHandler(new HTMLDataSource(message)));

		Transport.send(msg);
		return true;
	}
	
	public boolean postMail(String recipients[], String recipientscc[],
			String recipientsbcc[], String subject, String message, String from,String filename,String username)
			throws MessagingException {
		boolean debug = false;
		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		Authenticator auth = new SMTPAuthenticator();
		Session session = Session.getDefaultInstance(props, auth);
		session.setDebug(debug);
		Message msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);
		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		InternetAddress[] addressCC = new InternetAddress[recipientscc.length];
		for (int i = 0; i < recipientscc.length; i++)
			addressCC[i] = new InternetAddress(recipientscc[i]);
		msg.setRecipients(Message.RecipientType.CC, addressCC);

		InternetAddress[] addressBCC = new InternetAddress[recipientsbcc.length];
		for (int i = 0; i < recipientsbcc.length; i++)
			addressBCC[i] = new InternetAddress(recipientsbcc[i]);
		msg.setRecipients(Message.RecipientType.BCC, addressBCC);

		msg.setSubject(subject);
		msg.setSentDate(new Date());
		
		//msg.setDataHandler();
		 
	    MimeBodyPart p3 = new MimeBodyPart();
	    MimeBodyPart p1 = new MimeBodyPart();
	    p1.setDataHandler(new DataHandler(new HTMLDataSource(message)));
	    
	    FileDataSource fds2 = new FileDataSource(filename);
	    p3.setDataHandler(new DataHandler(fds2));
	    p3.setFileName(fds2.getName());
	    Multipart mp = new MimeMultipart();
	    mp.addBodyPart(p3);
	    mp.addBodyPart(p1);
	    msg.setContent(mp);
	   
	    Transport.send(msg);
		return true;
	}

	static class HTMLDataSource implements DataSource {
		private String html;

		public HTMLDataSource(String htmlString) {
			html = htmlString;
		}

		public InputStream getInputStream() throws IOException {
			if (html == null)
				throw new IOException("Null HTML");
			return new ByteArrayInputStream(html.getBytes());
		}

		public OutputStream getOutputStream() throws IOException {
			throw new IOException("This DataHandler cannot write HTML");
		}

		public String getContentType() {
			return "text/html";
		}

		public String getName() {
			return "JAF text/html dataSource to send e-mail only";
		}
	}

	private class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			String username = SMTP_AUTH_USER;
			String password = SMTP_AUTH_PWD;
			return new PasswordAuthentication(username, password);
		}
	}
}