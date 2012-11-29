package com.sourcen.microsite.action;

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
public class LoginAction extends SourcenActionSupport {

	private static final long serialVersionUID = 1L;
	protected boolean authnFailed = false;

	public String execute() {

		if (authnFailed) {
			addFieldError("login.error", getText("error.login"));
			return INPUT;
		}
		if (authProvider != null && authProvider.getAuthentication() != null
				&& authProvider.getAuthentication().isAuthenticated())
			return SUCCESS;

		return INPUT;
	}

	public boolean isAuthnFailed() {

		return authnFailed;
	}

	public void setAuthnFailed(boolean authnFailed) {
		this.authnFailed = authnFailed;
	}

}
