package com.dell.acs.dataimport.model;

import com.dell.acs.dataimport.DataImportServiceException;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created with IntelliJ IDEA.
 * User: Shawn_Fisk
 * Date: 9/11/12
 * Time: 8:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class ValidatorErrorTest {
    @Test
    public void constructor1() {
        Long entityId = 1L;
        String msg = "Message constructor1";
        ValidatorError error = new ValidatorError(entityId, Error.Severity.ERROR, msg);

        assertEquals("ValidatorError's entity should be correct", error.getEntityId(), entityId);
        assertEquals("ValidatorError's severity should be correct", error.getServerity(), Error.Severity.ERROR);
        assertEquals("ValidatorError's msg should be correct", error.getMsg(), msg);
        assertNull("ValidatorError's throwable should be null", error.getThrowable());
    }

    @Test
    public void constructor2() {
        Long entityId = 2L;
        String msg = "Message constructor1";
        DataImportServiceException throwable = mock(DataImportServiceException.class);
        ValidatorError error = new ValidatorError(entityId, Error.Severity.FATAL, msg, throwable);

        assertEquals("ValidatorError's entity should be correct", error.getEntityId(), entityId);
        assertEquals("ValidatorError's severity should be correct", error.getServerity(), Error.Severity.FATAL);
        assertEquals("ValidatorError's msg should be correct", error.getMsg(), msg);
        assertEquals("ValidatorError's throwable should be correct", error.getThrowable(), throwable);
    }
}
