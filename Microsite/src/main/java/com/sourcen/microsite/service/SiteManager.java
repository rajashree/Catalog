package com.sourcen.microsite.service;

import java.util.List;

import org.acegisecurity.acls.AlreadyExistsException;

import javassist.NotFoundException;

import com.sourcen.microsite.model.Site;

public interface SiteManager {

	public Site getSiteByName(String name) throws NotFoundException;

	public Site getSiteById(String id) throws NotFoundException;

	public int createSite(Site block)throws AlreadyExistsException;

	public void updateSite(Site block) throws NotFoundException;

	public void removeSite(String id) throws NotFoundException;

	public List<Site> listAllSites();

	public List<Site> listUserSites(String username);

	public void getAllPageData(int sid);

	public void getPageData(int sid, String pid);

	public int updatePageData(int sid, int pid, String lid, String data);

	public void savePageData(int sid, int pid, String bid, String data);

	public void updateSiteData(int sid, int pid, String lid, String data);

	
}
