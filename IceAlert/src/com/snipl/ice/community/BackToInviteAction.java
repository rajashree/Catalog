package com.snipl.ice.community;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class BackToInviteAction extends Action
{
	public ActionForward execute(
									ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response) 
									throws Exception
	{
		if(request.getSession().getAttribute("security_id")!=null)
		{
			if(form!=null)
			{
				BackToInviteForm btForm=(BackToInviteForm) form;
				request.setAttribute("contacts", btForm.getBackToInvite());
				return mapping.findForward("success");
			}
		}
		return mapping.findForward("sessionExpaired_Frame");
	}
}
