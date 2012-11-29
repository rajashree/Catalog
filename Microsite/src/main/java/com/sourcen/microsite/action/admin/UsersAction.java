package com.sourcen.microsite.action.admin;

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
import java.util.Iterator;
import java.util.List;

import com.sourcen.microsite.model.EmailMessage;
import com.sourcen.microsite.model.User;

public class UsersAction extends AdminAction {

	private String username = null;
	private Iterator<User> userList = null;
	private int searchCount = 0;

	public String list() {
		this.menuIndex = 4;
		this.actionIndex = 6;
		List<User> people = null;
		if (username != null)
			people = this.getUserManager().searchUser(username);
		else
			people = this.userManager.listUser( start,  range);

		searchCount = people.size();
		userList = people.iterator();

		return LIST;

	}


	public Iterator<User> getUserList() {
		return userList;
	}

	public int getSearchCount() {
		return searchCount;
	}

	public void setSearchCount(int searchCount) {
		this.searchCount = searchCount;
	}
	

	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String conformPassword;
	private boolean notifyUser;


	public String getConformPassword() {
		return conformPassword;
	}

	public void setConformPassword(String conformPassword) {
		this.conformPassword = conformPassword;
	}

	@Override
	public String execute() {
		this.actionIndex = 5;
		User user = new User();
		user.setUsername(username);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setPassword(this.applicationManager.encrypt(password));

		user.setCreated(this.applicationManager.getApplicationTime());
		user.setModified(this.applicationManager.getApplicationTime());

		userManager.createUser(user);
		
       if(notifyUser){
    	   EmailMessage message= new EmailMessage();
    	   message.setSender(username, email);
    	   this.emailManager.send(message);
    	   
       }
       
		return SUCCESS;

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String input() {
		this.actionIndex = 1;
		return INPUT;

	}

	public void validate() {
		if (getUsername() == null || getUsername().length() < 5) {
			addFieldError("username", getText("error.username.required"));

		}
		if (getEmail() == null || getEmail().length() < 10) {
			addFieldError("email", getText("error.email.required"));

		}
		if (getLastName() == null || getLastName().length() < 5) {
			addFieldError("lastname", getText("error.lastname.required"));

		}
		if (getPassword() == null || getPassword().length() < 5) {
			addFieldError("password", getText("error.password.required"));

		}
		if (!getPassword().equals(getConformPassword())) {
			addFieldError("password", getText("error.password.match"));

		}
		String tempKeycode = null;

		super.validate();
	}

	public boolean isNotifyUser() {
		return notifyUser;
	}

	public void setNotifyUser(boolean notifyUser) {
		this.notifyUser = notifyUser;
	}

}
