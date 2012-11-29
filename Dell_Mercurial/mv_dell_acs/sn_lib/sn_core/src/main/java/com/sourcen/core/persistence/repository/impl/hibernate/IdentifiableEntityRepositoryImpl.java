/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.repository.impl.hibernate;

import com.sourcen.core.persistence.domain.IdentifiableEntity;
import com.sourcen.core.persistence.domain.constructs.StatusAware;
import com.sourcen.core.persistence.domain.constructs.ThreadLockAware;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;
import com.sourcen.core.util.DateUtils;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;

/**
 * privides basic CRUD operations for an Entity.
 *
 * @param <K> the unique datatype Identifier for the record.
 * @param <R> the generic type of record that this Repository handles
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 3254 $, $Date:: 2012-06-18 09:36:09#$
 * @since 1.0
 */
public abstract class IdentifiableEntityRepositoryImpl<K extends Serializable, R extends IdentifiableEntity<K>>
        extends RepositoryImpl<R> implements IdentifiableEntityRepository<K, R> {

    public IdentifiableEntityRepositoryImpl(final Class<?> entityClass) {
        super(entityClass);
    }

    @Override
    public R get(final K id) {
        final R record = (R) getSession().get(getEntityClass(), id);
        if(record == null){
            throw new EntityNotFoundException("Entity "+entityClassName+" with id "+id + " not found.");
        }
        onFindForObject(record);
        return record;
    }

    @Override
    public boolean containsKey(final K id) {
        return get(id) != null;
    }

    @Override
    public void remove(final K entityId) {
        R entity = get(entityId);
        if(entity == null){
            throw new EntityNotFoundException("entity:="+entityClassName+" id:="+entityId);
        }
        super.remove(entity);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public synchronized <T extends StatusAware & IdentifiableEntity<K>> R acquireLock(final T obj,
                                                                                      final Integer oldStatus,
                                                                                      final Integer newStatus) {

        // TODO - replace with HQL so that we can do update dataFile SET status=? WHERE id=? AND status=?
        // so that we get an absolute DB level lock. This will still work as we are using VersionAware.
        try {
            Session session = getSession();
            StatusAware freshObj = (StatusAware) session.get(obj.getClass(), obj.getId());

            session.buildLockRequest(LockOptions.UPGRADE).setLockMode(LockMode.PESSIMISTIC_WRITE).setTimeOut(10000)
                    .lock(freshObj);
            if (freshObj.getStatus().equals(oldStatus)) {
                logger.debug("Updating status for id:=" + obj
                        .getId() + " oldStatus :=" + oldStatus + " newStatus:=" + newStatus);
                freshObj.setStatus(newStatus);
                if (freshObj instanceof ThreadLockAware) {
                    String threadId = DateUtils.JVM_START_TIME_UTC + "-" + Thread.currentThread().getId();
                    ((ThreadLockAware) freshObj).setLockedThread(threadId);
                }
                session.update(freshObj);
                session.flush();
                return onFindForObject((R) freshObj);
            }
            return null;
        } catch (Exception e) {
            logger.error("unable to lock dataFile :=" + obj.getId());
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public <O> O acquireLock(final IdentifiableEntity obj, final String field, final Object oldValue,
                             final Object newValue) {
        // TODO - replace with HQL so that we can do update dataFile SET status=? WHERE id=? AND status=?
        // so that we get an absolute DB level lock. This will still work as we are using VersionAware.
        try {
            Class clazz = obj.getClass();
            Session session = getSession();
            IdentifiableEntity freshObj = (IdentifiableEntity) session.get(clazz, obj.getId());

            session.buildLockRequest(LockOptions.UPGRADE).setLockMode(LockMode.PESSIMISTIC_WRITE).setTimeOut(10000)
                    .lock(freshObj);
            PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, field);
            Object actualOldValue = pd.getReadMethod().invoke(obj);
            if (oldValue.equals(actualOldValue)) {
                logger.info("Updating field for id:=" + freshObj
                        .getId() + " oldValue :=" + oldValue + " newValue:=" + newValue);
                pd.getWriteMethod().invoke(freshObj, newValue);
                if (freshObj instanceof ThreadLockAware) {
                    String threadId = DateUtils.JVM_START_TIME_UTC + "-" + Thread.currentThread().getId();
                    ((ThreadLockAware) freshObj).setLockedThread(threadId);
                }
                session.update(freshObj);
                session.flush();
                freshObj = onFindForObject((R) freshObj);
                return (O) freshObj;
            } else {
                logger.warn("unable to update record :=" + obj + " because field:=" + field + " with oldValue from the object doesn't match with" + oldValue);
            }
            return null;
        } catch (Exception e) {
            logger.warn("unable to lock dataFile :=" + obj.getId(), e);
        }
        return null;
    }
}
