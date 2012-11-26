package com.snipl.ice.community;

import java.sql.SQLException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mysql.jdbc.ResultSet;
import com.snipl.ice.security.Dao;

public class CommunityJoinAction extends Action
{
	
	public ActionForward execute(
									ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response) 
									throws Exception
	{
		if(request.getSession().getAttribute("security_id")!=null)
		{
			Dao d = new Dao();
			ResultSet rs;
			int user_id=Integer.parseInt(request.getSession().getAttribute("security_id").toString());
			String q="select * from user_details where id='"+user_id+"'";
			rs=d.executeQuery(q);
			try {
				if(rs.next())
				{
					String community_id=request.getParameter("communityid");
					int k=Integer.parseInt(community_id.charAt(0)+"");
					int div=Integer.parseInt(community_id.substring(1, community_id.length()-1));
					int rem=Integer.parseInt(community_id.charAt(community_id.length()-1)+"");
					int c_id=div*k+rem;
					d.executeUpdate("update community_assigned set flag=1 where comm_id="+c_id+" and user_id="+user_id);
					q="select * from community_details where id='"+c_id+"'";
					rs=d.executeQuery(q);
					CommunityBean cbean=new CommunityBean();
					if(rs.next())
					{
						k=0;rem=0;
						int dev;
						do {
							Random r = new Random();
							k =(int) (r.nextGaussian() * 10);
							k = k % 10;
							if (k < 0)
								k =-k;
						} while (k == 0 || k == 1);
						
						dev = c_id / k;
						rem = c_id % k;
						cbean.setCommunity_id(""+k+""+dev+""+rem);
						cbean.setCommunity_imgurl("CommunityPhoto.ice?communityid="+k+""+dev+""+rem);
						
						
						cbean.setCommid(""+c_id+"");/*Kamal*/
						cbean.setCommunity_name(rs.getString("name"));
						cbean.setCommunity_no_users(rs.getString("no_users")+"Members");
						cbean.setCommunity_description(rs.getString("description"));
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
						
						int no_users=0;
						rs=d.executeQuery("select no_users from community_details where id="+c_id);
				        /*if(rs.next())
				        	no_users=rs.getInt("no_users");
				        no_users++;
				        d.executeUpdate("update community_details set no_users="+no_users+" where id="+c_id);*/
						d.close();
					}
					//return mapping.findForward("success");
				}
				return mapping.findForward("success");
			}
			catch (SQLException e) {
			
				return mapping.findForward("failure");
			}	
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}
}
