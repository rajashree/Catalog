package com.sourcen.space.action;

import javassist.NotFoundException;

import com.sourcen.space.model.User;

public class AccountAction extends SpaceActionSupport {

	private static final long serialVersionUID = 1L;
	private User user;
	private String firstName=null;
	private String lastName=null;
	private String email=null;
	

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

	public String view() {
		tabIndex=0;
	
		this.getRequest().getRemoteUser();

		try {
			user = this.getUserManager().getUser(
					this.getRequest().getRemoteUser());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}

		return "view";

	}

	public String input() {
		tabIndex=0;
		try {
			user = this.getUserManager().getUser(
					this.getRequest().getRemoteUser());
		} catch (NotFoundException e) {

			return LOGIN;

		}
		return INPUT;

	}

	public String edit() {
		tabIndex=0;
		this.getRequest().getRemoteUser();

	
		try {
			user = this.getUserManager().getUser(
					this.getRequest().getRemoteUser());
		} catch (NotFoundException e) {
             
			return LOGIN;
		  
		}
		try {
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setModified(this.getApplicationManager().getApplicationTime().toString());
			user.setEmail(email);
			
			user = this.getUserManager().updateUser(user);
		} catch (NotFoundException e) {

			return LOGIN;
		}
		this.addActionMessage(getText("update.account.success"));
		
		return SUCCESS;

	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	public void validate() {
		
		/*if (getEmail() == null
				|| getEmail().length() < 10
						) {
			addFieldError("email", getText("error.email.required"));

		}
		if (getLastName() == null
				|| getLastName().length() < 4) {
			addFieldError("lastname", getText("error.username.required"));

		}*/
		super.validate();
	}
}
