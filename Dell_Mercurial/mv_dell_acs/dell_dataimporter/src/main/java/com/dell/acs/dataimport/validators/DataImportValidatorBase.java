/**
 * 
 */
package com.dell.acs.dataimport.validators;

import com.dell.acs.dataimport.DataImportService;


/**
 * @author Shawn_Fisk
 *
 */
public abstract class DataImportValidatorBase implements DataImportValidator {
	protected DataImportService _dataImportService;
	
	/**
	 * Constructor 
	 * @param dataImportService 
	 */
	public DataImportValidatorBase(DataImportService dataImportService) {
		this._dataImportService = dataImportService;
	}
}
