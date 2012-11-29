/**
 * 
 */
package com.dell.acs.managers;

import com.dell.acs.dataimport.DataImportService;
import com.dell.acs.managers.model.ProductValidationStatus;
import com.dell.acs.persistence.domain.*;
import com.dell.acs.persistence.repository.*;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.util.collections.PropertiesProvider;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;

/**
 * @author Shawn_Fisk
 * 
 */
@Service
public class TransferProductManagerImpl extends ManagerBase implements
		TransferProductManager {
	private static final Logger logger = LoggerFactory
			.getLogger(TransferProductManagerImpl.class);

	/**
	 * Constructor
	 */
	public TransferProductManagerImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.managers.ETLProductManager#getLatestUnvalidatedProduct(java
	 * .util.Collection, com.dell.acs.managers.model.ProductValidationStatus,
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
							nextStatus.getDbValue(),
							configurationService.getApplicationUrl());

			// acquire lock.
			if (lockedProduct == null) {
				// logger.info("Unable to lock object from " +
				// currentStatus.getLabel() + " TO " + nextStatus.getLabel() +
				// " objID:="
				// + product.getId());
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


    @Transactional
    @Override
    public UnvalidatedProduct getLatestUnvalidatedProductWithProperties(Collection<Long> retailerSiteIds, ProductValidationStatus currentStatus, ProductValidationStatus nextStatus) {
        UnvalidatedProduct product = unvalidatedProductRepository
                .getLastestUnvalidatedProductWithProperties(retailerSiteIds, currentStatus);
        if (product != null) {
            UnvalidatedProduct lockedProduct = unvalidatedProductRepository
                    .acquireLock(product, currentStatus.getDbValue(),
                            nextStatus.getDbValue(),
                            configurationService.getApplicationUrl());

            // acquire lock.
            if (lockedProduct == null) {
                // logger.info("Unable to lock object from " +
                // currentStatus.getLabel() + " TO " + nextStatus.getLabel() +
                // " objID:="
                // + product.getId());
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
	 * com.dell.acs.managers.ETLProductManager#moveToProduction(com.dell.acs
	 * .persistence.domain.UnvalidatedProduct)
	 */
	@Transactional
	@Override
	public UnvalidatedProduct moveToProduction(
			UnvalidatedProduct unvalidateProduct) {
		if (logger.isDebugEnabled()) {
			logger.debug("Thread " + Thread.currentThread().getId()
					+ " start processing product " + unvalidateProduct.getId());
		}
		try {
			Session session = this.getSession();

            this.unvalidatedProductRepository.refresh(unvalidateProduct);

			Product product = this.getProduct(unvalidateProduct
					.getUpdateProductId());

			this.getDataFileStatisticService().processTransfer(
					unvalidateProduct);

			if (product == null) {
				product = new Product();
			}

			this.transferState(unvalidateProduct, product);

			this.productRepository.put(product);

			unvalidateProduct.setUpdateProductId(product.getId());

			Collection<UnvalidatedProductImage> upis = unvalidateProduct
					.getImages();

			for (UnvalidatedProductImage upi : upis) {
				ProductImage productImage = getProductImage(upi
						.getUpdateProductImageId());

				if (productImage == null) {
					if (!uniquenessCheck(product, upi)) {
						throw new RuntimeException("Failed to transfer product because non-unique product image!");
					}
					productImage = new ProductImage();
					productImage.setProduct(product);
				}

				transferState(upi, productImage);

				this.productImageRepository.put(productImage);

				upi.setUpdateProductImageId(productImage.getId());

				this.unvalidatedProductImageRepository.update(upi);
			}

			Collection<UnvalidatedProductReview> uprs = this
					.getUnvalidateProductReviews(unvalidateProduct);

			if (uprs != null) {
				for (UnvalidatedProductReview upr : uprs) {
					ProductReview productReview = getProductReview(upr
							.getUpdateProductReviewId());

					if (productReview == null) {
						if (!uniquenessCheck(product, upr)) {
							throw new RuntimeException("Failed to transfer product because non-unique product review!");
						}
						productReview = new ProductReview();
						productReview.setProduct(product);
					}

					transferState(upr, productReview);

					this.productReviewRepository.put(productReview);

					upr.setUpdateProductReviewId(productReview.getId());

					this.unvalidatedProductReviewRepository.update(upr);
				}
			}

			unvalidateProduct
					.setStatus(ProductValidationStatus.ETL_SLIDER_INQUEUE
							.getDbValue());

			unvalidateProduct.setHost(null);
			this.unvalidatedProductRepository.update(unvalidateProduct);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("Thread " + Thread.currentThread().getId()
						+ " done processing product "
						+ unvalidateProduct.getId());
			}
		}
		return unvalidateProduct;
	}

	private boolean uniquenessCheck(Product product, UnvalidatedProductImage upi) {
		boolean result = true;
		Session session = this.getSession();

		Criteria uniquenessCriteria = session
				.createCriteria(ProductImage.class);
		uniquenessCriteria.add(Restrictions.eq("product", product));
		uniquenessCriteria.add(Restrictions.eq("imageName", upi.getImageName()));
		uniquenessCriteria.setProjection(Projections.rowCount());

		Long rowCount = (Long) uniquenessCriteria.uniqueResult();

		if (rowCount > 0) {
			result = false;
			this.handleException(upi.getProduct().getDataFile(), upi.getProduct().getDataFileRow(), 
					"Duplicate product image found for product: %d, iamge name: %s",
					product.getId(), upi.getImageName());
		}
		
		return result;
	}

	private boolean uniquenessCheck(Product product, UnvalidatedProductReview upr) {
		boolean result = true;
		Session session = this.getSession();

		Criteria uniquenessCriteria = session
				.createCriteria(ProductReview.class);
		uniquenessCriteria.add(Restrictions.eq("product", product));
		uniquenessCriteria.add(Restrictions.eq("title", upr.getTitle()));
		uniquenessCriteria.add(Restrictions.eq("name", upr.getName()));
		uniquenessCriteria.add(Restrictions.eq("location", upr.getLocation()));
		uniquenessCriteria.setProjection(Projections.rowCount());

		Long rowCount = (Long) uniquenessCriteria.uniqueResult();

		if (rowCount > 0) {
			result = false;
			this.handleException(upr.getProduct().getDataFile(), upr.getProduct().getDataFileRow(), 
					"Duplicate product review found for product: %d, title: %s, name : %s, and location %s",
					product.getId(), upr.getTitle(), upr.getName(),
					upr.getLocation());
		}
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.managers.TransferProductManager#moveSliderToProduction(com
	 * .dell.acs.persistence.domain.UnvalidatedProduct)
	 */
	@Transactional
	@Override
	public UnvalidatedProduct moveSliderToProduction(
			UnvalidatedProduct unvalidateProduct) {
		Session session = this.getSession();

		this.unvalidatedProductRepository.refresh(unvalidateProduct);

		this.getDataFileStatisticService().processTransferSlider(
				unvalidateProduct);

		// Slider has cross references, so it must be moved to it own phase.
		Collection<UnvalidatedProductSlider> upss = this
				.getUnvalidateProductSliders(unvalidateProduct);

		if (upss != null) {
			for (UnvalidatedProductSlider ups : upss) {
				ProductSlider productSlider = getProductSlider(ups
						.getUpdateProductSliderId());

				Product sourceProduct = this.getProduct(ups
						.getSourceProduct().getUpdateProductId());
				Product targetProduct = this.getProduct(ups
						.getTargetProduct().getUpdateProductId());

				if (productSlider == null) {
					if (!uniquenessCheck(ups, sourceProduct, targetProduct)) {
						throw new RuntimeException("Failed to transfer product because non-unique product slider!");
					}
					productSlider = new ProductSlider();
				}

				transferState(ups, productSlider);

				this.productSliderRepository.put(productSlider);

				ups.setUpdateProductSliderId(productSlider.getId());

				this.unvalidatedProductSliderRepository.update(ups);
			}
		}

		unvalidateProduct
				.setStatus(ProductValidationStatus.ETL_DELETION_INQUEUE
						.getDbValue());

		unvalidateProduct.setHost(null);
		this.unvalidatedProductRepository.update(unvalidateProduct);

		return unvalidateProduct;
	}

	private boolean uniquenessCheck(UnvalidatedProductSlider ups,
			Product sourceProduct, Product targetProduct) {
		boolean result = true;
		Session session = this.getSession();

		Criteria uniquenessCriteria = session
				.createCriteria(ProductSlider.class);
		uniquenessCriteria.add(Restrictions.eq("sourceProduct", sourceProduct));
		uniquenessCriteria.add(Restrictions.eq("targetProduct", targetProduct));
		uniquenessCriteria.setProjection(Projections.rowCount());

		Long rowCount = (Long) uniquenessCriteria.uniqueResult();

		if (rowCount > 0) {
			result = false;
			this.handleException(ups.getSourceProduct().getDataFile(), ups.getSourceProduct().getDataFileRow(), 
					"Duplicate product slider found for source product: %d, target product: %d",
					sourceProduct.getId(), targetProduct.getId());
		}
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dell.acs.managers.TransferProductManager#
	 * getLatestUnvalidatedProductSlider(java.util.Collection,
	 * com.dell.acs.managers.model.ProductValidationStatus,
	 * com.dell.acs.managers.model.ProductValidationStatus)
	 */
	@Transactional
	@Override
	public UnvalidatedProduct getLatestUnvalidatedProductSlider(
			Collection<Long> retailerSiteIds,
			ProductValidationStatus currentStatus,
			ProductValidationStatus nextStatus) {
		UnvalidatedProduct product = unvalidatedProductRepository
				.getLastestUnvalidatedProduct(retailerSiteIds, currentStatus);
		if (product != null) {
			UnvalidatedProduct lockedProduct = unvalidatedProductRepository
					.acquireLock(product, currentStatus.getDbValue(),
							nextStatus.getDbValue(),
							configurationService.getApplicationUrl());

			// acquire lock.
			if (lockedProduct == null) {
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
	 * com.dell.acs.managers.TransferProductManager#cleanup(com.dell.acs.persistence
	 * .domain.UnvalidatedProduct)
	 */
	@Transactional
	@Override
	public void cleanup(UnvalidatedProduct unvalidatedProduct) {
		Session session = this.getSession();

		this.unvalidatedProductRepository.refresh(unvalidatedProduct);

		int row = unvalidatedProduct.getDataFileRow();

		Collection<UnvalidatedProductImage> upis = unvalidatedProduct
				.getImages();

		Collection<UnvalidatedProductReview> uprs = this
				.getUnvalidateProductReviews(unvalidatedProduct);

		// Slider has cross references, so it must be moved to it own phase.
		Collection<UnvalidatedProductSlider> upss = this
				.getUnvalidateProductSliders(unvalidatedProduct);

		// Slider must be moved to another phase,
		// Delete un-validated stuff!
		for (UnvalidatedProductImage upi : upis) {
			session.delete(upi);
		}
		if (uprs != null) {
			for (UnvalidatedProductReview upr : uprs) {
				session.delete(upr);
			}
		}
		if (upss != null) {
			for (UnvalidatedProductSlider ups : upss) {
				session.delete(ups);
			}
		}

		unvalidatedProduct.setStatus(ProductValidationStatus.ETL_DELETED
                .getDbValue());

        this.unvalidatedProductRepository.remove(unvalidatedProduct);

		this.getDataFileStatisticService().endTransfer(
				unvalidatedProduct.getDataFile(), row);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.managers.TransferProductManager#isReadyForProcessing(java
	 * .util.Collection, com.dell.acs.managers.model.ProductValidationStatus[])
	 */
	@Transactional
	@Override
	public boolean isReadyForProcessing(Collection<Long> retailerSiteIds,
			ProductValidationStatus[] productValidationStatus) {
		boolean result = unvalidatedProductRepository.isReadyForProcessing(
				retailerSiteIds, productValidationStatus);

		return result;
	}

	@Transactional
	@Override
	public void acquireLock(UnvalidatedProduct product, Integer currentStatus,
			Integer nextStatus) {
		this.unvalidatedProductRepository.acquireLock(product, currentStatus,
				nextStatus, this.configurationService.getApplicationUrl());
	}

	@Override
	@Transactional
	public void rollbackStatus(UnvalidatedProduct unvalidatedProduct,
			Integer currentStatus, Integer nextStatus) {
		Session session = this.getSession();

		this.unvalidatedProductRepository.refresh(unvalidatedProduct);

		if (unvalidatedProduct.getStatus() == currentStatus) {
			unvalidatedProduct.setHost(null);
			unvalidatedProduct.setStatus(nextStatus);
			this.unvalidatedProductRepository.update(unvalidatedProduct);
		}
	}

	@Override
	@Transactional
	public void setAllTransferDone(Collection<Long> retailerSiteIds) {
		this.unvalidatedProductRepository.setAllTransferDone(retailerSiteIds);
	}

   private Product getProduct(Long productId) {
		if (productId == null) {
			return null;
		}
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Product.class);
		criteria.add(Restrictions.idEq(productId));
		criteria.setMaxResults(1);

		@SuppressWarnings("rawtypes")
		List result = criteria.list();
		if (result == null) {
			return null;
		} else if (result.size() == 0) {
			return null;
		} else if (result.size() == 1) {
			return (Product) result.iterator().next();
		} else {
			return null;
		}
	}

	private ProductImage getProductImage(Long productImageId) {
		if (productImageId == null) {
			return null;
		}
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(ProductImage.class);
		criteria.add(Restrictions.idEq(productImageId));
		criteria.setMaxResults(1);

		@SuppressWarnings("rawtypes")
		List result = criteria.list();
		if (result == null) {
			return null;
		} else if (result.size() == 0) {
			return null;
		} else if (result.size() == 1) {
			return (ProductImage) result.iterator().next();
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private Collection<UnvalidatedProductReview> getUnvalidateProductReviews(
			UnvalidatedProduct unvalidateProduct) {
		Session session = this.getSession();
		Criteria criteria = session
				.createCriteria(UnvalidatedProductReview.class);
		session.refresh(unvalidateProduct);
		criteria.add(Restrictions.eq("product", unvalidateProduct));

		@SuppressWarnings("rawtypes")
		List result = criteria.list();
		if (result == null) {
			return null;
		} else {
			return (Collection<UnvalidatedProductReview>) result;
		}
	}

	private ProductReview getProductReview(Long productReviewId) {
		if (productReviewId == null) {
			return null;
		}
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(ProductReview.class);
		criteria.add(Restrictions.idEq(productReviewId));
		criteria.setMaxResults(1);

		@SuppressWarnings("rawtypes")
		List result = criteria.list();
		if (result == null) {
			return null;
		} else if (result.size() == 0) {
			return null;
		} else if (result.size() == 1) {
			return (ProductReview) result.iterator().next();
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private Collection<UnvalidatedProductSlider> getUnvalidateProductSliders(
			UnvalidatedProduct unvalidateProduct) {
		Session session = this.getSession();
		Criteria criteria = session
				.createCriteria(UnvalidatedProductSlider.class);
		session.refresh(unvalidateProduct);
		criteria.add(Restrictions.eq("sourceProduct", unvalidateProduct));

		@SuppressWarnings("rawtypes")
		List result = criteria.list();
		if (result == null) {
			return null;
		} else {
			return (Collection<UnvalidatedProductSlider>) result;
		}
	}

	private ProductSlider getProductSlider(Long productSliderId) {
		if (productSliderId == null) {
			return null;
		}
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(ProductSlider.class);
		criteria.add(Restrictions.idEq(productSliderId));
		criteria.setMaxResults(1);

		@SuppressWarnings("rawtypes")
		List result = criteria.list();
		if (result == null) {
			return null;
		} else if (result.size() == 0) {
			return null;
		} else if (result.size() == 1) {
			return (ProductSlider) result.iterator().next();
		} else {
			return null;
		}
	}

	private void transferState(UnvalidatedProduct unvalidateProduct,
			Product product) {
		transferAll(unvalidateProduct, product);

		// Deal with properties
		PropertiesProvider pp = product.getProperties();
		PropertiesProvider upp = unvalidateProduct.getProperties();

		for (String key : upp.keySet()) {
			pp.setProperty(key, upp.getProperty(key));
		}

		// Deal with taxonomy
		TaxonomyCategory root = null;

		final Taxonomy taxonomy = this.taxonomyRepository.getTaxonomy(
				product.getRetailerSite(), "product");

		String[] productCategories = new String[6];
		searchAndInsert(unvalidateProduct.getCategory1(), productCategories, 0);
		searchAndInsert(unvalidateProduct.getCategory2(), productCategories, 1);
		searchAndInsert(unvalidateProduct.getCategory3(), productCategories, 2);
		searchAndInsert(unvalidateProduct.getCategory4(), productCategories, 3);
		searchAndInsert(unvalidateProduct.getCategory5(), productCategories, 4);
		searchAndInsert(unvalidateProduct.getCategory6(), productCategories, 5);

		if (taxonomy != null) {
			root = this.taxonomyCategoryRepository.getRootCategory(taxonomy);
		}

		TaxonomyCategory parent = root;
		for (int i = 0; i < productCategories.length; i++) {

			if (parent == null) {
				throw new RuntimeException("parent category cannot be null.");
			}
			String categoryName = productCategories[i];

			if (categoryName != null) {
				TaxonomyCategory category = this.taxonomyCategoryRepository
						.getCategory(taxonomy, parent, categoryName);
				if (category == null) {
					category = new TaxonomyCategory(categoryName);
					category.setTaxonomy(taxonomy);
					category.setParent(parent);
					category.setDepth(parent.getDepth() + 1);
					taxonomyCategoryRepository.insert(category);
				}

				// re assign parent for loop
				parent = category;
			} else {
				break;
			}
		}
		if (parent != null) {
			product.setCategory(parent);
		} else {
			throw new RuntimeException("Unable to set category for product :="
					+ product.getProductId());
		}
	}

	private void searchAndInsert(String category,
			final String[] productCategories, final int i) {
		if (category != null) {
			productCategories[i] = category.toString().trim();
			if (productCategories[i].length() == 0) {
				productCategories[i] = null;
			}
		}
	}

	private void transferState(UnvalidatedProductImage unvalidateProductImage,
			ProductImage productImage) {
		transferAll(unvalidateProductImage, productImage);
	}

	private void transferState(
			UnvalidatedProductReview unvalidateProductReview,
			ProductReview productReview) {
		transferAll(unvalidateProductReview, productReview);
	}

	private void transferState(
			UnvalidatedProductSlider unvalidateProductSlider,
			ProductSlider productSlider) {
		transferAll(unvalidateProductSlider, productSlider);

		Product sourceProduct = this.getProduct(unvalidateProductSlider
				.getSourceProduct().getUpdateProductId());
		Product targetProduct = this.getProduct(unvalidateProductSlider
				.getTargetProduct().getUpdateProductId());

		productSlider.setSourceProduct(sourceProduct);
		productSlider.setTargetProduct(targetProduct);
	}

	private void transferAll(Object source, Object target) {
		Class<?> sourceType = source.getClass();
		Class<?> targetType = target.getClass();
		Field[] sourceFields = sourceType.getDeclaredFields();
		for (Field sourceField : sourceFields) {
			Class<?> sourceFieldType = sourceField.getType();

			if (!this.validType(sourceFieldType)) {
				continue;
			}
			Method sourceGetterMethod = this.getGetterMethod(sourceType,
					sourceField);
			if (sourceGetterMethod == null) {
				// Skip, no way to get the value.
				continue;
			}
			Method targetSetterMethod = this.getSetterMethod(targetType,
					sourceField.getName(), sourceField.getType());
			if (targetSetterMethod == null) {
				// Skip, no way to set the value.
				continue;
			}

			try {
				targetSetterMethod.invoke(target,
						sourceGetterMethod.invoke(source));
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}

	private boolean validType(Class<?> type) {
		// Must skip collections, if not, then we will have two domain object
		// pointing to the same collection and hibernate will not be happy!
		if (type.isAssignableFrom(Collection.class)) {
			return false;
		} else {
			return true;
		}
	}

	private Method getGetterMethod(Class<?> type, Field field) {
		String name = field.getName();
		// Skip transient fields.
		if (Modifier.isTransient(field.getModifiers())) {
			return null;
		}
		String methodName = "get" + Character.toUpperCase(name.charAt(0))
				+ name.substring(1);
		try {
			return type.getMethod(methodName);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		}

		return null;
	}

	private Method getSetterMethod(Class<?> type, String name,
			Class<?> fieldType) {
		String methodName = "set" + Character.toUpperCase(name.charAt(0))
				+ name.substring(1);
		try {
			return type.getMethod(methodName, fieldType);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		}

		return null;
	}


	protected void handleException(DataFile dataFile, int row, Throwable t, String msg, Object... args) {
		this.getDataFileStatisticService().transportHandleError(dataFile, row, t, String.format(msg, args));
	}

	protected void handleException(DataFile dataFile, int row, String msg, Object... args) {
		this.handleException(dataFile, row, null, msg, args);
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
	private DataImportDataFileRepository dataImportDataFileRepository;

	public void setDataImportDataFileRepository(
			final DataImportDataFileRepository pDataImportDataFileRepository) {
		this.dataImportDataFileRepository = pDataImportDataFileRepository;
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
    private UnvalidatedProductReviewRepository unvalidatedProductReviewRepository;

    public void setUnvalidatedProductReviewRepository(
            final UnvalidatedProductReviewRepository pUnvalidatedProductReviewRepository) {
        this.unvalidatedProductReviewRepository = pUnvalidatedProductReviewRepository;
    }

    @Autowired
    private UnvalidatedProductSliderRepository unvalidatedProductSliderRepository;

    public void setUnvalidatedProductSliderRepository(
            final UnvalidatedProductSliderRepository pUnvalidatedProductSlierRepository) {
        this.unvalidatedProductSliderRepository = pUnvalidatedProductSlierRepository;
    }

    @Autowired
    private ProductRepository productRepository;

    public void setProductRepository(
            final ProductRepository pProductRepository) {
        this.productRepository = pProductRepository;
    }

    @Autowired
    private ProductImageRepository productImageRepository;

    public void setProductImageRepository(
            final ProductImageRepository pProductImageRepository) {
        this.productImageRepository = pProductImageRepository;
    }

    @Autowired
    private ProductReviewRepository productReviewRepository;

    public void setProductReviewRepository(
            final ProductReviewRepository pProductReviewRepository) {
        this.productReviewRepository = pProductReviewRepository;
    }

    @Autowired
    private ProductSliderRepository productSliderRepository;

    public void setProductSliderRepository(
            final ProductSliderRepository pProductSliderRepository) {
        this.productSliderRepository = pProductSliderRepository;
    }

    @Autowired
	private DataImportService dataImportService;

	public void setDataImportService(final DataImportService pDataImportService) {
		this.dataImportService = pDataImportService;
	}

	@Autowired
	private TaxonomyRepository taxonomyRepository;

	public void setTaxonomyRepository(TaxonomyRepository taxonomyRepository) {
		this.taxonomyRepository = taxonomyRepository;
	}

	@Autowired
	private TaxonomyCategoryRepository taxonomyCategoryRepository;

	public void setTaxonomyCategoryRepository(
			TaxonomyCategoryRepository taxonomyCategoryRepository) {
		this.taxonomyCategoryRepository = taxonomyCategoryRepository;
	}
}
