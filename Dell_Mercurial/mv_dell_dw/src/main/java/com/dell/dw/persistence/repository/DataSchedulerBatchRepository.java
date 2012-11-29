package com.dell.dw.persistence.repository;

import com.dell.dw.persistence.domain.DataSchedulerBatch;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy$
 * @version $Revision$, $Date::                     $
 */
public interface DataSchedulerBatchRepository extends IdentifiableEntityRepository<Long, DataSchedulerBatch> {
    List<DataSchedulerBatch> getBatches(Long dataSourceId, Long referenceId);

    List<DataSchedulerBatch> getDataFilesWithSrcPath(Set<String> availableFiles);

    List<DataSchedulerBatch> getUnprocessedBatches(Long dataSourceId);

    Date getInitialBatchStartDate(Long referenceId, String endPoint);

    Date getLastBatchEndDate(Long referenceId, String endPoint);

    List<DataSchedulerBatch> getBatches(Long datasourceId, String webPropertyProfile, String processStatus);

    void removeGABatchesTillDate(Long referenceId, Date date);

    List<DataSchedulerBatch> getInitialBatches(Long referenceId, String endPoint);

    void updateLastProcessedDate(Long id);
    void updateLastProcessedDate(Long id, Date date);
}
