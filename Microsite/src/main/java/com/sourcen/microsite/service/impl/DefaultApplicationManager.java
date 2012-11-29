package com.sourcen.microsite.service.impl;

import com.sun.image.codec.jpeg.*;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.text.DateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.acegisecurity.providers.dao.SaltSource;
import org.acegisecurity.providers.encoding.PasswordEncoder;

import com.sourcen.microsite.dao.PropertyDAO;
import com.sourcen.microsite.model.Property;
import com.sourcen.microsite.service.ApplicationManager;
import com.sourcen.microsite.util.ExtDateFormat;

import javassist.NotFoundException;

public class DefaultApplicationManager implements ApplicationManager {

	private PropertyDAO propertyDAO = null;
	private static String[] timeZoneIds = new String[] { "GMT", "Pacific/Apia",
			"HST", "AST", "America/Los_Angeles", "America/Phoenix",
			"America/Mazatlan", "America/Denver", "America/Belize",
			"America/Chicago", "America/Mexico_City", "America/Regina",
			"America/Bogota", "America/New_York", "America/Indianapolis",
			"America/Halifax", "America/Caracas", "America/Santiago",
			"America/St_Johns", "America/Sao_Paulo", "America/Buenos_Aires",
			"America/Godthab", "Atlantic/South_Georgia", "Atlantic/Azores",
			"Atlantic/Cape_Verde", "Africa/Casablanca", "Europe/Dublin",
			"Europe/Berlin", "Europe/Belgrade", "Europe/Paris",
			"Europe/Warsaw", "ECT", "Europe/Athens", "Europe/Bucharest",
			"Africa/Cairo", "Africa/Harare", "Europe/Helsinki",
			"Asia/Jerusalem", "Asia/Baghdad", "Asia/Kuwait", "Europe/Moscow",
			"Africa/Nairobi", "Asia/Tehran", "Asia/Muscat", "Asia/Baku",
			"Asia/Kabul", "Asia/Yekaterinburg", "Asia/Karachi",
			"Asia/Calcutta", "Asia/Katmandu", "Asia/Almaty", "Asia/Dhaka",
			"Asia/Colombo", "Asia/Rangoon", "Asia/Bangkok", "Asia/Krasnoyarsk",
			"Asia/Hong_Kong", "Asia/Irkutsk", "Asia/Kuala_Lumpur",
			"Australia/Perth", "Asia/Taipei", "Asia/Tokyo", "Asia/Seoul",
			"Asia/Yakutsk", "Australia/Adelaide", "Australia/Darwin",
			"Australia/Brisbane", "Australia/Sydney", "Pacific/Guam",
			"Australia/Hobart", "Asia/Vladivostok", "Pacific/Noumea",
			"Pacific/Auckland", "Pacific/Fiji", "Pacific/Tongatapu" };

	private static final Map<Locale, String[][]> timeZoneLists = Collections
			.synchronizedMap(new HashMap<Locale, String[][]>());
	private static final Map<String, String> nameMap = new HashMap<String, String>();

	public void setPropertyDAO(PropertyDAO propertyDAO) {
		this.propertyDAO = propertyDAO;
	}

	private PasswordEncoder passwordEncoder = null;
	private SaltSource saltSource = null;

	public SaltSource getSaltSource() {
		return saltSource;
	}

	public void setSaltSource(SaltSource saltSource) {
		this.saltSource = saltSource;
	}

