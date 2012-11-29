/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.upgrade.SeedDataInjector;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.util.FileSystem;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: sandeep $
 * @version $Revision: 3792 $, $Date:: 2012-07-03 12:11:59#$
 */
public class TestDataImportManagerImpl {

    protected static ApplicationContext applicationContext;
    protected static Logger logger = LoggerFactory.getLogger(TestDataImportManagerImpl.class);

    @BeforeClass
    public static void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext(new String[]{"/spring/applicationContext.xml"});
//        SeedDataInjector seedDataInjector = applicationContext.getBean("seedDataInjector", SeedDataInjector.class);
    }

    @Test
    public void testImportDataFile() {
        DataImportManager dataImportManager = applicationContext.getBean("dataImportManagerImpl", DataImportManager.class);
        DataFile dataFile = dataImportManager.getLatestImportedFile();
        if (dataFile != null) {
            dataImportManager.processImportedFile(dataFile);
        }
    }

    @Test
    public void testDataImportConfigFileUpload() {
        //Retailer site name - to be tested
        String retailerSiteName = "dell";
        //Import type for the retailer
        String dataImportType = "ficstar";
        ConfigurationService configurationService = applicationContext.getBean("configurationService", ConfigurationService.class);
        RetailerManager retailerManager = applicationContext.getBean("retailerManagerImpl", RetailerManager.class);
        RetailerSite retailerSite = retailerManager.getRetailerSitebyName(retailerSiteName);
        File schemaFile = null;
        String dataImportFileLocation = null;
        logger.info("RETAILER ----- " + retailerSite.getSiteName().toUpperCase());
        logger.info("DATA IMPORT TYPE ----- " + dataImportType.toUpperCase());
        if (dataImportType.equalsIgnoreCase("merchant")) {
            //load the merchant specific config file
            dataImportFileLocation = "/config/dataimport/providers/merchant/" + retailerSite.getRetailer().getName() + "/" + retailerSite.getSiteName() + "/data_import_config.xml";
        } else {
            //load the Provider config files  (FICSTAR,CJ,GOOGLE)
            dataImportFileLocation = "/config/dataimport/providers/" + dataImportType + "/data_import_config.xml";
        }
        try {
            schemaFile = configurationService.getFileSystem().getFile(dataImportFileLocation);
        } catch (IOException ioe) {
            logger.error(ioe.getMessage());
        }
        assertNotNull("Unable to load the schema file for *" + retailerSiteName + "* with Import Type as :=" + dataImportType, schemaFile);
    }

    public static void main(String[] args) {
        applicationContext = new ClassPathXmlApplicationContext(new String[]{"/spring/applicationContext.xml"});
        SeedDataInjector seedDataInjector = applicationContext.getBean("seedDataInjector", SeedDataInjector.class);
        DataImportManager dataImportManager = applicationContext.getBean("dataImportManagerImpl", DataImportManager.class);
        DataFile dataFile = dataImportManager.getLatestImportedFile();
        if (dataFile != null) {
            dataImportManager.processImportedFile(dataFile);
        }
    }
}
