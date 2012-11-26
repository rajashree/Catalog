package com.snipl.ice.utility;

import javax.mail.MessagingException;

import com.snipl.ice.mail.SendMailUsingAuthentication;

public class IceThread extends Thread{
	String recipients[]=null,recipientscc[]=null,recipientsbcc[]=null,subject=null,message=null,from=null;
	
	SendMailUsingAuthentication smtpMailSender = new SendMailUsingAuthentication();
	
	public IceThread(String recipients[], String recipientscc[],
			String recipientsbcc[], String subject, String message, String from )
	{
		this.recipients=recipients;
		this.recipientsbcc=recipientsbcc;
		this.recipientscc=recipientscc;
		this.subject=subject;
		this.message=message;
		this.from=from;
		Thread t=new Thread();
	}

	public void run() {
		try {
			smtpMailSender.postMail(recipients, recipientscc, recipientsbcc, subject, message, from);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
