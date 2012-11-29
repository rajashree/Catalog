package com.sourcen.microsite.dao;

import java.util.List;

import com.sourcen.microsite.model.Block;

public interface BlockDAO {

	public Block getBlock(String name);
	public void createBlock(Block block);

	public void updateBlock(Block block);

	public void removeBlock(String name);
	public List<Block> listAllBlocks();

}
