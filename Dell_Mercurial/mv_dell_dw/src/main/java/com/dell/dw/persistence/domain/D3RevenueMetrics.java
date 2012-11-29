package com.dell.dw.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy$
 * @version $Revision$, $Date::                     $
 */
@Entity
@Table(name = "d3_revenue_metrics")
public class D3RevenueMetrics extends IdentifiableEntityModel<Long> {
    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date landingDate;

    @Column(nullable = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date purchaseDate;

    @Column(nullable = true)
    private Float revenue;

    @Column(nullable = true)
    private Integer clickToOrderDaysInterval;

    @Column(nullable = false)
    private String orderId;

    @ManyToOne
    private D3Link d3Link;

    public D3RevenueMetrics() {
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Float getRevenue() {
        return revenue;
    }

    public void setRevenue(Float revenue) {
        this.revenue = revenue;
    }

    public Integer getClickToOrderDaysInterval() {
        return clickToOrderDaysInterval;
    }

    public void setClickToOrderDaysInterval(Integer clickToOrderDaysInterval) {
        this.clickToOrderDaysInterval = clickToOrderDaysInterval;
    }

    public D3Link getD3Link() {
        return d3Link;
    }

    public void setD3Link(D3Link d3Link) {
        this.d3Link = d3Link;
    }

    public Date getLandingDate() {
        return landingDate;
    }

    public void setLandingDate(Date landingDate) {
        this.landingDate = landingDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
