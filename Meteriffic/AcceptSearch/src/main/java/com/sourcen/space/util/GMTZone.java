package com.sourcen.space.util;
import java.util.*;
import java.text.*;     // for the test code in main only

public class GMTZone extends SimpleTimeZone {

   /**
    * You can add other "more than 3-letter zone ID's" if you wish by just
    * adding additional "else if" code to this method, and maybe renaming
    * the class.   This code is "forwards compatible" with Java 2 so
    * the long time zone IDs supported in JDK 1.2 will be honored when
    * users upgrade.  <br>
    * Overrides - getTimeZone() in class java.util.TimeZone
    */
   public static synchronized TimeZone getTimeZone(String ID) {
      if( ID.length() <= 3 && !ID.equals("WET") )
         return TimeZone.getTimeZone(ID);
      else if( ID.equals("Europe/London") ||
               ID.equals("Europe/Belfast") ||
               ID.equals("Europe/Dublin") ||
               ID.equals("Europe/Lisbon") ||
               ID.equals("Atlantic/Canary") ||
               ID.equals("Atlantic/Faeroe") ||
               ID.equals("Atlantic/Madeira") ||
               ID.equals("WET") ) {
         return new SimpleTimeZone( 0, ID,
                         Calendar.MARCH, -1, Calendar.SUNDAY, 1000*60*60*1,
                         Calendar.OCTOBER, -1, Calendar.SUNDAY, 1000*60*60*1);
      } else 
         return TimeZone.getTimeZone( ID );
   }
   // Programming note:  The data for long TimeZone ID's is stored in
   //  java.util.TimeZoneData, an internal (package private) class which can only be called
   //  by methods in the java.util.* package.  TimeZoneData is just a list of constructors
   //  for SimpleTimeZone (as written in my JDK 1.1.6 source code).  There is a look-up
   //  method that finds a constructor based on the ID String passed in.
   // The method in java.util.TimeZone that returns a TimeZone object is getTimeZone(),
   //  and, sure enough, in 1.1.x releases it is set up to pass only 3-letter time zone ID's
   //  to the get() method in TimeZoneData.
   // Hence, I concluded we have to override getTimeZone() if we want to use long ID's
   //  in one or more zones before Sun officially lets us do so in all zones.  This is
   //  admittedly a hack, but for the implementation of daylight "summer" time in the
   //  countries that use GMT in winter, it represents a good and valid fix, IMHO.  This
   //  means of getting "GMT/BST", "GMT/IST", or "WET/WE%sT" functionality can be implemented
   //  by anyone worldwide who is writing code that will run in the UK, Ireland, Portugal,
   //  etc.  The same code will work "normally" everywhere else, whether the client is
   //  running 1.1.x or 1.2.
   // Of course, eventually, someone may want to remove the call to GMTZone, say in 3 or
   //  4 years when no one is using JDK 1.1.x code anymore.  (This should only be necessary
   //  if the 1456 bytes of extra code in GMTZone need to be removed.)  So annotate your code
   //  accordingly.  You needn't fear that TimeZoneData might be changed: it isn't
   //  used here.  And java.util.resources, a package of locale data that is used internally
   //  to get strings for the formatter, is likely only to be expanded, not trashed.
   //  And changes to that package will be transparent to this class, although they
   //  might affect the output of your date and time formatters.
   //
   // There is a way to implement any time zone you want, including long ID's if
   //  you choose, and including any formatting symbols you choose.  That is to 
   //  subclass SimpleTimeZone to create the new ID, and then use SimpleDateFormat to replace 
   //  or add to the zoneStrings[][] array maintained in DateFormatSymbols.  All this trouble
   //  does *not* get you a substitute TimeZone class that simply adds your chosen zone
   //  and formatting symbols to all the others already available.  You end up instead
   //  with a "personal" time zone available only if you instantiate the personal
   //  SimpleTimeZone subclass.  
   //
   // And a minor note on daylight savings (summer) time.  The API now reflects that ending 
   //  time should be coded as the standard or non-daylight time, so using 1*1000*60*60 in the 
   //  constructor means we change from BST to GMT at 1:00 GMT. (I had this wrong for a year.) 

   /**
    * Adds the additional supported time zone ID's to the list
    * of all available ID's.<br>
    * Overrides - getAvailableIDs() in java.util.TimeZone
    */
   public static synchronized String[] getAvailableIDs() {
      String[] zoneIDs = TimeZone.getAvailableIDs();
      int len = zoneIDs.length;
      String[] gmtZoneIDs = new String[ len + 8 ];
      System.arraycopy(zoneIDs, 0, gmtZoneIDs, 0, len);
      String[] temp = {"Europe/London", "Europe/Belfast",
                       "Europe/Dublin", "Europe/Lisbon",
                       "Atlantic/Canary", "Atlantic/Faeroe",
                       "Atlantic/Madeira", "WET" };
      System.arraycopy(temp, 0, gmtZoneIDs, len, 8);
      return gmtZoneIDs;
   }

