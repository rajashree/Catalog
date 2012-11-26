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

public class ViewFeedbackAction extends Action {

	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException 
	{
		if(request.getSession().getAttribute("security_id")!=null)
		{ 
			String feedid = request.getParameter("feedid");
			Dao d=null;
			ResultSet rs = null;
			ResultSet rs1 = null;
			try 
			{
				d = new Dao();	
				String qs="update feedback_details set status=1 where fd_id='"+feedid+"'";
				d.executeUpdate(qs);
				String str = "SELECT * FROM feedback_details where feedback_details.fd_id='"+feedid+"'";
				rs = d.executeQuery(str);
				FeedbackBeanForm fbean = new FeedbackBeanForm();
				if(rs.next())
				{
					String str1 ="SELECT F_Name, L_Name FROM user_details where id ='"+rs.getString("user_id")+"'"; 
					rs1 = d.executeQuery(str1);
					String username = null;
					if(rs1.next())
					{
						username = rs1.getString("F_Name")+rs1.getString("L_Name");
					}
				
					fbean.setComment(rs.getString("comment"));
					fbean.setFeedbackid(rs.getInt("fd_id"));
					fbean.setStatus(rs.getInt("status"));
					fbean.setUserid(rs.getInt("user_id"));
					fbean.setUsername(username);
					
				}
				request.setAttribute("feedbackdetails", fbean);
				request.setAttribute("ret_flag", "feedback");
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
