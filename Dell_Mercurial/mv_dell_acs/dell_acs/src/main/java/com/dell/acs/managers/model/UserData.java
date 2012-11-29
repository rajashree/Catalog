package com.dell.acs.managers.model;

import com.dell.acs.persistence.domain.UserRole;
import com.sourcen.core.persistence.domain.constructs.PropertiesAware;

import java.util.Date;
import java.util.Set;

/**
 * @author Sandeep Heggi
 * @author $LastChangedBy: Sandeep $
 */
public class UserData extends PropertiesAwareData {

    private Long id;

    private String username;

    private String email;

    private String password;

    private Boolean enabled;

    private String firstName;

    private String lastName;

    private Date createdDate;

    private Date modifiedDate;

    private String facebookId;

    private Set<UserRoleData> roles;

    private Long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public Set<UserRoleData> getRoles() {
        return roles;
    }

    public void setRoles(final Set<UserRoleData> roles) {
        this.roles = roles;
    }


    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}


