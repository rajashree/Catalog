/*
 * Copyright (C) 2007-2009 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 * 
 */
package com.sourcen.meteriffic.service;

import com.sourcen.meteriffic.model.EmailMessage;

/**
 * Created by Eclipse.
 * User : rajashreem
 * Date : Jun 9, 2009
 * Time : 8:06:51 PM
 */

public interface EmailManager extends ServiceManager{
	
	void send(EmailMessage message);

}
