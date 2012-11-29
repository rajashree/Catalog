package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.DataSchedulerBatch;
import com.dell.dw.persistence.repository.DataSchedulerBatchRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.hql.internal.ast.QuerySyntaxException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 5/28/12
 * Time: 6:35 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class DataSchedulerBatchRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, DataSchedulerBatch>
        implements DataSchedulerBatchRepository {

    public DataSchedulerBatchRepositoryImpl() {
        super(DataSchedulerBatch.class);
    }

    /**
     * Get Scheduler Batch for a given Data Source and Reference ID
     * @param dataSourceId
     * @param referenceId
     * @return
     */
    @Override
    public List<DataSchedulerBatch> getBatches(Long dataSourceId, Long referenceId) {
        Criteria criteria = getSession().createCriteria(DataSchedulerBatch.class)
                .add(Restrictions.eq("DataSource.id", dataSourceId))
                .add(Restrictions.eq("referenceId", referenceId));
        List<DataSchedulerBatch> results = criteria.list();
        if(results.isEmpty()) {
            return null;
        }
        return results;
    }

    /**
     *
     * @param availableFiles
     * @return
     */
    @Override
    public List<DataSchedulerBatch> getDataFilesWithSrcPath(Set<String> availableFiles) {
        Criteria criteria = getSession().createCriteria(DataSchedulerBatch.class);
        criteria.add(Restrictions.in("srcFile", availableFiles));
        return (List<DataSchedulerBatch>) criteria.list();
    }

    /**
     * Get all unprocessed scheduler batches for a given data source
     * @param dataSourceId
     * @return
     */
    @Override
    public List<DataSchedulerBatch> getUnprocessedBatches(Long dataSourceId) {
        Criteria criteria = getSession().createCriteria(DataSchedulerBatch.class)
                .add(Restrictions.eq("dataSource.id", dataSourceId))
                .add(Restrictions.ne("status", DataSchedulerBatch.Status.PROCESSING))
                .add(Restrictions.ne("status", DataSchedulerBatch.Status.DONE))
                .addOrder(Order.asc("priority"));
        List<DataSchedulerBatch> results = criteria.list();
        if(results.isEmpty()) {
            return null;
        }
        return results;
    }

    /**
     *
     * @param referenceId
     * @param endPoint
     * @return
     */
    @Override
    public Date getInitialBatchStartDate(Long referenceId, String endPoint) {
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.min("startDate"));
        Criteria criteria = getSession().createCriteria(DataSchedulerBatch.class)
                .setProjection(projectionList)
                .add(Restrictions.eq("referenceId", referenceId))
                .add(Restrictions.eq("endpoint", endPoint));
        List result = criteria.list();
        if(result.isEmpty()) {
            return null;
        }
        return (Date) result.get(0);
    }

    /**
     *
     * @param referenceId
     * @param endPoint
     * @return
     */
    @Override
    public Date getLastBatchEndDate(Long referenceId, String endPoint) {
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.max("endDate"));
        Criteria criteria = getSession().createCriteria(DataSchedulerBatch.class)
                .setProjection(projectionList)
                .add(Restrictions.eq("referenceId", referenceId))
                .add(Restrictions.eq("endpoint", endPoint));
        List result = criteria.list();
        if(result.isEmpty()) {
            return null;
        }
        return (Date) result.get(0);
    }

    @Override
    public List<DataSchedulerBatch> getBatches(Long datasourceId, String webPropertyProfile, String processStatus) {
        Criteria criteria = getSession().createCriteria(DataSchedulerBatch.class);
        List<Integer> processStatusList = new ArrayList<Integer>();
        if(processStatus != null && processStatus.length() > 0){
            String[] arr  = processStatus.split(",");
            for(String str : arr){
                if(!str.contains("-"))
                    processStatusList.add(Integer.parseInt(str));
                else{
                    String[] statuses=str.split("-");
                    for(String status : statuses){
                        processStatusList.add(Integer.parseInt(status));
                    }
                }
            }
        }
        if(datasourceId != null)
            criteria.add(Restrictions.eq("dataSource.id", datasourceId));
        if(webPropertyProfile != null && webPropertyProfile.length() > 0){
            criteria.add(Restrictions.eq("referenceId", Long.parseLong(webPropertyProfile)));
        }
        if(processStatusList != null && processStatusList.size() > 0){
            criteria.add(Restrictions.in("status", processStatusList));
        }

        List<DataSchedulerBatch> results = criteria.list();
        if(results.isEmpty()) {
            return null;
        }
        return results;
    }

    @Override
    public void removeGABatchesTillDate(Long referenceId, Date date) {
        String hql = "delete from " + this.entityClassName + " where referenceId = :referenceId and endDate < :date";
        try {
            Query query = getSession().createQuery(hql);
            query.setParameter("referenceId", referenceId);
            query.setParameter("date", date);
            int rowCount = query.executeUpdate();
        } catch (QuerySyntaxException e) {

        }
    }

    @Override
    public List<DataSchedulerBatch> getInitialBatches(Long referenceId, String endPoint) {
        try {
            Query query = getSession().getNamedQuery("DataSchedulerBatch.getInitialBatches");
            query.setParameter("referenceId", referenceId);
            query.setParameter("endPoint", endPoint);
            return query.list();
        } catch (QuerySyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Transactional
    public void updateLastProcessedDate(Long id) {
        DataSchedulerBatch batch = this.get(id);
        batch.setLastProcessedDate(new Date());
        this.update(batch);
    }

    @Override
    @Transactional
    public void updateLastProcessedDate(Long id, Date date) {
        DataSchedulerBatch batch = this.get(id);
        batch.setLastProcessedDate(date);
        this.update(batch);
    }

}
