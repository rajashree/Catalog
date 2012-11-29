package com.sourcen.meteriffic.dwr;

public interface AdminDwrService {

	public boolean setDefaultTimeZone(String timeZone);

	public boolean setDefaultLocale();

	public boolean enableUserRegistration(boolean enable);

	public boolean enableResetPassword(boolean enable);

	public boolean enableEmailValidation(boolean enable);

	public boolean enableHumanValidation(boolean enable);

	public boolean updateAdminEmail(String adminEmail);

	public boolean enableUserAccount(String username,boolean enable);

	public boolean isEmailValidationEnabled();
	
	public boolean isResetPasswordEnabled() ;
	
	public boolean isUserRegistrationEnabled();
	
	public String getDefaultTimeZone();
	
	public String getAdminEmail();
	
}
