/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service.support.jdbc;

import com.sourcen.dataimport.definition.TableDefinition;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 @author Navin Raj Kumar G.S.
 @author $LastChangedBy: adarsh $
 @version $Revision: 1728 $, $Date:: 2012-04-18 11:49:45#$ */
public final class JdbcUtils {

    private static final Map<String, SimpleJdbcTemplate> simpleJdbcTemplates =
            new ConcurrentHashMap<String, SimpleJdbcTemplate>(100);


    static SimpleJdbcTemplate getSimpleJdbcTemplate(ApplicationContext applicationContext,
                                                    TableDefinition tableDefinition) {
        String beanName = tableDefinition.getDestinationDataSource();
        if (simpleJdbcTemplates.containsKey(beanName)) {
            return simpleJdbcTemplates.get(beanName);
        }
        try {
            if (!applicationContext.containsBean(beanName)) {
                beanName = "dataSource";
            }

            DataSource newSourceDs = applicationContext.getBean(beanName, DataSource.class);
            SimpleJdbcTemplate template = new SimpleJdbcTemplate(newSourceDs);
            simpleJdbcTemplates.put(beanName, template);
            return template;
        } catch (BeansException e) {
            throw new RuntimeException("Unable to instantiate dataSource beanName:=" + beanName + " for table:"
                    + tableDefinition.getSourceTable(), e);
        }
    }
}
