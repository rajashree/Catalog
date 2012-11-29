/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.persistence.repository.impl.hibernate;

import com.sourcen.core.persistence.domain.EntityProperty;
import com.sourcen.core.persistence.domain.PropertiesAwareEntity;
import com.sourcen.core.persistence.domain.constructs.jpa.RecordProperties;
import com.sourcen.core.persistence.domain.impl.jpa.EntityPropertyModelPK;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.BatchSize;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;


/**
 * privides basic CRUD operations for an Entity with properties.
 *
 * @param <R> the generic type of record that this Repository handles
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: ashish $
 * @version $Revision: 3590 $, $Date:: 2012-06-25 09:37:08#$
 * @since 1.01
 */
public abstract class PropertiesAwareRepositoryImpl<R extends PropertiesAwareEntity<Long>> extends IdentifiableEntityRepositoryImpl<Long, R> {

    //
    // =================================================================================================================
    // == Class Fields ==
    // =================================================================================================================
    //

    /**
     * the entity property class that this repository handles.
     */
    protected final Class<? extends EntityProperty> entityPropertiesClass;

    /**
     * the entity classname, that we can use to execute hibernate entity based HQL.
     */
    private final String entityPropertiesClassName;

    //
    // =================================================================================================================
    // == Constructors ==
    // =================================================================================================================
    //

    /**
     * default constructor.
     *
     * @param entityClass         this repository should manage.
     * @param entityPropertyClass this repository should manage.
     */
    public PropertiesAwareRepositoryImpl(final Class<?> entityClass, final Class<? extends EntityProperty> entityPropertyClass) {
        super(entityClass);
        this.entityPropertiesClass = entityPropertyClass;
        this.entityPropertiesClassName = entityPropertyClass.getSimpleName();
    }

    //
    // =================================================================================================================
    // == Class Methods ==
    // =================================================================================================================
    //

    @Override
    public R onFindForObject(final R record) {
        if (record == null) {
            return null;
        }
        loadProperties(record);
        return record;
    }

    @Override
    public List<R> onFindForList(final List<R> records) {
        for (final R record : records) {
            onFindForObject(record);
        }
        return records;
    }

    @Override
    public R onInsert(final R record) {
        final RecordProperties rp = (RecordProperties) record.getProperties();
        final Map<String, String> map = rp.getInsertedProperties();
        if (map.size() > 0) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Trying to insert properties for recordId:=" + record.getId() + ", and properties:=" + map + " into :=" + this.entityPropertiesClassName);
            }
            Session session = getSession();
            for (final Map.Entry<String, String> entry : map.entrySet()) {
                EntityProperty model = BeanUtils.instantiate(this.entityPropertiesClass);
                model.setId((Long) record.getId());
                model.setName(entry.getKey());
                model.setValue(entry.getValue());
                session.save(model);
            }
            rp.clearInsertedStateFlags();
        }
        return record;
    }

    @Override
    @BatchSize(size = 100)
    public R onUpdate(final R record) {

        // insert the new properties.
        onInsert(record);

        final RecordProperties rp = (RecordProperties) record.getProperties();
        final Map<String, String> updatedProperties = rp.getUpdatedProperties();
        if (updatedProperties.size() > 0) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Trying to update properties for recordId:=" + record.getId() + ", and properties:=" + updatedProperties + " into table:="
                        + this.entityPropertiesClassName);
            }

            Session session = getSession();
            for (final Map.Entry<String, String> entry : updatedProperties.entrySet()) {
                EntityProperty model;
                Object dbProp = session.get(this.entityPropertiesClass, new EntityPropertyModelPK((Long) record.getId(), entry.getKey()));
                if (dbProp != null) {
                    model = (EntityProperty) dbProp;
                } else {
                    model = BeanUtils.instantiate(this.entityPropertiesClass);
                    model.setId((Long) record.getId());
                    model.setName(entry.getKey());
                }
                model.setValue(entry.getValue());
                session.saveOrUpdate(model);

            }
            rp.clearUpdatedStateFlags();
        }

        // delete the keys removed.
        final Map<String, String> deletedProperties = rp.getDeletedProperties();
        if (deletedProperties.size() > 0) {

            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Trying to delete properties for recordId:=" + record.getId() + ", and properties:=" + deletedProperties + " into table:="
                        + this.entityPropertiesClassName);
            }
            Session session = getSession();
            for (final Map.Entry<String, String> entry : deletedProperties.entrySet()) {
                Query query = session.createQuery("DELETE FROM " + this.entityPropertiesClassName + " t WHERE t.pk.id=:id AND t.pk.name=:name");
                query.setParameter("id", record.getId());
                query.setParameter("name", entry.getKey());
                query.executeUpdate();
            }
            rp.clearDeletedStateFlags();
        }
        return record;
    }

    @Override
    public void onDelete(final R record) {
        if (record != null && record.getId() != null) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("deleting properties for recordId:=" + record.getId() + " from table:=" + this.entityPropertiesClassName);
            }
            Query query = getSession().createQuery("DELETE FROM " + this.entityPropertiesClassName + " t WHERE t.pk.id=:id");
            query.setParameter("id", record.getId());
            query.executeUpdate();
        }
    }

    //
    // =================================================================================================================
    // == Private Methods and Helpers ==
    // =================================================================================================================
    //

    /**
     * loads the properties for a record.
     *
     * @param record of type Object
     *
     * @return Object
     */
    @SuppressWarnings("unchecked")
    protected R loadProperties(final R record) {
        if (record == null) {
            return null;
        }

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Loading properties for record:=" + record + " from :=" + this.entityPropertiesClassName);
        }

        EntityProperty template = BeanUtils.instantiate(this.entityPropertiesClass);
        template.setId(record.getId());

        final Criteria criteria = getSession().createCriteria(entityPropertiesClass);
        criteria.add(Restrictions.eq("pk.id", record.getId()));
        final List<EntityProperty> list = criteria.list();
        if (list != null && !list.isEmpty()) {
            for (final EntityProperty entityProperty : list) {
                final RecordProperties rp = (RecordProperties) record.getProperties();
                rp.load(entityProperty.getName(), entityProperty.getValue());
            }
        }
        return record;
    }

    public void updateProperties(R record) {
        onUpdate(record);
    }

}
