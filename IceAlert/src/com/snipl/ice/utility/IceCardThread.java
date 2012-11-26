package com.snipl.ice.utility;

/**
* @Author Kamalakar Challa
*   
*/
import javax.mail.MessagingException;

import com.snipl.ice.mail.SendMailUsingAuthentication;

public class IceCardThread extends Thread{
	String recipients[]=null,recipientscc[]=null,recipientsbcc[]=null,subject=null,message=null,from=null,filename=null,username=null;
	
	SendMailUsingAuthentication smtpMailSender = new SendMailUsingAuthentication();
	
	public IceCardThread(String recipients[], String recipientscc[],
			String recipientsbcc[], String subject, String message, String from,String filename,String username )
	{
		this.recipients=recipients;
		this.recipientsbcc=recipientsbcc;
		this.recipientscc=recipientscc;
		this.subject=subject;
		this.message=message;
		this.from=from;
		this.filename=filename;
		this.username=username;
		Thread t=new Thread();
	}

	public void run() {
		try {
			smtpMailSender.postMail(recipients, recipientscc, recipientsbcc, subject, message, from,filename,username);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
