package com.sourcen.meteriffic.action;

import com.sourcen.meteriffic.model.User;

import javassist.NotFoundException;

public class IndexAction extends SpaceActionSupport{
	private String username;
	private String keycode;
	
	
	public String execute(){
		tabIndex=1;
		return LOGIN;
	}
	
	public String verifyEmail(){
		System.out.println("Inside Verify Email");
		try{
			this.getUserManager().getUser(username);
		}catch(NotFoundException ex){
			return ERROR;
		}
		String tempCode;
		try {
			tempCode = this.getApplicationManager().getProperty(username).getValue();
			if(keycode.equals(tempCode)){
				this.getApplicationManager().removeProperty(username);
				User user;
				user = getUserManager().getUser(username);
				user.setEnabled(true);
				user.setModified(this.applicationManager.getApplicationTime());
				getUserManager().updateUser(user);
				this.addActionMessage(getText("activation.account.success",new String[]{this.getBaseUrl()}));
				return SUCCESS;
			}
			this.addActionMessage(getText("activation.account.success", new String[]{this.getBaseUrl()}));
			return SUCCESS;
		} catch (NotFoundException e) {
			this.addActionMessage(getText("activation.account.alreadyexists", new String[]{this.getBaseUrl()}));
			return SUCCESS;
		}
		
		
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