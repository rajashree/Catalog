package com.snipl.ice.security;
/**
* @Author Kamalakar Challa & Sankara Rao
*   
*/

import java.awt.Image;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.ImageIcon;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mysql.jdbc.Blob;
import com.mysql.jdbc.ResultSet;
import com.oreilly.servlet.Base64Encoder;
import com.snipl.ice.community.CommunityBean;
import com.snipl.ice.utility.GeneralUtility;
import com.snipl.ice.utility.UserUtility;

public class LoginManagerAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if (form != null) {
			@SuppressWarnings("unused")
			String fname = null, City = null, country = null;
			Image userphoto = null;
			int cardstatus=0;
			int icecount=0;
			LoginForm log_frm = (LoginForm) form;

			String name = log_frm.getId_username();
			String pass = log_frm.getId_pass();
			boolean rememberMe = log_frm.getRememberMe();
			String role = null;
			int id = 0;
			ResultSet rs = null;
			int flag = 0;
			int f = 0;
			int status = 0;
			Dao d=null ;
			try {
				d = new Dao();
				rs = d.executeQuery("select * from user_details where Email='"
						+ name + "'");

				if (rs.next()) {
					f = rs.getInt("Flag");
					String s = new UserUtility().decrypt(rs
							.getString("Password"));
					if (pass.equals(s)) {
						fname = rs.getString("F_Name");
						City = rs.getString("City");
						country=rs.getString("Country");
						flag = 0;
						role = rs.getString("Role");
						id = Integer.parseInt(rs.getString("id"));
						request.setAttribute("security_iceid", rs.getString("ICEID"));
					} else {
						status = 2;
						flag++;
					}
				} else {
					status = 1;
					flag++;
				}
				
				if (flag == 0) {
					request.setAttribute("security_username", fname);
					request.setAttribute("security_prev", "0");
					HttpSession session=request.getSession();
					session.setAttribute("security_id", id);
					session.setAttribute("security_profile", role);
					int inv_count=0;
					rs=d.executeQuery("select * from community_assigned where flag=0 and user_id="+id);
		            while(rs.next())
		            {
		            	inv_count++;
		            }
		            request.setAttribute("invite_count",inv_count);
		            inv_count=0;
		            rs=d.executeQuery("select * from alert_receiver_details where flag=0 and receiver_id="+id+" and del_flag=0");
		            while(rs.next())
		            {
		            	inv_count++;
		            }
		            request.setAttribute("inbox_count", inv_count);
					
		            request.setAttribute("City",City);
		            request.setAttribute("country",new GeneralUtility().getFullCountry(country));
					rs = d.executeQuery("select * from community_details where owner="
							+ id );
					List list = new ArrayList();
					int k,dev,rem;
					do {
						Random r = new Random();
						k =(int) (r.nextGaussian() * 10);
						k = k % 10;
						if (k < 0)
							k =-k;
					} while (k == 0 || k == 1);
					int rscount=0;
					boolean c_flag=false;
					while(rs.next())
					{
						if(rscount<4)
						{
							CommunityBean cbean=new CommunityBean();
							int c_id=rs.getInt("id");
							dev = c_id / k;
							rem = c_id % k;
							cbean.setCommunity_id(""+k+""+dev+""+rem);
							cbean.setCommunity_imgurl("CommunityPhoto.ice?communityid="+k+""+dev+""+rem);
							cbean.setCommunity_name(rs.getString("name"));
							list.add(cbean);
						}
						else
							c_flag=true;
						rscount++;
					}
					if(c_flag)
						request.setAttribute("communityflag", "true");
					if(list.size()>0)
						request.setAttribute("list", list);
					
				/*Checking conditions to generate Card*/
					rs = d.executeQuery("select * from user_details where id="+ id);
					if (rs.next()) {
						if(rs.getString("Street").equalsIgnoreCase("") || rs.getString("Area").equalsIgnoreCase("") || rs.getString("City").equalsIgnoreCase("")||rs.getString("State").equalsIgnoreCase(""))
							cardstatus=3;//No Address Info
						if(rs.getString("Meds").equalsIgnoreCase("") || rs.getString("BloodGroup").equalsIgnoreCase("") || rs.getString("Conditions").equalsIgnoreCase("") || rs.getString("Allergies").equalsIgnoreCase(""))
						{
							cardstatus=4;//No Medical Info
						}
					}
					rs = d.executeQuery("select * from user_photo where id="+ id);	
					if(rs.next())
					{
						Blob aBlob;
						byte[] allBytesInBlob=null;
						aBlob =(Blob) rs.getBlob("image");
						allBytesInBlob= aBlob.getBytes(1, (int) aBlob.length());
						ImageIcon imageicon = new ImageIcon(allBytesInBlob);
						userphoto = imageicon.getImage();
					}

					if(userphoto == null)
					{
						cardstatus=2;//No Photo
					}
					rs = d.executeQuery("select * from ice_contacts where user_id="+ id);				
					while(rs.next())
					{
						icecount++;
					}	
					if(icecount <= 0 )
					{
						cardstatus=1;//No ICE Member
					}
					
					request.setAttribute("cardstatus",cardstatus);
				/*End*/	
				
					
					request.setAttribute("own_comm_count",rscount);
					d.close();
					if (rememberMe) {
						saveCookies(response, name, pass);
						} else {
						removeCookies(response);
						}
					if (f == 0)
						return mapping.findForward("conform_reg");
					else {
						if (role.equals("0"))
							return mapping.findForward("success");
						else {
							return mapping.findForward("success");
						}
					}

				} else {
					d.close();
					request.setAttribute("status", status);
					return mapping.findForward("failure");
				}
				
				
				
			} catch (SQLException e) {
				System.out.println("SqlException caught" + e.getMessage());
			}

			
		} else {
			request.setAttribute("status", 3);
			return mapping.findForward("failure");
		}
		return mapping.findForward("sessionExpaired");
	}
	
	private void saveCookies(HttpServletResponse response, String
			username, String password) {
			Cookie usernameCookie = new Cookie("StrutsCookbookUsername",
			Base64Encoder.encode(username));
			usernameCookie.setMaxAge(60 * 60 * 24 * 30); // 30 day expiration
			response.addCookie(usernameCookie);
			Cookie passwordCookie = new Cookie("StrutsCookbookPassword",
			Base64Encoder.encode(password));
			passwordCookie.setMaxAge(60 * 60 * 24 * 30); // 30 day expiration
			response.addCookie(passwordCookie);
		}
		private void removeCookies(HttpServletResponse response) {
//			expire the username cookie by setting maxAge to 0
//			(actual cookie value is irrelevant)
			Cookie unameCookie = new Cookie("StrutsCookbookUsername", "expired");
			unameCookie.setMaxAge(0);
			response.addCookie(unameCookie);
//			expire the password cookie by setting maxAge to 0
//			(actual cookie value is irrelevant)
			Cookie pwdCookie = new Cookie("StrutsCookbookPassword", "expired");
			pwdCookie.setMaxAge(0);
			response.addCookie(pwdCookie);
		}

}
