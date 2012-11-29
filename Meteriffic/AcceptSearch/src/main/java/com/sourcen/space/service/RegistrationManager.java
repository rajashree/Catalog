package com.sourcen.space.service;

import javassist.NotFoundException;

public interface RegistrationManager extends ServiceManager {

	
	/*
	 * If Enabled Ask User to Enter a vlaid his/her Email Address and Then Send
	 * a Link to user to Activate The Account.
	 */
	public boolean isEmailValidationEnabled();

	/*
	 * User Account is validated or not
	 */
	boolean isAccountValidated(String userID);

	public int getDefaultMinUsernameLength();
	
	public String getFeedbackMailFromAddress() throws NotFoundException;
	
	public String getFeedbackMailToAddress() throws NotFoundException;
	
	public String getFeedbackMailSubject() throws NotFoundException;

	public void setDefaultMinUsernameLength(int defaultMinUsernameLength);

	public boolean isHumanValidationEnabled();

	public boolean isPasswordResetEnabled();

	public void enableRegistration(boolean enabel);

	public void enablePasswordReset(boolean enabel);

	public void enableEmailValidation(boolean enable);

	public void enableHumanValidation(boolean enable);

}
