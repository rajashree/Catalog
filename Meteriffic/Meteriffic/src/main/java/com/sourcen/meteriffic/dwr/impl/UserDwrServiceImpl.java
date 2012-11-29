package com.sourcen.meteriffic.dwr.impl;

import java.util.List;

import org.acegisecurity.context.SecurityContextHolder;

import javassist.NotFoundException;

import com.sourcen.meteriffic.dwr.UserDwrService;
import com.sourcen.meteriffic.model.Product;
import com.sourcen.meteriffic.model.Search;
import com.sourcen.meteriffic.service.ApplicationManager;
import com.sourcen.meteriffic.service.SearchManager;
import com.sourcen.meteriffic.service.SerendioManager;
import com.sourcen.meteriffic.service.UserManager;

public class UserDwrServiceImpl implements UserDwrService {

	protected SerendioManager serendioManager = null;
	protected UserManager userManager = null;
	protected ApplicationManager applicationManager = null;
	protected SearchManager searchManager = null;

	public List<Product> getFeatureList(int pid) {
		return serendioManager.getFeatureList(pid);
	}

	
	public List<Product>  getAllFeatureList() {
		return serendioManager.getAllFeatureList();
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

	public String getAllSnippets(String postId){
		return serendioManager.getAllSnippets(postId);
	}
	
	public String getPostData(boolean isBuzz, String productIds,
			String featureIds, String[] sentimentIds, String[] postType,
			int count, int start) {
		return serendioManager.getPostData(isBuzz,productIds, featureIds,
				sentimentIds, postType, count, start);
	}

	public List<Product> getProductList(int pid) {
		return serendioManager.getProductList(pid);
	}

	public String getProductListAsXML(int pid) {
		return serendioManager.getProductListAsXML(pid);
	}

	public String getReviewData(boolean isBuzz, String[] productIds,
			String[] featureIds, String[] sentimentIds, String[] postType,String startDate, String endDate, String postURL, boolean isCurated) {
		return serendioManager.getReviewData(isBuzz, productIds, featureIds,
				sentimentIds, postType, startDate, endDate, postURL, isCurated);
	}

	public Search getSavedSearch(String title) {
		Search search;
		try {
			search = searchManager.getSavedSearch(title);
			return search;
		} catch (NotFoundException e) {
			return null;
		}
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
	
	public String getStatistics() {
		return serendioManager.getStatistics();
	}

	public boolean isDefaultSearch(int sid, String username) {
		try {
			userManager.getUser(username);
			return searchManager.isDefaultSearch(sid,username);

		} catch (NotFoundException e) {
			return false;
		}
	}

	public boolean isApplicationDefaultSearch(int sid) {
		return searchManager.isApplicationDefaultSearch(sid);
	}

	public boolean isUserAvailable(String username) {
		try {
			userManager.getUser(username);
			return true;

		} catch (NotFoundException e) {
			return false;
		}
	}
	
	public boolean isAdminUser(String username) {
			List list =getUserManager().getRoles(SecurityContextHolder.getContext().getAuthentication().getName());
			if(list.size() >0)
				return true;
			else
				return false;
	}

	public List<Search> listUserSavedSearch(String username, int start,
			int count) {
		return searchManager.listUserSavedSearch(username, start, count);
	}

	public boolean markAsSpam(String pid, String url, String title,
			String date, String post, String snippet) {
		try {
			serendioManager.markAsSpam(pid,url, title, date, post, snippet);
			return true;
		}catch(Exception ex){
			return false;
		}
	}

	public boolean removeSearch(int sid, String username) {
		try {
			userManager.getUser(username);
			return searchManager.removeSearch(sid,username);


		} catch (NotFoundException e) {
			return false;
		}
	}

	public boolean saveSearch(String title, String description, String user,
			boolean isBuzz, boolean isVolume, String[] productIds,
			String[] featureIds, String[] sentimentIds, String[] sourceTypes, String startdate, String enddate,
			String orientation, boolean isPrivate, boolean isDefault, boolean isApplicationDefault) {
		Search search = null ;
		try {
			userManager.getUser(user);
		} catch (NotFoundException e1) {
			return false;
		}
		
		try {
			if(searchManager.getSearch(title) == null){
				search = searchManager.saveSearch(title, description,
						user, isBuzz, isVolume, productIds, featureIds,
						sentimentIds, sourceTypes, startdate, enddate, orientation, isPrivate);
				if (isDefault)
					this.setUserDefaultSearch(user, String.valueOf(search.getId()));
				if (isApplicationDefault)
					this.setApplicationDefaultSearch(String.valueOf(search.getId()));
				return true;
			}else
				return false;
			
		} catch (NotFoundException e) {
			return false;
		}
			
		
	}

	public boolean setUserDefaultSearch(String username, String sid) {
		searchManager.setUserDefaultSearch(username, sid);
		return true;
	}
	

	public boolean setApplicationDefaultSearch(String sid) {
		searchManager.setDefaultSearch(sid);
		return true;
	}

	
	public boolean sendFeedback(String username, String comment,
			String postUrl, String postTitle, String postDate, String product,String metricName,
			String sentimentNames, String buzzNames, String displayTypeName,
			String prodNames, String featureNames) {
		try {
			serendioManager.sendFeedback(username,comment,postUrl,postTitle,postDate,product,metricName,sentimentNames,buzzNames,displayTypeName,prodNames,featureNames);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean updateSearch(int id, String title, String description,
			String username, boolean isBuzz, boolean isVolume,
			String[] productIds, String[] featureIds, String[] sentimentIds,
			String[] sourceTypes, String startdate, String enddate, String orientation, boolean isPrivate,
			boolean isDefault, boolean isApplicationDefault) {
		try {
			userManager.getUser(username);
		} catch (NotFoundException e1) {
			return false;
		}
		try {
			searchManager.getSavedSearch(String.valueOf(id));
			Search search = searchManager.updateSearch(id,title, description,
					username, isBuzz, isVolume, productIds, featureIds,
					sentimentIds, sourceTypes, startdate, enddate, orientation, isPrivate);

			if (isDefault)
				this.setUserDefaultSearch(username, String.valueOf(search.getId()));
			if(isApplicationDefault)
				this.setApplicationDefaultSearch(String.valueOf(search.getId()));
			return true;
			
		} catch (NotFoundException e) {
			return false;
			
		}
	}

	public SerendioManager getSerendioManager() {
		return serendioManager;
	}

	public void setSerendioManager(SerendioManager serendioManager) {
		this.serendioManager = serendioManager;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public ApplicationManager getApplicationManager() {
		return applicationManager;
	}

	public void setApplicationManager(ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
	}

	public SearchManager getSearchManager() {
		return searchManager;
	}

	public void setSearchManager(SearchManager searchManager) {
		this.searchManager = searchManager;
	}

		
}
