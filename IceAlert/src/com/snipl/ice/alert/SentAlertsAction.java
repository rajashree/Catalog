package com.snipl.ice.alert;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mysql.jdbc.ResultSet;
import com.snipl.ice.security.Dao;

public class SentAlertsAction extends Action
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
			Dao d=null;
			ResultSet rs = null;
			ResultSet rs1 =null;
			ResultSet rs2 =null;
			try 
			{
				d = new Dao();
				String str ="SELECT * FROM alert_sender_details where alert_sender_details.sender_id ='"+userid+"' AND alert_sender_details.alter_flag=0";
				rs = d.executeQuery(str);
				List sentalertlist = new ArrayList();
				AlterNavigationForm SalertForm=(AlterNavigationForm)form;
				int next=0,pre=0;
				if(request.getParameter("next")!=null)
					next=Integer.parseInt(SalertForm.getAlert_next());
				if(request.getParameter("previous")!=null)
					pre=Integer.parseInt(SalertForm.getAlert_pre());
				if(next!=pre)
					if(next!=0)
					{
						for(int n_rs=0;n_rs<next;n_rs++)
							rs.next();
					}
					else
					{
						if(pre!=0)
							for(int n_rs=0;n_rs<pre-10;n_rs++)
								rs.next();
					}
				int rscount=0;
				while(rscount<10)
				{
					if(!rs.next())
						break;
					String id = rs.getString("alert_id");
					String str1 = "SELECT receiver_id FROM alert_receiver_details WHERE alert_id ='"+id+"'";
					rs1 = d.executeQuery(str1);
					List rname = new ArrayList();
					int i = 0;
					String receiver_name =null;
					String fullname =null;
					while(rs1.next())
					{
						String str2 ="SELECT F_Name, L_Name FROM user_details where id ='"+rs1.getString("receiver_id")+"'"; 
						rs2 = d.executeQuery(str2);
						if(rs2.next())
						{	
							fullname =rs2.getString("F_Name")+rs2.getString("L_Name");
							rname.add(fullname);
						}
						else if(!rs2.next())
						{
							rname.add(rs1.getString("receiver_id"));
						}
							
						i++;
					}
					receiver_name = rname.toString();
						
					String sender_id = rs.getString("sender_id");
					AlertBeanForm abean = new AlertBeanForm();
					abean.setAlert_id(rs.getInt("alert_id"));
					abean.setBody(rs.getString("body"));
					String date= rs.getString("date_time").substring(0, 10);
					abean.setDate(date);
					abean.setSender_id(rs.getInt("sender_id"));
					abean.setSubject(rs.getString("subject"));
					abean.setType(rs.getInt("type"));
					abean.setReceiver_name(receiver_name);
					
					switch(rs.getInt("type"))
					{
					case 1 :abean.setAlert_imgurl("assets/images/alert_1.gif"); //result+="<td><img src='assets/images/alert_1.png'></td>";
							break;
					case 2 :abean.setAlert_imgurl("assets/images/alert_2.gif"); //result+="<td><img src='assets/images/alert_2.png'></td>";
							break;		
					case 3 :abean.setAlert_imgurl("assets/images/alert_3.gif");// result+="<td><img src='assets/images/alert_3.png'></td>";
							break;		
					}
					
					sentalertlist.add(abean);
					rscount++;
				}
				if(rscount==10)
				{
					if(next==pre)
						next=10;
					else
					{
						if(pre!=0)
						{
							next=pre;
							pre-=10;
						}
						else
						{
							pre=next;
							next+=10;
						}
					}
					if(!rs.next())
						next=0;
				}
				d.close();
				if(rscount!=10)
				{
					if(next!=pre)
					{
						pre=next;
						next=0;
					}
				}
				request.setAttribute("next", next);
				request.setAttribute("previous", pre);
				if(sentalertlist.size()>0)
					request.setAttribute("salertlist", sentalertlist);
				else
					request.setAttribute("salertlistflag", "true");
				request.setAttribute("count", sentalertlist.size());
				return (mapping.findForward("success"));
			}
			catch (SQLException e) 
			{
				e.printStackTrace();
				return (mapping.findForward("failure"));
			}
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}
}