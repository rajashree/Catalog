package com.dell.dw.managers;

import com.dell.dw.persistence.domain.DataSchedulerBatch;
import com.dell.dw.persistence.domain.DataSource;
import com.sourcen.core.managers.Manager;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 7/5/12
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataImportMonitorManager extends Manager {
    public Collection<DataSource> getAllDatasources();

    public Collection<DataSchedulerBatch> getDataSchedulerBatches(Long datasourceId, String webPropertyProfile, String processStatus);

    public DataSchedulerBatch getDataSchedulerBatch(Long batchId);
}
