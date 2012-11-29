package com.sourcen.microsite.service.impl;

import java.util.List;

import javassist.NotFoundException;

import com.sourcen.microsite.dao.SiteDAO;
import com.sourcen.microsite.model.Site;
import com.sourcen.microsite.service.SiteManager;

public class SiteManagerImpl implements SiteManager {

	private SiteDAO siteDAO = null;

	public int createSite(Site site) {
		return siteDAO.createSite(site);

	}

	public Site getSiteByName(String name) throws NotFoundException {
     Site site= siteDAO.getSite(name);

		if( site == null)
			throw new NotFoundException("Site with name " +name +"not found");
		else
			return site;
	}

	public List<Site> listAllSites() {

		return siteDAO.listAllSite();
	}

	public void removeSite(String sid) throws NotFoundException {
	
		Site site=siteDAO.getSiteById(sid);
		if(site ==null)			
			throw new NotFoundException("Site with id"+sid+"Not Found");
		siteDAO.removeSite(sid);

	}

	public void updateSite(Site site) throws NotFoundException {
		if(siteDAO.updateSite(site) <= 0)
			throw new NotFoundException("Site with id:"+site.getId()+"Not Found");
		
			

	}

	public SiteDAO getSiteDAO() {
		return siteDAO;
	}

	public void setSiteDAO(SiteDAO siteDAO) {
		this.siteDAO = siteDAO;
	}

	public Site getSiteById(String sid) throws NotFoundException {
		  Site site= siteDAO.getSiteById(sid);
		
		if( site == null)
			throw new NotFoundException("Site with Id " +sid +"not found");
		else
			return site;
	}

	public void getAllPageData(int sid) {
		siteDAO.getAllPageData(sid);

	}

	public void getPageData(int sid, String pid) {
		siteDAO.getPageData(sid, pid);

	}

	public void savePageData(int sid,int pid, String lid, String data) {
		siteDAO.savePageData(sid,pid, lid, data);

	}

	public int updatePageData(int sid,int pid,String lid,String data) {
	   return 	siteDAO.updatePageData(sid,pid, lid, data);
	}
	
	public void updateSiteData(int sid, int pid, String lid, String data) {
		
		if(updatePageData( sid, pid, lid, data) <=0)
			savePageData(sid, pid, lid, data);
		
	}

	public List<Site> listUserSites(String username) {
		return siteDAO.listUserSites( username);
	}

	

}
