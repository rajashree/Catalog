package com.snipl.ice.alert;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mysql.jdbc.ResultSet;
import com.snipl.ice.security.Dao;

public class ViewRinviteAction extends Action {

	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException 
	{
		if(request.getSession().getAttribute("security_id")!=null)
		{
			String community_id=request.getParameter("rinvite");
			int k=Integer.parseInt(community_id.charAt(0)+"");
			int div=Integer.parseInt(community_id.substring(1, community_id.length()-1));
			int rem=Integer.parseInt(community_id.charAt(community_id.length()-1)+"");
			int c_id=div*k+rem;
			Dao d=null;
			ResultSet rs = null;
			ResultSet rs1 =null;
			ResultSet rs2 =null;
			int user_id=Integer.parseInt(request.getSession().getAttribute("security_id").toString());
			try {
				d = new Dao();
				String communityname = null;
				String ownername = null; 
				String desc = null;
				int flag = 0;
				int communityid = 0;
				String date = null;
				int no_users =0;
				String creationdate=null;
				InviteBeanForm ibean = new InviteBeanForm();
					String str1 = "SELECT * FROM community_details where id = '"+c_id+"'"; 
					rs1 = d.executeQuery(str1);
					if(rs1.next())
					{
						String str2 ="SELECT F_Name, L_Name FROM user_details where id ='"+rs1.getString("owner")+"'"; 
						rs2 = d.executeQuery(str2);
						if(rs2.next())
						{
							ownername = rs2.getString("F_Name")+rs2.getString("L_Name");
						}
						communityname = rs1.getString("name");	
						creationdate =  rs1.getString("creation_date");
					}
					ibean.setCommunity_id(community_id);
					ibean.setCommunity_name(communityname);
					ibean.setDatetime(creationdate);
					ibean.setOwnername(ownername);
					ibean.setOwner_id(rs1.getInt("owner"));
				request.setAttribute("rinviteinfo", ibean );
				return (mapping.findForward("success"));
			}
			catch (SQLException e) 
			{
				e.printStackTrace();
				return (mapping.findForward("failure"));
			}
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}
}
