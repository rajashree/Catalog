/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs;

/**
 Created by IntelliJ IDEA. User: Mahalakshmi Date: 7/25/12 Time: 10:44 PM To change this template use File | Settings |
 File Templates.
 */
public class CurationSourceTypeAlreadyExistsException extends ACSException {

    public CurationSourceTypeAlreadyExistsException() {
    }

    public CurationSourceTypeAlreadyExistsException(String msg) {
        super(msg);
    }

    public CurationSourceTypeAlreadyExistsException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public CurationSourceTypeAlreadyExistsException(Throwable throwable) {
        super(throwable);
    }
}
