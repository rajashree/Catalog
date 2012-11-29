/*
 * Copyright (C) 2007-2009 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 * 
 */
package com.sourcen.service;

import com.sourcen.model.User;

public interface UserManager extends ServiceManager{
	
	public String getApplicationName();
	
	public User getUserByName(String username);

}
