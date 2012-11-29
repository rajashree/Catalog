package com.sourcen.dao.impl;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.sourcen.dao.UserDAO;
import com.sourcen.model.User;

public class UserDAOImpl extends SqlMapClientDaoSupport implements UserDAO {

	public User getUserByUserName(String userName) {
		return (User) this.getSqlMapClientTemplate().queryForObject("getUserByName",userName);		
	}

}
