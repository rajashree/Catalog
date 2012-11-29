/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util.proxy;


public class SourcenProxyException extends RuntimeException {

    private static final long serialVersionUID = -7183773828002633222L;

    public SourcenProxyException() {
        super();
    }

    public SourcenProxyException(final String message) {
        super(message);
    }

    public SourcenProxyException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SourcenProxyException(final Throwable cause) {
        super(cause);
    }

}
