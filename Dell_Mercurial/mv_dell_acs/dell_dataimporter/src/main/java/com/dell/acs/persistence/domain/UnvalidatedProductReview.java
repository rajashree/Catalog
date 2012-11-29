/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;

/**
 * @author Shawn R Fisk
 * @author $LastChangedBy
 * @version $Revision
 */
@SuppressWarnings("serial")
// sfisk - CS-380
@Table(name = "t_unvalidated_product_reviews")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UnvalidatedProductReview extends IdentifiableEntityModel<Long> {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UnvalidatedProduct product;

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

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = DataFile.class)
    private DataFile dataFile;
    
    //@Column(nullable = true) // The identifier of the product review being updated else null if insert
    private Long updateProductReviewId; 

    public UnvalidatedProduct getProduct() {
        return product;
    }

    public void setProduct(final UnvalidatedProduct product) {
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

    public Long getUpdateProductReviewId() {
    	return this.updateProductReviewId;
    }

    public void setUpdateProductReviewId(Long pUpdateProductReviewId) {
    	this.updateProductReviewId = pUpdateProductReviewId;
    }

	public DataFile getDataFile() {
		return dataFile;
	}

	public void setDataFile(DataFile dataFile) {
		this.dataFile = dataFile;
	}
}
