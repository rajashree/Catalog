/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 572 $, $Date:: 2012-03-08 10:26:09#$
 */

/**
 * DataImportException is the Excpetion class
 * which object is thrown at he time of the data import process
 * during at failure
 */
public class DataImportException extends RuntimeException {

    /**
     * DataImportException constructors
     */
    public DataImportException() {
    }

    public DataImportException(String message) {
        super(message);
    }

    public DataImportException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataImportException(Throwable cause) {
        super(cause);
    }
}
