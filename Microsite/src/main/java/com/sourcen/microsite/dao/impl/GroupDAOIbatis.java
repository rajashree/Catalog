package com.sourcen.microsite.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.sourcen.microsite.dao.GroupDAO;
import com.sourcen.microsite.model.Group;
import com.sourcen.microsite.model.User;

public class GroupDAOIbatis extends SqlMapClientDaoSupport implements GroupDAO {

	public Group createGroup(Group group) {
		return (Group) getSqlMapClientTemplate().insert("saveGroup", group);

	}

	public Group getGroup(String gid) {
		// TODO Auto-generated method stub
		return (Group) getSqlMapClientTemplate()
				.queryForObject("getGroup", gid);

	}

	public List<Group> listGroup(int start, int range) {
		// TODO Auto-generated method stub

		return getSqlMapClientTemplate().queryForList("listGroup", start, range);
	}

	public int removeGroup(String gid) {
		return this.getSqlMapClientTemplate().delete("deleteGroup", gid);

	}

	public int updateGroup(Group group) {
		// TODO Auto-generated method stub
		return getSqlMapClientTemplate().update("updateGroup", group);
	}

	public int getNumberOfGroup() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"groupCount");
	}

	public List<Group> searchGroups(String name) {

		return this.getSqlMapClientTemplate().queryForList("searchGroups",
				"%" + name + "%");

	}

	public int getGroupUserCount(String gid) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"getGroupUserCount", gid);

	}

	public List<User> getGroupUsers(String gid) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate()
				.queryForList("getGroupUsers", gid);

	}

	public List<Group> getUserGroups(String uid) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate()
				.queryForList("getUserGroups", uid);

	}

	public int getUserGroupsCount(String uid) {
		// TODO Auto-generated method stub
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"getUserGroupsCount", uid);

	}

	public void addUserToGroup(String gid, String uid,String created ) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("gid", gid);
		params.put("created", created);		
		getSqlMapClientTemplate().insert("addUserToGroup", params);

	}

	public int deleteUserFromGroup(String gid, String uid) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("gid", gid);

		return this.getSqlMapClientTemplate().delete("deleteUserFromGroup", params);

	}

	public List<User> getUserNotInGroups(String gid) {
		return this.getSqlMapClientTemplate()
		.queryForList("getUserNotInGroups", gid);

	}

}
