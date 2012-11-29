package com.dell.dw.managers;

import com.dell.dw.persistence.domain.DataSchedulerBatch;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 6/26/12
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */
public interface OrderDataImportManager  extends DataImportManager {
     List<DataSchedulerBatch> getUnprocessedBatches();
}
