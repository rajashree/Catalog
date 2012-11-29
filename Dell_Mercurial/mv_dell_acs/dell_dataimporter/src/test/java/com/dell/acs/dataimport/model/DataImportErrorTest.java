package com.dell.acs.dataimport.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

/**
 * Created with IntelliJ IDEA.
 * User: Shawn_Fisk
 * Date: 9/10/12
 * Time: 11:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class DataImportErrorTest {
    @Test
    public void constructor1() {
        String msg = "Test1";
        DataImportError dataImportError = new DataImportError(2, 3, Error.Severity.ERROR, msg);

        assertEquals("DataImportError row should be correct", dataImportError.getRow(), 2);
        assertEquals("DataImportError column should be correct", dataImportError.getColumn(), 3);
        assertEquals("DataImportError severity should be correct", dataImportError.getServerity(), Error.Severity.ERROR);
        assertEquals("DataImportError message should be correct", dataImportError.getMsg(), msg);
        assertNull("DataImportError throwable should be correct", dataImportError.getThrowable());
        assertNotNull("DataImportError toString should be non-null", dataImportError.toString());
    }

    @Test
    public void constructor2() {
        String msg = "Test1";
        Throwable t = mock(Throwable.class);
        DataImportError dataImportError = new DataImportError(2, 3, Error.Severity.ERROR, msg, t);

        assertEquals("DataImportError row should be correct", dataImportError.getRow(), 2);
        assertEquals("DataImportError column should be correct", dataImportError.getColumn(), 3);
        assertEquals("DataImportError severity should be correct", dataImportError.getServerity(), Error.Severity.ERROR);
        assertEquals("DataImportError message should be correct", dataImportError.getMsg(), msg);
        assertEquals("DataImportError throwable should be correct", dataImportError.getThrowable(), t);
        assertNotNull("DataImportError toString should be non-null", dataImportError.toString());
    }
}
