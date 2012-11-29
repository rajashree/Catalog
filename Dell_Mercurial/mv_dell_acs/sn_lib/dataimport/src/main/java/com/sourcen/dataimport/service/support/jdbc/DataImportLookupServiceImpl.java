/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service.support.jdbc;

import com.sourcen.dataimport.service.DataImportLookupService;
import com.sourcen.dataimport.util.DataTypeUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 @author Navin Raj Kumar G.S.
 @author $LastChangedBy: rajashreem $
 @version $Revision: 2993 $, $Date:: 2012-06-07 12:50:19#$ */
public final class DataImportLookupServiceImpl implements InitializingBean, DataImportLookupService {

    /**
     logger
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DataImportLookupServiceImpl.class);

    /**
     class fields
     */
    private static final Map<String, Map<String, Long>> cache = new ConcurrentHashMap<String, Map<String, Long>>();

    private static DataImportLookupService instance;

    private SimpleJdbcTemplate jdbcTemplate;

    private DataSource dataSource;

    private Boolean createTableIfNotExists;

    private String lookupTable;

    private String column_srcTableName;

    private String column_srcTableId;

    private String column_destTableName;

    private String column_destTableId;


    public static DataImportLookupService getInstance() {
        if (instance == null) {
            instance = new DataImportLookupServiceImpl();
        }
        return instance;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        jdbcTemplate = new SimpleJdbcTemplate(dataSource);
        if (createTableIfNotExists) {
            StringBuilder sb = new StringBuilder(10000);
            sb.append("CREATE TABLE IF NOT EXISTS " + this.lookupTable + " (").
                    append(column_srcTableName + " VARCHAR(50) NOT NULL,").
                    append(column_srcTableId + " BIGINT(20) NOT NULL,").
                    append(column_destTableName + " VARCHAR(255) NOT NULL,").
                    append(column_destTableId + " BIGINT(20) NOT NULL").
                    append(")");
            logger.info("Creating LookupTable: with query = " + sb.toString());
            jdbcTemplate.getJdbcOperations().execute(sb.toString());
        }
    }

    @Override
    public Long getKey(final String tableName, final String srcKey) {
        return getTableCache(tableName).get(srcKey);
    }

    private static final Map<String, String> insertQueryCache = new ConcurrentHashMap<String, String>();

    @Override
    public void putKeys(List<Object[]> keys) {
        Assert.notEmpty(keys, "keys cannot be empty.");
        // 0=srcTableName, 1= srcKey, 2=destTable, 3=destKey});
        String srcTableName = keys.get(0)[0].toString();
        Assert.notNull(srcTableName, "Source tableName must not be null");
        Assert.notNull(keys.get(0)[2], "destination table name must not be null");
        if (lookupTable == null) {
            throw new IllegalArgumentException("lookupTable name must not be null");
        }
        String sql_insert = insertQueryCache.get(lookupTable);
        if (sql_insert == null) {
            sql_insert = "INSERT INTO " + lookupTable + " ("
                    + column_srcTableName + ","
                    + column_srcTableId + ","
                    + column_destTableName + ","
                    + column_destTableId + ") VALUES(?,?,?,?)";
            insertQueryCache.put(lookupTable, sql_insert);
        }

        // load the tableCache before the update.
        Map<String, Long> tableCache = getTableCache(srcTableName);

        jdbcTemplate.batchUpdate(sql_insert, keys);

        // push in cache as well
        for (Object[] keyMapping : keys) {
            tableCache.put(String.valueOf(keyMapping[1]), DataTypeUtil.getLong(keyMapping[3]));
        }
    }

    @Override
    public void putKey(Object[] key) {
        Assert.notEmpty(key, "key cannot be empty.");
        // 0=srcTableName, 1= srcKey, 2=destTable, 3=destKey});
        String srcTableName = key[0].toString();
        Assert.notNull(srcTableName, "Source tableName must not be null");
        Assert.notNull(key[2], "destination table name must not be null");
        if (lookupTable == null) {
            throw new IllegalArgumentException("lookupTable name must not be null");
        }

        String sql_insert = insertQueryCache.get(lookupTable);
        if (sql_insert == null) {
            sql_insert = "INSERT INTO " + lookupTable + " ("
                    + column_srcTableName + ","
                    + column_srcTableId + ","
                    + column_destTableName + ","
                    + column_destTableId + ") VALUES(?,?,?,?)";
            insertQueryCache.put(lookupTable, sql_insert);
        }

        // load the tableCache before the update.
        Map<String, Long> tableCache = getTableCache(srcTableName);

        jdbcTemplate.update(sql_insert, key);
        // push in cache as well
        tableCache.put(String.valueOf(key[1]), DataTypeUtil.getLong(key[3]));
    }

    /**
     returns a map of SrcTableId, to destTableId

     @param tableName string
     */
    @Override
    public Map<String, Long> getTableCache(String tableName) {
        // initialize the cache,and load values from DB.
        Assert.notNull(tableName, "tableName should not be null");
        Map<String, Long> tableCache = cache.get(tableName);
        if (tableCache == null) {
            synchronized (cache) {
                if (tableCache == null) { // double check the table cache lock
                    tableCache = new ConcurrentHashMap<String, Long>();
                    cache.put(tableName, tableCache);
                    // preload the data from DB.
                    String sql_select_for_table =
                            "SELECT " + column_srcTableId + ", " + column_destTableId + " FROM " + lookupTable
                                    + " WHERE " + column_srcTableName + " = '" + tableName + "' ";

                    List<Map<String, Object>> result = jdbcTemplate.queryForList(sql_select_for_table);
                    for (Map<String, Object> object : result) {
                        // our select is only the oldKey and new Key.
                        tableCache.put((String) object.get(column_srcTableId), (Long) object.get(column_destTableId));
                    }
                }
            }
        }
        return tableCache;
    }

    /**
     getters and setters
     */


    public void setCreateTableIfNotExists(final Boolean createTableIfNotExists) {
        this.createTableIfNotExists = createTableIfNotExists;
    }

    public void setLookupTable(final String lookupTable) {
        this.lookupTable = lookupTable;
    }

    public void setColumn_srcTableName(final String column_srcTableName) {
        this.column_srcTableName = column_srcTableName;
    }

    public void setColumn_srcTableId(final String column_srcTableId) {
        this.column_srcTableId = column_srcTableId;
    }

    public void setColumn_destTableName(final String column_destTableName) {
        this.column_destTableName = column_destTableName;
    }

    public void setColumn_destTableId(final String column_destTableId) {
        this.column_destTableId = column_destTableId;
    }

    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
