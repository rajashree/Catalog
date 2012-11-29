/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core;

/**
 * All errors that can cause an instability of application execution should throw a SourcenError or a subclass of SourcenError.
 * These errors will never be caught, and might terminate the application execution.
 * <p/>
 * This will allow us to segregate application errors from core java errors.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 * @since 1.0
 */
public class SourcenError extends Error {

    //
    // =================================================================================================================
    // == Class Fields ==
    // =================================================================================================================
    //

    /**
     * Serializable.
     */
    private static final long serialVersionUID = 7958247378587760512L;

    //
    // =================================================================================================================
    // == Constructors ==
    // =================================================================================================================
    //

    /**
     * Constructs a new SourcenError with 'null' as its detail message.
     */
    public SourcenError() {
    }

    /**
     * Constructs an SourcenError class with the specified detail message.
     *
     * @param message the detail message.
     */
    public SourcenError(final String message) {
        super(message);
    }

    /**
     * Constructs a new SourcenError with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause of the exception
     */
    public SourcenError(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new SourcenError with the specified cause. The detailed message will be set to (cause==null ? null :
     * cause.toString());
     *
     * @param cause the cause of the exception
     */
    public SourcenError(final Throwable cause) {
        super(cause);
    }
}
