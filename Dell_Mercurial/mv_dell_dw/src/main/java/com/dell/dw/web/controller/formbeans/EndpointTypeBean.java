package com.dell.dw.web.controller.formbeans;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 8/6/12
 * Time: 12:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class EndpointTypeBean {
    private Long endpointTypeId;
    private String endpointTypeName;

    public EndpointTypeBean(Long endpointTypeId, String endpointTypeName) {
        this.endpointTypeId = endpointTypeId;
        this.endpointTypeName = endpointTypeName;
    }

    public Long getEndpointTypeId() {
        return endpointTypeId;
    }

    public void setEndpointTypeId(Long endpointTypeId) {
        this.endpointTypeId = endpointTypeId;
    }

    public String getEndpointTypeName() {
        return endpointTypeName;
    }

    public void setEndpointTypeName(String endpointTypeName) {
        this.endpointTypeName = endpointTypeName;
    }
}