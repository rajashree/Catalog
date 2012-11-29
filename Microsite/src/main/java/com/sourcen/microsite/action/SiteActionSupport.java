package com.sourcen.microsite.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javassist.NotFoundException;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Preparable;
import com.sourcen.microsite.model.Site;
import com.sourcen.microsite.service.PageManager;
import com.sourcen.microsite.service.SiteManager;

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
public class SiteActionSupport extends SourcenActionSupport implements
		Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SiteManager siteManager = null;
	private PageManager pageManager = null;
	private List<Site> sites = null;
	protected Site site = null;
	protected String sid = null;
	private int length = 0;

	public String input() {

		this.actionIndex = 1;
		site = new Site();
		return INPUT;

	}

	public String edit() {
		this.actionIndex = 1;
		try {
			site = siteManager.getSiteById(sid);

		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.addActionError(getText("update.account.success"));

			return ERROR;
		}
		return "update";

	}

	public String remove() {

		try {
			siteManager.removeSite(sid);
		} catch (NotFoundException e) {

			e.printStackTrace();
             this.addActionError(this.getText("site.not.found.error"));
			return ERROR;
		}
		sites = siteManager.listAllSites();
		return "list";

	}
	
	private void siteCleanUp(String sid){
		
		
			String sitePath = ServletActionContext.getServletContext()
					.getRealPath("sites")
					+ "/" + sid;
			File file = new File(sitePath);

			try {
				
				this.pageManager.cleanUserSitePages(sid);
				if (file.isDirectory()) {
					org.apache.commons.io.FileUtils.deleteDirectory(file);
				} else {
					if (!file.delete())
						throw new Exception();
				}
			} catch (Throwable e) {
				this.addActionError(this.getText("site.cleanup.failed.error"));
				e.printStackTrace();
			}
		
		
	}

	

	public String execute() {
		sites = siteManager.listUserSites(getUser().getUsername());
		return "list";

	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public SiteManager getSiteManager() {
		return siteManager;
	}

	public void setSiteManager(SiteManager siteManager) {
		this.siteManager = siteManager;
	}

	public List<Site> getSites() {
		return sites;
	}

	public void setSites(List<Site> sites) {
		this.sites = sites;
	}

	public void prepare() throws Exception {
		this.menuIndex = 1;
		this.actionIndex = 0;

	}

	public InputStream getInputStream() throws Exception {
		String path = ServletActionContext.getServletContext().getRealPath(
				"/sites");
		length = path.length()+1;
		path += "/" + sid;
		System.out.println(path);
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(out);
			zipDir(path, zos);
			zos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ByteArrayInputStream(out.toByteArray());
	}

	public String download() {

		try {
			site = this.siteManager.getSiteById(sid);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return ERROR;
		}
		return "download";
	}

	public void zipDir(String dir2zip, ZipOutputStream zos) {
		try {
			// create a new File object based on the directory we

			File zipDir = new File(dir2zip);
			// get a listing of the directory content
			String[] dirList = zipDir.list();
			byte[] readBuffer = new byte[2156];
			int bytesIn = 0;
			// loop through dirList, and zip the files
			for (int i = 0; i < dirList.length; i++) {
				File f = new File(zipDir, dirList[i]);
				if (f.isDirectory()) {
					// if the File object is a directory, call this
					// function again to add its content recursively
					String filePath = f.getPath();

					zipDir(filePath, zos);
					// loop again
					continue;
				}
				// if we reached here, the File object f was not

				// create a FileInputStream on top of f
				FileInputStream fis = new FileInputStream(f);

				String path = f.getPath();
				path = path.substring(length);
				// System.out.println(path);
				ZipEntry anEntry = new ZipEntry(path);
				// place the zip entry in the ZipOutputStream object
				zos.putNextEntry(anEntry);
				// now write the content of the file to the ZipOutputStream
				while ((bytesIn = fis.read(readBuffer)) != -1) {
					zos.write(readBuffer, 0, bytesIn);
				}
				// close the Stream
				fis.close();
			}
		} catch (Exception e) {
			// handle exception
		}
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public PageManager getPageManager() {
		return pageManager;
	}

	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}
}
