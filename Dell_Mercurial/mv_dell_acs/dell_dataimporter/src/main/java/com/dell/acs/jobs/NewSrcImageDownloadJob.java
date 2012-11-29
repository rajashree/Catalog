package com.dell.acs.jobs;

import java.util.Collection;
import java.util.Set;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.dell.acs.FeedUtil;
import com.dell.acs.managers.ImageManager;
import com.dell.acs.managers.model.ProductValidationStatus;
import com.dell.acs.persistence.domain.UnvalidatedProductImage;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.dell.acs.stats.CountMinMaxStat;
import com.dell.acs.stats.CountStat;
import com.dell.acs.stats.CountStatMutator;
import com.dell.acs.stats.StatUtil;
import com.dell.acs.stats.TimerStat;
import com.dell.acs.stats.TimerStatMutator;
import com.sourcen.core.config.ConfigurationServiceImpl;
import com.sourcen.core.jobs.AbstractJob;

/**
 * @author Shawn R Fisk
 * @author $LastChangedBy
 * @version $Revision
 */
public class NewSrcImageDownloadJob extends AbstractJob {

	private static final Logger logger = Logger
			.getLogger(NewSrcImageDownloadJob.class);
	private static final String EXCEED_MAX_EXECUTORS = "EXCEED_MAX_EXECUTORS";
	private static volatile int currentDataImportExecutors = 0;
	private static volatile int maxExecutors = 8;

	static {
		maxExecutors = ConfigurationServiceImpl.getInstance()
				.getIntegerProperty(ValidateProductImagesJob.class,
						"maxExecutors",
						Runtime.getRuntime().availableProcessors());
	}

	@Override
	protected void executeJob(JobExecutionContext context) {
		CountStatMutator count = null;
		CountStatMutator imageCount = null;
		TimerStatMutator imageTimer = null;
		try {
			count = (CountStatMutator) StatUtil.getInstance().getStat(
					CountStat.class, "NewSrcImageDownloadJob.Count");
			count.inc();
			count.apply();
			if (currentDataImportExecutors < maxExecutors) {
				currentDataImportExecutors++;
				try {
					Set<String> retailerSiteNames = FeedUtil
							.getRetailerSiteRestriction(configurationService);
					Collection<Long> retailerSiteIds = retailerSiteRepository
							.getByNameIds(retailerSiteNames);

					boolean done = false;
					while(!done) {
						UnvalidatedProductImage productImage = imageManager
								.getLatestUnresolvedImage(retailerSiteIds, ProductValidationStatus.WAITING_DOWNLOAD_IMAGES, ProductValidationStatus.PROCESS_DOWNLOAD_IMAGES);
						if (productImage != null) {
							imageCount = (CountStatMutator) StatUtil.getInstance()
									.getStat(CountMinMaxStat.class,
											"NewSrcImageDownloadJob.ImageCount");
							imageCount.incBy(1);
							imageTimer = (TimerStatMutator) StatUtil.getInstance()
									.getStat(TimerStat.class,
											"NewSrcImageDownloadJob.ImageTimer");
							imageTimer.start();
	
							if (productImage.getImageURLExists()) {
								continue;
							}
							
							imageManager.downloadImage(productImage);
						} else {
							done = true;
							continue;
						}
					}
				} finally {
					currentDataImportExecutors--;
				}
			} else {
				logger.warn("reached max level of threads := " + maxExecutors
						+ " will check on next Job execution.");
				context.getJobDetail()
						.getJobDataMap()
						.put(FeedPreprocessorJob.STATUS_KEY,
								NewSrcImageDownloadJob.EXCEED_MAX_EXECUTORS);
			}
		} catch (IllegalArgumentException illArgsExcp) {
			logger.error(illArgsExcp.getMessage());
		} catch (Exception e) {
			logger.error("Exception :- " + e.getMessage() + e);
		}

		finally {
			if (imageTimer != null) {
				imageTimer.stop();
				imageTimer.apply();
			}
			if (imageCount != null) {
				imageCount.apply();
			}
			if (count != null) {
				count.clear();
				count.dec();
				count.apply();
			}
		}
	}

	// private void downloadImage(String downloadImageUrl, File destinationFile)
	// throws Exception {
	// URL srcUrl = UrlUtils.buildUri(downloadImageUrl).toURL();
	// FileUtils.copyURLToFile(srcUrl, destinationFile);
	// }

	@Autowired
	private ImageManager imageManager;

	public void setImageManager(ImageManager imageManager) {
		this.imageManager = imageManager;
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
