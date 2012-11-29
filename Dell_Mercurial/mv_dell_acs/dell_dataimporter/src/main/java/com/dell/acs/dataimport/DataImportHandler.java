/**
 * 
 */
package com.dell.acs.dataimport;

import com.dell.acs.dataimport.DataImportService.Phases;
import com.dell.acs.dataimport.model.DataImportError;
import com.dell.acs.dataimport.model.KeyPairs;
import com.dell.acs.dataimport.model.Row;
import com.dell.acs.dataimport.transformers.DataImportColumnTransformer;
import com.dell.acs.persistence.domain.DataFile;
import com.sourcen.core.persistence.domain.impl.jpa.EntityModel;
import com.sourcen.dataimport.definition.ColumnDefinition;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Shawn_Fisk
 *
 */
public interface DataImportHandler {
	void setCurrentRow(int row);
	void setCurrentColumn(int column);
    void setHeader(String[] header);
    boolean hasErrors();
	Iterable<DataImportError> getErrors();
	List<DataImportColumnTransformer> getTransformers(Phases phase, String sourceTable);
	void init(DataImportServiceImpl dataImportServiceImpl, DataFile dataFile);
    void handleException(String msg, Object... args);
    void handleException(Throwable throwable, String msg, Object... args);

    @Transactional(readOnly = true)
	EntityModel lookupReference(ColumnDefinition cd, KeyPairs keys);

	@Transactional(readOnly = true)
	EntityModel getEntity(KeyPairs keys);

	@Transactional
	void saveOrUpdate(EntityModel entity, Row row);
}
