package com.snipl.ice.feedback;

import java.util.LinkedHashMap;

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

public class SaveFeedbackAction extends Action
{
	public ActionForward execute(
									ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response) 
									throws Exception
	{
		ResultSet rs;
		Dao d;
		LinkedHashMap hm;
		FeedbackForm feedbackform = (FeedbackForm) form;
		if(request.getSession().getAttribute("security_id")!=null)
		{
			int userid = Integer.parseInt(request.getSession().getAttribute("security_id").toString());
			d=new Dao();
			try{
				String str= "INSERT into feedback_details(user_id,comment,status) values (?,?,?)";
			       
				hm=new LinkedHashMap();
				hm.put("i1", userid);
				hm.put("s2", feedbackform.getComment());
				hm.put("i3", 0);
		
				d.executeUpdate(hm,str);
				hm.clear();
				
				str = "SELECT * FROM user_details where id ="+ userid;
				rs = d.executeQuery(str);
				String email=null;
				String fname=null;
				String lname=null;
				String from = ICEEnv.getInstance().getAdminEmail();
				if (rs.next()) {
					email = rs.getString("Email");
					fname = rs.getString("F_Name");
					lname = rs.getString("L_Name");
				}
				d.close();
				
				String[] emailList={};
				String[] recipientscc = {}, recipientsbcc = {email};
				
				StringBuffer msg=new StringBuffer();
				msg.append("<HTML><HEAD><TITLE>ICE Alert</TITLE></HEAD><BODY><div style='margin: 0px; padding: 0px; font-family: Arial,Helvetica,sans-serif; font-size: 12px;'><div><div><img src='"+ICEEnv.getInstance().getLogo()+"' height=60px width=117px/></div>");
				msg.append("<div style='margin: 0pt auto; padding: 10px; font-size: 15px; font-weight: bold; color: #00688b; background-color: rgb(242, 242, 242);'>Hi "+fname+" "+lname+" ,</div>");
				msg.append("<div style='margin: 0pt auto; padding: 10px; font-size: 13px; color: rgb(23, 149, 196); background-color: rgb(249, 249, 249);'> Thank you for your feedback. Please visit again.</div>");
				msg.append("<br/><div style='margin: 0pt auto; padding: 0 0px 0px 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);'> <strong>Be informed when it really matters!</strong></div><div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);' > If you need additional assistance, please contact <a target='_blank' style='color: rgb(255, 102, 0);' href='mailto:"+ICEEnv.getInstance().getSupportEmail()+"'>"+ICEEnv.getInstance().getSupportEmail()+"</a></div><hr color='#7A7A7A'> <div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(56, 144, 177);'><p style='margin: 0px; padding: 0px; font-size: 12px; color:#00688b;'><strong>Thank you for using ,<a target='_blank' style='color: rgb(255, 102, 0);' href='http://"+ICEEnv.getInstance().getCompany()+"'> "+ICEEnv.getInstance().getCompany()+"</a></strong><br/>The ICE Alert Team </p></div><div style='margin: 0pt auto; padding: 20px 10px 10px 80px; font-size: 11px; color: rgb(153, 153, 153);'>This is a post-only mailing.  Replies to this message are not monitored or answered.</div></div></div></BODY></HTML>");
				
				new IceThread(recipientsbcc, recipientscc, emailList, "Thank you for using ICE Alert!!!", msg.toString(), from).start();
				
				String msg1="<HTML><HEAD><TITLE>ICE Alert</TITLE></HEAD><BODY><div style='margin: 0px; padding: 0px; font-family: Arial,Helvetica,sans-serif; font-size: 12px;'><div><div><img src='"+ICEEnv.getInstance().getLogo()+"' height=60px width=117px/></div>"+
				"<div style='margin: 0pt auto; padding: 10px; font-size: 15px; font-weight: bold; color: #00688b; background-color: rgb(242, 242, 242);'>Hi ,</div>"+
				"<div style='margin: 0pt auto; padding: 10px; font-size: 13px; color: rgb(23, 149, 196); background-color: rgb(249, 249, 249);'> <b>"+fname+" "+lname+" has sent you a feedback</b><br>"+feedbackform.getComment()+"</div>"+
				"<br/><div style='margin: 0pt auto; padding: 0 0px 0px 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);'> <strong>Be informed when it really matters!</strong></div><div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);' > If you need additional assistance, please contact <a target='_blank' style='color: rgb(255, 102, 0);' href='mailto:"+ICEEnv.getInstance().getSupportEmail()+"'>"+ICEEnv.getInstance().getSupportEmail()+"</a></div><hr color='#7A7A7A'> <div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(56, 144, 177);'><p style='margin: 0px; padding: 0px; font-size: 12px; color:#00688b;'><strong>Thank you for using ,<a target='_blank' style='color: rgb(255, 102, 0);' href='http://"+ICEEnv.getInstance().getCompany()+"'> "+ICEEnv.getInstance().getCompany()+"</a></strong><br/>The ICE Alert Team </p></div><div style='margin: 0pt auto; padding: 20px 10px 10px 80px; font-size: 11px; color: rgb(153, 153, 153);'>This is a post-only mailing.  Replies to this message are not monitored or answered.</div></div></div></BODY></HTML>";
				
				String[] recipientsbcc1 = {from};
				new IceThread(recipientsbcc1, recipientscc, emailList, "You got a feedback from "+fname+" "+lname, msg1, email).start();
			}
			catch(Exception e){
			}
			return mapping.findForward(feedbackform.getSource());
		}
		else
			return mapping.findForward("sessionExpaired");		
	}
}