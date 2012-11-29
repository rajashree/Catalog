package com.sourcen.meteriffic.action;

import com.sourcen.meteriffic.model.User;

import javassist.NotFoundException;

public class PasswordChangeAction extends SpaceActionSupport{
	private String oldPassword;
	private String newPassword;
	private String newConfirmPassword;
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
	public String getNewConfirmPassword() {
		return newConfirmPassword;
	}
	public void setNewConfirmPassword(String newConfirmPassword) {
		this.newConfirmPassword = newConfirmPassword;
	}
	
	public String input(){
		tabIndex = 0;
		return INPUT;
	}
	
	public String execute(){
		User user = null;
		try{
			user =this.getUserManager().getUser(getRequest().getRemoteUser());
		}catch(NotFoundException e){
			return LOGIN;
		}
		if(!user.getPassword().equals(oldPassword)){
			addFieldError("password.match", getText("error.password.not.match"));
			return INPUT;
		}
		tabIndex = 0;
		try{
			this.userManager.changePassword(this.request.getRemoteUser(), newPassword);
		}catch(NotFoundException e){
			return INPUT;
		}
		this.addActionMessage(getText("change.password.success"));
		return SUCCESS;
	}
	
	public void validate() {
		if (getOldPassword() == null) {
			addFieldError("oldPassword", getText("error.field.required"));

		}
		if (getOldPassword().length() < 5) {
			addFieldError("oldPassword", getText("error.password.minlength"));

		}
		if (getNewPassword() == null) {
			addFieldError("newConfirmPassword", getText("error.field.required"));

		}
		if (getNewPassword().length() < registrationManager.getDefaultMinUsernameLength()) {
			addFieldError("newConfirmPassword", getText("error.password.minlength"));
		}
		
		if (getNewConfirmPassword() == null) {
			addFieldError("newPassword", getText("error.field.required"));

		}
		if (getNewConfirmPassword().length() < 5) {
			addFieldError("newPassword", getText("error.password.minlength"));

		}

		if (!getNewPassword().equals(getNewConfirmPassword())) {
			addFieldError("password.match", getText("error.password.not.match"));

		}

		super.validate();
	}
	
}
