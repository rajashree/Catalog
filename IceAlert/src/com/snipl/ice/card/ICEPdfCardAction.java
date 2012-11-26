package com.snipl.ice.card;

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

import com.mysql.jdbc.ResultSet;


public class ICEPdfCardAction extends Action{
	
	ResultSet rs = null;
	
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {
		if(request.getSession().getAttribute("security_id")!=null)
		{
			String type=request.getParameter("type"); 
			int userid=Integer.parseInt(request.getSession().getAttribute("security_id").toString());
			if(type.equalsIgnoreCase("jpg")){
				JpgCardGenerator dp = new JpgCardGenerator();
				String card=dp.generateJPG(userid);			
				int status= dp.getCardStatus();
				request.setAttribute("status", status);
				if(status==0)
				{
					request.setAttribute("cardurl", card);	
					request.setAttribute("type", "jpg");	
				}
			}
			else {
				PdfCardGenerator dp = new PdfCardGenerator();
				String card=dp.generatePDF(userid);			
				int status= dp.getCardStatus();
				request.setAttribute("status", status);
				if(status==0)
				{
					request.setAttribute("cardurl", card);	
					request.setAttribute("type", "pdf");
				}
			}
			return (mapping.findForward("success"));
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}

}
