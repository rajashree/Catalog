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

public class AccountAction extends SourcenActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	private String firstName = null;
	private String lastName = null;
	private String email = null;

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
	

		
        User user=getUser();

		try {
			user = this.getUserManager().getUser(
					user.getUsername());
		} catch (NotFoundException e) {

			e.printStackTrace();
		}

		return "view";

	}

	public String input() {
		
		try {
			user = this.getUserManager().getUser(
					getUser().getUsername());
		} catch (NotFoundException e) {

			return LOGIN;

		}
		return INPUT;

	}

	public String edit() {
		
		

		try {
			user = this.getUserManager().getUser(
					getUser().getUsername());
		} catch (NotFoundException e) {

			return LOGIN;

		}
		try {
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setModified(this.getApplicationManager().getApplicationTime()
					.toString());
			user.setEmail(email);

			user = this.getUserManager().updateUser(user);
		} catch (NotFoundException e) {

			return LOGIN;
		}
		this.addActionMessage(getText("update.account.success"));

		return SUCCESS;

	}

	

	public void setUser(User user) {
		this.user = user;
	}

	public void validate() {

		/*
		 * if (getEmail() == null || getEmail().length() < 10 ) {
		 * addFieldError("email", getText("error.email.required"));
		 *  } if (getLastName() == null || getLastName().length() < 4) {
		 * addFieldError("lastname", getText("error.username.required"));
		 *  }
		 */
		super.validate();
	}
}
