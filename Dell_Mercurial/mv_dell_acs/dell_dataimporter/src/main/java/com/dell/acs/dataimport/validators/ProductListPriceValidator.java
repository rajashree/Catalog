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
public class ProductListPriceValidator extends DataImportValidatorBase {

	/**
	 * Constructor
	 */
	public ProductListPriceValidator(DataImportService dataImportService) {
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
		
		context.notNull(product.getId(), product.getListPrice(), Error.Severity.DEFAULT, "Trying to default the product's list price to the price.");
		
		if (product.getPrice() != null) {
			product.setListPrice(product.getPrice());
		}
		
		context.notNull(product.getId(), product.getListPrice(), Error.Severity.FATAL, "The product's list price can not be null.");
	}
}
