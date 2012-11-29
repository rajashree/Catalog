package com.sourcen.core;

/**
 * This exception can be thrown when a specific handler is not found for execution.
 * Handlers are objects which specialize in handling and executing operations.
 *
 * @author Navin Raj Kumar G.S.
 */
public class HandlerNotFoundException extends ObjectNotFoundException {

    public HandlerNotFoundException() {
    }

    public HandlerNotFoundException(final String message) {
        super(message);
    }

    public HandlerNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public HandlerNotFoundException(final Throwable cause) {
        super(cause);
    }
}
