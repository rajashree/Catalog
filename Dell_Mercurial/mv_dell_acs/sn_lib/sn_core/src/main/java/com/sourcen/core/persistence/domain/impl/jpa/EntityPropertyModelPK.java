/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain.impl.jpa;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * {@link EntityPropertyModelPK} Primary Key for Hibernate mapping.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 3423 $, $Date:: 2012-06-20 13:53:05#$
 */
@Embeddable
public final class EntityPropertyModelPK implements Serializable {

    private static final long serialVersionUID = -388875921492450372L;

    /**
     * Entity Owner Id.
     */
    @Column(unique = false, nullable = false)
    @Index(name = "idx_entity_id", columnNames = {"id"})
    private Long id;

    /**
     * Name of the property. This must be unique for the entity. Each entity must have only 1 property of the same
     * name.
     */
    @Column(unique = false, nullable = false, length = 250)
    private String name;

    //
    // =================================================================================================================
    // == Constructors ==
    // =================================================================================================================
    //

    /**
     * default constructor.
     *
     * @param id   entity identifier.
     * @param name of the property.
     */
    public EntityPropertyModelPK(final Long id, final String name) {
        super();
        this.id = id;
        this.name = name;
    }

    /**
     * default constructor.
     */
    public EntityPropertyModelPK() {
    }

    /**
     * override this to reduce stack overflow issues.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EntityPropertyModelPK) {
            EntityPropertyModelPK key = (EntityPropertyModelPK) obj;
            return (this.id.equals(key.id) && this.name.equals(key.name));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (id + name).intern().hashCode();
    }

    //
    // =================================================================================================================
    // == Getters / Setters ==
    // =================================================================================================================
    //

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
