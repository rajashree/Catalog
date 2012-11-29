package com.sourcen.microsite.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.sourcen.microsite.model.User;
import com.sourcen.microsite.security.AuthenticationProvider;
import com.sourcen.microsite.service.ApplicationManager;
import com.sourcen.microsite.service.EmailManager;
import com.sourcen.microsite.service.EmailTemplateManager;
import com.sourcen.microsite.service.UserManager;

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
public class SourcenActionSupport extends ActionSupport implements
		SessionAware, ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String VIEW = "view";
	public static final String NEW = "new";
	public static final String EDIT = "edit";
	public static final String DELETE = "delete";
	public static final String SAVE = "save";
	public static final String UPDATE = "update";
	public static final String SEARCH = "search";
	public static final String LIST = "list";
	protected int start = 0;
	protected int range = 15;
	
	protected int menuIndex = 0;
	protected int actionIndex = 0;
	protected Map<String, Object> session;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected String siteDirectory = "sites";

	protected EmailManager emailManager = null;
	protected EmailTemplateManager templateManager = null;
	protected UserManager userManager = null;
	protected ApplicationManager applicationManager = null;
	protected AuthenticationProvider authProvider;
	private User user;
	private static Logger log= Logger.getLogger("MicrositeActionSupport");


	public int getTabIndex() {
		return menuIndex;
	}

	public void setTabIndex(int tabIndex) {
		this.menuIndex = tabIndex;
	}

	public int getMenuIndex() {
		return menuIndex;
	}

	public void setMenuIndex(int menuIndex) {
		this.menuIndex = menuIndex;
	}

	public int getActionIndex() {
		return actionIndex;
	}

	public void setActionIndex(int actionIndex) {
		this.actionIndex = actionIndex;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

	}

	public void setServletResponse(HttpServletResponse response) {

		this.response = response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getBaseUrl() {

		String BaseUrl = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + request.getContextPath();

		return BaseUrl;
	}

	public String getSiteUrl(String sid) {

		String SiteUrl = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + request.getContextPath()
				+ "/" + getSiteDirectory() + "/" + sid;

		return SiteUrl;
	}

	public String getSiteDirectory() {
		return siteDirectory;
	}

	public void setSiteDirectory(String siteDirectory) {
		this.siteDirectory = siteDirectory;
	}

	public AuthenticationProvider getAuthProvider() {
		return authProvider;
	}

	public void setAuthProvider(AuthenticationProvider authProvider) {
		this.authProvider = authProvider;
	}

	public EmailManager getEmailManager() {
		return emailManager;
	}

	public void setEmailManager(EmailManager emailManager) {
		this.emailManager = emailManager;
	}

	public void setSession(Map arg0) {
		// TODO Auto-generated method stub
		this.session = arg0;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public ApplicationManager getApplicationManager() {
		return applicationManager;
	}

	public void setApplicationManager(ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
	}
	public final User getUser() {
        if (null == user) {
            try {
                user  = authProvider.getJiveUser();
                
            }
            catch (Exception ex) {
                   log.warn("Unable to retrieve jive user from authentication context.");
            }
        }
        return user;
    }

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}


	/**
	 * @return the templateManager
	 */
	public EmailTemplateManager getTemplateManager() {
		return templateManager;
	}

	/**
	 * @param templateManager the templateManager to set
	 */
	public void setTemplateManager(EmailTemplateManager templateManager) {
		this.templateManager = templateManager;
	}

}
