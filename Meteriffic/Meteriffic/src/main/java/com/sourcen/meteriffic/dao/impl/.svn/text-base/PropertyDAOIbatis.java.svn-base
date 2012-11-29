/*
 * Copyright (C) 2007-2009 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 * 
 */
package com.sourcen.meteriffic.dao.impl;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.sourcen.meteriffic.dao.PropertyDAO;
import com.sourcen.meteriffic.model.Property;

/**
 * Created by Eclipse.
 * User : rajashreem
 * Date : Jun 9, 2009
 * Time : 4:53:32 PM
 */

public class PropertyDAOIbatis extends SqlMapClientDaoSupport implements PropertyDAO{

	public void deleteProperty(String key) {
		getSqlMapClientTemplate().delete("deleteProperty",key);
	}

	@SuppressWarnings("unchecked")
	public List<Property> getAllProperty() {
		return getSqlMapClientTemplate().queryForList("getAllProperty");
	}

	public Property getProperty(String key) {
		return (Property) getSqlMapClientTemplate().queryForObject("getProperty",key);
	}

	public void saveProperty(Property property) {
		getSqlMapClientTemplate().insert("saveProperty",property);
		
	}

	public void updateProperty(Property property) {
		getSqlMapClientTemplate().update("updateProperty",property);
		
	}

}
