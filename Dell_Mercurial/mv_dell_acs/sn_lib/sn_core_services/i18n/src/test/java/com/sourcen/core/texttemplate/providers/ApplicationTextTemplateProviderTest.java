/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.texttemplate.providers;

import com.sourcen.core.App;
import com.sourcen.core.texttemplate.TextTemplateService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class ApplicationTextTemplateProviderTest {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationTextTemplateProviderTest.class);

    @Test
    public void testGetMessage0() {
        TextTemplateService service = App.getService(TextTemplateService.class);
        //service.addProvider(6,new I18NResourceBundleTemplateProvider("mymessages"));
//        logger.info(service.get18NProvider().get("test.hello10", new Object[]{"a", "b", "c", "d"}));
//
//        logger.info(service.get18NProvider().get("test.hello0", new Object[]{"a", "b", "c", "d"}));
//        logger.info(service.get18NProvider().get("test.hello1", new Object[]{"a", "b", "c", "d"}));
//        logger.info(service.get18NProvider().get("test.hello2", new Object[]{123, 1234, new Date(), "d"}));
//        logger.info(service.get18NProvider().get("test.hello3", new Object[]{123, 1234, new Date(), "d"}));
//
//        logger.info(service.get18NProvider().get("test.hello0", Locale.FRENCH, new Object[]{"a", "b", "c", "d"}));
//        logger.info(service.get18NProvider().get("test.hello1", Locale.FRENCH, new Object[]{"a", "b", "c", "d"}));
//        logger.info(service.get18NProvider().get("test.hello2", Locale.FRENCH, new Object[]{123, 1234, new Date(), "d"}));
//        logger.info(service.get18NProvider().get("test.hello3", Locale.FRENCH, new Object[]{123, 1234, new Date(), "d"}));


    }


}
