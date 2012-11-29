/**
 * 
 */
package com.sourcen.dataimport.service.support;

/**
 * @author Shawn_Fisk
 *
 */
public interface DataImportListener {

	void beginRun(AbstractDataImportService dataImportService);

	void endRun(AbstractDataImportService dataImportService);

	void startLine(AbstractDataImportService dataImportService,
			int recordIndex);

	void endLine(AbstractDataImportService dataImportService,
			int recordIndex);

	void failureLine(AbstractDataImportService dataImportService,
			int recordIndex, Exception e);

	void batchFailed(AbstractDataImportService dataImportService,
			String batchId, Integer batchSize);

}
