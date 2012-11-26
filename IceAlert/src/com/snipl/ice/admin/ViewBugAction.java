package com.snipl.ice.admin;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mysql.jdbc.ResultSet;
import com.snipl.ice.security.Dao;

public class ViewBugAction extends Action {

	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException 
	{
		if(request.getSession().getAttribute("security_id")!=null)
		{ 
			String bugid = request.getParameter("bugid");
			Dao d=null;
			ResultSet rs = null;
			ResultSet rs1 = null;
			try 
			{
				d = new Dao();	
				//String qs="update report_bug set status=1 where fd_id='"+feedid+"'";
				//d.executeUpdate(qs);
				String str = "SELECT * FROM report_bug where report_bug.rep_id='"+bugid+"'";
				rs = d.executeQuery(str);
				BugBeanForm bbean = new BugBeanForm();
				if(rs.next())
				{
					String str1 ="SELECT F_Name, L_Name FROM user_details where id ='"+rs.getString("user_id")+"'"; 
					rs1 = d.executeQuery(str1);
					String username = null;
					if(rs1.next())
					{
						username = rs1.getString("F_Name")+rs1.getString("L_Name");
					}
				
					bbean.setBrowsertype(rs.getString("btype"));
					bbean.setBrowserversion(rs.getString("bversion"));
					bbean.setBugid(rs.getInt("rep_id"));
					bbean.setCategory(rs.getString("category"));
					bbean.setComment(rs.getString("bug"));
					bbean.setUsername(username);
					bbean.setStatus(rs.getInt("status"));
					bbean.setUserid(rs.getInt("user_id"));
										
				}
				request.setAttribute("bugdetails", bbean);
				request.setAttribute("ret_flag", "bugs");
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
