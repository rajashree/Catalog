package com.dell.dw.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 8/1/12
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "sysmon_endpoint_metrics")
@Entity
public class SysMonEndPointMetrics extends IdentifiableEntityModel<Long>{

    @Column
    private Long responseTime;

    @Column
    private boolean status;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updateDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private SysMonEndPoint endPoint;

    public SysMonEndPointMetrics() {
    }

    public SysMonEndPointMetrics(Long responseTime,boolean status, Date updateDate, SysMonEndPoint endPoint) {
        this.responseTime = responseTime;
        this.status = status;
        this.updateDate = updateDate;
        this.endPoint = endPoint;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public SysMonEndPoint getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(SysMonEndPoint endPoint) {
        this.endPoint = endPoint;
    }
}
