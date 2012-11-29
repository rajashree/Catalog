/*
 * Copyright (C) 2007-2009 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 * 
 */
package com.sourcen.meteriffic.service;

/**
 * Created by Eclipse.
 * User : rajashreem
 * Date : Jun 9, 2009
 * Time : 9:11:16 PM
 */

public interface RegistrationManager extends ServiceManager{
	
	public boolean isEmailValidationEnabled();
	
	public boolean isAccountValidated(String userID);
	
	public boolean isHumanValidationEnabled();
	
	public boolean isPasswordResetEnabled();
	
	public boolean isUserRegistrationEnabled();
	
	public int getDefaultMinUsernameLength();
	
	public void setDefaultMinUsernameLength();
	
	public void enableRegistration(boolean enable);
	
	public void enablePasswordReset(boolean enable);
	
	public void enableEmailValidation(boolean enable);
	
	public void enableHumanValidation(boolean enable);
	
	
}
