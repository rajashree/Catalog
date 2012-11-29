/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.config;

import com.sourcen.core.App;
import com.sourcen.core.test.SimpleTestCase;
import org.junit.Test;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2756 $, $Date:: 2012-05-29 20:43:55#$
 */
public class ConfigurationServiceTest extends SimpleTestCase {


    @Test
    public void testGetObjectProperty() throws Exception {
        ConfigurationService configurationService = App.getService(ConfigurationService.class);
        logger.info(String.valueOf(configurationService.getBooleanProperty("prop.booleanType", true)));
        logger.info(String.valueOf(configurationService.getIntegerProperty("prop.intType", 256)));
        logger.info(String.valueOf(configurationService.getLongProperty("prop.longType", 1230000L)));
        logger.info(String.valueOf(configurationService.hasProperty("prop.myProp")));
//        logger.info("changing profile to test1");
//        ProfileServiceImpl.getInstance().setCurrentProfile("test1");
//        logger.info(configurationService.getIntegerProperty("prop.intType", 256));
//        logger.info(configurationService.getIntegerProperty("prop.intType", 256));
//        configurationService.setProperty("prop.intType", 256);
    }


}
