/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.upgrade;

import com.sourcen.core.persistence.util.SQLScriptTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Samee K.S
 * @author : sameeks $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */

/**
 * This is a generic task to push all Sprint-4 related schema changes.
 * Please ensure that the upgrade version is changed so that i doesn't run the same script again.
 *
 */

public class Sprint4UpgradeTask extends SQLScriptTask {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public void run() {
        logger.info("Executing - Sprint4: " + version +" task ");
        super.run();
        logger.info("Sprint4: " + version +" related schema update scripts executed successfully");
    }
}
