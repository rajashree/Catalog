package com.dell.acs;

/**
 Created by IntelliJ IDEA. User: Mahalakshmi Date: 5/2/12 Time: 3:06 PM To change this template use File | Settings |
 File Templates.
 */
@Deprecated
public class DocumentAlreadyExistsException extends ACSException {

    public DocumentAlreadyExistsException() {
        super();
    }

    public DocumentAlreadyExistsException(String msg) {
        super(msg);
    }

    public DocumentAlreadyExistsException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public DocumentAlreadyExistsException(Throwable throwable) {
        super(throwable);
    }
}
