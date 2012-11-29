package com.sourcen.microsite.action;

import com.sourcen.microsite.model.User;

import javassist.NotFoundException;



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
public class IndexAction extends SourcenActionSupport {

	private String username;
	private String keycode;

	public String execute() {
		return SUCCESS;

	}

	public String varifyEmail() {
		User user = null;
		try {

			try {
				user = getUserManager().getUser(username);
			} catch (NotFoundException e) {
				return ERROR;
			}

			String tempCode = this.getApplicationManager()
					.getProperty(username).getValue();

			if (keycode.equals(tempCode)) {

				this.getApplicationManager().removeProperty(username);

				user.setEnabled(true);
				user.setModified(this.applicationManager.getApplicationTime());
				this.userManager.updateUser(user);

			}
		} catch (NotFoundException e) {
			return ERROR;
		}

		return SUCCESS;

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getKeycode() {
		return keycode;
	}

	public void setKeycode(String keycode) {
		this.keycode = keycode;
	}

}
