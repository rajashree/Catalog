/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain.constructs;

/**
 * Basic Nested set interface that provides entity with parent/child relations.
 *
 * @param <K> datatype of the parent identifier.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public interface HierarchyAware<K> {

    /**
     * @return parent object identifier.
     */
    K getParentId();

    /**
     * @param id parent object identifier for this nested set object.
     */
    void setParentId(K id);

    /**
     * @return depth of this node in the hierarchy.
     */
    Integer getDepth();

    /**
     * @param depth to be set for this node in the hierarchy.
     */
    void setDepth(Integer depth);

    /**
     * @return order of the node in the node.
     */
    Integer getOrderIndex();

    /**
     * @param orderIndex to be set for this node.
     */
    void setOrderIndex(Integer orderIndex);

    /**
     * @return left bounds of this node.
     */
    Long getLeft();

    /**
     * @param left bound to be set for this node.
     */
    void setLeft(Long left);

    /**
     * @return right bounds of this node.
     */
    Long getRight();

    /**
     * @param right bound to be set for this node.
     */
    void setRight(Long right);

}
