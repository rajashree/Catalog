/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.jobs;

import java.util.Collection;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.dell.acs.FeedUtil;
import com.dell.acs.managers.TransferProductManager;
import com.dell.acs.managers.model.ProductValidationStatus;
import com.dell.acs.persistence.domain.UnvalidatedProduct;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.dell.acs.stats.CountStat;
import com.dell.acs.stats.CountStatMutator;
import com.dell.acs.stats.StatUtil;
import com.sourcen.core.config.ConfigurationServiceImpl;
import com.sourcen.core.jobs.AbstractJob;

/**
 * @author Shawn R Fisk
 * @author $LastChangedBy
 * @version $Revision
 */
public final class TransferProductSliderJob extends AbstractJob {

	private static final String STATUS_COMPLETED = "COMPLETED";
	private static final String ERROR = "ERROR";
	private static final String EXCEED_MAX_EXECUTORS = "EXCEED_MAX_EXECUTORS";

	private static volatile int currentDataImportExecutors = 0;
	private static volatile int maxExecutors = 8;

	static {
		maxExecutors = ConfigurationServiceImpl.getInstance()
				.getIntegerProperty(FeedPreprocessorJob.class, "maxExecutors",
						Runtime.getRuntime().availableProcessors());
	}

	/**
	 * @param context
	 */
	@Override
	protected void executeJob(final JobExecutionContext context) {
		CountStatMutator count = null;

		try {
			count = (CountStatMutator) StatUtil.getInstance().getStat(
					CountStat.class, "TransferProductSliderJob.Count");
			count.inc();
			count.apply();
			if (currentDataImportExecutors < maxExecutors) {
				currentDataImportExecutors++;
				UnvalidatedProduct product = null;
				try {
					Set<String> retailerSiteNames = FeedUtil
							.getRetailerSiteRestriction(configurationService);
					Collection<Long> retailerSiteIds = retailerSiteRepository
							.getByNameIds(retailerSiteNames);
					boolean done = false;
					while (!done) {
						if (transferProductManager
								.isReadyForProcessing(
										retailerSiteIds,
										new ProductValidationStatus[] {
												ProductValidationStatus.ETL_SLIDER_INQUEUE,
												ProductValidationStatus.ETL_SLIDER_PROCESSING,
												ProductValidationStatus.ETL_DELETION_INQUEUE,
												ProductValidationStatus.ERROR,
												ProductValidationStatus.INVALID })) {
							product = transferProductManager
									.getLatestUnvalidatedProductSlider(
											retailerSiteIds,
											ProductValidationStatus.ETL_SLIDER_INQUEUE,
											ProductValidationStatus.ETL_SLIDER_PROCESSING);
							if (product != null) {
								transferProductManager
										.moveSliderToProduction(product);
								context.getJobDetail()
										.getJobDataMap()
										.put(TransferProductSliderJob.STATUS_KEY,
												TransferProductSliderJob.STATUS_COMPLETED);
							} else {
								done = true;
								continue;
							}
						} else {
							done = true;
							continue;
						}
					}
				} catch (Exception e) {
					transferProductManager.acquireLock(product,
							ProductValidationStatus.ETL_SLIDER_PROCESSING
									.getDbValue(),
							ProductValidationStatus.ERROR.getDbValue());
					logger.warn(e.getMessage(), e);
					context.getJobDetail()
							.getJobDataMap()
							.put(TransferProductSliderJob.STATUS_KEY,
									TransferProductSliderJob.ERROR);
				} finally {
					currentDataImportExecutors--;
				}
			} else {
				logger.warn("reached max level of threads := " + maxExecutors
						+ " will check on next Job execution.");
				context.getJobDetail()
						.getJobDataMap()
						.put(FeedPreprocessorJob.STATUS_KEY,
								TransferProductSliderJob.EXCEED_MAX_EXECUTORS);
			}
		} finally {
			if (count != null) {
				count.clear();
				count.dec();
				count.apply();
			}
		}
	}

	@Autowired
	@Qualifier("hibernateSessionFactory")
	private SessionFactory sessionFactory;

	public void setSessionFactory(final SessionFactory pSessionFactory) {
		this.sessionFactory = pSessionFactory;
	}

	/**
	 * properties and corresponding setter() for value injection
	 */
	@Autowired
	protected TransferProductManager transferProductManager;

	public void setTransferProductManager(
			final TransferProductManager pTransferProductManager) {
		this.transferProductManager = pTransferProductManager;
	}

	/**
	 * RetailerSiteRepository bean injection.
	 */
	@Autowired
	private RetailerSiteRepository retailerSiteRepository;

	/**
	 * Setter for retailerSiteRepository
	 */
	public void setRetailerSiteRepository(
			final RetailerSiteRepository retailerSiteRepository) {
		this.retailerSiteRepository = retailerSiteRepository;
	}
}
