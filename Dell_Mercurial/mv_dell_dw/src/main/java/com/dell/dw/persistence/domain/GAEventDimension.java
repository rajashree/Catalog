package com.dell.dw.persistence.domain;


import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Devashree
 * Date: 5/8/12
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "ga_event_dimension")
@Entity
public class GAEventDimension extends IdentifiableEntityModel<Long> {

    @ManyToOne
    private GAWebPropertyProfile gaWebPropertyProfile;

    @Column(nullable = false, columnDefinition = "VARCHAR(1000) COLLATE Latin1_General_CS_AS")
    private String eventCategory;

    @Column(nullable = false, columnDefinition = "VARCHAR(1000) COLLATE Latin1_General_CS_AS")
    private String eventAction;

    @Column(nullable = false, columnDefinition = "VARCHAR(1000) COLLATE Latin1_General_CS_AS")
    private String eventLabel;

    public GAEventDimension() {
    }

    public GAEventDimension(GAWebPropertyProfile gaWebPropertyProfile,
                            String eventCategory,
                            String eventAction, String eventLabel) {
        this.gaWebPropertyProfile = gaWebPropertyProfile;
        this.eventCategory = eventCategory;
        this.eventAction = eventAction;
        this.eventLabel = eventLabel;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(final String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public String getEventAction() {
        return eventAction;
    }

    public void setEventAction(final String eventAction) {
        this.eventAction = eventAction;
    }

    public String getEventLabel() {
        return eventLabel;
    }

    public void setEventLabel(final String eventLabel) {
        this.eventLabel = eventLabel;
    }

    public GAWebPropertyProfile getGaWebPropertyProfile() {
        return gaWebPropertyProfile;
    }

    public void setGaWebPropertyProfile(GAWebPropertyProfile gaWebPropertyProfile) {
        this.gaWebPropertyProfile = gaWebPropertyProfile;
    }
}

