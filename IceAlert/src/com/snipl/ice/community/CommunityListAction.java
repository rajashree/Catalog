package com.snipl.ice.community;

import java.io.IOException;
import java.sql.ResultSet;
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

import com.snipl.ice.security.Dao;

public class CommunityListAction extends Action{

	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {
		
		if(request.getSession().getAttribute("security_id")!=null)
		{
			String q;
			ResultSet rs,rs1;
			int user_id=Integer.parseInt(request.getSession().getAttribute("security_id").toString());
			Dao d=new Dao();
			q="select * from user_details where id='"+user_id+"'";
			rs=d.executeQuery(q);
			try {
				if(rs.next())
				{
					q="select comm_id from community_assigned where user_id='"+user_id+"'";
					rs=d.executeQuery(q);
					List Own_Community_list = new ArrayList();
					List Subscribed_Community_list = new ArrayList();
					boolean ownedcommunitiesflag=false,subscribedcommunitiesflag=false;
					int ownedcommunities=0,subscribedcommunities=0;
					int k,dev,rem;
					do {
						Random r = new Random();
						k =(int) (r.nextGaussian() * 10);
						k = k % 10;
						if (k < 0)
							k =-k;
					} while (k == 0 || k == 1);
					int rscount=0;
					while(rs.next())
					{
						int community_id=rs.getInt("comm_id");
						q="select * from community_details where id='"+community_id+"'";
						rs1=d.executeQuery(q);
						
						if(rs1.next())
						{
							boolean innerflag=false;
							int community_owner=rs1.getInt("owner");
							if(community_owner==user_id)
							{
								if(ownedcommunities==4)
									ownedcommunitiesflag=true;
								else
								{
									ownedcommunities++;
									innerflag=true;
								}
							}
							else
							{
								if(subscribedcommunities==4)
									subscribedcommunitiesflag=true;
								else
								{
									subscribedcommunities++;
									innerflag=true;
								}
							}
							if(innerflag)
							{
								CommunityBean cbean_list=new CommunityBean();
								int c_id=rs1.getInt("id");
								dev = c_id / k;
								rem = c_id % k;
								cbean_list.setCommunity_id(""+k+""+dev+""+rem);
								cbean_list.setCommunity_imgurl("CommunityPhoto.ice?communityid="+k+""+dev+""+rem);
								cbean_list.setCommunity_name(rs1.getString("name"));
								
								if(community_owner==user_id)
									Own_Community_list.add(cbean_list);
								else
									Subscribed_Community_list.add(cbean_list);
							}
						}
					}
					d.close();
					if(ownedcommunitiesflag)
						request.setAttribute("ownedcommunitysflag", "true");
					if(subscribedcommunitiesflag)
						request.setAttribute("subscribedcommunitiesflag", "true");
					if(Own_Community_list.size()>0)
						request.setAttribute("Own",Own_Community_list);
					if(Subscribed_Community_list.size()>0)
						request.setAttribute("sub",Subscribed_Community_list);
					
					return mapping.findForward("success");
					
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			
			return mapping.findForward("sessionExpaired_Frame");
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}
}
