package com.dell.dw.web.controller.formbeans;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 8/2/12
 * Time: 11:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class ServerHealthBean {
    private Long serverId;

    private String serverName;

    private String ip;

    private String uptime;

    private Long connections;

    private String memory;

    private String totalMemory;

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
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

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public Long getConnections() {
        return connections;
    }

    public void setConnections(Long connections) {
        this.connections = connections;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(String totalMemory) {
        this.totalMemory = totalMemory;
    }
}
