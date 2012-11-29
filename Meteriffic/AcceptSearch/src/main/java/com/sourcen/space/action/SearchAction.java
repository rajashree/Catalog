package com.sourcen.space.action;

import java.util.Iterator;
import java.util.List;

import com.sourcen.space.service.SearchManager;

public class SearchAction extends SpaceActionSupport {

	
	private SearchManager searchManager=null;
	
	private Iterator savedSearch=null;
	
	private static final long serialVersionUID = 1L;

	public String execute() {
	this.tabIndex=2;
	  
		
		return SUCCESS;
		
	}
	
	

	public SearchManager getSearchManager() {
		return searchManager;
	}

	public void setSearchManager(SearchManager searchManager) {
		this.searchManager = searchManager;
	}

	public Iterator getSavedSearch() {
		return savedSearch;
	}

	public void setSavedSearch(Iterator savedSearch) {
		this.savedSearch = savedSearch;
	}
}
