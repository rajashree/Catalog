package com.sourcen.space.service;

import java.util.List;

import javassist.NotFoundException;

import com.sourcen.space.model.Search;

public interface SearchManager extends ServiceManager {

	public List<Search> listUserSavedSearch(String username);

	public Search saveSearch(Search search);

	public void editSearch();

	public boolean removeSearch(int id, String username);

	public boolean isDefaultSearch(int id, String username);

	public Search getSearch(String title) throws NotFoundException;

	public Search saveSearch(String title, String description, String user,
                             boolean isBuzz, boolean isVolume, String[] productsIds,
                             String[] featureIds, String[] sentimentIds, String[] postType,
                             String orientation, boolean isPrivate);

	public Search updateSearch(int id, String title, String description,
                               String user, boolean isBuzz, boolean isVolume,
                               String[] productsIds, String[] featureIds, String[] sentimentIds,
                               String[] postType, String orientation, boolean isPrivate);

	public Search getSavedSearch(String sid) throws NotFoundException;

	public List<Search> listUserSavedSearch(String username, int start,
                                            int count);

	public void setUserDefaultSearch(String username, String sid);

	public void setDefaultSearch(String sid);

	public Search getUserDefaultSearch(String username)
			throws NotFoundException;

	public Search getDefaultSearch();

}
