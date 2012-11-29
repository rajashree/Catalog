package com.dell.dw.persistence.domain;

import com.sourcen.core.persistence.domain.IdentifiableEntity;
import com.sourcen.core.persistence.domain.impl.jpa.EntityModel;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy$
 * @version $Revision$, $Date::                     $
 */
@Entity
@Table(name = "d3_link")
public class D3Link extends EntityModel implements IdentifiableEntity<Long> {

    @Id
    @Column(insertable = true, nullable = false, unique = true, updatable = true)
    private Long id;

    @ManyToOne
    private Retailer retailer;

    @ManyToOne
    private Campaign campaign;

    @Column(nullable = false)
    private String description;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "d3Link")
    private Collection<D3LinkTrackerMetrics> d3LinkTrackerMetrices;




    public D3Link() {
    }

    public D3Link(Long id, Campaign campaign, String description, Retailer retailer) {
        this.id = id;
        this.description = description;
        this.campaign = campaign;
        this.retailer = retailer;
    }

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<D3LinkTrackerMetrics> getD3LinkTrackerMetrices() {
        return d3LinkTrackerMetrices;
    }

    public void setD3LinkTrackerMetrices(Collection<D3LinkTrackerMetrics> d3LinkTrackerMetrices) {
        this.d3LinkTrackerMetrices = d3LinkTrackerMetrices;
    }


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
