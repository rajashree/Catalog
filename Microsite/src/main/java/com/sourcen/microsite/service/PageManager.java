package com.sourcen.microsite.service;

import java.util.HashMap;
import java.util.List;

import com.sourcen.microsite.model.Page;

public interface PageManager {

	public Page getPage(String name);

	public void createPage(Page page);

	public void updatePage(Page page);

	public void removePage(String name);

	public List<Page> listAllPages();

	public List<Page> getThemePages(int theme);

	public String joinBlocks(int pid, Object properties);

	public void addPageblock(int id, int bid, int pos);

	public void removePageBlocks(int id);

	public Page getPageById(int pid);

	public Page getUserSavedPage(int sid, int pid, HashMap<String, Object> properties);

	public void updateUserSitePage(int sid, int pid, String content,
                                   String applicationTime, String applicationTime2);

	public void cleanUserSitePages(String sid);

	
	
}
