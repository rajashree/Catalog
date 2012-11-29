package com.sourcen.space.service.impl;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javassist.NotFoundException;

import javax.mail.*;
import javax.mail.internet.*;

import com.sourcen.space.model.EmailMessage;
import com.sourcen.space.service.ApplicationManager;
import com.sourcen.space.service.EmailManager;


public class DefaultEmailManager implements EmailManager {

	protected Collection<EmailHandler> handlers;
	protected static boolean initialized = false;
	protected static final int MAX_MAILS_PER_SESSION = 25;
	protected static final int QUEUE_TRIGGER_LIMIT = 100;
	protected static final int NUMBER_CONNECTIONS = 1;
    private ApplicationManager applicationManager=null;
    private String host;
    private int port;
    private String user;
    private String password;
    
    
	protected BlockingQueue<EmailMessage> emailQueue = new LinkedBlockingQueue<EmailMessage>();
	protected static final DefaultEmailManager instance = new DefaultEmailManager();

	protected DefaultEmailManager() {
		
	}

	
	private void loadSetup(){
		try {
			host=applicationManager.getMailHost();
		} catch (NotFoundException e) {
			
			e.printStackTrace();
		}
		try {
			port=applicationManager.getMailPort();
		} catch (NotFoundException e) {
			
			e.printStackTrace();
		}
		try {
			user=applicationManager.getMailUser();
		} catch (NotFoundException e) {
			
			e.printStackTrace();
		}
		try {
			password=applicationManager.getMailPassword();
		} catch (NotFoundException e) {
			
			e.printStackTrace();
		}	
		
	}
	public static DefaultEmailManager getInstance() {
		return instance;
	}

	public void send(EmailMessage message) {
		try {
			emailQueue.put(message);
		} catch (InterruptedException e) {

		}
	}

	public void init() {
	
		loadSetup();
		if (!initialized) {
			int numberConnections = NUMBER_CONNECTIONS;
			handlers = new ArrayList<EmailHandler>(numberConnections);
			for (int i = 0; i < numberConnections; i++) {
				EmailHandler handler = new EmailHandler(host,port,user,password,i);
				handlers.add(handler);
				handler.start();
			}
			initialized = true;
		}
	}

	public boolean isEnabled() {

		return true;
	}

	public void start() {

	}

	public void stop() {

	}

	public void restart() {

	}

	public ApplicationManager getApplicationManager() {
		return applicationManager;
	}

	public void setApplicationManager(ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
	}
	protected class EmailHandler extends Thread {
		private int id;
		private SmtpProxy smtpProxy;
		private long lastRun = 0;
		private boolean running;
		protected static final int QUEUE_TRIGGER_LIMIT = 2;
		protected static final int MAX_MAILS_PER_SESSION = 25;
	    private String host="localhost";
	    private int port=25;
	    private String username=null;
	    private String password=null;
		
		EmailHandler(int id) {		
			running = true;
			this.setName("EmailHandler"+id);
		}
	  EmailHandler(String host,int port,String username,String password,int id) {
		  System.out.println("#EmailHandler:" + id + " initializing");
		    this.host=host;
			this.port=port;
			this.username=username;
			this.password=password;
			
			running = true;
			this.setName("EmailHandler " +id);
		}

		public void run() {

			
			while (running) {
				 
				if (shouldRun()) {
					 System.out.println("#EmailHandler:" + id + " running");

					smtpProxy = new SmtpProxy();
					smtpProxy.setDebugEnabled(false);
					smtpProxy.setSSLEnabled(false);
					smtpProxy.setHost(host);
					smtpProxy.setPort(port);
					smtpProxy.setUsername(username);
					smtpProxy.setPassword(password);

					while (!emailQueue.isEmpty()) {
						try {
							Collection<EmailMessage> messages = getMessagesFromQueue();
							process(messages);
						} catch (RuntimeException e) {
							e.printStackTrace();
						}

						if (!emailQueue.isEmpty()) {
							try {
								Thread.sleep(2 * 3600);
							} catch (InterruptedException e) {
	                          e.printStackTrace();
							}
						}
					}

					lastRun = System.currentTimeMillis();
				}

				try {
					Thread.sleep(2 * 3600);
				} catch (InterruptedException e) {
	              e.printStackTrace();
				}

			}
		}

		protected Collection<EmailMessage> getMessagesFromQueue() {
			Set<EmailMessage> mails = new LinkedHashSet<EmailMessage>();
			emailQueue.drainTo(mails, MAX_MAILS_PER_SESSION);
			return mails;
		}

		protected void process(Collection<EmailMessage> messages) {
			ArrayList<MimeMessage> mimeMessages = new ArrayList<MimeMessage>();

			for (final EmailMessage message : messages) {

				try {
					MimeMessage mimeMessage = processMessage(message);
					mimeMessages.add(mimeMessage);
				} catch (Exception e) {
	             e.printStackTrace();
				}
			}

			if (!mimeMessages.isEmpty()) {
				try {
					smtpProxy.send(mimeMessages);
				} catch (MessagingException e) {
	             e.printStackTrace();
				}
			}
		}

