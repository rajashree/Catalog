package com.dell.acs;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/26/12
 * Time: 12:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class CurationAlreadyExistsException extends ACSException {

    private static final long serialVersionUID = -281519022344503239L;

    public CurationAlreadyExistsException() {
        super();
    }

    public CurationAlreadyExistsException(String msg) {
        super(msg);
    }

    public CurationAlreadyExistsException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public CurationAlreadyExistsException(Throwable throwable) {
        super(throwable);
    }
}
