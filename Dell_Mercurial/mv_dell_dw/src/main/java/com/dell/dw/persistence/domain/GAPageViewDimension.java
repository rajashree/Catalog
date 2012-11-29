package com.dell.dw.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Devashree
 * Date: 5/8/12PM
 * Time: 2:50
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "ga_pageview_dimension")
@Entity
public class GAPageViewDimension extends IdentifiableEntityModel<Long> {


    @ManyToOne
    private GAWebPropertyProfile gaWebPropertyProfile;

    @Column(nullable = false, columnDefinition = "VARCHAR(1000) COLLATE Latin1_General_CS_AS")
    private String pagePath;

    @Column(nullable = false, columnDefinition = "VARCHAR(1000) COLLATE Latin1_General_CS_AS")
    private String pageTitle;

    @Column(nullable = false)
    private Integer pageDepth;

    @Column(nullable = true)
    private String campaignId;

    @Column(nullable = true)
    private String productId;

    public GAPageViewDimension() {}

    public GAPageViewDimension(GAWebPropertyProfile gaWebPropertyProfile,
                               String pageTitle, String pagePath, Integer pageDepth) {
        this.gaWebPropertyProfile = gaWebPropertyProfile;
        this.pageTitle = pageTitle;
        this.pagePath = pagePath;
        this.pageDepth = pageDepth;
    }

    public GAWebPropertyProfile getGaWebPropertyProfile() {
        Session session = null;
        Criteria criteria = session.createCriteria(GAPageViewDimension.class);
        criteria.add(
                Restrictions.eq("gaWebPropertyProfile.gaWebProperty.gaAccount.retailer.id", 1));
        ProjectionList list = Projections.projectionList();
        list.add(Projections.distinct(Projections.property("campaignId")));
        criteria.setProjection(list);
        List<String> campaignIds = criteria.list();

        return gaWebPropertyProfile;
    }

    public void setGaWebPropertyProfile(final GAWebPropertyProfile gaWebPropertyProfile) {
        this.gaWebPropertyProfile = gaWebPropertyProfile;
    }

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(final String pagePath) {
        this.pagePath = pagePath;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(final String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public Integer getPageDepth() {
        return pageDepth;
    }

    public void setPageDepth(final Integer pageDepth) {
        this.pageDepth = pageDepth;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(final String campaignId) {
        this.campaignId = campaignId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(final String productId) {
        this.productId = productId;
    }
}

