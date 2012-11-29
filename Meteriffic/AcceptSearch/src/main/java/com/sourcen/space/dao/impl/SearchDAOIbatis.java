package com.sourcen.space.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.sourcen.space.dao.SearchDAO;
import com.sourcen.space.model.Search;



public class SearchDAOIbatis extends SqlMapClientDaoSupport implements SearchDAO{

	@SuppressWarnings("unchecked")
	public List<Search> listSearch(String username) {
		
		return this.getSqlMapClientTemplate().queryForList("listSavedSearch",username);
		
	}
	

	@SuppressWarnings("unchecked")
	public List<Search> listSearch(String username, int start,int count) {
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("username", username);
		params.put("start", start);
		params.put("count", count);
		System.out.println();
		return this.getSqlMapClientTemplate().queryForList("listSavedSearch_limit",params);
		
	}

	public int saveSearch(Search search) {
	
		return (Integer) this.getSqlMapClientTemplate().insert("saveSearch",search);
	}

	public int updateSearch(Search search) {
		
		return (Integer) this.getSqlMapClientTemplate().update("updateSearch",search);
	}
	
	public Search getSearchByTitle(String title) {
		return (Search) getSqlMapClientTemplate().queryForObject("getSearchByTitle",title);
	}


	public Search getSearchById(String sid) {
		
		return (Search) getSqlMapClientTemplate().queryForObject("getSearchById",sid);
	}


	public void removeSearch(int id) {
		
		getSqlMapClientTemplate().delete("removeSearch",id);
		
	}

	

	

}
