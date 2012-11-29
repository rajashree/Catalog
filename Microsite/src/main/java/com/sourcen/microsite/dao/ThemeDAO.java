package com.sourcen.microsite.dao;

import java.util.List;

import com.sourcen.microsite.model.Theme;

public interface ThemeDAO {

	public Theme getTheme(String name);

	public int createTheme(Theme theme);

	public void updateTheme(Theme theme);

	public void removeTheme(String name);

	public List<Theme> listAllTheme();

}
