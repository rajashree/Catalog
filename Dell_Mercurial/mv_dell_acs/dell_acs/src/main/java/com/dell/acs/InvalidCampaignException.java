/*
 * Copyright (c) SourceN Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs;

/**
 * @author Samee K.S
 * @author : skammar $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */

public class InvalidCampaignException extends ACSException {

    private static final long serialVersionUID = -281519022344503929L;

    public InvalidCampaignException() {
        super();
    }

    public InvalidCampaignException(String msg) {
        super(msg);
    }

    public InvalidCampaignException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public InvalidCampaignException(Throwable throwable) {
        super(throwable);
    }
}