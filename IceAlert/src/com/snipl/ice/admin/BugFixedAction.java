package com.snipl.ice.admin;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.snipl.ice.security.Dao;

public class BugFixedAction extends Action
{
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException 
		{
		if(request.getSession().getAttribute("security_id")!=null)
		{
			String bugid =  request.getParameter("bugid");
			Dao d=new Dao();
			try{
				d.executeUpdate("update report_bug set status=1 where rep_id="+bugid);
				return mapping.findForward("success");
			}
			catch(Exception e)
			{
				return mapping.findForward("failure");
			}
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}
}