/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs;

/**
 Created by IntelliJ IDEA. User: Ashish Date: 5/2/12 Time: 11:16 AM To change this template use File | Settings | File
 Templates.
 */
public class EventAlreadyExistException extends ACSException {

    private static final long serialVersionUID = -281519022344503929L;

    public EventAlreadyExistException() {
        super();
    }

    public EventAlreadyExistException(String msg) {
        super(msg);
    }

    public EventAlreadyExistException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public EventAlreadyExistException(Throwable throwable) {
        super(throwable);
    }

}
