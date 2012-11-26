package com.snipl.ice.security;


/**
* @Author Chethan jayaraj 
*   
*/

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mysql.jdbc.ResultSet;
import com.snipl.ice.config.ICEEnv;
import com.snipl.ice.utility.IceThread;
import com.snipl.ice.utility.UserUtility;

public class ForgotPasswordAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		ForgotPasswordForm pass_frm = (ForgotPasswordForm) form;
		UserUtility usermanager = new UserUtility();
		Dao d = null;
		ResultSet rs = null;
		String target = "failure";
		if (form != null) {
			String emailid = pass_frm.getEmailid();
			String password = null;
			String mobile = null;
			String provider = null;
			String name=null;

			try {
				d = new Dao();
				String str = "SELECT * FROM user_details where Email ='"
						+ emailid + "'";
				rs = d.executeQuery(str);
				if (rs.next()) {
					password = usermanager.decrypt(rs.getString("Password"));
					mobile = rs.getString("Mobile");
					provider = rs.getString("S_Provider");
					name=rs.getString("F_Name");
					if (password == null) {
						System.out.println("Could not Retrive Password");
						target = "failure";
					}				
					
					StringBuffer msg=new StringBuffer();
					msg.append("<HTML><HEAD><TITLE>ICE Alert</TITLE></HEAD><BODY><div style='margin: 0px; padding: 0px; font-family: Arial,Helvetica,sans-serif; font-size: 12px;'><div><div><img src='"+ICEEnv.getInstance().getLogo()+"' height=60px width=117px/></div>");
					msg.append("<div style='margin: 0pt auto; padding: 10px; font-size: 15px; font-weight: bold; color: #00688b; background-color: rgb(242, 242, 242);'>Hi "+rs.getString("F_Name")+" "+rs.getString("L_Name")+" ,</div>");
					msg.append("<div style='margin: 0pt auto; padding: 10px; font-size: 13px; color: rgb(23, 149, 196); background-color: rgb(249, 249, 249);'>This is your requested login info. reminder from icealert.net. Below are the username and password for your icealert account.</div>");
					msg.append("<div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);'>");
					msg.append("<span style='font-weight: bold; color: rgb(255, 102, 0); padding-right: 10px;'> Here is your account info:<br/><br/></span> <div style='margin: 0pt auto; padding: 10px 10px 0pt; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);'>");
					msg.append(" <div style='clear: both; padding-bottom: 10px;'><span style='width: 100px; float: left; font-weight: bold; color:red;'>ICEID :</span><span style='color:red'><b>"+rs.getString("iceid")+"</b></span></div>");
					msg.append("<div style='clear: both; padding-bottom: 10px;'><span style='width: 100px; float: left; font-weight: bold; color: rgb(102, 102, 102);'>Username :</span><span>"+rs.getString("Email")+"</span></div><div style='clear: both; padding-bottom: 10px;'><span style='width: 100px; float: left; font-weight: bold; color: rgb(102, 102, 102);'>Password :</span><span>"+password+"</span></div>");
					msg.append("</div><br/><div style='margin: 0pt auto; padding: 0 0px 0px 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);'> <strong>Be informed when it really matters!</strong></div><div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);' > If you need additional assistance, please contact <a target='_blank' style='color: rgb(255, 102, 0);' href='mailto:"+ICEEnv.getInstance().getSupportEmail()+"'>"+ICEEnv.getInstance().getSupportEmail()+"</a></div><hr color='#7A7A7A'> <div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(56, 144, 177);'><p style='margin: 0px; padding: 0px; font-size: 12px; color:#00688b;'><strong>Thank you for using ,<a target='_blank' style='color: rgb(255, 102, 0);' href='http://"+ICEEnv.getInstance().getCompany()+"'> "+ICEEnv.getInstance().getCompany()+"</a></strong><br/>The ICE Alert Team </p></div><div style='margin: 0pt auto; padding: 20px 10px 10px 80px; font-size: 11px; color: rgb(153, 153, 153);'>This is a post-only mailing.  Replies to this message are not monitored or answered.</div></div></div></BODY></HTML>");
					
					String mailmessage = msg.toString();
					
					String smsmessage = "Hello "+name +" Here is your Account Information - Your Username : "+emailid+"   Password :"+password;
					String subject = "icealert Login Reminder Info";
					String from = ICEEnv.getInstance().getAdminEmail();
					
					String[] emailList=null;
					String[] recipientscc = {}, recipientsbcc = {};
					
					if(provider.equalsIgnoreCase("0"))
					{
						emailList=new String[1];
						emailList[0]=emailid;
						new IceThread(recipientsbcc, recipientscc, emailList, subject, mailmessage, from).start();
					}
					else
					{
						emailList=new String[1];
						emailList[0]=emailid;
						new IceThread(recipientsbcc, recipientscc, emailList, subject, mailmessage, from).start();
						String emailList1[]={mobile+provider};
						new IceThread(recipientsbcc, recipientscc, emailList1, subject, smsmessage, from).start();
					}					
					
					d.close();
					target = "success";
					request.setAttribute("status", "8");
					return (mapping.findForward(target));
				}
				else
				{
					request.setAttribute("status", "9");
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
		return (mapping.findForward(target));
	}
}
