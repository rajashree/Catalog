/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.transformer;

/**
 * Created by IntelliJ IDEA.
 * User: Sandeep
 * Date: 2/27/12
 * Time: 4:27 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 ForeignKeyException class is the custom exception class which is throws at the time of invalid Forigen keys operations
 */
public class ForeignKeyException extends RuntimeException {

    /**
     ForeignKeyException defaults and parameterized constructores
     */
    public ForeignKeyException() {
    }

    public ForeignKeyException(String message) {
        super(message);
    }

    public ForeignKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForeignKeyException(Throwable cause) {
        super(cause);
    }
}
