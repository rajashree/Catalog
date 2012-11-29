package com.dell.acs;
/*
 * Copyright (c) SourceN Inc. 2004-2012
 * All rights reserved.
 */

import com.sourcen.core.SourcenException;

/**
 * @author Samee K.S
 * @author skammar: svnName $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */

public class ACSException extends SourcenException {
    public ACSException() {
        super();
    }

    public ACSException(String msg) {
        super(msg);
    }

    public ACSException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public ACSException(Throwable throwable) {
        super(throwable);
    }
}
