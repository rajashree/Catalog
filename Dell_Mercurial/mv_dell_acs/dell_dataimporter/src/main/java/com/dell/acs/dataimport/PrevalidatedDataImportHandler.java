/**
 * 
 */
package com.dell.acs.dataimport;

import org.springframework.transaction.annotation.Transactional;

import com.dell.acs.dataimport.model.KeyPairs;
import com.sourcen.core.persistence.domain.impl.jpa.EntityModel;

/**
 * @author Shawn_Fisk
 *
 */
public interface PrevalidatedDataImportHandler extends DataImportHandler {
	@Transactional(readOnly = true)
	EntityModel getPrevalidatedEntity(KeyPairs keys);
}
