/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain.impl.jpa;

import com.sourcen.core.persistence.domain.ObjectProperty;
import com.sourcen.core.persistence.domain.constructs.jpa.Typed;

import javax.persistence.Embedded;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class ObjectPropertyModel extends EntityPropertyModel implements ObjectProperty {

    @Embedded
    private Typed<Integer> typed;

    public ObjectPropertyModel() {

    }

    public ObjectPropertyModel(String name, String value) {
        super(name, value);
    }

    public ObjectPropertyModel(Typed<Integer> typed) {
        this.typed = typed;
    }

    @Override
    public Integer getType() {
        return typed.getType();
    }

    @Override
    public void setType(Integer type) {
        typed.setType(type);
    }
}
