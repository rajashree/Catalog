package com.sourcen.microsite.dwr.impl;

import com.sourcen.microsite.dwr.AdminDwrService;
import com.sourcen.microsite.model.User;
import com.sourcen.microsite.service.RegistrationManager;

import javassist.NotFoundException;



public class AdminDwrServiceImpl extends UserDwrServiceImpl implements
		AdminDwrService {

	private RegistrationManager registrationManager = null;

	public boolean setDefaultLocale() {

		
		return true;
		
	}

	public boolean setDefaultTimeZone(String timezoneId) {

		this.applicationManager.setApplicationTimeZone(timezoneId);
		return true;
	}

	public boolean enableUserRegistration(boolean enable) {
		registrationManager.enableRegistration(enable);
		return true;
	}

	public boolean enableResetPassword(boolean enable) {
		registrationManager.enablePasswordReset(enable);
		return true;
	}

	public boolean enableEmailValidation(boolean enable) {
		registrationManager.enableEmailValidation(enable);
		return true;
	}

	public boolean enableHumanValidation(boolean enable) {
		registrationManager.enableHumanValidation(enable);
		return true;
	}

	public void setRegistrationManager(RegistrationManager registrationManager) {
		this.registrationManager = registrationManager;
	}

	public boolean updateAdminEmail(String adminEmail) {
		this.applicationManager.setAdminEmail(adminEmail);
       
		return true;
	}
	
	public boolean enableUserAccount(String username,boolean enable) {
	    User user;
		try {
			user = this.userManager.getUser(Integer.parseInt(username));
			user.setEnabled(enable);
			user.setModified(this.applicationManager.getApplicationTime());
			this.userManager.updateUser(user);
			return true;
		} catch (NotFoundException e) {
			return false;
		}
	
		
	}


}
