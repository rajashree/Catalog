/*
 * Copyright (C) 2007-2009 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 * 
 */
package com.sourcen.meteriffic.service;

import java.util.List;

import javassist.NotFoundException;

import com.sourcen.meteriffic.model.Search;

/**
 * Created by Eclipse.
 * User : rajashreem
 * Date : Jun 9, 2009
 * Time : 8:45:53 PM
 */

public interface SearchManager extends ServiceManager{
	
	public List<Search> listUserSavedSearch(String userName);

	public List<Search> listUserSavedSearch(String username, int start,
			int count);
	
	public Search saveSearch(String title, String description, String user,
			boolean isBuzz, boolean isVolume, String[] productIds, String[] featureIds,
			String[] sentimentIds, String[] sourceTypes, String startdate, String enddate, String orientation, boolean isPrivate);


	public Search updateSearch(int id, String title, String description, String user,
			boolean isBuzz, boolean isVolume, String[] productIds, String[] featureIds,
			String[] sentimentIds, String[] sourceTypes, String startdate, String enddate, String orientation, boolean isPrivate);

	public boolean isDefaultSearch(int id, String username);
	
	public boolean isApplicationDefaultSearch(int id);

	public boolean removeSearch(int id, String username);
	
	public void setUserDefaultSearch(String username, String sid);

	public void setDefaultSearch(String sid);

	public Search getSearch(String title) throws NotFoundException;

	public Search getSavedSearch(String sid) throws NotFoundException;

	public Search getUserDefaultSearch(String username)	throws NotFoundException;

	public Search getDefaultSearch();

}
