package com.sourcen.microsite.dao;

import java.util.List;


import com.sourcen.microsite.model.Group;
import com.sourcen.microsite.model.User;

public interface GroupDAO {

	public Group getGroup(String name);

	public Group createGroup(Group site) ;

	public int updateGroup(Group site) ;

	public int removeGroup(String sid) ;

	public List<Group> searchGroups(String name);

	public List<Group> getUserGroups(String uid);

	public int getUserGroupsCount(String uid);

	public int getGroupUserCount(String gid);

	public List<User> getGroupUsers(String gid);

	public void addUserToGroup(String gid, String uid, String created);

	public int deleteUserFromGroup(String gid, String uid);

	public List<User> getUserNotInGroups(String gid);

	public List<Group> listGroup(int start, int range);

	
	
}
