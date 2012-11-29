/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.jobs;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.dell.acs.FeedUtil;
import com.dell.acs.managers.TransferProductManager;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.dell.acs.stats.CountStat;
import com.dell.acs.stats.CountStatMutator;
import com.dell.acs.stats.StatUtil;
import com.sourcen.core.jobs.AbstractJob;

/**
 * @author Shawn R Fisk
 * @author $LastChangedBy
 * @version $Revision
 */
public final class TransferDoneJob extends AbstractJob {

	private static final String STATUS_COMPLETED = "COMPLETED";
	private static final String ERROR = "ERROR";
	private static final String EXCEED_MAX_EXECUTORS = "EXCEED_MAX_EXECUTORS";
	private static AtomicBoolean running = new AtomicBoolean(false);

	/**
	 * @param context
	 */
	@Override
	protected void executeJob(final JobExecutionContext context) {
		CountStatMutator count = null;
		try {
			count = (CountStatMutator) StatUtil.getInstance().getStat(
					CountStat.class, "ValidateProductJob.Count");
			count.inc();
			count.apply();
			if (running.compareAndSet(false, true)) {
				try {
					Set<String> retailerSiteNames = FeedUtil
							.getRetailerSiteRestriction(configurationService);
					Collection<Long> retailerSiteIds = retailerSiteRepository
							.getByNameIds(retailerSiteNames);
					transferProductManager.setAllTransferDone(retailerSiteIds);
					context.getJobDetail()
					.getJobDataMap()
					.put(TransferDoneJob.STATUS_KEY,
							TransferDoneJob.STATUS_COMPLETED);
				} catch (Exception e) {
					logger.warn(e.getMessage(), e);
					context.getJobDetail()
							.getJobDataMap()
							.put(TransferDoneJob.STATUS_KEY,
									TransferDoneJob.ERROR);
				} finally {
					running.compareAndSet(true, false);
				}
			} else {
				logger.warn("reached max level of threads := 1 will check on next Job execution.");
				context.getJobDetail()
						.getJobDataMap()
						.put(TransferDoneJob.STATUS_KEY,
								TransferDoneJob.EXCEED_MAX_EXECUTORS);
			}
		} finally {
			if (count != null) {
				count.clear();
				count.dec();
				count.apply();
			}
		}
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
