package com.snipl.ice.report;

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

public class SaveBugAction extends Action
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
		ReportBugForm bugfrm = (ReportBugForm) form;
		if(request.getSession().getAttribute("security_id")!=null)
		{
			int userid = Integer.parseInt(request.getSession().getAttribute("security_id").toString());
			d=new Dao();
			try{
				String str= "INSERT into report_bug(user_id,bug,status,btype,bversion,category) values (?,?,?,?,?,?)";
			       
				hm=new LinkedHashMap();
				hm.put("i1", userid);
				hm.put("s2", bugfrm.getBug());
				hm.put("i3", 0);
				hm.put("s4", bugfrm.getBrowserType());
				hm.put("s5", bugfrm.getBrowserVersion());
				hm.put("s6", bugfrm.getCategory());
		
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
				msg.append("<div style='margin: 0pt auto; padding: 10px; font-size: 13px; color: rgb(23, 149, 196); background-color: rgb(249, 249, 249);'> Thank you for reporting a bug. Will deal with it soon.</div>");
				msg.append("<br/><div style='margin: 0pt auto; padding: 0 0px 0px 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);'> <strong>Be informed when it really matters!</strong></div><div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);' > If you need additional assistance, please contact <a target='_blank' style='color: rgb(255, 102, 0);' href='mailto:"+ICEEnv.getInstance().getSupportEmail()+"'>"+ICEEnv.getInstance().getSupportEmail()+"</a></div><hr color='#7A7A7A'> <div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(56, 144, 177);'><p style='margin: 0px; padding: 0px; font-size: 12px; color:#00688b;'><strong>Thank you for using ,<a target='_blank' style='color: rgb(255, 102, 0);' href='http://"+ICEEnv.getInstance().getCompany()+"'> "+ICEEnv.getInstance().getCompany()+"</a></strong><br/>The ICE Alert Team </p></div><div style='margin: 0pt auto; padding: 20px 10px 10px 80px; font-size: 11px; color: rgb(153, 153, 153);'>This is a post-only mailing.  Replies to this message are not monitored or answered.</div></div></div></BODY></HTML>");
				
				new IceThread(recipientsbcc, recipientscc, emailList, "Thank you for reporting the Bug in ICE Alert!!!", msg.toString(), from).start();
				
				String msg1="<HTML><HEAD><TITLE>ICE Alert</TITLE></HEAD><BODY><div style='margin: 0px; padding: 0px; font-family: Arial,Helvetica,sans-serif; font-size: 12px;'><div><div><img src='"+ICEEnv.getInstance().getLogo()+"' height=60px width=117px/></div>"+
				"<div style='margin: 0pt auto; padding: 10px; font-size: 15px; font-weight: bold; color: #00688b; background-color: rgb(242, 242, 242);'>Hi ,</div>"+
				"<div style='margin: 0pt auto; padding: 10px; font-size: 13px; color: rgb(23, 149, 196); background-color: rgb(249, 249, 249);'> <b>"+fname+" "+lname+" has has reported a bug</b><br><br><font color='#000'<b> Details </b></font><br><div style='margin: 0pt auto; padding: 10px 10px 0pt; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);'> <div style='clear: both; padding-bottom: 10px;'><span style='width: 100px; float: left; font-weight: bold;'>Browser :</span><span>"+bugfrm.getBrowserType()+" V "+bugfrm.getBrowserVersion()+"</span></div><div style='clear: both; padding-bottom: 10px;'><span style='width: 100px; float: left; font-weight: bold; color: rgb(102, 102, 102);'>Category :</span><span>"+bugfrm.getCategory()+"</span></div><div style='clear: both; padding-bottom: 10px;'><span style='width: 100px; float: left; font-weight: bold; color: rgb(102, 102, 102);'>Bug :</span><span>"+bugfrm.getBug()+"</span></div></div></div>"+
				"<br/><div style='margin: 0pt auto; padding: 0 0px 0px 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);'> <strong>Be informed when it really matters!</strong></div><div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);' > If you need additional assistance, please contact <a target='_blank' style='color: rgb(255, 102, 0);' href='mailto:"+ICEEnv.getInstance().getSupportEmail()+"'>"+ICEEnv.getInstance().getSupportEmail()+"</a></div><hr color='#7A7A7A'> <div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(56, 144, 177);'><p style='margin: 0px; padding: 0px; font-size: 12px; color:#00688b;'><strong>Thank you for using ,<a target='_blank' style='color: rgb(255, 102, 0);' href='http://"+ICEEnv.getInstance().getCompany()+"'> "+ICEEnv.getInstance().getCompany()+"</a></strong><br/>The ICE Alert Team </p></div><div style='margin: 0pt auto; padding: 20px 10px 10px 80px; font-size: 11px; color: rgb(153, 153, 153);'>This is a post-only mailing.  Replies to this message are not monitored or answered.</div></div></div></BODY></HTML>";
				
				String[] recipientsbcc1 = {from};
				new IceThread(recipientsbcc1, recipientscc, emailList, "You got a bug from "+fname+" "+lname, msg1, email).start();
				d.close();
			}
			catch(Exception e){
			}
			return mapping.findForward(bugfrm.getSource());
		}
		else
			return mapping.findForward("sessionExpaired");	
	}
}