package com.snipl.ice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;

import com.snipl.ice.config.ICEEnv;
import com.snipl.ice.utility.IceThread;
import com.snipl.ice.utility.UserUtility;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class InviteFriendsAction extends Action {
	
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {
	
		if(request.getSession().getAttribute("security_id")!=null)
		{
			try {
				String mailids = request.getParameter("maillist");
				int id = Integer.parseInt(request.getSession().getAttribute("security_id").toString());
				String name=new UserUtility().getUserName(id);
				String useremail=new UserUtility().getUserEmail(id);
				StringBuffer msg=new StringBuffer();
				msg.append("<HTML><HEAD><TITLE>ICE Alert</TITLE></HEAD><BODY><div style='margin: 0px; padding: 0px; font-family: Arial,Helvetica,sans-serif; font-size: 12px;'><div><div><img src='"+ICEEnv.getInstance().getLogo()+"' height=60px width=117px/></div>");
				msg.append("<div style='margin: 0pt auto; padding: 10px; font-size: 15px; font-weight: bold; color: #00688b; background-color: rgb(242, 242, 242);'>Hi  ,</div>");
				msg.append("<div style='margin: 0pt auto; padding: 10px; font-size: 13px; color: rgb(23, 149, 196); background-color: rgb(249, 249, 249);'> You have been invited to join ICE Alert by "+name+" <a target='_blank' style='color: rgb(255, 102, 0);' href='http://"+ICEEnv.getInstance().getCompany()+"'> "+ICEEnv.getInstance().getCompany()+"</a> ( "+useremail+" ) </div>");
				msg.append("<br/><div style='margin: 0pt auto; padding: 0 0px 0px 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);'> <strong>Be informed when it really matters!</strong></div><div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);' > If you need additional assistance, please contact <a target='_blank' style='color: rgb(255, 102, 0);' href='mailto:"+ICEEnv.getInstance().getSupportEmail()+"'>"+ICEEnv.getInstance().getSupportEmail()+"</a></div><hr color='#7A7A7A'> <div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(56, 144, 177);'><p style='margin: 0px; padding: 0px; font-size: 12px; color:#00688b;'><strong>Thank you for using ,<a target='_blank' style='color: rgb(255, 102, 0);' href='http://"+ICEEnv.getInstance().getCompany()+"'> "+ICEEnv.getInstance().getCompany()+"</a></strong><br/>The ICE Alert Team </p></div><div style='margin: 0pt auto; padding: 20px 10px 10px 80px; font-size: 11px; color: rgb(153, 153, 153);'>This is a post-only mailing.  Replies to this message are not monitored or answered.</div></div></div></BODY></HTML>");
				String mailmessage = msg.toString();
				
				String from=ICEEnv.getInstance().getAdminEmail();
				String[] emailList=null;
				String[] recipientscc = {},recipientsbcc={};
				StringTokenizer str=  new  StringTokenizer(mailids,",");
				emailList=new String[str.countTokens()];
				int i=0;
				while(str.hasMoreTokens()){
					emailList[i]=str.nextToken();
					i++;
				}
				String subject=name+" invited you to join ICE Alert.";
				new IceThread(emailList, recipientscc, recipientsbcc, subject, mailmessage, from).start();
				return mapping.findForward("success");
			}
			catch (Exception e) {
				e.printStackTrace();
				return mapping.findForward("failure");
			}
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}
}



