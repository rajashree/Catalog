package com.dell.acs;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/26/12
 * Time: 12:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class CouponAlreadyExistsException extends ACSException {

    private static final long serialVersionUID = -281519022344503239L;

    public CouponAlreadyExistsException() {
        super();
    }

    public CouponAlreadyExistsException(String msg) {
        super(msg);
    }

    public CouponAlreadyExistsException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public CouponAlreadyExistsException(Throwable throwable) {
        super(throwable);
    }
}
