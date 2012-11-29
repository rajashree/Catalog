package com.sourcen.microsite.dao;

import java.util.List;

import com.sourcen.microsite.model.Block;
import com.sourcen.microsite.model.Page;

public interface PageDAO {
	public Page getPage(String name);

	public void createPage(Page page);

	public void updatePage(Page page);

	public void removePage(String name);

	public List<Page> listAllPages();

	public List<Page> getThemePages(int tid);

	public List<Block> getPageBlocks(int pid);

	public void addPageblock(int pid, int bid, int pos);

	public void removePageBlocks(int pid);

	public Page getPageById(int pid);

	public Object getUserSavedPage(int sid, int pid);
	public int updateUserSitePage(int sid, int pid, String content,
                                  String modified);

	public void saveUserSitePage(int sid, int pid, String content,
                                 String created, String modified);

	public int updatePageblock(int pid, int bid, int pos);

	public void cleanUserSitePages(String sid);


}
