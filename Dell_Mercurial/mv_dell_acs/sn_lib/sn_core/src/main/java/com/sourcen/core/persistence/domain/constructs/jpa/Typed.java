/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain.constructs.jpa;

import com.sourcen.core.util.ObjectUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
@Embeddable
public class Typed<T> implements com.sourcen.core.persistence.domain.constructs.Typed<T> {

    @Column(nullable = false)
    private T type;

    public T getType() {
        return type;
    }

    public void setType(T type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TypedModel{" +
                "type=" + type +
                '}';
    }


    //
    // Object
    //

    @Override
    public int hashCode() {
        return ObjectUtils.hashCode(type);
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass().equals(com.sourcen.core.persistence.domain.constructs.Typed.class) && obj.hashCode() == this.hashCode();
    }
}
