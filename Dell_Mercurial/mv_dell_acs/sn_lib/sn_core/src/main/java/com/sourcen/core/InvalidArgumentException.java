/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class InvalidArgumentException extends SourcenRuntimeException {
    public InvalidArgumentException() {
    }

    public InvalidArgumentException(String paraName, Object paramValue) {
        super(getMessage(paraName, paramValue));
    }

    public InvalidArgumentException(String message) {
        super(message);
    }

    protected static String getMessage(String paraName, Object paramValue) {
        return "value :=" + paramValue + " passed for parmeter:=" + paraName + " is invlid";
    }

    public InvalidArgumentException(String paraName, Object paramValue, Throwable cause) {
        super(getMessage(paraName, paramValue), cause);
    }

}
