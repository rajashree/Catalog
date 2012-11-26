package com.snipl.ice.community;
/**
* @Author Kamalakar Challa 
*   
*/
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.snipl.ice.config.ICEEnv;
import com.snipl.ice.mail.SendMailUsingAuthentication;
import com.snipl.ice.security.Dao;
import com.snipl.ice.utility.IceThread;
import com.snipl.ice.utility.UserUtility;

public class SendCommunityAlertAction extends Action{
	
	SendMailUsingAuthentication smtpMailSender = new SendMailUsingAuthentication();
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {
		if(request.getSession().getAttribute("security_id")!=null)
		{
			ResultSet rs;	
			ResultSet rs1;
			Dao d=new Dao();	
			
			
			rs=d.executeQuery("select * from user_details where id='"+request.getSession().getAttribute("security_id")+"'");
			try {
				if(rs.next())
				{
					
					String[] emailList = null;
					String[] smsList = null;
					String[] recipientscc = {}, recipientsbcc = {};
					
					String subject=null;
					String message=null;
					String mailmessage=null;
					int alertid=0;
					int idtemp=0;
					
					int commcount=0;
					int commcount1=0;
					
					String from = null;
					int sed_id=0;
					
					SendCommunityAlertForm comm_frm=(SendCommunityAlertForm) form;				
					int commid=Integer.parseInt(comm_frm.getCommid());
					subject = comm_frm.getSubject();
					message = comm_frm.getDescription();
					
					sed_id=Integer.parseInt(request.getSession().getAttribute("security_id").toString());
					from=new UserUtility().getUserEmail(Integer.parseInt(request.getSession().getAttribute("security_id").toString()));
	//				Inbox
					String str= "INSERT into alert_sender_details(sender_id,subject,body,type,date_time) values (?,?,?,?,?)";
					
					Calendar cal = Calendar.getInstance();
				    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
				    
			        String cur_date=sdf.format(cal.getTime());
			        
					LinkedHashMap hm=new LinkedHashMap();
			        hm.put("i1",sed_id);
			        hm.put("s2",subject);
			        hm.put("s3",message);
			        hm.put("i4",2);
			        hm.put("s5",cur_date);
			        d.executeUpdate(hm,str);
				        
			        rs = d.executeQuery("select * from alert_sender_details where date_time='"+cur_date+"'");
			        
			        if(rs.next())
			        {
			        	alertid = Integer.parseInt(rs.getString("alert_id"));
			        }	        	
					
					rs=d.executeQuery("select * from community_assigned where flag=1 and comm_id="+commid);
					
					StringBuffer stb = new StringBuffer();
					StringBuffer stb1 = new StringBuffer();
					while(rs.next())
					{
						int uid=rs.getInt("user_id");
						if(Integer.parseInt(request.getSession().getAttribute("security_id").toString())==uid)
							continue;
						rs1=d.executeQuery("select * from user_details where id="+uid);
						if (rs1.next()) {
							commcount++;
							stb.append(rs1.getString("Email") + "|");
							idtemp=new UserUtility().getIDbyEmailID(rs1.getString("Email"));
							d.executeUpdate("INSERT into alert_receiver_details values("+alertid+",'"+idtemp+"',0)");
							
							if (rs1.getString("S_Provider").equalsIgnoreCase("0")) {
							} else {
								stb1.append(rs1.getString("Mobile")
										+ rs1.getString("S_Provider") + "|");
								commcount1++;
							}
						}
					}
					
					StringTokenizer st1 = new StringTokenizer(stb.toString(), "|");
					int i = 0;
	
					emailList = new String[commcount];
					smsList = new String[commcount1];
					while (st1.hasMoreTokens()) {
						emailList[i] = st1.nextToken();
						i++;
					}
					
					st1 = new StringTokenizer(stb1.toString(), "|");
					i=0;
					while (st1.hasMoreTokens()) {
						smsList[i] = st1.nextToken();
						i++;
					}
					
					
					rs=d.executeQuery("select * from community_details where id="+commid);
					if(rs.next())
					{
						mailmessage = "<HTML><HEAD><TITLE>ICE Alert</TITLE></HEAD><BODY><div style='margin: 0px; padding: 0px; font-family: Arial,Helvetica,sans-serif; font-size: 12px;'><div><div><img src='"+ICEEnv.getInstance().getLogo()+"' height=60px width=117px/></div>"+
						"<div style='margin: 0pt auto; padding: 10px; font-size: 15px; font-weight: bold; color: #00688b; background-color: rgb(242, 242, 242);'>Hello ,You got a alert from '"+rs.getString("name")+"' Community	</div><div style='margin: 0pt auto; padding: 10px; font-size: 13px; color: rgb(23, 149, 196); background-color: rgb(249, 249, 249);'> "+message+" <br><br></div><b>Details of Community:</b><br><div style='margin: 0pt auto; padding: 10px; font-size: 13px; color: rgb(23, 149, 196); '><div><div>Name: "+rs.getString("name")+"</div><div>Created On: "+rs.getString("creation_date")+"</div><div>Description: "+rs.getString("description")+"</div><div>No of Members: "+rs.getString("no_users")+"</div></div></div><br/><div style='margin: 0pt auto; padding: 0 0px 0px 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);'> <strong>Be informed when it really matters!</strong></div><div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);' > If you need additional assistance, please contact <a  target='_blank' style='color: rgb(255, 102, 0);' href='mailto:"+ICEEnv.getInstance().getSupportEmail()+"'>"+ICEEnv.getInstance().getSupportEmail()+"</a></div><hr color='#7A7A7A'> <div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(56, 144, 177);'><p style='margin: 0px; padding: 0px; font-size: 12px; color:#00688b;'><strong>Thank you for using ,<a target='_blank' style='color: rgb(255, 102, 0);' href='http://"+ICEEnv.getInstance().getCompany()+"'> "+ICEEnv.getInstance().getCompany()+"</a></strong><br/>The ICE Alert Team </p></div></div></div></BODY></HTML>";
						message="You got a alert from '"+rs.getString("name")+"' Community  "+message;
					}
					
					d.close();
					if (commcount > 0) {
						new IceThread(recipientsbcc, recipientscc,emailList, subject, mailmessage, from).start();
						new IceThread(recipientsbcc, recipientscc,smsList, subject, message, from).start();
						return mapping.findForward("success");					
					} else
						return mapping.findForward("success");				
					
				}
				else
				{
					d.close();
					return mapping.findForward("failure");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return mapping.findForward("sessionExpaired_Frame");
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}
}