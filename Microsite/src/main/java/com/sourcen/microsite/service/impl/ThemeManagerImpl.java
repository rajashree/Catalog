package com.sourcen.microsite.service.impl;

import java.util.List;

import com.sourcen.microsite.dao.ThemeDAO;
import com.sourcen.microsite.model.Theme;
import com.sourcen.microsite.service.ThemeManager;

public class ThemeManagerImpl implements ThemeManager{

	ThemeDAO themeDAO = null;
	
	public int createTheme(Theme theme) {
		
		return themeDAO.createTheme(theme);
	}

	public Theme getTheme(String name) {
		
		return themeDAO.getTheme(name);
	}

	public List<Theme> listAllThemes() {
		
		return themeDAO.listAllTheme();
	}

	public void removeTheme(String name) {
		themeDAO.removeTheme(name);
		
	}

	public void updateTheme(Theme theme) {
		themeDAO.updateTheme(theme);
		
	}

	public ThemeDAO getThemeDAO() {
		return themeDAO;
	}

	public void setThemeDAO(ThemeDAO themeDAO) {
		this.themeDAO = themeDAO;
	}

}
