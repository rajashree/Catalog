/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.PropertiesAwareEntity;
import com.sourcen.core.persistence.domain.constructs.jpa.PropertiesAware;
import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;
import com.sourcen.core.util.beans.Scope;
import com.sourcen.core.util.beans.Scopes;
import com.sourcen.core.util.collections.PropertiesProvider;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2929 $, $Date:: 2012-06-05 23:25:24#$
 */
//sfisk - CS-380
@Table(name = "t_users")
@Entity
@Scopes({
        @Scope(name = "default", fields = {"username", "firstName", "lastName", "email", "properties"}),
        @Scope(name = "minimal", fields = {"username", "firstName", "lastName", "email"})
})
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User extends IdentifiableEntityModel<Long>
        implements PropertiesAwareEntity<Long> {

    public User() {

    }

    public User(String username, String firstName, String lastName, String email) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @Embedded
    private PropertiesAware properties;

    @Column(unique = true, length = 24, nullable = false)
    private String username;

    @Column(length = 100, nullable = false)
    private String email;


    @Column(length = 100, nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(length = 24, nullable = false)
    private String firstName;

    @Column(length = 24, nullable = true)
    private String lastName;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @Column(length = 24, nullable = true)
    private String facebookId;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    private Retailer retailer;

    //We can have Multiple Roles assigned to a User. Therefore, the
    //constraint "unique=true" is set to false
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // sfisk - CS-380
    @JoinTable(name = "t_user_role_mapping",
            joinColumns = {
                    @JoinColumn(name = "user_role_id", unique = false)
            }
    )
    private Set<UserRole> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    @Override
    public PropertiesProvider getProperties() {
        if (properties == null) {
            properties = new PropertiesAware();
        }
        return properties.getProperties();
    }

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }
}
