/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.recover;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.dell.acs.managers.model.ProductValidationStatus;
import com.dell.acs.persistence.domain.UnvalidatedProduct;
import com.dell.acs.persistence.repository.UnvalidatedProductRepository;
import com.sourcen.core.config.ConfigurationService;

/**
 * @author Shawn R Fisk.
 * @author $LastChangedBy
 * @version $Revision
 */
public final class StuckProductRecoverTask implements RecoverTask {
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
		this._status = StuckProductRecoverTask.SUCCESSFUL;
		if (running.compareAndSet(false, true)) {
			try {
				Collection<UnvalidatedProduct> products = this.unvalidatedProductRepository.getRecoverProducts(configurationService
						.getApplicationUrl());

				for (UnvalidatedProduct product : products) {
					ProductValidationStatus pvs = ProductValidationStatus.lookup(product.getStatus());

					switch (pvs) {
						case UNKNOWN: {
							product.setStatus(ProductValidationStatus.IN_QUEUE.getDbValue());
							break;
						}
						case PROCESSING: {
							product.setStatus(ProductValidationStatus.IN_QUEUE.getDbValue());
							break;
						}
						case VALIDATING_IMAGES: {
							product.setStatus(ProductValidationStatus.WAITING_IMAGES.getDbValue());
							break;
						}
						case ETL_PROCESSING: {
							product.setStatus(ProductValidationStatus.DONE.getDbValue());
							break;
						}
						case ETL_SLIDER_PROCESSING: {
							product.setStatus(ProductValidationStatus.ETL_SLIDER_INQUEUE.getDbValue());
							break;
						}
						case ETL_DELETING: {
							product.setStatus(ProductValidationStatus.ETL_DELETION_INQUEUE.getDbValue());
							break;
						}
						default: {
							continue;
						}
					}
					product.setHost(null);
					
					this.unvalidatedProductRepository.update(product);
				}
			} catch (Exception e) {
				logger.warn(e.getMessage() != null ? e.getMessage()
						: "Null pointer exception", e);
				this._status = StuckProductRecoverTask.ERROR;
			}

			running.compareAndSet(true, false);
		} else {
			logger.warn("The pre-validated data import recover task is already running, skip!");
			this._status = StuckProductRecoverTask.ALREADY_RUNNING;
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
