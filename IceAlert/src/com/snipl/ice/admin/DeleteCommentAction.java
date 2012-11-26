package com.snipl.ice.admin;

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

public class DeleteCommentAction extends Action
{
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException 
		{
		if(request.getSession().getAttribute("security_id")!=null)
		{
			String clist =  request.getParameter("adelinfo");
			String method=request.getParameter("method");
			Dao d=new Dao();
			String qs="";
			StringTokenizer st=new StringTokenizer(clist);
			int flag=0;
			if(method.equalsIgnoreCase("feedback"))
			{
				//qs+="update alert_receiver_details set del_flag=1 where receiver_id='"+userid+"' and ";
				qs+="delete from feedback_details where ";
				while(st.hasMoreElements())
				{
					if(flag==0)
					{
						qs+="fd_id='"+st.nextElement()+"'";
						flag++;
					}
					else
						qs+=" or fd_id='"+st.nextElement()+"'";
				}
				d.executeUpdate(qs);
				
			}
			else if(method.equalsIgnoreCase("bug"))
			{
				//qs+="update alert_sender_details set alter_flag=1 where sender_id='"+userid+"' and ";
				qs+="delete from report_bug where ";
				while(st.hasMoreElements())
				{
					if(flag==0)
					{
						qs+="rep_id='"+st.nextElement()+"'";
						flag++;
					}
					else
						qs+=" or rep_id='"+st.nextElement()+"'";
				}
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