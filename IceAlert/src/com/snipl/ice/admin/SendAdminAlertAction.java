package com.snipl.ice.admin;
/**
* @Author Kamalakar Challa 
*   
*/
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mysql.jdbc.ResultSet;
import com.snipl.ice.config.ICEEnv;
import com.snipl.ice.mail.SendMailUsingAuthentication;
import com.snipl.ice.security.Dao;
import com.snipl.ice.utility.UserUtility;

public class SendAdminAlertAction extends Action {

	SendMailUsingAuthentication smtpMailSender = new SendMailUsingAuthentication();
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
	
	String from = null;
	int sed_id=0;
	int alertid=0;
	int idtemp=0;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if(request.getSession().getAttribute("security_id")!=null)
		{
			// Get the parameters from the form
			int actionmap = Integer.parseInt(request.getParameter("msghidden"));
			String sms = request.getParameter("smsdata");
			String sub = request.getParameter("smssub");
			Dao d = new Dao();
			ResultSet rs;
			
			String icesend = "off";
			String stat = null;
			if (request.getParameter("sendice") == null) {
				icesend = "off";
			} else {
				icesend = request.getParameter("sendice");
			}
			sed_id=Integer.parseInt(request.getSession().getAttribute("security_id").toString());
			from=new UserUtility().getUserEmail(Integer.parseInt(request.getSession().getAttribute("security_id").toString()));
	
			
	//		Inbox
			String str= "INSERT into alert_sender_details(sender_id,subject,body,type,date_time) values (?,?,?,?,?)";
			
			Calendar cal = Calendar.getInstance();
		    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		    
	        String cur_date=sdf.format(cal.getTime());
	        
			LinkedHashMap hm=new LinkedHashMap();
	        hm.put("i1",sed_id);
	        hm.put("s2",sub);
	        hm.put("s3",sms);
	        hm.put("i4",1);
	        hm.put("s5",cur_date);
	        d.executeUpdate(hm,str);
		        
	        rs = d.executeQuery("select * from alert_sender_details where date_time='"+cur_date+"'");
	        
	        if(rs.next())
	        {
	        	alertid = Integer.parseInt(rs.getString("alert_id"));
	        }
	        
			
			// Switch the control based on actionmap
			switch (actionmap) {
			case 1:
				stat = sendSMStoALL(sms, sub, icesend);
				break;
			case 2:
				stat = sendSMStoCommunity(sms, sub, request.getParameter("stdata"),
						icesend);
				break;
			case 3:
				stat = sendSMStoSelectedUsers(sms, sub, request
						.getParameter("stdata"), icesend);
				break;
			case 4:
				stat = sendSMStoSelectedMobiles(sms, sub, request
						.getParameter("stdata"), icesend);
				break;
			case 5:
				stat = sendSMStoSelectedEmails(sms, sub, request
						.getParameter("stdata"), icesend);
				break;
			case 6:
				stat = sendSMStoSelectedCountry(sms, sub, request
						.getParameter("stdata"), icesend);
				break;
			case 7:
				stat = sendSMStoSelectedOccupation(sms, sub, request
						.getParameter("stdata"), icesend);
				break;
			}
			request.setAttribute("flag", "11");
			return mapping.findForward(stat);
		}
		else
			return mapping.findForward("sessionExpaired");
	}

	// Send Message to all users
	private String sendSMStoALL(String message, String subject, String icesend)
			throws SQLException {

		Dao d = new Dao();
		int usercount = 0;
		String ret_status = null;
		ResultSet rs;
		ResultSet rs1;

		StringBuffer stb = new StringBuffer();

		String[] emailList = null;
		String[] recipientscc = {}, recipientsbcc = {};


        
		rs = d.executeQuery("select * from user_details where flag=1");

		
		// loop for all the Users
		while (rs.next()) {
			usercount++;
			stb.append(rs.getString("Email") + "|");
			idtemp=new UserUtility().getIDbyEmailID(rs.getString("Email"));
			d.executeUpdate("INSERT into alert_receiver_details values("+alertid+",'"+idtemp+"',0)");
			// Based on the provider add the mobile number to the Email List
			if (rs.getString("S_Provider").equalsIgnoreCase("0")) {
			} else {
				stb.append(rs.getString("Mobile") + rs.getString("S_Provider")
						+ "|");
				usercount++;
			}

			// Add ICE numbers to the email list based on the check
			if (icesend.equals("on")) {
				rs1 = d.executeQuery("select * from ice_contacts where user_id="+ Integer.parseInt(rs.getString("id")));

				while (rs1.next()) {
					usercount++;
					stb.append(rs1.getString("contact_email") + "|");
					idtemp=new UserUtility().getIDbyEmailID(rs1.getString("contact_email"));
					if(idtemp != 0)
						d.executeUpdate("INSERT into alert_receiver_details values("+alertid+",'"+idtemp+"',0)");

					// Based on the provider add the mobile number to the Email
					// List
					if (rs1.getString("S_Provider").equalsIgnoreCase("0")) {
					} else {
						stb.append(rs1.getString("contact_no")
								+ rs1.getString("S_Provider") + "|");
						usercount++;
					}
				}
				rs1.close();
			}
		}

		emailList = new String[usercount];

		StringTokenizer st = new StringTokenizer(stb.toString(), "|");
		int i = 0;

		// Add all the data
		while (st.hasMoreTokens()) {
			emailList[i] = st.nextToken();
			i++;
		}

		if (usercount > 0) {
			// Send message to all the users
			try {
				if (smtpMailSender.postMail(recipientsbcc, recipientscc,emailList
						, subject, message, from)) {
					ret_status = "success";
				} else {
					ret_status = "failure";
				}
			} catch (MessagingException e) {
				ret_status = "failure";
			}
		} else
			ret_status = "success";

		// Close all the connections
		rs.close();
		stb.delete(0, stb.length());
		d.close();
		return ret_status;
	}

	// Send Message based on Community
	private String sendSMStoCommunity(String message, String subject,
			String stdata, String icesend) throws SQLException {

		Dao d = new Dao();
		int commcount = 0;
		String ret_status = null;
		ResultSet rs1 = null;

		String from = ICEEnv.getInstance().getAdminEmail();
		String[] emailList = null;
		String[] recipientscc = {}, recipientsbcc = {};

		StringTokenizer st = new StringTokenizer(stdata, "|");

		StringBuffer stb = new StringBuffer();

		// loop for all the Communitie based on selected
		while (st.hasMoreElements()) {
			rs1 = d
					.executeQuery("select * from community_assigned where comm_id="
							+ Integer.parseInt(st.nextToken()));
			if (rs1.next()) {
				rs1 = d.executeQuery("select * from user_details where id="
						+ Integer.parseInt(rs1.getString("user_id")));
				if (rs1.next()) {
					commcount++;
					stb.append(rs1.getString("Email") + "|");
					idtemp=new UserUtility().getIDbyEmailID(rs1.getString("Email"));
					d.executeUpdate("INSERT into alert_receiver_details values("+alertid+",'"+idtemp+"',0)");
					
					if (rs1.getString("S_Provider").equalsIgnoreCase("0")) {
					} else {
						stb.append(rs1.getString("Mobile")
								+ rs1.getString("S_Provider") + "|");
						commcount++;
					}

					if (icesend.equals("on")) {
						rs1 = d
								.executeQuery("select * from ice_contacts where user_id="
										+ Integer.parseInt(rs1.getString("id")));
						while (rs1.next()) {
							commcount++;
							stb.append(rs1.getString("contact_email") + "|");							
							idtemp=new UserUtility().getIDbyEmailID(rs1.getString("contact_email"));							
							if(idtemp != 0)
								d.executeUpdate("INSERT into alert_receiver_details values("+alertid+",'"+idtemp+"',0)");
							if (rs1.getString("S_Provider").equalsIgnoreCase(
									"0")) {
							} else {
								stb.append(rs1.getString("contact_no")
										+ rs1.getString("S_Provider") + "|");
								commcount++;
							}
						}
					}
				}
			}
		}

		StringTokenizer st1 = new StringTokenizer(stb.toString(), "|");
		int i = 0;

		emailList = new String[commcount];
		while (st1.hasMoreTokens()) {
			emailList[i] = st1.nextToken();
			i++;
		}

		if (commcount > 0) {
			try {
				if (smtpMailSender.postMail(recipientsbcc, recipientscc,emailList
						, subject, message, from)) {
					ret_status = "success";
				} else {
					ret_status = "failure";
				}
			} catch (MessagingException e) {
				ret_status = "failure";
			}
		} else {
			ret_status = "success";
		}

		rs1.close();
		d.close();
		stb.delete(0, stb.length());
		return ret_status;
	}

	// Send Message based on Selected User
	private String sendSMStoSelectedUsers(String message, String subject,
			String stdata, String icesend) throws SQLException {

		Dao d = new Dao();
		int commcount = 0;
		String ret_status = null;
		ResultSet rs1 = null;

		String from = ICEEnv.getInstance().getAdminEmail();
		String[] emailList = null;
		String[] recipientscc = {}, recipientsbcc = {};

		StringTokenizer st_u = new StringTokenizer(stdata, "|");
		StringBuffer stb_u = new StringBuffer();

		// loop for all the Communitie based on selected
		while (st_u.hasMoreElements()) {
			rs1 = d.executeQuery("select * from user_details where id="+Integer.parseInt(st_u.nextToken()));
			if (rs1.next()) {
				commcount++;
				stb_u.append(rs1.getString("Email") + "|");
				idtemp=new UserUtility().getIDbyEmailID(rs1.getString("Email"));
				d.executeUpdate("INSERT into alert_receiver_details values("+alertid+",'"+idtemp+"',0)");
				if (rs1.getString("S_Provider").equalsIgnoreCase("0")) {
				} else {
					stb_u.append(rs1.getString("Mobile")
							+ rs1.getString("S_Provider") + "|");
					commcount++;
				}

				if (icesend.equals("on")) {
					rs1 = d
							.executeQuery("select * from ice_contacts where user_id="
									+ Integer.parseInt(rs1.getString("id")));
					while (rs1.next()) {
						commcount++;
						stb_u.append(rs1.getString("contact_email") + "|");
						idtemp=new UserUtility().getIDbyEmailID(rs1.getString("contact_email"));
						if(idtemp != 0)
							d.executeUpdate("INSERT into alert_receiver_details values("+alertid+",'"+idtemp+"',0)");
						if (rs1.getString("S_Provider").equalsIgnoreCase("0")) {
						} else {
							stb_u.append(rs1.getString("contact_no")
									+ rs1.getString("S_Provider") + "|");
							commcount++;
						}
					}
				}
			}
		}
		StringTokenizer st_u1 = new StringTokenizer(stb_u.toString(), "|");
		int i = 0;

		emailList = new String[commcount];

		while (st_u1.hasMoreTokens()) {
			emailList[i] = st_u1.nextToken();
			i++;
		}

		if (commcount > 0) {
			try {
				if (smtpMailSender.postMail(recipientsbcc, recipientscc,emailList
						, subject, message, from)) {
					ret_status = "success";
				} else {
					ret_status = "failure";
				}
			} catch (MessagingException e) {
				ret_status = "failure";
			}
		} else {
			ret_status = "success";
		}

		rs1.close();
		d.close();
		stb_u.delete(0, stb_u.length());
		return ret_status;
	}

	private String sendSMStoSelectedMobiles(String message, String subject,
			String stdata, String icesend) throws SQLException {

		Dao d = new Dao();
		int commcount = 0;
		String ret_status = null;
		ResultSet rs1 = null;

		String from = ICEEnv.getInstance().getAdminEmail();
		String[] emailList = null;
		String[] recipientscc = {}, recipientsbcc = {};

		StringTokenizer st_u = new StringTokenizer(stdata, "|");
		StringBuffer stb_u = new StringBuffer();

		// loop for all the Communitie based on selected
		while (st_u.hasMoreElements()) {
			rs1 = d.executeQuery("select * from user_details where id="
					+ Integer.parseInt(st_u.nextToken()));
			if (rs1.next()) {
				commcount++;
				stb_u.append(rs1.getString("Email") + "|");
				idtemp=new UserUtility().getIDbyEmailID(rs1.getString("Email"));
				d.executeUpdate("INSERT into alert_receiver_details values("+alertid+",'"+idtemp+"',0)");
				if (rs1.getString("S_Provider").equalsIgnoreCase("0")) {
				} else {
					stb_u.append(rs1.getString("Mobile")
							+ rs1.getString("S_Provider") + "|");
					commcount++;
				}

				if (icesend.equals("on")) {
					rs1 = d
							.executeQuery("select * from ice_contacts where user_id="
									+ Integer.parseInt(rs1.getString("id")));
					while (rs1.next()) {
						commcount++;
						stb_u.append(rs1.getString("contact_email") + "|");
						idtemp=new UserUtility().getIDbyEmailID(rs1.getString("contact_email"));
						if(idtemp != 0)
							d.executeUpdate("INSERT into alert_receiver_details values("+alertid+",'"+idtemp+"',0)");
						if (rs1.getString("S_Provider").equalsIgnoreCase("0")) {
						} else {
							stb_u.append(rs1.getString("contact_no")
									+ rs1.getString("S_Provider") + "|");
							commcount++;
						}
					}
				}
			}
		}
		StringTokenizer st_u1 = new StringTokenizer(stb_u.toString(), "|");
		int i = 0;

		emailList = new String[commcount];

		while (st_u1.hasMoreTokens()) {
			emailList[i] = st_u1.nextToken();
			i++;
		}

		if (commcount > 0) {
			try {
				if (smtpMailSender.postMail(recipientsbcc, recipientscc,emailList
						, subject, message, from)) {
					ret_status = "success";
				} else {
					ret_status = "failure";
				}
			} catch (MessagingException e) {
				ret_status = "failure";
			}
		} else {
			ret_status = "success";
		}

		rs1.close();
		d.close();
		stb_u.delete(0, stb_u.length());
		return ret_status;
	}

	private String sendSMStoSelectedEmails(String message, String subject,
			String stdata, String icesend) throws SQLException {

		Dao d = new Dao();
		int commcount = 0;
		String ret_status = null;
		ResultSet rs1 = null;

		String from = ICEEnv.getInstance().getAdminEmail();
		String[] emailList = null;
		String[] recipientscc = {}, recipientsbcc = {};

		StringTokenizer st_u = new StringTokenizer(stdata, "|");
		StringBuffer stb_u = new StringBuffer();

		// loop for all the Communitie based on selected
		while (st_u.hasMoreElements()) {
			rs1 = d.executeQuery("select * from user_details where id="
					+ Integer.parseInt(st_u.nextToken()));
			if (rs1.next()) {
				commcount++;
				stb_u.append(rs1.getString("Email") + "|");
				idtemp=new UserUtility().getIDbyEmailID(rs1.getString("Email"));
				d.executeUpdate("INSERT into alert_receiver_details values("+alertid+",'"+idtemp+"',0)");
				if (rs1.getString("S_Provider").equalsIgnoreCase("0")) {
				} else {
					stb_u.append(rs1.getString("Mobile")
							+ rs1.getString("S_Provider") + "|");
					commcount++;
				}

				if (icesend.equals("on")) {
					rs1 = d
							.executeQuery("select * from ice_contacts where user_id="
									+ Integer.parseInt(rs1.getString("id")));
					while (rs1.next()) {
						commcount++;
						stb_u.append(rs1.getString("contact_email") + "|");
						idtemp=new UserUtility().getIDbyEmailID(rs1.getString("contact_email"));
						if(idtemp != 0)
							d.executeUpdate("INSERT into alert_receiver_details values("+alertid+",'"+idtemp+"',0)");
						if (rs1.getString("S_Provider").equalsIgnoreCase("0")) {
						} else {
							stb_u.append(rs1.getString("contact_no")
									+ rs1.getString("S_Provider") + "|");
							commcount++;
						}
					}
				}
			}
		}
		StringTokenizer st_u1 = new StringTokenizer(stb_u.toString(), "|");
		int i = 0;

		emailList = new String[commcount];

		while (st_u1.hasMoreTokens()) {
			emailList[i] = st_u1.nextToken();
			i++;
		}

		if (commcount > 0) {
			try {
				if (smtpMailSender.postMail(recipientsbcc, recipientscc,emailList
						, subject, message, from)) {
					ret_status = "success";
				} else {
					ret_status = "failure";
				}
			} catch (MessagingException e) {
				ret_status = "failure";
			}
		} else {
			ret_status = "success";
		}

		rs1.close();
		d.close();
		stb_u.delete(0, stb_u.length());
		return ret_status;
	}

	private String sendSMStoSelectedCountry(String message, String subject,
			String stdata, String icesend) throws SQLException {

		Dao d = new Dao();
		int commcount = 0;
		String ret_status = null;
		ResultSet rs1 = null;

		String from = ICEEnv.getInstance().getAdminEmail();
		String[] emailList = null;
		String[] recipientscc = {}, recipientsbcc = {};

		StringTokenizer st_u = new StringTokenizer(stdata, "|");
		StringBuffer stb_u = new StringBuffer();

		// loop for all the Communitie based on selected
		while (st_u.hasMoreElements()) {
			rs1 = d.executeQuery("select * from user_details where id="
					+ Integer.parseInt(st_u.nextToken()));
			if (rs1.next()) {
				commcount++;
				stb_u.append(rs1.getString("Email") + "|");
				idtemp=new UserUtility().getIDbyEmailID(rs1.getString("Email"));
				d.executeUpdate("INSERT into alert_receiver_details values("+alertid+",'"+idtemp+"',0)");
				if (rs1.getString("S_Provider").equalsIgnoreCase("0")) {
				} else {
					stb_u.append(rs1.getString("Mobile")
							+ rs1.getString("S_Provider") + "|");
					commcount++;
				}

				if (icesend.equals("on")) {
					rs1 = d
							.executeQuery("select * from ice_contacts where user_id="
									+ Integer.parseInt(rs1.getString("id")));
					while (rs1.next()) {
						commcount++;
						stb_u.append(rs1.getString("contact_email") + "|");
						idtemp=new UserUtility().getIDbyEmailID(rs1.getString("contact_email"));
						if(idtemp != 0)
							d.executeUpdate("INSERT into alert_receiver_details values("+alertid+",'"+idtemp+"',0)");
						if (rs1.getString("S_Provider").equalsIgnoreCase("0")) {
						} else {
							stb_u.append(rs1.getString("contact_no")
									+ rs1.getString("S_Provider") + "|");
							commcount++;
						}
					}
				}
			}
		}
		StringTokenizer st_u1 = new StringTokenizer(stb_u.toString(), "|");
		int i = 0;

		emailList = new String[commcount];

		while (st_u1.hasMoreTokens()) {
			emailList[i] = st_u1.nextToken();
			i++;
		}

		if (commcount > 0) {
			try {
				if (smtpMailSender.postMail(recipientsbcc, recipientscc,emailList
						, subject, message, from)) {
					ret_status = "success";
				} else {
					ret_status = "failure";
				}
			} catch (MessagingException e) {
				ret_status = "failure";
			}
		} else {
			ret_status = "success";
		}

		rs1.close();
		d.close();
		stb_u.delete(0, stb_u.length());
		return ret_status;
	}
	
	private String sendSMStoSelectedOccupation(String message, String subject,
			String stdata, String icesend) throws SQLException {

		Dao d = new Dao();
		int commcount = 0;
		String ret_status = null;
		ResultSet rs1 = null;
		String from = ICEEnv.getInstance().getAdminEmail();
		String[] emailList = null;
		String[] recipientscc = {}, recipientsbcc = {};

		StringTokenizer st_u = new StringTokenizer(stdata, "|");
		StringBuffer stb_u = new StringBuffer();

		// loop for all the Communitie based on selected
		while (st_u.hasMoreElements()) {
			rs1 = d.executeQuery("select * from user_details where id="
					+ Integer.parseInt(st_u.nextToken()));
			if (rs1.next()) {
				commcount++;
				stb_u.append(rs1.getString("Email") + "|");
				idtemp=new UserUtility().getIDbyEmailID(rs1.getString("Email"));
				d.executeUpdate("INSERT into alert_receiver_details values("+alertid+",'"+idtemp+"',0)");
				if (rs1.getString("S_Provider").equalsIgnoreCase("0")) {
				} else {
					stb_u.append(rs1.getString("Mobile")
							+ rs1.getString("S_Provider") + "|");
					commcount++;
				}

				if (icesend.equals("on")) {
					rs1 = d
							.executeQuery("select * from ice_contacts where user_id="
									+ Integer.parseInt(rs1.getString("id")));
					while (rs1.next()) {
						commcount++;
						stb_u.append(rs1.getString("contact_email") + "|");
						idtemp=new UserUtility().getIDbyEmailID(rs1.getString("contact_email"));
						if(idtemp != 0)
							d.executeUpdate("INSERT into alert_receiver_details values("+alertid+",'"+idtemp+"',0)");
						if (rs1.getString("S_Provider").equalsIgnoreCase("0")) {
						} else {
							stb_u.append(rs1.getString("contact_no")
									+ rs1.getString("S_Provider") + "|");
							commcount++;
						}
					}
				}
			}
		}
		StringTokenizer st_u1 = new StringTokenizer(stb_u.toString(), "|");
		int i = 0;

		emailList = new String[commcount];

		while (st_u1.hasMoreTokens()) {
			emailList[i] = st_u1.nextToken();
			i++;
		}

		if (commcount > 0) {
			try {
				if (smtpMailSender.postMail(recipientsbcc, recipientscc,emailList
						, subject, message, from)) {
					ret_status = "success";
				} else {
					ret_status = "failure";
				}
			} catch (MessagingException e) {
				ret_status = "failure";
			}
		} else {
			ret_status = "success";
		}

		rs1.close();
		d.close();
		stb_u.delete(0, stb_u.length());
		return ret_status;
	}
}
