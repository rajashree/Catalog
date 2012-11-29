package com.sourcen.microsite.dwr;

public interface AdminDwrService extends UserDwrService {

	public boolean setDefaultTimeZone(String timeZone);

	public boolean setDefaultLocale();

	public boolean enableUserRegistration(boolean enable);

	public boolean enableResetPassword(boolean enable);

	public boolean enableEmailValidation(boolean enable);

	public boolean enableHumanValidation(boolean enable);

	public boolean updateAdminEmail(String adminEmail);

	public boolean enableUserAccount(String username, boolean enable);

}
