package com.sourcen.microsite.action;

import java.util.List;

import com.opensymphony.xwork2.Preparable;
import com.sourcen.microsite.action.SourcenActionSupport;
import com.sourcen.microsite.model.Block;
import com.sourcen.microsite.model.Page;
import com.sourcen.microsite.service.BlockManager;
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
public class PageAddBlockAction extends SourcenActionSupport implements Preparable{

	private PageManager pageManager = null;
	private BlockManager blockManager = null;
	private List<Page> pages = null;
	private List<Block> blocks = null;
	private int  pid  ;
	private int  pos =0 ;
	private int  bid  ;


	public PageManager getPageManager() {
		return pageManager;
	}

	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}

	public BlockManager getBlockManager() {
		return blockManager;
	}

	public void setBlockManager(BlockManager blockManager) {
		this.blockManager = blockManager;
	}

	public String execute() {
		 pageManager.addPageblock(pid, bid, pos);
			
		this.addActionMessage(this.getText("label.page.add.block.success"));
		return  SUCCESS;

	}

	public List<Page> getPages() {
		return pages;
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
		/*
		 * if (page.getName() == null || page.getName().length() < 10) {
		 * addFieldError("name", getText("error.username.required"));
		 *  } if (page.getTitle() == null || page.getTitle().length() < 10) {
		 * addFieldError("title", getText("error.email.required"));
		 *  }
		 */

		super.validate();
	}
	public String input() {
		this.actionIndex = 2;
		blocks = blockManager.listAllBlocks();
		pages = pageManager.listAllPages();
		return INPUT;

	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}
	public void prepare() throws Exception {
		this.menuIndex = 3;
		this.actionIndex = 0;

	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getBid() {
		return bid;
	}

	public void setBid(int bid) {
		this.bid = bid;
	}

}
