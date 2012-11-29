/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs;

/**
 @author Ashish
 @author $LastChangedBy: navinr $
 @version $Revision: 2705 $, $Date:: 2012-05-29 10:#$ */

public class EventNotFoundException extends ACSException {

    private static final long serialVersionUID = -281519022344503929L;

    public EventNotFoundException() {
        super();
    }

    public EventNotFoundException(String msg) {
        super(msg);
    }

    public EventNotFoundException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public EventNotFoundException(Throwable throwable) {
        super(throwable);
    }

}
