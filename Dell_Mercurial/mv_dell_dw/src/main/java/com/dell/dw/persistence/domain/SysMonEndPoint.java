package com.dell.dw.persistence.domain;

import com.sourcen.core.persistence.domain.PropertiesAwareEntity;
import com.sourcen.core.persistence.domain.constructs.jpa.PropertiesAware;
import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;
import com.sourcen.core.util.collections.PropertiesProvider;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 8/1/12
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "sysmon_endpoint")
@Entity
public class SysMonEndPoint extends IdentifiableEntityModel<Long>
        implements PropertiesAwareEntity<Long> {

    @Embedded
    private PropertiesAware properties;

    @Column(nullable = false, unique = true)
    private String endpointName;

    @Column
    private String endpointUrl;

    @Column
    private Long thresholdLimit;

    @Column
    private String httpMethod;

    @ManyToOne(optional=false)
    private SysMonEndPointType endPointType;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            targetEntity = SysMonEndPointMetrics.class,
            mappedBy = "endPoint")
    private Collection<SysMonEndPointMetrics> endPointMetricses;


    public SysMonEndPoint() {
    }

    public SysMonEndPoint(String endpointName, String endpointUrl, String httpMethod, SysMonEndPointType endPointType,Long thresholdLimit) {
        this.endpointName = endpointName;
        this.endpointUrl = endpointUrl;
        this.httpMethod = httpMethod;
        this.endPointType = endPointType;
        this.thresholdLimit = thresholdLimit;
    }



    public String getEndpointName() {
        return endpointName;
    }

    public void setEndpointName(String endpointName) {
        this.endpointName = endpointName;
    }


    public String getEndpointUrl() {
        return endpointUrl;
    }

    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public Long getThresholdLimit() {
        return thresholdLimit;
    }

    public void setThresholdLimit(Long thresholdLimit) {
        this.thresholdLimit = thresholdLimit;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public SysMonEndPointType getEndPointType() {
        return endPointType;
    }

    public void setEndPointType(SysMonEndPointType endPointType) {
        this.endPointType = endPointType;
    }

    public Collection<SysMonEndPointMetrics> getEndPointMetricses() {
        return endPointMetricses;
    }

    public void setEndPointMetricses(Collection<SysMonEndPointMetrics> endPointMetricses) {
        this.endPointMetricses = endPointMetricses;
    }

    @Override
    public PropertiesProvider getProperties() {
        if (properties == null) {
            properties = new PropertiesAware();
        }
        return properties.getProperties();
    }
}
