package com.snipl.ice.community;

/**
* @Author Kamalakar Challa 
*   
*/
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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

public class SearchCommunityAction extends Action{
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {
		if(request.getSession().getAttribute("security_id")!=null)
		{
			if(form!=null)
			{
				int comm_count=0;
				Dao d=null;
				//SearchCommunityForm search_frm=(SearchCommunityForm) form;
				d = new Dao();
		        
				String search =  request.getParameter("id_search");//search_frm.getId_search();
				ResultSet rs = null;
				String str = "SELECT * FROM community_details where name like ?";
				LinkedHashMap hm=new LinkedHashMap();
				hm.put("s1","%"+search+"%" );
				rs = d.executeQuery(hm,str);
				AllCommunitiesForm searchForm=(AllCommunitiesForm)form;
				List searchlist = new ArrayList();
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
					next=Integer.parseInt(searchForm.getCommunity_next());
				if(request.getParameter("previous")!=null)
					pre=Integer.parseInt(searchForm.getCommunity_pre());
				try 
				{
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
						CommunityBean CSearchbean=new CommunityBean();
						int c_id=rs.getInt("id");
						dev = c_id / k;
						rem = c_id % k;
						CSearchbean.setCommunity_id(""+k+""+dev+""+rem);
						CSearchbean.setCommunity_imgurl("CommunityPhoto.ice?communityid="+k+""+dev+""+rem);
						CSearchbean.setCommunity_name(rs.getString("name"));
						searchlist.add(CSearchbean);
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
					if(rscount!=10)
					{
						if(next!=pre)
						{
							pre=next;
							next=0;
						}
					}
				}catch (SQLException e) {
					e.printStackTrace();
				}
				d.close();
				request.setAttribute("comm_count", comm_count);
				request.setAttribute("next", next);
				request.setAttribute("previous", pre);
				if(searchlist.size()>0)
					request.setAttribute("Searchlist", searchlist);
				request.setAttribute("searchkey", search);
				return mapping.findForward("success");
			}
			return mapping.findForward("sessionExpaired_Frame");
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}

}