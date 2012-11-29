package com.sourcen.microsite.action;

import java.util.List;

import com.sourcen.microsite.model.Block;
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
public class BlockAction extends SourcenActionSupport {

	/**
	 * 
	 */
	private Block block=null;
	private List<Block> blocks=null;
	
	private BlockManager blockManager=null;
	
	private static final long serialVersionUID = 1L;

	
	public String input(){
		this.actionIndex=1;
		block = new Block();
		return INPUT;
	}
	
	public String edit(){
		this.actionIndex=1;
		block = blockManager.getBlock(block.getName());
		return "update";
	}
	
	public String execute() {
		
		
		blocks =blockManager.listAllBlocks();
			
			
			return "list";

	}
	public String create() {
		
		blockManager.createBlock(block);
			return "list";

	}
 
	public String update() {
		
		blockManager.updateBlock(block);
		blocks=blockManager.listAllBlocks();
		return "list";

	}
    public String remove() {
		blockManager.removeBlock(block.getName());
		blocks=blockManager.listAllBlocks();
		return "list";

	}

	public void setBlockManager(BlockManager blockManager) {
		this.blockManager = blockManager;
	}

	public Block getBlock() {
		return block;
	}

	public List<Block> getBlocks() {
		return blocks;
	}

	public void setBlock(Block block) {
		this.block = block;
	}
	public void prepare() throws Exception {
		this.menuIndex=0;
		this.actionIndex=0;
		
			
		}

	

}
