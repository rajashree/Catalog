package com.sourcen.microsite.service.impl;

import java.util.List;

import javassist.NotFoundException;

import com.sourcen.microsite.dao.EmailTemplateDAO;
import com.sourcen.microsite.model.EmailTemplate;
import com.sourcen.microsite.service.EmailTemplateManager;

public class EmailTemplateManagerImpl implements EmailTemplateManager {

	
	private EmailTemplateDAO templateDAO=null;
	
	
	public void init() {
		// TODO Auto-generated method stub

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

	
	public EmailTemplate createEmailTemplate(EmailTemplate site) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public EmailTemplate getEmailTemplate(String name) throws NotFoundException {
		// TODO Auto-generated method stub
		return templateDAO.getEmailTemplate( name);
	}

	
	public List<EmailTemplate> listAllEmailTemplate() {
		// TODO Auto-generated method stub
		return templateDAO.listAllEmailTemplates();
	}

	
	public void removeEmailTemplate(String sid) throws NotFoundException {
		// TODO Auto-generated method stub
		
	}

	
	public List<EmailTemplate> searchEmailTemplates(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public int updateEmailTemplate(EmailTemplate emailTemplate) throws NotFoundException {
		return templateDAO.updateEmailTemplate(emailTemplate);
	}

	public EmailTemplateDAO getTemplateDAO() {
		return templateDAO;
	}

	public void setTemplateDAO(EmailTemplateDAO templateDAO) {
		this.templateDAO = templateDAO;
	}
	
	public boolean enable() {
		return true;
		// TODO Auto-generated method stub
		
	}

}
