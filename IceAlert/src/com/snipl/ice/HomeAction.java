package com.snipl.ice;

import java.awt.Image;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mysql.jdbc.Blob;
import com.mysql.jdbc.ResultSet;
import com.snipl.ice.community.CommunityBean;
import com.snipl.ice.security.Dao;
import com.snipl.ice.utility.GeneralUtility;

public class HomeAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if(request.getSession().getAttribute("security_id")!=null)
		{
			if(request.getParameter("backToHome")!=null)
				request.setAttribute("friendids", request.getParameter("backToHome"));
			if(request.getAttribute("backToHome")!=null)
				request.setAttribute("friendids", request.getAttribute("backToHome"));
			String fname = null, City = null, country = null;
			Dao d=null;
			Image userphoto = null;
			int cardstatus=0;
			int icecount=0;
			ResultSet rs=null;
			d = new Dao();
			int id=Integer.parseInt(request.getSession().getAttribute("security_id").toString());
			try
			{
				rs = d.executeQuery("select * from user_details where id='"+id+ "'");
				if(rs.next())
				{
					fname = rs.getString("F_Name");
					City = rs.getString("City");
					country=rs.getString("Country");
					request.setAttribute("security_iceid", rs.getString("ICEID"));
				}
				request.setAttribute("security_username", fname);
				request.setAttribute("security_prev", "0");
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
				/*Checking conditions to generate Card*/
				rs = d.executeQuery("select * from user_details where id="+ id);
				if (rs.next()) {
					if(rs.getString("Street").equalsIgnoreCase("") || rs.getString("Area").equalsIgnoreCase("") || rs.getString("City").equalsIgnoreCase("")||rs.getString("State").equalsIgnoreCase(""))
						cardstatus=3;//No Address Info
					//if(rs.getString("Meds").equalsIgnoreCase("") || rs.getString("BloodGroup").equalsIgnoreCase("") || rs.getString("Conditions").equalsIgnoreCase("") || rs.getString("Allergies").equalsIgnoreCase(""))
					if(rs.getString("BloodGroup").equalsIgnoreCase(""))
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
				
				if(c_flag)
					request.setAttribute("communityflag", "true");
				if(list.size()>0)
					request.setAttribute("list", list);
				
				request.setAttribute("own_comm_count",rscount);
				d.close();
				return mapping.findForward("success");
			}
			catch(SQLException e)
			{
				
			}
			return mapping.findForward("failure");
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}
}
