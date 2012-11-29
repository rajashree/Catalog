/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain;


import com.sourcen.core.persistence.domain.constructs.VersionLockAware;

import java.io.Serializable;

/**
 * An entity that has a unique Identifier that.
 *
 * @param <K> datatype of the identifier.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2699 $, $Date:: 2012-05-29 10:00:40#$
 * @since 1.0
 */
public interface IdentifiableEntity<K extends Serializable> extends Entity, VersionLockAware {

    /**
     * @return the unique Identifier for this entity.
     */
    K getId();

    /**
     * @param id to be set for this entity.
     */
    void setId(K id);

}
