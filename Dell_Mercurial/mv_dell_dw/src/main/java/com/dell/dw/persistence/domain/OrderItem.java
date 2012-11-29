package com.dell.dw.persistence.domain;

import com.sourcen.core.persistence.domain.PropertiesAwareEntity;
import com.sourcen.core.persistence.domain.constructs.jpa.PropertiesAware;
import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;
import com.sourcen.core.util.collections.PropertiesProvider;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 6/26/12
 * Time: 11:41 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "order_items")
public class OrderItem extends IdentifiableEntityModel<Long>  implements PropertiesAwareEntity<Long> {

    @Embedded
    private PropertiesAware properties;

    @Column(insertable = true, nullable = false, unique = true, updatable = true)
    private String orderItemId;

    @Column
    private String sku;

    @Column
    private String name;

    @Column
    private String  category;

    @Column
    private Double unitPrice;

    @Column
    private Double unitDiscount;

    @Column
    private Integer quantity;

    @Column
    private Boolean orderItemCancelled;

    @Column
    private Integer cancelQuantity;

    @Column
    private String cancelReason;

    @Column
    private Double commissionAmount;

    @ManyToOne(optional=false)
    private Order order;


    @Override
    public PropertiesProvider getProperties() {
        if (properties == null) {
            properties = new PropertiesAware();
        }
        return properties.getProperties();
    }



    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Double getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(Double commissionAmount) {
        this.commissionAmount = commissionAmount;
    }


}
