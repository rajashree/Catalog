package com.sourcen.space.service.impl;

import java.util.List;

import javassist.NotFoundException;

import com.sourcen.space.action.SpaceActionSupport;
import com.sourcen.space.dao.PropertyDAO;
import com.sourcen.space.dao.SearchDAO;
import com.sourcen.space.model.Property;
import com.sourcen.space.model.Search;
import com.sourcen.space.service.ApplicationManager;
import com.sourcen.space.service.SearchManager;

public class DefaultSearchManager extends SpaceActionSupport implements
		SearchManager {

	private static final long serialVersionUID = 1L;
	private SearchDAO searchDAO = null;
	private PropertyDAO propertyDAO = null;
	private ApplicationManager applicationManager = null;

	@Override
	public void setApplicationManager(ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
	}

	public void setSearchDAO(SearchDAO searchDAO) {
		this.searchDAO = searchDAO;
	}

	public void init() {

	}

	public boolean isEnabled() {

		return true;
	}

	public void restart() {

	}

	public void start() {

	}

	public void stop() {

	}

	public void editSearch() {

	}

	@SuppressWarnings("unchecked")
	public List<Search> listUserSavedSearch(String username) {

		return searchDAO.listSearch(username);

	}

	public List<Search> listUserSavedSearch(String username, int start,
			int count) {
		return searchDAO.listSearch(username, start, count);

	}

	public boolean removeSearch(int id,String username) {
		String key = "default" + username + "search";
		try {
			this.applicationManager.deleteProperty(key);
			searchDAO.removeSearch(id);
			return true;
		} catch (NotFoundException e) {
			return false;
		}

	}

	public boolean isDefaultSearch(int id, String username){
		String key = "default" + username + "search";
		
		try {
			if(this.applicationManager.getProperty(key).getValue().equals(String.valueOf(id)))
				return true;
			else
				return false;
		} catch (NotFoundException e) {
			return false;
		}
		

    
	}
	public Search saveSearch(Search search) {
		return null;

		// return searchDAO.saveSearch(search);
	}

	public Search getSearch(String title) throws NotFoundException {
		Search search = searchDAO.getSearchByTitle(title);
		if (search == null) {
			throw new NotFoundException("Search Not exists.");
		}
		return search;

	}

	public Search getSearchs(String title) {
		Search search = searchDAO.getSearchByTitle(title);

		return search;

	}

	public Search getSavedSearch(String sid) {
		Search search = searchDAO.getSearchById(sid);

		return search;
	}

	public Search saveSearch(String title, String description, String user,
			boolean isBuzz, boolean isVolume, String[] productsIds,
			String[] featureIds, String[] sentimentIds, String[] postType,
			String orientation, boolean isPrivate) {

		Search search = new Search();
		search.setBuzz(isBuzz);
		search.setVolume(isVolume);
		search.setDescription(description);
		search.setFeatureIds(applicationManager.convertArrayToString(featureIds));
		search.setPostType(applicationManager.convertArrayToString(postType));
		search.setProductsIds(applicationManager
				.convertArrayToString(productsIds));
		search.setSentimentIds(applicationManager
				.convertArrayToString(sentimentIds));
		search.setTitle(title);

		search.setCreated(applicationManager.getApplicationTime().toString());
		search.setModified(applicationManager.getApplicationTime().toString());
		search.setUser(user);
		search.setOrientation(orientation);
		search.setPrivate(isPrivate);
		int id=searchDAO.saveSearch(search);
		search.setId(id);
		return search;

	}

	public Search updateSearch(int id,String title, String description,String user, boolean isBuzz,boolean isVolume, String[] productsIds, String[] featureIds,
			String[] sentimentIds, String[] postType,String orientation, boolean isPrivate){
				Search search = new Search();
				search.setId(id);
				search.setBuzz(isBuzz);
				search.setVolume(isVolume);
				search.setDescription(description);
				search.setFeatureIds(applicationManager
						.convertArrayToString(featureIds));
				search.setPostType(applicationManager.convertArrayToString(postType));
				search.setProductsIds(applicationManager
						.convertArrayToString(productsIds));
				search.setSentimentIds(applicationManager
						.convertArrayToString(sentimentIds));
				search.setTitle(title);
				search.setModified(applicationManager.getApplicationTime().toString());
				search.setUser(user);
				search.setOrientation(orientation);
				search.setPrivate(isPrivate);
				searchDAO.updateSearch(search);
				return search;
		
	}

	
	public void setUserDefaultSearch(String username, String sid) {
	
		String key = "default"+username+"search";
		try {
			this.applicationManager.getProperty(key);
			this.applicationManager.updateProperty(new Property(key, sid));
		} catch (NotFoundException e) {

			this.applicationManager.saveProperty(new Property(key, sid));
		}

	}

	public Search getUserDefaultSearch(String username) throws NotFoundException  {
		String key = "default" + username + "search";
		
		String sid=	this.applicationManager.getProperty(key).getValue();
		
	   return	this.getSavedSearch(sid);
	}

	public PropertyDAO getPropertyDAO() {
		return propertyDAO;
	}

	public void setPropertyDAO(PropertyDAO propertyDAO) {
		this.propertyDAO = propertyDAO;
	}

	public SearchDAO getSearchDAO() {
		return searchDAO;
	}

	public ApplicationManager getApplicationManager() {
		return applicationManager;
	}

	public Search getDefaultSearch() {
        String key = "defaultapplicationsearch";
		
		String sid=null;
		try {
			sid = this.applicationManager.getProperty(key).getValue();
		} catch (NotFoundException e) {
		    System.err.println("Erro!! No Application Wide Default Search Found");
		
		}
		
	   return	this.getSavedSearch(sid);
	}

	public void setDefaultSearch(String sid) {
		  String key = "defaultapplicationsearch";
		 
			try {
				this.applicationManager.getProperty(key);
				this.applicationManager.updateProperty(new Property(key, sid));
			} catch (NotFoundException e) {

				this.applicationManager.saveProperty(new Property(key, sid));
			}
	}
}
