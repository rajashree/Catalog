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

public class DeleteCommentinfoAction extends Action
{
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException 
		{
		if(request.getSession().getAttribute("security_id")!=null)
		{
			String clist =  request.getParameter("cdelinfo");
			String method=request.getParameter("method");
			Dao d=new Dao();
			String qs="";
			
			if(method.equalsIgnoreCase("feedback"))
			{	
				qs="delete from feedback_details where fd_id="+clist;
				d.executeUpdate(qs);
				
			}
			else if(method.equalsIgnoreCase("bug"))
			{
				qs="delete from report_bug where rep_id="+clist;
				d.executeUpdate(qs);
			}
			
			if(method.equalsIgnoreCase("feedback"))
				return mapping.findForward("feedback");
			else
				return mapping.findForward("bug");
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}
}