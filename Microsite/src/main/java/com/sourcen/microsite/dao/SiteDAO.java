package com.sourcen.microsite.dao;

import java.util.List;


import com.sourcen.microsite.model.Site;

public interface SiteDAO {

	public Site getSite(String name);

	public int createSite(Site site) ;

	public int updateSite(Site site) ;

	public void removeSite(String sid) ;

	public List<Site> listAllSite() ;

	public Site getSiteById(String id) ;

	public void savePageData(int sid, int pid, String lbid, String data);

	public int updatePageData(int sid, int pid, String lbid, String data);

	public List getAllPageData(int sid);

	public List getPageData(int sid, String pid);

	public List<Site> listUserSites(String username);

	
}
