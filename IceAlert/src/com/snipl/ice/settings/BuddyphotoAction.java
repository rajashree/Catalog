package com.snipl.ice.settings;

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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mysql.jdbc.Blob;
import com.snipl.ice.security.Dao;

public class BuddyphotoAction extends Action{

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
			q="select * from user_photo where id='"+request.getSession().getAttribute("security_id")+"'";
			rs=d.executeQuery(q);
			OutputStream o = response.getOutputStream();
			response.setContentType("image/jpg");
			Blob aBlob;
			byte[] allBytesInBlob=null;
			if(rs.next())
			{
				request.getSession().setAttribute("user_photo", "user");
				aBlob =(Blob) rs.getBlob("image");
				allBytesInBlob= aBlob.getBytes(1, (int) aBlob.length());
			 }
			else
			{
				q="select * from user_photo where id='0'";
				rs=d.executeQuery(q);
				if(rs.next())
				{
					request.getSession().setAttribute("user_photo", "default");
					aBlob =(Blob) rs.getBlob("image");
					allBytesInBlob= aBlob.getBytes(1, (int) aBlob.length());
				 }
			}
			o.write(allBytesInBlob);
		  	o.flush();
			o.close();
			d.close();
		} catch (Exception e) {
		}
		return null;
		}
		return null;
	}
	
}
