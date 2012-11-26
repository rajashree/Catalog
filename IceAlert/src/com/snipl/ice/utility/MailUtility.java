package com.snipl.ice.utility;
/**
* @Author Kamalakar Challa & Sankara Rao
*   
*/
import java.sql.SQLException;

import com.mysql.jdbc.ResultSet;
import com.snipl.ice.config.ICEEnv;
import com.snipl.ice.security.Dao;

public class MailUtility {
	
	Dao d = new Dao();
	ResultSet rs;

	private static final long serialVersionUID = 1L;
	
	public String getMailHeader(){
		return null;
	}
	
	public String getMailFooter(){
		return null;
	}
	
	public String getNewRegBodyForComm(int commid,String inv_msg, String sender) throws SQLException{
		String mailmessage="";
		rs=d.executeQuery("select * from community_details where id="+commid);
		if(rs.next())
		{
			mailmessage = "<HTML><HEAD><TITLE>ICE Alert</TITLE></HEAD><BODY><div style='margin: 0px; padding: 0px; font-family: Arial,Helvetica,sans-serif; font-size: 12px;'><div><div><img src='"+ICEEnv.getInstance().getLogo()+"'height=60px width=117px/></div>"+
			"<div style='margin: 0pt auto; padding: 10px; font-size: 15px; font-weight: bold; color: #00688b; background-color: rgb(242, 242, 242);'>Hello ,</div><div style='margin: 0pt auto; padding: 10px; font-size: 13px; color: rgb(23, 149, 196); background-color: rgb(249, 249, 249);'> You have been invited to join '"+rs.getString("name")+"' Community	 <br><br>"+inv_msg+"</div><b>Details:</b><br><div style='margin: 0pt auto; padding: 10px; font-size: 13px; color: rgb(23, 149, 196); '><div><div>Name: "+rs.getString("name")+"</div><div>Created On: "+rs.getString("creation_date")+"</div><div>Description: "+rs.getString("description")+"</div><div>No of Members: "+rs.getString("no_users")+"</div></div></div><div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);'><br/><span style='font-weight: bold; color: rgb(255, 102, 0); padding-right: 10px;'>Please Register at <a href='http://www.icealert.net'>www.icealert.net</a></span></div><br/><div style='margin: 0pt auto; padding: 0 0px 0px 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);'> <strong>Be informed when it really matters!</strong></div><div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);' > If you need additional assistance, please contact <a target='_blank' style='color: rgb(255, 102, 0);' href='mailto:"+ICEEnv.getInstance().getSupportEmail()+"'>"+ICEEnv.getInstance().getSupportEmail()+"</a></div><hr color='#7A7A7A'> <div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(56, 144, 177);'><p style='margin: 0px; padding: 0px; font-size: 12px; color:#00688b;'><strong>Thank you for using ,<a target='_blank' style='color: rgb(255, 102, 0);' href='http://"+ICEEnv.getInstance().getCompany()+"'> "+ICEEnv.getInstance().getCompany()+"</a></strong><br/>The ICE Alert Team </p></div></div></div></BODY></HTML>";
		}
		return mailmessage;
	}
	
