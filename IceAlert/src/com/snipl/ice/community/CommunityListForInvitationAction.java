package com.snipl.ice.community;

import java.io.IOException;
import java.sql.ResultSet;
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

import com.snipl.ice.security.Dao;

public class CommunityListForInvitationAction  extends Action{

	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {
		if(request.getSession().getAttribute("security_id")!=null)
		{
			ResultSet rs;
			int user_id=Integer.parseInt(request.getSession().getAttribute("security_id").toString());
			Dao d=new Dao();
			List Commlist = new ArrayList();
			rs=d.executeQuery("select * from community_details where owner='"+user_id+"'");
			try {
				while(rs.next())
				{
					DummyBeanForm Commnbean=new DummyBeanForm();
					Commnbean.setName(rs.getString("name"));
					Commnbean.setId(rs.getString("id"));
					Commlist.add(Commnbean);
				}
				rs=d.executeQuery("select F_Name,L_Name from user_details where id='"+user_id+"'");
				if(rs.next())
					request.setAttribute("User_Name", rs.getString("F_Name")+" "+rs.getString("L_Name"));
				d.close();
				if(Commlist.size()>0)
					request.setAttribute("Commlist", Commlist);
				if(request.getAttribute("contacts")!=null)
					request.setAttribute("contacts", request.getAttribute("contacts"));
				return mapping.findForward("success");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return mapping.findForward("failure");
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}
}
