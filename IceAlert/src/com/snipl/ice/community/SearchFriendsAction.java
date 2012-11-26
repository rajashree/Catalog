package com.snipl.ice.community;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mysql.jdbc.ResultSet;
import com.snipl.ice.security.Dao;

public class SearchFriendsAction extends Action
{
	
	ResultSet rs;
	LinkedHashMap hm;
	
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) 
			throws Exception
	{
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		ResultSet rs4 = null;
			
			if(request.getSession().getAttribute("security_id")!=null)
			{
				Dao d=new Dao();
				int id = Integer.parseInt(request.getSession().getAttribute("security_id").toString());

				if(request.getParameter("friendtype").equalsIgnoreCase("icemember"))
				{
					List icememberlist = new ArrayList();
					try{
						String str = "SELECT * FROM ice_contacts where user_id='"+id+"'";
						rs = d.executeQuery(str);
						
						while(rs.next())
						{
							IceMembersForm icebean = new IceMembersForm();
//							String str1 ="SELECT F_Name, L_Name FROM user_details where id ='"+rs.getString("user_id")+"'"; 
//							rs1 = d.executeQuery(str1);
//							String user_name = null;
//							if(rs1.next())
//							{
//								user_name = rs1.getString("F_Name")+" "+rs1.getString("L_Name");
//							}
							icebean.setContactemail(rs.getString("contact_email"));
							icebean.setContactname(rs.getString("contact_name"));
//							icebean.setUsername(user_name);
							icememberlist.add(icebean);
						}
						
						if(icememberlist.size()>0)
							request.setAttribute("icemembers", icememberlist);
						request.setAttribute("count", icememberlist.size());
						if(request.getParameter("prev_contact_friend")!=null)
							request.setAttribute("prev_contact_friend", request.getParameter("prev_contact_friend"));
						return (mapping.findForward("success"));
					}catch (SQLException e) 
					{
						e.printStackTrace();
						return (mapping.findForward("failure"));
					}
				}
				
				else if(request.getParameter("friendtype").equalsIgnoreCase("community"));
				{
					List commmemberlist = new ArrayList();
					try{
						String str2  ="SELECT id FROM community_details WHERE owner = '"+id+"'";
						rs2 = d.executeQuery(str2);
						
						while(rs2.next())
						{
							
							String str3  = "SELECT user_id FROM community_assigned WHERE comm_id='"+rs2.getString("id")+"' AND user_id!=(SELECT owner FROM community_details WHERE id='"+id+"')";
							rs3 = d.executeQuery(str3);
							while(rs3.next())
							{
								CommunityMembersForm commbean = new CommunityMembersForm();
								String str4 ="SELECT F_Name, L_Name, Email FROM user_details where id ='"+rs3.getString("user_id")+"'"; 
								rs4 = d.executeQuery(str4);
								String user_name = null;
								if(rs4.next())
								{
									user_name = rs4.getString("F_Name")+" "+rs4.getString("L_Name");
									commbean.setContactemail(rs4.getString("Email"));
									commbean.setContactname(user_name);									
								}
								commmemberlist.add(commbean);
								//commbean.setUserid(rs3.getInt("user_id"));
								
							}
						}
						if(commmemberlist.size()>0)
							request.setAttribute("community", commmemberlist);
						request.setAttribute("count", commmemberlist.size());
						if(request.getParameter("prev_contact_friend")!=null)
							request.setAttribute("prev_contact_friend", request.getParameter("prev_contact_friend"));
						return (mapping.findForward("success"));
					}catch (SQLException e) 
					{
						e.printStackTrace();
						return (mapping.findForward("failure"));
					}
				}
			}		
			else
				return mapping.findForward("sessionExpaired_Frame");			
	}
}
