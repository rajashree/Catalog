package com.dell.dw.managers;

import com.dell.dw.persistence.domain.DataSchedulerBatch;
import com.dell.dw.persistence.domain.DataSource;
import com.dell.dw.persistence.domain.GAWebPropertyProfile;
import com.dell.dw.persistence.repository.DataSchedulerBatchRepository;
import com.dell.dw.persistence.repository.DataSourceRepository;
import com.dell.dw.persistence.repository.GAWebPropertyProfileRepository;
import com.dell.dw.persistence.repository.GAWebPropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 7/5/12
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class DataImportMonitorManagerImpl implements DataImportMonitorManager{
    @Override
    public Collection<DataSource> getAllDatasources() {

        return dataSourceRepository.getAll();
    }

    @Override
    public Collection<DataSchedulerBatch> getDataSchedulerBatches(Long datasourceId, String webPropertyProfile, String processStatus) {
        Collection<DataSchedulerBatch> dataSchedulerBatches =  dataSchedulerBatchRepository.getBatches(datasourceId, webPropertyProfile, processStatus);
      return dataSchedulerBatches;
    }

    @Override
    public DataSchedulerBatch getDataSchedulerBatch(Long batchId){
        return dataSchedulerBatchRepository.get(batchId);
    }

    @Autowired
    DataSourceRepository dataSourceRepository;

    public DataSourceRepository getDataSourceRepository() {
        return dataSourceRepository;
    }

    public void setDataSourceRepository(DataSourceRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }

    @Autowired
    DataSchedulerBatchRepository dataSchedulerBatchRepository;

    public DataSchedulerBatchRepository getDataSchedulerBatchRepository() {
        return dataSchedulerBatchRepository;
    }

    public void setDataSchedulerBatchRepository(DataSchedulerBatchRepository dataSchedulerBatchRepository) {
        this.dataSchedulerBatchRepository = dataSchedulerBatchRepository;
    }

    @Autowired
    GAWebPropertyProfileRepository gaWebPropertyProfileRepository;

    public GAWebPropertyProfileRepository getGaWebPropertyProfileRepository() {
        return gaWebPropertyProfileRepository;
    }

    public void setGaWebPropertyProfileRepository(GAWebPropertyProfileRepository gaWebPropertyProfileRepository) {
        this.gaWebPropertyProfileRepository = gaWebPropertyProfileRepository;
    }

    @Autowired
    GAWebPropertyRepository gaWebPropertyRepository;

    public GAWebPropertyRepository getGaWebPropertyRepository() {
        return gaWebPropertyRepository;
    }

    public void setGaWebPropertyRepository(GAWebPropertyRepository gaWebPropertyRepository) {
        this.gaWebPropertyRepository = gaWebPropertyRepository;
    }
}
