package com.snipl.ice.settings;

/**
* @Author Sankara Rao
*   
*/
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.snipl.ice.security.Dao;
import com.snipl.ice.utility.IceImage;

public class Upload_BuddyphotoAction extends Action{
	ResultSet rs;
	

	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {
		if(request.getSession().getAttribute("security_id")!=null)
		{
			int id = Integer.parseInt(request.getSession().getAttribute("security_id").toString());
			Dao d= new Dao();
			rs = d.executeQuery("SELECT * FROM user_details WHERE id="+id);
	        try {
					if(rs.next())
					{
						if(form!=null)
						{
							Upload_BuddyphotoForm file_form=(Upload_BuddyphotoForm) form;
							
							FormFile file=file_form.getFile();
					        int fileSize       = file.getFileSize();
					        byte[] fileData    = IceImage.resize(file.getFileData(),80,80);
					        InputStream is=new ByteArrayInputStream(fileData);
					        Vector v=new Vector();
					        v.addElement(is);
					        v.addElement(fileSize);
					        rs = d.executeQuery("SELECT * FROM user_photo WHERE id="+id);
					        String Str=null;
					        LinkedHashMap hm=new LinkedHashMap();
					        if(rs.next())
					        	Str="UPDATE user_photo SET image = ? WHERE id ='"+id+"'";
					        else
					        {
						        Str= "INSERT into user_photo(id,image) values (?,?)";
						        hm.put("i1",id);
					        }
					        hm.put("f2",v);
					        int k=d.executeUpdate(hm,Str);
					        if(k!=0)
					        {
					        d.close();
					        request.getSession().setAttribute("user_photo", "user");
					        return mapping.findForward("success");
					        }
						}
						return null;
					}
			}catch(SQLException e)
		    {
		    	System.out.println("SqlException caught"+e.getMessage());
		    } 
			return mapping.findForward("sessionExpaired_Frame");
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}

}
