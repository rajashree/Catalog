package com.dell.dw.web.controller.formbeans;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 8/2/12
 * Time: 11:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class EndPointBean {
    private Long endpointId;
    private String endpointName;
    private String endpointUrl;
    private String httpMethod;
    private Double responseTime;
    private Double avgResponseTime;
    private Double thresholdLimit;

    public EndPointBean() {
    }

    public EndPointBean(String endpointName, Double responseTime, Double avgResponseTime,Double thresholdLimit) {
        this.endpointName = endpointName;
        this.responseTime = responseTime;
        this.avgResponseTime = avgResponseTime;
        this.thresholdLimit = thresholdLimit;
    }

    public Long getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(Long endpointId) {
        this.endpointId = endpointId;
    }

    public String getEndpointName() {
        return endpointName;
    }

    public void setEndpointName(String endpointName) {
        this.endpointName = endpointName;
    }

    public Double getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Double responseTime) {
        this.responseTime = responseTime;
    }

    public Double getAvgResponseTime() {
        return avgResponseTime;
    }

    public void setAvgResponseTime(Double avgResponseTime) {
        this.avgResponseTime = avgResponseTime;
    }

    public Double getThresholdLimit() {
        return thresholdLimit;
    }

    public void setThresholdLimit(Double thresholdLimit) {
        this.thresholdLimit = thresholdLimit;
    }

    public String getEndpointUrl() {
        return endpointUrl;
    }

    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }
}
