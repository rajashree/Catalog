/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service.support.hibernate;

import com.sourcen.dataimport.definition.ColumnDefinition;
import com.sourcen.dataimport.definition.DataImportConfig;
import com.sourcen.dataimport.definition.TableDefinition;
import com.sourcen.dataimport.service.TableSequenceService;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Since we are using hibernate we dont cache keys. preferable not to use tablesequence
 * and let hibernate entity manage it.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 1724 $, $Date:: 2012-04-18 11:47:03#$
 */

/**
 TableSequenceServiceImpl having the implementation for the SequenceService for generating the sequence by using the
 database tables
 */
public final class TableSequenceServiceImpl implements InitializingBean, TableSequenceService {

    /*
     class fields
     */
    private static final Map<String, Long> cache = new ConcurrentHashMap<String, Long>();

    private static TableSequenceService instance;

    private DataImportConfig dataImportConfig;

    /**
     {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
    }


    /**
     {@inheritDoc}
     */
    @Override
    public Long getSequenceId(String tableName, ColumnDefinition definition) {
        return getSequenceId(dataImportConfig.getTableDefinition(tableName), definition);
    }

    /**
     {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Long getSequenceId(final TableDefinition tableDefinition, final ColumnDefinition definition) {
        Long id = 0L;
        synchronized (cache) {
            String cacheKey = tableDefinition.getDestinationTable() + "." + definition.getDestination();
            if (cache.containsKey(cacheKey)) {
                id = cache.get(cacheKey);
            } else {
                // check from DB.
                String sql_queryForMax = "SELECT MAX(" + definition.getDestination() + ") AS ID FROM " +
                        tableDefinition.getDestinationTable();
                Criteria criteria = getSession().createCriteria(tableDefinition.getDestinationTable());
                Integer max =
                        (Integer) criteria.setProjection(Projections.max(definition.getDestination())).uniqueResult();
                if (max == null) {
                    return 1L;
                } else {
                    return max.longValue() + 1;
                }
            }
            id++;
            cache.put(cacheKey, id);
        }
        return id;
    }

    /**
     Class Properties and corresponding setter() and getter() for value injejction
     */


    public void setDataImportConfig(DataImportConfig dataImportConfig) {
        this.dataImportConfig = dataImportConfig;
    }

    private SessionFactory sessionFactory;

    @Autowired
    public void setAutowiredSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }


    public static TableSequenceService getInstance() {
        if (instance == null) {
            instance = new TableSequenceServiceImpl();
        }
        return instance;
    }
}
