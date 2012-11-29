/**
 * 
 */
package com.dell.acs.dataimport.transformers;

import com.dell.acs.dataimport.DataImportService;
import com.dell.acs.dataimport.model.Row;
import com.dell.acs.persistence.domain.Product;

/**
 * @author Shawn_Fisk
 *
 */
public class AvailabilityColumnTransformer extends DataImportColumnTransformerBase {

	/**
	 * 
	 */
	public AvailabilityColumnTransformer(DataImportService dataImportServices, String affectedColumn) {
		super(dataImportServices, affectedColumn);
	}

	/*
	 * (non-Javadoc)
	 * @see com.dell.acs.dataimport.transformers.DataImportColumnTransformerBase#transform(com.dell.acs.dataimport.model.Row)
	 */
	@Override
	public void transform(Row row) {
		String value = (String)row.get("Availability");
		
		Object result = this._dataImportService.transform(DataImportService.TRANSFORM_AVAILABILITY, value, Product.Availability.IN_STOCK.getValue());
        
        row.set("availability", result);
	}
}
