package com.snipl.ice.community;

/**
* @Author Kamalakar Challa 
*   
*/
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mysql.jdbc.ResultSet;
import com.snipl.ice.security.Dao;

public class JoinCommunityAction extends Action
{
	
	Dao d = new Dao();
	ResultSet rs;
	String str=null;
	int no_users=0;
	
	public ActionForward execute(
									ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response) 
									throws Exception
	{
		if(request.getSession().getAttribute("security_id")!=null)
		{
	        try{
	        	String community_id=request.getParameter("communityid").toString();
				int k=Integer.parseInt(community_id.charAt(0)+"");
				int div=Integer.parseInt(community_id.substring(1, community_id.length()-1));
				int rem=Integer.parseInt(community_id.charAt(community_id.length()-1)+"");
				int c_id=div*k+rem;
	        	str="INSERT into community_assigned(comm_id,user_id,flag) values(?,?,?)";
	        	LinkedHashMap hm=new LinkedHashMap();
		        hm.put("i1",c_id);
		        hm.put("i2",request.getSession().getAttribute("security_id").toString());
		        hm.put("i3",1);
		        d.executeUpdate(hm,str);
		        rs=d.executeQuery("select no_users from community_details where id="+c_id);
		        if(rs.next())
		        	no_users=rs.getInt("no_users");
		        no_users++;
		        d.executeUpdate("update community_details set no_users="+no_users+" where id="+c_id);
	    	
	        	return mapping.findForward("success");
	        }
	        catch(Exception e){
	        	return mapping.findForward("failure");
	        }
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}
}
