package com.snipl.ice.community;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mysql.jdbc.ResultSet;

public class SelectedContactsAction extends Action
{
	
	ResultSet rs;
	LinkedHashMap hm;
	
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
				SelectedContactsForm scForm=(SelectedContactsForm) form;
				StringBuffer sbuf=new StringBuffer(scForm.getQueary());
				int ind_home=sbuf.indexOf("home");
				if(ind_home!=-1)
				{
					sbuf.replace(0, sbuf.length(), sbuf.substring(4));
					ind_home=sbuf.indexOf("home");
					if(ind_home!=-1)
					{
						sbuf.replace(0, sbuf.length(), sbuf.substring(0,ind_home)+sbuf.substring(ind_home+4,sbuf.length()));
						request.setAttribute("friendids", sbuf.toString());
						return mapping.findForward("friend");
					}
				}
				request.setAttribute("contacts", scForm.getQueary());
				return mapping.findForward("success");
			}
		}
		return mapping.findForward("sessionExpaired_Frame");
	}
}
