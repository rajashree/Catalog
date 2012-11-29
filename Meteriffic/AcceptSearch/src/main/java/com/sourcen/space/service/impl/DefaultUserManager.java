package com.sourcen.space.service.impl;

import java.util.List;

import javassist.NotFoundException;


import com.sourcen.space.dao.UserDAO;
import com.sourcen.space.model.Role;
import com.sourcen.space.model.User;
import com.sourcen.space.service.ApplicationManager;
import com.sourcen.space.service.UserAlreadyExistsException;
import com.sourcen.space.service.UserManager;


public class DefaultUserManager implements UserManager {

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

	public User createUser(User user) throws UserAlreadyExistsException,
			UnsupportedOperationException {

		User testUser = userDAO.getUserByUsername(user.getUsername());
		if (testUser != null) {
			throw new UserAlreadyExistsException("User already exists.");
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

		return null;
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
	public List<User> getAllUser() {

		return (List<User>) userDAO.getAllUser();
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
	


}
