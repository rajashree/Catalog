/*
 * Copyright (C) 2007-2009 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 * 
 */
package com.sourcen.meteriffic.service.impl;

import javassist.NotFoundException;

import com.sourcen.meteriffic.model.Property;
import com.sourcen.meteriffic.service.ApplicationManager;
import com.sourcen.meteriffic.service.EmailManager;
import com.sourcen.meteriffic.service.RegistrationManager;
import com.sourcen.meteriffic.service.UserManager;

/**
 * Created by Eclipse.
 * User : rajashreem
 * Date : Jun 10, 2009
 * Time : 12:15:22 AM
 */

public class RegistrationManagerImpl implements RegistrationManager{


	private EmailManager emailManager;
	private UserManager userManager;
	private int defaultMinUsernameLength=5;
	private ApplicationManager applicationManager=null;
	
	
	public void enableEmailValidation(boolean enable) {
		String status = "true";
		if(!enable)
			status = "false";
		try{
			this.applicationManager.getProperty("enableEmailValidation").getValue();
			this.applicationManager.updateProperty(new Property("enableEmailValidation",status));
		}catch(NotFoundException e){
			this.applicationManager.saveProperty(new Property("enableEmailValidation",status));
		}
	}

	public void enableHumanValidation(boolean enable) {
		String status="true";
		if(!enable)
			status="false";
		try {
			this.applicationManager.getProperty("enableHumanValidation").getValue();
			this.applicationManager.updateProperty(new Property("enableHumanValidation",status));
		} catch (NotFoundException e) {
			this.applicationManager.saveProperty(new Property("enableHumanValidation",status));
		}
	}

	public void enablePasswordReset(boolean enable) {
		String status="true";
		if(!enable)
			status="false";
		try {
			this.applicationManager.getProperty("enablePasswordReset").getValue();
			this.applicationManager.updateProperty(new Property("enablePasswordReset",status));
		} catch (NotFoundException e) {
			this.applicationManager.saveProperty(new Property("enablePasswordReset",status));
		}
	}

	public void enableRegistration(boolean enable) {
		String status="true";
		if(!enable)
			status="false";
		try {
			this.applicationManager.getProperty("enableRegistration").getValue();
			this.applicationManager.updateProperty(new Property("enableRegistration",status));
		} catch (NotFoundException e) {
			this.applicationManager.saveProperty(new Property("enableRegistration",status));
		}
	}

	public int getDefaultMinUsernameLength() {
		return defaultMinUsernameLength;
	}

	public boolean isAccountValidated(String userID) {
		return true;
	}

	public boolean isEmailValidationEnabled() {
		boolean enabled=true;
		try{
			enabled = applicationManager.getBooleanProperty("enableEmailValidation", enabled);
		}catch(NotFoundException e){
			return false;
		}
		return enabled;
		
	}

	public boolean isHumanValidationEnabled() {
		boolean enabled=false;
		try {
			enabled =applicationManager.getBooleanProperty("enableHumanValidation",enabled);
		} catch (NotFoundException e) {
			return false;
		}
		return enabled;
	}
	
	public boolean isUserRegistrationEnabled() {
		boolean enabled=false;
		try {
			enabled =applicationManager.getBooleanProperty("enableRegistration",enabled);
		} catch (NotFoundException e) {
			return false;
		}
		return enabled;
	}

	public boolean isPasswordResetEnabled() {
		boolean enabled=true;
		try {
			enabled = applicationManager.getBooleanProperty("enablePasswordReset",enabled);
		} catch (NotFoundException e) {
			return false;
		}
		return enabled;
	}

	public void setDefaultMinUsernameLength() {
		this.defaultMinUsernameLength=defaultMinUsernameLength;
		
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

	public boolean isEnabled() {
		boolean enabled = true;
		try{
			enabled = applicationManager.getBooleanProperty("enableRegistration",enabled);
		}catch(NotFoundException e){
			return false;			
		}
		return enabled;
	}

	public void restart() {
		// TODO Auto-generated method stub
		
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}

	public void stop() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the emailManager
	 */
	public EmailManager getEmailManager() {
		return emailManager;
	}

	/**
	 * @param emailManager the emailManager to set
	 */
	public void setEmailManager(EmailManager emailManager) {
		this.emailManager = emailManager;
	}

	/**
	 * @return the userManager
	 */
	public UserManager getUserManager() {
		return userManager;
	}

	/**
	 * @param userManager the userManager to set
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 * @return the applicationManager
	 */
	public ApplicationManager getApplicationManager() {
		return applicationManager;
	}

	/**
	 * @param applicationManager the applicationManager to set
	 */
	public void setApplicationManager(ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
	}

	/**
	 * @param defaultMinUsernameLength the defaultMinUsernameLength to set
	 */
	public void setDefaultMinUsernameLength(int defaultMinUsernameLength) {
		this.defaultMinUsernameLength = defaultMinUsernameLength;
	}

}
