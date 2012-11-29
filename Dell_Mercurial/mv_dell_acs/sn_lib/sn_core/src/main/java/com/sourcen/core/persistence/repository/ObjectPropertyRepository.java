/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.repository;

import com.sourcen.core.persistence.domain.ObjectProperty;

import java.util.List;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public interface ObjectPropertyRepository extends IdentifiableEntityRepository<Long, ObjectProperty> {

    List<ObjectProperty> getAllForType(Integer objectTypeId);

    ObjectProperty get(Integer objectTypeId, String name);

    void remove(Integer objectTypeId, String pattern);
}
