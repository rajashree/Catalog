package com.dell.acs.web.controller.formbeans;

import org.hibernate.validator.constraints.NotEmpty;

/**
 @author Navin Raj Kumar G.S.
 @author $LastChangedBy: navinr $
 @version $Revision: 0 $, $Date:: 2000-01-01 00:00:01#$ */
public class RetailerBean implements FormBean {

    private Long id;

    @NotEmpty(message = "Name field can't be empty")
    private String name;

    @NotEmpty(message = "Description field can't be empty")
    private String description;

    private String url;
    
    private Boolean active;


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
}
