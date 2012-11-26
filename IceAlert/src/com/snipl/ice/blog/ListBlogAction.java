package com.snipl.ice.blog;


/**
* @Author Kamalakar Challa
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
import com.snipl.ice.config.ICEEnv;
import com.snipl.ice.security.Dao;
import com.snipl.ice.utility.GeneralUtility;
import com.snipl.ice.utility.UserUtility;


public class ListBlogAction extends Action{
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {
		
			ResultSet rs = null;
			Dao d=null;
			try {
				d = new Dao();
				rs = d.executeQuery("select * from blog_info");
				List bloglist=new ArrayList();
				while (rs.next()) {
					BlogBean blogBean=new BlogBean();
					blogBean.setComments(rs.getString("comments"));
					blogBean.setUser_id(new UserUtility().getUserName(Integer.parseInt(rs.getString("user_id"))));
					blogBean.setDoc(rs.getString("doc"));
					bloglist.add(blogBean);
				}
				d.close();
				if(bloglist.size()>0)					
					request.setAttribute("bloglist", bloglist);
				return mapping.findForward("success");
					
			} catch (SQLException e) {
				System.out.println("SqlException caught" + e.getMessage());
		}
		return mapping.findForward("success");
	}
}
