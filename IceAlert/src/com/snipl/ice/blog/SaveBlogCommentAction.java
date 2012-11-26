package com.snipl.ice.blog;

/**
* @Author Kamalakar Challa 
*   
*/
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.ResultSet;
import com.snipl.ice.security.Dao;
import com.snipl.ice.utility.UserUtility;

public class SaveBlogCommentAction extends Action
{
	
	ResultSet rs;
	LinkedHashMap hm;
	
 public static Map blogs = new HashMap();

	public ActionForward execute(
									ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response) 
									throws Exception
	{
		if(request.getSession().getAttribute("security_bid")!=null)
		{
			if(request.getParameter(("captchaid"))!=null)
			{
				if(request.getParameter(("captchaid")).equals(getCaptcha(request.getParameter(("captchaIds")))))
				{
					int id = Integer.parseInt(request.getSession().getAttribute("security_bid").toString());
					Dao d=new Dao();
			        try{
			        	
			        	String str= "INSERT into blog_info values (?,?,?)";
			        	//Date date = new Date();
			        	
			        	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						Date currentdate = new java.sql.Date(System.currentTimeMillis());
						String datestring=dateFormat.format(currentdate);
			        				        
			        	hm=new LinkedHashMap();
			        	hm.put("s1", request.getParameter("comm"));
			        	hm.put("i2", id);
			        	hm.put("s3", datestring);
			        
			        	d.executeUpdate(hm,str);
			        	hm.clear();

			        	ResultSet rs = null;
						rs = d.executeQuery("select * from blog_info");
						List bloglist=new ArrayList();
						while (rs.next()) {
							BlogBean blogBean=new BlogBean();
							blogBean.setComments(rs.getString("comments"));
							blogBean.setUser_id(new UserUtility().getUserName(Integer.parseInt(rs.getString("user_id"))));
							blogBean.setDoc(rs.getString("doc"));
							bloglist.add(blogBean);
						}
						d.close();
						if(bloglist.size()>0)					
							request.setAttribute("bloglist", bloglist);
						return mapping.findForward("success");
			        	
		        	}
			        catch(Exception e){
			        	return mapping.findForward("failure");
			        }
				}
				else
				{
					request.setAttribute("comm", request.getParameter("comm"));
					return mapping.findForward("failure");
				}
			}
			else
			{
				request.setAttribute("comm", request.getParameter("comm"));
				return mapping.findForward("failure");
			}	
		}
		else
			return mapping.findForward("sessionExpaired_Frame");
		
	}
	 public static void addCaptcha(String hash, String captcha) {
		 blogs.put(hash, captcha);
    }
	
    public static String getCaptcha(String hash) {
		return (String) blogs.get(hash);
    }

}