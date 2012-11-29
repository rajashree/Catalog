/*
 * Copyright (C) 2007-2009 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 * 
 */
package com.sourcen.meteriffic.service;

import com.sourcen.meteriffic.model.Property;
import javassist.NotFoundException;
import org.apache.commons.httpclient.HttpClient;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Eclipse.
 * User : rajashreem
 * Date : Jun 9, 2009
 * Time : 10:07:09 PM
 */

public interface ApplicationManager extends ServiceManager{
	
	public String DEFAULT_CHAR_ENCODING = "URF-8";
	
	public String getFeedbackMailFromAddress() throws NotFoundException;
	
	public String getFeedbackMailToAddress() throws NotFoundException;
	
	public String getFeedbackMailSubject() throws NotFoundException;

	public String getMailHost() throws NotFoundException;
	
	public int getMailPort() throws NotFoundException;
	
	public String getMailUser() throws NotFoundException;
	
	public String getMailPassword() throws NotFoundException;
	
	public TimeZone getTimeZone();
	
	public Locale getLocale();
	
	public String getCharacterEncoding();
	
	public TimeZone getApplicationTimeZone();
	
	public void setApplicationTimeZone(String timezoneId);
	
	public String getServerTime();
	
	public String getApplicationTime();
	
	public String getAdminEmail();
	
	public void setAdminEmail(String email);
	
	public String[] getAvailableTimeZones();
	
	public HttpClient getClient();
	
	public String convertArrayToString(String[] array);
	
	public void saveProperty(Property property);
	
	public void removeProperty(String key) throws NotFoundException;
	
	public void updateProperty(Property property) throws NotFoundException;
	
	public Property getProperty(String key) throws NotFoundException;
	
	public int getIntProperty(String key) throws NotFoundException;
	
	public void deleteProperty(String key) throws NotFoundException;
	
	public List<Property> getAllProperty();
	
	public boolean getBooleanProperty(String key, boolean defaultValue)throws NotFoundException;
	
	public String encrypt(String password);
	
	public String getStringToken();

}
