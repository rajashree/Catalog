package com.sourcen.microsite.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.sourcen.microsite.dao.PageDAO;
import com.sourcen.microsite.model.Block;
import com.sourcen.microsite.model.Page;

public class PageDAOIbatis extends SqlMapClientDaoSupport implements PageDAO {

	public void createPage(Page page) {

		this.getSqlMapClientTemplate().insert("savePage", page);

	}

	public void removePage(String name) {
		this.getSqlMapClientTemplate().delete("deletePage", name);

	}

	public void updatePage(Page page) {
		this.getSqlMapClientTemplate().update("updatePage", page);

	}

	public Page getPage(String name) {

		return (Page) this.getSqlMapClientTemplate().queryForObject("getPage",
				name);
	}

	@SuppressWarnings("unchecked")
	public List<Page> listAllPages() {

		return getSqlMapClientTemplate().queryForList("getAllPages");
	}

	@SuppressWarnings("unchecked")
	public List<Page> getThemePages(int tid) {

		return getSqlMapClientTemplate().queryForList("getThemePages", tid);
	}

	@SuppressWarnings("unchecked")
	public List<Block> getPageBlocks(int pid) {
		return getSqlMapClientTemplate().queryForList("getPageBlocks", pid);
	}

	public void addPageblock(int pid, int bid, int pos) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pid", pid);
		params.put("bid", bid);
		params.put("pos", pos);
		this.getSqlMapClientTemplate().insert("addPageblock", params);
	}
	public int updatePageblock(int pid, int bid, int pos) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("pid", pid);
		params.put("bid", bid);
		params.put("pos", pos);
		return  this.getSqlMapClientTemplate().update("updatePageblock", params);

	}

	public void removePageBlocks(int pid) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		this.getSqlMapClientTemplate().delete("removePageBlocks", pid);

	}

	public Page getPageById(int pid) {

		return (Page) this.getSqlMapClientTemplate().queryForObject(
				"getPageById", pid);

	}

	public Object getUserSavedPage(int sid, int pid) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("sid", sid);
		params.put("pid", pid);

		return (Page) this.getSqlMapClientTemplate().queryForObject(
				"getUserSavedPage", params);

	}
	public void saveUserSitePage(int sid, int pid, String content,String created,String modified) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("sid", sid);
		params.put("pid", pid);
		params.put("content", content);	
		params.put("created", created);		
		params.put("modified", modified);		


	    this.getSqlMapClientTemplate().insert("saveUserSitePage", params);
	
	}

	public int updateUserSitePage(int sid, int pid, String content,String modified) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("sid", sid);
		params.put("pid", pid);
		params.put("content", content);	
		params.put("modified", modified);	

	  return  this.getSqlMapClientTemplate().update("updateUserSitePage", params);
	
	}

	public void cleanUserSitePages(String sid) {
		this.getSqlMapClientTemplate().delete("cleanUserSitePages", sid);

	}

	

}
