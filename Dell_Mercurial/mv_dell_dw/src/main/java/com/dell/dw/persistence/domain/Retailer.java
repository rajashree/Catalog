package com.dell.dw.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy$
 * @version $Revision$, $Date::                     $
 */
@Entity
@Table(name = "retailers")
public class Retailer extends IdentifiableEntityModel<Long> {

    @Column(insertable = true, nullable = false, unique = true, updatable = true)
    private String retailerId;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String emailAddress;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "retailer")
    Collection<GAAccount> gaAccounts;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "retailer")
    Collection<D3Link> d3Links;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "retailer")
    Collection<DataSchedulerBatch> schedulerBatches;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "merchant")
    Collection<Store> stores;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "siteId")
    private Collection<Lead> lead;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "siteId")
    private Collection<Order> order;

    public Retailer() {
    }

    public Retailer(String name, String retailerId, String description) {
        this.name = name;
        this.retailerId = retailerId;
        this.description = description;
    }

    public Retailer(String retailerId) {
        this.retailerId = retailerId;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<GAAccount> getGaAccounts() {
        return gaAccounts;
    }

    public void setGaAccounts(Collection<GAAccount> gaAccounts) {
        this.gaAccounts = gaAccounts;
    }

    public Collection<D3Link> getD3Links() {
        return d3Links;
    }

    public void setD3Links(Collection<D3Link> d3Links) {
        this.d3Links = d3Links;
    }

    public Collection<DataSchedulerBatch> getSchedulerBatches() {
        return schedulerBatches;
    }

    public void setSchedulerBatches(Collection<DataSchedulerBatch> schedulerBatches) {
        this.schedulerBatches = schedulerBatches;
    }

    public Collection<Store> getStores() {
        return stores;
    }

    public void setStores(Collection<Store> stores) {
        this.stores = stores;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
