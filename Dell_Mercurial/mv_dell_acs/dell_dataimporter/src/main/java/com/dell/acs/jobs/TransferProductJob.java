/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.jobs;

import com.dell.acs.FeedUtil;
import com.dell.acs.managers.TransferProductManager;
import com.dell.acs.managers.model.ProductValidationStatus;
import com.dell.acs.persistence.domain.UnvalidatedProduct;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.dell.acs.stats.CountStat;
import com.dell.acs.stats.CountStatMutator;
import com.dell.acs.stats.StatUtil;
import com.sourcen.core.jobs.AbstractJob;
import org.hibernate.SessionFactory;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Shawn R Fisk
 * @author $LastChangedBy
 * @version $Revision
 */
public final class TransferProductJob extends AbstractJob {

	private static final String STATUS_COMPLETED = "COMPLETED";
	private static final String ERROR = "ERROR";
	private static final String EXCEED_MAX_EXECUTORS = "EXCEED_MAX_EXECUTORS";
	private static AtomicBoolean running = new AtomicBoolean(false);
	private static volatile int maxExecutors = 1;

	/**
	 * @param context
	 */
	@Override
	protected void executeJob(final JobExecutionContext context) {
		CountStatMutator count = null;

		try {
			count = (CountStatMutator) StatUtil.getInstance().getStat(
					CountStat.class, "ETLProductJob.Count");
			count.inc();
			count.apply();
			if (running.compareAndSet(false, true)) {
				UnvalidatedProduct product = null;
				try {
					boolean done = false;
					while (!done) {
						Set<String> retailerSiteNames = FeedUtil
								.getRetailerSiteRestriction(configurationService);
						Collection<Long> retailerSiteIds = retailerSiteRepository
								.getByNameIds(retailerSiteNames);
						product = transferProductManager
								.getLatestUnvalidatedProductWithProperties(retailerSiteIds,
										ProductValidationStatus.DONE,
										ProductValidationStatus.ETL_PROCESSING);
						if (product != null) {
							product = transferProductManager.moveToProduction(product);
							context.getJobDetail()
									.getJobDataMap()
									.put(TransferProductJob.STATUS_KEY,
											TransferProductJob.STATUS_COMPLETED);
						} else {
							done = true;
							continue;
						}
					}
				} catch (Exception e) {
					transferProductManager
							.acquireLock(product,
									ProductValidationStatus.ETL_PROCESSING
											.getDbValue(),
									ProductValidationStatus.ERROR.getDbValue());
					logger.warn(e.getMessage(), e);
					context.getJobDetail()
							.getJobDataMap()
							.put(TransferProductJob.STATUS_KEY,
									TransferProductJob.ERROR);
				} finally {
					running.compareAndSet(true, false);
				}
			} else {
				logger.warn("reached max level of threads := " + maxExecutors
						+ " will check on next Job execution.");
				context.getJobDetail()
						.getJobDataMap()
						.put(FeedPreprocessorJob.STATUS_KEY,
								TransferProductJob.EXCEED_MAX_EXECUTORS);
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
