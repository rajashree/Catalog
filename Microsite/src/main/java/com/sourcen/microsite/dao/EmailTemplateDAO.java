package com.sourcen.microsite.dao;

import java.util.List;

import com.sourcen.microsite.model.EmailTemplate;

public interface EmailTemplateDAO {
	public EmailTemplate getEmailTemplate(String key);

	public void createEmailTemplate(EmailTemplate emailTemplate);

	public int updateEmailTemplate(EmailTemplate emailTemplate);

	public int removeEmailTemplate(String key);

	public List<EmailTemplate> listAllEmailTemplates();

	
}
