/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service.support.jdbc;

import com.sourcen.dataimport.definition.ColumnDefinition;
import com.sourcen.dataimport.definition.DataImportConfig;
import com.sourcen.dataimport.definition.TableDefinition;
import com.sourcen.dataimport.service.TableSequenceService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 @author Navin Raj Kumar G.S.
 @author $LastChangedBy: adarsh $
 @version $Revision: 1729 $, $Date:: 2012-04-18 11:50:07#$ */
public final class TableSequenceServiceImpl implements InitializingBean, TableSequenceService {

    /**
     logger class
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TableSequenceServiceImpl.class);

    /**
     class fields
     */
    private static final Map<String, Long> cache = new ConcurrentHashMap<String, Long>();

    private SimpleJdbcTemplate jdbcTemplate;

    private DataSource dataSource;

    private DataImportConfig dataImportConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        jdbcTemplate = new SimpleJdbcTemplate(dataSource);
    }


    @Override
    public Long getSequenceId(String tableName, ColumnDefinition definition) {
        return getSequenceId(dataImportConfig.getTableDefinition(tableName), definition);
    }

    @Override
    public Long getSequenceId(TableDefinition tableDefinition, ColumnDefinition definition) {
        Long id = 0L;
        synchronized (cache) {
            String cacheKey = tableDefinition.getDestinationTable() + "." + definition.getDestination();
            if (cache.containsKey(cacheKey)) {
                id = cache.get(cacheKey);
            } else {
                // check from DB.
                String sql_queryForMax = "SELECT MAX(" + definition.getDestination() + ") AS ID FROM "
                        + tableDefinition.getDestinationTable();
                id = jdbcTemplate.queryForLong(sql_queryForMax);
            }
            id++;
            cache.put(cacheKey, id);
        }
        return id;
    }

    /**
     setter and getter
     */
    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void setDataImportConfig(final DataImportConfig dataImportConfig) {
        this.dataImportConfig = dataImportConfig;
    }
}
