package com.snipl.ice.community;

/**
* @Author Sankara Rao
*   
*/

import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mysql.jdbc.Blob;
import com.snipl.ice.security.Dao;

public class CommunityphotoAction extends Action{

	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {
		if(request.getSession().getAttribute("security_id")!=null)
		{
				String q;
				ResultSet rs;
			
			Dao d=new Dao();
			try {
				q="select * from user_details where id='"+request.getSession().getAttribute("security_id")+"'";
				rs=d.executeQuery(q);
				OutputStream o = response.getOutputStream();
				response.setContentType("image/jpg");
				Blob aBlob;
				byte[] allBytesInBlob=null;
				if(rs.next())
				{
					String community_id=request.getParameter("communityid");
					int k=Integer.parseInt(community_id.charAt(0)+"");
					int div=Integer.parseInt(community_id.substring(1, community_id.length()-1));
					int rem=Integer.parseInt(community_id.charAt(community_id.length()-1)+"");
					int c_id=div*k+rem;
					q="select * from community_photo where community_id='"+c_id+"'";
					rs=d.executeQuery(q);
					if(rs.next())
					{
						aBlob =(Blob) rs.getBlob("image");
						allBytesInBlob= aBlob.getBytes(1, (int) aBlob.length());
					 }
					else
					{
						q="select * from community_photo where community_id='0'";
						rs=d.executeQuery(q);
						if(rs.next())
						{
							aBlob =(Blob) rs.getBlob("image");
							allBytesInBlob= aBlob.getBytes(1, (int) aBlob.length());
						 }
					}
					o.write(allBytesInBlob);
				  	o.flush();
					o.close();
					d.close();
				}
			} catch (Exception e) {
			}
			return null;
		}
		else
			return null;
	}
	
}
