package com.dell.acs.web.dataimport.model.admin;

import java.util.Date;

import org.apache.commons.httpclient.util.DateUtil;

public class FormatUtils {

	public FormatUtils() {
		// TODO Auto-generated constructor stub
	}

	public static final String formatElapse(long totalTime) {
		if (totalTime != 0) {
			int millis = (int) (totalTime % 1000L);
			totalTime /= 1000L;
			int seconds = (int) (totalTime % 60L);
			totalTime /= 60L;
			int minutes = (int) (totalTime % 60L);
			totalTime /= 60L;
			int hours = (int) totalTime;

			return String.format("%03d:%02d:%02d:%03d", hours, minutes,
					seconds, millis);
		} else {
			return "-";
		}
	}

	public static final String formatSeconds(long total) {
		int millis = (int) (total % 1000L);
		total /= 1000L;
		int seconds = (int) total;

		return String.format("%04d:%03d", seconds, millis);
	}

	public static final String formatDateTime(Date date) {
		if (date != null) {
			return DateUtil.formatDate(date, "yyyy-MM-dd HH:mm:ss");
		} else {
			return "-";
		}
	}

	public static String formatFilePath(String fileName) {
		int pos = fileName.lastIndexOf('/');
		if (pos != -1) {
			return fileName.substring(pos+1);
		} else {
			return fileName;
		}
	}

	public static String formatDateTime(long startTime) {
		if (startTime != 0) {
			return FormatUtils.formatDateTime(new Date(startTime));
		} else {
			return "-";
		}
	}

	public static String formatElapse(long startTime, long endTime) {
		if (startTime != 0) {
			long totalTime = ((endTime != 0) ? endTime : System.currentTimeMillis()) - startTime;
			
			return FormatUtils.formatElapse(totalTime);
		} else {
			return "-";
		}
	}

	public static String formatElapse(Date startTime, Date endTime) {
		if (startTime != null) {
			long totalTime = ((endTime != null) ? endTime.getTime() : System.currentTimeMillis()) - startTime.getTime();
			
			return FormatUtils.formatElapse(totalTime);
		} else {
			return "-";
		}
	}

}
