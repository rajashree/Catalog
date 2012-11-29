package com.dell.dw.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 6/26/12
 * Time: 11:41 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "sf_order_items")
public class SFOrderItem extends IdentifiableEntityModel<Long> {

    @Column(insertable = true, nullable = false, unique = true, updatable = true)
    private String orderItemId;

    @ManyToOne(optional=false)
    private SFOrder order;

    @Column
    private String itemImageSlug;


    @Column//(nullable = false)
    private String itemNumber;

    @Column//(nullable = false)
    private Double unitPrice;

    @Column//(nullable = false)
    private Double unitDiscount;


    @Column//(nullable = false)
    private Double commissionAmount;

    @Column//(nullable = false)
    private Integer quantity;


    @Column
    private String cancelReason;

    @Column//(nullable = false)
    private Boolean orderItemCancelled;

    @Column//(nullable = false)
    private Integer cancelQuantity;

    @Column
    private String sku;

    @Column
    private String  category;

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public SFOrder getOrder() {
        return order;
    }

    public void setOrder(SFOrder order) {
        this.order = order;
    }

    public String getItemImageSlug() {
        return itemImageSlug;
    }

    public void setItemImageSlug(String itemImageSlug) {
        this.itemImageSlug = itemImageSlug;
    }


    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getUnitDiscount() {
        return unitDiscount;
    }

    public void setUnitDiscount(Double unitDiscount) {
        this.unitDiscount = unitDiscount;
    }

    public Double getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(Double commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }



    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Boolean getOrderItemCancelled() {
        return orderItemCancelled;
    }

    public void setOrderItemCancelled(Boolean orderItemCancelled) {
        this.orderItemCancelled = orderItemCancelled;
    }

    public Integer getCancelQuantity() {
        return cancelQuantity;
    }

    public void setCancelQuantity(Integer cancelQuantity) {
        this.cancelQuantity = cancelQuantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }



    }
