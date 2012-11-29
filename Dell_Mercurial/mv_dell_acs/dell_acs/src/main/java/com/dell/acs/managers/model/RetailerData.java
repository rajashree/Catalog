package com.dell.acs.managers.model;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 0 $, $Date:: 2000-01-01 00:00:01#$
 */
public class RetailerData implements FormData {

    private Long id;

    @NotEmpty(message = "Name field can't be empty")
    private String name;

    @NotEmpty(message = "Description field can't be empty")
    private String description;

    private String url;

    private Boolean active;

    private Date createdDate;

    private Date modifiedDate;

    private Long version;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
