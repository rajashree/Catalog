/**
 * 
 */
package com.dell.acs.dataimport.transformers;

import com.dell.acs.dataimport.model.Row;

/**
 * @author Shawn_Fisk
 *
 */
public interface DataImportColumnTransformer {
    public String getAffectedColumn();

    public void transform(Row row);
}
