/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.constructs.StatusAware;
import com.sourcen.core.persistence.domain.constructs.ThreadLockAware;
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
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 3388 $, $Date:: 2012-06-19 15:51:11#$
 */
//sfisk - CS-380
@Table(name = "t_data_files")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DataFile extends IdentifiableEntityModel<Long> implements StatusAware<Integer>, ThreadLockAware {

    // trunk-rev#3254 patch for https://jira.marketvine.com/browse/CS-330
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private RetailerSite retailerSite;

    @Column(length = 1000)
    private String srcFile;

    @Column(length = 255)
    private String filePath;

    @Column(length = 3)
    private Integer status;

    @Column(length = 3)
    private Integer outputStatus;

    @Column(length = 50, nullable = false)
    private String importType;

    @Column(length = 3, nullable = false)
    private Integer priority;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date modifiedDate;

    private String lockedThread;

    private String host;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(columnDefinition = "numeric(10,0) not null default -1")
    private Integer currentRow = -1;

    @Column(columnDefinition = "numeric(10,0) not null default 0")
    private Integer numErrorRows = 0;

    @Column(columnDefinition = "numeric(10,0) not null default 0")
    private Integer numRows = 0;

    @Column(nullable = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(nullable = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date endTime;

    @Column(length = 1000)
    private String splitSrcFile;

    public DataFile() {
    }

    public DataFile(String filePath, Integer status) {
        this.filePath = filePath;
        this.status = status;
        this.outputStatus = 0;
        this.numRows = 0;
        this.currentRow = -1;
    }

    public DataFile(RetailerSite retailerSite, String srcFile, String filePath, Integer status, String importType,
                    Integer priority) {
        this.retailerSite = retailerSite;
        this.filePath = filePath;
        this.srcFile = srcFile;
        this.status = status;
        this.priority = priority;
        this.importType = importType;
        this.createdDate = new Date();
        this.modifiedDate = new Date();
        this.outputStatus = 0;
        this.numRows = 0;
        this.currentRow = -1;
    }

    public String getLockedThread() {
        return lockedThread;
    }

    public void setLockedThread(String lockedThread) {
        this.lockedThread = lockedThread;
    }

    public Integer getPriority() {
        return priority;
    }

    public Integer getOutputStatus() {
        return outputStatus;
    }

    public void setOutputStatus(final Integer outputStatus) {
        this.outputStatus = outputStatus;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getSrcFile() {
        return srcFile;
    }

    public void setSrcFile(String srcFile) {
        this.srcFile = srcFile;
    }

    public String getImportType() {
        return importType;
    }

    public void setImportType(String importType) {
        this.importType = importType;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getHost() {
    	return this.host;
    }

    public void setHost(String host) {
    	this.host = host;
    }

    public Date getStartDate() {
    	return this.startDate;
    }

    public void setStartDate(Date pStartDate) {
    	this.startDate = pStartDate;
    }

    public Date getEndDate() {
    	return this.endDate;
    }

    public void setEndDate(Date pEndDate) {
    	this.endDate = pEndDate;
    }

    public Date getStartTime() {
    	return this.startTime;
    }

    public void setStartTime(Date pStartTime) {
    	this.startTime = pStartTime;
    }

    public Date getEndTime() {
    	return this.endTime;
    }

    public void setEndTime(Date pEndTime) {
    	this.endTime = pEndTime;
    }

    public int getCurrentRow() {
    	return this.currentRow;
    }

    public void setCurrentRow(int pCurrentRow) {
    	this.currentRow = pCurrentRow;
    }

    public int getNumErrorRows() {
    	return this.numErrorRows;
    }

    public void setNumErrorRows(int numErrorRows) {
    	this.numErrorRows = numErrorRows;
    }

    public int getNumRows() {
    	return this.numRows;
    }

    public void setNumRows(int pNumRows) {
    	this.numRows = pNumRows;
    }

	public String getSplitSrcFile() {
		return splitSrcFile;
	}

	public void setSplitSrcFile(String splitSrcFile) {
		this.splitSrcFile = splitSrcFile;
	}
}
