/*
 * Copyright (C) 2007-2009 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 * 
 */
package com.sourcen.meteriffic.service.impl;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import javassist.NotFoundException;

import org.acegisecurity.providers.dao.SaltSource;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.apache.commons.httpclient.HttpClient;

import com.sourcen.meteriffic.dao.PropertyDAO;
import com.sourcen.meteriffic.model.Property;
import com.sourcen.meteriffic.service.ApplicationManager;
import com.sourcen.meteriffic.util.ExtDateFormat;

/**
 * Created by Eclipse.
 * User : rajashreem
 * Date : Jun 9, 2009
 * Time : 11:06:15 PM
 */

public class ApplicationManagerImpl implements ApplicationManager{

	private PropertyDAO propertyDAO = null;
	private HttpClient client = new HttpClient();
	private PasswordEncoder passwordEncoder = null;
	private SaltSource saltSource = null;
	
	public String convertArrayToString(String[] array) {
		StringBuffer buf = new StringBuffer("");
		if(array != null)
			for(int i=0; i < array.length; i++){
				buf.append(array[i]);
				if(i != array.length - 1)
					buf.append(",");
			}
		return buf.toString();
	}

	public String getStringToken() {
		Random r = new Random();
		return Long.toString(Math.abs(r.nextLong()), 36);
	}
	
	public void deleteProperty(String key) throws NotFoundException {
		this.propertyDAO.deleteProperty(key);
	}

	public String getAdminEmail() {
		String adminEmail;
		try {
			adminEmail = this.getProperty("admin.email").getValue();
		} catch (NotFoundException e) {
			return "admin@sourcen.com";
		}
		return adminEmail;
	}

	public List<Property> getAllProperty() {
		return this.propertyDAO.getAllProperty();
	}

	public String getApplicationTime() {
		Date now = new Date();
		ExtDateFormat edf = new ExtDateFormat(DateFormat.DEFAULT,
				DateFormat.LONG, Locale.getDefault());
		return edf.format(now, this.getApplicationTimeZone());
	}

	public TimeZone getApplicationTimeZone() {
		try {
			String timezone = this.getProperty("application.timezone").getValue();
			return TimeZone.getTimeZone(timezone);
		} catch (NotFoundException e){
			return TimeZone.getDefault();
		}
	}

	public String[] getAvailableTimeZones() {
		String[] zoneIDs = TimeZone.getAvailableIDs();
		int len = zoneIDs.length;
		String[] gmtZoneIDs = new String[len+8];
		System.arraycopy(zoneIDs,0,gmtZoneIDs,0,len);
		String[] temp = {"Europe/London", "Europe/Belfast", "Europe/Dublin",
				"Europe/Lisbon", "Atlantic/Canary", "Atlantic/Faeroe",
				"Atlantic/Madeira", "WET" };
		System.arraycopy(temp, 0, gmtZoneIDs, len, 8);
		return gmtZoneIDs;
	}


	public boolean getBooleanProperty(String key, boolean defaultValue)
			throws NotFoundException {
		if(this.getProperty(key) != null)
			return Boolean.valueOf(this.getProperty(key).getValue());
		else
			return defaultValue;
	}

	public String getCharacterEncoding() {
		return DEFAULT_CHAR_ENCODING;
	}

	public HttpClient getClient() {
		return client;
	}

	public String getFeedbackMailFromAddress() throws NotFoundException {
		try {
			return this.getProperty("feedback.mail.fromaddress").getValue();
		} catch (NotFoundException e) {
			return this.getAdminEmail();
		}
		
	}

	public String getFeedbackMailSubject() throws NotFoundException {
		try {
			return this.getProperty("feedback.mail.subject").getValue();
		} catch (NotFoundException e) {
			return null;
		}
	}

	public String getFeedbackMailToAddress() throws NotFoundException {
		try{
			return this.getProperty("feedback.mail.toaddress").getValue();
		}catch(NotFoundException e){
			return this.getAdminEmail();
		}
	}

	public int getIntProperty(String key) throws NotFoundException {
		String value = getProperty(key).getValue();
		if(value != null){
			try{
				return Integer.parseInt(value);
			}catch(NumberFormatException nfe){
				
			}
		}
		return Integer.parseInt(value);
	}

	public Locale getLocale() {
		return Locale.getDefault();
	}

	public String getMailHost() throws NotFoundException {
		return this.getProperty("mail.smtp.host").getValue();
	}

	public String getMailPassword() throws NotFoundException {
		return this.getProperty("mail.smtp.password").getValue();
	}

	public int getMailPort() throws NotFoundException {
		return this.getIntProperty("mail.smtp.port");
	}

	public String getMailUser() throws NotFoundException {
		return this.getProperty("mail.smtp.user").getValue();
	}

	public Property getProperty(String key) throws NotFoundException {
		if(propertyDAO.getProperty(key) == null)
			throw new NotFoundException("Property With"+ key +" Not Found");
		System.out.println("propertyDAO.getProperty(key)"+propertyDAO.getProperty(key).getValue());
		return propertyDAO.getProperty(key);
	}

	public String getServerTime() {
		return (new Date()).toString();
	}

	public TimeZone getTimeZone() {
		return TimeZone.getDefault();
	}

	public void removeProperty(String key) throws NotFoundException {
		this.propertyDAO.deleteProperty(key);
		
	}

	public void saveProperty(Property property) {
		Property databaseProperty;
		try{
			databaseProperty = this.getProperty(property.getKey());
			databaseProperty.setValue(property.getValue());
		}catch(NotFoundException e){
			this.propertyDAO.saveProperty(property);
		}
	}

	public void setAdminEmail(String email) {
		try{
			this.getProperty("admin.email").getValue();
			this.updateProperty(new Property("admin.email",email));
		}catch(NotFoundException e){
			this.saveProperty(new Property("admin.email",email));
		}
	}

	public void setApplicationTimeZone(String timezoneId) {
		try{
			this.getProperty("application.timezone").getValue();
			this.updateProperty(new Property("application.timezone", timezoneId));
		}catch(NotFoundException e){
			this.saveProperty(new Property("application.timezone",timezoneId));
		}
	}

	public void updateProperty(Property property) throws NotFoundException {
		this.getProperty(property.getKey());
		this.propertyDAO.updateProperty(property);
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

	public boolean isEnabled() {
		return true;
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

	public String encrypt(String password) {
		return passwordEncoder.encodePassword(password, "meteriffic");
	}

	/**
	 * @return the propertyDAO
	 */
	public PropertyDAO getPropertyDAO() {
		return propertyDAO;
	}

	/**
	 * @param propertyDAO the propertyDAO to set
	 */
	public void setPropertyDAO(PropertyDAO propertyDAO) {
		this.propertyDAO = propertyDAO;
	}

	/**
	 * @return the passwordEncoder
	 */
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	/**
	 * @param passwordEncoder the passwordEncoder to set
	 */
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * @return the saltSource
	 */
	public SaltSource getSaltSource() {
		return saltSource;
	}

	/**
	 * @param saltSource the saltSource to set
	 */
	public void setSaltSource(SaltSource saltSource) {
		this.saltSource = saltSource;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(HttpClient client) {
		this.client = client;
	}

}
