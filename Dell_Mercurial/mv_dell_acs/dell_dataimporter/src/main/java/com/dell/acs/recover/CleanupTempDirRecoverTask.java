/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.recover;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.dell.acs.persistence.repository.UnvalidatedProductRepository;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.util.FileUtils;

/**
 * @author Shawn R Fisk.
 * @author $LastChangedBy
 * @version $Revision
 */
public final class CleanupTempDirRecoverTask implements RecoverTask {
	private static AtomicBoolean running = new AtomicBoolean(false);

	/**
	 * logger
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private int _status;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Transactional
	@Override
	public void run() {
		this._status = CleanupTempDirRecoverTask.SUCCESSFUL;
		if (!running.get()) {
			running.set(true);

			try {
				File tempDir = configurationService.getFileSystem().getTempDirectory();
				
				if (tempDir.exists()) {
					FileUtils.deleteDirectory(tempDir);
				}
			} catch (Exception e) {
				logger.warn(e.getMessage() != null ? e.getMessage()
						: "Null pointer exception", e);
				this._status = CleanupTempDirRecoverTask.ERROR;
			}

			running.set(false);
		} else {
			logger.warn("The clean up temp directory recover task is already running, skip!");
			this._status = CleanupTempDirRecoverTask.ALREADY_RUNNING;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dell.acs.recover.RecoverTask#getStatus()
	 */
	@Override
	public int getStatus() {
		return this._status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.context.ApplicationContextAware#setApplicationContext
	 * (org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
	}

	/**
	 * ConfigurationService bean injection.
	 */
	@Autowired
	protected ConfigurationService configurationService;

	public void setConfigurationService(
			final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	@Autowired
	private UnvalidatedProductRepository unvalidatedProductRepository;

	public void setUnvalidatedProductRepository(
			final UnvalidatedProductRepository pUnvalidatedProductRepository) {
		this.unvalidatedProductRepository = pUnvalidatedProductRepository;
	}
}
