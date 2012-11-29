package com.sourcen.space.dao.impl;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.sourcen.space.dao.TemplateDao;
import com.sourcen.space.model.Template;


public class TemplateDAOIbatis extends SqlMapClientDaoSupport implements
		TemplateDao {

	public void createTemplate(Template template) {

		getSqlMapClientTemplate().insert("saveTemplate", template);

	}

	public void editTemplate(Template template) {
		getSqlMapClientTemplate().insert("updateTemplate",template);

	}

	public Template getTemplate(String name) {
		// TODO Auto-generated method stub
		return (Template) getSqlMapClientTemplate().queryForObject(
				"getTemplateByName", name);
	}

	public List<Template> listTemplates() {

		return this.getSqlMapClientTemplate().queryForList("listTemplate");

	}

	public void removeTemplate(int id) {
		// TODO Auto-generated method stub

	}

	public void removeTemplate(String name) {
		// TODO Auto-generated method stub

	}

}
