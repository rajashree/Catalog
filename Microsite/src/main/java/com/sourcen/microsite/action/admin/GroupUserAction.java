package com.sourcen.microsite.action.admin;

import java.util.Iterator;
import java.util.List;

import javassist.NotFoundException;

import com.sourcen.microsite.model.Group;
import com.sourcen.microsite.model.User;
import com.sourcen.microsite.service.GroupManager;

public class GroupUserAction extends AdminAction {

	private String gid;
	private String uid;
	private GroupManager groupManager = null;
	private Iterator<User> memberList = null;
	private Iterator<User> nonMemberList = null;
	private int userCount = 0;
	private Group group;

	public String execute() {
		
		List<User> people = null;
		try {
			 group=groupManager.getGroup(gid);
			
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR;
		}
		
		people = groupManager.getGroupUsers(gid);
		nonMemberList=groupManager.getUserNotInGroups(gid).iterator();

		userCount = people.size();
		memberList = people.iterator();
		return SUCCESS;

	}

	public String addGroupUser() {
		try {
			Group group=groupManager.getGroup(gid);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR;
		}
		groupManager.addUserToGroup(gid, uid,applicationManager.getApplicationTime());

		return SUCCESS;

	}

	public String removeGroupUser() {
		
		try {
			Group group=groupManager.getGroup(gid);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR;
		}
		groupManager.deleteUserFromGroup(gid, uid);

		return SUCCESS;

	}

	/*
	 * public String execute() { this.actionIndex = 4; List<User> people =
	 * null;
	 * 
	 * people = groupManager.getGroupUsers(gid);
	 * 
	 * userCount = people.size(); userList = people.iterator(); return SUCCESS;
	 *  }
	 */

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public GroupManager getGroupManager() {
		return groupManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public Iterator<User> getUserList() {
		return memberList;
	}

	public void setUserList(Iterator<User> userList) {
		this.memberList = userList;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Iterator<User> getNonMemberList() {
		return nonMemberList;
	}

	public void setNonMemberList(Iterator<User> nonMemberList) {
		this.nonMemberList = nonMemberList;
	}

	public Iterator<User> getMemberList() {
		return memberList;
	}

	public void setMemberList(Iterator<User> memberList) {
		this.memberList = memberList;
	}
}
