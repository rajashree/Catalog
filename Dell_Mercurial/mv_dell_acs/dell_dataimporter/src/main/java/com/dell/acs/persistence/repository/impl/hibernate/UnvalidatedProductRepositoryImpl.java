/**
 * 
 */
package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.managers.DataImportManager.FileStatus;
import com.dell.acs.managers.model.ProductImageCache;
import com.dell.acs.managers.model.ProductValidationStatus;
import com.dell.acs.persistence.domain.*;
import com.dell.acs.persistence.repository.UnvalidatedProductRepository;
import com.sourcen.core.persistence.domain.constructs.ThreadLockAware;
import com.sourcen.core.persistence.repository.impl.hibernate.PropertiesAwareRepositoryImpl;
import com.sourcen.core.util.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Shawn_Fisk
 * 
 */
@Repository
public class UnvalidatedProductRepositoryImpl extends
		PropertiesAwareRepositoryImpl<UnvalidatedProduct> implements
		UnvalidatedProductRepository {

	/**
	 * 
	 */
	public UnvalidatedProductRepositoryImpl() {
		super(UnvalidatedProduct.class, UnvalidatedProductProperty.class);
	}

	@Override
	public UnvalidatedProduct getLastestUnvalidatedProduct(
			Collection<Long> retailerSiteIds,
			ProductValidationStatus currentStatus) {
		Session session = this.getSession();

		Criteria criteria = session.createCriteria(UnvalidatedProduct.class);
		if (retailerSiteIds.size() > 0) {
			criteria.add(Restrictions.in("retailerSite.id", retailerSiteIds));
		}
		criteria.add(Restrictions.eq("status", currentStatus.getDbValue()));
		criteria.setMaxResults(1);

		@SuppressWarnings("rawtypes")
		List result = criteria.list();
		if (result == null) {
			return null;
		} else if (result.size() == 0) {
			return null;
		} else if (result.size() == 1) {
			return (UnvalidatedProduct) result.iterator().next();
		}

		return null;
	}

    @Override
    public UnvalidatedProduct getLastestUnvalidatedProductWithProperties(Collection<Long> retailerSiteIds, ProductValidationStatus currentStatus) {
        Session session = this.getSession();

        Criteria criteria = session.createCriteria(UnvalidatedProduct.class);
        if (retailerSiteIds.size() > 0) {
            criteria.add(Restrictions.in("retailerSite.id", retailerSiteIds));
        }
        criteria.add(Restrictions.eq("status", currentStatus.getDbValue()));
        criteria.setMaxResults(1);

        @SuppressWarnings("rawtypes")
        List result = criteria.list();
        if (result == null) {
            return null;
        } else if (result.size() == 0) {
            return null;
        } else if (result.size() == 1) {
            UnvalidatedProduct unvalidatedProduct = (UnvalidatedProduct) result.iterator().next();
            this.loadProperties(unvalidatedProduct);

            return unvalidatedProduct;
        }

        return null;
    }

    /*
      * (non-Javadoc)
      *
      * @see com.dell.acs.persistence.repository.UnvalidatedProductRepository#
      * getUnvalidateProductByDataFile(com.dell.acs.persistence.domain.DataFile)
      */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<UnvalidatedProduct> getUnvalidateProductByDataFile(
			DataFile dataFile) {
		Session session = this.getSession();

		Criteria criteria = session.createCriteria(UnvalidatedProduct.class);

		criteria.add(Restrictions.eq("dataFile", dataFile));

		return criteria.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dell.acs.persistence.repository.UnvalidatedProductRepository#
	 * isReadyForProcessing(java.util.Collection,
	 * com.dell.acs.managers.model.ProductValidationStatus[])
	 */
	@Override
	public boolean isReadyForProcessing(Collection<Long> retailerSiteIds,
			ProductValidationStatus[] statuses) {
		Session session = this.getSession();

		Criteria countCriteria = session
				.createCriteria(UnvalidatedProduct.class);
		if (retailerSiteIds.size() > 0) {
			countCriteria.add(Restrictions.in("retailerSite.id",
					retailerSiteIds));
		}
		Collection<Integer> dbStatuses = new ArrayList<Integer>();
		for (ProductValidationStatus status : statuses) {
			dbStatuses.add(status.getDbValue());
		}

		countCriteria.add(Restrictions.not(Restrictions
				.in("status", dbStatuses)));
		countCriteria.setProjection(Projections.rowCount());

		Long count = (Long) countCriteria.list().iterator().next();

		if (count == 0) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dell.acs.persistence.repository.UnvalidatedProductRepository#
	 * getRecoverProducts(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<UnvalidatedProduct> getRecoverProducts(String host) {
		Criteria criteria = getSession().createCriteria(
				UnvalidatedProduct.class);
		criteria.add(Restrictions.eq("host", host));

		return (Collection<UnvalidatedProduct>) criteria.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.persistence.repository.UnvalidatedProductRepository#acquireLock
	 * (com.dell.acs.persistence.domain.UnvalidatedProduct, java.lang.Integer,
	 * java.lang.Integer, java.lang.String)
	 */
	@Override
	public UnvalidatedProduct acquireLock(UnvalidatedProduct product,
			Integer currentStatus, Integer nextStatus, String host) {
		// TODO sfisk 2012/08/23 - Need to clean this update, add support for
		// StatusAware to include host.
		// TODO - replace with HQL so that we can do update dataFile SET
		// status=? WHERE id=? AND status=?
		// so that we get an absolute DB level lock. This will still work as we
		// are using VersionAware.
		try {
			Session session = getSession();
			UnvalidatedProduct freshObj = (UnvalidatedProduct) session.get(
					product.getClass(), product.getId());

			session.buildLockRequest(LockOptions.UPGRADE)
					.setLockMode(LockMode.PESSIMISTIC_WRITE).setTimeOut(10000)
					.lock(freshObj);
			if (freshObj.getStatus().equals(currentStatus)) {
				logger.debug("Updating status for id:=" + product.getId()
						+ " oldStatus :=" + currentStatus + " newStatus:="
						+ nextStatus);
				freshObj.setHost(host);
				freshObj.setStatus(nextStatus);
				if (freshObj instanceof ThreadLockAware) {
					String threadId = DateUtils.JVM_START_TIME_UTC + "-"
							+ Thread.currentThread().getId();
					((ThreadLockAware) freshObj).setLockedThread(threadId);
				}
				session.update(freshObj);
				session.flush();
				return onFindForObject(freshObj);
			}
			return null;
		} catch (Exception e) {
			logger.error("unable to lock dataFile :=" + product.getId());
		}
		return null;
	}

	@Override
	public UnvalidatedProduct getLastestUnvalidatedProductWithImages(
			Collection<Long> retailerSiteIds,
			ProductValidationStatus currentStatus) {
		Session session = this.getSession();
		
		Criteria productCriteria = session.createCriteria(UnvalidatedProduct.class);
		if (retailerSiteIds.size() > 0) {
			productCriteria.add(Restrictions.in("retailerSite.id",
					retailerSiteIds));
		}
		productCriteria.add(Restrictions.eq("status",
				currentStatus.getDbValue()));
		productCriteria.setProjection(Projections.rowCount());
		
		Long rowCount = (Long)productCriteria.uniqueResult(); 
		
		if (rowCount > 0) {
			Criteria criteria = session
					.createCriteria(UnvalidatedProductImage.class);
			productCriteria = criteria.createCriteria("product");
			productCriteria.add(Restrictions.eq("status",
					currentStatus.getDbValue()));
			if (retailerSiteIds.size() > 0) {
				productCriteria.add(Restrictions.in("retailerSite.id",
						retailerSiteIds));
			}
			criteria.add(Restrictions.eq("imageURLExists", false));
			criteria.add(Restrictions.ne("cached",
					ProductImageCache.ERROR.getDbValue()));
			criteria.addOrder(Order.asc("retryCount"));
			criteria.setMaxResults(1);
	
			@SuppressWarnings("rawtypes")
			List result = criteria.list();
			if (result == null) {
				return null;
			} else if (result.size() == 0) {
				return null;
			} else if (result.size() == 1) {
				return ((UnvalidatedProductImage) result.iterator().next())
						.getProduct();
			}
		}
		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dell.acs.persistence.repository.UnvalidatedProductRepository#
	 * getUnvalidateProductByDataFiles(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<UnvalidatedProduct> getUnvalidateProductByDataFiles(
			List<DataFile> dataFiles) {
		Session session = this.getSession();

		Criteria criteria = session.createCriteria(UnvalidatedProduct.class);

		criteria.add(Restrictions.in("dataFile", dataFiles));

		return criteria.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dell.acs.persistence.repository.UnvalidatedProductRepository#
	 * setAllTransferDone(java.util.Collection)
	 */
	@Override
	public void setAllTransferDone(Collection<Long> retailerSiteIds) {
		Session session = this.getSession();

		Criteria dataFileCriteria = session.createCriteria(DataFile.class);
		dataFileCriteria.add(Restrictions.eq("status", FileStatus.READY_TO_VALIDATE));
		dataFileCriteria.add(Restrictions.in("retailerSite.id", retailerSiteIds));
		
		@SuppressWarnings("unchecked")
		List<DataFile> dataFiles = (List<DataFile>)dataFileCriteria.list();
		for(DataFile dataFile : dataFiles) {
			
			Criteria productCriteria = session.createCriteria(UnvalidatedProduct.class);
			productCriteria.add(Restrictions.eq("dataFile", dataFile));
			productCriteria.add(Restrictions.not(Restrictions.in("status", ProductValidationStatus.COMPLETE_STATUS_LIST)));
			productCriteria.setProjection(Projections.rowCount());
			
			Long rowCount = (Long)productCriteria.uniqueResult();
			
			if (rowCount == 0) {
                dataFile.setEndDate(new Date());
				dataFile.setStatus(FileStatus.TRANSFER_DONE);
				session.update(dataFile);


                Criteria dataFileStatCriteria = session.createCriteria(DataFileStatistic.class);
                dataFileStatCriteria.add(Restrictions.eq("dataFile_id", dataFile.getId()));

                List<DataFileStatistic> stats = dataFileStatCriteria.list();

                DataFileStatisticSummary summary = new DataFileStatisticSummary();
                summary.setDataFile_id(dataFile.getId());
                summary.setSrcFile(dataFile.getSrcFile());
                summary.setRetailerSite_id(dataFile.getRetailerSite().getId());

                for(DataFileStatistic stat : stats) {
                    Criteria dataFileErrorCriteria = session.createCriteria(DataFileError.class);
                    dataFileErrorCriteria.add(Restrictions.eq("dataFileStat_id", stat.getId()));
                    dataFileErrorCriteria.setProjection(Projections.rowCount());

                    Long errorCount = (Long)dataFileErrorCriteria.uniqueResult();

                    summary.add(stat, errorCount);
                }

                session.saveOrUpdate(summary);
            }
		}
	}
}
