/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain.constructs;

/**
 * Containers are nothing but owners of entities, usually of Nested Sets type.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public interface ContainerAware {

    /**
     * @return get the owner of this entity.
     */
    Long getContainerId();

    /**
     * @param containerId set the owner of this entity.
     */
    void setContainerId(Long containerId);

}
