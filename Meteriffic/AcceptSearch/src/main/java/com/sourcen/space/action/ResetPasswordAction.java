package com.sourcen.space.action;

import javassist.NotFoundException;

import com.sourcen.space.model.Property;
import com.sourcen.space.model.User;


public class ResetPasswordAction extends SpaceActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String newPassword;
	private String newconformPassword;
	private String keycode;

	public String getKeycode() {
		return keycode;
	}

	public void setKeycode(String keycode) {
		this.keycode = keycode;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewconformPassword() {
		return newconformPassword;
	}

	public void setNewconformPassword(String newconformPassword) {
		this.newconformPassword = newconformPassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String input() {

		try {
			this.getUserManager().getUser(username);

			this.getApplicationManager().getProperty(username);
		

		} catch (NotFoundException e) {

			return ERROR;

		}

		return INPUT;

	}

	public String execute() {

		try {
			Property property=this.getApplicationManager().getProperty(username);
			
			if(!property.getValue().equals(this.getKeycode())){
				this.addActionError(getText("error.invalid.keycode"));		
			  return ERROR;
			}
				
		} catch (NotFoundException e1) {
			this.addActionError(getText("error.invalid.keycode"));
			e1.printStackTrace();
			return ERROR;
		}
		
		try {
			User user = this.userManager.getUser(username);

			user.setPassword(this.applicationManager.encrypt(newPassword));
			user.setModified(this.applicationManager.getApplicationTime()
					.toString());
			this.userManager.updateUser(user);
			this.applicationManager.removeProperty(username);

		} catch (NotFoundException e) {

			return ERROR;
		}
		this.addActionMessage(getText("reset.password.success"));
		return SUCCESS;

	}

	public void validate() {

		boolean flag = true;
		if (getKeycode()== null) {
			
			addFieldError("keycode", getText("invalid.keycode"));

		}

		if (getUsername() == null ) {
			addFieldError("username", getText("error.field.required"));
			
			flag = false;
		}
		if (getNewPassword() == null || getNewPassword().length() < 5) {
			addFieldError("newConformPassword", getText("error.field.required"));
			
			flag = false;
		}
		if (getNewconformPassword() == null
				|| getNewconformPassword().length() < 5) {
				
			
			addFieldError("newConformPassword", getText("error.field.required"));
			flag = false;
		}

		if (flag && !getNewconformPassword().equals(getNewPassword())) {
						
			addFieldError("password.match", getText("error.password.match"));

		}

		/*
		 * String tempKeycode=null; boolean isResponseCorrect=false;
		 * 
		 * if (this.getSession().containsKey("keycode"))
		 * 
		 * tempKeycode = (String) this.getSession().get("keycode");
		 * 
		 * try { isResponseCorrect = CaptchaServiceSingleton.getInstance()
		 * .validateResponseForID(keycode, tempKeycode); } catch
		 * (CaptchaServiceException e) { e.printStackTrace(); }
		 * 
		 * if(!isResponseCorrect){ addFieldError("keycode",
		 * getText("error.username.required")); }
		 */
		super.validate();
	}

}
