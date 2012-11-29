package com.sourcen.microsite.action;
import java.util.List;

import com.opensymphony.xwork2.Preparable;
import com.sourcen.microsite.model.Block;
import com.sourcen.microsite.model.Page;
import com.sourcen.microsite.model.Theme;
import com.sourcen.microsite.service.BlockManager;
import com.sourcen.microsite.service.PageManager;
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

public class PageAction extends SourcenActionSupport implements Preparable {

	/**
	 * 
	 */
	private Page page = null;
	private List<Page> pages = null;
	private List<Theme> themes = null;
	private List<Block> blocks = null;

	private PageManager pageManager = null;
	private ThemeManager themeManager = null;
	private BlockManager blockManager = null;

	private static final long serialVersionUID = 1L;

	public String input() {
		this.actionIndex = 1;
		page = new Page();
		themes = themeManager.listAllThemes();
		return INPUT;
	}

	public String edit() {
		this.actionIndex = 1;
		themes = themeManager.listAllThemes();
		page = pageManager.getPage(page.getName());
		return "update";
	}

	public String execute() {

		pages = pageManager.listAllPages();

		return "list";

	}

	public String create() {

		pageManager.createPage(page);

		pages = pageManager.listAllPages();
		return "list";

	}
	

	
	public String list() {
		pages = pageManager.listAllPages();
		return "list";

	}

	public String update() {

		pageManager.updatePage(page);
		pages = pageManager.listAllPages();
		return "list";

	}

	public String remove() {
		pageManager.removePage(page.getName());
		pageManager.removePageBlocks(page.getId());
		pages = pageManager.listAllPages();
		return "list";

	}

	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}

	public Page getPage() {
		return page;
	}

	public List<Page> getPages() {
		return pages;
	}

	public void prepare() throws Exception {
		this.menuIndex = 3;
		this.actionIndex = 0;

	}

	public ThemeManager getThemeManager() {
		return themeManager;
	}

	public void setThemeManager(ThemeManager themeManager) {
		this.themeManager = themeManager;
	}

	public List<Theme> getThemes() {
		return themes;
	}

	public void setThemes(List<Theme> themes) {
		this.themes = themes;
	}

	public BlockManager getBlockManager() {
		return blockManager;
	}

	public void setBlockManager(BlockManager blockManager) {
		this.blockManager = blockManager;
	}

	public PageManager getPageManager() {
		return pageManager;
	}

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

	public List<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}

	public void validate() {
		if (page.getName() == null || page.getName().length() < 10) {
			addFieldError("name", getText("error.username.required"));

		}
		if (page.getTitle() == null || page.getTitle().length() < 10) {
			addFieldError("title", getText("error.email.required"));

		}

		super.validate();
	}

}
