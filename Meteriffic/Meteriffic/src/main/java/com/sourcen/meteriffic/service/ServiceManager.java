/*
 * Copyright (C) 2007-2009 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 * 
 */
package com.sourcen.meteriffic.service;

/**
 * Created by Eclipse.
 * User : rajashreem
 * Date : Jun 9, 2009
 * Time : 7:59:11 PM
 */

public interface ServiceManager {
	
	public void start();

	public void restart();

	public void stop();

	public void init();

	public boolean isEnabled();

}
