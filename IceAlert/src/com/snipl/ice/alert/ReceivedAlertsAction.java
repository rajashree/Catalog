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

public class ReceivedAlertsAction extends Action
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
				try {
					d = new Dao();
					String str = "SELECT * FROM alert_sender_details, alert_receiver_details where alert_receiver_details.receiver_id ='"+userid+"' AND alert_sender_details.alert_id=alert_receiver_details.alert_id AND alert_receiver_details.del_flag=0";
					rs = d.executeQuery(str);
					List receivedalertlist = new ArrayList();
					AlterNavigationForm RalertForm=(AlterNavigationForm)form;
					int next=0,pre=0;
					if(request.getParameter("next")!=null)
						next=Integer.parseInt(RalertForm.getAlert_next());
					if(request.getParameter("previous")!=null)
						pre=Integer.parseInt(RalertForm.getAlert_pre());
					
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
						String str1 ="SELECT F_Name, L_Name FROM user_details where id ='"+rs.getString("sender_id")+"'"; 
						rs1 = d.executeQuery(str1);
						String sender_name = null;
						if(rs1.next())
						{
							sender_name = rs1.getString("F_Name")+rs1.getString("L_Name");
						}
						AlertBeanForm abean = new AlertBeanForm();
						abean.setAlert_id(rs.getInt("alert_id"));
						String date= rs.getString("date_time").substring(0, 10);
						abean.setDate(date);
						abean.setSender_name(sender_name);
						abean.setSubject(rs.getString("subject"));
						abean.setType(rs.getInt("type"));
						abean.setFlag(rs.getInt("flag"));
							switch(rs.getInt("type"))
							{
							case 1 :abean.setAlert_imgurl("assets/images/alert_1.gif"); //result+="<td><img src='assets/images/alert_1.png'></td>";
									break;
							case 2 :abean.setAlert_imgurl("assets/images/alert_2.gif"); //result+="<td><img src='assets/images/alert_2.png'></td>";
									break;		
							case 3 :abean.setAlert_imgurl("assets/images/alert_3.gif");// result+="<td><img src='assets/images/alert_3.png'></td>";
									break;		
							}
							receivedalertlist.add(abean);
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
					if(receivedalertlist.size()>0)
						request.setAttribute("ralertlist", receivedalertlist);
					else
						request.setAttribute("ralertlistflag", "true");
					request.setAttribute("count", receivedalertlist.size());
					return (mapping.findForward("success"));
				}
				catch (SQLException e) 
				{
					e.printStackTrace();
					System.out.println("failure");
					return (mapping.findForward("failure"));
				}
			}
			else
				return mapping.findForward("sessionExpaired_Frame");
	}
}
