package com.snipl.ice.blog;
/**
* @Author Kamalakar Challa & Sankara Rao
*   
*/

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
import com.snipl.ice.utility.UserUtility;

public class BlogLoginAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		Dao d=null ;
		ResultSet rs = null;
		String role = null;
		try {
			d = new Dao();
			rs = d.executeQuery("select * from user_details where Email='"+ request.getParameter("buser") + "'");
			if (rs.next()) {
				String s = new UserUtility().decrypt(rs.getString("Password"));				
				if (request.getParameter("bpwd").equals(s)) {					
					role = rs.getString("Role");
					request.setAttribute("login", 2);
					request.getSession().setAttribute("security_bid", rs.getString("id"));
					//request.getSession().setAttribute("security_name", new UserUtility().getUserName(Integer.parseInt(rs.getString("id"))));
					request.getSession().setAttribute("role", role);
					return mapping.findForward("success");
				}
				else 
				{
					request.setAttribute("login", 1);
					return mapping.findForward("failure");
				}
			} else {
				request.setAttribute("login", 1);
				return mapping.findForward("failure");
			}
			} catch (SQLException e) {
				System.out.println("SqlException caught" + e.getMessage());
				return mapping.findForward("failure");
			}
	}

}