	public String getNewReqBodyForComm(int commid,String inv_msg, String sender) throws SQLException{
		String mailmessage="";
		rs=d.executeQuery("select * from community_details where id="+commid);
		if(rs.next())
		{
			mailmessage = "<HTML><HEAD><TITLE>ICE Alert</TITLE></HEAD><BODY><div style='margin: 0px; padding: 0px; font-family: Arial,Helvetica,sans-serif; font-size: 12px;'><div><div><img src='"+ICEEnv.getInstance().getLogo()+"'height=60px width=117px/></div>"+
			"<div style='margin: 0pt auto; padding: 10px; font-size: 15px; font-weight: bold; color: #00688b; background-color: rgb(242, 242, 242);'>Hello ,</div><div style='margin: 0pt auto; padding: 10px; font-size: 13px; color: rgb(23, 149, 196); background-color: rgb(249, 249, 249);'> You have been invited to join '"+rs.getString("name")+"' Community	 <br><br>"+inv_msg+"</div><b>Details:</b><br><div style='margin: 0pt auto; padding: 10px; font-size: 13px; color: rgb(23, 149, 196); '><div><div>Name: "+rs.getString("name")+"</div><div>Created On: "+rs.getString("creation_date")+"</div><div>Description: "+rs.getString("description")+"</div><div>No of Members: "+rs.getString("no_users")+"</div></div></div><div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);'><br/><span style='font-weight: bold; color: rgb(255, 102, 0); padding-right: 10px;'>Please login to accept invitation at <a href='http://www.icealert.net'>www.icealert.net</a></span></div><br/><div style='margin: 0pt auto; padding: 0 0px 0px 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);'> <strong>Be informed when it really matters!</strong></div><div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);' > If you need additional assistance, please contact <a target='_blank' style='color: rgb(255, 102, 0);' href='mailto:"+ICEEnv.getInstance().getSupportEmail()+"'>"+ICEEnv.getInstance().getSupportEmail()+"</a></div><hr color='#7A7A7A'> <div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(56, 144, 177);'><p style='margin: 0px; padding: 0px; font-size: 12px; color:#00688b;'><strong>Thank you for using ,<a target='_blank' style='color: rgb(255, 102, 0);' href='http://"+ICEEnv.getInstance().getCompany()+"'> "+ICEEnv.getInstance().getCompany()+"</a></strong><br/>The ICE Alert Team </p></div></div></div></BODY></HTML>";
		}
		return mailmessage;
	}
	
	public String getNewReBodyForComm(int commid,String inv_msg, String sender) throws SQLException{
		String mailmessage="";
		rs=d.executeQuery("select * from community_details where id="+commid);
		if(rs.next())
		{
			mailmessage = "<HTML><HEAD><TITLE>ICE Alert</TITLE></HEAD><BODY><div style='margin: 0px; padding: 0px; font-family: Arial,Helvetica,sans-serif; font-size: 12px;'><div><div><img src='"+ICEEnv.getInstance().getLogo()+"'height=60px width=117px/></div>"+
			"<div style='margin: 0pt auto; padding: 10px; font-size: 15px; font-weight: bold; color: #00688b; background-color: rgb(242, 242, 242);'>Hello ,</div><div style='margin: 0pt auto; padding: 10px; font-size: 13px; color: rgb(23, 149, 196); background-color: rgb(249, 249, 249);'> You have been invited to join '"+rs.getString("name")+"' Community	 <br><br>"+inv_msg+"</div><b>Details:</b><br><div style='margin: 0pt auto; padding: 10px; font-size: 13px; color: rgb(23, 149, 196); '><div><div>Name: "+rs.getString("name")+"</div><div>Created On: "+rs.getString("creation_date")+"</div><div>Description: "+rs.getString("description")+"</div><div>No of Members: "+rs.getString("no_users")+"</div></div></div><div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);'><br/><span style='font-weight: bold; color: rgb(255, 102, 0); padding-right: 10px;'>Please login to accept invitation at <a href='http://www.icealert.net'>www.icealert.net</a></span></div><br/><div style='margin: 0pt auto; padding: 0 0px 0px 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);'> <strong>Be informed when it really matters!</strong></div><div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);' > If you need additional assistance, please contact <a  target='_blank' style='color: rgb(255, 102, 0);' href='mailto:"+ICEEnv.getInstance().getSupportEmail()+"'>"+ICEEnv.getInstance().getSupportEmail()+"</a></div><hr color='#7A7A7A'> <div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(56, 144, 177);'><p style='margin: 0px; padding: 0px; font-size: 12px; color:#00688b;'><strong>Thank you for using ,<a target='_blank' style='color: rgb(255, 102, 0);' href='http://"+ICEEnv.getInstance().getCompany()+"'> "+ICEEnv.getInstance().getCompany()+"</a></strong><br/>The ICE Alert Team </p></div></div></div></BODY></HTML>";
		}
		return mailmessage;
	}
	
/*	public String getMailFooter(){
		return null;
	}
	
	public String getMailFooter(){
		return null;
	}*/
	
}
