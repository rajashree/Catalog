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
public interface ServiceManager {

	public void init();

	public boolean enable();

	public void start();

	public void stop();

	public void restart();

}
