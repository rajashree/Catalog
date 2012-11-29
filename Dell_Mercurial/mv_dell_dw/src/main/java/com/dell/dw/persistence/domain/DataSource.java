package com.dell.dw.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 5/23/12
 * Time: 12:46 PM
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "data_source")
@Entity
public class DataSource extends IdentifiableEntityModel<Long> {


    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String importType;

    public DataSource(){

    }

    public DataSource(String importType, String name) {
        this.importType = importType;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getImportType() {
        return importType;
    }

    public void setImportType(final String importType) {
        this.importType = importType;
    }

    public static final class DSConstants {
        public static final Long GA = 1L;
        public static final Long D3 = 2L;
        public static final Long SF_ORDER = 3L;
        public static final Long OTG = 4L;
        public static final Long ORDER = 5L;
    }


}
