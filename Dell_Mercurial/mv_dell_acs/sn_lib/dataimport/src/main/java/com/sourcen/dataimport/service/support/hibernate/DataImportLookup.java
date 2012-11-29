/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service.support.hibernate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Random;

/**
 * just create a bean in your persistence layer that used DataImportLookup as a subclass for DataImportLookupService to
 * work.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: rajashreem $
 * @version $Revision: 2993 $, $Date:: 2012-06-07 12:50:19#$
 */
@MappedSuperclass
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DataImportLookup {


    @EmbeddedId
    private PK pk = new PK();

    @Column(nullable = false, length = 255)
    private String destTableName;

    @Column(nullable = false)
    private Long destTableId;

    public String getSrcTableName() {
        return pk.getSrcTableName();
    }

    public void setSrcTableName(String srcTableName) {
        pk.setSrcTableName(srcTableName);
    }

    public String getSrcTableId() {
        return pk.getSrcTableId();
    }

    public void setSrcTableId(String srcTableId) {
        pk.setSrcTableId(srcTableId);
    }

    public Long getDestTableId() {
        return destTableId;
    }

    public void setDestTableId(Long destTableId) {
        this.destTableId = destTableId;
    }

    public String getDestTableName() {
        return destTableName;
    }

    public void setDestTableName(String destTableName) {
        this.destTableName = destTableName;
    }

    //
    // Composite key for DataImportLookup bean.
    //
    public static final class PK implements Serializable {

        @Column(nullable = false, length = 50)
        private String srcTableName;

        @Column(nullable = false, length = 50)
        private String srcTableId;

        public PK() {
        }

        public PK(String srcTableName, String srcTableId) {
            this.srcTableName = srcTableName;
            this.srcTableId = srcTableId;
        }

        public String getSrcTableName() {
            return srcTableName;
        }

        public void setSrcTableName(String srcTableName) {
            this.srcTableName = srcTableName;
        }

        public String getSrcTableId() {
            return srcTableId;
        }

        public void setSrcTableId(String srcTableId) {
            this.srcTableId = srcTableId;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof PK) {
                PK objPk = (PK) obj;
                return (srcTableName != null && srcTableName.equals(objPk.srcTableName))
                        && (srcTableId != null && srcTableId.equals(objPk.srcTableId));
            }
            return false;
        }

        private static final int pkHashCodeRandom = new Random().nextInt();

        @Override
        public int hashCode() {

            return 31 * pkHashCodeRandom * ((srcTableId != null) ? srcTableId.intern().hashCode() : 0)
                    * ((srcTableName != null) ? srcTableName.intern().hashCode() : 0);
        }
    }

}
