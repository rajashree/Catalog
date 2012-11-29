package com.sourcen.microsite.service.impl;

/*
 * Revision: 1.0
 * Date: October 25, 2008
 *
 * Copyright (C) 2005 - 2008 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 *
 * By : Chandra Shekher
 *
 */
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.acegisecurity.acls.AlreadyExistsException;

import com.sourcen.microsite.dao.UserDAO;
import com.sourcen.microsite.model.Role;
import com.sourcen.microsite.model.User;
import com.sourcen.microsite.service.ApplicationManager;
import com.sourcen.microsite.service.UserManager;

import javassist.NotFoundException;





public class DefaultUserManager implements UserManager,Serializable {

	private UserDAO userDAO = null;

	private ApplicationManager applicationManager = null;

	public ApplicationManager getApplicationManager() {
		return applicationManager;
	}

	public void setApplicationManager(ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public User createUser(User user) throws AlreadyExistsException,
			UnsupportedOperationException {

		User testUser = userDAO.getUserByUsername(user.getUsername());
		if (testUser != null) {
			throw new AlreadyExistsException("User already exists.");
		}

		user.setPassword(applicationManager.encrypt(user.getPassword()));
		return userDAO.saveUser(user);
	}

	public void deleteUser(User user) throws UnsupportedOperationException,
			NotFoundException {

	}

	public int getTotalUserCount() {

		return userDAO.getNumberOfUser();
	}

	public User getUser(long userId) throws NotFoundException {
		User user = userDAO.getUser(userId);
		if (user==null ||user.getUsername() == null)
			throw new NotFoundException("User "+userId+" Not Found");
		return user;
	}

	public User getUser(String userName) throws NotFoundException {

		User user = userDAO.getUserByUsername(userName);
		if (user==null ||user.getUsername() == null)
			throw new NotFoundException("User "+userName+" Not Found");
		return user;

	}

	public User updateUser(User user) throws NotFoundException {

		 user.setModified(this.applicationManager.getApplicationTime().toString());
		 userDAO.updateUser(user);
		 return user;
	}

	public void init() {

	}

	public boolean isEnabled() {

		return false;
	}

	public void start() {

	}

	public void stop() {

	}
	public List<User> listUser(int start,int count) {

		return (List<User>) userDAO.listUser( start, count);
	}
	public void restart() {

	}

	public void changePassword(String username,String newPassword) throws NotFoundException {
		User tempuser=userDAO.getUserByUsername(username);
		if (tempuser.getUsername() == null)
			throw new NotFoundException("User "+username+"Not Found");
		else
			tempuser.setPassword(applicationManager.encrypt(newPassword));
		
		this.updateUser(tempuser);
			
	}

	public List<Role> getRoles(String username)  {
		
		return userDAO.getRoles( username);
	}

	public void dummymethod() {
		System.out.println("dddddddddddddddddddddddddddddd");
	}

	public List<User> searchUser(String username) {
		// TODO Auto-generated method stub
		return userDAO.searchUser( username);
	}

	public boolean enable() {
		return true;
		// TODO Auto-generated method stub
		
	}
	


}
