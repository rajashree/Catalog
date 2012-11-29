/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain.constructs.jpa;

import com.sourcen.core.util.ObjectUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
@Embeddable
public class Dated implements com.sourcen.core.persistence.domain.constructs.Dated {

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModified;

    @Override
    public Date getDateCreated() {
        return dateCreated;
    }

    @Override
    public void setDateCreated(Date date) {
        this.dateCreated = date;
    }

    @Override
    public Date getDateModified() {
        return dateModified;
    }

    @Override
    public void setDateModified(Date date) {
        this.dateModified = date;
    }

    @PreUpdate
    @PrePersist
    public void preUpdate() {
        this.setDateModified(new Date());
        if (this.getDateCreated() == null) {
            this.setDateCreated(new Date());
        }
    }


    //
    // Object
    //

    @Override
    public int hashCode() {
        return ObjectUtils.hashCode(dateCreated, dateModified);
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass().equals(com.sourcen.core.persistence.domain.constructs.Dated.class) && obj.hashCode() == this.hashCode();
    }

    @Override
    public String toString() {
        return "DatedModel{" +
                "dateCreated=" + dateCreated +
                ", dateModified=" + dateModified +
                '}';
    }
}
