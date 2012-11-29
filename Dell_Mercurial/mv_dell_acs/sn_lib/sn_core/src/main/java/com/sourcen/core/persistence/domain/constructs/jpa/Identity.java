/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain.constructs.jpa;

import com.sourcen.core.persistence.domain.IdentifiableEntity;
import com.sourcen.core.persistence.domain.constructs.VersionLockAware;
import com.sourcen.core.util.ObjectUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import java.io.Serializable;


/**
 * @param <K> the datatype of the Enitity Identifier.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
@Embeddable
public class Identity<K extends Serializable> implements IdentifiableEntity<K>, VersionLockAware {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(insertable = true, nullable = false, unique = true, updatable = true)
    private K id;

    @Version
    @Column
    private Long version;


    public K getId() {
        return id;
    }

    public void setId(K id) {
        this.id = id;
    }


    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return ObjectUtils.toString(this);
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hashCode(id, version);
    }

    @Override
    public boolean equals(Object o1) {
        return (o1.getClass().isAssignableFrom(IdentifiableEntity.class) && o1.hashCode() == this.hashCode());
    }

}
