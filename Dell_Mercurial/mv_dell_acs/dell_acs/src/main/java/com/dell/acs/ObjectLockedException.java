package com.dell.acs;

/** @author Navin Raj Kumar G.S. */
public class ObjectLockedException extends ACSException {

    public ObjectLockedException() {
    }

    public ObjectLockedException(final String msg) {
        super(msg);
    }

    public ObjectLockedException(final String msg, final Throwable throwable) {
        super(msg, throwable);
    }

    public ObjectLockedException(final Throwable throwable) {
        super(throwable);
    }
}
