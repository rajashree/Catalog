package com.sourcen.space.dao;

import java.util.List;

import com.sourcen.space.model.Template;

public interface TemplateDao {

	public void createTemplate(Template template);

	public void editTemplate(Template template);

	public Template getTemplate(String name);

	public List listTemplates();

	public void removeTemplate(int id) ;

	public void removeTemplate(String name) ;

	

}
