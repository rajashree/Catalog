/*
 * Copyright (C) 2007-2009 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 * 
 */
package com.sourcen.meteriffic.service.impl;

import java.util.List;

import javassist.NotFoundException;

import com.sourcen.meteriffic.dao.UserDAO;
import com.sourcen.meteriffic.model.Role;
import com.sourcen.meteriffic.model.User;
import com.sourcen.meteriffic.service.ApplicationManager;
import com.sourcen.meteriffic.service.UserAlreadyExistsException;
import com.sourcen.meteriffic.service.UserManager;

/**
 * Created by Eclipse.
 * User : rajashreem
 * Date : Jun 10, 2009
 * Time : 12:48:27 AM
 */

public class UserManagerImpl implements UserManager{

	private UserDAO userDAO = null;	
	private ApplicationManager applicationManager = null;
	
	public void changePassword(String username, String newPassword)
			throws NotFoundException {
		User tempUser = userDAO.getUserByUserName(username);
		if(tempUser.getUserName() == null)
			throw new NotFoundException("User "+username+" Not Found");
		else
			tempUser.setPassword(applicationManager.encrypt(newPassword));
		this.updateUser(tempUser);
	}

	public User createUser(User user) throws UserAlreadyExistsException,
			UnsupportedOperationException {
		User tempUser = userDAO.getUserByUserName(user.getUserName());
		if(tempUser != null){
			throw new UserAlreadyExistsException("User already exists");
		}
		user.setPassword(applicationManager.encrypt(user.getPassword()));
		return userDAO.saveUser(user);
	}

	public void deleteUser(User user) throws UnsupportedOperationException,
			NotFoundException {
		// TODO Auto-generated method stub
		
	}

	public List<User> getAllUsers() {
		return (List<User>) userDAO.getAllUsers();
	}

	public List<Role> getRoles(String userName) {
		return userDAO.getRoles(userName);
	}

	public int getTotalUserCount() {
		return userDAO.getNumberOfUsers();
	}

	public User getUser(int userId) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public User getUser(String userName) throws NotFoundException {
		User user = userDAO.getUserByUserName(userName);
		if(user == null || user.getUserName() == null)
			throw new NotFoundException("User "+userName+" Not Found");
		return user;
	}

	public User updateUser(User user) throws NotFoundException {
		user.setModified(this.applicationManager.getApplicationTime().toString());
		userDAO.updateUser(user);
		return user;
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

	public boolean isEnabled() {
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

	/**
	 * @return the userDAO
	 */
	public UserDAO getUserDAO() {
		return userDAO;
	}

	/**
	 * @param userDAO the userDAO to set
	 */
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	/**
	 * @return the applicationManager
	 */
	public ApplicationManager getApplicationManager() {
		return applicationManager;
	}

	/**
	 * @param applicationManager the applicationManager to set
	 */
	public void setApplicationManager(ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
	}
	

}
