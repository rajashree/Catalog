package com.snipl.ice.provider;


/**
* @Author Sankara Rao
*   
*/

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.snipl.ice.utility.Providers;

public class ProvidersAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if (form != null) {
			ProvidersForm reg_frm = (ProvidersForm) form;
			Providers providers = new Providers();
			response.setContentType("text/XML");
			PrintWriter out = response.getWriter();
			out.print(providers.mprovider(reg_frm.getCountry()));
		}
		return null;
	}

}
