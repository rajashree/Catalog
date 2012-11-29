package com.dell.dw.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 8/29/12
 * Time: 11:41 AM
 * To change this tempate use File | Settings | File Templates.
 */
@Entity
@Table(name = "orders")
public class Order  extends IdentifiableEntityModel<Long> {


    @Column(insertable = true, nullable = false, unique = true, updatable = true)
    private String orderId;

    @ManyToOne(optional=false)
    private Lead lead;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate;

    @ManyToOne(optional=false)
    private Retailer siteId;

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String country;

    @Column
    private String transactionId;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date transactionDate;

    @Column
    private String status;

    @Column
    private Boolean orderCancelled;

    @Column
    private String cancelReason;

    @Column
    private Double originalSaleAmount;

    @Column
    private Double originalCommissionAmount;

    @Column
    private Double finalSaleAmount;

    @Column
    private Double finalCommissionAmount;

    @Column
    private Double totalDiscountAmountToDate;

    @Column
    private Double totalCancellationAmountToDate;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "order")
    private Collection<OrderItem> orderItems;

    public Order() {
    }

    public Order(String orderId, Lead lead, Retailer siteId, String city, String state, String country, String transactionId, Date transactionDate, String status, Boolean orderCancelled, String cancelReason, Double originalSaleAmount, Double originalCommissionAmount, Double finalSaleAmount, Double finalCommissionAmount, Double totalDiscountAmountToDate, Double totalCancellationAmountToDate) {
        this.orderId = orderId;
        this.lead = lead;
        this.siteId = siteId;
        this.city = city;
        this.state = state;
        this.country = country;
        this.transactionId = transactionId;
        this.transactionDate = transactionDate;
        this.status = status;
        this.orderCancelled = orderCancelled;
        this.cancelReason = cancelReason;
        this.originalSaleAmount = originalSaleAmount;
        this.originalCommissionAmount = originalCommissionAmount;
        this.finalSaleAmount = finalSaleAmount;
        this.finalCommissionAmount = finalCommissionAmount;
        this.totalDiscountAmountToDate = totalDiscountAmountToDate;
        this.totalCancellationAmountToDate = totalCancellationAmountToDate;
    }

    public Order(String orderId, Lead lead, Retailer siteId) {
        this.orderId = orderId;
        this.lead = lead;
        this.siteId = siteId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Retailer getSiteId() {
        return siteId;
    }

    public void setSiteId(Retailer siteId) {
        this.siteId = siteId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getOrderCancelled() {
        return orderCancelled;
    }

    public void setOrderCancelled(Boolean orderCancelled) {
        this.orderCancelled = orderCancelled;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
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

    public Collection<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Collection<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }


}
