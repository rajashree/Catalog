package com.dell.dw.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 6/26/12
 * Time: 11:41 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "stores")
public class Store extends IdentifiableEntityModel<Long> {

    @Column(insertable = true, nullable = false, unique = true, updatable = true)
    private String storeId;

    @Column
    private String storeName;

    @Column
    private String storeOwnerId;

    @Column
    private String storeOwnerName;

    @ManyToOne
    @JoinColumn(name="merchantId", referencedColumnName="retailerId", nullable=false, updatable=false)
    private Retailer merchant;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "store")
    private Collection<SFOrder> orders;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "entityId")
    private Collection<Lead> lead;

    public Store() {
    }

    public Store(String storeId, Retailer merchant) {
        this.storeId = storeId;
        this.merchant = merchant;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Retailer getMerchant() {
        return merchant;
    }

    public void setMerchant(Retailer merchant) {
        this.merchant = merchant;
    }

    public String getStoreOwnerId() {
        return storeOwnerId;
    }

    public void setStoreOwnerId(String storeOwnerId) {
        this.storeOwnerId = storeOwnerId;
    }

    public String getStoreOwnerName() {
        return storeOwnerName;
    }

    public void setStoreOwnerName(String storeOwnerName) {
        this.storeOwnerName = storeOwnerName;
    }

    public Collection<SFOrder> getOrders() {
        return orders;
    }

    public void setOrders(Collection<SFOrder> orders) {
        this.orders = orders;
    }

    public Collection<Lead> getLead() {
        return lead;
    }

    public void setLead(Collection<Lead> lead) {
        this.lead = lead;
    }
}
