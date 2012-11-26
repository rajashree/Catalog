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

public class CommunityViewAction extends Action{

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
			int user_id=Integer.parseInt(request.getSession().getAttribute("security_id").toString());
			q="select * from user_details where id='"+user_id+"'";
			rs=d.executeQuery(q);
			try {
				if(rs.next())
				{
					String community_id=request.getParameter("communityid");
					if(!(""+community_id.charAt(0)).equalsIgnoreCase("0"))
					{
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
							cbean.setCommunity_description(rs.getString("description"));
							cbean.setCommunity_imgurl("CommunityPhoto.ice?communityid="+community_id);
							cbean.setCommunity_creation_date(rs.getString("creation_date"));
							int c_owner=rs.getInt("owner");
							if(c_owner==user_id)
								request.setAttribute("owner_view", "0");
							q="select F_Name from user_details where id='"+c_owner+"'";
							rs=d.executeQuery(q);
							if(rs.next())
								cbean.setCommunity_owner(rs.getString("F_Name"));
							q="select * from community_assigned where comm_id='"+c_id+"' and user_id='"+user_id+"'";
							rs=d.executeQuery(q);
							if(rs.next())
								request.setAttribute("sus_view", "1");
							request.setAttribute("cview", cbean);
							
							d.close();
							return mapping.findForward("view");
						}
						else
						{
							d.close();
							return mapping.findForward("failure");
						}
					}
					else
					{
						community_id=community_id.substring(1, community_id.length());
						request.setAttribute("community_id", community_id);
						return mapping.findForward("alert");
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
