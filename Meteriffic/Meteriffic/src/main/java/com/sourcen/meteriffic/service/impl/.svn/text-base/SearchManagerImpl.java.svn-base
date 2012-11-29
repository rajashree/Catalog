package com.sourcen.meteriffic.service.impl;

import java.util.List;

import javassist.NotFoundException;

import com.sourcen.meteriffic.dao.PropertyDAO;
import com.sourcen.meteriffic.dao.SearchDAO;
import com.sourcen.meteriffic.model.Property;
import com.sourcen.meteriffic.model.Search;
import com.sourcen.meteriffic.service.ApplicationManager;
import com.sourcen.meteriffic.service.SearchManager;


public class SearchManagerImpl implements SearchManager{
	private SearchDAO searchDAO = null;
	private PropertyDAO propertyDAO = null;
	private ApplicationManager applicationManager = null;

	public Search getDefaultSearch() {
		String key = "defaultapplicationsearch";
		String sid=null;
		try{
			sid=this.applicationManager.getProperty(key).getValue();
			return this.getSavedSearch(sid);
		}catch(NotFoundException e){
			System.err.println("Error !! No Application Wide Default Search Found");
			return null;
		}
	}

	public Search getSavedSearch(String sid) throws NotFoundException {
		Search search = searchDAO.getSearchById(sid);
		return search;
	}

	public Search getSearch(String title) throws NotFoundException {
		Search search = searchDAO.getSearchByTitle(title);
		return search;
	}

	public Search getUserDefaultSearch(String username)
			throws NotFoundException {
		String key = "default" + username + "search";
		String sid=	this.applicationManager.getProperty(key).getValue();
		return	this.getSavedSearch(sid);
	}

	public boolean isDefaultSearch(int id, String username) {
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

	public boolean isApplicationDefaultSearch(int id){
		String key = "defaultapplicationsearch";
		try {
			if(this.applicationManager.getProperty(key).getValue().equals(String.valueOf(id)))
				return true;
			else
				return false;
		} catch (NotFoundException e) {
			return false;
		}
	}

	public List<Search> listUserSavedSearch(String userName) {
		return searchDAO.listSearch(userName);
	}

	public List<Search> listUserSavedSearch(String username, int start,
			int count) {
		List<Search> abc = searchDAO.listSearch(username, start, count);
		return searchDAO.listSearch(username, start, count);
	}

	public boolean removeSearch(int id, String username) {
		String key = "default" + username + "search";
		try {
			this.applicationManager.deleteProperty(key);
			searchDAO.removeSearch(id);
			return true;
		} catch (NotFoundException e) {
			return false;
		}
	}

	public Search saveSearch(String title, String description, String user,
			boolean isBuzz, boolean isVolume, String[] productIds,
			String[] featureIds, String[] sentimentIds, String[] sourceTypes,
			String startdate, String enddate, String orientation, boolean isPrivate) {
		Search search = new Search();
		search.setTitle(title);
		search.setDescription(description);
		search.setUser(user);
		search.setBuzz(isBuzz);
		search.setVolume(isVolume);
		search.setFeatureIds(applicationManager.convertArrayToString(featureIds));
		search.setProductIds(applicationManager.convertArrayToString(productIds));
		search.setSentimentIds(applicationManager.convertArrayToString(sentimentIds));
		search.setSourceTypes(applicationManager.convertArrayToString(sourceTypes));
		search.setOrientation(orientation);
		search.setStartdate(startdate);
		search.setEnddate(enddate);
		search.setCreated(applicationManager.getApplicationTime().toString());
		search.setModified(applicationManager.getApplicationTime().toString());;
		search.setPrivate(isPrivate);
		int id=searchDAO.saveSearch(search);
		search.setId(id);
		
		return search;
	}
	
	public void setDefaultSearch(String sid) {
		String key="defaultapplicationsearch";
		try{
			this.applicationManager.getProperty(key);
			this.applicationManager.updateProperty(new Property(key,sid));
		}catch(NotFoundException e){
			this.applicationManager.saveProperty(new Property(key,sid));			
		}
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

	public Search updateSearch(int id, String title, String description,
			String user, boolean isBuzz, boolean isVolume, String[] productIds, 
			String[] featureIds, String[] sentimentIds,	String[] sourceTypes, 
			String startdate, String enddate, String orientation, boolean isPrivate) {
		Search search = new Search();
		search.setId(id);
		search.setBuzz(isBuzz);
		search.setVolume(isVolume);
		search.setTitle(title);
		search.setDescription(description);
		search.setFeatureIds(applicationManager.convertArrayToString(featureIds));
		search.setSourceTypes(applicationManager.convertArrayToString(sourceTypes));
		search.setProductIds(applicationManager.convertArrayToString(productIds));
		search.setSentimentIds(applicationManager.convertArrayToString(sentimentIds));
		search.setStartdate(startdate);
		search.setEnddate(enddate);
		search.setModified(applicationManager.getApplicationTime().toString());
		search.setUser(user);
		search.setOrientation(orientation);
		search.setPrivate(isPrivate);
		searchDAO.updateSearch(search);
		return search;
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public void restart() {
		// TODO Auto-generated method stub
		
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}

	public void stop() {
		// TODO Auto-generated method stub
		
	}

	public SearchDAO getSearchDAO() {
		return searchDAO;
	}

	public void setSearchDAO(SearchDAO searchDAO) {
		this.searchDAO = searchDAO;
	}

	public PropertyDAO getPropertyDAO() {
		return propertyDAO;
	}

	public void setPropertyDAO(PropertyDAO propertyDAO) {
		this.propertyDAO = propertyDAO;
	}

	public ApplicationManager getApplicationManager() {
		return applicationManager;
	}

	public void setApplicationManager(ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
	}
	

}
