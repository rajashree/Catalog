package com.sourcen.microsite.action;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import com.sourcen.microsite.model.Site;
import com.sourcen.microsite.model.Theme;
import com.sourcen.microsite.service.SiteManager;
import com.sourcen.microsite.service.ThemeManager;

public class CreateSiteAction extends SourcenActionSupport {

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
		site = new Site();
		return INPUT;

	}

	public String execute() {
		this.actionIndex = 1;

		site.setUsername(getUser().getUsername());
		site.setCreated(applicationManager.getApplicationTime());
		site.setModified(applicationManager.getApplicationTime());
		
		int siteId = siteManager.createSite(site);

		String siteThemePath = ServletActionContext.getServletContext()
				.getRealPath("theme")
				+ System.getProperty("file.separator")
				+ "site"
				+ System.getProperty("file.separator") + site.getTheme();
		File siteThemePathRef = new File(siteThemePath);

	
		String publishSitePath = ServletActionContext.getServletContext()
				.getRealPath("sites")
				+ System.getProperty("file.separator") + siteId;
		File publishSitePathRef = new File(publishSitePath);

		try {
			FileUtils.copyDirectory(siteThemePathRef, publishSitePathRef);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			if (site.getLogo() != null) {
				File logoFileName = new File(publishSitePathRef
						.getAbsolutePath()
						+ "/css/images/logo.jpg");

				FileUtils.copyFile(site.getLogo(), logoFileName);

			}
		} catch (IOException e) {

			e.printStackTrace();
		}

		return SUCCESS;

	}

	public void validate() {
		if (site.getName() == null || site.getName().length() < 5) {
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
