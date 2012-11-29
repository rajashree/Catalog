/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.sourcen.dataimport.definition.DataImportConfig;
import com.sourcen.dataimport.definition.Schema;
import com.sourcen.dataimport.definition.TableDefinition;
import com.sourcen.dataimport.service.DataReader;
import com.sourcen.dataimport.service.DataWriter;
import com.sourcen.dataimport.service.support.GenericDataImportService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 572 $, $Date:: 2012-03-08 10:26:09#$
 */
public class TestCSVDataExportAdapterImpl {

    protected static ApplicationContext applicationContext;
    protected static Logger logger = LoggerFactory.getLogger(TestCSVDataExportAdapterImpl.class);

    @BeforeClass
    public static void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext(new String[]{"/spring/test/applicationContext-testDataImport.xml"});
//        SeedDataInjector seedDataInjector = applicationContext.getBean("seedDataInjector", SeedDataInjector.class);
    }

    @Test
    public void testImportDataFile() {
        DataImportConfig dataImportConfig = applicationContext.getBean("dataImportConfig", DataImportConfig.class);
        dataImportConfig.setConfigFilePath(getClass().getResource("/config/data_import_config.xml").getFile());
        dataImportConfig.afterPropertiesSet();

        Schema schema = dataImportConfig.getSchema();
        TableDefinition tableDefinition = schema.getDefinitionByDestination("com.dell.acs.persistence.domain.Product");
        tableDefinition.setProperty("path", "/Volumes/WORK/dell_acs_work_dir/data_import/1_dell/1_dell/products-2012_01_01-00_00_00.csv");


        DataReader dataReader = applicationContext.getBean("csvDataReader", DataReader.class);
        DataWriter dataWriter = applicationContext.getBean("hibernateDataWriter", DataWriter.class);
        dataReader.setTableDefinition(tableDefinition);
        dataWriter.setTableDefinition(tableDefinition);

        GenericDataImportService dataImportService = applicationContext.getBean("genericDataImportService", GenericDataImportService.class);
        dataImportService.setDataImportConfig(dataImportConfig);
        dataImportService.setDataReader(dataReader);
        dataImportService.setDataWriter(dataWriter);
        dataReader.initialize();
        dataWriter.initialize();
        dataImportService.run();


//        Collection<Map<String, Object>> result = dataExportAdapter.getRows();
//        for (Map<String, Object> row : result) {
//            logger.info(Joiner.on(",").withKeyValueSeparator("=").join(row));
//        }
    }
}
