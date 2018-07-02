/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.persistence.repository.impl;

import com.bmsils.gcn.persistence.domain.User;
import com.bmsils.gcn.persistence.repository.Repository;
import org.hibernate.*;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import javax.persistence.Table;
import java.util.Collection;
import java.util.List;


/**
 * foundation repository class that handles most of the core data operations. All repositories can handle multiple
 * entities. but provide basic get/put/remove methods for their core Entity.
 *
 * @param <R> entity type this Repository handles by default.
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: chethanj $
 * @version $Revision: 1127 $, $Date:: 2012-03-30 21:02:10#$
 * @since 1.0
 */
@SuppressWarnings({"unchecked"})
public abstract class RepositoryImpl<R> implements Repository<R> {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    //
    // =================================================================================================================
    // == Class Fields ==
    // =================================================================================================================
    //

    /**
     * the entity class that this repository handles.
     */
    protected final Class<?> entityClass;

    /**
     * the entity classname, that we can use to execute hibernate entity based HQL.
     */
    protected final String entityClassName;

    /**
     * the database table name that this entity manages. This is read usign the {@link javax.persistence.Table} annotation.
     */
    protected final String entityTableName;

    //
    // =================================================================================================================
    // == Constructors ==
    // =================================================================================================================
    //

    /**
     * default constructor.
     *
     * @param entityClass to be managed by this repository.
     */
    public RepositoryImpl(final Class<?> entityClass) {
        this.entityClass = entityClass;
        this.entityClassName = this.entityClass.getSimpleName();
        this.entityTableName = this.entityClass.getAnnotation(Table.class).name();
    }

    //
    // =================================================================================================================
    // == Repository Implementation ==
    // =================================================================================================================
    //

    
    public List<R> getAll() {
        return onFindForList(getSession().createCriteria(entityClass).list());
    }

    
    public List<R> getAll(final Integer firstResult, final Integer maxResults) {
        final Criteria criteria = getSession().createCriteria(entityClass);
        criteria.setFirstResult(firstResult);
        criteria.setMaxResults(maxResults);
        final List<R> list = criteria.list();
        onFindForList(list);
        return list;
    }

    @Deprecated
    
    public List<R> getByExample(R example) {
        final Criteria criteria = getSession().createCriteria(entityClass);
        criteria.add(Example.create(example));
        final List<R> list = criteria.list();
        onFindForList(list);
        return list;
    }

    public R getUniqueByExample(R example) {
        final Criteria criteria = getSession().createCriteria(entityClass);
        criteria.add(Example.create(example));
        R result = (R) criteria.uniqueResult();
        onFindForObject(result);
        return result;
    }

    
    public Long size() {
        final Criteria criteria = getSession().createCriteria(entityClass);
        criteria.setProjection(Projections.rowCount()).uniqueResult();
        return (Long) criteria.uniqueResult();
    }

    
    public void insert(final R record) {
        getSession().save(record);
        onInsert(record);
    }

    
    public void insertAll(final Collection<R> items) {
        for (final R record : items) {
            getSession().save(record);
            onInsert(record);
        }
    }

    
    public void put(final R record) {
        getSession().saveOrUpdate(record);
        onUpdate(record);
    }

    
    public void put(final Collection<R> records) {
        Session session = getSession();
        for (R record : records) {
            session.saveOrUpdate(record);
            onUpdate(record);
        }
    }

    
    public void update(final R record) throws HibernateException {
        getSession().update(record);
        getSession().flush();
        onUpdate(record);
    }

    
    public void updateAll(final Collection<R> items) {
        for (final R record : items) {
            getSession().update(record);
            onUpdate(record);
        }
    }

    
    public void remove(final R entity) {
        try {
            getSession().delete(entity);
            onDelete(entity);
        } catch (final Exception e) {
            logger.error("Unable to delete entity : " + this.entityClassName + ", :" + entity, e);
        }
    }

    
    public void clear() {
        getSession().createQuery("DELETE FROM " + entityClassName).executeUpdate();
    }

