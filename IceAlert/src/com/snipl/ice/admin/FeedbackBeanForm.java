package com.snipl.ice.admin;

import org.apache.struts.action.ActionForm;

public class FeedbackBeanForm extends ActionForm {
	
	private int feedbackid;
	private int userid;
	private String username;
	private String comment;
	private int status;
	private int feedback_count;
	
	public void setFeedbackid(int feedbackid)
	{
		this.feedbackid = feedbackid;
	}
	public int getFeedbackid()
	{
		return this.feedbackid;
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
	
	public void setFeedback_count(int feedback_count)
	{
		this.feedback_count = feedback_count;
	}
	public int getFeedback_count()
	{
		return this.feedback_count;
	}
	
	public void setStatus(int status)
	{
		this.status = status;
	}
	public int getStatus()
	{
		return this.status;
	}
	
	
	
	
	public void reset()
	{
		this.comment=null;
		this.feedback_count= 0;
		this.feedbackid=0;
		this.status = 0;
		this.userid=0;
		this.username=null;
	}
	
}
