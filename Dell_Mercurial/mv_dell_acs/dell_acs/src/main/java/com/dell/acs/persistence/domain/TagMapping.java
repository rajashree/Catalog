/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.domain;


import com.sourcen.core.persistence.domain.impl.jpa.EntityModel;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Samee K.S
 * @author : sameeks $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */

@Table(name = "t_tag_mapping")
@Entity
@IdClass(TagMapping.TagMappingPK.class)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TagMapping extends EntityModel {

    public TagMapping(){}

    public TagMapping(TagMapping.TagMappingPK pk) {
        this.tagID = pk.tagID;
        this.entityID = pk.entityID;
        this.entityType = pk.entityType;
    }

    @Id
    @AttributeOverrides({
            @AttributeOverride(name = "tagID", column = @Column(name="tagID")),
            @AttributeOverride(name = "entityType", column = @Column(name="entityType")),
            @AttributeOverride(name = "entityID", column = @Column(name="entityID"))
    })

    private Long tagID;

    private Integer entityType;

    private Long entityID;

    public Long getTagID() {
        return tagID;
    }

    public void setTagID(Long tagID) {
        this.tagID = tagID;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public void setEntityType(Integer entityType) {
        this.entityType = entityType;
    }

    public Long getEntityID() {
        return entityID;
    }

    public void setEntityID(Long entityID) {
        this.entityID = entityID;
    }


    @Embeddable
    public static class TagMappingPK implements Serializable {

        private Long tagID;

        private Integer entityType;

        private Long entityID;

        public TagMappingPK() {
        }

        public TagMappingPK(Long tagID, Integer entityType, Long entityID) {
            this.tagID = tagID;
            this.entityType = entityType;
            this.entityID = entityID;
        }

        public Long getTagID() {
            return tagID;
        }

        public void setTagID(Long tagID) {
            this.tagID = tagID;
        }

        public Integer getEntityType() {
            return entityType;
        }

        public void setEntityType(Integer entityType) {
            this.entityType = entityType;
        }

        public Long getEntityID() {
            return entityID;
        }

        public void setEntityID(Long entityID) {
            this.entityID = entityID;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TagMappingPK)) return false;

            TagMappingPK that = (TagMappingPK) o;

            if (!entityID.equals(that.entityID)) return false;
            if (!entityType.equals(that.entityType)) return false;
            if (!tagID.equals(that.tagID)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = tagID.hashCode();
            result = 31 * result + entityType.hashCode();
            result = 31 * result + entityID.hashCode();
            return result;
        }
    }
}




