package com.sourcen.microsite.service;

/*
 * Revision: 1.0
 * Date: October 25, 2008
 *
 * Copyright (C) 2005 - 2008 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 *
 * By : Chandra Shekher
 *
 */
import com.sourcen.microsite.model.EmailMessage;

public interface EmailManager extends ServiceManager {

	void send(EmailMessage message);

	void send(String body, String subject, String from, String to);

}
