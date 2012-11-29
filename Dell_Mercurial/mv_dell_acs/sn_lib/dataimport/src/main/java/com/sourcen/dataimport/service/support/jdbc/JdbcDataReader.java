/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service.support.jdbc;

import com.sourcen.dataimport.service.DataReader;
import com.sourcen.dataimport.service.support.BaseDataAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 @author Navin Raj Kumar G.S.
 @author $LastChangedBy: navinr $
 @version $Revision: 2651 $, $Date:: 2012-05-28 08:27:49#$ */
public class JdbcDataReader extends BaseDataAdapter implements DataReader {

    /**
     class fields
     */
    private SimpleJdbcTemplate sourceJdbcTemplate;

    /**
     default constructor.
     */
    public JdbcDataReader() {
    }

    @Override
    public void initialize() {
        super.initialize();
        if (tableDefinition.getSourceDataSource() != null) {
            sourceJdbcTemplate = JdbcUtils.getSimpleJdbcTemplate(applicationContext, tableDefinition);
        } else {
            sourceJdbcTemplate = new SimpleJdbcTemplate(srcDataSource);
        }
    }

    /**
     datareader
     */

    @Override
    public Collection<Map<String, Object>> getRows() {

        logger.info("Fetching records : " + tableDefinition.sqlSelect);
        profiler.markEvent("SQL_SELECT");
        Collection<Map<String, Object>> result = sourceJdbcTemplate.queryForList(tableDefinition.sqlSelect);
        Iterator<Map<String, Object>> interator = result.iterator();
        int i = 0;
        while (interator.hasNext()) {
            Map<String, Object> record = interator.next();
            processRowAfterExtraction(record, i);
            i++;
        }

        logger.info("Query returned recordCount:" + result.size());
        profiler.endEvent("SQL_SELECT", "Query returned recordCount:" + result.size());
        return result;
    }


    @Override
    public void processRowAfterExtraction(Map<String, Object> record, Integer recordIndex) {
    }

    /**
     injection
     */

    @Autowired
    private DataSource srcDataSource;

    public void setSrcDataSource(final DataSource srcDataSource) {
        this.srcDataSource = srcDataSource;
    }

}
