package com.sourcen.service.impl;

import com.sourcen.dao.UserDAO;
import com.sourcen.model.User;
import com.sourcen.service.UserManager;

public class UserManagerImpl implements UserManager{


	public UserDAO userDAO=null;
	

	public String getApplicationName() {
		return "Sample Application";
	}

	

	public User getUserByName(String username) {
		if(this.userDAO != null)
		return this.userDAO.getUserByUserName(username);
		else {
			return null;
			
		}
		
	}


	public void init() {
		// TODO Auto-generated method stub
		
	}



	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}



	public void restart() {
		// TODO Auto-generated method stub
		
	}



	public void start() {
		// TODO Auto-generated method stub
		
	}



	public void stop() {
		// TODO Auto-generated method stub
		
	}



	public UserDAO getUserDAO() {
		return userDAO;
	}



	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

}
