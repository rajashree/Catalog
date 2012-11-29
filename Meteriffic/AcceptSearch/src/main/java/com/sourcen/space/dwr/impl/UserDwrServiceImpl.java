package com.sourcen.space.dwr.impl;

import java.util.List;

import javassist.NotFoundException;

import com.sourcen.space.dwr.UserDwrService;
import com.sourcen.space.model.*;
import com.sourcen.space.service.*;

public class UserDwrServiceImpl implements UserDwrService {

	protected SerendioManager serendioManager = null;
	protected UserManager userManager = null;
	protected ApplicationManager applicationManager = null;
	protected SearchManager searchManager = null;

	public String getReviewData(boolean isBuzz, String[] productsIds,
			String[] featureIds, String[] sentimentIds, String[] postType) {

		return serendioManager.getReviewData(isBuzz, productsIds, featureIds,
				sentimentIds, postType);
	}

	
	
	public List<Product> getProductList(int pid) {

		return serendioManager.getProductList(pid);
	}

	public List<Product> getFeatureList(int pid) {

		return serendioManager.getFeatureList(pid);
	}

	public String getPostData(boolean isBuzz,String productsIds, String[] featureIds,
			String[] sentimentIds, String[] postType, int count, int start) {

		return serendioManager.getPostData(isBuzz,productsIds, featureIds,
				sentimentIds, postType, count, start);
	}

	public String getStatistics() {

		return serendioManager.getStatistics();
	}

	public Search getSavedSearch(String sid) {
		Search search;
		try {
			search = searchManager.getSavedSearch(sid);
			return search;
		} catch (NotFoundException e) {
			return null;
		}

	}
	
	public boolean markAsSpam(String pid,String url, String title, String date, String post, String snippet){
	try {
			serendioManager.markAsSpam(pid,url, title, date, post, snippet);
			return true;
		}catch(Exception ex){
			return false;
		}
		

	}


	public boolean saveSearch(String title, String description,
			String username, boolean isBuzz, boolean isVolume,
			String[] productsIds, String[] featureIds, String[] sentimentIds,
			String[] postType, String orientation, boolean isPrivate,
			boolean isDefault) {

		try {
			userManager.getUser(username);
		} catch (NotFoundException e1) {
			return false;
		}
		try {
			searchManager.getSearch(title);
			return false;
		} catch (NotFoundException e) {
			Search search = searchManager.saveSearch(title, description,
					username, isBuzz, isVolume, productsIds, featureIds,
					sentimentIds, postType, orientation, isPrivate);

			if (isDefault)
				this.setUserDefaultSearch(username, String.valueOf(search.getId()));
			return true;
		}

	}
	public boolean updateSearch(int id,String title, String description,
			String username, boolean isBuzz, boolean isVolume,
			String[] productsIds, String[] featureIds, String[] sentimentIds,
			String[] postType, String orientation, boolean isPrivate,
			boolean isDefault) {

		try {
			userManager.getUser(username);
		} catch (NotFoundException e1) {
			return false;
		}
		try {
			searchManager.getSavedSearch(String.valueOf(id));
			System.out.println("INSIDE UPDATION");
			Search search = searchManager.updateSearch(id,title, description,
					username, isBuzz, isVolume, productsIds, featureIds,
					sentimentIds, postType, orientation, isPrivate);

			if (isDefault)
				this.setUserDefaultSearch(username, String.valueOf(search.getId()));
			return true;
		} catch (NotFoundException e) {
			return false;
			
		}

	}
	
	public boolean removeSearch(int sid,String username){
		try {
			userManager.getUser(username);
			return searchManager.removeSearch(sid,username);

		} catch (NotFoundException e) {
			return false;
		}
		
		
		
	}

	
	public boolean isDefaultSearch(int sid, String username){
		try {
			userManager.getUser(username);
			return searchManager.isDefaultSearch(sid,username);

		} catch (NotFoundException e) {
			return false;
		}
		
	}

	public List<Search> listUserSavedSearch(String username, int start,
			int count) {
		return searchManager.listUserSavedSearch(username, start, count);
	}

	/*
	 * public List<Search> listUserSavedSearch(String username) {
	 * 
	 * return searchManager.listUserSavedSearch(username); }
	 */

	public boolean isUserAvailable(String username) {

		try {
			userManager.getUser(username);
			return true;

		} catch (NotFoundException e) {
			return false;
		}

	}

	public void setSerendioManager(SerendioManager serendioManager) {
		this.serendioManager = serendioManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setApplicationManager(ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
	}

	public void setSearchManager(SearchManager searchManager) {
		this.searchManager = searchManager;
	}

	public String getFeatureListAsXML(int fid) {
		
		return serendioManager.getFeatureListAsXML(fid);
	}
	
	public String getFilterTreeAsXML(boolean isProduct) {
		if(isProduct)
		  return serendioManager.getProductListAsXML(1);
		else
			 return serendioManager.getFeatureListAsXML(1);
	}
	public String getProductListAsXML(int pid) {
		return serendioManager.getProductListAsXML(pid);
	}

	public boolean setUserDefaultSearch(String username, String sid) {

		searchManager.setUserDefaultSearch(username, sid);

		return true;

	}

	public Search getUserDefaultSearch(String username) {

		Search search;
		try {
			search = searchManager.getUserDefaultSearch(username);
			return search;
		} catch (NotFoundException e) {
			searchManager.getDefaultSearch();
			return searchManager.getDefaultSearch();
		}

	}
	
	public boolean sendFeedback(String username,String comment, String postUrl, String postTitle, String postDate, String product, String sentimentExtraction, String metricName,String sentimentNames,String buzzNames,String displayTypeName,String prodNames,String featureNames){
	
	/*	try {
			serendioManager.sendFeedback(message);
			return true;
		} catch (Exception e) {
			return false;
		}*/
		try {
			serendioManager.sendFeedback(username,comment,postUrl,postTitle,postDate,product,sentimentExtraction,metricName,sentimentNames,buzzNames,displayTypeName,prodNames,featureNames);
			return true;
		} catch (Exception e) {
			return false;
		}
	}



	
}
