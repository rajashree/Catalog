/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;


/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2929 $, $Date:: 2012-06-05 23:25:24#$
 */
//sfisk - CS-380
@Table(name = "t_ad_publisher_request")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AdPublisherRequest extends IdentifiableEntityModel<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    private AdPublisher adPublisher;

    @Column(length = 100, nullable = false)
    private String apiMethod;

    @Column(nullable = true)
    private String parameters;

    // sfisk - CS-380
    @Column(nullable = true)
    private String response;

    @Column(nullable = true)
    private String errors;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date modifiedDate;


    public AdPublisherRequest() {
    }

    public AdPublisher getAdPublisher() {
        return adPublisher;
    }

    public void setAdPublisher(AdPublisher adPublisher) {
        this.adPublisher = adPublisher;
    }

    public String getApiMethod() {
        return apiMethod;
    }

    public void setApiMethod(String apiMethod) {
        this.apiMethod = apiMethod;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
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

}
