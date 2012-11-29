package com.sourcen.microsite.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Preparable;
import com.sourcen.microsite.model.Theme;
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
public class ThemeAction extends SourcenActionSupport implements Preparable {

	/**
	 * 
	 */
	private Theme theme = null;
	private List<Theme> themes = null;
	private File zipFile;

	private ThemeManager themeManager = null;

	private static final long serialVersionUID = 1L;

	public String input() {
		this.actionIndex = 1;
		theme = new Theme();
		return INPUT;
	}

	public String edit() {
		this.actionIndex = 1;
		theme = themeManager.getTheme(theme.getName());
		return "update";
	}

	public String execute() {

		themes = themeManager.listAllThemes();

		return "list";

	}

	public String create() {
		if (zipFile != null)
			unZipFile();

		themeManager.createTheme(theme);

		return "list";

	}

	public String update() {

		themeManager.updateTheme(theme);
		themes = themeManager.listAllThemes();

		return "list";

	}

	public String remove() {
		themeManager.removeTheme(theme.getName());
		themes = themeManager.listAllThemes();

		return "list";

	}

	protected void unZipFile() {
		Enumeration entries = null;

		String siteThemePath = ServletActionContext.getServletContext()
				.getRealPath("theme")
				+ System.getProperty("file.separator")
				+ "site"
				+ System.getProperty("file.separator") + theme.getName();

		File siteThemePathRef = new File(siteThemePath);
		if (!siteThemePathRef.exists())
			siteThemePathRef.mkdir();
		try {
			ZipFile zipFile1 = new ZipFile(zipFile);
			entries = zipFile1.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();

				if (entry.isDirectory()) {
					System.err.println("Extracting directory: "
							+ entry.getName());

					(new File(siteThemePath + "/" + entry.getName())).mkdir();
					continue;
				}

				System.err.println("Extracting file: " + entry.getName());
				File siteThemePathRef1 = new File(siteThemePath + "/"
						+ entry.getName());

				// FileUtils.copyFile(zipFile.getInputStream(entry),
				// siteThemePathRef);

				copyInputStream(zipFile1.getInputStream(entry),
						new BufferedOutputStream(new FileOutputStream(
								siteThemePathRef1)));
			}

			zipFile1.close();
		} catch (IOException ioe) {
			System.err.println("Unhandled exception:");
			ioe.printStackTrace();
			return;
		}
	}

	public void copyInputStream(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		while ((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);

		in.close();
		out.close();
	}

	public void prepare() throws Exception {
		this.menuIndex = 2;
		this.actionIndex = 0;

	}

	public Theme getTheme() {
		return theme;
	}

	public void setTheme(Theme theme) {
		this.theme = theme;
	}

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

	public File getZipFile() {
		return zipFile;
	}

	public void setZipFile(File zipFile) {
		this.zipFile = zipFile;
	}

}
