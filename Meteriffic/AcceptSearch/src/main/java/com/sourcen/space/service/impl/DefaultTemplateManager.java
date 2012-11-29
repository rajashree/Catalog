package com.sourcen.space.service.impl;

import java.util.List;

import org.acegisecurity.acls.AlreadyExistsException;
import com.sourcen.space.dao.TemplateDao;
import com.sourcen.space.model.Template;
import com.sourcen.space.service.TemplateManager;


public class DefaultTemplateManager implements TemplateManager {

	private TemplateDao templateDAO = null;

	public void createTemplate(Template template) throws AlreadyExistsException {
		
		
		Template temp = templateDAO.getTemplate(template.getName());
		if (temp != null) {
			throw new AlreadyExistsException("User already exists.");
		}
			
		
		templateDAO.createTemplate(template);
	}

	public void editTemplate(Template template) {
		// TODO Auto-generated method stub

	}

	public Template getTemplate(String name) {
		return templateDAO.getTemplate(name);
	}

	public List listTemplates() {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeTemplate(int id) {
		// TODO Auto-generated method stub

	}

	public void removeTemplate(String name) {
		// TODO Auto-generated method stub

	}

	public void init() {
		// TODO Auto-generated method stub

	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public void restart() {
		// TODO Auto-generated method stub

	}

	public void start() {
		// TODO Auto-generated method stub

	}

	public void stop() {
		// TODO Auto-generated method stub

	}

	public TemplateDao getTemplateDAO() {
		return templateDAO;
	}

	public void setTemplateDAO(TemplateDao templateDAO) {
		this.templateDAO = templateDAO;
	}

}
