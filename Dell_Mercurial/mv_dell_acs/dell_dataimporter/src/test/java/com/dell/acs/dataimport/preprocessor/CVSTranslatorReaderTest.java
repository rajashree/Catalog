package com.dell.acs.dataimport.preprocessor;

import com.dell.acs.dataimport.CSVReaderException;
import com.dell.acs.testing.DellBaseTest;
import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: Shawn_Fisk
 * Date: 9/11/12
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class CVSTranslatorReaderTest {

    @Test
    public void constructor() {
        String userDir = DellBaseTest.getTestingRootDir();
        File filePath = null;

        try {
            filePath = new File(String.format(
                    "%s/testing/reader/products-2012_07_25_08_09_00.csv",
                    userDir));
        } catch(Throwable t) {
            fail("Failed to initialize the test");
        }
        String encoding = "UTF8";
        String separator = "\t";
        String quote = "\"";

        try {
            CVSTranslatorReader reader = new CVSTranslatorReader(filePath, encoding, separator, quote);
        } catch(CSVReaderException e) {
            fail("CVSTranslatorReader should not have thrown a exception");
        } catch(Throwable t) {
            fail("CVSTranslatorReader should not have thrown a exception");
        }
    }
}
