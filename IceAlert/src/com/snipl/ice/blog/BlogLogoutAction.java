package com.snipl.ice.blog;
/**
* @Author Kamalakar Challa & Sankara Rao
*   
*/

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class BlogLogoutAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		request.getSession().setAttribute("security_bid", null);
		request.getSession().setAttribute("security_name", null);
		request.getSession().setAttribute("role", null);
		request.setAttribute("status", 2);
		

		return mapping.findForward("success");
	}

}
