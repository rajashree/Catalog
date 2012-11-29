/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */


package com.sourcen.core.persistence.domain.constructs;

/**
 * Entity Type Definition.
 *
 * @param <T> datatype of integer.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public interface Typed<T> {

    /**
     * @return of entity.q
     */
    T getType();

    /**
     * @param type of the entity. entity types must be defined in the extending Entity. Entity.Type.CREATED etc. Could
     *             be a final Integer that developers can access
     */
    void setType(T type);

}
