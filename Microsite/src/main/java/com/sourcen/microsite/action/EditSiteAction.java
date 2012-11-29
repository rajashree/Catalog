package com.sourcen.microsite.action;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javassist.NotFoundException;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import com.sourcen.microsite.model.Site;
import com.sourcen.microsite.model.Theme;
import com.sourcen.microsite.service.SiteManager;
import com.sourcen.microsite.service.ThemeManager;

public class EditSiteAction extends SourcenActionSupport {

	private static final long serialVersionUID = 1L;
	protected Site site = null;
	private SiteManager siteManager = null;
	private List<Theme> themes = null;

	private ThemeManager themeManager = null;

	public ThemeManager getThemeManager() {
		return themeManager;
	}

	public void setThemeManager(ThemeManager themeManager) {
		this.themeManager = themeManager;
	}

	public List<Theme> getThemes() {
		return themes;
	}

	public void setThemes(List<Theme> themes) {
		this.themes = themes;
	}

	public SiteManager getSiteManager() {
		return siteManager;
	}

	public void setSiteManager(SiteManager siteManager) {
		this.siteManager = siteManager;
	}

	public String input() {

		themes = themeManager.listAllThemes();
		if (site != null) {

			try {

				site= siteManager.getSiteById(Integer.toString(site.getId()));
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.addActionError(this.getText("site.edit.not.found.error"));
				return ERROR;
			}
		}

		return INPUT;

	}

	public String execute() {

		if (site.getLogo() != null) {
			String sitePath = ServletActionContext.getServletContext()
					.getRealPath("sites")
					+ "/" + site.getId();
			File file = new File(sitePath);
			if (file.mkdir()) {
				System.out.println("Site Directory Cretaed For Site--"
						+ site.getName() + file.getAbsolutePath());
			}
			File fileToCreate = new File(file.getAbsolutePath() + "/css/images/logo.jpg");
			try {
				FileUtils.copyFile(site.getLogo(), fileToCreate);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		try {
			site.setStatus(0); // edit
			site.setModified(applicationManager.getApplicationTime());

			siteManager.updateSite(site);
		
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR;
		}

		return SUCCESS;

	}

	public void validate() {
		if (site.getName() == null || site.getName().length() < 10) {
			addFieldError("name", getText("error.site.name.required"));

		}
		if (site.getTheme() == null) {
			addFieldError("theme", getText("error.site.theme.required"));

		}

		super.validate();
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

}
