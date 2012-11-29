/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs;

import com.sourcen.core.ObjectNotFoundException;

/**
 * @author Samee K.S
 * @author : sameeks $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */

public class CurationNotFoundException extends ObjectNotFoundException {
    private static final long serialVersionUID = -281519022344503239L;

    public CurationNotFoundException() {
        super();
    }

    public CurationNotFoundException(String msg) {
        super(msg);
    }

    public CurationNotFoundException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public CurationNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
