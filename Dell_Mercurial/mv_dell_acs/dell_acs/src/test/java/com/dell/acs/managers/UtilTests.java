/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.sourcen.core.util.FileSystem;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 572 $, $Date:: 2012-03-08 10:26:09#$
 */
public class UtilTests {

    private static final Logger logger = LoggerFactory.getLogger(UtilTests.class);

    @Test
    public void testMD5() {
//        Dell	oc=bqct44f&model_id=vostro-3450
        //logger.debug(StringUtils.MD5AsHex("Dell,oc=bqct44f&model_id=vostro-3450"));

        //Dell,oc=bqct44f&model_id=vostro-3450
        //Dell,oc=bqct44f&model_id=vostro-3450

//        logger.debug(new Date(1328546226L));
//        logger.debug(new Date(1328546226L * 100));
//        TimeZone timeZone =Calendar.getInstance().getTimeZone();
//        logger.debug();
//        timeZone.getOffset()
//        logger.debug( new Date( (1328546226L * 1000) - timeZone.getRawOffset()));
    }

    @Test
    public void fileSystemTest() throws Exception {
//       String fileSystemPath = "/Volumes/WORK/dell_acs_work_dir/";

//        logger.debug(new File(fileSystemPath).getAbsoluteFile());
        FileSystem fileSystem = new FileSystem("/Volumes/WORK/dell_acs_work_dir/");

        logger.debug(fileSystem.getTempDirectory().getAbsolutePath());

    }

}
