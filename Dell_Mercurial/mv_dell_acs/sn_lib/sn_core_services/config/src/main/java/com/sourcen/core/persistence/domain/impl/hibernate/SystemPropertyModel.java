/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */


package com.sourcen.core.persistence.domain.impl.hibernate;

import com.sourcen.core.persistence.domain.SystemProperty;
import com.sourcen.core.persistence.domain.impl.jpa.EntityModel;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

//sfisk - CS-380
@javax.persistence.Entity
@Table(name = "t_system_properties", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SystemPropertyModel extends EntityModel implements SystemProperty {

    private static final long serialVersionUID = 2113942939323843738L;

    @Id
    @Column(insertable = true, nullable = false, unique = true, updatable = true)
    private String id;

    @Column
    private String value;

    public SystemPropertyModel(final String id, final String value) {
        super();
        setId(id);
        this.value = value;
    }

    public SystemPropertyModel() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
