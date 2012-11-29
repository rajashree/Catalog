package com.sourcen.microsite.action;

import com.sourcen.microsite.model.User;
import com.sourcen.microsite.service.RegistrationManager;

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


public class PasswordChangeAction extends SourcenActionSupport {

	/**
	 * 
	 */

	private String oldPassword;
	private String newPassword;
	private String newConformPassword;

	private static final long serialVersionUID = 1L;

	RegistrationManager registrationManager = null;

	public RegistrationManager getRegistrationManager() {
		return registrationManager;
	}

	public void setRegistrationManager(RegistrationManager registrationManager) {
		this.registrationManager = registrationManager;

	}

	public String input() {
		

		return INPUT;
	}

	public void validate() {
		if (getOldPassword() == null
				|| getOldPassword().length() < registrationManager
						.getDefaultMinUsernameLength()) {
			addFieldError("oldPassword", getText("error.field.required"));

		}
		if (getNewPassword() == null
				|| getNewPassword().length() < registrationManager
						.getDefaultMinUsernameLength()) {
			addFieldError("newConformPassword", getText("error.field.required"));

		}
		if (getNewConformPassword() == null
				|| getNewConformPassword().length() < registrationManager
						.getDefaultMinUsernameLength()) {
			addFieldError("newPassword", getText("error.field.required"));

		}

		if (!getNewPassword().equals(getNewConformPassword())) {
			addFieldError("password.match", getText("error.password.not.match"));

		}

		super.validate();
	}

	public String execute() {

		User user = null;
		try {
			user = this.userManager.getUser(this.getRequest().getRemoteUser());

		} catch (NotFoundException e1) {

			return "unauthorized";
		}
		if (!user.getPassword().equals(oldPassword)) {

			addFieldError("password.match", getText("error.password.not.match"));
			return INPUT;
		}

		
		try {
			this.userManager.changePassword(this.getRequest().getRemoteUser(),
					newPassword);
		} catch (NotFoundException e) {
			return INPUT;
		}
		this.addActionMessage(getText("change.password.success"));
		return SUCCESS;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = this.applicationManager.encrypt(oldPassword);
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewConformPassword() {
		return newConformPassword;
	}

	public void setNewConformPassword(String newConformPassword) {
		this.newConformPassword = newConformPassword;
	}

}