	public void init() {

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

	public java.util.List<Property> getAllProperty() {

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

	public String[][] getTimeZoneList() {
		Locale jiveLocale = getLocale();

		String[][] timeZoneList = timeZoneLists.get(jiveLocale);
		if (timeZoneList == null) {
			Date now = new Date();
			String[] timeZoneIDs = timeZoneIds;
			// Now, create String[][] using the unique zones.
			timeZoneList = new String[timeZoneIDs.length][2];
			for (int i = 0; i < timeZoneList.length; i++) {
				String zoneID = timeZoneIDs[i];
				timeZoneList[i][0] = zoneID;
				timeZoneList[i][1] = getTimeZoneName(zoneID, now, jiveLocale);
			}

			// Add the new list to the map of locales to lists
			timeZoneLists.put(jiveLocale, timeZoneList);
		}

		return timeZoneList;
	}

	private static String getTimeZoneName(String zoneID, Date now, Locale locale) {
		TimeZone zone = TimeZone.getTimeZone(zoneID);
		StringBuffer buf = new StringBuffer();
		// Add in the GMT part to the name. First, figure out the offset.
		int offset = zone.getRawOffset();
		if (zone.inDaylightTime(now) && zone.useDaylightTime()) {
			offset += (int) 60 * 60 * 1000;
		}

		buf.append("(");
		if (offset < 0) {
			buf.append("GMT-");
		} else {
			buf.append("GMT+");
		}
		offset = Math.abs(offset);
		int hours = offset / (60 * 60 * 1000);
		int minutes = (offset % (60 * 60 * 1000)) / (60 * 60 * 1000);
		buf.append(hours).append(":");
		if (minutes < 10) {
			buf.append("0").append(minutes);
		} else {
			buf.append(minutes);
		}
		buf.append(") ");

		// Use a friendly english timezone name if the locale is en, otherwise
		// use the timezone id
		if ("en".equals(locale.getLanguage())) {
			String name = nameMap.get(zoneID);
			if (name == null) {
				name = zoneID;
			}

			buf.append(name);
		} else {
			buf.append(zone.getDisplayName(true, TimeZone.LONG, locale)
					.replace('_', ' ').replace('/', ' '));
		}

		return buf.toString();
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

	public TimeZone[] getAvailableTimeZones() {
		String[] availableTimezones = TimeZone.getAvailableIDs();
		Arrays.sort(availableTimezones);
		int len = availableTimezones.length;
		TimeZone[] gmtZoneIDs = new TimeZone[len];

		for (int i = 0; i < len; i++) {
			gmtZoneIDs[i] = TimeZone.getTimeZone(availableTimezones[i]);

		}

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

	public String getAdminEmail() {

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

	public void zipDir(String dir2zip, ZipOutputStream zos) {
		try {
			// create a new File object based on the directory we

			File zipDir = new File(dir2zip);
			// get a listing of the directory content
			String[] dirList = zipDir.list();
			byte[] readBuffer = new byte[2156];
			int bytesIn = 0;
			// loop through dirList, and zip the files
			for (int i = 0; i < dirList.length; i++) {
				File f = new File(zipDir, dirList[i]);
				if (f.isDirectory()) {
					// if the File object is a directory, call this
					// function again to add its content recursively
					String filePath = f.getPath();

					zipDir(filePath, zos);
					// loop again
					continue;
				}
				// if we reached here, the File object f was not

				// create a FileInputStream on top of f
				FileInputStream fis = new FileInputStream(f);

				String path = f.getPath();
				// path = path.substring(path);
				// System.out.println(path);
				ZipEntry anEntry = new ZipEntry(path);
				// place the zip entry in the ZipOutputStream object
				zos.putNextEntry(anEntry);
				// now write the content of the file to the ZipOutputStream
				while ((bytesIn = fis.read(readBuffer)) != -1) {
					zos.write(readBuffer, 0, bytesIn);
				}
				// close the Stream
				fis.close();
			}
		} catch (Exception e) {
			// handle exception
		}
	}

	public void createThumnail(String srcpath, String disPath, int quality)
			throws FileNotFoundException {
		Image image = Toolkit.getDefaultToolkit().getImage(srcpath);
		MediaTracker mediaTracker = new MediaTracker(new Container());
		mediaTracker.addImage(image, 0);
		try {
			mediaTracker.waitForID(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// determine thumbnail size from WIDTH and HEIGHT
		int thumbWidth = 100;
		int thumbHeight = 80;
		double thumbRatio = (double) thumbWidth / (double) thumbHeight;
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		double imageRatio = (double) imageWidth / (double) imageHeight;
		if (thumbRatio < imageRatio) {
			thumbHeight = (int) (thumbWidth / imageRatio);
		} else {
			thumbWidth = (int) (thumbHeight * imageRatio);
		}
		// draw original image to thumbnail image object and
		// scale it to the new size on-the-fly
		BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = thumbImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
		// save thumbnail image to OUTFILE
		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(disPath));
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);

		quality = Math.max(0, Math.min(quality, 100));
		param.setQuality((float) quality / 100.0f, false);
		encoder.setJPEGEncodeParam(param);
		try {
			encoder.encode(thumbImage);
			out.close();
		} catch (ImageFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getTermsAndCondition() {
		try {
			return getProperty("application.registration.tandc").getValue();
			

		} catch (NotFoundException e) {
			return "";
		}
	}


	public boolean enable() {
		// TODO Auto-generated method stub
		return true;
	}

}
