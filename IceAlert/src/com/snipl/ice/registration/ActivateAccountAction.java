package com.snipl.ice.registration;

/**
* @Author Kamalakar Challa
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
import com.snipl.ice.security.Dao;
import com.snipl.ice.utility.IceThread;

public class ActivateAccountAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		if((request.getParameter("method")!=null)&&(request.getParameter("email")!=null)&&(request.getParameter("cc")!=null)&&(request.getParameter("method").equalsIgnoreCase("confirm")))
		{
			Dao d=new Dao();
			ResultSet rs=null;
			String query="select * from user_details where Email='"+request.getParameter("email")+"' and Code="+Integer.parseInt(request.getParameter("cc").toString());
			rs=d.executeQuery(query);
			try {
				if(rs.next())
				{
					if(rs.getInt("Flag")!=1)
					{
						d.executeUpdate("update user_details set Flag=1 where Email='" + request.getParameter("email")+"'");
						request.setAttribute("ActivateAccountFlag", "0");
						StringBuffer msg=new StringBuffer();
						msg.append("<HTML><HEAD><TITLE>ICE Alert</TITLE></HEAD><BODY><div style='margin: 0px; padding: 0px; font-family: Arial,Helvetica,sans-serif; font-size: 12px;'><div><div><img src='"+ICEEnv.getInstance().getLogo()+"' height=60px width=117px/></div>");
						msg.append("<div style='margin: 0pt auto; padding: 10px; font-size: 15px; font-weight: bold; color: #00688b; background-color: rgb(242, 242, 242);'>Hi "+rs.getString("F_Name")+" "+rs.getString("L_Name")+" ,</div>");
						msg.append("<div style='margin: 0pt auto; padding: 10px; font-size: 13px; color: rgb(23, 149, 196); background-color: rgb(249, 249, 249);'>Thank you for activating your icealert account! Your account is now ready.</div>");
						msg.append("<br/><div style='margin: 0pt auto; padding: 0 0px 0px 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);'> <strong>Be informed when it really matters!</strong></div><div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);' > If you need additional assistance, please contact <a target='_blank' style='color: rgb(255, 102, 0);' href='mailto:"+ICEEnv.getInstance().getSupportEmail()+"'>"+ICEEnv.getInstance().getSupportEmail()+"</a></div><hr color='#7A7A7A'> <div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(56, 144, 177);'><p style='margin: 0px; padding: 0px; font-size: 12px; color:#00688b;'><strong>Thank you for using ,<a target='_blank' style='color: rgb(255, 102, 0);' href='http://"+ICEEnv.getInstance().getCompany()+"'> "+ICEEnv.getInstance().getCompany()+"</a></strong><br/>The ICE Alert Team </p></div><div style='margin: 0pt auto; padding: 20px 10px 10px 80px; font-size: 11px; color: rgb(153, 153, 153);'>This is a post-only mailing.  Replies to this message are not monitored or answered.</div></div></div></BODY></HTML>");
						
						String mailmessage = msg.toString();
						String subject = "Your icealert Account is Activated";
						String from = ICEEnv.getInstance().getAdminEmail();
						
						String[] emailList={rs.getString("Email")};
						String[] recipientscc = {}, recipientsbcc = {};
						
						new IceThread(recipientsbcc, recipientscc, emailList, subject, mailmessage, from).start();
					}
					else
						request.setAttribute("ActivateAccountFlag", "1");
					d.close();
					return mapping.findForward("Activated");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return mapping.findForward("Activated");
		}
		else
			return mapping.findForward("Activated");
	}
}
