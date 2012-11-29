/**
 * 
 */
package com.dell.acs.managers;

import com.dell.acs.managers.DataImportManager.FileStatus;
import com.dell.acs.managers.model.ProductImageCache;
import com.dell.acs.managers.model.ProductValidationStatus;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.domain.UnvalidatedProduct;
import com.dell.acs.persistence.domain.UnvalidatedProductImage;
import com.dell.acs.persistence.repository.DataFileRepository;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.dell.acs.persistence.repository.UnvalidatedProductImageRepository;
import com.dell.acs.persistence.repository.UnvalidatedProductRepository;
import com.dell.acs.stats.*;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.util.Assert;
import com.sourcen.core.util.FileSystem;
import com.sourcen.core.util.UrlUtils;
import com.sourcen.core.util.ZipUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;

/**
 * @author Shawn_Fisk
 * 
 */
@Service
public class ImageManagerImpl extends ManagerBase implements ImageManager {
	private static final String successfulMsg = " : Downloaded successful and updated for Product Image  :- ";
	@SuppressWarnings("unused")
	private static final String failureMsg = " : Downloaded fails and updated for Error status for Product Image :- ";

	private static final Logger logger = LoggerFactory
			.getLogger(ImageManagerImpl.class);

	/**
	 * 
	 */
	public ImageManagerImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.managers.ImageManager#getLatestImagesDataFile(java.util.
	 * Collection)
	 */
	@Override
	public DataFile getLatestImagesDataFile(Collection<Long> retailerSiteIds) {
		DataFile dataFile = dataFileRepository
				.getLatestImagesDataFile(retailerSiteIds);
		if (dataFile != null) {
			DataFile lockedDataFile = dataFileRepository.acquireLock(dataFile,
					FileStatus.IN_QUEUE, FileStatus.PROCESSING);
			// acquire lock.
			if (lockedDataFile == null) {
				logger.info("Unable to lock object from IN_QUEUE TO PROCESSING  objID:="
						+ dataFile
						+ " as it was locked by "
						+ dataFile.getLockedThread());
				return null;
			}
			dataFile.getRetailerSite(); // load the lazy object for future use.
			dataFile.getRetailerSite().getRetailer();
			return lockedDataFile;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.managers.ImageManager#processImages(com.dell.acs.persistence
	 * .domain.DataFile)
	 */
	@Override
	public void processImages(DataFile dataFile) {
		CountStatMutator count = null;
		TimerStatMutator timing = null;
		try {
			count = (CountStatMutator) StatUtil.getInstance().getStat(
					CountMinMaxStat.class, "ImageManager.Count");
			count.inc();
			count.apply();
			timing = (TimerStatMutator) StatUtil.getInstance().getStat(
					TimerStat.class, "ImageManager.Timer");
			timing.start();

			/* productRepository.resetUpdateFlag(); */
			if (!dataFile.getStatus().equals(FileStatus.PROCESSING)) {
				logger.warn("dataFile was not locked for processing");
				return;
			}
			dataFile.setHost(this.configurationService.getApplicationUrl());
			dataFile.setStartTime(new Date());
			dataFile = dataFileRepository.atomicUpdate(dataFile);

			RetailerSite retailerSite = dataFile.getRetailerSite();
			FileSystem fileSystem = configurationService.getFileSystem();

			String retailerSiteCDNDir = FileSystemUtil.getPath(retailerSite,
					"cdn");
			fileSystem.getDirectory(retailerSiteCDNDir, true);

			String retailerSiteCDNImageDir = FileSystem
					.getSimpleFilename(retailerSiteCDNDir + "images/");
			File retailerSiteCDNImageDirectory = fileSystem.getDirectory(
					retailerSiteCDNImageDir, true);
			String zipFilePath = FilenameUtils.normalize(configurationService
					.getFileSystem().getFileSystemAsString()
					+ "/"
					+ dataFile.getFilePath());
			File zipFile = new File(zipFilePath);

			ZipUtils.extractZipFileWithStructure(zipFile,
					retailerSiteCDNImageDirectory.getAbsolutePath());

			dataFile.setCurrentRow(dataFile.getNumRows());
			dataFile.setHost(null);
			dataFile.setEndTime(new Date());
			dataFile = dataFileRepository.atomicUpdate(dataFile);

			dataFile = dataFileRepository.acquireLock(dataFile,
					FileStatus.PROCESSING, FileStatus.TRANSFER_DONE);
		} catch (Throwable t) {
			logger.error(String.format(
					"Unknown error while processing data file %d(%s)",
					dataFile.getId(), dataFile.getFilePath()), t);
			// Fatal error, mark entire data file as error.
			dataFile.setCurrentRow(dataFile.getNumRows());
			dataFile.setNumErrorRows(dataFile.getNumRows());
			dataFile.setEndTime(new Date());
			dataFile.setHost(null);
			dataFile = dataFileRepository.atomicUpdate(dataFile);

			dataFile = dataFileRepository.acquireLock(dataFile,
					FileStatus.PROCESSING, FileStatus.ERROR_EXTRACTING);
		} finally {
			if (timing != null) {
				timing.stop();
				timing.apply();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.managers.ImageManager#getLatestUnresolvedImage(java.util
	 * .Collection)
	 */
	@Override
	public UnvalidatedProductImage getLatestUnresolvedImage(
			Collection<Long> retailerSiteIds,
			ProductValidationStatus currentStatus,
			ProductValidationStatus nextStatus) {
		Session session = this.getSession();
		UnvalidatedProductImage result = null;
		try {
			UnvalidatedProduct product = this.unvalidatedProductRepository
					.getLastestUnvalidatedProductWithImages(retailerSiteIds,
							currentStatus);

			if (product == null) {
				return null;
			}
			
			UnvalidatedProduct lockedProduct = this.unvalidatedProductRepository
					.acquireLock(product, currentStatus.getDbValue(),
							nextStatus.getDbValue(),
							this.configurationService.getApplicationUrl());
			if (lockedProduct != null) {
				session.refresh(product);

				for (UnvalidatedProductImage upi : product.getImages()) {
					if (upi.getImageURLExists()) {
						continue;
					}

					if (upi.getCached() != ProductImageCache.ERROR.getDbValue()) {
						result = upi;
						break;
					}
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
			// Ignore, do not process by return null.
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.managers.ImageManager#downloadImage(com.dell.acs.persistence
	 * .domain.UnvalidatedProductImage)
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void downloadImage(UnvalidatedProductImage productImage) {
		Session session = this.getSession();
		
		session.refresh(productImage);

		if (productImage.getProduct().getStatus() != ProductValidationStatus.PROCESS_DOWNLOAD_IMAGES.getDbValue()) {
			return;
		}

		int maxRetryCount = configurationService.getIntegerProperty(
				ImageManager.class, productImage.getProduct().getRetailerSite()
						.getSiteName()
						+ ImageManager.MAX_RETRY_COUNT_KEY,
				ImageManager.DEFAULT_MAX_RETRY_COUNT);

		String srcUrl = productImage.getSrcImageURL();
		String destinationUrl = productImage.getImageURL();

		Assert.notNull(srcUrl.trim(), "Source Image Url is Null "
				+ productImage.getId());

		if (destinationUrl.contains("?")) {
			destinationUrl = destinationUrl.substring(0,
					destinationUrl.indexOf("?"));
		}

		try {
			File destinationFile = configurationService.getFileSystem()
					.getFile(destinationUrl, false, false);

			/* download the images and cache */
			if (!destinationFile.exists() || destinationFile.length() == 0) {
				/* downloadImage(srcUrl, destinationFile); */
				URL srcUrl1 = UrlUtils.buildUri(srcUrl).toURL();
				FileUtils.copyURLToFile(srcUrl1, destinationFile);
				this.updateDownloadedImage(productImage,
						ProductImageCache.DOWNLOADED, maxRetryCount);
				logger.debug(successfulMsg + productImage.getId());
			} else {
				logger.debug("Destination File exists, Updating Image cache :- "
						+ productImage.getId());
				this.updateDownloadedImage(productImage,
						ProductImageCache.DOWNLOADED, maxRetryCount);
			}
		} catch (MalformedURLException e) {
			// logger.error(failureMsg+productImage.getId()+" " + srcUrl);
			// update it to status error
			this.updateDownloadedImage(productImage, ProductImageCache.ERROR,
					maxRetryCount);
		} catch (UnsupportedEncodingException unSupEx) {
			// logger.error("Unsupported encoding. for Product image id :- "+
			// productImage.getId(), unSupEx);
			// logger.error(failureMsg+productImage.getId()+" " + srcUrl);
			// update it to status error
			this.updateDownloadedImage(productImage, ProductImageCache.ERROR,
					maxRetryCount);
		} catch (IOException ioEx) {
			// logger.error(failureMsg+productImage.getId()+" " + srcUrl);
			// update it to status error
			// logger.error("Unable to read/write/access the image for Product image id :- "+
			// productImage.getId(), ioEx);
			this.updateDownloadedImage(productImage, ProductImageCache.ERROR,
					maxRetryCount);
		} catch (Exception e) {
			// logger.error(failureMsg+productImage.getId()+" " + srcUrl);
			// logger.error("Unable to Download for Product image id :- "+
			// productImage.getId() + e.getMessage(), e);
			// update it to status error
			this.updateDownloadedImage(productImage, ProductImageCache.ERROR,
					maxRetryCount);
		}
	}

	private void updateDownloadedImage(UnvalidatedProductImage productImage,
			ProductImageCache cache, int maxRetryCount) {
		ProductValidationStatus pvs = ProductValidationStatus.UNKNOWN; // error state, should never be in this state at the end.
		productImage.setModifiedDate(new Date());
		if (cache == ProductImageCache.DOWNLOADED) {
			productImage.setImageURLExists(true);
			pvs = ProductValidationStatus.WAITING_IMAGES; // default, normal case, it will download.
		} else if (cache == ProductImageCache.ERROR) {
			productImage.setRetryCount(productImage.getRetryCount() + 1);
			if (productImage.getRetryCount() >= maxRetryCount) {
				productImage.setCached(cache.getDbValue());
				// Have retried, must not detail with the fact we can not download the image.
				pvs = ProductValidationStatus.WAITING_IMAGES;
			} else {
				pvs = ProductValidationStatus.WAITING_DOWNLOAD_IMAGES;
			}
		} else {
			// Unknown state, error out!
		}
		if (pvs == ProductValidationStatus.UNKNOWN) {
			throw new RuntimeException("Protocol errork, should not be in the UNKNOWN state, case missing above!");
		}
		this.unvalidatedProductImageRepository.update(productImage);
		// Let the validate do it job, double check it.
		UnvalidatedProduct product = productImage.getProduct();
		product.setStatus(pvs.getDbValue());
		this.unvalidatedProductRepository.update(product);
	}

	//
	// IoC
	//

	/**
	 * ApplicationContext bean injection.
	 */
	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * Setter for applicationContext
	 */
	public void setApplicationContext(
			final ApplicationContext pApplicationContext) {
		this.applicationContext = pApplicationContext;
	}

	/**
	 * DataFileRepository bean injection.
	 */
	@Autowired
	@Qualifier("dataFileRepositoryImpl")
	private DataFileRepository dataFileRepository;

	/**
	 * Setter for dataFileRepository
	 */
	public void setDataFileRepository(
			final DataFileRepository pDataFileRepository) {
		this.dataFileRepository = pDataFileRepository;
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
			final RetailerSiteRepository pRetailerSiteRepository) {
		this.retailerSiteRepository = pRetailerSiteRepository;
	}

	/**
	 * ConfigurationService bean injection.
	 */
	@Autowired
	private ConfigurationService configurationService;

	/**
	 * Setter for configurationService
	 */
	public void setConfigurationService(
			final ConfigurationService pConfigurationService) {
		this.configurationService = pConfigurationService;
	}

	/**
	 * UnvalidateProductRepository bean injection.
	 */
	@Autowired
	private UnvalidatedProductRepository unvalidatedProductRepository;

	/**
	 * Setter for unvalidatedProductImageRepository
	 */
	public void setUnvalidatedProductRepository(
			final UnvalidatedProductRepository pUnvalidatedProductRepository) {
		this.unvalidatedProductRepository = pUnvalidatedProductRepository;
	}

	/**
	 * UnvalidateProductImageRepository bean injection.
	 */
	@Autowired
	private UnvalidatedProductImageRepository unvalidatedProductImageRepository;

	/**
	 * Setter for unvalidatedProductImageRepository
	 */
	public void setUnvalidatedProductImageRepository(
			final UnvalidatedProductImageRepository pUnvalidatedProductImageRepository) {
		this.unvalidatedProductImageRepository = pUnvalidatedProductImageRepository;
	}
}
