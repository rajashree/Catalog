package com.dell.dw.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Devashree
 * Date: 5/8/12
 * Time: 4:09 PM
 * To change this template use File | Settings | File Templates.
 */


@NamedQueries({
        @NamedQuery(
                name="GAEventMetrics.deleteTillDate",
                query = "delete from GAEventMetrics where eventDimension.id in " +
                        "(select id from GAEventDimension where gaWebPropertyProfile.id = :profileId ) and date < :date"
        )
})

@Table(name = "ga_event_metrics")
@Entity
public class GAEventMetrics extends IdentifiableEntityModel<Long> {


    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private GAEventDimension eventDimension;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date date;

    @Column(nullable = false)
    private Integer totalEvents;

    @Column(nullable = false)
    private Integer uniqueEvents;

    @Column(nullable = false)
    private Integer eventValue;

    @Column(nullable = false)
    private Integer visitsWithEvent;


    // Calculated Metrics

    @Column(nullable = false)
    private Float avgEventValue;

    @Column(nullable = false)
    private Float eventsPerVisitWithEvent;

    public GAEventMetrics() {
    }

    public GAEventDimension getEventDimension() {
        return eventDimension;
    }

    public void setEventDimension(final GAEventDimension eventDimension) {
        this.eventDimension = eventDimension;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public Integer getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(final Integer totalEvents) {
        this.totalEvents = totalEvents;
    }

    public Integer getUniqueEvents() {
        return uniqueEvents;
    }

    public void setUniqueEvents(final Integer uniqueEvents) {
        this.uniqueEvents = uniqueEvents;
    }

    public Integer getEventValue() {
        return eventValue;
    }

    public void setEventValue(final Integer eventValue) {
        this.eventValue = eventValue;
    }

    public Integer getVisitsWithEvent() {
        return visitsWithEvent;
    }

    public void setVisitsWithEvent(final Integer visitsWithEvent) {
        this.visitsWithEvent = visitsWithEvent;
    }

    public Float getAvgEventValue() {
        return avgEventValue;
    }

    public void setAvgEventValue(final Float avgEventValue) {
        this.avgEventValue = avgEventValue;
    }

    public Float getEventsPerVisitWithEvent() {
        return eventsPerVisitWithEvent;
    }

    public void setEventsPerVisitWithEvent(final Float eventsPerVisitWithEvent) {
        this.eventsPerVisitWithEvent = eventsPerVisitWithEvent;
    }
}
