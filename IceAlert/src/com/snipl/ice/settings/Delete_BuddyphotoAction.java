package com.snipl.ice.settings;

/**
* @Author Sankara Rao
*   
*/

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.snipl.ice.security.Dao;

public class Delete_BuddyphotoAction extends Action{

	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {
		if(request.getSession().getAttribute("security_id")!=null)
		{
			ResultSet rs;
			String q;
			int Status=0;
			
			Dao d=new Dao();
			
			try {
				q="select * from user_photo where id='"+request.getSession().getAttribute("security_id")+"'";
				rs=d.executeQuery(q);
				if(rs.next())
				{
					rs=null;
					q="delete from user_photo where id='"+request.getSession().getAttribute("security_id")+"'";
					int k=0;
					k=d.executeUpdate(q);
					if(k!=0)
						Status=1;
				}
			}
			catch (Exception e) {
			}
			finally{
				d.close();
				if(Status==1)
				{
					request.getSession().setAttribute("user_photo", "default");	
				}
				else
				{
					request.getSession().setAttribute("user_photo", "user");	
				}
				return mapping.findForward("success");
			}
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
		
	}

}
