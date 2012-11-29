/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;
import com.sourcen.core.util.beans.Scope;
import com.sourcen.core.util.beans.Scopes;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2929 $, $Date:: 2012-06-05 23:25:24#$
 */
// sfisk - CS-380
@Table(name = "t_product_reviews")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Scopes({
                @Scope(name = "minimal", fields = {"id", "name", "title", "review", "stars","computedWeight"}),

                @Scope(name = "default", fields = {"title","date","name","location","stars","featuredReview",
                        "durationOfProductUse","levelOfExpertise","review","productUses","numberOfHelpfulReviews",
                        "spellCheckWeight","profanityWeight","sentimentWeight","computedWeight"})
        })
public class ProductReview extends IdentifiableEntityModel<Long> {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Fetch(FetchMode.SELECT)
    @Scope(name = "id")
    private Product product;

    @Column(length = 255, nullable = false)
    private String title;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date date;

    @Column(length = 255, nullable = true)
    private String name;

    @Column(length = 255, nullable = true)
    private String location;

    @Column(nullable = true)
    private Float stars;

    @Column(nullable = true)
    private Boolean featuredReview;

    @Column(length = 255, nullable = true)
    private String durationOfProductUse;

    @Column(length = 255, nullable = true)
    private String levelOfExpertise;

    @Column(length = 4000, nullable = true)
    private String review;

    @Column(length = 4000, nullable = true)
    private String productUses;

    @Column(nullable = true)
    private Integer numberOfHelpfulReviews;

    @Column(nullable = true)
    private Integer spellCheckWeight;

    @Column(nullable = true)
    private Integer profanityWeight;

    @Column(nullable = true)
    private Integer sentimentWeight;

    @Column(nullable = true)
    private Float computedWeight;

    public Product getProduct() {
        return product;
    }

    public void setProduct(final Product product) {
        this.product = product;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public Float getStars() {
        return stars;
    }

    public void setStars(final Float stars) {
        this.stars = stars;
    }

    public Boolean getFeaturedReview() {
        return featuredReview;
    }

    public void setFeaturedReview(final Boolean featuredReview) {
        this.featuredReview = featuredReview;
    }

    public String getDurationOfProductUse() {
        return durationOfProductUse;
    }

    public void setDurationOfProductUse(final String durationOfProductUse) {
        this.durationOfProductUse = durationOfProductUse;
    }

    public String getLevelOfExpertise() {
        return levelOfExpertise;
    }

    public void setLevelOfExpertise(final String levelOfExpertise) {
        this.levelOfExpertise = levelOfExpertise;
    }

    public String getReview() {
        return review;
    }

    public void setReview(final String review) {
        this.review = review;
    }

    public String getProductUses() {
        return productUses;
    }

    public void setProductUses(final String productUses) {
        this.productUses = productUses;
    }

    public Integer getNumberOfHelpfulReviews() {
        return numberOfHelpfulReviews;
    }

    public void setNumberOfHelpfulReviews(final Integer numberOfHelpfulReviews) {
        this.numberOfHelpfulReviews = numberOfHelpfulReviews;
    }

    public Integer getSpellCheckWeight() {
        return spellCheckWeight;
    }

    public void setSpellCheckWeight(final Integer spellCheckWeight) {
        this.spellCheckWeight = spellCheckWeight;
    }

    public Integer getProfanityWeight() {
        return profanityWeight;
    }

    public void setProfanityWeight(final Integer profanityWeight) {
        this.profanityWeight = profanityWeight;
    }

    public Integer getSentimentWeight() {
        return sentimentWeight;
    }

    public void setSentimentWeight(final Integer sentimentWeight) {
        this.sentimentWeight = sentimentWeight;
    }

    public Float getComputedWeight() {
        return computedWeight;
    }

    public void setComputedWeight(final Float computedWeight) {
        this.computedWeight = computedWeight;
    }
}
