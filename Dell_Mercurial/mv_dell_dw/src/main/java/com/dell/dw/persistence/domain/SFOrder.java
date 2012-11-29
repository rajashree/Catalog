package com.dell.dw.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 6/26/12
 * Time: 11:41 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "sf_orders")
public class SFOrder extends IdentifiableEntityModel<Long> {

    @Column(insertable = true, nullable = false, unique = true, updatable = true)
    private String orderId;

    @Column
    private String merchantOrderId;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date purchaseDate;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date clickThroughDate;

    @ManyToOne(optional=false)
    private Store store;

    @Column
    private String status;

    @Column
    private String cancelOrderReason;

    @Column//(nullable = false)
    private Boolean orderCancelled;

    @Column
    private String cancelStatus;

    @Column//(nullable = false)
    private Double originalSaleAmount;

    @Column//(nullable = false)
    private Double originalCommissionAmount;

    @Column//(nullable = false)
    private Double finalSaleAmount;

    @Column//(nullable = false)
    private Double finalCommissionAmount;

    @Column//(nullable = false)
    private Double totalDiscountAmountToDate;

    @Column//(nullable = false)
    private Double totalCancellationAmountToDate;

    @Column//(nullable = false)
    private Double weightedCommission;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date reconiliationDate;


    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "order")
    private Collection<SFOrderItem> orderItems;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    public Boolean getOrderCancelled() {
        return orderCancelled;
    }

    public void setOrderCancelled(Boolean orderCancelled) {
        this.orderCancelled = orderCancelled;
    }

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Date getClickThroughDate() {
        return clickThroughDate;
    }

    public void setClickThroughDate(Date clickThroughDate) {
        this.clickThroughDate = clickThroughDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCancelOrderReason() {
        return cancelOrderReason;
    }

    public void setCancelOrderReason(String cancelOrderReason) {
        this.cancelOrderReason = cancelOrderReason;
    }


    public String getCancelStatus() {
        return cancelStatus;
    }

    public void setCancelStatus(String cancelStatus) {
        this.cancelStatus = cancelStatus;
    }

    public Double getOriginalSaleAmount() {
        return originalSaleAmount;
    }

    public void setOriginalSaleAmount(Double originalSaleAmount) {
        this.originalSaleAmount = originalSaleAmount;
    }

    public Double getOriginalCommissionAmount() {
        return originalCommissionAmount;
    }

    public void setOriginalCommissionAmount(Double originalCommissionAmount) {
        this.originalCommissionAmount = originalCommissionAmount;
    }

    public Double getFinalSaleAmount() {
        return finalSaleAmount;
    }

    public void setFinalSaleAmount(Double finalSaleAmount) {
        this.finalSaleAmount = finalSaleAmount;
    }

    public Double getFinalCommissionAmount() {
        return finalCommissionAmount;
    }

    public void setFinalCommissionAmount(Double finalCommissionAmount) {
        this.finalCommissionAmount = finalCommissionAmount;
    }

    public Double getTotalDiscountAmountToDate() {
        return totalDiscountAmountToDate;
    }

    public void setTotalDiscountAmountToDate(Double totalDiscountAmountToDate) {
        this.totalDiscountAmountToDate = totalDiscountAmountToDate;
    }

    public Double getTotalCancellationAmountToDate() {
        return totalCancellationAmountToDate;
    }

    public void setTotalCancellationAmountToDate(Double totalCancellationAmountToDate) {
        this.totalCancellationAmountToDate = totalCancellationAmountToDate;
    }

    public Double getWeightedCommission() {
        return weightedCommission;
    }

    public void setWeightedCommission(Double weightedCommission) {
        this.weightedCommission = weightedCommission;
    }

    public Date getReconiliationDate() {
        return reconiliationDate;
    }

    public void setReconiliationDate(Date reconiliationDate) {
        this.reconiliationDate = reconiliationDate;
    }


    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Collection<SFOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Collection<SFOrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
