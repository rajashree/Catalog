package com.dell.acs.managers.model;

import com.dell.acs.persistence.domain.Curation;
import com.dell.acs.persistence.domain.CurationSource;
import com.sourcen.core.persistence.domain.constructs.jpa.PropertiesAware;
import com.sourcen.core.util.collections.PropertiesProvider;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Adarsh
 * Date: 7/19/12
 * Time: 11:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class CurationCacheData extends PropertiesAwareData {

    private Long id;

    private Long verison;

    private CurationData curationData;

    private CurationSourceData curationSourceData;

    private String guid;

    private String title;

    private String link;

    private String description;

    private String body;

    private String source;

    private Date publishedDate;

    private Date importedDate;

    private Date updatedDate;



    public CurationCacheData() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVerison() {
        return verison;
    }

    public void setVerison(Long verison) {
        this.verison = verison;
    }

    public CurationData getCurationData() {
        return curationData;
    }

    public void setCurationData(CurationData curationData) {
        this.curationData = curationData;
    }

    public CurationSourceData getCurationSourceData() {
        return curationSourceData;
    }

    public void setCurationSourceData(CurationSourceData curationSourceData) {
        this.curationSourceData = curationSourceData;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Date getImportedDate() {
        return importedDate;
    }

    public void setImportedDate(Date importedDate) {
        this.importedDate = importedDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

}
