/*
 * Copyright (C) 2007-2009 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 * 
 */
package com.sourcen.meteriffic.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.sourcen.meteriffic.dao.UserDAO;
import com.sourcen.meteriffic.model.Role;
import com.sourcen.meteriffic.model.User;

/**
 * Created by Eclipse.
 * User : rajashreem
 * Date : Jun 9, 2009
 * Time : 4:15:38 PM
 */

public class UserDAOIbatis extends SqlMapClientDaoSupport implements UserDAO{

	@SuppressWarnings("unchecked")
	public List<User> getAllUsers() {
		return this.getSqlMapClientTemplate().queryForList("listAllUsers");
	}

	public int getNumberOfUsers() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("userCount");
	}

	@SuppressWarnings("unchecked")
	public List<Role> getRoles(String userName) {
		return (List<Role>)this.getSqlMapClientTemplate().queryForList("getRoles", userName);
	}

	public User getUserByUserName(String userName) {
		return (User) this.getSqlMapClientTemplate().queryForObject("getUserByName", userName);
	}
	
	@SuppressWarnings("unchecked")
	public List<User> listUser(int start, int count) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("start",start);
		params.put("count",count);
		return this.getSqlMapClientTemplate().queryForList("listUsers_limit",params);
	}

	public User saveUser(User user) {
		return (User) this.getSqlMapClientTemplate().insert("saveUser", user);
	}

	public void updateUser(User user) {
		this.getSqlMapClientTemplate().update("updateUser",user);
		
	}

}
