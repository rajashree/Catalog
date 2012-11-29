package com.sourcen.meteriffic.dwr.impl;

import javassist.NotFoundException;

import com.sourcen.meteriffic.dwr.AdminDwrService;
import com.sourcen.meteriffic.dwr.impl.UserDwrServiceImpl;
import com.sourcen.meteriffic.service.RegistrationManager;
import com.sourcen.meteriffic.model.User;

public class AdminDwrServiceImpl extends UserDwrServiceImpl implements AdminDwrService{

	private RegistrationManager registrationManager = null;

	public boolean isEmailValidationEnabled() {
		return registrationManager.isEmailValidationEnabled();
	}
	
	public boolean isResetPasswordEnabled() {
		return registrationManager.isPasswordResetEnabled();
	}
	
	public boolean isUserRegistrationEnabled() {
		return registrationManager.isUserRegistrationEnabled();
	}
	
	public String getDefaultTimeZone() {
		return this.applicationManager.getApplicationTime();
	}

	public String getAdminEmail() {
		return this.applicationManager.getAdminEmail();
	}
	
	public boolean enableEmailValidation(boolean enable) {
		registrationManager.enableEmailValidation(enable);
		return true;
	}

	public boolean enableHumanValidation(boolean enable) {
		registrationManager.enableHumanValidation(enable);
		return true;
	}

	public boolean enableResetPassword(boolean enable) {
		registrationManager.enableEmailValidation(enable);
		return true;
	}

	public boolean enableUserAccount(String username, boolean enable) {
		User user;
		try {
			user = this.userManager.getUser(username);
			user.setEnabled(enable);
			user.setModified(this.getApplicationManager().getApplicationTime());
			this.userManager.updateUser(user);
			return true;
		} catch (NotFoundException e) {
			return false;
		}
	}

	public boolean enableUserRegistration(boolean enable) {
		registrationManager.enableRegistration(enable);
		return true;
	}

	public boolean setDefaultLocale() {
		return true;
	}

	public boolean setDefaultTimeZone(String timeZone) {
		this.applicationManager.setApplicationTimeZone(timeZone);
		return true;
	}

	public boolean updateAdminEmail(String adminEmail) {
		this.applicationManager.setAdminEmail(adminEmail);
	    return true;
	}

	public RegistrationManager getRegistrationManager() {
		return registrationManager;
	}

	public void setRegistrationManager(RegistrationManager registrationManager) {
		this.registrationManager = registrationManager;
	}

}
