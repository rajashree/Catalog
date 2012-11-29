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
import javassist.NotFoundException;

import com.sourcen.microsite.model.Group;
import com.sourcen.microsite.service.GroupManager;

public class CreateGroupAction extends AdminAction {

	private GroupManager groupManager = null;
	private String name;
	private String description;
	private String id;
	private boolean editMode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public GroupManager getGroupManager() {
		return groupManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public String execute() {
		this.actionIndex = 5;
		Group group = new Group(name, description);
		group.setModified(this.applicationManager.getApplicationTime());

		if (id == null) {
			group.setCreated(this.applicationManager.getApplicationTime());

			groupManager.createGroup(group);
		} else {
			group.setId(Integer.parseInt(id));
			try {
				groupManager.updateGroup(group);
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return SUCCESS;

	}

	public String delete() {
		this.actionIndex = 5;
		if (id != null)
			try {
				groupManager.removeGroup(id);
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return SUCCESS;

	}

	public String input() {
		this.actionIndex = 5;
		if (id != null)
			loadGroup();

		return INPUT;

	}

	private void loadGroup() {

		Group group=null;
		try {
			group = groupManager.getGroup(id);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (group != null) {
			id = Integer.toString(group.getId());
			name = group.getName();
			description = group.getDescription();
			editMode = true;
		}

	}
	public void validate() {
		if (getName() == null || getName().length() < 5) {
			addFieldError("groupname", getText("error.groupname.required"));

		}
		
		

		super.validate();
	}


	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}
}
