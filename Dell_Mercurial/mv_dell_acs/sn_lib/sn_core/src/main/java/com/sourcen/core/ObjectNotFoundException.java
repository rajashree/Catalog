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
public class ObjectNotFoundException extends SourcenRuntimeException {
    public ObjectNotFoundException() {
    }

    public ObjectNotFoundException(final String message) {
        super(message);
    }

    public ObjectNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ObjectNotFoundException(final Throwable cause) {
        super(cause);
    }
}
