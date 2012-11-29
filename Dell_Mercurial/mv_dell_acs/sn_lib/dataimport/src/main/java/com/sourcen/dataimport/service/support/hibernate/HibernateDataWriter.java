/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service.support.hibernate;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.base.Joiner;
import com.sourcen.core.persistence.domain.EntityProperty;
import com.sourcen.core.persistence.domain.IdentifiableEntity;
import com.sourcen.core.persistence.domain.PropertiesAwareEntity;
import com.sourcen.core.persistence.domain.constructs.jpa.RecordProperties;
import com.sourcen.dataimport.DataImportException;
import com.sourcen.dataimport.service.DataWriter;
import com.sourcen.dataimport.service.errors.DataWriterException;
import com.sourcen.dataimport.service.support.BaseDataAdapter;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 3964 $, $Date:: 2012-07-12 14:51:53#$
 */

/**
 * {@inheritDoc}
 */
public final class HibernateDataWriter extends BaseDataAdapter implements DataWriter {

    /**
     * class fields
     */
    private Collection<BeanProcessor> beanProcessors = new CopyOnWriteArraySet<BeanProcessor>();

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public List<Object[]> executeBatchUpdate(Collection<Map<String, Object>> batch, String batchId,
                                             Map<Object[], Map<String, Object>> recordsToInsert,
                                             Map<Object[], Map<String, Object>> recordsToUpdate) {
        Assert.notNull(sessionFactory, "sessionFactory cannot be null, ie, sessionFactory is not initialized.");

        List<Object[]> keysList = new LinkedList<Object[]>();
        try {
            Session session = getSession();
            final Class<?> clazz;
            // when Entities are of type - Properties Aware, then we need to load the properties before PostProcessing the bean.
            // fixed after noticing this in CS-354
            Class<?> entityPropertiesClass = null;
            try {
                clazz = Class.forName(tableDefinition.getDestinationTable());
                if(PropertiesAwareEntity.class.isAssignableFrom(clazz)){
                    if(tableDefinition.getProperties().containsKey("propertiesClass")){
                        try {
                            entityPropertiesClass = Class.forName(tableDefinition.getProperty("propertiesClass"));
                        } catch (ClassNotFoundException e) {
                            throw  new DataImportException(e);
                        }
                    } else{
                        entityPropertiesClass = Class.forName(clazz.getCanonicalName()+"Property");
                    }
                    Assert.isAssignable(EntityProperty.class, entityPropertiesClass);
                }
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage(), e);
                throw new DataImportException(e);
            }

            preExecuteBatchUpdate(batch, batchId, recordsToInsert, recordsToUpdate);
            int commitBatchSize = 0;
            if (tableDefinition.getBooleanProperty("disableBatchUpdate", false)) {
                commitBatchSize = 1;
            } else {
                commitBatchSize = tableDefinition.getIntegerProperty("batchSize", 1);
            }

            int batchCount = 0;
            final Map<String, Method> methods = HibernateUtils.getBeanSetters(tableDefinition.getColumns(), clazz);
            Boolean insertKeys = tableDefinition.getBooleanProperty("mapKeys", false);
            Object bean = null;
            Map<String, Object> row = null;

            for (Map.Entry<Object[], Map<String, Object>> entry : recordsToInsert.entrySet()) {
                row = null;
                bean = null;
                Object[] key = entry.getKey();
                row = entry.getValue();

                bean = BeanUtils.instantiate(clazz);
                try {
                    row = preProcessBeanValues(clazz, row);
                    for (Map.Entry<String, Object> column : row.entrySet()) {
                        if (methods.containsKey(column.getKey())) {
                            try {
                                methods.get(column.getKey()).invoke(bean, column.getValue());
                            } catch (Exception e) {
                                logger.error("Unable to set value for column:" + column.getKey() + " on method:="
                                        + methods.get(column.getKey()) + " with value:=" + column.getValue(),
                                        e.getMessage());
                                throw e;
                            }
                        }
                    }
                    preProcessBeforePersist(bean, row);
                    session.save(bean);
                    postProcessAfterPersist(bean, row);
                    try {
                        Method method = PropertyUtils
                                .getPropertyDescriptor(bean, tableDefinition.getPrimaryKey().getDestinationKey())
                                .getReadMethod();
                        Object savedKey = method.invoke(bean);
                        key[3] = savedKey;
                        keysList.add(key);
                    } catch (Exception e) {
                        logger.warn("Unable to add keyMap:=" + Joiner.on(",").join(key) + " for bean:="
                                + bean.toString());
                        throw e;
                    }
                } catch (Exception e) {
                    logger.error("Unable to insert row error Message:=" + e.getMessage());
                    DataWriterException dataWriterException = new DataWriterException("SINGLE_ROW", tableDefinition);
                    dataWriterException
                            .set("e", "ERROR_WRITE := "+e.getMessage())
                            .set("row", row);
                    exceptionHandler.onDataWriterException(dataWriterException);
                    if (row == null) {
                        logger.error("null row");
                    }
                }
                if (++batchCount % commitBatchSize == 0) {
                    logger.info("flushing insert session for batchSize:=" + commitBatchSize + " and recordIndex:="
                            + batchCount);
                    session.flush();
                }
            }

            if (!recordsToInsert.isEmpty()) {
                if (insertKeys && !keysList.isEmpty()) {
                    try {
                        profiler.markEvent("keyMapping-" + batchId);
                        dataImportLookupService.putKeys(keysList);
                        profiler.endEvent("keyMapping-" + batchId);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e.getMessage());
                    }
                }
            }


