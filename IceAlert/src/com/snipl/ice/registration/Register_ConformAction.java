package com.snipl.ice.registration;

/**
* @Author Sankara Rao & Kamalakar Challa
*   
*/

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mysql.jdbc.ResultSet;
import com.snipl.ice.config.ICEEnv;
import com.snipl.ice.security.Dao;
import com.snipl.ice.utility.GeneralUtility;
import com.snipl.ice.utility.IceThread;

public class Register_ConformAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if(request.getSession().getAttribute("security_id")!=null)
		{
			Dao d=null;
			ResultSet rs=null;
			if (form != null) {
				Register_ConformForm frm = (Register_ConformForm) form;
				int code;
				d = new Dao();
				int id=Integer.parseInt(request.getSession()
						.getAttribute("security_id").toString());
				rs = d.executeQuery("select * from user_details where id='"+id+ "'");
				String fname=null,City=null,country=null,lname=null,email=null;
	
				try {
					if (rs.next()) 
					{
						code = Integer.parseInt(rs.getString("Code"));
						int code_req = Integer.parseInt(frm.getId_code());
						if (code_req == code)
						{
							fname = rs.getString("F_Name");
							City = rs.getString("City");
							country=rs.getString("Country");
							lname=rs.getString("L_Name");
							email=rs.getString("Email");
							d.executeUpdate("update user_details set Flag=1 where id=" + id);
							HttpSession session = request.getSession();
							session.setAttribute("security_username", fname);
							session.setAttribute("security_profile", "1");
							session.setAttribute("security_prev", "0");
							session.setAttribute("security_id", id);
							int inv_count=0;
							rs=d.executeQuery("select * from community_assigned where flag=0 and user_id="+id);
				            while(rs.next())
				            {
				            	inv_count++;
				            }
				            session.setAttribute("invite_count",inv_count);
				            inv_count=0;
				            rs=d.executeQuery("select * from alert_receiver_details where flag=0 and receiver_id="+id);
				            while(rs.next())
				            {
				            	inv_count++;
				            }
							session.setAttribute("inbox_count", inv_count);
							session.setAttribute("own_comm_count",new GeneralUtility().getOwnCommunityCount(id));
							session.setAttribute("City",City);
							session.setAttribute("country",new GeneralUtility().getFullCountry(country));
							
							StringBuffer msg=new StringBuffer();
							msg.append("<HTML><HEAD><TITLE>ICE Alert</TITLE></HEAD><BODY><div style='margin: 0px; padding: 0px; font-family: Arial,Helvetica,sans-serif; font-size: 12px;'><div><div><img src='"+ICEEnv.getInstance().getLogo()+"' height=60px width=117px/></div>");
							msg.append("<div style='margin: 0pt auto; padding: 10px; font-size: 15px; font-weight: bold; color: #00688b; background-color: rgb(242, 242, 242);'>Hi "+fname+" "+lname+" ,</div>");
							msg.append("<div style='margin: 0pt auto; padding: 10px; font-size: 13px; color: rgb(23, 149, 196); background-color: rgb(249, 249, 249);'>Thank you for activating your icealert account! Your account is now ready.</div>");
							msg.append("<br/><div style='margin: 0pt auto; padding: 0 0px 0px 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);'> <strong>Be informed when it really matters!</strong></div><div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);' > If you need additional assistance, please contact <a target='_blank' style='color: rgb(255, 102, 0);' href='mailto:"+ICEEnv.getInstance().getSupportEmail()+"'>"+ICEEnv.getInstance().getSupportEmail()+"</a></div><hr color='#7A7A7A'> <div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(56, 144, 177);'><p style='margin: 0px; padding: 0px; font-size: 12px; color:#00688b;'><strong>Thank you for using ,<a target='_blank' style='color: rgb(255, 102, 0);' href='http://"+ICEEnv.getInstance().getCompany()+"'> "+ICEEnv.getInstance().getCompany()+"</a></strong><br/>The ICE Alert Team </p></div><div style='margin: 0pt auto; padding: 20px 10px 10px 80px; font-size: 11px; color: rgb(153, 153, 153);'>This is a post-only mailing.  Replies to this message are not monitored or answered.</div></div></div></BODY></HTML>");
							
							String mailmessage = msg.toString();
							String subject = "Your icealert Account is Activated";
							String from = ICEEnv.getInstance().getAdminEmail();
							
							String[] emailList={email};
							String[] recipientscc = {}, recipientsbcc = {};
							
							new IceThread(recipientsbcc, recipientscc, emailList, subject, mailmessage, from).start();
							
							d.close();
							return mapping.findForward("success");
						} else
						{
							d.close();
							request.setAttribute("code", "false");
							return mapping.findForward("failure");
						}
	
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			d.close();
			return mapping.findForward("sessionExpaired_Frame");
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}
}
