package com.dell.acs.curation;

import com.dell.acs.ACSException;

/** @author Navin Raj Kumar G.S. */
public class ExecutorException extends ACSException {

    public ExecutorException() {
    }

    public ExecutorException(final String msg) {
        super(msg);
    }

    public ExecutorException(final String msg, final Throwable throwable) {
        super(msg, throwable);
    }

    public ExecutorException(final Throwable throwable) {
        super(throwable);
    }
}
