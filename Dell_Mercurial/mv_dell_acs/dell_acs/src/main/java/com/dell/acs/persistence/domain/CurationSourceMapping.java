package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.IdentifiableEntity;
import com.sourcen.core.persistence.domain.impl.jpa.EntityModel;

import javax.persistence.*;
import java.io.Serializable;

/**
 @author Mahalakshmi
 @author $LastChangedBy: mmahalaxmi $
 @version $Revision: 1595 $, $Date:: 7/30/12  4:41PM#$ */

@Table(name = "t_curation_source_mapping")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CurationSourceMapping extends EntityModel implements IdentifiableEntity<CurationSourceMapping.PK> {


    public CurationSourceMapping() {

    }
    @EmbeddedId
    private PK pk = new PK();

    @Column(nullable = false, columnDefinition = "int default 1 ")
    private Integer status;

    public PK getPk() {
        return pk;
    }

    public void setPk(final PK pk) {
        this.pk = pk;
    }

    public PK getId() {
        return pk;
    }

    public void setId(final PK id) {
        this.pk = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(final Integer status) {
        this.status = status;
    }

    public CurationSource getCurationSource() {
        return pk.getCurationSource();
    }

    @Embeddable
    public static final class PK implements Serializable {

        @ManyToOne
        private CurationSource curationSource;

        @ManyToOne
        private Curation curation;

        public CurationSource getCurationSource() {
            return curationSource;
        }

        public void setCurationSource(final CurationSource curationSource) {
            this.curationSource = curationSource;
        }

        public Curation getCuration() {
            return curation;
        }

        public void setCuration(final Curation curation) {
            this.curation = curation;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PK pk = (PK) o;

            if (!curation.equals(pk.curation)) return false;
            if (!curationSource.equals(pk.curationSource)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = curationSource.hashCode();
            result = 31 * result + curation.hashCode();
            return result;
        }
    }

}
