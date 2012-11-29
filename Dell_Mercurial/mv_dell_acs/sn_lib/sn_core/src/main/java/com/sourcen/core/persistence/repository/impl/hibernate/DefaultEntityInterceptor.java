/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.repository.impl.hibernate;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Iterator;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class DefaultEntityInterceptor extends EmptyInterceptor {

    @Override
    public void preFlush(final Iterator entities) {
        super.preFlush(entities);
    }

    @Override
    public boolean onLoad(Object entity, final Serializable id, final Object[] state, final String[] propertyNames, final Type[] types) {
        RepositoryImpl repository = RepositoryImpl.repositoryInstances.get(entity.getClass());
        entity = repository.onFindForObject(entity);
        return true;
    }
}