            // now handle updates.
            final Map<Serializable, Map.Entry<Object[], Map<String, Object>>> updateMapping =
                    new LinkedHashMap<Serializable, Map.Entry<Object[], Map<String, Object>>>();
            for (Map.Entry<Object[], Map<String, Object>> entry : recordsToUpdate.entrySet()) {
                try {
                    updateMapping.put((Serializable) entry.getKey()[3], entry);
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e.getMessage());
                }
            }

            try {
                if (!updateMapping.isEmpty()) {
                    final String entityName = clazz.getSimpleName();
                    for (Map.Entry<Serializable, Map.Entry<Object[], Map<String, Object>>> updateEntityKeyMapping
                            : updateMapping.entrySet()) {

                        Serializable entityKey = updateEntityKeyMapping.getKey();
                        IdentifiableEntity identifiableEntity = (IdentifiableEntity) session.get(clazz, entityKey);
                        if(entityPropertiesClass != null){
                            // must load properties as well.
                            final Criteria criteria = getSession().createCriteria(entityPropertiesClass);
                            criteria.add(Restrictions.eq("pk.id", identifiableEntity.getId()));
                            final List<EntityProperty> list = criteria.list();
                            if (list != null && !list.isEmpty()) {
                                for (final EntityProperty entityProperty : list) {
                                    final RecordProperties rp = (RecordProperties) ((PropertiesAwareEntity) identifiableEntity)
                                            .getProperties();
                                    rp.load(entityProperty.getName(), entityProperty.getValue());
                                }
                            }
                        }
                        Map.Entry<Object[], Map<String, Object>> entry = updateMapping.get(identifiableEntity.getId());
                        if (entry != null) {
                            try {
                                preProcessBeanValues(identifiableEntity.getClass(), entry.getValue());
                                for (Map.Entry<String, Object> column : entry.getValue().entrySet()) {
                                    if (methods.containsKey(column.getKey())) {
                                        methods.get(column.getKey()).invoke(identifiableEntity, column.getValue());
                                    }
                                }
                                preProcessBeforePersist(identifiableEntity, entry.getValue());
                                session.saveOrUpdate(identifiableEntity);
                                postProcessAfterPersist(identifiableEntity, entry.getValue());
                            } catch (Exception e) {
                                logger.error("Unable to update row  := ", e.getMessage());
                                DataWriterException dataWriterException = new DataWriterException("SINGLE_ROW", tableDefinition);
                                dataWriterException
                                        .set("e", e.getMessage())
                                        .set("row", entry.getValue());
                                exceptionHandler.onDataWriterException(dataWriterException);
                                logger.error("failed row details :="
                                        + Joiner.on(",").withKeyValueSeparator("=").join(entry.getValue()));
                            }
                        }

                        if (++batchCount % commitBatchSize == 0) {
                            logger.info(
                                    "flushing update session for batchSize:=" + commitBatchSize + " and recordIndex:="
                                            + batchCount);
                            session.flush();
                        }
                    }
                    return null;
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e.getMessage());
            }


            postExecuteBatchUpdate(batch, batchId, recordsToInsert, recordsToUpdate, keysList);
        } catch (Exception e) {
            exceptionHandler.onDataWriterException(e);
            logger.warn(e.getMessage(), e.getMessage());
        }
        return keysList;
    }


    /**
     * preProcessBeforePersist() helps in the pre processing of the bean to manipulate the  bean data before persisting
     * bean object
     *
     * @param bean accepts the reference of the bean object
     * @param row  accepts the row data in the form of the java.util.Map object
     * @return the Bean object which is been manupulated in side the method
     */
    protected Object preProcessBeforePersist(Object bean, Map<String, Object> row) {
        for (BeanProcessor processor : beanProcessors) {
            if (processor != null && processor.supportsBean(bean.getClass())) {
                bean = processor.preProcessBeforePersist(bean, row);
            }
        }
        return bean;
    }

    private Object postProcessAfterPersist(Object bean, final Map<String, Object> row) {
        for (BeanProcessor processor : beanProcessors) {
            if (processor != null && processor.supportsBean(bean.getClass())) {
                bean = processor.postProcessAfterPersist(bean, row);
            }
        }
        return bean;
    }


    /**
     * preProcessBeanValues() is dedicated method for the mamupulateing the bean object data
     *
     * @param beanClass accept the java.lang.Class types reference of the Bean class
     * @param row       accept the row data in the form of the java.util.Map object
     * @param row       accept the row data in the form of the java.util.Map object
     * @return java.util.Map<String,Object> object having the row data value
     */
    protected Map<String, Object> preProcessBeanValues(Class beanClass, Map<String, Object> row) {
        for (BeanProcessor processor : beanProcessors) {

            try {
                if (processor != null && processor.supportsBean(beanClass)) {
                    row = processor.preProcessBeanValues(row);
                }
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }
        return row;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void processRowBeforeInsertion(Map<String, Object> srcRecord, Map<String, Object> record,
                                          Integer recordIndex) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preExecuteBatchUpdate(Collection<Map<String, Object>> batch, String batchId, Map<Object[],
            Map<String, Object>> recordsToInsert, Map<Object[], Map<String, Object>> recordsToUpdate) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postExecuteBatchUpdate(Collection<Map<String, Object>> batch, String batchId, Map<Object[],
            Map<String, Object>> recordsToInsert, Map<Object[], Map<String, Object>> recordsToUpdate, List result) {
    }

    /**
     * Class Properties and its setter() and getter() for the value injection
     */

    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void setBeanProcessors(final Collection<BeanProcessor> beanProcessors) {
        this.beanProcessors = beanProcessors;
    }

    @Autowired
    public void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}

