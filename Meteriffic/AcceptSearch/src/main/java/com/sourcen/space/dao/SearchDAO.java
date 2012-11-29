package com.sourcen.space.dao;

import java.util.List;

import com.sourcen.space.model.Search;

public interface SearchDAO {

	
	
	public Search getSearchByTitle(String title);
	
	public int saveSearch(Search search);
	
	public int updateSearch(Search search);
	
	public List<Search> listSearch(String username);

	public List<Search> listSearch(String username, int start, int count);

	public Search getSearchById(String sid);

	public void removeSearch(int id);
	
}
