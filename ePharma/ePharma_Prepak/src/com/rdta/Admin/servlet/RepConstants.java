/*
 * Created on Jul 14, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
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

 
package com.rdta.Admin.servlet;

import java.lang.reflect.Field;

/**
 * @author johnson joseph
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RepConstants {
	
	public static final String SESSION_VALID = "VALID";
	public static final String SUBMIT_TYPE_SAVE = "Save";
	public static final String SUBMIT_TYPE_EDIT = "ED";
	public static final String SUBMIT_TYPE_NEW = "New";
	
	public static final String DB_INSERT = "INSERT";
	public static final String DB_UPDATE = "UPDATE";
	public static final String LIST_FORMS_KEY = "LocForm";
	public static final String check="";
	public static String APPL_PATH="";
	public static final String LOC_FORMS_KEY = "LocationForm";
	public static final String LOC_DEVICE_KEY = "LocationDeviceForm";
	public static final String LOC_DEVICE_KEY1 = "LocationDeviceForm";
	
	public static final String LOC_CONTACTS_KEY = "LocContactsForm";
	public static final String PRODUCT_FORMS_KEY = "ProductsForm";
	public static final String PRD_THRESHOLD_KEY = "ThresholdForm";
	public static final String PRD_INVENTORY_KEY = "InventoryForm";
	public static final String BUILD_GROUP_KEY = "RepositoryBLForm";
	public static final String EDIT_REPOS_DET = "EditRepositoryForm";
	public static final String EVENT_FORMS_KEY = "EventForm";
	public static final String FCDETAILS_FORMS_KEY = "FCDetailsInfoForm";
	public static final String VOCABULARY_FORMS_KEY = "CategoryForm";
	
	public static final String CATEGORY_LOCATION_TYPE = "LocationType";
	public static final String CATEGORY_SYS_EVENTS_TYPE = "SysEvents";
	public static final String CATEGORY_SYS_EVENTS_TERM_TYPE = "SysEvents";
	public static final String CATEGORY_PRODUCT_CATEGORY_TYPE = "ProductCategory";
	public static final String CATEGORY_PRODUCT_TAG_TYPE = "TagType";
	public static final String CATEGORY_PRODUCT_UOM_TYPE = "UOM";
	
	public static final String ACCESS_INSERT = "Insert";
	public static final String ACCESS_UPDATE = "Update";
	public static final String ACCESS_DELETE = "Delete";
	public static final String ACCESS_READ = "Read";
	
	public static final String ACCESS_VALID = "true";
	public static final String ADMIN_MODULE_ID = "1.0";
	
	
	public static final String USER_FORMS_KEY = "UserForm";
	public static final String GROUP_FORMS_KEY = "GroupForm";
	
	public static final String ADMIN_LOCATION = "1.01";
	public static final String ADMIN_MAPS = "1.02";
	public static final String ADMIN_PRODUCTS = "1.03";
	public static final String ADMIN_DEVICES = "1.04";
	public static final String ADMIN_FILTERS = "1.05";
	public static final String ADMIN_REPOSITORIES = "1.06";
	public static final String ADMIN_USERS = "1.07";
	public static final String ADMIN_GROUPS = "1.08";
	public static final String ADMIN_EVENTS = "1.09";
	public static final String ADMIN_FCDETAILS = "1.10";	
	public static final String ADMIN_VOCABULARIES = "1.11";
	
	public static final String ADMIN_SYSCONFIG = "1.12";
	public static final String ADMIN_REPORT = "1.13";
	public static final String ADMIN_MONITORING = "1.14";
	public static final String ADMIN_DB_PROCEDURES = "1.15";
	public static final String ADMIN_LOGIN_DETAILS = "1.16";
	
	public static String  getProperty(String module){		
		Class c = RepConstants.class;		
		try{
			Field f = c.getField(module);	
			return (String) f.get(null);
		}catch(Exception e){
			System.out.println(e);
		}
		return null;
	}
	
	
	
	
}
