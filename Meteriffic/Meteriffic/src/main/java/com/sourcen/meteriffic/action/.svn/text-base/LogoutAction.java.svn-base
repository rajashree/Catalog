package com.sourcen.meteriffic.action;

import javax.servlet.http.Cookie;

import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.ui.rememberme.TokenBasedRememberMeServices;

public class LogoutAction extends SpaceActionSupport{

	public String execute(){
		SecurityContextHolder.clearContext();
		this.getSession().clear();
		return SUCCESS;
	}
}
