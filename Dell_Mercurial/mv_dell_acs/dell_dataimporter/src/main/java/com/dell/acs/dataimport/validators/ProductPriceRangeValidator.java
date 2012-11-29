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
public class ProductPriceRangeValidator extends DataImportValidatorBase {

	/**
	 * Constructor
	 */
	public ProductPriceRangeValidator(DataImportService dataImportService) {
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
		
		context.lessThanOrEqual(product.getId(), product.getListPrice(), product.getPrice(), Error.Severity.FATAL, "The product's list price should be less than or equal to the price.");
	}
}
