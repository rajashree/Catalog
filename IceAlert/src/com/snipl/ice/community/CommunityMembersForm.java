package com.snipl.ice.community;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class CommunityMembersForm  extends ActionForm
{
	private static final long serialVersionUID = 1L;
	private int userid;
//	private String username = null;
	private String contactname = null;
	private String contactno;
	private String contactemail = null;
	private String country=  null;
	private String sprovider= null;
	private String mobileext = null;
	
   public void setUserid(int uid)
   {
	   this.userid = uid; 
   }
   public int getUserid()
   {
	   return this.userid; 
   }
   
//   public void setUsername(String uname)
//   {
//	   this.username = uname; 
//   }
//   public String getUsername()
//   {
//	   return this.username; 
//   }
   
   public void setContactname(String cname)
   {
	   this.contactname = cname; 
   }
   public String getContactname()
   {
	   return this.contactname; 
   }
   
   public void setContactno(String cno)
   {
	   this.contactno = cno; 
   }
   public String getContactno()
   {
	   return this.contactno; 
   }
   
   public void setContactemail(String cemail)
   {
	   this.contactemail = cemail; 
   }
   public String getContactemail()
   {
	   return this.contactemail; 
   }
   
   public void setCountry(String country)
   {
	   this.country = country; 
   }
   public String getCountry()
   {
	   return this.country; 
   }
   
   public void setSprovider(String sprovider)
   {
	   this.sprovider = sprovider; 
   }
   public String getSprovider()
   {
	   return this.sprovider; 
   }
   
   public void setMobileext(String mobileext)
   {
	   this.mobileext = mobileext; 
   }
   public String getMobileext()
   {
	   return this.mobileext; 
   }
  
   
   public void reset(ActionMapping mapping,HttpServletRequest request){

	   this.contactemail = null;
	   this.contactname	=  null;
		this.contactno = null;
		this.country = null;
		this.sprovider = null;
		this.userid = 0;
//		this.username = null;
		this.mobileext=null;
   }

} 