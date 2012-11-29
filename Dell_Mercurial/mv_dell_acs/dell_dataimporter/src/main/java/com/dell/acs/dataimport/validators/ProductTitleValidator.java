/**
 * 
 */
package com.dell.acs.dataimport.validators;

import com.dell.acs.dataimport.DataImportService;
import com.dell.acs.dataimport.model.*;
import com.dell.acs.dataimport.model.Error;
import com.sourcen.core.persistence.domain.impl.jpa.EntityModel;
import com.dell.acs.persistence.domain.UnvalidatedProduct;

/**
 * @author Shawn_Fisk
 * 
 */
public class ProductTitleValidator extends DataImportValidatorBase {

	/**
	 * Constructor
	 */
	public ProductTitleValidator(DataImportService dataImportService) {
		super(dataImportService);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.dataimport.validators.DataImportValidator#isSupport(com.
	 * sourcen.core.persistence.domain.impl.jpa.EntityModel)
	 */
	@Override
	public boolean isSupport(EntityModel model) {
		return model instanceof UnvalidatedProduct;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.dataimport.validators.DataImportValidator#validate(com.dell
	 * .acs.dataimport.model.ValidatorContext,
	 * com.sourcen.core.persistence.domain.impl.jpa.EntityModel)
	 */
	@Override
	public void validate(ValidatorContext context, EntityModel model) {
		UnvalidatedProduct product = (UnvalidatedProduct)model;
		
		context.notNull(product.getId(), product.getTitle(), Error.Severity.WARNING, "The product's title should not be null.");
		context.notEmptyString(product.getId(), product.getTitle(), Error.Severity.WARNING, "The product's title should not be an empty string.");
	}
}
