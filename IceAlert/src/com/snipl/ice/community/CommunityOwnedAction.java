package com.snipl.ice.community;

import java.io.IOException;
import java.sql.ResultSet;
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

import com.snipl.ice.security.Dao;

public class CommunityOwnedAction  extends Action{

	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {
		if(request.getSession().getAttribute("security_id")!=null)
		{
			String q;
			ResultSet rs;
			int user_id=Integer.parseInt(request.getSession().getAttribute("security_id").toString());
			Dao d=new Dao();
			q="select * from user_details where id='"+user_id+"'";
			rs=d.executeQuery(q);
			try {
				if(rs.next())
				{
					AllCommunitiesForm cownForm=(AllCommunitiesForm)form;
					q="select * from community_details where owner='"+user_id+"'";
					rs=d.executeQuery(q);
					List Ownlist = new ArrayList();
					int k,dev,rem;
					do {
						Random r = new Random();
						k =(int) (r.nextGaussian() * 10);
						k = k % 10;
						if (k < 0)
							k =-k;
					} while (k == 0 || k == 1);
					int next=0,pre=0;
					if(request.getParameter("next")!=null)
						next=Integer.parseInt(cownForm.getCommunity_next());
					if(request.getParameter("previous")!=null)
						pre=Integer.parseInt(cownForm.getCommunity_pre());
					int comm_count=0;
					if(next!=pre)
						if(next!=0)
						{
							for(int n_rs=0;n_rs<next;n_rs++)
								rs.next();
							comm_count=next;
						}
						else
						{
							if(pre!=0)
							{
								for(int n_rs=0;n_rs<pre-10;n_rs++)
									rs.next();
								comm_count=pre-10;
							}
						}
					int rscount=0;
					while(rscount<10)
					{
						if(!rs.next())
							break;
						CommunityBean COwnbean=new CommunityBean();
						int c_id=rs.getInt("id");
						dev = c_id / k;
						rem = c_id % k;
						if(request.getParameter("alert")!=null)
							COwnbean.setCommunity_id("0"+k+""+dev+""+rem);
						else
							COwnbean.setCommunity_id(""+k+""+dev+""+rem);
						COwnbean.setCommunity_imgurl("CommunityPhoto.ice?communityid="+k+""+dev+""+rem);
						COwnbean.setCommunity_name(rs.getString("name"));
						Ownlist.add(COwnbean);
						rscount++;
						comm_count++;
					}
					boolean b=false;
					while(rs.next())
					{
						comm_count++;
						b=true;
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
						if(!b)
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
					if(cownForm.getAlert().equalsIgnoreCase("true"))
						request.setAttribute("alert_community", "true");
					request.setAttribute("comm_count", comm_count);
					request.setAttribute("next", next);
					request.setAttribute("previous", pre);
					if(Ownlist.size()>0)
						request.setAttribute("Ownlist", Ownlist);
					
					return mapping.findForward("success");
					
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			return mapping.findForward("failure");
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}
}
