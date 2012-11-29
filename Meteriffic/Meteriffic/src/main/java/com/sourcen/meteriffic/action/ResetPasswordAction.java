package com.sourcen.meteriffic.action;

import com.sourcen.meteriffic.model.Property;
import com.sourcen.meteriffic.model.User;

import javassist.NotFoundException;


public class ResetPasswordAction extends SpaceActionSupport{
	private String username;
	private String newPassword;
	private String newConfirmPassword;
	private String keycode;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public String getKeycode() {
		return keycode;
	}
	public void setKeycode(String keycode) {
		this.keycode = keycode;
	}

	public String input(){
		try{
			this.getUserManager().getUser(username);
			this.getApplicationManager().getProperty(username);
		}catch(NotFoundException e){
			return ERROR;
		}
		return INPUT;
	}

	public String execute(){
		try{
			Property property=this.getApplicationManager().getProperty(username);
			if(!property.getValue().equals(this.getKeycode())){
				this.addActionError(getText("error.invalid.keycode"));
				return ERROR;
			}
		}catch(NotFoundException e){
			this.addActionError(getText("error.invalid.keycode"));
			return ERROR;
		}
		try{
			User user = this.userManager.getUser(username);
			user.setPassword(this.applicationManager.encrypt(newPassword));
			user.setModified(this.applicationManager.getApplicationTime().toString());
			this.userManager.updateUser(user);
			this.applicationManager.removeProperty(username);
		}catch(NotFoundException e){
			return ERROR;
		}
		this.addActionMessage(getText("reset.password.success"));
		return SUCCESS;
	}

	public void validate(){
		boolean flag=true;
		if (getKeycode()== null) {
			addFieldError("keycode", getText("invalid.keycode"));
		}
		if(getUsername() == null){
			addFieldError("username", getText("error.field.required"));
			flag = false;
		}
		if (getNewPassword() == null) {
			addFieldError("newConfirmPassword", getText("error.field.required"));
			flag = false;
		}
		if (getNewPassword().length() < 5) {
			addFieldError("newConfirmPassword", getText("error.password.minlength"));
			flag = false;
		}
		if (getNewConfirmPassword() == null) {
			addFieldError("newConfirmPassword", getText("error.field.required"));
			flag = false;
		}
		if (getNewConfirmPassword().length() < 5) {
			addFieldError("newConfirmPassword", getText("error.password.minlength"));
			flag = false;
		}
		if (flag && !getNewConfirmPassword().equals(getNewPassword())) {
			addFieldError("password.match", getText("error.password.match"));
		}
		super.validate();
	}


}
