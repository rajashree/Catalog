/**
 * 
 */
package com.dell.acs.managers;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dell.acs.dataimport.DataImportService;
import com.dell.acs.dataimport.model.ValidatorContext;
import com.dell.acs.dataimport.validators.DataImportValidator;
import com.dell.acs.managers.model.ProductImageCache;
import com.dell.acs.managers.model.ProductValidationStatus;
import com.dell.acs.persistence.domain.UnvalidatedProduct;
import com.dell.acs.persistence.domain.UnvalidatedProductImage;
import com.dell.acs.persistence.repository.UnvalidatedProductImageRepository;
import com.dell.acs.persistence.repository.UnvalidatedProductRepository;
import com.dell.acs.stats.CountMinMaxStat;
import com.dell.acs.stats.CountStatMutator;
import com.dell.acs.stats.StatUtil;
import com.dell.acs.stats.TimerStat;
import com.dell.acs.stats.TimerStatMutator;
import com.sourcen.core.config.ConfigurationService;

/**
 * @author Shawn_Fisk
 * 
 */
@Service
public class ValidateProductManagerImpl extends ManagerBase implements ValidateProductManager {
	private static final Logger logger = LoggerFactory
			.getLogger(ValidateProductManagerImpl.class);

	/**
	 * Constructor
	 */
	public ValidateProductManagerImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.managers.ValidateProductManager#getLatestUnvalidatedProduct
	 * (java.util.Collection,
	 * com.dell.acs.managers.model.ProductValidationStatus)
	 */
	@Transactional
	@Override
	public UnvalidatedProduct getLatestUnvalidatedProduct(
			Collection<Long> retailerSiteIds,
			ProductValidationStatus currentStatus,
			ProductValidationStatus nextStatus) {
		UnvalidatedProduct product = unvalidatedProductRepository
				.getLastestUnvalidatedProduct(retailerSiteIds, currentStatus);
		if (product != null) {
			UnvalidatedProduct lockedProduct = unvalidatedProductRepository
					.acquireLock(product, currentStatus.getDbValue(),
							nextStatus.getDbValue(), this.configurationService.getApplicationUrl());

			// acquire lock.
			if (lockedProduct == null) {
				//logger.info("Unable to lock object from " + currentStatus.getLabel() + " TO " + nextStatus.getLabel() + " objID:="
				//		+ product.getId());
				return null;
			}
			lockedProduct.getRetailerSite(); // load the lazy object for future
												// use.
			lockedProduct.getRetailerSite().getRetailer();
			lockedProduct.getDataFile();
			
			return lockedProduct;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.managers.ValidatedProductManager#validateProduct(com.dell
	 * .acs.persistence.domain.UnvalidatedProduct)
	 */
	@Transactional
	@Override
	public void validateProduct(UnvalidatedProduct product) {
		CountStatMutator count = null;
		TimerStatMutator timing = null;
		ValidatorContext context = new ValidatorContext(product.getDataFile(),
				product.getDataFileRow());
		try {
			count = (CountStatMutator) StatUtil.getInstance().getStat(
					CountMinMaxStat.class, "ValidateProduct.Count");
			count.inc();
			count.apply();
			timing = (TimerStatMutator) StatUtil.getInstance().getStat(
					TimerStat.class, "ValidateProduct.Timer");
			timing.start();

			if (product.getStatus() != ProductValidationStatus.PROCESSING
					.getDbValue()) {
				return;
			}
			
			this.getDataFileStatisticService().processValidation(product); 

			List<DataImportValidator> validators = this.dataImportService
					.getValidators();

			for (DataImportValidator validator : validators) {
				if (validator.isSupport(product)) {
					validator.validate(context, product);
				}
			}

			if (context.isValid()) {
				this.getDataFileStatisticService().endValidation(product, context); 
				this.getDataFileStatisticService().startImages(product); // Start Validation time
				product.setStatus(ProductValidationStatus.WAITING_IMAGES.getDbValue());
				unvalidatedProductRepository.update(product);
			} else if (context.isFatal()) {
				this.getDataFileStatisticService().endValidation(product, context); 
				product.setStatus(ProductValidationStatus.ERROR.getDbValue());
				unvalidatedProductRepository.update(product);
			} else {
				this.getDataFileStatisticService().endValidation(product, context); 
				product.setStatus(ProductValidationStatus.INVALID.getDbValue());
				unvalidatedProductRepository.update(product);
			}
		} catch (Throwable t) {
			this.getDataFileStatisticService().validationHandleException(product, t); 
			product.setStatus(ProductValidationStatus.ERROR.getDbValue());
			unvalidatedProductRepository.update(product);
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
	 * com.dell.acs.managers.ValidateProductManager#validateProductImages(com
	 * .dell.acs.persistence.domain.UnvalidatedProduct)
	 */
	@Transactional
	@Override
	public void validateProductImages(UnvalidatedProduct product) {
		Session session = this.getSession();

		session.refresh(product);
		
		CountStatMutator count = null;
		TimerStatMutator timing = null;
		
		try {
			count = (CountStatMutator) StatUtil.getInstance().getStat(
					CountMinMaxStat.class, "ValidateProduct.Count");
			count.inc();
			count.apply();
			timing = (TimerStatMutator) StatUtil.getInstance().getStat(
					TimerStat.class, "ValidateProduct.Timer");
			timing.start();

			if (product.getStatus() != ProductValidationStatus.VALIDATING_IMAGES
					.getDbValue()) {
				return;
			}

			this.getDataFileStatisticService().processImages(product);

			boolean valid = true;
			boolean downloadImages = false;

			for (UnvalidatedProductImage upi : product.getImages()) {
				try {
					if (upi.getCached() != ProductImageCache.ERROR.getDbValue()) {
						if (upi.getImageURLExists()) {
							continue;
						} else {
				            File imageFile = configurationService.getFileSystem().getFile(upi.getImageURL(), false, false);
	
							if (imageFile.exists()) {
								upi.setImageURLExists(true);
								unvalidatedProductImageRepository.update(upi);
							} else {
								downloadImages = true;
							}
						}
					} else {
						valid = false;
			    		ValidatorContext context = new ValidatorContext(product.getDataFile(),
			    				product.getDataFileRow());
						this.getDataFileStatisticService().downloadImageFailed(product, upi, context); 
					}
				} catch (Throwable t) {
					valid = false;
					this.getDataFileStatisticService().imagesHandleError(product, upi);
					logger.error(String.format(
							"Failed to validate exists of image with URL: %s",
							upi.getImageURL()), t);
				}
			}
			
			product.setHost(null);
			this.getDataFileStatisticService().endImages(product);
			
			if (downloadImages == true) {
				product.setStatus(ProductValidationStatus.WAITING_DOWNLOAD_IMAGES.getDbValue());
			} else { 
				if (valid) {
					this.getDataFileStatisticService().startTransfer(product);
					
					product.setStatus(ProductValidationStatus.DONE.getDbValue());
				} else {
					product.setStatus(ProductValidationStatus.INVALID.getDbValue());
				}
			}
			unvalidatedProductRepository.update(product);
		} finally {
			if (timing != null) {
				timing.stop();
				timing.apply();
			}
		}
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

	@Autowired
	private UnvalidatedProductRepository unvalidatedProductRepository;

	public void setUnvalidatedProductRepository(
			final UnvalidatedProductRepository pUnvalidatedProductRepository) {
		this.unvalidatedProductRepository = pUnvalidatedProductRepository;
	}

	@Autowired
	private UnvalidatedProductImageRepository unvalidatedProductImageRepository;

	public void setUnvalidatedProductImageRepository(
			final UnvalidatedProductImageRepository pUnvalidatedProductImageRepository) {
		this.unvalidatedProductImageRepository = pUnvalidatedProductImageRepository;
	}

	@Autowired
	private DataImportService dataImportService;

	public void setDataImportService(final DataImportService pDataImportService) {
		this.dataImportService = pDataImportService;
	}
}
