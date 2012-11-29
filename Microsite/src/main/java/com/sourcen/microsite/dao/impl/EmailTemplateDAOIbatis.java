package com.sourcen.microsite.dao.impl;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.sourcen.microsite.dao.EmailTemplateDAO;
import com.sourcen.microsite.model.EmailTemplate;


public class EmailTemplateDAOIbatis extends SqlMapClientDaoSupport implements EmailTemplateDAO{

	
	public void createEmailTemplate(EmailTemplate emailTemplate) {
		  getSqlMapClientTemplate().insert("saveEmailTemplate",emailTemplate);

		
	}

	
	public EmailTemplate getEmailTemplate(String key) {
		return (EmailTemplate) getSqlMapClientTemplate().queryForObject("getEmailTemplate",key);

	}

	
	
	public List<EmailTemplate> listAllEmailTemplates() {
		return getSqlMapClientTemplate().queryForList("listAllEmailTemplate");

	}

	
	public int removeEmailTemplate(String key) {
		return this.getSqlMapClientTemplate().delete("deleteEmailTemplate", key);

	}

	
	public int updateEmailTemplate(EmailTemplate emailTemplate) {
		 return getSqlMapClientTemplate().update("updateEmailTemplate",emailTemplate);
	}

}
