package com.dell.dw.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 5/18/12
 * Time: 6:22 PM
 * To change this template use File | Settings | File Templates.
 */


@NamedQueries({
        @NamedQuery(
                name="GAPageViewMetrics.deleteTillDate",
                query = "delete from GAPageViewMetrics where " +
                        "pageViewDimension.id in " +
                        "(select id from GAPageViewDimension where gaWebPropertyProfile.id = :profileId) and date < :date"
        )//,
//
//        @NamedQuery(
//                        name="GAPageViewMetrics.getAppHealth",
//                        query = "select" +
//                                " m.pageViewDimension.gaWebPropertyProfile.id, m.pageViewDimension.gaWebPropertyProfile.name,"+
//                                "(select SUM(m1.pageViews) from GAPageViewMetrics m1 where m1.pageViewDimension.gaWebPropertyProfile.id = m.pageViewDimension.gaWebPropertyProfile.id and (m1.date between :todayStart and :todayEnd) group by m1.pageViewDimension.gaWebPropertyProfile.id) as PageViewsToday" +
//                                " from GAPageViewMetrics m" +
//                                " group by m.pageViewDimension.gaWebPropertyProfile.id, m.pageViewDimension.gaWebPropertyProfile.name"
//                )
})
@Table(name = "ga_pageview_metrics")
@Entity
public class GAPageViewMetrics extends IdentifiableEntityModel<Long> {


    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private GAPageViewDimension pageViewDimension;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date date;


    /*@Column(nullable = false)
    private Long entrances;*/

    @Column(nullable = false)
    private Long pageViews;

    @Column(nullable = true)
    private Long uniquePageViews;

    /*@Column(nullable = false)
    private Double timeOnPage;*/

    /*@Column(nullable = true)
    private Long exits;*/

    @Column(nullable = true)
    private Long visits;

    @Column(nullable = true)
    private Long bounces;

    @Column(nullable = true)
    private Double timeOnSite;

    @Column(nullable = true)
    private Long visitors;

    @Column(nullable = true)
    private Long newVisits;

    @Column(nullable = true)
    private Long pageLoadTime;

    @Column(nullable = true)
    private Long pageLoadSample;

    //
    //Calculated Metrics
    //

    /*@Column(nullable = true)
    private Double entranceRate;*/

    @Column(nullable = true)
    private Double pageViewsPerVisit;

    /*@Column(nullable = true)
    private Double avgTimeOnPage;*/

    /*@Column(nullable = true)
    private Double exitRate;*/

    @Column(nullable = true)
    private Double avgTimeOnSite;

    @Column(nullable = true)
    private Double visitBounceRate;

    /*@Column(nullable = true)
    private Double entranceBounceRate;*/

    @Column(nullable = true)
    private Double percentNewVisits;

    @Column(nullable = true)
    private Double avgPageLoadTime;


    public GAPageViewDimension getPageViewDimension() {
        return pageViewDimension;
    }

    public void setPageViewDimension(final GAPageViewDimension pageViewDimension) {
        this.pageViewDimension = pageViewDimension;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    /*public Long getEntrances() {
        return entrances;
    }

    public void setEntrances(final Long entrances) {
        this.entrances = entrances;
    }*/

    public Long getPageViews() {
        return pageViews;
    }

    public void setPageViews(final Long pageViews) {
        this.pageViews = pageViews;
    }

    public Long getUniquePageViews() {
        return uniquePageViews;
    }

    public void setUniquePageViews(final Long uniquePageViews) {
        this.uniquePageViews = uniquePageViews;
    }

    /*public Double getTimeOnPage() {
        return timeOnPage;
    }

    public void setTimeOnPage(final Double timeOnPage) {
        this.timeOnPage = timeOnPage;
    }*/

    /*public Long getExits() {
        return exits;
    }

    public void setExits(final Long exits) {
        this.exits = exits;
    }*/

    public Long getVisits() {
        return visits;
    }

    public void setVisits(Long visits) {
        this.visits = visits;
    }

    public Long getBounces() {
        return bounces;
    }

    public void setBounces(Long bounces) {
        this.bounces = bounces;
    }

    public Double getTimeOnSite() {
        return timeOnSite;
    }

    public void setTimeOnSite(Double timeOnSite) {
        this.timeOnSite = timeOnSite;
    }

    public Long getVisitors() {
        return visitors;
    }

    public void setVisitors(Long visitors) {
        this.visitors = visitors;
    }

    public Long getNewVisits() {
        return newVisits;
    }

    public void setNewVisits(Long newVisits) {
        this.newVisits = newVisits;
    }

    public Long getPageLoadTime() {
        return pageLoadTime;
    }

    public void setPageLoadTime(Long pageLoadTime) {
        this.pageLoadTime = pageLoadTime;
    }

    public Long getPageLoadSample() {
        return pageLoadSample;
    }

    public void setPageLoadSample(Long pageLoadSample) {
        this.pageLoadSample = pageLoadSample;
    }

    /*public Double getEntranceRate() {
        return entranceRate;
    }

    public void setEntranceRate(final Double entranceRate) {
        this.entranceRate = entranceRate;
    }*/

    public Double getPageViewsPerVisit() {
        return pageViewsPerVisit;
    }

    public void setPageViewsPerVisit(final Double pageViewsPerVisit) {
        this.pageViewsPerVisit = pageViewsPerVisit;
    }

    /*public Double getAvgTimeOnPage() {
        return avgTimeOnPage;
    }

    public void setAvgTimeOnPage(final Double avgTimeOnPage) {
        this.avgTimeOnPage = avgTimeOnPage;
    }*/

    /*public Double getExitRate() {
        return exitRate;
    }

    public void setExitRate(final Double exitRate) {
        this.exitRate = exitRate;
    }*/

    public Double getAvgTimeOnSite() {
        return avgTimeOnSite;
    }

    public void setAvgTimeOnSite(Double avgTimeOnSite) {
        this.avgTimeOnSite = avgTimeOnSite;
    }

    public Double getVisitBounceRate() {
        return visitBounceRate;
    }

    public void setVisitBounceRate(Double visitBounceRate) {
        this.visitBounceRate = visitBounceRate;
    }

    /*public Double getEntranceBounceRate() {
        return entranceBounceRate;
    }

    public void setEntranceBounceRate(Double entranceBounceRate) {
        this.entranceBounceRate = entranceBounceRate;
    }*/

    public Double getPercentNewVisits() {
        return percentNewVisits;
    }

    public void setPercentNewVisits(Double percentNewVisits) {
        this.percentNewVisits = percentNewVisits;
    }

    public Double getAvgPageLoadTime() {
        return avgPageLoadTime;
    }

    public void setAvgPageLoadTime(Double avgPageLoadTime) {
        this.avgPageLoadTime = avgPageLoadTime;
    }
}
