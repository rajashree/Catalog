package com.sourcen.microsite.service;

import java.util.List;

import javassist.NotFoundException;

import com.sourcen.microsite.model.EmailTemplate;

public interface EmailTemplateManager extends ServiceManager {

	public EmailTemplate getEmailTemplate(String name) throws NotFoundException;

	public EmailTemplate createEmailTemplate(EmailTemplate site);

	public int updateEmailTemplate(EmailTemplate site) throws NotFoundException;

	public void removeEmailTemplate(String sid) throws NotFoundException;

	public List<EmailTemplate> listAllEmailTemplate();

	public List<EmailTemplate> searchEmailTemplates(String name);


}
