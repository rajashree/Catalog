/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util.beans;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * @author Sandeep Heggi
 * @author $LastChangedBy: Sandeep $
 */
public class DocumentFilterBean {

    private String name;
    private MultipartFile image;
    private MultipartFile document;
    private String description;
    private String startDate;
    private String endDate;
    private Object site;
    private String type;
    private String body;
    private String url;
    private Integer status;
    private String author;
    private String source;
    private String abstractText;
    private String publishDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public MultipartFile getDocument() {
        return document;
    }

    public void setDocument(MultipartFile document) {
        this.document = document;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {

        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Object getSite() {
        return site;
    }

    public void setSite(Object site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(final String abstractText) {
        this.abstractText = abstractText;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(final String publishDate) {
        this.publishDate = publishDate;
    }
}
