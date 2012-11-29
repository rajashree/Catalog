package com.sourcen.microsite.action;

import java.io.*;
import java.util.*;

import javassist.NotFoundException;

import org.apache.struts2.ServletActionContext;

import com.sourcen.microsite.model.Page;
import com.sourcen.microsite.model.Site;
import com.sourcen.microsite.service.BlockManager;
import com.sourcen.microsite.service.PageManager;
import com.sourcen.microsite.service.SiteManager;
import com.sourcen.microsite.service.ThemeManager;

/*
 * Revision: 1.0
 * Date: October 25, 2008
 *
 * Copyright (C) 2005 - 2008 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 *
 * By : Chandra Shekher
 *
 */
public class SiteInfoPublishAction extends SourcenActionSupport {
	/**
	 * 
	 */
	private SiteManager siteManager = null;
	private PageManager pageManager = null;
	private BlockManager blockManager = null;
	private ThemeManager themeManager = null;
	private List<String> blocks = null;

	private String sid ;

	private static final long serialVersionUID = 1L;

	public String execute() {

		Site site;
		try {
			site = siteManager.getSiteById(sid);
		} catch (NotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return ERROR;
		}
		String theme = site.getTheme();

		String sitePath = ServletActionContext.getServletContext().getRealPath(
				"sites")
				+ System.getProperty("file.separator") + sid;
		File file = new File(sitePath);
		List<Page> pages = pageManager.getThemePages(1);
		Iterator<Page> pageIt = pages.iterator();
		HashMap<String, Object> properties = new HashMap<String, Object>();
		properties.put("site_id", sid);
		properties.put("base", this.getBaseUrl());
		while (pageIt.hasNext()) {
			Page page = (Page) pageIt.next();
			properties.put("pid", page.getId());
			properties.put("title", page.getTitle());
			properties.put("description", page.getDescription());
			properties.put("editable", false);
			Page temp=pageManager.getUserSavedPage(Integer.parseInt(sid),  page.getId(),properties);
			 			
			String pageHtml = temp.getContent();

			File fileToCreate = new File(file.getAbsolutePath() + "/"
					+ page.getName() + ".html");
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(
						fileToCreate));
				out.write(pageHtml);
				out.close();
			} catch (IOException e) {
				
				 this.addActionError(getText("label.site.publish.failed"));
				    
			}
		}
		site.setStatus(1); //published
		site.setModified(this.applicationManager.getApplicationTime());
		try {
			siteManager.updateSite(site);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR;
		}
      this.addActionMessage(getText("label.site.publish.success"));
      
		return SUCCESS;

	}

	public void setBlockManager(BlockManager blockManager) {
		this.blockManager = blockManager;
	}

	public BlockManager getBlockManager() {
		return blockManager;
	}

	public List<String> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<String> blocks) {
		this.blocks = blocks;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public SiteManager getSiteManager() {
		return siteManager;
	}

	public void setSiteManager(SiteManager siteManager) {
		this.siteManager = siteManager;
	}

	public PageManager getPageManager() {
		return pageManager;
	}

	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}

	public ThemeManager getThemeManager() {
		return themeManager;
	}

	public void setThemeManager(ThemeManager themeManager) {
		this.themeManager = themeManager;
	}

}
