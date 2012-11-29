package com.sourcen.microsite.dao.impl;

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
import java.util.HashMap;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.sourcen.microsite.dao.UserDAO;
import com.sourcen.microsite.model.Page;
import com.sourcen.microsite.model.Role;
import com.sourcen.microsite.model.User;







public class UserDAOIbatis extends SqlMapClientDaoSupport implements UserDAO {

	

	public int getNumberOfUser() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"UserCount");
	}

	public User getUser(long uid) {
		return (User) getSqlMapClientTemplate().queryForObject("getUser",uid);

	}

	public User getUserByUsername(String username) {
		return (User) getSqlMapClientTemplate().queryForObject("getUserByName",username);
	}

	public User saveUser(User user) {
		return (User) getSqlMapClientTemplate().insert("saveUser",user);
	}

	public void updateUser(User user) {
	
		getSqlMapClientTemplate().update("updateUser",user);
	}

	@SuppressWarnings("unchecked")
	public List<Role> getRoles(String username) {
		// TODO Auto-generated method stub
		return (List<Role>) getSqlMapClientTemplate().queryForList("getRoles",username);

	}

	public List<User> listUser(int start, int range) {
		HashMap<String, Object> params=new HashMap<String, Object>();

		return this.getSqlMapClientTemplate().queryForList("listUser",start,range);
		
	}
	

	public List<User> searchUser(String username) {
		
		return this.getSqlMapClientTemplate().queryForList("searchUser","%"+username+"%");

	}

	

}
