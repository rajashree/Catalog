package com.snipl.ice.alert;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mysql.jdbc.ResultSet;
import com.snipl.ice.security.Dao;

public class ReceivedInvitesAction extends Action
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
			try {
				d = new Dao();
				String str = "SELECT * FROM community_assigned where user_id ='"+userid+"'AND flag='"+0+"'";
				rs = d.executeQuery(str);
				List receivedinvitelist = new ArrayList();
				String ownername = null; 
				String date = null;
				AlterNavigationForm IalertForm=(AlterNavigationForm)form;
				int next=0,pre=0;
				if(request.getParameter("next")!=null)
					next=Integer.parseInt(IalertForm.getAlert_next());
				if(request.getParameter("previous")!=null)
					pre=Integer.parseInt(IalertForm.getAlert_pre());
				
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
					String str1 = "SELECT name, owner, creation_date, description, no_users FROM community_details where id = '"+rs.getString("comm_id")+"'"; 
					rs1 = d.executeQuery(str1);
					if(rs1.next())
					{
						String str2 ="SELECT F_Name, L_Name FROM user_details where id ='"+rs1.getString("owner")+"'"; 
						rs2 = d.executeQuery(str2);
						if(rs2.next())
						{
							ownername = rs2.getString("F_Name")+rs2.getString("L_Name");
						}
						date= rs1.getString("creation_date").substring(0, 10);
					}
					int k,dev,rem;
					do {
						Random r = new Random();
						k =(int) (r.nextGaussian() * 10);
						k = k % 10;
						if (k < 0)
							k =-k;
					} while (k == 0 || k == 1);
					
					int c_id=rs.getInt("comm_id");
					dev = c_id / k;
					rem = c_id % k;
					
					InviteBeanForm ibean = new InviteBeanForm();
					ibean.setCommunity_id(""+k+""+dev+""+rem);
					ibean.setCommunity_name(rs1.getString("name"));
					ibean.setDate(date);
					ibean.setDatetime(rs1.getString("creation_date"));
					ibean.setDescription(rs1.getString("description"));
					ibean.setFlag(rs.getInt("flag"));
					ibean.setNo_users(rs1.getInt("no_users"));
					ibean.setOwnername(ownername);
					ibean.setOwner_id(rs1.getInt("owner"));
					receivedinvitelist.add(ibean);
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
				if(receivedinvitelist.size()>0)
					request.setAttribute("rinvitelist", receivedinvitelist);
				else
					request.setAttribute("rinvitelistflag", "true");
				request.setAttribute("count", receivedinvitelist.size());
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
