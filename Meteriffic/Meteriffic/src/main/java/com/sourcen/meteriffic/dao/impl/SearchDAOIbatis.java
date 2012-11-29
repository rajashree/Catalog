/*
 * Copyright (C) 2007-2009 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 * 
 */
package com.sourcen.meteriffic.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.sourcen.meteriffic.dao.SearchDAO;
import com.sourcen.meteriffic.model.Search;

/**
 * Created by Eclipse.
 * User : rajashreem
 * Date : Jun 9, 2009
 * Time : 5:58:12 PM
 */

public class SearchDAOIbatis extends SqlMapClientDaoSupport implements SearchDAO{

	public Search getSearchById(String sid) {
		return (Search)getSqlMapClientTemplate().queryForObject("getSearchById",sid);
	}

	public Search getSearchByTitle(String title) {
		return (Search)getSqlMapClientTemplate().queryForObject("getSearchByTitle",title);
	}

	@SuppressWarnings("unchecked")
	public List<Search> listSearch(String username) {
		return getSqlMapClientTemplate().queryForList("listSavedSearch",username);
	}

	@SuppressWarnings("unchecked")
	public List<Search> listSearch(String username, int start, int count) {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("userName",username);
		params.put("start",start);
		params.put("count",count);
		
		return getSqlMapClientTemplate().queryForList("listSavedSearch_limit", params);
		
			
		
	}

	public void removeSearch(int id) {
		getSqlMapClientTemplate().delete("removeSearch",id);
		
	}

	public int saveSearch(Search search) {
		return (Integer)getSqlMapClientTemplate().insert("saveSearch",search);
	}

	public int updateSearch(Search search) {
		return (Integer)getSqlMapClientTemplate().update("updateSearch",search);
	}

}
