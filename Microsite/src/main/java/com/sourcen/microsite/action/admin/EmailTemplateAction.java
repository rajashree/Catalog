package com.sourcen.microsite.action.admin;

import java.util.Iterator;
import java.util.List;

import javassist.NotFoundException;

import com.sourcen.microsite.model.EmailTemplate;
import com.sourcen.microsite.service.EmailTemplateManager;

/*
 * Revision: 1.0
 * Date: October 25, 2008
 *
 * Copyright (C) 2005 - 2008 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 *
 * By : Chandra Shekher
 *
 */
public class EmailTemplateAction extends AdminAction {

	private String tid;
	private String name;
	private String description;
	private String subject;
	private String body;
	private EmailTemplateManager templateManager = null;
	private String created;
	private String modified;

	private Iterator<EmailTemplate> templateList = null;
	private int searchCount = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String execute() {
		this.actionIndex = 3;

		List<EmailTemplate> people = null;
		people = this.templateManager.listAllEmailTemplate();

		searchCount = people.size();
		templateList = people.iterator();
		return SUCCESS;

	}

	public String update() {
		this.actionIndex = 3;
		EmailTemplate emailTemplate=null;
		try {
			emailTemplate=templateManager.getEmailTemplate(tid);
		} catch (NotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return ERROR;
		}
				
		emailTemplate.setName(name);
		emailTemplate.setBody(body);
		emailTemplate.setSubject(subject);
		emailTemplate.setModified(this.applicationManager.getApplicationTime());

		try {
			templateManager.updateEmailTemplate(emailTemplate);
		} catch (NotFoundException e) {

			e.printStackTrace();
			return ERROR;
		}

		return SUCCESS;

	}

	public String edit() {
		this.actionIndex = 3;
		EmailTemplate emailTemplate = null;
		try {
			emailTemplate = templateManager.getEmailTemplate(tid);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR;
		}

		tid = emailTemplate.getTid();
		name = emailTemplate.getName();
		description = emailTemplate.getDescription();
		subject = emailTemplate.getSubject();
		body = emailTemplate.getBody();
		created = emailTemplate.getCreated();
		modified = emailTemplate.getModified();
		return INPUT;

	}

	public void setTemplateManager(EmailTemplateManager templateManager) {
		this.templateManager = templateManager;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public Iterator<EmailTemplate> getTemplateList() {
		return templateList;
	}

	public int getSearchCount() {
		return searchCount;
	}

}
