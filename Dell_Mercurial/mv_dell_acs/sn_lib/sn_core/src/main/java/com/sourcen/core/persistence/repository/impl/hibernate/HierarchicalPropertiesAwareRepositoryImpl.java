/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.repository.impl.hibernate;

import com.sourcen.core.persistence.domain.EntityProperty;
import com.sourcen.core.persistence.domain.HierarchicalPropertiesAwareEntity;
import com.sourcen.core.persistence.repository.HierarchicalPropertiesAwareRepository;

import java.util.List;

public abstract class HierarchicalPropertiesAwareRepositoryImpl<R extends HierarchicalPropertiesAwareEntity> extends
        PropertiesAwareRepositoryImpl<R> implements
        HierarchicalPropertiesAwareRepository<R> {

    private final HierarchyAwareRepositoryImpl<R> delegate;


    protected HierarchicalPropertiesAwareRepositoryImpl(Class<?> entityClass, Class<? extends EntityProperty> entityPropertyClass) {
        super(entityClass, entityPropertyClass);
        this.delegate = new SimpleHierarchyAwareRepositoryImpl(entityClass);
    }

    private class SimpleHierarchyAwareRepositoryImpl extends HierarchyAwareRepositoryImpl<R> {

        public SimpleHierarchyAwareRepositoryImpl(final Class<?> entityClass) {
            super(entityClass);
        }

    }

    @Override
    public void insert(final R record) {
        this.delegate.insert(record);
        super.onInsert(record);
    }

    @Override
    public List<R> getTree(final Long id) {
        return super.onFindForList(this.delegate.getTree(id));
    }

    @Override
    public List<R> getChildren(final Long id) {
        return super.onFindForList(this.delegate.getChildren(id));
    }

    @Override
    public void moveTo(final R record, final R newParent) {
        this.delegate.moveTo(record, newParent);
    }

    @Override
    public List<R> getPath(final R record) {
        return super.onFindForList(this.delegate.getPath(record));
    }
}
