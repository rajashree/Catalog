package com.sourcen.space.action;

import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.ui.rememberme.TokenBasedRememberMeServices;

public class LogoutAction extends SpaceActionSupport {

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
