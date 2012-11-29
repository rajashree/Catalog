package com.sourcen.space.service;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.httpclient.HttpClient;

import javassist.NotFoundException;

import com.sourcen.space.model.Property;

public interface ApplicationManager extends ServiceManager {

	public final String DEFAULT_CHAR_ENCODING = "UTF-8";
	
	void saveProperty(Property property);

	void removeProperty(String key);

	void updateProperty(Property property) throws NotFoundException;

	public Property getProperty(String key) throws NotFoundException;

	public String getXMLProperty(String key) throws NotFoundException;

	public List<Property> getAllProperty();

	public String exportPropertAsXML();

	public String exportPropertAsCSV();

	String getCharacterEncoding();

	public Locale getLocale();

	public TimeZone getTimeZone();

	int getIntProperty(String string) throws NotFoundException;

	public String getConfigFile();

	public void setConfigFile(String configFile);

	public String getMailHost() throws NotFoundException;

	public int getMailPort() throws NotFoundException;

	public String getMailUser() throws NotFoundException;

	public String getMailPassword() throws NotFoundException;

	public String encrypt(String password);

	public TimeZone getApplicationTimeZone();

	public void setApplicationTimeZone(String timezoneId);

	public String convertArrayToString(String[] array);

	boolean getBooleanProperty(String string, boolean defaultValue)
			throws NotFoundException;

	public String getStringToken();

	public String getServerTime();

	public String getApplicationTime();

	public String getAdminEmail();

	public void setAdminEmail(String email);

	public String[] getAvailableTimeZones();

	void deleteProperty(String key) throws NotFoundException;

	HttpClient getClient();

}
