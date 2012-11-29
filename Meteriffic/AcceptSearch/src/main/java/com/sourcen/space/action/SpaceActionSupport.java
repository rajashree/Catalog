package com.sourcen.space.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.sourcen.space.security.AuthenticationProvider;
import com.sourcen.space.service.ApplicationManager;
import com.sourcen.space.service.EmailManager;
import com.sourcen.space.service.UserManager;

public class SpaceActionSupport extends ActionSupport implements SessionAware,
ServletRequestAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	
	protected UserManager userManager = null;
	protected EmailManager emailManager = null;
	protected ApplicationManager applicationManager;
	protected AuthenticationProvider authProvider;
	protected Map<String, Object> session;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	public static Logger log = Logger.getLogger(SpaceActionSupport.class);
	public static final String CANCEL = "cancel";
	public short   tabIndex =1;
	
	public String getBaseUrl(){

		String BaseUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		return BaseUrl;
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

	public void setSession(Map session) {
		this.session = session;

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

	public ApplicationManager getApplicationManager() {
		return applicationManager;
	}

	public void setApplicationManager(ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public EmailManager getEmailManager() {
		return emailManager;
	}

	public void setEmailManager(EmailManager emailManager) {
		this.emailManager = emailManager;
	}

	public AuthenticationProvider getAuthProvider() {
		return authProvider;
	}

	public void setAuthProvider(AuthenticationProvider authProvider) {
		this.authProvider = authProvider;
	}

	public short getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(short tabIndex) {
		this.tabIndex = tabIndex;
	}




}
