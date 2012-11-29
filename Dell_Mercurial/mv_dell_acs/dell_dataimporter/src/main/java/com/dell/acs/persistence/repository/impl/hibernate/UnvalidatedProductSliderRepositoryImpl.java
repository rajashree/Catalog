/**
 * 
 */
package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.persistence.domain.UnvalidatedProductImage;
import com.dell.acs.persistence.domain.UnvalidatedProductSlider;
import com.dell.acs.persistence.repository.UnvalidatedProductSliderRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.springframework.stereotype.Repository;

/**
 * @author Shawn_Fisk
 * 
 */
@Repository
public class UnvalidatedProductSliderRepositoryImpl extends
		IdentifiableEntityRepositoryImpl<Long, UnvalidatedProductSlider>
		implements UnvalidatedProductSliderRepository {

	public UnvalidatedProductSliderRepositoryImpl() {
		super(UnvalidatedProductImage.class);
	}
}
