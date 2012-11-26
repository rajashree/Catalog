/***********************************************************************
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
************************************************************************/
package com;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtility {

    private static final String INIT_DIR = "TLDocs/InitDocs/";

    

	
	/**
	 * Return init data files for the specified collection.
	 *  
	 * @param colName
	 * @return
	 */
	public static List getInitDataFiles(String dName,String colName) {
		
		System.out.println(dName+"   "+colName);
		return getFilesFromDir(INIT_DIR +dName+"/"+colName);
	}
	
	 
	
	
	/**
	 * Return init data files for the specified collection.
	 *  
	 * @param colName
	 * @return
	 */
	public static List getInitDataFiles(String colName) {
		
		return getFilesFromDir(INIT_DIR+colName);
	}
	

	
	/**
	 * Return init data files for the specified collection.
	 *  
	 * @param colName - collection name
	 * @param file name - name of the file
	 * @return
	 */
	public static File getInitDataFile(String colName, String fileName) {
		
		return getFileFromDir(INIT_DIR+colName, fileName);
	}
	
	
	
    /**
     * Utility function for retirving files from a given Directory/Folder.
     * 
     * @param dirName :
     *            The name of the Directory/Folder from which the files need to
     *            be fetched.
     * @return Reurns a list contianing all the files found the particular
     *         Directory/Folder.
     */
    public static List getFilesFromDir(String dirName) {
    	System.out.println("Directory is "+dirName);
		 List list = new ArrayList();
		 
        try {
            if (dirName != null) {
                File sourceDir = new File(dirName);
                File[] files = sourceDir.listFiles();
                for (int i = files.length - 1; i >= 0; --i) {
                    File cf = files[i];
                    if (cf.isFile() && cf.canRead()) {
                         list.add(cf);
					}
                }
            }

        } catch (Exception e) {
			e.printStackTrace();
        }
		
		return list;

    }

    /**
     * Utility function for retirving a particular file from a given
     * Directory/Folder.
     * 
     * @param dirName -
     *            The name of the Directory/Folder from which the file need to
     *            be fetched.
     * @param fileName -
     *            The name of the file which needs to be fetched.
     * @return
     */
    public static File getFileFromDir(String dirName, String fileName) {
		
		 File requiredFile = null;
		 if ((dirName != null) && (fileName != null)) {
			  requiredFile =  getFile(dirName + File.separator + fileName);
		 }
		 
		 return requiredFile;
    }
	
	
	/**
	 * Returns file object from speciifed path.
	 * 
	 * @param filePath
	 * @return
	 */
	public static File getFile(String filePath){
		
		 File requiredFile = null;
		 if(filePath != null) {
		      File cf =  new File(filePath);
              if (cf.isFile() && cf.canRead()) {
				  requiredFile = cf;
               }
		 }
     	return requiredFile;
	}
	
	/**
	 * Return file name without extension.
	 * 
	 * @param file
	 * @return String
	 */
	public static String getFileNameWithOutExtension(File file) {
		
		String fileName = file.getName();
		int index = fileName.lastIndexOf(".");
		if(index > 0)
			return fileName.substring(0, index);
		else
			return fileName;
	}
	
	
	/**
	 * Return true if file extansion is XML.
	 * 
	 * @param file
	 * @return boolean - true
	 */
	public static boolean isXMLExtension(File file) {
		
		String fileName = file.getName();
		int index = fileName.lastIndexOf(".");
		if(index > 0) {
			String ext = fileName.substring(index+1, fileName.length());
			//System.out.println("File Extension:  " + ext );
			if(ext != null && ext.trim().equalsIgnoreCase("xml")){
				return true;
			} 
		}
		
		return false;
	}
	

}