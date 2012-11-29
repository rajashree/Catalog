package com.sourcen.space.service;

import com.sourcen.space.model.User;

/*
 * A base class for all other service manager
 * All other service manager need to implment these basic functions 
 */
public interface ServiceManager {

	/*
	 * Start The Service
	 */
	public void start();

	/*
	 * Restart the Service
	 */
	public void restart();

	/*
	 * Stop The Service
	 */
	public void stop();

	/*
	 * Initialize The Service Manager
	 */
	public void init();

	/*
	 * Service is enabled/disabled
	 */
	public boolean isEnabled();

	

}
