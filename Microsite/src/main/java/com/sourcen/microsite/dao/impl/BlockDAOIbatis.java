package com.sourcen.microsite.dao.impl;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.sourcen.microsite.dao.BlockDAO;
import com.sourcen.microsite.model.Block;

public class BlockDAOIbatis extends SqlMapClientDaoSupport implements BlockDAO {

	public void createBlock(Block block) {

		this.getSqlMapClientTemplate().insert("saveBlock", block);

	}

	public void removeBlock(String name) {
		this.getSqlMapClientTemplate().delete("deleteBlock", name);

	}

	public void updateBlock(Block block) {
		this.getSqlMapClientTemplate().update("updateBlock", block);

	}

	public Block getBlock(String name) {
		// TODO Auto-generated method stub
		return (Block) this.getSqlMapClientTemplate().queryForObject("getBlock", name);
	}

	@SuppressWarnings("unchecked")
	public List<Block> listAllBlocks() {
		// TODO Auto-generated method stub
		return getSqlMapClientTemplate().queryForList("getAllBlocks");
	}

}
