/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */


package com.sourcen.core.persistence.domain.constructs;

/**
 * provides owner constructs for the entity.
 *
 * @param <T> datatype of the owner identifier.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public interface ParentAware<T> {

    /**
     * @return ownerId of this entity.
     */
    T getParent();

    /**
     * @param ownerId to be set for this entity.
     */
    void setParent(T ownerId);

}
