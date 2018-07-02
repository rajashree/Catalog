/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.utils;

import com.bmsils.gcn.persistence.domain.User;
import com.bmsils.gcn.web.beans.ProfileDataBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/11/12
 * Time: 3:03 PM
 * An EntityToBeanMapper is an utility for Entity to UI Beans mapper
 */

public class EntityToBeanMapper extends Mapper
{
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private static EntityToBeanMapper _instance;
	
	public static EntityToBeanMapper getInstance() {
		if(_instance == null)
			_instance = new EntityToBeanMapper();
		
		return _instance;
	}
	
	protected EntityToBeanMapper() {
		super();
	}

}
