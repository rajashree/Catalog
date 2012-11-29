package com.sourcen.space.util;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


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
		
		/*String[] zoneIDs = GMTZone.getAvailableIDs();
		System.out.println("Zone ID              Current Date and Time");
		System.out.println("-------              ----------------------------------");
		for( int i=0; i< zoneIDs.length; i++ ) {
         tz = GMTZone.getTimeZone( zoneIDs[i] );
			int offset = Math.max( 1, 18 - zoneIDs[i].length() );
			System.out.println("  " + zoneIDs[i]
							+ pad.substring( 0, offset )
							+ edf.format( now, tz ) );
		}*/
	}
}
