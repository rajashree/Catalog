/*
 * Copyright (C) 2007-2009 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 * 
 */
package com.sourcen.meteriffic.dao;

import java.util.List;

import com.sourcen.meteriffic.model.Role;
import com.sourcen.meteriffic.model.User;

/**
 * Created by Eclipse.
 * User : rajashreem
 * Date : Jun 9, 2009
 * Time : 4:03:28 PM
 */

public interface UserDAO {
	
	public User getUserByUserName(String userName);
	
	public User saveUser(User user);
	
	public int getNumberOfUsers();
	
	public List<User> getAllUsers();
	
	public List<Role> getRoles(String userName);
	
	public List<User> listUser(int start, int count);
	
	public void updateUser(User user);
		
}
