package com.sourcen.meteriffic.action;

import java.util.Iterator;

import com.sourcen.meteriffic.model.User;

public class AdminAction extends SpaceActionSupport{

	private Iterator<User> userList = null;
	private String timezoneId;
	
	public String execute(){
		this.tabIndex = 1;
		this.tab = 1;
		return SUCCESS;
	}
	
	public String listUser(){
		userList = this.userManager.getAllUsers().iterator();
		return "listUser";
	}

	/**
	 * @return the userList
	 */
	public Iterator<User> getUserList() {
		return userList;
	}

	/**
	 * @param userList the userList to set
	 */
	public void setUserList(Iterator<User> userList) {
		this.userList = userList;
	}

	/**
	 * @return the timezoneId
	 */
	public String getTimezoneId() {
		return timezoneId;
	}

	/**
	 * @param timezoneId the timezoneId to set
	 */
	public void setTimezoneId(String timezoneId) {
		this.timezoneId = timezoneId;
	}

}