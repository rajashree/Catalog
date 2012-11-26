/*
 * Created on Jun 10, 2005
 *
 */
/********************************************************************************

* Raining Data Corp.

*

* Copyright (c) Raining Data Corp. All Rights Reserved.

*

* This software is confidential and proprietary information belonging to

* Raining Data Corp. It is the property of Raining Data Corp. and is protected

* under the Copyright Laws of the United States of America. No part of this

* software may be copied or used in any form without the prior

* written permission of Raining Data Corp.

*

*********************************************************************************/

 

package com.rdta.commons;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.Enumeration;

import org.w3c.dom.Node;
import  com.rdta.commons.xml.XMLUtil;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;


/**
 * @author asangha
 *  
 */
public class CommonUtil {

	//don't use MiscFns.class.getName() in this class
    static Logger debuglogger = Logger.getLogger("CommonUtil");

	//this value is going to set when Log4j intialized
	private static String WEB_CONTEXT_PATH ;

	private static final String DBCONFIG_PATH = "dbconfig.xml";
	
	public static final String DATA_BASE_DATE_FORMAT = "yyyy-MM-dd";
	
	public static final String DATA_BASE_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss" ;
	
	private static final String UPLOAD_FILE_PATH = "uploadedFiles";

	public static String getWebInfConfPath() {
		return WEB_CONTEXT_PATH + "/WEB-INF/conf";
	}
	
	public static String getImageStoreLocation() {
		
		return WEB_CONTEXT_PATH + "/images";
	}
	
	public static String getUploadedFilePath() {
		
		return WEB_CONTEXT_PATH + "/uploadedFiles";
	}

    public static void initLog4J(String webContext, String fileName) {
        //config log4j
        if (webContext != null) {

			WEB_CONTEXT_PATH = webContext;
			System.out.println("WEB_CONTEXT_PATH :"+WEB_CONTEXT_PATH);
		    DOMConfigurator.configureAndWatch(webContext + fileName, 300000);
        } else {
            System.out.println("*********Logger did not initialize.*******");
        }

        if (!isLoggerConfigured())
            System.out.println("*********Logger did not initialize.*******");

         debuglogger.debug("Debug is on. webContext + " + WEB_CONTEXT_PATH);
    }

    /**
     * This code checks to see if there are any appenders defined for log4j
     */
    public static boolean isLoggerConfigured() {
        Enumeration enum = Logger.getRoot().getAllAppenders();
        if (!(enum instanceof org.apache.log4j.helpers.NullEnumeration)) {
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

	
    public static String getGUID() {
        GUID g = new GUID();
        return g.getGUID();
    }


	/**
	 * Returns Database configuration file object.
	 * 
	 * @return File.
	 */
	public static File getDatabaseConfigFile() {
		return getFileFromConfDir(DBCONFIG_PATH);
	}
	
	
	/**
	 * Returns speciifed directory or file name from the resource directory. 
	 * 
	 * 
	 * @param fileName
	 * @return
	 */
	public static File getFileFromConfDir(String fileName) {

		StringBuffer buf = new StringBuffer(getWebInfConfPath());
		buf.append(File.separator);
		buf.append(fileName);
		
		return getFile(buf.toString());
	}
	
	
	/**
	 * Returns file object from speciifed path.
	 * 
	 * @param filePath
	 * @return
	 */
	public static File getFile(String filePath){
		
		 File requiredFile = null;
		 
        try {
			
			 if(filePath != null) {
			      File cf =  new File(filePath);
	              if (cf.isFile() && cf.canRead()) {
					  requiredFile = cf;
	               }
			 }
   
        } catch (Exception e) {
			e.printStackTrace();
        }
		
		return requiredFile;
		
	}
	
	
	 public static String saveImageFile(InputStream stream) {
		 String fileName = getGUID();
		 return saveImageFile(stream,fileName );
	 }
	 
	
	 public static String saveImageFile(InputStream stream,String fileName) {
		 
		 if(stream == null)
			 return null;
		 
		 try {
			 
				String path  =  getImageStoreLocation() + File.separator +fileName ;
				FileOutputStream fos = new FileOutputStream(path);
				BufferedInputStream   buffer = new BufferedInputStream (stream);
				int k = 0 ;
				while( (k = buffer.read())> -1) {
					fos.write(k);
				}				
				fos.flush();
				fos.close();
				return fileName;
				
			} catch (java.io.IOException ie) {
				
				ie.printStackTrace();
			}
			
			return null;
	 }
	
	 
	 public static void saveUploadedFile(InputStream stream, String fileName) {
		 
		 if(stream == null || fileName == null )
			 return ;
		 
		 try {
			  	String path  =  getUploadedFilePath() + File.separator +fileName ;
				FileOutputStream fos = new FileOutputStream(path);
				BufferedInputStream   buffer = new BufferedInputStream (stream);
				int k = 0 ;
				while( (k = buffer.read())> -1) {
					fos.write(k);
				}				
				fos.flush();
				fos.close();
		 	} catch (java.io.IOException ie) {
				
				ie.printStackTrace();
			}
	
	 }
	
	
	public static boolean isNullOrEmpty(String str) {
		
		return str == null || str.trim().equalsIgnoreCase("") || str.trim().equalsIgnoreCase("null");
	}

	
	public static String returnEmptyIfNull(String str) {
		return (str == null) ? "" : str;
	}
	
	public static String jspDisplayValue(Node node, String xpath) {
			
		return returnEmptyIfNull(XMLUtil.getValue(node,xpath));
	}
	
	
	/**
	 * Returns string format of given date
	 * 
	 * @param date
	 * @return string format of date
	 */
	public static String dateToString(Date date) {
		return dateToString(date,DATA_BASE_DATE_FORMAT);
	}


   /**
    * Returns date object to string as per given format
    * 
    * @param date
    * @param format
    * @return
    */
	public static String dateToString(Date date, String format) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
		
	}
	
	/**
	 * Returns string format of given date
	 * 
	 * @param date
	 * @return string format of date
	 */
	public static String dateToTLString(Date date) {
		return dateToTLString(date,DATA_BASE_DATE_TIME_FORMAT);
	}


   /**
    * Returns date object to string as per given format
    * 
    * @param date
    * @param format
    * @return
    */
	public static String dateToTLString(Date date, String format) {
		
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			String dateTimeStamp =  dateFormat.format(date);
			String[] result = dateTimeStamp.split("\\s");
		    return result[0]+"T"+result[1];
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
}
