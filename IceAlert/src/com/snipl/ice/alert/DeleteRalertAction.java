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

public class DeleteRalertAction extends Action
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
			String clist =  request.getParameter("rdelinfo");
			String method=request.getParameter("method");
			Dao d=new Dao();
			String qs="";
			StringTokenizer st=new StringTokenizer(clist);
			int flag=0;
			if(method.equalsIgnoreCase("received"))
			{
				qs+="update alert_receiver_details set del_flag=1 where receiver_id='"+userid+"' and ";
				//qs+="delete from alert_receiver_details where receiver_id='"+userid+"' and ";
				while(st.hasMoreElements())
				{
					if(flag==0)
					{
						qs+="alert_id='"+st.nextElement()+"'";
						flag++;
					}
					else
						qs+=" or alert_id='"+st.nextElement()+"'";
				}
				d.executeUpdate(qs);
				
			}
			else if(method.equalsIgnoreCase("sented"))
			{
				qs+="update alert_sender_details set alter_flag=1 where sender_id='"+userid+"' and ";
				while(st.hasMoreElements())
				{
					if(flag==0)
					{
						qs+="alert_id='"+st.nextElement()+"'";
						flag++;
					}
					else
						qs+=" or alert_id='"+st.nextElement()+"'";
				}
				d.executeUpdate(qs);
			}
			
			if(method.equalsIgnoreCase("received"))
				return mapping.findForward("received");
			else
				return mapping.findForward("sented");
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}
}