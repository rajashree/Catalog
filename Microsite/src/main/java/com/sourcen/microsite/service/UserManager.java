package com.sourcen.microsite.service;
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
import java.util.List;

import org.acegisecurity.acls.AlreadyExistsException;

import com.sourcen.microsite.model.Role;
import com.sourcen.microsite.model.User;

import javassist.NotFoundException;

public interface UserManager extends ServiceManager {

	User createUser(User userTemplate) throws AlreadyExistsException,
			UnsupportedOperationException;

	User updateUser(User user) throws NotFoundException;

	int getTotalUserCount();

	void deleteUser(User user) throws UnsupportedOperationException,
			NotFoundException;

	User getUser(long userId) throws NotFoundException;

	List<Role> getRoles(String username);

	User getUser(String userName) throws NotFoundException;

	
	void changePassword(String username, String newPassword)
			throws NotFoundException;

	void dummymethod();

	List<User> searchUser(String username);

	List<User> listUser(int start, int range);

}
