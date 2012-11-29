package com.dell.acs.dataimport.model;

import com.dell.acs.persistence.domain.DataFile;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created with IntelliJ IDEA.
 * User: Shawn_Fisk
 * Date: 9/10/12
 * Time: 11:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class DataImportContextTest {
    @Test
    public void noError() {
        DataFile dataFile = mock(DataFile.class);
        DataImportContext context = new DataImportContext(dataFile);

        assertFalse("Context should not have errors", context.hasErrors());
        assertFalse("Context should not have fatal errors", context.isFatal());
        assertTrue("Context should be valid", context.isValid());
        assertEquals("Context should have the data file", context.getDataFile(), dataFile);
        assertNotNull("Context should be valid", context.getErrors());
        Iterable<DataImportError> errorIterator = context.getErrors();
        assertFalse("Context should not have any elements the error collection", context.getErrors().iterator().hasNext());
    }

    @Test
    public void handleException() {
        DataFile dataFile = mock(DataFile.class);
        DataImportContext context = new DataImportContext(dataFile);
        Throwable t = mock(Throwable.class);
        String msg = "Message: %s";
        Object arg = "handleExceptionTest";

        context.handleException(t, 1, 2, msg, arg);

        assertTrue("Context should have errors", context.hasErrors());
        assertTrue("Context should be fatal", context.isFatal());
        assertFalse("Context should be valid", context.isValid());
        assertEquals("Context should have the data file", context.getDataFile(), dataFile);
        assertNotNull("Context should be valid", context.getErrors());
        Iterable<DataImportError> errorIterator = context.getErrors();
        assertTrue("Context should have any elements the error collection", context.getErrors().iterator().hasNext());
        DataImportError dataImportError = context.getErrors().iterator().next();
        assertNotNull("DataImportError not should be null", dataImportError);
        assertEquals("DataImportError should be fatal", dataImportError.getServerity(), Error.Severity.FATAL);
        assertEquals("DataImportError have the throwable", dataImportError.getThrowable(), t);
        assertEquals("DataImportError have the correct row", dataImportError.getRow(), 1);
        assertEquals("DataImportError have the correct column", dataImportError.getColumn(), 2);
        assertEquals("DataImportError have the correct message", dataImportError.getMsg(), String.format(msg, arg));
    }

    @Test
    public void infoError() {
        DataFile dataFile = mock(DataFile.class);
        DataImportContext context = new DataImportContext(dataFile);
        String msg = "Message: %s";
        Object arg = "infoError";
        context.info(1, 2, msg, arg);

        assertTrue("Context should have errors", context.hasErrors());
        assertFalse("Context should not be fatal error", context.isFatal());
        assertTrue("Context should be valid", context.isValid());
        assertEquals("Context should have the data file", context.getDataFile(), dataFile);
        assertNotNull("Context should be valid", context.getErrors());
        Iterable<DataImportError> errorIterator = context.getErrors();
        assertTrue("Context should have any elements the error collection", context.getErrors().iterator().hasNext());
        DataImportError dataImportError = context.getErrors().iterator().next();
        assertNotNull("DataImportError should be null", dataImportError);
        assertEquals("DataImportError should be info", dataImportError.getServerity(), Error.Severity.INFO);
        assertNull("DataImportError throwable should be null", dataImportError.getThrowable());
        assertEquals("DataImportError have the correct row", dataImportError.getRow(), 1);
        assertEquals("DataImportError have the correct column", dataImportError.getColumn(), 2);
        assertEquals("DataImportError have the correct message", dataImportError.getMsg(), String.format(msg, arg));
    }

    @Test
    public void errorError() {
        DataFile dataFile = mock(DataFile.class);
        DataImportContext context = new DataImportContext(dataFile);
        String msg = "Message: %s";
        Object arg = "errorError";
        context.error(1, 2, msg, arg);

        assertTrue("Context should have errors", context.hasErrors());
        assertFalse("Context should not be fatal error", context.isFatal());
        assertFalse("Context should not be valid", context.isValid());
        assertEquals("Context should have the data file", context.getDataFile(), dataFile);
        assertNotNull("Context errors should not be null", context.getErrors());
        Iterable<DataImportError> errorIterator = context.getErrors();
        assertTrue("Context should have any elements the error collection", context.getErrors().iterator().hasNext());
        DataImportError dataImportError = context.getErrors().iterator().next();
        assertNotNull("DataImportError should be null", dataImportError);
        assertEquals("DataImportError should be error", dataImportError.getServerity(), Error.Severity.ERROR);
        assertNull("DataImportError throwable should be null", dataImportError.getThrowable());
        assertEquals("DataImportError have the correct row", dataImportError.getRow(), 1);
        assertEquals("DataImportError have the correct column", dataImportError.getColumn(), 2);
        assertEquals("DataImportError have the correct message", dataImportError.getMsg(), String.format(msg, arg));
    }

    @Test
    public void warningError() {
        DataFile dataFile = mock(DataFile.class);
        DataImportContext context = new DataImportContext(dataFile);
        String msg = "Message: %s";
        Object arg = "warningError";
        context.warning(1, 2, msg, arg);

        assertTrue("Context should have errors", context.hasErrors());
        assertFalse("Context should not be fatal error", context.isFatal());
        assertTrue("Context should be valid", context.isValid());
        assertEquals("Context should have the data file", context.getDataFile(), dataFile);
        assertNotNull("Context should be valid", context.getErrors());
        Iterable<DataImportError> errorIterator = context.getErrors();
        assertTrue("Context should have any elements the error collection", context.getErrors().iterator().hasNext());
        DataImportError dataImportError = context.getErrors().iterator().next();
        assertNotNull("DataImportError should be null", dataImportError);
        assertEquals("DataImportError should be warn", dataImportError.getServerity(), Error.Severity.WARNING);
        assertNull("DataImportError throwable should be null", dataImportError.getThrowable());
        assertEquals("DataImportError have the correct row", dataImportError.getRow(), 1);
        assertEquals("DataImportError have the correct column", dataImportError.getColumn(), 2);
        assertEquals("DataImportError have the correct message", dataImportError.getMsg(), String.format(msg, arg));
    }

    @Test
    public void defaultError() {
        DataFile dataFile = mock(DataFile.class);
        DataImportContext context = new DataImportContext(dataFile);
        String msg = "Message: %s";
        Object arg = "defaultError";
        context.def(1, 2, msg, arg);

        assertTrue("Context should have errors", context.hasErrors());
        assertFalse("Context should not be fatal error", context.isFatal());
        assertTrue("Context should be valid", context.isValid());
        assertEquals("Context should have the data file", context.getDataFile(), dataFile);
        assertNotNull("Context should be valid", context.getErrors());
        Iterable<DataImportError> errorIterator = context.getErrors();
        assertTrue("Context should have any elements the error collection", context.getErrors().iterator().hasNext());
        DataImportError dataImportError = context.getErrors().iterator().next();
        assertNotNull("DataImportError should be null", dataImportError);
        assertEquals("DataImportError should be default", dataImportError.getServerity(), Error.Severity.DEFAULT);
        assertNull("DataImportError throwable should be null", dataImportError.getThrowable());
        assertEquals("DataImportError have the correct row", dataImportError.getRow(), 1);
        assertEquals("DataImportError have the correct column", dataImportError.getColumn(), 2);
        assertEquals("DataImportError have the correct message", dataImportError.getMsg(), String.format(msg, arg));
    }
}