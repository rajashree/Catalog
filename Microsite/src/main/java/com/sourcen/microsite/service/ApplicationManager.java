package com.sourcen.microsite.service;

/*
 * Revision: 1.0
 * Date: October 25, 2008
 *
 * Copyright (C) 2005 - 2008 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 *
 * By : Chandra Shekher
 *
 */
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.zip.ZipOutputStream;

import com.sourcen.microsite.model.Property;

import javassist.NotFoundException;

public interface ApplicationManager extends ServiceManager {

	public final String DEFAULT_CHAR_ENCODING = "UTF-8";

	void saveProperty(Property property);

	void removeProperty(String key);

	void updateProperty(Property property) throws NotFoundException;

	public Property getProperty(String key) throws NotFoundException;

	public List<Property> getAllProperty();

	public String exportPropertAsXML();

	public String exportPropertAsCSV();

	String getCharacterEncoding();

	public Locale getLocale();

	public TimeZone getTimeZone();

	int getIntProperty(String string) throws NotFoundException;

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

	public String getTermsAndCondition();

	public String getApplicationTime();

	public String getAdminEmail();

	public void setAdminEmail(String email);

	public TimeZone[] getAvailableTimeZones();

	public void deleteProperty(String key) throws NotFoundException;

	public void zipDir(String path, ZipOutputStream zos);

	public void createThumnail(String srcpath, String disPath, int quality)
			throws FileNotFoundException;

	public String[][] getTimeZoneList();
}
