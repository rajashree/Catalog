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
@Table(name = "sysmon_endpoint_type")
@Entity
public class SysMonEndPointType extends IdentifiableEntityModel<Long>
        implements PropertiesAwareEntity<Long> {

    @Embedded
    private PropertiesAware properties;

    @Column
    private String endpointType;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "endPointType")
    private Collection<SysMonEndPoint> endPoints;

    public SysMonEndPointType() {
    }

    public String getEndpointType() {
        return endpointType;
    }

    public void setEndpointType(String endpointType) {
        this.endpointType = endpointType;
    }

    public Collection<SysMonEndPoint> getEndPoints() {
        return endPoints;
    }

    public void setEndPoints(Collection<SysMonEndPoint> endPoints) {
        this.endPoints = endPoints;
    }

    @Override
    public PropertiesProvider getProperties() {
        if (properties == null) {
            properties = new PropertiesAware();
        }
        return properties.getProperties();
    }


    public SysMonEndPointType(Long endpointTypeId,String endpointType) {
        super.setId(endpointTypeId);
        this.endpointType = endpointType;
    }
}
