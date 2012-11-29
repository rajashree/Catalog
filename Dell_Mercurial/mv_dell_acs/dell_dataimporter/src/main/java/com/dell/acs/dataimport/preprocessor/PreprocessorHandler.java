/**
 * 
 */
package com.dell.acs.dataimport.preprocessor;

import com.dell.acs.persistence.domain.DataFile;

/**
 * @author Shawn_Fisk
 *
 */
public interface PreprocessorHandler {
	void preprocess(DataFile dataFile);
}
