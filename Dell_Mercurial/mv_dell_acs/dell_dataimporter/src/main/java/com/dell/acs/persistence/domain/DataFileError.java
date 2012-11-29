/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;

/**
 * @author Shawn R Fisk
 * @author $LastChangedBy
 * @version $Revision
 */
@SuppressWarnings("serial")
@Table(name = "t_data_file_errors")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DataFileError extends IdentifiableEntityModel<Long> {
    @Column(nullable = false)
    private Long dataFileStat_id;

    /**
     * Stores the column with the error (-1 means message is for the entire row).
     */
    @Column(nullable = false)
    private int col;

    /**
     * Stores the error message.
     */
    @Column(nullable = false, length = 4000)
    private String message;

    public DataFileError() {
    }

    public DataFileError(DataFileStatistic pDataFileStat, int pColumn, String pMessage) {
        this.setDataFileStat_id(pDataFileStat.getId());
        this.col = pColumn;
        this.message = pMessage;
    }

    public int getCol() {
    	return this.col;
    }

    public void setCol(int pColumn) {
    	this.col = pColumn;
    }

    public String getMessage() {
    	return this.message;
    }

    public void setMessage(String pMessage) {
    	this.message = pMessage;
    }

	public Long getDataFileStat_id() {
		return dataFileStat_id;
	}

	public void setDataFileStat_id(Long dataFileValidation_id) {
		this.dataFileStat_id = dataFileValidation_id;
	}
}
