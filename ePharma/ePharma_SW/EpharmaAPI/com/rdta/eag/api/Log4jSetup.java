/*
 * Created on Apr 23, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.rdta.eag.api;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * @author ajoshi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Log4jSetup {

	static Logger debuglogger = Logger.getLogger("Log4jSetup");
	
    public static void initLog4J(String fileName) {
        //config log4j
        try {
        URL url = //new URL("http://localhost:8080/axis/api_log.xml");
            Log4jSetup.class.getResource(fileName);
        
        
       
        System.out.println("URL    " + url.toString());
	    DOMConfigurator.configure(url);


        if (!isLoggerConfigured())
            System.out.println("*********Logger did not initialize.*******");

         debuglogger.debug("Debug is on.  " );
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This code checks to see if there are any appenders defined for log4j
     */
    public static boolean isLoggerConfigured() {
        Enumeration enu = Logger.getRoot().getAllAppenders();
        if (!(enu instanceof org.apache.log4j.helpers.NullEnumeration)) {
            return true;
        } else {
            Enumeration loggers = LogManager.getCurrentLoggers();
            while (loggers.hasMoreElements()) {
                Logger l = (Logger) loggers.nextElement();
                if (!(l.getAllAppenders() instanceof org.apache.log4j.helpers.NullEnumeration))
                    return true;
            }
        }
        return false;
    }	
	
	public static void main(String[] args) {
	}
}
