/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.beans;

import com.sourcen.core.web.ws.beans.base.WSBeanModel;

import java.util.Date;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class WSProductReview extends WSBeanModel {

    //WSProduct product;
    String title;
    Date date;
    String name;
    String location;
    Float stars;
    Boolean featuredReview;
    String durationOfProductUse;
    String levelOfExpertise;
    String review;
    String productUses;
    Integer numberOfHelpfulReviews;
    Integer spellCheckWeight;
    Integer profanityWeight;
    Integer sentimentWeight;
    Float computedWeight;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Float getStars() {
        return stars;
    }

    public void setStars(Float stars) {
        this.stars = stars;
    }

    public Boolean getFeaturedReview() {
        return featuredReview;
    }

    public void setFeaturedReview(Boolean featuredReview) {
        this.featuredReview = featuredReview;
    }

    public String getDurationOfProductUse() {
        return durationOfProductUse;
    }

    public void setDurationOfProductUse(String durationOfProductUse) {
        this.durationOfProductUse = durationOfProductUse;
    }

    public String getLevelOfExpertise() {
        return levelOfExpertise;
    }

    public void setLevelOfExpertise(String levelOfExpertise) {
        this.levelOfExpertise = levelOfExpertise;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getProductUses() {
        return productUses;
    }

    public void setProductUses(String productUses) {
        this.productUses = productUses;
    }

    public Integer getNumberOfHelpfulReviews() {
        return numberOfHelpfulReviews;
    }

    public void setNumberOfHelpfulReviews(Integer numberOfHelpfulReviews) {
        this.numberOfHelpfulReviews = numberOfHelpfulReviews;
    }

    public Integer getSpellCheckWeight() {
        return spellCheckWeight;
    }

    public void setSpellCheckWeight(Integer spellCheckWeight) {
        this.spellCheckWeight = spellCheckWeight;
    }

    public Integer getProfanityWeight() {
        return profanityWeight;
    }

    public void setProfanityWeight(Integer profanityWeight) {
        this.profanityWeight = profanityWeight;
    }

    public Integer getSentimentWeight() {
        return sentimentWeight;
    }

    public void setSentimentWeight(Integer sentimentWeight) {
        this.sentimentWeight = sentimentWeight;
    }

    public Float getComputedWeight() {
        return computedWeight;
    }

    public void setComputedWeight(Float computedWeight) {
        this.computedWeight = computedWeight;
    }

    @Override
    public String toString() {
        return "WSProductReview{" +
                "title='" + title + '\'' +
                ", date=" + date +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", stars=" + stars +
                ", featuredReview=" + featuredReview +
                ", durationOfProductUse='" + durationOfProductUse + '\'' +
                ", levelOfExpertise='" + levelOfExpertise + '\'' +
                ", review='" + review + '\'' +
                ", productUses='" + productUses + '\'' +
                ", numberOfHelpfulReviews=" + numberOfHelpfulReviews +
                ", spellCheckWeight=" + spellCheckWeight +
                ", profanityWeight=" + profanityWeight +
                ", sentimentWeight=" + sentimentWeight +
                ", computedWeight=" + computedWeight +
                '}';
    }
}
