/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.repository.impl.hibernate;

import com.sourcen.core.persistence.domain.ObjectProperty;
import com.sourcen.core.persistence.domain.impl.jpa.ObjectPropertyModel;
import com.sourcen.core.persistence.repository.ObjectPropertyRepository;

import java.util.List;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class ObjectPropertyRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, ObjectProperty>
        implements ObjectPropertyRepository {

    public ObjectPropertyRepositoryImpl() {
        super(ObjectPropertyModel.class);
    }

    @Override
    public List<ObjectProperty> getAllForType(Integer objectTypeId) {
        ObjectProperty template = new ObjectPropertyModel();
        template.setType(objectTypeId);
        return getByExample(template);
    }

    @Override
    public ObjectProperty get(Integer objectTypeId, String name) {
        ObjectProperty template = new ObjectPropertyModel();
        template.setType(objectTypeId);
        template.setName(name);
        return getUniqueByExample(template);
    }

    @Override
    public void remove(Integer objectTypeId, String pattern) {
        ObjectProperty record = get(objectTypeId, pattern);
        if (record != null) {
            super.remove(record);
        }
    }
}