    //
    // =================================================================================================================
    // == Private Methods and Helpers ==
    // =================================================================================================================
    //

    /**
     * @param queryOrQueryName the HQL query, or the named query ID.
     * @param parameters       is a Object[] array identified by parameterPosition.
     * @return a list or an object based on the sql.
     * @throws IllegalArgumentException is thrown if the queryOrQueryName or firstResult is invalid.
     */
    protected final List<R> findByNamedQuery(final String queryOrQueryName, final Object... parameters) throws IllegalArgumentException {
        return findByNamedQuery(queryOrQueryName, 0, 0, parameters);
    }

    /**
     * @param queryOrQueryName the HQL query, or the named query ID.
     * @param firstResult      a non "0" number for the first result for any kinda of pagination.
     * @param maxResults       the maximum results we want to see per page. if this is "0" all results are returned.
     * @param parameters       is a Object[] array identified by parameterPosition.
     * @return a list or an object based on the sql.
     * @throws IllegalArgumentException is thrown if the queryOrQueryName or firstResult is invalid.
     */
    protected final List<R> findByNamedQuery(final String queryOrQueryName, final int firstResult, final int maxResults, final Object... parameters)
            throws IllegalArgumentException {
        return executeQuery(queryOrQueryName, true, false, false, firstResult, maxResults, parameters);
    }

    /**
     * @param queryOrQueryName the HQL query, or the named query ID.
     * @param parameters       is a Object[] array identified by parameterPosition.
     * @return a list or an object based on the sql.
     * @throws IllegalArgumentException is thrown if the queryOrQueryName or firstResult is invalid.
     */
    protected final List<R> findByQuery(final String queryOrQueryName, final Object... parameters) throws IllegalArgumentException {
        return findByQuery(queryOrQueryName, 0, 0, parameters);
    }

    /**
     * @param queryOrQueryName the HQL query, or the named query ID.
     * @param firstResult      a non "0" number for the first result for any kinda of pagination.
     * @param maxResults       the maximum results we want to see per page. if this is "0" all results are returned.
     * @param parameters       is a Object[] array identified by parameterPosition.
     * @return a list or an object based on the sql.
     * @throws IllegalArgumentException is thrown if the queryOrQueryName or firstResult is invalid.
     */
    protected final List<R> findByQuery(final String queryOrQueryName, final int firstResult, final int maxResults, final Object... parameters) throws IllegalArgumentException {
        return executeQuery(queryOrQueryName, false, false, false, firstResult, maxResults, parameters);
    }

    /**
     * @param query      the HQL query, or the named query ID.
     * @param parameters is a Object[] array identified by parameterPosition.
     * @return a list or an object based on the sql.
     * @throws IllegalArgumentException is thrown if the queryOrQueryName or firstResult is invalid.
     */
    protected final Integer executeQuery(final String query, final Object... parameters) throws IllegalArgumentException {
        return executeQuery(query, false, false, true, 0, 0, parameters);
    }

    /**
     * @param queryOrQueryName the HQL query, or the named query ID.
     * @param parameters       is a Object[] array identified by parameterPosition.
     * @return a list or an object based on the sql.
     * @throws IllegalArgumentException is thrown if the queryOrQueryName or firstResult is invalid.
     */
    protected final Integer executeNamedQuery(final String queryOrQueryName, final Object... parameters) throws IllegalArgumentException {
        return executeQuery(queryOrQueryName, true, false, true, 0, 0, parameters);
    }

