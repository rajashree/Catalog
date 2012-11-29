package com.sourcen.dwr.impl;

import com.sourcen.dwr.UserDwrService;
import com.sourcen.model.User;
import com.sourcen.service.UserManager;

public class UserDwrServiceImpl implements UserDwrService {
	UserManager userManager = null;

	public String getApplicationVersion(boolean allow) {
			if(allow)
			return "1.0.0";
			else
			return "2.0.0";
	}
	
	public User getUser(String username){
		return this.userManager.getUserByName(username);
		
	}

	/**
	 * @return the userManager
	 */
	public UserManager getUserManager() {
		return userManager;
	}

	/**
	 * @param userManager the userManager to set
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

}
