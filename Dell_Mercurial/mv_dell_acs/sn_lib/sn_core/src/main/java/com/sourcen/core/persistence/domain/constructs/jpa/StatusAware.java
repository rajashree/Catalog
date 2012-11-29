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
public class StatusAware<T> implements com.sourcen.core.persistence.domain.constructs.StatusAware<T> {

    @Column(nullable = false)
    private T status;

    public T getStatus() {
        return status;
    }

    public void setStatus(T status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StatusAwareModel{" +
                "type=" + status +
                '}';
    }


    //
    // Object
    //

    @Override
    public int hashCode() {
        return ObjectUtils.hashCode(status);
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass().equals(com.sourcen.core.persistence.domain.constructs.StatusAware.class) && obj.hashCode() == this.hashCode();
    }
}