   /**
    * Replaces the list of supported time zone ID's at zero offset from GMT.<br>
    * Overrides - getAvailableIDs( rawOffset ) in java.util.TimeZone
    */
   public static synchronized String[] getAvailableIDs(int rawOffset) {             
      if (rawOffset == 0) {
         String[] result = { "GMT", "UTC", "Europe/London", "Europe/Belfast",
                             "Europe/Dublin", "Europe/Lisbon", "Atlantic/Canary",
                             "Atlantic/Faeroe", "Atlantic/Madeira", "WET" };
         return result;
      } else 
         return TimeZone.getAvailableIDs( rawOffset ); 
   }

   // Dummy constructor.  Works around lack of default constructor in SimpleTimeZone.
   private GMTZone() {
      super(0,"blah");
   }

   /** Illustrative test code */
   public static void main(String[] args) {
      String[][] data = { { "en","GB","Europe/London"   ,"London        " },
                          { "en","IE","Europe/Dublin"   ,"Dublin        " },
                          { "en","GB","Europe/Belfast"  ,"Belfast       " },
                          { "es","ES","Atlantic/Canary" ,"Canary Islands" },
                          { "da","DK","Atlantic/Faeroe" ,"Faeroe Islands" },
                          { "pt","PT","Atlantic/Madeira","Madeira       " },
                          { "pt","PT","Europe/Lisbon"   ,"Lisbon        " },
                          { "pt","PT","WET","Continental Europe (WET WE%sT)" },
                          { "fr","FR","ECT","Continental Europe (CET CE%sT)" }
                        };

      Date now = new Date();
      Locale.setDefault( new Locale("en", "GB") );
      System.out.println("Setting default locale to en_GB...");
      System.out.println("The formatter will use various locales as shown...");

      Locale loc;
      TimeZone tz;
      // To access the Locale data lookup you need to use
      //  SimpleDateFormat, not DateFormat, when you instantiate
      //  the formatter.  SimpleDateFormat will access 
      //  DateFormatSymbols, which in turn can access the 
      //  array of zone-specific identifiers (like BST and IST)
      SimpleDateFormat sdf;

      // Show "now" in the supported zones
      for( int i=0; i<data.length-2; i++ ) {
         loc = new Locale( data[i][0], data[i][1] );
         // here is the override of getTimeZone()...
         tz = GMTZone.getTimeZone( data[i][2] );
         TimeZone.setDefault( tz );
         sdf = (SimpleDateFormat)DateFormat.getDateTimeInstance(
                        DateFormat.LONG, DateFormat.LONG, loc);
         sdf.setTimeZone(tz);
         System.out.println("Loc " + data[i][0] + "_" + data[i][1] + "   "
                           + data[i][3] + ": " 
                           + sdf.format( now ) );
      }

      // Show "now" with French locale, zones WET and CET
      System.out.println();
      System.out.println("Setting default locale to pt_PT, then fr_FR...");
      Locale.setDefault( new Locale("fr","FR") );
      for( int i=data.length-2; i<data.length; i++ ) {
         loc = new Locale( data[i][0], data[i][1] );
         // here is the override of getTimeZone()...
         tz = GMTZone.getTimeZone( data[i][2] );
         TimeZone.setDefault( tz );
         sdf = (SimpleDateFormat)DateFormat.getDateTimeInstance(
                        DateFormat.LONG, DateFormat.LONG, loc);
         sdf.setTimeZone(tz);
         System.out.println( data[i][3] + ": " 
                           + sdf.format( now ) );
      }

      // Here are GMT and PST from en_US locale
      System.out.println();
      System.out.println("Setting default locale to en_US, then using the following ID's...");
      Locale.setDefault( new Locale("en", "US") );
      SimpleDateFormat df = (SimpleDateFormat)DateFormat.getDateTimeInstance(
                        DateFormat.LONG, DateFormat.LONG, Locale.getDefault() );
      df.setTimeZone( GMTZone.getTimeZone("GMT") );
      System.out.println("GMT: " + df.format( now ) );

      df.setTimeZone (GMTZone.getTimeZone("ECT") );
      System.out.println("ECT: " + df.format( now) );

      df.setTimeZone( GMTZone.getTimeZone("PST") );
      System.out.println("PST: " + df.format( now ) );
   }
}
