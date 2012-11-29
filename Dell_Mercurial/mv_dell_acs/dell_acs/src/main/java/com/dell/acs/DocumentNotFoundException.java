package com.dell.acs;

/**
 Created by IntelliJ IDEA. User: Mahalakshmi Date: 5/2/12 Time: 6:56 PM To change this template use File | Settings |
 File Templates.
 */
@Deprecated
public class DocumentNotFoundException extends ACSException {

    private static final long serialVersionUID = -281519022344503929L;

    public DocumentNotFoundException() {
        super();
    }

    public DocumentNotFoundException(String msg) {
        super(msg);
    }

    public DocumentNotFoundException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public DocumentNotFoundException(Throwable throwable) {
        super(throwable);
    }
}