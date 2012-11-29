package com.sourcen.microsite.service.impl;

import java.util.List;

import javassist.NotFoundException;

import com.sourcen.microsite.dao.GroupDAO;
import com.sourcen.microsite.model.Group;
import com.sourcen.microsite.model.User;
import com.sourcen.microsite.service.GroupManager;

public class GroupManagerImpl implements GroupManager {

	private GroupDAO groupDAO = null;

	
	public Group createGroup(Group site) {
		
		return groupDAO.createGroup(site);
	}

	
	public Group getGroup(String name) throws NotFoundException {
		
		Group group = groupDAO.getGroup(name);

		if (group == null)
			throw new NotFoundException("Group with ID:" + name + "Not Found!");
		return group;
	}

	
	public List<Group> listGroup(int start,int range) {
		
		return groupDAO.listGroup(start,range);
	}

	
	public void removeGroup(String sid) throws NotFoundException {
		if (groupDAO.removeGroup(sid) > 0) {
			// do nothing
		} else
			throw new NotFoundException("Group with ID:" + sid + "Not Found!");
	}

	
	public int updateGroup(Group site) throws NotFoundException {
		
		int result = groupDAO.updateGroup(site);
		if (result > 0)
			return result;
		else
			throw new NotFoundException("Group with ID:" + site.getId()
					+ "Not Found!");

	}

	public GroupDAO getGroupDAO() {
		return groupDAO;
	}

	public void setGroupDAO(GroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}



	
	public List<Group> searchGroups(String name) {
		
		return groupDAO.searchGroups(name);
	}

	
	public void addUserToGroup(String gid, String uid,String created) {
		 groupDAO.addUserToGroup(gid,uid, created);
	}

	
	public void addUsersToGroup(Group group, List<User> users) {
		
		//return groupDAO.addUsersToGroup(gid);
	}

	
	public void deleteUsersFromGroup(Group group, List<User> users) {
		
		
		
	}

	
	public List<User> getGroupUsers(String gid) {
		return groupDAO.getGroupUsers(gid);
	}

	
	public int getGroupUserCount(String gid) {
		// TODO Auto-generated method stub
		return groupDAO.getGroupUserCount(gid);
	}

	
	public List<Group> getUserGroups(String uid) {
		// TODO Auto-generated method stub
		return groupDAO.getUserGroups(uid);
	}

	
	public int  getUserGroupsCount(String uid) {
		// TODO Auto-generated method stub
		return groupDAO.getUserGroupsCount(uid);
	}
	
	
	public List<User> getUserNotInGroups(String gid) {
		return groupDAO.getUserNotInGroups(gid);
	}

	
	public int deleteUserFromGroup(String gid, String uid) {
		return groupDAO.deleteUserFromGroup(gid,uid);
	}
	
	public void init() {
		// TODO Auto-generated method stub

	}

	
	public void restart() {
		// TODO Auto-generated method stub

	}

	
	public void start() {
		// TODO Auto-generated method stub

	}

	
	public void stop() {
		// TODO Auto-generated method stub

	}

	
	public boolean enable() {
		return true;
		// TODO Auto-generated method stub
		
	}

		

}
