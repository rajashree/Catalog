/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain.constructs;

/**
 * A simple interface that all Entities extend to provide hibernate style optimistic locking.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 533 $, $Date:: 2012-03-07 05:28:35#$
 * @since 1.0
 */
public interface VersionLockAware {

    /**
     * @return the version-lock for this entity.
     */
    Long getVersion();

    /**
     * @param version lock to be set for this entity.
     */
    void setVersion(Long version);

}
