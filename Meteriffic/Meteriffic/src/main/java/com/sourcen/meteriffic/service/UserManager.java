/*
 * Copyright (C) 2007-2009 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 * 
 */
package com.sourcen.meteriffic.service;

import java.util.List;

import javassist.NotFoundException;

import com.sourcen.meteriffic.model.User;
import com.sourcen.meteriffic.model.Role;

/**
 * Created by Eclipse.
 * User : rajashreem
 * Date : Jun 9, 2009
 * Time : 8:08:31 PM
 * @param <Role>
 */

public interface UserManager extends ServiceManager{
	
	User createUser(User user)throws UserAlreadyExistsException, UnsupportedOperationException;
	
	User updateUser(User user) throws NotFoundException;
	
	User getUser(int userId) throws NotFoundException;
	
	User getUser(String userName) throws NotFoundException;
	
	List<Role> getRoles(String userName);
	
	void deleteUser(User user) throws UnsupportedOperationException, NotFoundException;
	
	List<User> getAllUsers();
	
	int getTotalUserCount();
	
	void changePassword(String username, String newPassword) throws NotFoundException;

}
