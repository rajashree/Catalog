package com.sourcen.meteriffic.action;

import com.sourcen.meteriffic.model.User;

import javassist.NotFoundException;

public class AccountAction extends SpaceActionSupport{
	
	private User user;
	private String firstName=null;
	private String lastName=null;
	private String email=null;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String input(){
		tabIndex=0;
		try {
			user = this.getUserManager().getUser(this.getRequest().getRemoteUser());
		} catch (NotFoundException e) {
			return LOGIN;
		}
		return INPUT;
	}
	
	public String view(){
		tabIndex=0;
		try{
			user = this.getUserManager().getUser(this.getRequest().getRemoteUser());
		}catch(NotFoundException e){
			return LOGIN;			
		}
		return "view";
	}
	
	public String edit(){
		tabIndex=0;
		try{
			user = this.getUserManager().getUser(this.getRequest().getRemoteUser());
		}catch(NotFoundException e){
			return LOGIN;
		}
		try{
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setModified(this.getApplicationManager().getApplicationTime().toString());
			user.setEmail(email);
			
			user = this.getUserManager().updateUser(user);
		}catch(NotFoundException e){
			return LOGIN;
		}
		this.addActionMessage(getText("update.account.success"));
		return SUCCESS;
	}
	
}
