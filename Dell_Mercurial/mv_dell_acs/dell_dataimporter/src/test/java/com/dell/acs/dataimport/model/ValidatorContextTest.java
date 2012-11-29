package com.dell.acs.dataimport.model;

import com.dell.acs.dataimport.DataImportServiceException;
import com.dell.acs.persistence.domain.DataFile;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created with IntelliJ IDEA.
 * User: Shawn_Fisk
 * Date: 9/11/12
 * Time: 8:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class ValidatorContextTest {
    @Test
    public void constructor() {
        DataFile dataFile = mock(DataFile.class);
        int row = 1;
        ValidatorContext context = new ValidatorContext(dataFile, row);

        assertEquals("ValidatorContext's dataFile should be correct", context.getDataFile(), dataFile);
        assertEquals("ValidatorContext's row should be correct", context.getRow(), 1);
    }

    @Test
    public void notNullPositive() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        Object notNullValue = new Object();
        Object nullValue = null;
        String notNullMsg = "Value should not be null";
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.notNull(entityId, notNullValue, Error.Severity.ERROR, notNullMsg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 0);
        assertFalse("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertTrue("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    @Test
    public void notNullNegative1() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        Object nullValue = null;
        String notNullMsg = "Value should not be null";
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.notNull(entityId, nullValue, Error.Severity.ERROR, notNullMsg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 1);
        Iterator<ValidatorError> iterator = context.getErrors().iterator();
        assertTrue("ValidatorContext's should a error in the iterator.", iterator.hasNext());
        ValidatorError error1 = iterator.next();
        assertNotNull("ValidatorContext's the error from the iterator should be non null", error1);
        assertFalse("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertFalse("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    @Test
    public void notNullNegative2a() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        Object nullValue = null;
        String notNullMsg = "Value should not be null";
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.notNull(entityId, nullValue, Error.Severity.DEFAULT, notNullMsg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 1);
        Iterator<ValidatorError> iterator = context.getErrors().iterator();
        assertTrue("ValidatorContext's should a error in the iterator.", iterator.hasNext());
        ValidatorError error1 = iterator.next();
        assertNotNull("ValidatorContext's the error from the iterator should be non null", error1);
        assertFalse("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertTrue("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    @Test
    public void notNullNegative2b() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        Object nullValue = null;
        String notNullMsg = "Value should not be null";
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.notNull(entityId, nullValue, Error.Severity.INFO, notNullMsg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 1);
        Iterator<ValidatorError> iterator = context.getErrors().iterator();
        assertTrue("ValidatorContext's should a error in the iterator.", iterator.hasNext());
        ValidatorError error1 = iterator.next();
        assertNotNull("ValidatorContext's the error from the iterator should be non null", error1);
        assertFalse("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertTrue("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    @Test
    public void notNullNegative2c() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        Object nullValue = null;
        String notNullMsg = "Value should not be null";
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.notNull(entityId, nullValue, Error.Severity.WARNING, notNullMsg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 1);
        Iterator<ValidatorError> iterator = context.getErrors().iterator();
        assertTrue("ValidatorContext's should a error in the iterator.", iterator.hasNext());
        ValidatorError error1 = iterator.next();
        assertNotNull("ValidatorContext's the error from the iterator should be non null", error1);
        assertFalse("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertTrue("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    @Test
    public void notNullNegative3() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        Object nullValue = null;
        String notNullMsg = "Value should not be null";
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.notNull(entityId, nullValue, Error.Severity.FATAL, notNullMsg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 1);
        Iterator<ValidatorError> iterator = context.getErrors().iterator();
        assertTrue("ValidatorContext's should a error in the iterator.", iterator.hasNext());
        ValidatorError error1 = iterator.next();
        assertNotNull("ValidatorContext's the error from the iterator should be non null", error1);
        assertTrue("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertFalse("ValidatorContext's isValid should be the correct value.", context.isValid());
    }


    @Test
    public void notEmptyStringPositive() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        String notNullEmpty = "value";
        String notEmptyStringMsg = "Value should not be empty";
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.notEmptyString(entityId, notNullEmpty, Error.Severity.ERROR, notEmptyStringMsg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 0);
        assertFalse("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertTrue("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    @Test
    public void notEmptyStringNegative1a() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        String nullString = null;
        String notEmptyStringMsg = "Value should not be empty";
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.notEmptyString(entityId, nullString, Error.Severity.ERROR, notEmptyStringMsg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 1);
        Iterator<ValidatorError> iterator = context.getErrors().iterator();
        assertTrue("ValidatorContext's should a error in the iterator.", iterator.hasNext());
        ValidatorError error1 = iterator.next();
        assertNotNull("ValidatorContext's the error from the iterator should be non null", error1);
        assertFalse("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertFalse("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    @Test
    public void notEmptyStringNegative1b() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        String emptyString = "";
        String notEmptyStringMsg = "Value should not be empty";
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.notEmptyString(entityId, emptyString, Error.Severity.ERROR, notEmptyStringMsg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 1);
        Iterator<ValidatorError> iterator = context.getErrors().iterator();
        assertTrue("ValidatorContext's should a error in the iterator.", iterator.hasNext());
        ValidatorError error1 = iterator.next();
        assertNotNull("ValidatorContext's the error from the iterator should be non null", error1);
        assertFalse("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertFalse("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    @Test
    public void notEmptyStringNegative2a() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        String nullString = null;
        String notEmptyStringMsg = "Value should not be empty";
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.notEmptyString(entityId, nullString, Error.Severity.DEFAULT, notEmptyStringMsg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 1);
        Iterator<ValidatorError> iterator = context.getErrors().iterator();
        assertTrue("ValidatorContext's should a error in the iterator.", iterator.hasNext());
        ValidatorError error1 = iterator.next();
        assertNotNull("ValidatorContext's the error from the iterator should be non null", error1);
        assertFalse("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertTrue("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    @Test
    public void notEmptyStringNegative2b() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        String nullString = null;
        String notEmptyStringMsg = "Value should not be empty";
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.notEmptyString(entityId, nullString, Error.Severity.INFO, notEmptyStringMsg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 1);
        Iterator<ValidatorError> iterator = context.getErrors().iterator();
        assertTrue("ValidatorContext's should a error in the iterator.", iterator.hasNext());
        ValidatorError error1 = iterator.next();
        assertNotNull("ValidatorContext's the error from the iterator should be non null", error1);
        assertFalse("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertTrue("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    @Test
    public void notEmptyStringNegative2c() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        String nullString = null;
        String notEmptyStringMsg = "Value should not be empty";
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.notEmptyString(entityId, nullString, Error.Severity.WARNING, notEmptyStringMsg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 1);
        Iterator<ValidatorError> iterator = context.getErrors().iterator();
        assertTrue("ValidatorContext's should a error in the iterator.", iterator.hasNext());
        ValidatorError error1 = iterator.next();
        assertNotNull("ValidatorContext's the error from the iterator should be non null", error1);
        assertFalse("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertTrue("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    @Test
    public void notEmptyStringNegative3() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        String nullString = null;
        String notEmptyStringMsg = "Value should not be empty";
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.notEmptyString(entityId, nullString, Error.Severity.FATAL, notEmptyStringMsg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 1);
        Iterator<ValidatorError> iterator = context.getErrors().iterator();
        assertTrue("ValidatorContext's should a error in the iterator.", iterator.hasNext());
        ValidatorError error1 = iterator.next();
        assertNotNull("ValidatorContext's the error from the iterator should be non null", error1);
        assertTrue("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertFalse("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    @Test
    public void lessThanOrEqualPositivea() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        Float floatValue = 3.4f;
        String lessThanOrEqualMsg = "Value should less than or equal to specified value";
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.lessThanOrEqual(entityId, floatValue, 4.0f, Error.Severity.ERROR, lessThanOrEqualMsg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 0);
        assertFalse("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertTrue("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    @Test
    public void lessThanOrEqualPositiveb() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        Float floatValue = 3.4f;
        String lessThanOrEqualMsg = "Value should less than or equal to specified value";
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.lessThanOrEqual(entityId, floatValue, 3.4f, Error.Severity.ERROR, lessThanOrEqualMsg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 0);
        assertFalse("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertTrue("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    @Test
    public void lessThanOrEqualNegative1a() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        Float floatValue = 3.4f;
        String lessThanOrEqualMsg = "Value should less than or equal to specified value";
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.lessThanOrEqual(entityId, floatValue, 3.0f, Error.Severity.ERROR, lessThanOrEqualMsg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 1);
        Iterator<ValidatorError> iterator = context.getErrors().iterator();
        assertTrue("ValidatorContext's should a error in the iterator.", iterator.hasNext());
        ValidatorError error1 = iterator.next();
        assertNotNull("ValidatorContext's the error from the iterator should be non null", error1);
        assertFalse("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertFalse("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    @Test
    public void lessThanOrEqualNegative2a() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        Float floatValue = 3.4f;
        String lessThanOrEqualMsg = "Value should less than or equal to specified value";
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.lessThanOrEqual(entityId, floatValue, 3.0f, Error.Severity.DEFAULT, lessThanOrEqualMsg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 1);
        Iterator<ValidatorError> iterator = context.getErrors().iterator();
        assertTrue("ValidatorContext's should a error in the iterator.", iterator.hasNext());
        ValidatorError error1 = iterator.next();
        assertNotNull("ValidatorContext's the error from the iterator should be non null", error1);
        assertFalse("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertTrue("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    @Test
    public void lessThanOrEqualNegative2b() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        Float floatValue = 3.4f;
        String lessThanOrEqualMsg = "Value should less than or equal to specified value";
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.lessThanOrEqual(entityId, floatValue, 3.0f, Error.Severity.INFO, lessThanOrEqualMsg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 1);
        Iterator<ValidatorError> iterator = context.getErrors().iterator();
        assertTrue("ValidatorContext's should a error in the iterator.", iterator.hasNext());
        ValidatorError error1 = iterator.next();
        assertNotNull("ValidatorContext's the error from the iterator should be non null", error1);
        assertFalse("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertTrue("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    @Test
    public void lessThanOrEqualNegative2c() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        Float floatValue = 3.4f;
        String lessThanOrEqualMsg = "Value should less than or equal to specified value";
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.lessThanOrEqual(entityId, floatValue, 3.0f, Error.Severity.WARNING, lessThanOrEqualMsg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 1);
        Iterator<ValidatorError> iterator = context.getErrors().iterator();
        assertTrue("ValidatorContext's should a error in the iterator.", iterator.hasNext());
        ValidatorError error1 = iterator.next();
        assertNotNull("ValidatorContext's the error from the iterator should be non null", error1);
        assertFalse("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertTrue("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    @Test
    public void lessThanOrEqualNegative3() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        Float floatValue = 3.4f;
        String lessThanOrEqualMsg = "Value should less than or equal to specified value";
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.lessThanOrEqual(entityId, floatValue, 3.0f, Error.Severity.FATAL, lessThanOrEqualMsg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 1);
        Iterator<ValidatorError> iterator = context.getErrors().iterator();
        assertTrue("ValidatorContext's should a error in the iterator.", iterator.hasNext());
        ValidatorError error1 = iterator.next();
        assertNotNull("ValidatorContext's the error from the iterator should be non null", error1);
        assertTrue("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertFalse("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    @Test
    public void handleException() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        String unableHandleMsg = "Unable to handle exception.";
        DataImportServiceException throwable = mock(DataImportServiceException.class);
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.handleException(entityId, unableHandleMsg, throwable);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 1);
        Iterator<ValidatorError> iterator = context.getErrors().iterator();
        assertTrue("ValidatorContext's should a error in the iterator.", iterator.hasNext());
        ValidatorError error1 = iterator.next();
        assertNotNull("ValidatorContext's the error from the iterator should be non null", error1);
        assertTrue("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertFalse("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    @Test
    public void invalida() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        String invalidMsg = "Found invalid value: %s";
        String invalidArg = "invalida";
        DataImportServiceException throwable = mock(DataImportServiceException.class);
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.invalid(entityId, Error.Severity.FATAL, invalidMsg, invalidArg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 1);
        Iterator<ValidatorError> iterator = context.getErrors().iterator();
        assertTrue("ValidatorContext's should a error in the iterator.", iterator.hasNext());
        ValidatorError error1 = iterator.next();
        assertNotNull("ValidatorContext's the error from the iterator should be non null", error1);
        assertTrue("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertFalse("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    @Test
    public void invalidb() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        String invalidMsg = "Found invalid value: %s";
        String invalidArg = "invalidb";
        DataImportServiceException throwable = mock(DataImportServiceException.class);
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.invalid(entityId, Error.Severity.ERROR, invalidMsg, invalidArg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 1);
        Iterator<ValidatorError> iterator = context.getErrors().iterator();
        assertTrue("ValidatorContext's should a error in the iterator.", iterator.hasNext());
        ValidatorError error1 = iterator.next();
        assertNotNull("ValidatorContext's the error from the iterator should be non null", error1);
        assertFalse("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertFalse("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    @Test
    public void invalidc() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        String invalidMsg = "Found invalid value: %s";
        String invalidArg = "invalidc";
        DataImportServiceException throwable = mock(DataImportServiceException.class);
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.invalid(entityId, Error.Severity.WARNING, invalidMsg, invalidArg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 1);
        Iterator<ValidatorError> iterator = context.getErrors().iterator();
        assertTrue("ValidatorContext's should a error in the iterator.", iterator.hasNext());
        ValidatorError error1 = iterator.next();
        assertNotNull("ValidatorContext's the error from the iterator should be non null", error1);
        assertFalse("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertTrue("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    @Test
    public void invalidd() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        String invalidMsg = "Found invalid value: %s";
        String invalidArg = "invalidd";
        DataImportServiceException throwable = mock(DataImportServiceException.class);
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.invalid(entityId, Error.Severity.INFO, invalidMsg, invalidArg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 1);
        Iterator<ValidatorError> iterator = context.getErrors().iterator();
        assertTrue("ValidatorContext's should a error in the iterator.", iterator.hasNext());
        ValidatorError error1 = iterator.next();
        assertNotNull("ValidatorContext's the error from the iterator should be non null", error1);
        assertFalse("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertTrue("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    @Test
    public void invalide() {
        DataFile dataFile = mock(DataFile.class);
        int row = 2;
        Long entityId = 3L;
        String invalidMsg = "Found invalid value: %s";
        String invalidArg = "invalide";
        DataImportServiceException throwable = mock(DataImportServiceException.class);
        ValidatorContext context = new ValidatorContext(dataFile, row);

        context.invalid(entityId, Error.Severity.INFO, invalidMsg, invalidArg);

        assertEquals("ValidatorContext's should have the correct number errors", this.countErrors(context), 1);
        Iterator<ValidatorError> iterator = context.getErrors().iterator();
        assertTrue("ValidatorContext's should a error in the iterator.", iterator.hasNext());
        ValidatorError error1 = iterator.next();
        assertNotNull("ValidatorContext's the error from the iterator should be non null", error1);
        assertFalse("ValidatorContext's isFatal should be the correct value.", context.isFatal());
        assertTrue("ValidatorContext's isValid should be the correct value.", context.isValid());
    }

    private int countErrors(ValidatorContext context) {
        int result  = 0;
        assertNotNull("ValidatorContext's getErrors should return a valid iterator", context.getErrors());
        for(ValidatorError error : context.getErrors()) {
            result++;
        }
        return result;
    }
}