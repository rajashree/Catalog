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
@Table(name = "sysmon_server_health_metrics")
@Entity
public class SysMonServerHealthMetrics extends IdentifiableEntityModel<Long> {

    @Column
    private String serverUptime;

    @Column
    private Long dbConnections;

    @Column
    private String serverMemoryUsage;

    @Column
    private String totalMemory;

    @Column
    private String appUptime;

    @Column
    private String appMemoryUsage;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updateDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private SysMonServer server;


    public String getServerUptime() {
        return serverUptime;
    }

    public void setServerUptime(String serverUptime) {
        this.serverUptime = serverUptime;
    }

    public Long getDbConnections() {
        return dbConnections;
    }

    public void setDbConnections(Long dbConnections) {
        this.dbConnections = dbConnections;
    }

    public String getServerMemoryUsage() {
        return serverMemoryUsage;
    }

    public void setServerMemoryUsage(String serverMemoryUsage) {
        this.serverMemoryUsage = serverMemoryUsage;
    }

    public String getAppUptime() {
        return appUptime;
    }

    public void setAppUptime(String appUptime) {
        this.appUptime = appUptime;
    }

    public String getAppMemoryUsage() {
        return appMemoryUsage;
    }

    public void setAppMemoryUsage(String appMemoryUsage) {
        this.appMemoryUsage = appMemoryUsage;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public SysMonServer getServer() {
        return server;
    }

    public void setServer(SysMonServer server) {
        this.server = server;
    }

    public String getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(String totalMemory) {
        this.totalMemory = totalMemory;
    }
}
