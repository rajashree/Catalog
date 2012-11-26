package com.snipl.ice.admin;

import org.apache.struts.action.ActionForm;

public class BugBeanForm extends ActionForm {
	
	private int bugid;
	private int userid;
	private String username;
	private String comment;
	private int status;
	private int bug_count;
	private String browsertype;
	private String browserversion;
	private String category;
	
	
	public void setBugid(int bugid)
	{
		this.bugid = bugid;
	}
	public int getBugid()
	{
		return this.bugid;
	}
	
	public void setUserid(int userid)
	{
		this.userid = userid;
	}
	public int getUserid()
	{
		return this.userid;
	}
	
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	public String getUsername()
	{
		return this.username;
	}
	
	public void setComment(String comment)
	{
		this.comment = comment;
	}
	public String getComment()
	{
		return this.comment;
	}
	
	public void setbug_count(int bug_count)
	{
		this.bug_count = bug_count;
	}
	public int getbug_count()
	{
		return this.bug_count;
	}
	
	public void setStatus(int status)
	{
		this.status = status;
	}
	public int getStatus()
	{
		return this.status;
	}

	public void setBrowsertype(String browsertype)
	{
		this.browsertype = browsertype;
	}
	public String getBrowsertype()
	{
		return this.browsertype;
	}
	
	public void setBrowserversion(String browserversion)
	{
		this.browserversion = browserversion;
	}
	public String getBrowserversion()
	{
		return this.browserversion;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}
	public String getCategory()
	{
		return this.category;
	}

	
	
	public void reset()
	{
		this.comment=null;
		this.bug_count= 0;
		this.bugid=0;
		this.status = 0;
		this.userid=0;
		this.username=null;
		this.browsertype = null;
		this.browserversion=null;
		this.category = null;
	}
	
}
