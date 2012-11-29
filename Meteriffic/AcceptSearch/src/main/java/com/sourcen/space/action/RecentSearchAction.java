package com.sourcen.space.action;


import java.util.List;


import com.sourcen.space.model.Search;
import com.sourcen.space.service.SearchManager;

public class RecentSearchAction extends SpaceActionSupport {

	
	private SearchManager searchManager=null;
	
	private List<Search> savedSearch=null;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String execute() {
		
				
	  savedSearch=searchManager.listUserSavedSearch(this.getRequest().getRemoteUser(),0,5);
		
		return "list";
		
	}
	

	public void setSearchManager(SearchManager searchManager) {
		this.searchManager = searchManager;
	}


	public List<Search> getSavedSearch() {
		return savedSearch;
	}


	public void setSavedSearch(List<Search> savedSearch) {
		this.savedSearch = savedSearch;
	}


}
