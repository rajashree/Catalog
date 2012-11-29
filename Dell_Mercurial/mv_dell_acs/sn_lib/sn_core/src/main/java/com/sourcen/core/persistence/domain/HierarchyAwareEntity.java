/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.domain;

import com.sourcen.core.persistence.domain.constructs.HierarchyAware;

/**
 * A nested set entity interface. All objects that implement this entity must be "nestable" and must have a hierarchy
 * that it can refer to.
 *
 * @see http://en.wikipedia.org/wiki/Nested_set_model
 */
public interface HierarchyAwareEntity extends IdentifiableEntity<Long>, HierarchyAware<Long> {

    /**
     * This method is invoked by the entityPersister before inserting/updating the entity. We use this as a oppurtunity
     * to validate fields of the model like left, right and correct the values before its being persisted into the
     * datastore.
     */
    void onUpdate();

}
