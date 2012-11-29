/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain.impl.jpa;

import com.sourcen.core.persistence.domain.HierarchyAwareEntity;
import com.sourcen.core.persistence.domain.constructs.jpa.HierarchyAware;

import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;


/**
 * Hierarchical models are inserted using stored procedure 'treeNodes_insert' Hence we cannot have any not-null columns,
 * and will have to ensure not-null using code.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
@SuppressWarnings({"serial", "unused"})
@MappedSuperclass
public class HierarchyAwareModel extends IdentifiableEntityModel<Long> implements HierarchyAwareEntity {

    @Embedded
    private HierarchyAware hierarchy;

    @Override
    @PreUpdate
    public void onUpdate() {
    }

    @Override
    public Long getParentId() {
        return hierarchy.getParentId();
    }

    @Override
    public void setParentId(Long parentId) {
        hierarchy.setParentId(parentId);
    }

    @Override
    public Long getLeft() {
        return hierarchy.getLeft();
    }

    @Override
    public void setLeft(Long left) {
        hierarchy.setLeft(left);
    }

    @Override
    public Long getRight() {
        return hierarchy.getRight();
    }

    @Override
    public void setRight(Long right) {
        hierarchy.setRight(right);
    }

    @Override
    public Integer getOrderIndex() {
        return hierarchy.getOrderIndex();
    }

    @Override
    public void setOrderIndex(Integer orderIndex) {
        hierarchy.setOrderIndex(orderIndex);
    }

    @Override
    public Integer getDepth() {
        return hierarchy.getDepth();
    }

    @Override
    public void setDepth(Integer depth) {
        hierarchy.setDepth(depth);
    }

    @Override
    public String toString() {
        return hierarchy.toString();
    }

    @Override
    public int hashCode() {
        return hierarchy.hashCode();
    }

    @Override
    public boolean equals(Object o1) {
        return hierarchy.equals(o1);
    }
}
