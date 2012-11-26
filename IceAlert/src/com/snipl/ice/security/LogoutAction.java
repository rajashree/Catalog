package com.snipl.ice.security;

/**
* @Author Kamalakar Challa
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

public class LogoutAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		request.getSession().setAttribute("security_id",null);
		request.getSession().setAttribute("security_profile",null);
		request.getSession().invalidate();
		request.setAttribute("status", 4);
		return mapping.findForward("success");
	}

}
