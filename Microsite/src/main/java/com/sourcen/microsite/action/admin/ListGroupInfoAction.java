package com.sourcen.microsite.action.admin;

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
import java.util.Iterator;
import java.util.List;

import com.sourcen.microsite.model.Group;
import com.sourcen.microsite.service.GroupManager;

public class ListGroupInfoAction extends AdminAction {
	private GroupManager groupManager = null;
	private Iterator<Group> groupList = null;
	private int groupCount = 0;
	private String name ;


	public GroupManager getGroupManager() {
		return groupManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public String execute() {
		this.actionIndex = 4;
		List<Group> people = null;
		if (name == null)
			people = groupManager.listGroup(start,range);
		else
			people=groupManager.searchGroups(name);
		
		groupCount = people.size();
		groupList = people.iterator();
		return SUCCESS;

	}

	public Iterator<Group> getGroupList() {
		return groupList;
	}

	public int getGroupCount() {
		return groupCount;
	}

	public void setGroupCount(int groupCount) {
		this.groupCount = groupCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
