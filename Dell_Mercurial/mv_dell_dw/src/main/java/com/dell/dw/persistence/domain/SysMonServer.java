package com.dell.dw.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 8/1/12
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "sysmon_server")
@Entity
public class SysMonServer  extends IdentifiableEntityModel<Long> {

    @Column(insertable = true, nullable = false, unique = true, updatable = true)
    private String serverName;

    @Column
    private String serverType;

    @Column
    private String ip;

    @Column
    private Long port;

    @Column
    private String monitoringEndpoint;


    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            targetEntity = SysMonServerHealthMetrics.class,
            mappedBy = "server")
    private Collection<SysMonServerHealthMetrics> serverHealthMetricses;

    public SysMonServer() {
    }

    public SysMonServer(String monitoringEndpoint, String serverName, String ip, Long port, String serverType) {
        this.monitoringEndpoint = monitoringEndpoint;
        this.serverName = serverName;
        this.ip = ip;
        this.port = port;
        this.serverType = serverType;
    }

    public SysMonServer(Long id, String monitoringEndpoint, String serverName, String ip, Long port, String serverType) {
           this.setId(id);
           this.monitoringEndpoint = monitoringEndpoint;
           this.serverName = serverName;
           this.ip = ip;
           this.port = port;
           this.serverType = serverType;
       }


    public String getMonitoringEndpoint() {
        return monitoringEndpoint;
    }

    public void setMonitoringEndpoint(String monitoringEndpoint) {
        this.monitoringEndpoint = monitoringEndpoint;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getPort() {
        return port;
    }

    public void setPort(Long port) {
        this.port = port;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public Collection<SysMonServerHealthMetrics> getServerHealthMetricses() {
        return serverHealthMetricses;
    }

    public void setServerHealthMetricses(Collection<SysMonServerHealthMetrics> serverHealthMetricses) {
        this.serverHealthMetricses = serverHealthMetricses;
    }
}
