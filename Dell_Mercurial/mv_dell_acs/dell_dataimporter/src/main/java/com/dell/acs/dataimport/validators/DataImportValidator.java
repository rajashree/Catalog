package com.dell.acs.dataimport.validators;

import com.dell.acs.dataimport.model.ValidatorContext;
import com.sourcen.core.persistence.domain.impl.jpa.EntityModel;

public interface DataImportValidator {
	boolean isSupport(EntityModel model);
	
	void validate(ValidatorContext context, EntityModel model);
}
