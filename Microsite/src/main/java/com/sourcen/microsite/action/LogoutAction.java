package com.sourcen.microsite.action;

import javax.servlet.http.Cookie;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.ui.rememberme.TokenBasedRememberMeServices;

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
public class LogoutAction extends SourcenActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String execute() {

		SecurityContextHolder.clearContext();

		// Remove all session data

		this.getSession().clear();

		// Remove the cookie
		Cookie terminate = new Cookie(
				TokenBasedRememberMeServices.ACEGI_SECURITY_HASHED_REMEMBER_ME_COOKIE_KEY,
				null);
		terminate.setMaxAge(-1);
		terminate.setPath(request.getContextPath() + "/");
		response.addCookie(terminate);
		return SUCCESS;
	}

}
