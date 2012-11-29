package com.sourcen.microsite.dao;

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

import com.sourcen.microsite.model.Role;
import com.sourcen.microsite.model.User;



public interface UserDAO {

	public User getUser(long uid);

	public User getUserByUsername(String username);

	public int getNumberOfUser();

	public User saveUser(User user);

	public void updateUser(User user);

	public List<Role> getRoles(String username);

	public List<User> listUser(int start, int count);

	public List<User> searchUser(String username);

	
}
