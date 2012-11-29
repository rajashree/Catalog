/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain.impl.jpa;

import com.sourcen.core.persistence.domain.EntityProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;


/**
 * EntityModel Property construct that can provide simple Entity PropertyModel extension.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2929 $, $Date:: 2012-06-05 23:25:24#$
 */
@SuppressWarnings("serial")
@MappedSuperclass
public abstract class EntityPropertyModel extends EntityModel implements EntityProperty {

    //
    // =================================================================================================================
    // == Class Fields ==
    // =================================================================================================================
    //

    /**
     * Primary Key for this model.
     */
    @EmbeddedId
    private EntityPropertyModelPK pk = null;

    /**
     * String value that this entity stores.
     */
    @SuppressWarnings("unused")
    @Column(nullable = true, length = Integer.MAX_VALUE)
    private String value;

    //
    // =================================================================================================================
    // == Constructors ==
    // =================================================================================================================
    //

    /**
     * default constructor.
     */
    public EntityPropertyModel() {
        this(new EntityPropertyModelPK());
    }

    protected EntityPropertyModel(String name, String value) {
        this();
        this.setName(name);
        this.setValue(value);
    }

    /**
     * default constructor with a primary key.
     *
     * @param pk to be used
     */
    public EntityPropertyModel(final EntityPropertyModelPK pk) {
        super();
        this.pk = pk;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
    //
    // =================================================================================================================
    // == Getters and Setters ==
    // =================================================================================================================
    //

    @Override
    public final Long getId() {
        return this.pk.getId();
    }

    @Override
    public final void setId(final Long id) {
        this.pk.setId(id);
    }

    @Override
    public final String getName() {
        return this.pk.getName();
    }

    @Override
    public final void setName(final String name) {
        this.pk.setName(name);
    }

    public EntityPropertyModelPK getPk() {
        return pk;
    }

    public void setPk(EntityPropertyModelPK pk) {
        this.pk = pk;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
