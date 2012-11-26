
/********************************************************************************

* Raining Data Corp.

*

* Copyright (c) Raining Data Corp. All Rights Reserved.

*

* This software is confidential and proprietary information belonging to

* Raining Data Corp. It is the property of Raining Data Corp. and is protected

* under the Copyright Laws of the United States of America. No part of this

* software may be copied or used in any form without the prior

* written permission of Raining Data Corp.

*

*********************************************************************************/

package com.rdta.security;


import java.util.Hashtable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class User implements HttpSessionBindingListener{

	private static Log log = LogFactory.getLog("User");
	
	private String sessionid;
	private String userid;
	private String username;
	private String firstname;
	private String lastname;
	private String tp_company_nm;
	private String tp_company_id;
	private String clientIP;
	private String[] roles;
	private String password;
	private boolean disable;
	/**
	 * @param accesslevel
	 * @param sessionid
	 * @param tp_company_id
	 * @param tp_company_nm
	 * @param userid
	 * @param username
	 */
	public User(String userid, String username ,String password,String firstname,String lastname,String clientIP,String tp_company_id, String tp_company_nm,boolean disable) {
		super();
		// TODO Auto-generated constructor stub
		
		this.tp_company_id = tp_company_id;
		this.tp_company_nm = tp_company_nm;
		this.userid = userid;
		this.username = username;
		this.password = password;
		this.firstname= firstname;
		this.lastname= lastname;
		this.clientIP = clientIP; 
		this.disable = disable;
	}
	
	public User(){
		super();
	}
	
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	public String getTp_company_id() {
		return tp_company_id;
	}
	public void setTp_company_id(String tp_company_id) {
		this.tp_company_id = tp_company_id;
	}
	public String getTp_company_nm() {
		return tp_company_nm;
	}
	public void setTp_company_nm(String tp_company_nm) {
		this.tp_company_nm = tp_company_nm;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String[] getRoles() {
		return roles;
	}
	public void setRoles(String[] roles) {
		this.roles = roles;
	}
	
	
	public boolean passwordMatch(String pwd) {
		log.info("In passwordMatch method");
		
	    return password.equals(pwd);
	  }

	
	  public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public boolean hasRole(String role) {
	    if (roles.length > 0) {
	      for (int i=0; i<roles.length; i++) {
	        if (role.equals(roles[i])) return true;
	      }
	    }
	    return false;
	  }

	  public boolean isAdministrator() {
	    return hasRole("administrator");
	  }
	
	  
	  public void valueBound(HttpSessionBindingEvent event){
		  
		  log.info("In valueBound method");
		  
		  //get a handle to the context page
		  ServletContext ctx = event.getSession().getServletContext();
		  //get a handle to the user's list ctxt attribute
		  Hashtable userList = (Hashtable)ctx.getAttribute("currentUsers");
		  
		  //add this object to the user list
		  userList.put(event.getSession().getId(),this);
		}
			 
		public void valueUnbound(HttpSessionBindingEvent event){
			
		  log.info("In valueUnbound method");
		  
		  ServletContext ctx = event.getSession().getServletContext();
		  Hashtable userList = (Hashtable)ctx.getAttribute("currentUsers");
		  //remove this object to the user list
		  userList.remove(event.getSession().getId());
		}

		public String getClientIP() {
			return clientIP;
		}

		public void setClientIP(String clientIP) {
			this.clientIP = clientIP;
		}



	  
	
	
}
