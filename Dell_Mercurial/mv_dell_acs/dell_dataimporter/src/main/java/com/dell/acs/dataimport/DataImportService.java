/**
 * 
 */
package com.dell.acs.dataimport;

import com.dell.acs.dataimport.preprocessor.PreprocessorHandler;
import com.dell.acs.dataimport.transformers.DataImportColumnTransformer;
import com.dell.acs.dataimport.validators.DataImportValidator;
import com.dell.acs.persistence.domain.DataFile;

import java.util.List;


/**
 * @author Shawn_Fisk
 *
 */
public interface DataImportService {
	public static final String TRANSFORM_AVAILABILITY = "Availability"; 

	enum Phases {
		PREVALIDATED("prevalidated");
		
		private String _text;
		
		private Phases(String text) {
			this._text = text;
		}
		
		public String getText() {
			return this._text;
		}
		
		public static Phases lookupText(String text) {
			for(Phases phase : Phases.values()) {
				if (phase.getText().compareTo(text) == 0) {
					return phase;
				}
			}
			
			return null;
		}
	}

	<T extends DataImportHandler> T get(Class<T> type, DataFile dataFile, String source, Phases phase);

	List<DataImportColumnTransformer> getTransforms(Phases phase, String sourceTable) throws DataImportServiceException;

	Object transform(String type, String value, Object defaultValue);

	List<DataImportValidator> getValidators() throws DataImportServiceException;

	PreprocessorHandler getPreprocessorHandler(String provider, String retailerSite);
}
