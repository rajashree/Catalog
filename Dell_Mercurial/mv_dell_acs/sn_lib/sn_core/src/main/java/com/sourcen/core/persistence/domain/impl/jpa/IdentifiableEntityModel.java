/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain.impl.jpa;

import com.sourcen.core.persistence.domain.IdentifiableEntity;
import com.sourcen.core.util.ObjectUtils;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.UniqueConstraint;

import org.springframework.context.annotation.Primary;

import java.io.Serializable;


/**
 * @param <K> the datatype of the Enitity Identifier.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2764 $, $Date:: 2012-05-29 23:24:47#$
 * @since 1.0
 */
@MappedSuperclass
public abstract class IdentifiableEntityModel<K extends Serializable> extends EntityModel implements IdentifiableEntity<K> {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = true, nullable = false, unique = true, updatable = true)
    private K id;


    public K getId() {
        return id;
    }

    public void setId(K id) {
        this.id = id;
    }

}
