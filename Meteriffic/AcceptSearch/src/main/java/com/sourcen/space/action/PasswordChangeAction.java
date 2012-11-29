package com.sourcen.space.action;

import javassist.NotFoundException;

import com.sourcen.space.model.User;
import com.sourcen.space.service.RegistrationManager;


public class PasswordChangeAction extends SpaceActionSupport {

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
		tabIndex = 0;

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

		tabIndex = 0;
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
