package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 4/8/12
 * Time: 11:42 AM
 */
//sfisk - CS-380
@Table(name = "t_data_export_files")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DataExportFile extends IdentifiableEntityModel<Long> {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private RetailerSite retailerSite;

    @Column(length = 255, nullable = false)
    private String filePath;

    @Column(length = 3, nullable = false)
    private Integer exportStatus;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date modifiedDate;


    public DataExportFile() {

    }

    public DataExportFile(RetailerSite retailerSite, String filePath, Integer exportStatus) {
        this.retailerSite = retailerSite;
        this.filePath = filePath;
        this.exportStatus = exportStatus;
    }

    public RetailerSite getRetailerSite() {
        return retailerSite;
    }

    public void setRetailerSite(RetailerSite retailerSite) {
        this.retailerSite = retailerSite;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getExportStatus() {
        return exportStatus;
    }

    public void setExportStatus(Integer exportStatus) {
        this.exportStatus = exportStatus;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
