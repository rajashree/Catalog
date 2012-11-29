package com.dell.acs;

/**
 * Created with IntelliJ IDEA.
 * User: MarketVine
 * Date: 7/19/12
 * Time: 1:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductNotFoundException extends ACSException {

    private static final long serialVersionUID = -281519022344503929L;

    public ProductNotFoundException() {
        super();
    }

    public ProductNotFoundException(String msg) {
        super(msg);
    }

    public ProductNotFoundException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public ProductNotFoundException(Throwable throwable) {
        super(throwable);
    }
}