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
public class ParentAware<T> implements com.sourcen.core.persistence.domain.constructs.ParentAware<T> {

    @Column(nullable = true)
    private T parent;

    public T getParent() {
        return parent;
    }

    public void setParent(T parent) {
        this.parent = parent;
    }

    //
    // Object
    //

    @Override
    public int hashCode() {
        return ObjectUtils.hashCode(parent);
    }

    @Override
    public boolean equals(Object obj) {
        return com.sourcen.core.persistence.domain.constructs.ParentAware.class.isAssignableFrom(obj.getClass())
                && obj.hashCode() == this.hashCode();
    }

    @Override
    public String toString() {
        return "ParentAware{" +
                "parent=" + parent +
                '}';
    }
}
