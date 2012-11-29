/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */


package com.sourcen.core.cache;


/**
 * Cache exceptions are thrown by cache mangers, and cache provider implementations of the db like ibatis, hibernate
 * etc...
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 * @since 1.01
 */
public final class CacheException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 7356281129528914598L;

    /**
     * Constructs a new <code>CacheException</code> with <code>null</code> as its detail message.
     */
    public CacheException() {
    }

    /**
     * Constructs a new <code>CacheException</code> with the specified cause.
     * <p/>
     * The detailed message will be set to (cause==null ? null : cause.toString());
     *
     * @param cause the cause of the exception
     */
    public CacheException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an <code>CacheException</code> class with the specified detail message.
     *
     * @param message the detail message.
     */
    public CacheException(final String message) {
        super(message);
    }

    /**
     * Constructs a new <code>CacheException</code> with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause of the exception
     */
    public CacheException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
