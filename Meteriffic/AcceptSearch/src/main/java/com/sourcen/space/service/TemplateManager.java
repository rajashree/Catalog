package com.sourcen.space.service;

import java.util.List;

import com.sourcen.space.model.Template;

public interface TemplateManager extends ServiceManager {

	public void createTemplate(Template template);

	public void removeTemplate(int id);

	public void removeTemplate(String name);

	public void editTemplate(Template template);

	public List listTemplates();

	public Template getTemplate(String name);
}