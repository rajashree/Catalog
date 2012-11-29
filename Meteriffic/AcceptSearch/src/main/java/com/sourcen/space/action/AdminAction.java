package com.sourcen.space.action;

import java.util.Iterator;

import com.sourcen.space.model.User;

public class AdminAction extends SpaceActionSupport{

	/**
	 * 
	 */
	private Iterator<User> userList=null;

	private String timezoneId;

	private static final long serialVersionUID = 1L;

	public String execute() {
		this.tabIndex=3;
		return SUCCESS;

	}
	public String listUser() {
		this.tabIndex=1;
		userList= this.userManager.getAllUser().iterator();
		return "listUser";

	}

	public String getTimezoneId() {
		return timezoneId;
	}

	public void setTimezoneId(String timezoneId) {
		this.timezoneId = timezoneId;
	}
	public Iterator getUserList() {
		return userList;
	}


}
