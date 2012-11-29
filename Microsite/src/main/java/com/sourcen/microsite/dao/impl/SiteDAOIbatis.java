package com.sourcen.microsite.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.sourcen.microsite.dao.SiteDAO;
import com.sourcen.microsite.model.Site;

public class SiteDAOIbatis extends SqlMapClientDaoSupport implements SiteDAO {

	public int createSite(Site site) {
		return (Integer) this.getSqlMapClientTemplate()
				.insert("saveSite", site);

	}

	public Site getSite(String name) {
		return (Site) this.getSqlMapClientTemplate().queryForObject("getSite",
				name);

	}

	public List<Site> listAllSite() {
		return getSqlMapClientTemplate().queryForList("getAllSites");

	}

	public void removeSite(String sid) {
		this.getSqlMapClientTemplate().delete("deleteSite", sid);

	}

	public int updateSite(Site site) {
		return getSqlMapClientTemplate().update("updateSite", site);

	}

	public Site getSiteById(String id) {
		return (Site) this.getSqlMapClientTemplate().queryForObject(
				"getSiteById", id);

	}

	public void savePageData(int sid,int pid, String lid, String data) {
		HashMap<String, Object> params = new HashMap<String, Object>();

		params.put("sid", sid);
		params.put("pid", pid);
		params.put("lid", lid);
		params.put("data", data);

		getSqlMapClientTemplate().insert("savePageData", params);

	}

	public int updatePageData(int sid, int pid, String lid, String data) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("sid", sid);
		params.put("pid", pid);
		params.put("lid", lid);
		params.put("data", data);

	  return  getSqlMapClientTemplate().update("updatePageData", params);

	}

	public List getAllPageData(int sid) {

		return getSqlMapClientTemplate().queryForList("getAllPageData",
				sid);

	}

	public List getPageData(int sid, String pid) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("sid", sid);
		params.put("pid", pid);

		return getSqlMapClientTemplate().queryForList("getSiteById",
				params);

	}

	public List<Site> listUserSites(String username) {
		return getSqlMapClientTemplate().queryForList("listUserSites",username);

	}

	
	
	

}
