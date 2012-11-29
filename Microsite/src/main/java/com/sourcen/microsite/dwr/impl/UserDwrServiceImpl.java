package com.sourcen.microsite.dwr.impl;

import java.util.List;

import com.sourcen.microsite.dwr.UserDwrService;
import com.sourcen.microsite.service.ApplicationManager;
import com.sourcen.microsite.service.UserManager;

import javassist.NotFoundException;


public class UserDwrServiceImpl implements UserDwrService {

	protected UserManager userManager = null;
	protected ApplicationManager applicationManager = null;
	
	

	/*
	 * public List<Search> listUserSavedSearch(String username) {
	 * 
	 * return searchManager.listUserSavedSearch(username); }
	 */

	public boolean isUserAvailable(String username) {

		try {
			userManager.getUser(username);
			return true;

		} catch (NotFoundException e) {
			return false;
		}

	}

	
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setApplicationManager(ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
	}

	

	
}