		private MimeMessage processMessage(EmailMessage message)
				throws MessagingException, UnsupportedEncodingException {
			String textBody = null;
			if (message.getTextBodyProperty() != null) {
				textBody = message.getTextBodyProperty();
			} else if (message.getTextBody() != null) {
				textBody = message.getTextBody();
			}

			String htmlBody = null;
			if (message.getHtmlBody() != null) {
				htmlBody = message.getHtmlBody();
			}

			String subject = null;
			if (message.getSubject() != null) {
				subject = message.getSubject();
			}

			if (subject == null) {
				String msg = "Could not determine an email string : key ";
				throw new IllegalStateException(msg);
			} else if (subject.trim().length() == 0) {

			}

			if (textBody == null && htmlBody == null) {
				String msg = "Could not determine an email body: text body key ";
				throw new IllegalStateException(msg);
			}

			MimeMessage mimeMessage = smtpProxy.createMessage();
			// set the date on exported message to be the current date
			String encoding = MimeUtility.mimeCharset(applicationManager
					.getCharacterEncoding());
			SimpleDateFormat format = new SimpleDateFormat(
					"EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
			format.setTimeZone(applicationManager.getApplicationTimeZone());
			mimeMessage.setHeader("Date", format.format(new Date()));
			mimeMessage.setHeader("Content-Transfer-Encoding", "8bit");
			mimeMessage.addRecipients(MimeMessage.RecipientType.TO,
					getRecipients(message));
			mimeMessage.addRecipients(MimeMessage.RecipientType.BCC,
					getBccRecipients(message));
			
			if (message.getSender() == null) {
				String email = "admin@localhost";
				String name = "Jive Administrator";
				mimeMessage.setFrom(new InternetAddress(email, name));
			} else {
				mimeMessage.setFrom(new InternetAddress(message.getSender()
						.getEmail(), message.getSender().getName()));
			}
			String replyToHeader = null;

			// if the reply to header was loaded above, set the mime message header
			if (replyToHeader != null && replyToHeader.length() > 0) {
				mimeMessage.setReplyTo(new InternetAddress[] { new InternetAddress(
						replyToHeader, "Jive Administrator") });
			}

			mimeMessage.setSubject(subject, encoding);
			// Create HTML, plain-text, or combination mimeMessagel
			if (textBody != null && htmlBody != null) {
				MimeMultipart content = new MimeMultipart("alternative");
				// Plain-text
				MimeBodyPart text = new MimeBodyPart();
				text.setText(textBody, encoding);
				text.setDisposition(Part.INLINE);
				content.addBodyPart(text);
				// HTML
				MimeBodyPart html = new MimeBodyPart();
				html.setContent(htmlBody, "text/html");
				html.setDisposition(Part.INLINE);
				content.addBodyPart(html);
				// process attachments
				processAttachments(message, content);
				// Add multipart to mimeMessage.
				mimeMessage.setContent(content);
				mimeMessage.setDisposition(Part.INLINE);
			} else if (textBody != null) {
				MimeBodyPart bPart = new MimeBodyPart();
				bPart.setText(textBody, encoding);
				bPart.setDisposition(Part.INLINE);
				MimeMultipart mPart = new MimeMultipart();
				mPart.addBodyPart(bPart);
				// process attachments
				processAttachments(message, mPart);
				mimeMessage.setContent(mPart);
				mimeMessage.setDisposition(Part.INLINE);
			} else if (htmlBody != null) {
				MimeBodyPart bPart = new MimeBodyPart();
				bPart.setContent(htmlBody, "text/html");
				bPart.setDisposition(Part.INLINE);
				MimeMultipart mPart = new MimeMultipart();
				mPart.addBodyPart(bPart);
				// process attachments
				processAttachments(message, mPart);
				mimeMessage.setContent(mPart);
				mimeMessage.setDisposition(Part.INLINE);
			}
			return mimeMessage;
		}

		private InternetAddress[] getBccRecipients(EmailMessage message) {
			ArrayList<InternetAddress> list = new ArrayList<InternetAddress>();

			for (Object o : message.getBccRecipients()) {
				EmailMessage.EmailAddress address = (EmailMessage.EmailAddress) o;
				try {
					list.add(new InternetAddress(address.getEmail(), address
							.getName()));
				} catch (UnsupportedEncodingException e) {
	                e.printStackTrace();
				}
			}

			return list.toArray(new InternetAddress[list.size()]);
		}
		protected void processAttachments(EmailMessage message, MimeMultipart mPart)
				throws MessagingException {

		}

		protected boolean shouldRun() {
			
			
			if (emailQueue != null && emailQueue.size() >= QUEUE_TRIGGER_LIMIT) {
				return true;
			}

			// If the last time we ran was 5 minutes or more we should run
			return System.currentTimeMillis() >= lastRun + 30 * 3600;
		}

		protected InternetAddress[] getRecipients(EmailMessage message) {
			ArrayList<InternetAddress> list = new ArrayList<InternetAddress>();

			for (Object o : message.getRecipients()) {
				EmailMessage.EmailAddress address = (EmailMessage.EmailAddress) o;
				try {
					list.add(new InternetAddress(address.getEmail(), address
							.getName()));
				} catch (UnsupportedEncodingException e) {
	                e.printStackTrace();
				}
			}

			return list.toArray(new InternetAddress[list.size()]);
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
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

	}	

}