    /**
     * Master execute query method. this does not support named parameter queries at this movement.
     *
     * @param <REZ_TYPE>       result type of the query we are expecting.
     * @param queryOrQueryName the HQL query, or the named query ID.
     * @param namedQuery       true if its a named query.
     * @param singleResult     true if we are expecting a single result.
     * @param isExecuteUpdate  true if we are executing a update statement.
     * @param firstResult      a non "0" number for the first result for any kinda of pagination.
     * @param maxResults       the maximum results we want to see per page. if this is "0" all results are returned.
     * @param parameters       is a Object[] array identified by parameterPosition.
     * @return a list or an object based on the sql.
     * @throws IllegalArgumentException is thrown if the queryOrQueryName or firstResult is invalid.
     */
    protected final <REZ_TYPE> REZ_TYPE executeQuery(final String queryOrQueryName, final boolean namedQuery, final boolean singleResult, final boolean isExecuteUpdate,
                                                     final int firstResult, final int maxResults, final Object... parameters) throws IllegalArgumentException {

        Assert.hasLength(queryOrQueryName, "Query for executing cannot be null");
        Assert.isTrue(firstResult > -1, "First result cannot be less than zero");

        try {
            Session session = getSession();
            Object result = null;

            Query query;
            if (namedQuery) {
                query = session.getNamedQuery(queryOrQueryName);
            } else {
                query = session.createQuery(queryOrQueryName);
            }
            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    query.setParameter(i, parameters[i]);
                }
            }
            if (firstResult >= 0) {
                query.setFirstResult(firstResult);
            }
            if (maxResults >= 0) {
                query.setMaxResults(maxResults);
            }
            if (singleResult) {
                result = query.uniqueResult();
            } else if (!isExecuteUpdate) {
                result = query.list();
            } else {
                result = new Integer(query.executeUpdate());
            }
            if (result != null && !isExecuteUpdate) {
                if (singleResult) {
                    onFindForObject((R) result);
                } else {
                    onFindForList((List<R>) result);
                }
            }
            return (REZ_TYPE) result;
        } catch (final Exception e) {
            logger.error("Unable to execute query :" + queryOrQueryName + ", namedQuery:" + namedQuery + ", singleResult" + singleResult + ", firstResult" + firstResult
                    + ", maxResults" + maxResults + ",parameters" + parameters, e);
        }
        return null;
    }

    //
    // =================================================================================================================
    // == Getters and Setters ==
    // =================================================================================================================
    //

    /**
     * @return the entityClass
     */
    protected final Class<?> getEntityClass() {
        return entityClass;
    }

    /**
     * @return the entityClassName
     */
    protected final String getEntityClassName() {
        return entityClassName;
    }

    /**
     * @return the entityTableName
     */
    protected final String getEntityTableName() {
        return entityTableName;
    }

    protected SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession() {
    	try{
        return sessionFactory.getCurrentSession();
    	}catch (Exception e) {
    		
    		return sessionFactory.openSession();
		}
    }
    
   

    //
    // =================================================================================================================
    // == empty hooks that are invoked during db operations. ==
    // =================================================================================================================
    //

    /**
     * simple hook invoked after the queryForList method is called.
     *
     * @param records is the list of records loaded.
     * @return the processed list of records aftter loading.
     */
    protected List<R> onFindForList(final List<R> records) {
        for (final R record : records) {
            onFindForObject(record);
        }
        // can be overridden.
        return records;
    }

    /**
     * simple hook invoked after the queryForObject method is called.
     *
     * @param record that was loaded.
     * @return the processed record after the loading.
     */
    protected R onFindForObject(final R record) {
        return record;
        // can be overridden.
    }

    /**
     * simple hook invoked after the insert method is called.
     *
     * @param record that was inserted.
     * @return the processed record after the insert.
     */
    protected R onInsert(final R record) {
        // can be overridden.
        return record;
    }

    /**
     * simple hook invoked after the insert method is called.
     *
     * @param record that was inserted.
     * @return the processed record after the update.
     */
    protected R onUpdate(final R record) {
        // can be overridden.
        return record;
    }

    /**
     * simple hook invoked after the deleteById method is called.
     *
     * @param record object that was deleted.
     */
    protected void onDelete(final R record) {
        // can be overridden.
    }

    
    public void merge(final R record) {
    	
    	
    	Session s=getSession();
    	s.merge(record);
        s.flush(); 
        onUpdate(record);
    }

}
