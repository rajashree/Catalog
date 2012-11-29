package com.sourcen.space.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.sourcen.space.dao.UserDAO;
import com.sourcen.space.model.Role;
import com.sourcen.space.model.User;





public class UserDAOIbatis extends SqlMapClientDaoSupport implements UserDAO {

	@SuppressWarnings("unchecked")
	public List<User> getAllUser() {
		return this.getSqlMapClientTemplate().queryForList("listAllUser");
	}

	public int getNumberOfUser() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"UserCount");
	}

	public User getUser(User template) {
		return null;
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

	public List<User> listUser(int start, int count) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("start", start);
		params.put("count", count);
		System.out.println();
		return this.getSqlMapClientTemplate().queryForList("listUser_limit",params);
	}

	

}
