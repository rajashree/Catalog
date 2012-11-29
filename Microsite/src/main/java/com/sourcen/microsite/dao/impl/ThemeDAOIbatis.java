package com.sourcen.microsite.dao.impl;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.sourcen.microsite.dao.ThemeDAO;
import com.sourcen.microsite.model.Theme;



public class ThemeDAOIbatis extends SqlMapClientDaoSupport implements ThemeDAO {

	public int createTheme(Theme theme) {
		return (Integer) this.getSqlMapClientTemplate().insert("saveTheme", theme);
		
	}

	public Theme getTheme(String name) {
		return (Theme) this.getSqlMapClientTemplate().queryForObject("getTheme", name);
		
	}

	public List<Theme> listAllTheme() {
		return getSqlMapClientTemplate().queryForList("getAllThemes");
		
	}

	public void removeTheme(String name) {
		this.getSqlMapClientTemplate().delete("deleteTheme", name);
		
	}

	public void updateTheme(Theme theme) {
		this.getSqlMapClientTemplate().update("updateTheme", theme);
		
	}

}
