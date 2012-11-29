/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs;

/**
 * @author Samee K.S
 * @author : sameeks $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */

public class TagNotFoundException extends ACSException {

    public TagNotFoundException() {
        super();
    }

    public TagNotFoundException(String msg) {
        super(msg);
    }

    public TagNotFoundException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public TagNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
