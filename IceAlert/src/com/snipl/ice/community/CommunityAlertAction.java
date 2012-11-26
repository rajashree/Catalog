package com.snipl.ice.community;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.snipl.ice.security.Dao;

public class CommunityAlertAction extends Action{

	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {
		if(request.getSession().getAttribute("security_id")!=null)
		{
			String q;
			ResultSet rs;
		
			Dao d=new Dao();
			q="select * from user_details where id='"+request.getSession().getAttribute("security_id")+"'";
			rs=d.executeQuery(q);
			try {
				if(rs.next())
				{
					String community_id=null;
					if(request.getAttribute("community_id")!=null)
						community_id=(String) request.getAttribute("community_id");
					else
						community_id=request.getParameter("communityid");				
					int k=Integer.parseInt(community_id.charAt(0)+"");
					int div=Integer.parseInt(community_id.substring(1, community_id.length()-1));
					int rem=Integer.parseInt(community_id.charAt(community_id.length()-1)+"");
					int c_id=div*k+rem;
					q="select * from community_details where id='"+c_id+"'";
					rs=d.executeQuery(q);
					CommunityBean cbean=new CommunityBean();
					if(rs.next())
					{
						cbean.setCommunity_id(community_id);/*Kamal*/
						cbean.setCommid(""+c_id+"");/*Kamal*/
						cbean.setCommunity_name(rs.getString("name"));
						cbean.setCommunity_no_users(rs.getString("no_users")+"Members");
						request.setAttribute("mem", rs.getString("no_users"));
						cbean.setCommunity_description(rs.getString("description"));
						cbean.setCommunity_imgurl("CommunityPhoto.ice?communityid="+community_id);
						cbean.setCommunity_creation_date(rs.getString("creation_date"));
						q="select F_Name from user_details where id='"+rs.getInt("owner")+"'";
						rs=d.executeQuery(q);
						if(rs.next())
							cbean.setCommunity_owner(rs.getString("F_Name"));
						request.setAttribute("cview", cbean);					
						d.close();
						return mapping.findForward("success");
					}
					else
					{
						d.close();
						return mapping.findForward("failure");
					}
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