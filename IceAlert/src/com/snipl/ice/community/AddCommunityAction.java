package com.snipl.ice.community;

/**
* @Author Chethan jayaraj 
*   
*/
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import com.mysql.jdbc.ResultSet;
import com.snipl.ice.security.Dao;
import com.snipl.ice.utility.IceImage;

public class AddCommunityAction extends Action
{
	
	ResultSet rs;
	LinkedHashMap hm;
	
	public ActionForward execute(
									ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response) 
									throws Exception
	{
		if(request.getSession().getAttribute("security_id")!=null)
		{
			AddCommunityForm comaddform = (AddCommunityForm) form;
			
			int id = Integer.parseInt(request.getSession().getAttribute("security_id").toString());
			Dao d=new Dao();
			ResultSet rs = d.executeQuery("SELECT * FROM user_details WHERE id="+id);
			
			if(rs.next())
			{
				String str_1 = "SELECT * FROM community_details WHERE name= ?";
				LinkedHashMap hm=new LinkedHashMap();
				hm.put("s1",comaddform.getCommunity_name());
				rs = d.executeQuery(hm,str_1);
				if(!rs.next())
				{
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					Date currentdate = new java.sql.Date(System.currentTimeMillis());
					String datestring=dateFormat.format(currentdate);
					
			        try{
			        	
			        	String str= "INSERT into community_details(name,owner,creation_date,description) values (?,?,?,?)";
			        
			        	hm=new LinkedHashMap();
			        	hm.put("s1", comaddform.getCommunity_name());
			        	hm.put("i2", id);
			        	hm.put("s3", datestring);
			        	hm.put("s4", comaddform.getDesciption());
			        
			        	d.executeUpdate(hm,str);
			        	hm.clear();
			        	
			        	
			        	String str_2 = "SELECT id FROM community_details where name = ? and owner="+Integer.parseInt(request.getSession().getAttribute("security_id").toString())+" and creation_date='"+datestring+"'";
						hm=new LinkedHashMap();
						hm.put("s1",comaddform.getCommunity_name());
						rs = d.executeQuery(hm,str_2);
			        	hm.clear();
			        	
			        	
			        	
			        	
			        	int commid = 0;
			        	if(rs.next())
			        	{
			        		commid = rs.getInt("id");
			        		FormFile file=comaddform.getFile();
			        		if(file!=null)
			        		{
						        int fileSize       = file.getFileSize();
						        byte[] fileData    = IceImage.resize(file.getFileData(),80,80);
						        InputStream is=new ByteArrayInputStream(fileData);
						        Vector v=new Vector();
						        v.addElement(is);
						        v.addElement(fileSize);
			        			String str2="INSERT into community_photo(community_id,image) values(?,?)";
			        			hm.clear();
			        			hm.put("i1", commid);
			        			hm.put("f2",v);
			        			d.executeUpdate(hm,str2);
			        			hm.clear();
			        		}
				        	String str3="INSERT into community_assigned(comm_id,user_id,flag) values(?,?,?)";
				        	hm.put("i1", commid);
				        	hm.put("i2", id);
				        	hm.put("i3", 1);
				        	d.executeUpdate(hm,str3);
				        	
				        	List dCommlist = new ArrayList();
				        	List Commlist = new ArrayList();
				    		rs=d.executeQuery("select * from community_details where owner='"+id+"'");
				    		String CommAdded=null;
				    		while(rs.next())
							{
				    			if(comaddform.getCommunity_name().equalsIgnoreCase(rs.getString("name")))
				    				CommAdded=rs.getString("id");
				    			else
				    			{
								DummyBeanForm Commnbean=new DummyBeanForm();
								Commnbean.setName(rs.getString("name"));
								Commnbean.setId(rs.getString("id"));
								dCommlist.add(Commnbean);
				    			}
							}
				    		DummyBeanForm Commnbean=new DummyBeanForm();
							Commnbean.setName(comaddform.getCommunity_name());
							Commnbean.setId(CommAdded);
							Commlist.add(Commnbean);
							Commlist.addAll(dCommlist);
							if(Commlist.size()>0)
							{
								request.setAttribute("Commlist", Commlist);
								request.setAttribute("CommAddFlag", CommAdded);
							}
				        	d.close();
				        	return mapping.findForward("success");
			        	}
			        }
			        catch(Exception e){
			        	return mapping.findForward("failure");
			        }
				}
				else
				{
					d.close();
					return mapping.findForward("failure");
				}
			}
			else
			{
				d.close();
				return mapping.findForward("failure");
			}
			return mapping.findForward("sessionExpaired_Frame");
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
	}
}