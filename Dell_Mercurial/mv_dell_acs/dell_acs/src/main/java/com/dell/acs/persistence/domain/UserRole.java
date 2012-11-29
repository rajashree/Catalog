/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;
import com.sourcen.core.util.beans.Scope;
import com.sourcen.core.util.beans.Scopes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2929 $, $Date:: 2012-06-05 23:25:24#$
 */
//sfisk - CS-380
@Table(name = "t_user_roles")
@Entity
@Scopes({
            @Scope(name = "default", fields = {"name"})
})
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserRole extends IdentifiableEntityModel<Long> {

    @Column(nullable = false, length = 100)
    private String name;

    public UserRole() {
    }

    public UserRole(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
