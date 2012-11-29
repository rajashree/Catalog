package com.sourcen.space.action;

import com.sourcen.space.model.User;

import javassist.NotFoundException;

public class IndexAction extends SpaceActionSupport {

	private static final long serialVersionUID = 1L;
	//Variables used for Activation Process
	private String username;
	private String keycode;

	public String execute() {

		return SUCCESS;

	}
	
	//Activation Process - Action called from Activation email link.
	public String varifyEmail() {
		try {

			try {
				this.getUserManager().getUser(username);
			} catch (NotFoundException e) {
				return ERROR;
			}

			String tempCode = this.getApplicationManager()
					.getProperty(username).getValue();

			if (keycode.equals(tempCode)) {

				this.getApplicationManager().removeProperty(username);
				User user;
				try {
					user = getUserManager().getUser(username);
					user.setEnabled(true);
					user.setModified(this.applicationManager
							.getApplicationTime());
					this.userManager.updateUser(user);
					return LOGIN;

				} catch (NotFoundException e) {
					return ERROR;
				}

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
