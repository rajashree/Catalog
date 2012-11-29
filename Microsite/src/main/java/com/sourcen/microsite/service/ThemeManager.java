package com.sourcen.microsite.service;

import java.util.List;

import com.sourcen.microsite.model.Theme;

public interface ThemeManager {

	public Theme getTheme(String name);

	public int createTheme(Theme theme);

	public void updateTheme(Theme theme);

	public void removeTheme(String name);
	
	public List<Theme> listAllThemes();
}
