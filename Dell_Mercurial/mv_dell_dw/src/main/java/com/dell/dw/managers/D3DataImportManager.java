package com.dell.dw.managers;

import com.dell.dw.persistence.domain.DataSchedulerBatch;

import java.util.List;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: rajashreem $
 * @version $Revision: 2808 $, $Date:: 2012-06-01 14:31:00#$
 */
public interface D3DataImportManager extends DataImportManager {
     List<DataSchedulerBatch> getUnprocessedBatches();
}
