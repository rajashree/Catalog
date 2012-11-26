package com.snipl.ice.blog;


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


public class PostComment extends Action{
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {
				
		if(request.getSession().getAttribute("security_bid")!=null)
		{
			return mapping.findForward("success");
			
		}
		else
		{
			return mapping.findForward("failure");
		}		
	}
}
