package com.snipl.ice.alert;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.snipl.ice.security.Dao;

public class DeleteAlertInfoAction extends Action
{
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException 
	{
		if(request.getSession().getAttribute("security_id")!=null)
		{
			String userid = request.getSession().getAttribute("security_id").toString();
			String alertid =  request.getParameter("adelinfo");
			String method=request.getParameter("method");
			Dao d=new Dao();
			String qs=null;
			if(method.equalsIgnoreCase("ralert"))
				//qs="delete from alert_receiver_details where receiver_id='"+userid+"' and alert_id="+alertid;
				qs="update alert_receiver_details set del_flag=1 where receiver_id='"+userid+"' and alert_id="+alertid;
			else if(method.equalsIgnoreCase("salert"))
				qs="update alert_sender_details set alter_flag=1 where sender_id='"+userid+"' and alert_id="+alertid;
			d.executeUpdate(qs);
			if(method.equalsIgnoreCase("ralert"))
				return mapping.findForward("ralert");
			else
				return mapping.findForward("salert");
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}
}