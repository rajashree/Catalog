/**
 * 
 */
package com.dell.acs.persistence.repository.impl.hibernate;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.dell.acs.managers.model.ProductImageCache;
import com.dell.acs.persistence.domain.UnvalidatedProductImage;
import com.dell.acs.persistence.repository.UnvalidatedProductImageRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;

/**
 * @author Shawn_Fisk
 * 
 */
@Repository
public class UnvalidatedProductImageRepositoryImpl extends
		IdentifiableEntityRepositoryImpl<Long, UnvalidatedProductImage>
		implements UnvalidatedProductImageRepository {

	public UnvalidatedProductImageRepositoryImpl() {
		super(UnvalidatedProductImage.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.persistence.repository.UnvalidatedProductImageRepository
	 * #getLatestUnresolvedImage(java.util.Collection)
	 */
	@Override
	public UnvalidatedProductImage getLatestUnresolvedImage(
			Collection<Long> retailerSiteIds) {
		Session session = this.getSession();

		Criteria criteria = session.createCriteria(UnvalidatedProductImage.class);
		if (retailerSiteIds.size() > 0) {
			Criteria productCrieria = criteria.createCriteria("product");
			productCrieria.add(Restrictions.in("retailerSite.id", retailerSiteIds));
		}
		criteria.add(Restrictions.eq("imageURLExists", false));
		criteria.add(Restrictions.ne("cached", ProductImageCache.ERROR.getDbValue()));
		criteria.addOrder(Order.asc("retryCount"));
		criteria.setMaxResults(1);

		@SuppressWarnings("rawtypes")
		List result = criteria.list();
		if (result == null) {
			return null;
		} else if (result.size() == 0) {
			return null;
		} else if (result.size() == 1) {
			return (UnvalidatedProductImage) result.iterator().next();
		}

		return null;
	}
}
