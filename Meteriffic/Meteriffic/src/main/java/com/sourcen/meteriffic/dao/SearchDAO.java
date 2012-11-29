/*
 * Copyright (C) 2007-2009 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 * 
 */
package com.sourcen.meteriffic.dao;

import java.util.List;

import com.sourcen.meteriffic.model.Search;

/**
 * Created by Eclipse.
 * User : rajashreem
 * Date : Jun 9, 2009
 * Time : 4:58:43 PM
 */

public interface SearchDAO {
	
	public Search getSearchByTitle(String title);
	
	public Search getSearchById(String sid);
	
	public List<Search> listSearch(String username);
	
	public List<Search> listSearch(String username, int start, int count);
	
	
	public int saveSearch(Search search);
	
	public int updateSearch(Search search);
	
	public void removeSearch(int id);
	
	

}
