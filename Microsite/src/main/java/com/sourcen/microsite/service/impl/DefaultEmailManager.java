package com.sourcen.microsite.service.impl;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javassist.NotFoundException;

import javax.mail.*;
import javax.mail.internet.*;

import org.apache.log4j.Logger;

import com.sourcen.microsite.model.EmailMessage;
import com.sourcen.microsite.service.ApplicationManager;
import com.sourcen.microsite.service.EmailManager;
import com.sourcen.util.mail.SmtpMailer;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class DefaultEmailManager implements EmailManager {

	protected Collection<EmailHandler> handlers;
	protected static boolean initialized = false;
	protected static final int MAX_MAILS_PER_SESSION = 2;
	protected static final int QUEUE_TRIGGER_LIMIT = 100;
	protected static final int NUMBER_CONNECTIONS = 3;
	private ApplicationManager applicationManager = null;
	private String host;
	private int port;
	private String username;
	private String password;
	private String defaultEncoding;
	private static Logger log = Logger.getLogger("DefaultEmailManager");
	private Configuration configuration;

	protected BlockingQueue<EmailMessage> emailQueue = new LinkedBlockingQueue<EmailMessage>();
	protected static final DefaultEmailManager instance = new DefaultEmailManager();

	protected DefaultEmailManager() {

		configuration = new Configuration();
		configuration.setLocalizedLookup(false);

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
		log.info("Initializing Email Manager");
		initialized = true;
		try {
			host = applicationManager.getMailHost();
		} catch (NotFoundException e) {

			e.printStackTrace();

		}
		try {
			port = applicationManager.getMailPort();
		} catch (NotFoundException e) {

			e.printStackTrace();

		}
		try {
			username = applicationManager.getMailUser();
		} catch (NotFoundException e) {

			e.printStackTrace();

		}
		try {
			password = applicationManager.getMailPassword();
		} catch (NotFoundException e) {

			e.printStackTrace();

		}

		int numberConnections = NUMBER_CONNECTIONS;
		handlers = new ArrayList<EmailHandler>(numberConnections);
		for (int i = 0; i < numberConnections; i++) {
			EmailHandler handler = new EmailHandler(host, port, username,
					password, i);
			handlers.add(handler);

		}
		log.info("Initializing Email Manager-done");

	}

	public void start() {
		init();

		if (initialized) {

			if (handlers != null) {
				for (EmailHandler handler : handlers) {
					log.info("Email Handler #" + handler.getId() + " Started");
					handler.start();
				}
			}
		}
	}

	public void stop() {
		log.info("Stoping Email Handler-Started");
		if (initialized) {
			if (handlers != null) {
				for (EmailHandler handler : handlers) {
					handler.stopHandler();
				}
			}
		}
		log.info("Stoping Email Handler-Done");
	}

	public void restart() {
		log.info("Restarting Email Handler-Started");
		stop();
		start();
		log.info("Restarting Email Handler-Done");

	}

	public ApplicationManager getApplicationManager() {
		return applicationManager;
	}

	public void setApplicationManager(ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
	}

	protected class EmailHandler extends Thread {
		private int id;
		private SmtpMailer smtpProxy;
		private long lastRun = 0;
		private boolean running;
		protected static final int QUEUE_TRIGGER_LIMIT = 1;
		protected static final int MAX_MAILS_PER_SESSION = 25;
		private String host = "localhost";
		private int port = 25;
		private String username = null;
		private String password = null;

		EmailHandler(int id) {
			running = true;
			this.setName("EmailHandler" + id);
		}

		EmailHandler(String host, int port, String username, String password,
				int id) {
			log.info("#EmailHandler:" + id + " initializing");
			this.host = host;
			this.port = port;
			this.username = username;
			this.password = password;
			this.id = id;

			running = true;
			this.setName("EmailHandler " + id);
		}

		public void run() {

			while (running) {

				if (shouldRun()) {
					log.info("#EmailHandler:" + id + " running");

					smtpProxy = new SmtpMailer();
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
								System.out
										.println("Interrupted!-ReInterrupting");
								Thread.currentThread().interrupt();
							}
						}
					}

					lastRun = System.currentTimeMillis();
				}

				try {
					Thread.sleep(2 * 3600);
				} catch (InterruptedException e) {
					System.out.println("Interrupted!-ReInterrupting");
					Thread.currentThread().interrupt();
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
			String htmlBody = null;
			String subject = null;

			if (message.isParse()) {

				
				subject = processMessage(message.getSubject(), message
						.getContext());
				htmlBody = processMessage(message.getHtmlBody(), message
						.getContext());

			} else {
				htmlBody = message.getHtmlBody();
				subject = message.getSubject();

			}

			if (subject == null) {
				String msg = "Could not determine an email string : ";
				throw new IllegalStateException(msg);
			} else if (subject.trim().length() == 0) {

			}

			if (htmlBody == null) {
				String msg = "Could not determine an email body: text body  ";
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

			
			if (replyToHeader != null && replyToHeader.length() > 0) {
				mimeMessage
						.setReplyTo(new InternetAddress[] { new InternetAddress(
								replyToHeader, "Administrator") });
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

		public String processMessage(String message, Object hash) {
			Template template = null;
			StringWriter out = null;
			try {
				StringTemplateLoader stringLoader = new StringTemplateLoader();
				stringLoader.putTemplate("default", message);

				configuration.setTemplateLoader(stringLoader);

				template = configuration.getTemplate("default");

				out = new StringWriter();
				template.process(hash, out);

			} catch (Exception e) {

				e.printStackTrace();
			}

			String content = out.toString();

			return content;
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

		protected void processAttachments(EmailMessage message,
				MimeMultipart mPart) throws MessagingException {

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

		public synchronized void stopHandler() {
			this.running = false;
			this.interrupt();
		}

		public long getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

	}

	
	public void send(String body, String subject, String from, String to) {

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

	public String getDefaultEncoding() {
		return defaultEncoding;
	}

	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}

	
	public boolean enable() {
		return true;
		// TODO Auto-generated method stub
		
	}

}
