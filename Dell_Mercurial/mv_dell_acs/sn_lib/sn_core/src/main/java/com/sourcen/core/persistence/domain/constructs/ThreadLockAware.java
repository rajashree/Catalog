/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain.constructs;

/**
 * Created by IntelliJ IDEA.
 * User: Sandeep
 * Date: 4/12/12
 * Time: 5:18 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ThreadLockAware extends VersionLockAware {

    String getLockedThread();

    void setLockedThread(String threadId);

}
