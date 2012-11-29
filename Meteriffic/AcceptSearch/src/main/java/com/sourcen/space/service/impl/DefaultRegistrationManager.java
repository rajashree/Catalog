package com.sourcen.space.service.impl;

import javassist.NotFoundException;

import com.sourcen.space.model.Property;
import com.sourcen.space.service.ApplicationManager;
import com.sourcen.space.service.EmailManager;
import com.sourcen.space.service.RegistrationManager;
import com.sourcen.space.service.UserManager;

public class DefaultRegistrationManager implements RegistrationManager {

	private EmailManager emailManager;
	private UserManager userManager;
	private int defaultMinUsernameLength=5;
	private ApplicationManager applicationManager=null;
	   

	public ApplicationManager getApplicationManager() {
		return applicationManager;
	}

	public void setApplicationManager(ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
	}

	public int getDefaultMinUsernameLength() {
		return defaultMinUsernameLength;
	}

	public void setDefaultMinUsernameLength(int defaultMinUsernameLength) {
		this.defaultMinUsernameLength = defaultMinUsernameLength;
	}

	public boolean isAccountValidated(String userID) {
		return true;
	}

	
	public boolean isEnabled() {
		boolean enabled=true;
		try {
			enabled =applicationManager.getBooleanProperty("enableRegistration",enabled);
		} catch (NotFoundException e) {
			
			return false;
		}
		
		return enabled;
	}

	
	public void init() {
	
	}

	public void start() {
	}

	public void stop() {
	
	}

	public EmailManager getEmailManager() {
		return emailManager;
	}

	public void setEmailManager(EmailManager emailManager) {
		this.emailManager = emailManager;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	

	public void restart() {
		
		
	}
	public boolean isEmailValidationEnabled() {
		boolean enabled=true;
		try {
			enabled =applicationManager.getBooleanProperty("enableEmailValidation",enabled);
		} catch (NotFoundException e) {
			
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

	public boolean isPasswordResetEnabled() {
		boolean enabled=true;
		try {
			enabled =applicationManager.getBooleanProperty("enablePasswordReset",enabled);
		} catch (NotFoundException e) {
			
			return false;
		}
		
		return enabled;
	}

	public void enablePasswordReset(boolean enabel) {
		String status="true";
		
		if(!enabel)
			status="false";
		
		try {
			 this.applicationManager.getProperty("enablePasswordReset").getValue();
			this.applicationManager.updateProperty(new Property("enablePasswordReset",status));
			
		} catch (NotFoundException e) {
			this.applicationManager.saveProperty(new Property("enablePasswordReset",status));
		}
		
	}
	
	public void enableRegistration(boolean enabel) {
	String status="true";
	
		if(!enabel)
			status="false";
		
		try {
			 this.applicationManager.getProperty("enableRegistration").getValue();
			this.applicationManager.updateProperty(new Property("enableRegistration",status));
			
		} catch (NotFoundException e) {
			this.applicationManager.saveProperty(new Property("enableRegistration",status));
		}
		
	}

	public void enableEmailValidation(boolean enable) {
		String status="true";
		
		if(!enable)
			status="false";
		
		try {
			 this.applicationManager.getProperty("enableEmailValidation").getValue();
			this.applicationManager.updateProperty(new Property("enableEmailValidation",status));
			
		} catch (NotFoundException e) {
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
	
	
	public String getFeedbackMailFromAddress() throws NotFoundException{
			
		try {
			return this.applicationManager.getProperty("feedback.mail.fromaddress").getValue();
			
		} catch (NotFoundException e) {
			
			return this.applicationManager.getAdminEmail();
		}
		
	}
		
	public String getFeedbackMailToAddress() throws NotFoundException{
		try {
			return this.applicationManager.getProperty("feedback.mail.toaddress").getValue();
			
		} catch (NotFoundException e) {
			
			return this.applicationManager.getAdminEmail();
		}
	}
		
	public String getFeedbackMailSubject() throws NotFoundException{
		try {
			return this.applicationManager.getProperty("feedback.mail.subject").getValue();
			
		} catch (NotFoundException e) {
			
			return null;
		}
	}
	
	public void setFeedbackMailSubject(String subject) throws NotFoundException{
		this.applicationManager.saveProperty(new Property("feedback.mail.subject",subject));
	}
	
	public void setFeedbackMailFromAddress(String fromAddress) throws NotFoundException{
		this.applicationManager.saveProperty(new Property("feedback.mail.fromaddress",fromAddress));
	}
	
	public void setFeedbackMailToAddress(String toAddress) throws NotFoundException{
		this.applicationManager.saveProperty(new Property("feedback.mail.toaddress",toAddress));
	}
		

}
