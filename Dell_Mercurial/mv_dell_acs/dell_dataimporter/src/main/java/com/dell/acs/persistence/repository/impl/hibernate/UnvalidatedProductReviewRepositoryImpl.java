/**
 * 
 */
package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.persistence.domain.UnvalidatedProductImage;
import com.dell.acs.persistence.domain.UnvalidatedProductReview;
import com.dell.acs.persistence.repository.UnvalidatedProductReviewRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.springframework.stereotype.Repository;

/**
 * @author Shawn_Fisk
 * 
 */
@Repository
public class UnvalidatedProductReviewRepositoryImpl extends
		IdentifiableEntityRepositoryImpl<Long, UnvalidatedProductReview>
		implements UnvalidatedProductReviewRepository {

	public UnvalidatedProductReviewRepositoryImpl() {
		super(UnvalidatedProductImage.class);
	}
}
