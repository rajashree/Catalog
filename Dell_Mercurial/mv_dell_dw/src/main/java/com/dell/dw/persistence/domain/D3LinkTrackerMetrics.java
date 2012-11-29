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
@Table(name = "d3_linktracker_metrics")
public class D3LinkTrackerMetrics extends IdentifiableEntityModel<Long> {

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date landingDate;


    @Column(nullable = false)
    private Integer clicks;

    @Column(nullable = true)
    private String cookieId;

    @Column(nullable = true)
    private String clickId;

    @ManyToOne
    private D3Link d3Link;

    public D3LinkTrackerMetrics() {
    }


    public Integer getClicks() {
        return clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }

    public String getCookieId() {
        return cookieId;
    }

    public void setCookieId(String cookieId) {
        this.cookieId = cookieId;
    }

    public String getClickId() {
        return clickId;
    }

    public void setClickId(String clickId) {
        this.clickId = clickId;
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
}
