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

import javassist.NotFoundException;

import com.sourcen.microsite.model.Group;
import com.sourcen.microsite.model.User;

public interface GroupManager extends ServiceManager {

	public Group getGroup(String name) throws NotFoundException;

	public Group createGroup(Group site);

	public int updateGroup(Group site) throws NotFoundException;

	public void removeGroup(String sid) throws NotFoundException;

	public List<Group> searchGroups(String name);

	// Add a User to a particular group
	void addUserToGroup(String gid, String uid, String created);

	// Add a List of Users to a particular group
	public void addUsersToGroup(Group group, List<User> users);

	// Remove a List of Users from a particular group
	public void deleteUsersFromGroup(Group group, List<User> users);

	// return all the users belongs to a particular group

	public List<User> getGroupUsers(String gid);
	
	public List<User> getUserNotInGroups(String gid);

	public List<Group> getUserGroups(String uid);

	public int getUserGroupsCount(String uid);

	public int getGroupUserCount(String gid);

	public int deleteUserFromGroup(String gid, String uid);

	public List<Group> listGroup(int start, int range);

}
