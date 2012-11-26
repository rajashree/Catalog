package com.snipl.ice.community;

/**
* @Author Kamalakar Challa 
*   
*/
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mysql.jdbc.ResultSet;
import com.snipl.ice.mail.SendMailUsingAuthentication;
import com.snipl.ice.security.Dao;
import com.snipl.ice.utility.IceThread;
import com.snipl.ice.utility.MailUtility;
import com.snipl.ice.utility.UserUtility;

public class InviteCommunityAction  extends Action
{
	SendMailUsingAuthentication smtpMailSender = new SendMailUsingAuthentication();
	
	public ActionForward execute(
									ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response) 
									throws Exception
	{
		if(request.getSession().getAttribute("security_id")!=null)
		{
			InviteCommunityForm inv_form = (InviteCommunityForm) form;
			String email = inv_form.getEmail_data();
			int commid=Integer.parseInt(inv_form.getComid());
			String inv_msg = inv_form.getId_inv_msg();
			
			String reg_data="";
			String new_data="";
			String re_data="";
			@SuppressWarnings("unused")
			boolean sendmail = false;
			
			
			ResultSet rs;
			Dao d = new Dao();
			
			String subject=null;
			String message=null;
			String from = new UserUtility().getUserEmail(Integer.parseInt(request.getSession().getAttribute("security_id").toString()));
			
			String[] emailList = {};
			String[] recipientscc = {}, recipientsbcc = {};
			
			StringTokenizer str = new StringTokenizer(email,"|");
			
			while(str.hasMoreTokens())
			{
				String temp=str.nextToken();
				int seach_id = new UserUtility().getIDbyEmailID(temp);
				if(seach_id == 0)
				{
					reg_data+=temp.trim()+"|";
					sendmail = true;
				}
				else
				{
					rs=d.executeQuery("select * from community_assigned where user_id="+seach_id+" and comm_id="+commid);
					if(rs.next())
					{
						if(rs.getInt("flag")== 0)
						{
							re_data+=temp.trim()+"|";
							sendmail = true;
						}
						else
						{
							sendmail = false;
						}
						
					}
					else
					{
						new_data+=temp.trim()+"|";
						sendmail = true;
					}
				}
			}
	
			//send new acc invit
			str = new StringTokenizer(reg_data,"|");		
			recipientsbcc= new String[str.countTokens()];
			
			int i_temp=0;
	
			while(str.hasMoreTokens())
			{
				recipientsbcc[i_temp]=str.nextToken().trim();
				rs=d.executeQuery("select * from community_dump where user_emailid='"+recipientsbcc[i_temp]+"' and comm_id="+commid);
				if(!rs.next())
				{
					d.executeUpdate("insert into community_dump values("+commid+",'"+recipientsbcc[i_temp]+"')");
				}
				i_temp++;
			}
			
			message=new MailUtility().getNewRegBodyForComm(commid,inv_msg, from);
			subject="Your Friend invited you please register";
			
			
			
			if(i_temp > 0 )
			{
				new IceThread(emailList, recipientscc, recipientsbcc, subject, message, from).start();
				
			}
			
		//send new invit
			str = new StringTokenizer(new_data,"|");
			recipientsbcc= new String[str.countTokens()];
			
			i_temp=0;
	
			while(str.hasMoreTokens())
			{
				recipientsbcc[i_temp]=str.nextToken().trim();
				d.executeUpdate("insert into community_assigned values("+commid+","+new UserUtility().getIDbyEmailID(recipientsbcc[i_temp])+",0)");
				i_temp++;
			}
			
			message=new MailUtility().getNewReqBodyForComm(commid,inv_msg, from);
			subject="Your Friend invited you please accept";
			
			if(i_temp > 0 )
			{
				new IceThread(emailList, recipientscc, recipientsbcc, subject, message, from).start();
				
			}
			
		//send re invit
			str = new StringTokenizer(re_data,"|");
			recipientsbcc= new String[str.countTokens()];
			
			i_temp=0;
	
			while(str.hasMoreTokens())
			{
				recipientsbcc[i_temp]=str.nextToken();
				i_temp++;
			}
			
			message=new MailUtility().getNewReBodyForComm(commid,inv_msg, from);
			subject="Your Friend invited you again please accept";
			
			if(i_temp > 0 )
			{
				new IceThread(emailList, recipientscc, recipientsbcc, subject, message, from).start();
				
			}
			return mapping.findForward("success");
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
		
	}
}
