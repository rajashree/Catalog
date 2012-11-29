/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs;

/**
 * Created with IntelliJ IDEA.
 * User: Chethan
 * Date: 4/9/12
 * Time: 6:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductReviewNotFoundException extends ACSException {

    private static final long serialVersionUID = -281519022344503929L;

    public ProductReviewNotFoundException() {
        super();
    }

    public ProductReviewNotFoundException(String msg) {
        super(msg);
    }

    public ProductReviewNotFoundException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public ProductReviewNotFoundException(Throwable throwable) {
        super(throwable);
    }
}