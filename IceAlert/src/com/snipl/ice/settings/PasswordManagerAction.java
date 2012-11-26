package com.snipl.ice.settings;

/**
* @Author Kamalakar Challa
*   
*/


import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mysql.jdbc.ResultSet;
import com.snipl.ice.security.Dao;
import com.snipl.ice.utility.UserUtility;

public class PasswordManagerAction extends Action{
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {
		if(request.getSession().getAttribute("security_id")!=null)
		{
			if(form!=null)
			{
				Dao d=new Dao();
				PasswordManagerForm pwd_frm=(PasswordManagerForm) form;
				
				String oldpass = pwd_frm.getId_cur_pass();
				String newpass = pwd_frm.getId_new_pass();
				int flag=0;
				
				int id = Integer.parseInt(request.getSession().getAttribute("security_id").toString());
				try 
				{
					
			        String str= "SELECT Password FROM user_details WHERE id="+id;
			        ResultSet rs = d.executeQuery(str);
			        
			        if(rs.next())
			        {
			        	String st=new UserUtility().decrypt(rs.getString("Password"));
			        	if(oldpass.equals(st)){
			        		str= "UPDATE user_details set password='"+new UserUtility().encrypt(newpass)+"' WHERE id="+id;
			    	        d.executeUpdate(str);
			    	        flag=3;
			        	}
			        	else
			        	{
			        		flag=2;
			        	}
			        }
			        
				}  catch (SQLException e) {
					e.printStackTrace();
				}
				 finally
		        {
			        		d.close();
		        } 
				 request.setAttribute("flag", flag);			 
				 pwd_frm.reset(mapping, request);
				 if(flag==3){
					 return mapping.findForward("success"); 
				 }
				 else if(flag==2)
				 {
					 return mapping.findForward("failure");
				 }
	
			}
			return mapping.findForward("sessionExpaired_Frame");
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}

}
