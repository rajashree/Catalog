package com.sourcen.microsite.action;

import java.util.List;

import javassist.NotFoundException;

import com.sourcen.microsite.model.Page;
import com.sourcen.microsite.model.Site;
import com.sourcen.microsite.model.Theme;
import com.sourcen.microsite.service.PageManager;
import com.sourcen.microsite.service.SiteManager;
import com.sourcen.microsite.service.ThemeManager;

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
public class SiteEditPageAction extends SourcenActionSupport {

	private String sid = null;
	private ThemeManager themeManager = null;
	private SiteManager siteManager = null;
	private PageManager pageManager = null;
	private List<Page> pages = null;

	public String execute() {

		Site site;
		try {
			site = siteManager.getSiteById(sid);
		} catch (NotFoundException e) {

			e.getMessage();
			this.addActionError(getText("update.account.success"));

			return ERROR;

		}
		Theme theme = themeManager.getTheme(site.getTheme());

		pages = pageManager.getThemePages(theme.getId());
		return "edit";

	}
	public String publish() {

		
		return SUCCESS;

	}


	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public SiteManager getSiteManager() {
		return siteManager;
	}

	public void setSiteManager(SiteManager siteManager) {
		this.siteManager = siteManager;
	}

	public PageManager getPageManager() {
		return pageManager;
	}

	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}

	public List<Page> getPages() {
		return pages;
	}

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

	public ThemeManager getThemeManager() {
		return themeManager;
	}

	public void setThemeManager(ThemeManager themeManager) {
		this.themeManager = themeManager;
	}

}
