/*
 * Copyright (C) 2007-2009 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 * 
 */
package com.sourcen.meteriffic.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Eclipse.
 * User : rajashreem
 * Date : Jun 9, 2009
 * Time : 11:24:19 PM
 */


public class ExtDateFormat extends SimpleDateFormat {
	public ExtDateFormat( int dtDisplay, int tmDisplay, Locale loc ) {
		SimpleDateFormat temp = (SimpleDateFormat)
					DateFormat.getDateTimeInstance( dtDisplay, tmDisplay, loc );
		String pat = temp.toLocalizedPattern();
		applyLocalizedPattern( pat );
	}

	public String format( Date date, TimeZone tz ) {
		setTimeZone( tz );
		return format( date );
	}

	public static void main(String[] args) {
		
		String pad = "                    ";	// 20 spaces
		Date now = new Date();
		TimeZone tz;
		ExtDateFormat edf = new ExtDateFormat( DateFormat.DEFAULT, DateFormat.LONG, Locale.getDefault() );
		System.out.println(edf.format( now, TimeZone.getTimeZone("Asia/Calcutta") ));
		
	}
}
