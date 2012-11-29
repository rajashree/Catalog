/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs;

/**
 * Created by IntelliJ IDEA.
 * User: Sandeep
 * Date: 3/29/12
 * Time: 2:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class CampaignCategoryNotFoundException extends ACSException {

    public CampaignCategoryNotFoundException() {
    }

    public CampaignCategoryNotFoundException(String msg) {
        super(msg);
    }

    public CampaignCategoryNotFoundException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public CampaignCategoryNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
