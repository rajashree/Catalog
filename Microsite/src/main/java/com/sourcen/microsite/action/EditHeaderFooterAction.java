package com.sourcen.microsite.action;


import com.sourcen.microsite.service.BlockManager;

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
public class EditHeaderFooterAction extends SourcenActionSupport {

	/**
	 * 
	 */
	private BlockManager blockManager=null;
	
	private static final long serialVersionUID = 1L;

	public String execute() {
		
	
		return SUCCESS;

	}

	public void setBlockManager(BlockManager blockManager) {
		this.blockManager = blockManager;
	}

	public BlockManager getBlockManager() {
		return blockManager;
	}

}
