/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain.constructs.jpa;

import com.sourcen.core.util.ObjectUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
@Embeddable
public class HierarchyAware implements com.sourcen.core.persistence.domain.constructs.HierarchyAware<Long> {

    /**
     * the Identifier record which is the parent of this record.
     */
    @Column(nullable = true)
    private Long parentId = null;

    /**
     * stores the left start of the hierarchy. The column name is "lft" instead of left, as it conflicts with the SQL
     * keyword 'LEFT"
     */
    @Column(name = "lft", nullable = false)
    private Long left;

    /**
     * holds the end of this hiearchy set.
     */
    @Column(name = "rgt", nullable = false)
    private Long right;

    /**
     * the order of the entity in this tree.
     */
    @Column(nullable = true)
    private Integer orderIndex = 0;

    /**
     * the hiearchy depth, just in case the tree insert crashes.
     */
    @Column(nullable = true)
    private Integer depth = 0;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getLeft() {
        return left;
    }

    public void setLeft(Long left) {
        this.left = left;
    }

    public Long getRight() {
        return right;
    }

    public void setRight(Long right) {
        this.right = right;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    @Override
    public String toString() {
        return ObjectUtils.toString(this);
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hashCode(parentId, left, right, orderIndex, depth);
    }

    @Override
    public boolean equals(Object o1) {
        return (o1.getClass().isAssignableFrom(com.sourcen.core.persistence.domain.constructs.HierarchyAware.class)
                && o1.hashCode() == this.hashCode());
    }
}
