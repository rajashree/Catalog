package com.dell.acs;

/**
 * Created with IntelliJ IDEA.
 * User: MarketVine
 * Date: 7/19/12
 * Time: 12:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductDisabledException extends ACSException {

    private static final long serialVersionUID = -281519022344503929L;

    public ProductDisabledException() {
        super();
    }

    public ProductDisabledException(String msg) {
        super(msg);
    }

    public ProductDisabledException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public ProductDisabledException(Throwable throwable) {
        super(throwable);
    }
}
