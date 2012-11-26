package com.snipl.ice.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mysql.jdbc.ResultSet;
import com.snipl.ice.security.Dao;

public class BugListAction extends Action
{
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException 
		{
			if(request.getSession().getAttribute("security_id")!=null)
			{
//				String userid = request.getSession().getAttribute("security_id").toString();
				
				Dao d=null;
				ResultSet rs = null;
				ResultSet rs1 =null;
				try {
					d = new Dao();
					String str = "SELECT * FROM report_bug";
					rs = d.executeQuery(str);
					List buglist = new ArrayList();
					int next=0,pre=0;
					if(request.getParameter("next")!=null)
						next=Integer.parseInt(request.getParameter("next"));
					if(request.getParameter("previous")!=null)
						pre=Integer.parseInt(request.getParameter("previous"));
					
					if(next!=pre)
						if(next!=0)
						{
							for(int n_rs=0;n_rs<next;n_rs++)
								rs.next();
						}
						else
						{
							if(pre!=0)
								for(int n_rs=0;n_rs<pre-10;n_rs++)
									rs.next();
						}
					int rscount=0;
					while(rscount<10)
					{
						if(!rs.next())
							break;
						BugBeanForm bbean = new BugBeanForm();
						String str1 ="SELECT F_Name, L_Name FROM user_details where id ='"+rs.getString("user_id")+"'"; 
						rs1 = d.executeQuery(str1);
						String user_name = null;
						if(rs1.next())
						{
							user_name = rs1.getString("F_Name")+" "+rs1.getString("L_Name");
						}
						bbean.setBrowsertype(rs.getString("btype"));
						bbean.setBrowserversion(rs.getString("bversion"));
						bbean.setBugid(rs.getInt("rep_id"));
						bbean.setCategory(rs.getString("category"));
						bbean.setComment(rs.getString("bug"));
						bbean.setUsername(user_name);
						bbean.setStatus(rs.getInt("status"));
						bbean.setUserid(rs.getInt("user_id"));
						buglist.add(bbean);	
						rscount++;
					}
					if(rscount==10)
					{
						if(next==pre)
							next=10;
						else
						{
							if(pre!=0)
							{
								next=pre;
								pre-=10;
							}
							else
							{
								pre=next;
								next+=10;
							}
						}
						if(!rs.next())
							next=0;
					}
					d.close();
					if(rscount!=10)
					{
						if(next!=pre)
						{
							pre=next;
							next=0;
						}
					}
					request.setAttribute("next", next);
					request.setAttribute("previous", pre);
					request.setAttribute("ret_flag", "bugs");
					if(buglist.size()>0)
					{
						request.setAttribute("buglist", buglist);
						request.setAttribute("count", buglist.size());
						return (mapping.findForward("success"));
					}
					else
						return (mapping.findForward("failure"));
				}
				catch (SQLException e) 
				{
					e.printStackTrace();
					System.out.println("failure");
					return (mapping.findForward("failure"));
				}
			}
			else
				return mapping.findForward("sessionExpaired_Frame");
	}
}
					
					
					
					
					
					
					
					
					
					
