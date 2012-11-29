/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs;

/**
 * Created by IntelliJ IDEA.
 * User: Sandeep
 * Date: 3/29/12
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class CampaignCategoryAlreadyExistsException extends ACSException {

    public CampaignCategoryAlreadyExistsException() {
    }

    public CampaignCategoryAlreadyExistsException(String msg) {
        super(msg);
    }

    public CampaignCategoryAlreadyExistsException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public CampaignCategoryAlreadyExistsException(Throwable throwable) {
        super(throwable);
    }
}
