package com.rdta.eag.epharma.util;

import java.util.*;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

public class SendMail
{

public void send(String smtpHost, String smtpPort, String from, String to, String pass, String subject, String content, 
         String contentType)
     throws AddressException, MessagingException
 {
	System.out.println("***** Sending mail ***** ");
     String toAddrs = to;
     Properties props = new Properties();
     StringTokenizer strToken = new StringTokenizer(toAddrs, ",");
     int count = strToken.countTokens();
     String recipientList[] = new String[count];
     for(int i = 0; strToken.hasMoreElements(); i++)
         recipientList[i] = strToken.nextToken();

     props.put("mail.smtp.host", smtpHost);
     props.put("mail.smtp.port", smtpPort);
     props.put("mail.smtp.auth", "true");
     Authenticator auth = new SMTPAuthenticator(from, pass);
     for(int j = 0; j < recipientList.length; j++)
     {
         Session session = Session.getDefaultInstance(props, auth);
         Message msg = new MimeMessage(session);
         msg.setFrom(new InternetAddress(from));
         msg.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(recipientList[j]));
         msg.setSubject(subject);
         if(contentType.equals("body"))
         {
             msg.setText(content);
             Transport.send(msg);
         }
         if(contentType.equals("attachement"))
         {
             MimeBodyPart mbp2 = new MimeBodyPart();
             FileDataSource fds = new FileDataSource(content);
             mbp2.setDataHandler(new DataHandler(fds));
             mbp2.setFileName(fds.getName());
             Multipart mp = new MimeMultipart();
             mp.addBodyPart(mbp2);
             msg.setContent(mp);
             msg.setSentDate(new Date());
             Transport.send(msg);
         }
     }

 }

private class SMTPAuthenticator extends Authenticator
{

    public PasswordAuthentication getPasswordAuthentication()
    {
        return new PasswordAuthentication(userName, password);
    }

    String userName;
    String password;

    SMTPAuthenticator(String userName, String password)
    {
        this.userName = userName;
        this.password = password;
    }
}

/*public static void main(String s[]) throws Exception{
	
	SendMail obj = new SendMail();
	obj.send("smtp.bizmail.yahoo.com","25","testepharma@sourcen.com","venu.gopal@sourcen.com","sniplpass","Testing....venu","Testing...","body");
}*/
}