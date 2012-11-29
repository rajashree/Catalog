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

public class CampaignItemNotFoundException extends ACSException {

    private static final long serialVersionUID = -281519022344503929L;

    public CampaignItemNotFoundException() {
        super();
    }

    public CampaignItemNotFoundException(String msg) {
        super(msg);
    }

    public CampaignItemNotFoundException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public CampaignItemNotFoundException(Throwable throwable) {
        super(throwable);
    }
}