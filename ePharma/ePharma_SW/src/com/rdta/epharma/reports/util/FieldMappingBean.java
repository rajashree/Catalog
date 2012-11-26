/*
 * Created on Aug 15, 2005
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

package com.rdta.epharma.reports.util;

/**
 * @author mgawbhir
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FieldMappingBean {
	
	private String fieldName;
	private String key;
	private String xPath;
	private String cubeName;
	

	/**
	 * @return Returns the cubeName.
	 */
	public String getCubeName() {
		return cubeName;
	}
	/**
	 * @param cubeName The cubeName to set.
	 */
	public void setCubeName(String cubeName) {
		this.cubeName = cubeName;
	}
	/**
	 * @return Returns the fieldName.
	 */
	public String getFieldName() {
		return fieldName;
	}
	/**
	 * @param fieldName The fieldName to set.
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	/**
	 * @return Returns the key.
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key The key to set.
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * @return Returns the xPath.
	 */
	public String getXPath() {
		return xPath;
	}
	/**
	 * @param path The xPath to set.
	 */
	public void setXPath(String path) {
		xPath = path;
	}
}
