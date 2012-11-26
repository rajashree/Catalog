package com.snipl.ice.icemem;


/**
* @Author Sankara Rao
*   
*/

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.mysql.jdbc.ResultSet;
import com.snipl.ice.config.ICEEnv;
import com.snipl.ice.mail.SendMailUsingAuthentication;
import com.snipl.ice.security.Dao;
import com.snipl.ice.utility.GeneralUtility;
import com.snipl.ice.utility.IceThread;
import com.snipl.ice.utility.UserUtility;

public class IceSaveAction  extends DispatchAction{
	
	SendMailUsingAuthentication smtpMailSender = new SendMailUsingAuthentication();
	int flag;
	
	@SuppressWarnings({ "finally", "unchecked" })
	public ActionForward Add(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {
		if(request.getSession().getAttribute("security_id")!=null)
		{
			String target = "failure";
			flag=0;
			if(form!=null)
			{
				IceSaveForm addIceForm=(IceSaveForm) form;
				int id = Integer.parseInt(request.getSession().getAttribute("security_id").toString());
				Dao d= new Dao();
				ResultSet rs = d.executeQuery("SELECT * FROM user_details WHERE id="+id);
		        
		        try {
		        	
		        	if(rs.next())
		        	{
		        		if(rs.getString("Email").equalsIgnoreCase(addIceForm.getEmail())||rs.getString("Mobile").equalsIgnoreCase(addIceForm.getM_Number()))
		        			flag=8;
		        		else
		        		{
		        			rs = d.executeQuery("SELECT * FROM ice_contacts WHERE user_id="+id);
		        			while(rs.next())
		        				if(rs.getString("contact_email").equalsIgnoreCase(addIceForm.getEmail())||rs.getString("contact_no").equalsIgnoreCase(addIceForm.getM_Number()))
		        				{
		        					flag=9;
		        				}
		        		}
		        	}
		        	else
		        		flag=1;
		        	
					if(flag==0)
					{
						String str= "INSERT into ice_contacts(user_id,contact_name,contact_no,contact_email,Country,S_Provider) values (?,?,?,?,?,?)";
				        LinkedHashMap hm=new LinkedHashMap();
				        hm.put("i1",id);
				        hm.put("s2",addIceForm.getId_ice_name());
				        hm.put("s3",addIceForm.getM_Number());
				        hm.put("s4",addIceForm.getEmail());
				        hm.put("s5",addIceForm.getDiv_combo6());
				        hm.put("s6",addIceForm.getDiv_provider());
						
				        d.executeUpdate(hm,str);
				        target="success";
			            flag=6;
			            
			        /*sending mail*/
			            
			            StringBuffer msg=new StringBuffer();
						msg.append("<HTML><HEAD><TITLE>ICE Alert</TITLE></HEAD><BODY><div style='margin: 0px; padding: 0px; font-family: Arial,Helvetica,sans-serif; font-size: 12px;'><div><div><img src='"+ICEEnv.getInstance().getLogo()+"' height=60px width=117px/></div>");
						msg.append("<div style='margin: 0pt auto; padding: 10px; font-size: 15px; font-weight: bold; color: #00688b; background-color: rgb(242, 242, 242);'>Hi "+addIceForm.getId_ice_name()+" ,</div>");
						msg.append("<div style='margin: 0pt auto; padding: 10px; font-size: 13px; color: rgb(23, 149, 196); background-color: rgb(249, 249, 249);'>"+new UserUtility().getUserName(id)+" ( "+new UserUtility().getUserEmail(id)+" ) added you as ICE Member</div>");
						msg.append("<br/><div style='margin: 0pt auto; padding: 0 0px 0px 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);'> <strong>Be informed when it really matters!</strong></div><div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(102, 102, 102); background-color: rgb(255, 255, 255);' > If you need additional assistance, please contact <a target='_blank' style='color: rgb(255, 102, 0);' href='mailto:"+ICEEnv.getInstance().getSupportEmail()+"'>"+ICEEnv.getInstance().getSupportEmail()+"</a></div><hr color='#7A7A7A'> <div style='margin: 0pt auto; padding: 10px; font-size: 12px; color: rgb(56, 144, 177);'><p style='margin: 0px; padding: 0px; font-size: 12px; color:#00688b;'><strong>Thank you for using ,<a target='_blank' style='color: rgb(255, 102, 0);' href='http://"+ICEEnv.getInstance().getCompany()+"'> "+ICEEnv.getInstance().getCompany()+"</a></strong><br/>The ICE Alert Team </p></div><div style='margin: 0pt auto; padding: 20px 10px 10px 80px; font-size: 11px; color: rgb(153, 153, 153);'>This is a post-only mailing.  Replies to this message are not monitored or answered.</div></div></div></BODY></HTML>");
						
						String mailmessage = msg.toString();
						String message=new UserUtility().getUserName(id)+" ( "+new UserUtility().getUserEmail(id)+" ) added you as ICE Member";
						String from = ICEEnv.getInstance().getAdminEmail();
						
						String[] emailList={addIceForm.getEmail()};
						String[] recipientscc = {}, recipientsbcc = {};
						
						new IceThread(emailList,recipientscc, recipientsbcc, message, mailmessage, from).start();
						
						if (addIceForm.getDiv_provider().equalsIgnoreCase("0")) {						
						} else {
							String[] emailList1={addIceForm.getM_Number()+addIceForm.getDiv_provider()};
							new IceThread(emailList1,recipientscc, recipientsbcc, message, message, from).start();
						}
						new IceThread(emailList,recipientscc, recipientsbcc, message, mailmessage, from).start();
					}				
					
					rs = d.executeQuery("select * from ice_contacts where user_id="
							+ id);
					List icelist=new ArrayList();
					while (rs.next()) {
						IcememBean iceBean=new IcememBean();
						iceBean.setIce_name(rs.getString("contact_name"));
						iceBean.setIce_mobile(rs.getString("contact_no"));
						iceBean.setIce_email(rs.getString("contact_email"));
						iceBean.setIce_country(new GeneralUtility().getFullCountry(rs.getString("Country")));
						icelist.add(iceBean);
					}
					if(target.equalsIgnoreCase("success"))
						request.setAttribute("Add", "0");
					else
						request.setAttribute("Add", "1");
					if(icelist.size()>0)					
						request.setAttribute("iceList", icelist);
					if(icelist.size()==ICEEnv.getInstance().getMaxlimit())
						request.setAttribute("addflag", icelist.size());
					request.setAttribute("flag", flag);
					request.setAttribute("count", icelist.size());
				} catch (SQLException e) {
					e.printStackTrace();
		            flag=7;
				}
				finally{
					d.close();
					return mapping.findForward(target);
				}
			}
			return mapping.findForward("sessionExpaired");
		}
		else
			return mapping.findForward("sessionExpaired");
		
	}
	@SuppressWarnings({ "unchecked", "finally" })
	public ActionForward Update(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {
		if(request.getSession().getAttribute("security_id")!=null)
		{
			String target = "failure";
			flag=0;
			if(form!=null)
			{
				int id = Integer.parseInt(request.getSession().getAttribute("security_id").toString());
				Dao d= new Dao();
				ResultSet rs = d.executeQuery("SELECT * FROM user_details WHERE id="+id);
		        
		        try {
	        		IceSaveForm updateIceForm=(IceSaveForm) form;
		        	if(rs.next())
		        	{
		        		if(rs.getString("Email").equalsIgnoreCase(updateIceForm.getEmail())||rs.getString("Mobile").equalsIgnoreCase(updateIceForm.getM_Number()))
		        			flag=8;
		        		else
		        		{
		        			rs = d.executeQuery("SELECT * FROM ice_contacts WHERE user_id='"+id+"' and contact_email!='"+updateIceForm.getIce_email()+"' and contact_no!='"+updateIceForm.getIce_no()+"'");
		        			while(rs.next())
		        				if(rs.getString("contact_email").equalsIgnoreCase(updateIceForm.getEmail())||rs.getString("contact_no").equalsIgnoreCase(updateIceForm.getM_Number()))
		        				{
		        					flag=9;
		        				}
		        		}
			       	}
		        	else
		        		flag=1;
		        	if(flag==0)
		        	{
		        		String str= "update ice_contacts set contact_name=?,contact_no=?,contact_email=?,Country=?,S_Provider=? where user_id='"+id+"' and contact_no='"+updateIceForm.getIce_no()+"' and contact_email='"+updateIceForm.getIce_email()+"'";
				        LinkedHashMap hm=new LinkedHashMap();
				        hm.put("s1",updateIceForm.getId_ice_name());
				        hm.put("s2",updateIceForm.getM_Number());
				        hm.put("s3",updateIceForm.getEmail());
				        hm.put("s4",updateIceForm.getDiv_combo6());
				        hm.put("s5",updateIceForm.getDiv_provider());
				        d.executeUpdate(hm,str);
				        target="success";
			            flag=10;
			            rs = d.executeQuery("select * from ice_contacts where user_id="
								+ id);
						List icelist=new ArrayList();
						while (rs.next()) {
							IcememBean iceBean=new IcememBean();
							iceBean.setIce_name(rs.getString("contact_name"));
							iceBean.setIce_mobile(rs.getString("contact_no"));
							iceBean.setIce_email(rs.getString("contact_email"));
							iceBean.setIce_country(new GeneralUtility().getFullCountry(rs.getString("Country")));
							icelist.add(iceBean);
						}
						if(target.equalsIgnoreCase("success"))
							request.setAttribute("Update", "0");
						else
							request.setAttribute("Update", "1");
						if(icelist.size()>0)					
							request.setAttribute("iceList", icelist);
						if(icelist.size()==ICEEnv.getInstance().getMaxlimit())
							request.setAttribute("addflag", icelist.size());
						request.setAttribute("count", icelist.size());
						request.setAttribute("flag", flag);
	        		}
		        	else
		        	{
		        		rs=d.executeQuery("select * from ice_contacts where contact_email='"+updateIceForm.getIce_email()+"'");
	        			try {
	        				if(rs.next())
	        				{
	        					request.setAttribute("contact_name", rs.getString("contact_name"));
	        					request.setAttribute("contact_no", rs.getString("contact_no"));
	        					request.setAttribute("contact_email", rs.getString("contact_email"));
	        					request.setAttribute("Country", rs.getString("Country"));
	        					request.setAttribute("S_Provider", rs.getString("S_Provider"));
	        				}
	        			} catch (SQLException e) {
	        				e.printStackTrace();
	        			}
		        	}
		        }
		        catch (SQLException e) {
					e.printStackTrace();
		            flag=7;
				}
		        finally{
		        	d.close();
					return mapping.findForward(target);
		        }
		        
			}
			return mapping.findForward("sessionExpaired_Frame");
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
		
	}
}
