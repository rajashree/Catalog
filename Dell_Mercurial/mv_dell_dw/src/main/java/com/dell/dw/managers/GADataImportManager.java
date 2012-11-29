package com.dell.dw.managers;

import com.dell.dw.persistence.domain.DataSchedulerBatch;

import java.util.List;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy$
 * @version $Revision$, $Date::                     $
 */
public interface GADataImportManager extends DataImportManager {
    /**
     * Get Unprocessed Google Analytics batches
     * @return
     */
    List<DataSchedulerBatch> getUnprocessedGABatches();

}
