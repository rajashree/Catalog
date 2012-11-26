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

public class ViewSalertAction  extends Action {

	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException 
	{
		if(request.getSession().getAttribute("security_id")!=null)
		{
			String alertid = request.getParameter("salert");
			Dao d=null;
			ResultSet rs = null;
			ResultSet rs1 = null;
			ResultSet rs2 = null;
			ResultSet rs3 = null;
			try 
			{
				d = new Dao();	
				AlertBeanForm abean = new AlertBeanForm();
				String str = "SELECT * FROM alert_sender_details where alert_sender_details.alert_id='"+alertid+"'";
				rs = d.executeQuery(str);
				if(rs.next())
				{
					String str1 ="SELECT F_Name, L_Name FROM user_details where id ='"+rs.getString("sender_id")+"'"; 
					rs1 = d.executeQuery(str1);
					String sender_name = null;
					if(rs1.next())
					{
						sender_name = rs1.getString("F_Name")+rs1.getString("L_Name");
					}
				
					String str2 = "SELECT receiver_id FROM alert_receiver_details WHERE alert_id ='"+alertid+"'";
					rs2 = d.executeQuery(str2);
					List rname = new ArrayList();
					int i = 0;
					String receiver_name =null;
					String fullname =null;
					while(rs2.next())
					{
						String str4 ="SELECT F_Name, L_Name FROM user_details where id ='"+rs2.getString("receiver_id")+"'"; 
						rs3 = d.executeQuery(str4);
						if(rs3.next())
						{	
							fullname =rs3.getString("F_Name")+rs3.getString("L_Name");
							rname.add(fullname);
						}
						i++;
					}
					receiver_name = rname.toString();
					
					abean.setAlert_id(rs.getInt("alert_id"));
					abean.setBody(rs.getString("body"));
					abean.setDatetime(rs.getString("date_time"));
					abean.setSender_name(sender_name);
					abean.setSubject(rs.getString("subject"));
					abean.setType(rs.getInt("type"));
					abean.setReceiver_name(receiver_name);
					
					
					
					
					switch(rs.getInt("type"))
					{
					case 1 :abean.setAlert_imgurl("assets/images/alert_1.png"); //result+="<td><img src='assets/images/alert_1.png'></td>";
							break;
					case 2 :abean.setAlert_imgurl("assets/images/alert_2.png"); //result+="<td><img src='assets/images/alert_2.png'></td>";
							break;		
					case 3 :abean.setAlert_imgurl("assets/images/alert_3.png");// result+="<td><img src='assets/images/alert_3.png'></td>";
							break;
					}
					
				}
				request.setAttribute("salertdetails", abean);
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
