/**
 * 
 */
package com.dell.acs.dataimport.transformers;

import com.dell.acs.dataimport.DataImportService;
import com.dell.acs.dataimport.model.Row;

/**
 * @author Shawn_Fisk
 * 
 */
public class DataImportColumnTransformerBase implements
		DataImportColumnTransformer {
	protected DataImportService _dataImportService;
    protected String _affectedColumn;

	/**
	 * @param dataImportService
	 * 
	 */
	public DataImportColumnTransformerBase(DataImportService dataImportService, String affectedColumn) {
		this._dataImportService = dataImportService;
        this._affectedColumn = affectedColumn;
	}

    @Override
    public String getAffectedColumn() {
        return this._affectedColumn;
    }

    /*
      * (non-Javadoc)
      *
      * @see
      * com.dell.acs.dataimport.transformers.DataImportColumnTransformer#transform
      * (com.dell.acs.dataimport.model.Row)
      */
	@Override
	public void transform(Row row) {
		// TODO Auto-generated method stub

	}

}
