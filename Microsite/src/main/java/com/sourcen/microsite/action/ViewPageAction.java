package com.sourcen.microsite.action;

import java.util.HashMap;

import com.sourcen.microsite.model.Page;
import com.sourcen.microsite.service.PageManager;

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

public class ViewPageAction extends SourcenActionSupport {

	private int pid;
	private int sid;
	private String pageHTML = "";
	private PageManager pageManager = null;
	private boolean editable = false;

	public String execute() {
		HashMap<String, Object> properties = new HashMap<String, Object>();
		properties.put("site_id", sid);
		Page page = pageManager.getPageById(pid);
		
		
		properties.put("pid", pid);
		properties.put("base", this.getBaseUrl());
		properties.put("title", page.getTitle());
		properties.put("description", page.getDescription());
		properties.put("editable", editable);

		Page temp=pageManager.getUserSavedPage(sid, pid,properties);
		 
		pageHTML= temp.getContent();

		return SUCCESS;

	}

	public String save() {
		System.out.println(pageHTML);
		return SUCCESS;

	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getPageHTML() {
		return pageHTML;
	}

	public void setPageHTML(String pageHTML) {
		this.pageHTML = pageHTML;
	}

	public PageManager getPageManager() {
		return pageManager;
	}

	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

}
