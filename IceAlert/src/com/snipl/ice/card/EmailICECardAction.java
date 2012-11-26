package com.snipl.ice.card;

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
import com.snipl.ice.config.InitConfig;
import com.snipl.ice.security.Dao;
import com.snipl.ice.utility.IceCardThread;

public class EmailICECardAction extends Action{
	
	Dao d = null;
	ResultSet rs = null;
	
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {
		if(request.getSession().getAttribute("security_id")!=null)
		{
			d= new Dao();
			String card=null;
			int userid=Integer.parseInt(request.getSession().getAttribute("security_id").toString());
			String type=request.getParameter("type"); 
			if(type.equalsIgnoreCase("jpg")){
				JpgCardGenerator dp = new JpgCardGenerator();
				card=dp.generateJPG(userid);			
			}
			else {
				PdfCardGenerator dp = new PdfCardGenerator();
				card=dp.generatePDF(userid);			
			}
			rs = d.executeQuery("select * from user_details where id='"+userid+ "'");
			String fname=null,lname=null,email=null;

			try {
				if (rs.next()) 
				{
						fname = rs.getString("F_Name");
						lname=rs.getString("L_Name");
						email=rs.getString("Email");
						
						StringBuffer msg=new StringBuffer();
						msg.append("<HTML><HEAD><TITLE>ICE Alert</TITLE></HEAD><BODY><div style='margin: 0px; padding: 0px; font-family: Arial,Helvetica,sans-serif; font-size: 12px;'><div><div><img src='"+ICEEnv.getInstance().getLogo()+"' height=60px width=117px/></div>");
						msg.append("<div style='margin: 0pt auto; padding: 10px; font-size: 15px; font-weight: bold; color: #00688b; background-color: rgb(242, 242, 242);'>Hi "+fname+" "+lname+" ,</div>");
						msg.append("<div style='margin: 0pt auto; padding: 10px; font-size: 13px; color: rgb(23, 149, 196); background-color: rgb(249, 249, 249);'>Thank you for using icealert. Here is the your ICE Card</div>");
						msg.append("<br/><div style='margin: 0pt auto; padding: 0 0px 0px 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);'> <strong>Be informed when it really matters!</strong></div><div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);' > If you need additional assistance, please contact <a target='_blank' style='color: rgb(255, 102, 0);' href='mailto:"+ICEEnv.getInstance().getSupportEmail()+"'>"+ICEEnv.getInstance().getSupportEmail()+"</a></div><hr color='#7A7A7A'> <div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(56, 144, 177);'><p style='margin: 0px; padding: 0px; font-size: 12px; color:#00688b;'><strong>Thank you for using ,<a target='_blank' style='color: rgb(255, 102, 0);' href='http://"+ICEEnv.getInstance().getCompany()+"'> "+ICEEnv.getInstance().getCompany()+"</a></strong><br/>The ICE Alert Team </p></div><div style='margin: 0pt auto; padding: 20px 10px 10px 80px; font-size: 11px; color: rgb(153, 153, 153);'>This is a post-only mailing.  Replies to this message are not monitored or answered.</div></div></div></BODY></HTML>");
						
						String mailmessage = msg.toString();
						String subject = "Hi "+fname+" "+lname+". Your ICE Card.";
						String from = ICEEnv.getInstance().getAdminEmail();
						
						String[] emailList={email};
						String[] recipientscc = {}, recipientsbcc = {};
						
						card=InitConfig.path+card;
						new IceCardThread(recipientsbcc, recipientscc, emailList, subject, mailmessage, from,card,fname+" "+lname).start();
						
						d.close();
					} 
				}
			catch (SQLException e) {
				e.printStackTrace();
			}
			return (mapping.findForward("done"));
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}
}
