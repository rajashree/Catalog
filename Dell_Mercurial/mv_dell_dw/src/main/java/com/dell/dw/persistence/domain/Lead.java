package com.dell.dw.persistence.domain;

import com.sourcen.core.persistence.domain.PropertiesAwareEntity;
import com.sourcen.core.persistence.domain.constructs.jpa.PropertiesAware;
import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;
import com.sourcen.core.util.collections.PropertiesProvider;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "lead")
public class Lead extends IdentifiableEntityModel<Long>  implements PropertiesAwareEntity<Long> {

    @Embedded
    private PropertiesAware properties;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "lead")
    private Collection<Order> order;

    @Column(insertable = true, nullable = false, unique = true, updatable = true)
    private String leadId;

    @ManyToOne(optional=false)
    private Retailer siteId;

    @ManyToOne(optional=false)
    private Store entityId;

    @Column
    private String redirectUrl;


    @Column
    private Integer maxOrderCount;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date expirationDate;

    @Override
    public PropertiesProvider getProperties() {
        if (properties == null) {
            properties = new PropertiesAware();
        }
        return properties.getProperties();
    }

    public Lead() {
    }

    public Lead(String leadId, Retailer siteId, Store entityId, String redirectUrl,Integer maxOrderCount, Date creationDate, Date expirationDate) {
        this.leadId = leadId;
        this.siteId = siteId;
        this.entityId = entityId;
        this.redirectUrl = redirectUrl;
        this.maxOrderCount = maxOrderCount;
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
    }

    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }

    public Store getEntityId() {
        return entityId;
    }

    public void setEntityId(Store entityId) {
        this.entityId = entityId;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Integer getMaxOrderCount() {
        return maxOrderCount;
    }

    public void setMaxOrderCount(Integer maxOrderCount) {
        this.maxOrderCount = maxOrderCount;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Collection<Order> getOrder() {
        return order;
    }

    public void setOrder(Collection<Order> order) {
        this.order = order;
    }

    public Retailer getSiteId() {
        return siteId;
    }

    public void setSiteId(Retailer siteId) {
        this.siteId = siteId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
