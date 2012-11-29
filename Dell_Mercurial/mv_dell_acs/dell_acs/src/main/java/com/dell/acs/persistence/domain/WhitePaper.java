/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Adarsh
 * Date: 3/22/12
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */
//sfisk - CS-380
@Table(name = "t_whitepaper")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WhitePaper extends IdentifiableEntityModel<Long> {

    @Column(length = 100, nullable = false)
    private String solution;
    @Column(length = 100, nullable = false)
    private String author;
    @Column(length = 100, nullable = false)
    private String title;
    @Column(length = 100, nullable = false)
    private String download;
    @Column(length = 100, nullable = false)
    private Date date;
    @Column(length = 100, nullable = false)
    private String typeOfAsset;
    @Column(length = 100, nullable = false)
    private String buyerCycle;
    @Column(length = 100, nullable = false)
    private String buyerPersona;
    @Column(length = 100, nullable = false)
    private String mdfPartner;
    @Column(length = 100, nullable = false)
    private String acqPartner;
    @Column(length = 100, nullable = false)
    private String owner;
    @Column(length = 100, nullable = false)
    private Product product;
    @Column(length = 100, nullable = false)
    private String Keywords;
    @Column(length = 100, nullable = false)
    private String segment;
    @Column(length = 100, nullable = false)
    private String year;
    @Column(length = 100, nullable = false)
    private String notes;
    @Column(length = 100, nullable = false)
    private String linkToAssetOnWik;
    @Column(length = 100, nullable = false)
    private String articleVideoOnlineTool;

    /*   @Column(length = 100, nullable = false)
       private   permissionNeeded;
    @Column(length = 100, nullable = false)
  private  getedOrUnGated;
    @Column(length = 100, nullable = false)
  private  legalAd;*/
    @Column(length = 100, nullable = false)
    private String trackingCode;

    public String getSolution() {
        return solution;
    }

    public void setSolution(final String solution) {
        this.solution = solution;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(final String download) {
        this.download = download;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public String getTypeOfAsset() {
        return typeOfAsset;
    }

    public void setTypeOfAsset(final String typeOfAsset) {
        this.typeOfAsset = typeOfAsset;
    }

    public String getBuyerCycle() {
        return buyerCycle;
    }

    public void setBuyerCycle(final String buyerCycle) {
        this.buyerCycle = buyerCycle;
    }

    public String getBuyerPersona() {
        return buyerPersona;
    }

    public void setBuyerPersona(final String buyerPersona) {
        this.buyerPersona = buyerPersona;
    }

    public String getMdfPartner() {
        return mdfPartner;
    }

    public void setMdfPartner(final String mdfPartner) {
        this.mdfPartner = mdfPartner;
    }

    public String getAcqPartner() {
        return acqPartner;
    }

    public void setAcqPartner(final String acqPartner) {
        this.acqPartner = acqPartner;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(final Product product) {
        this.product = product;
    }

    public String getKeywords() {
        return Keywords;
    }

    public void setKeywords(final String keywords) {
        Keywords = keywords;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(final String segment) {
        this.segment = segment;
    }

    public String getYear() {
        return year;
    }

    public void setYear(final String year) {
        this.year = year;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(final String notes) {
        this.notes = notes;
    }

    public String getLinkToAssetOnWik() {
        return linkToAssetOnWik;
    }

    public void setLinkToAssetOnWik(final String linkToAssetOnWik) {
        this.linkToAssetOnWik = linkToAssetOnWik;
    }

    public String getArticleVideoOnlineTool() {
        return articleVideoOnlineTool;
    }

    public void setArticleVideoOnlineTool(final String articleVideoOnlineTool) {
        this.articleVideoOnlineTool = articleVideoOnlineTool;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(final String trackingCode) {
        this.trackingCode = trackingCode;
    }


}
