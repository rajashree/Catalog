package com.sourcen.microsite.dwr.impl;

import com.sourcen.microsite.dwr.DwrService;
import com.sourcen.microsite.service.ApplicationManager;
import com.sourcen.microsite.service.PageManager;
import com.sourcen.microsite.service.SiteManager;

public class PageDwrServiceImpl implements DwrService {

	SiteManager siteManager = null;
	ApplicationManager applicationManager = null;
	PageManager pageManager = null;
	

	public SiteManager getSiteManager() {
		return siteManager;
	}

	public void setSiteManager(SiteManager siteManager) {
		this.siteManager = siteManager;
	}

	public void addPageBlock(int pid, int bid, int pos) {
		// TODO Auto-generated method stub

	}

	public void updateSiteData(int sid, int pid, String lid, String data) {
		siteManager.updateSiteData(sid, pid, lid, data);

	}

	public void updateUserSitePage(int sid, int pid, String content) {
		pageManager.updateUserSitePage(sid, pid, content,applicationManager.getApplicationTime(),applicationManager.getApplicationTime());

	}

	public ApplicationManager getApplicationManager() {
		return applicationManager;
	}

	public void setApplicationManager(ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
	}

	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}

}
