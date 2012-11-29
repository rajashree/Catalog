package com.sourcen.space.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.util.*;

import org.acegisecurity.providers.dao.SaltSource;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.apache.commons.httpclient.HttpClient;

import javassist.NotFoundException;

import com.sourcen.space.dao.PropertyDAO;
import com.sourcen.space.model.Property;
import com.sourcen.space.model.XMLProperties;
import com.sourcen.space.service.ApplicationManager;
import com.sourcen.space.util.ExtDateFormat;

public class DefaultApplicationManager implements ApplicationManager {

	private String configFile = "startup.xml";

	public XMLProperties setupProperties = null;

	private PropertyDAO propertyDAO = null;
	
	public HttpClient client = new HttpClient();

	public void setPropertyDAO(PropertyDAO propertyDAO) {
		this.propertyDAO = propertyDAO;
	}

	private PasswordEncoder passwordEncoder = null;
	private SaltSource saltSource = null;

	public XMLProperties getSetupProperties() {
		return setupProperties;
	}

	public void setSetupProperties(XMLProperties setupProperties) {
		this.setupProperties = setupProperties;
	}

	public SaltSource getSaltSource() {
		return saltSource;
	}

	public void setSaltSource(SaltSource saltSource) {
		this.saltSource = saltSource;
	}

	public void init() {

		this.loadSetupProperties();
	}

	public boolean isEnabled() {

		return true;
	}

	public void start() {

	}

	public void stop() {

	}

	public Property getProperty(String key) throws NotFoundException {

		if (propertyDAO.getProperty(key) == null)
			throw new NotFoundException("Property With" + key + "Not Found");
		return propertyDAO.getProperty(key);
	}

	public void deleteProperty(String key) throws NotFoundException {

		propertyDAO.deleteProperty(key);
		
	}
	
	public void saveProperty(Property property) {
		Property databaseProperty;
		try {
			databaseProperty = this.getProperty(property.getKey());
			databaseProperty.setValue(property.getValue());
		} catch (NotFoundException e) {

			this.propertyDAO.saveProperty(property);
		}

	}

	public String exportPropertAsCSV() {

		return null;
	}

	public String exportPropertAsXML() {

		return null;
	}

	public List<Property> getAllProperty() {

		return propertyDAO.getAllProperty();
	}

	public String getCharacterEncoding() {

		return DEFAULT_CHAR_ENCODING;
	}

	public int getIntProperty(String key) throws NotFoundException {

		String value = getProperty(key).getValue();
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException nfe) {

			}
		}
		return Integer.parseInt(value);
	}

	public void restart() {

	}

	public Locale getLocale() {

		return Locale.getDefault();
	}

	public TimeZone getTimeZone() {

		return TimeZone.getDefault();
	}

	public String getXMLProperty(String key) throws NotFoundException {
		if (setupProperties == null) {
			System.err.println("Initializing Required!!");
			return key;
		}

		return setupProperties.get(key);
	}

	private synchronized void loadSetupProperties() {

		if (setupProperties == null) {
			try {
				setupProperties = new XMLProperties(this.configFile);
			} catch (IOException ioe) {
				System.err
						.println("JiveHome initialization failed, this is normal part of the setup process, however it should not occur during the normal operations of Clearspace.");

			}
		}
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

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public static void main(String args[]) {
		DefaultApplicationManager defaultApplicationManager = new DefaultApplicationManager();
		defaultApplicationManager.setConfigFile("c:/wtp/setup.xml");
		defaultApplicationManager.init();
		try {
			System.out.println(defaultApplicationManager
					.getXMLProperty("setup"));
		} catch (NotFoundException e) {
			e.printStackTrace();
		}

	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public String encrypt(String password) {
		return passwordEncoder.encodePassword(password, "accept");
	}

	public TimeZone getApplicationTimeZone() {

		try {
			String timezone = this.getProperty("applciation.timezone")
					.getValue();
			return TimeZone.getTimeZone(timezone);
		} catch (NotFoundException e) {
			return TimeZone.getDefault();
		}

	}

	public String convertArrayToString(String[] array) {
		StringBuffer buf = new StringBuffer("");
		if (array != null)
			for (int i = 0; i < array.length; ++i) {
				buf.append(array[i]);
				if (i != array.length - 1)
					buf.append(',');
			}
		return (buf.toString());
	}

	public void removeProperty(String key) {
		this.propertyDAO.deleteProperty(key);
	}

	public boolean getBooleanProperty(String key, boolean defaultValue)
			throws NotFoundException {

		if (this.getProperty(key) != null)

			return Boolean.valueOf(this.getProperty(key).getValue());
		else
			return defaultValue;
	}

	public String getStringToken() {
		Random r = new Random();
		return Long.toString(Math.abs(r.nextLong()), 36);
	}

	public String getServerTime() {

		return (new Date()).toString();
	}

	public String getApplicationTime() {
		Date now = new Date();

		ExtDateFormat edf = new ExtDateFormat(DateFormat.DEFAULT,
				DateFormat.LONG, Locale.getDefault());

		return edf.format(now, this.getApplicationTimeZone());
	}

	public String[] getAvailableTimeZones() {
		String[] zoneIDs = TimeZone.getAvailableIDs();
		int len = zoneIDs.length;
		String[] gmtZoneIDs = new String[len + 8];
		System.arraycopy(zoneIDs, 0, gmtZoneIDs, 0, len);
		String[] temp = { "Europe/London", "Europe/Belfast", "Europe/Dublin",
				"Europe/Lisbon", "Atlantic/Canary", "Atlantic/Faeroe",
				"Atlantic/Madeira", "WET" };
		System.arraycopy(temp, 0, gmtZoneIDs, len, 8);
		return gmtZoneIDs;
	}

	public void setApplicationTimeZone(String timezoneId) {
		try {
			this.getProperty("applciation.timezone").getValue();
			this
					.updateProperty(new Property("applciation.timezone",
							timezoneId));

		} catch (NotFoundException e) {
			this.saveProperty(new Property("applciation.timezone", timezoneId));
		}
	}

	public void updateProperty(Property property) throws NotFoundException {
		this.getProperty(property.getKey());
		this.propertyDAO.updateProperty(property);

	}

	public String getAdminEmail()  {

		String adminEmail;
		try {
			adminEmail = this.getProperty("admin.email").getValue();
		} catch (NotFoundException e) {
			return "admin@domain.com";
		}
		return adminEmail;

	}

	public void setAdminEmail(String email) {
		try {
			this.getProperty("admin.email").getValue();
			this.updateProperty(new Property("admin.email", email));

		} catch (NotFoundException e) {
			this.saveProperty(new Property("admin.email", email));
		}
	}

	public HttpClient getClient() {
		return client;
	}

	public void setClient(HttpClient client) {
		this.client = client;
	}

}
