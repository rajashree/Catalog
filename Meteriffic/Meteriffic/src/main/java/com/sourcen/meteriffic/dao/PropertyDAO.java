/*
 * Copyright (C) 2007-2009 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 * 
 */
package com.sourcen.meteriffic.dao;

import java.util.List;

import com.sourcen.meteriffic.model.Property;

/**
 * Created by Eclipse.
 * User : rajashreem
 * Date : Jun 9, 2009
 * Time : 4:51:01 PM
 */

public interface PropertyDAO {
	
	Property getProperty(String key);
	
	public void updateProperty(Property property);
	
	void saveProperty(Property property);
	
	public List<Property> getAllProperty();
	
	public void deleteProperty(String key);

}
