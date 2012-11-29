/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.persistence.domain.AdPublisher;
import com.dell.acs.persistence.domain.AdPublisherProperty;
import com.dell.acs.persistence.domain.User;
import com.dell.acs.persistence.repository.AdPublisherRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.PropertiesAwareRepositoryImpl;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * @author Navin Raj Kumar G.S., Ashish
 * @author $LastChangedBy: ashish $
 * @version $Revision: 3834 $, $Date:: 2012-07-04 13:43:05#$
 */
@Repository
public class AdPublisherRepositryImpl extends PropertiesAwareRepositoryImpl<AdPublisher>
        implements AdPublisherRepository {

    /**
     * calling the super class constructor.
     */
    public AdPublisherRepositryImpl() {
        super(AdPublisher.class, AdPublisherProperty.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdPublisher get(User user, String website) {
        String userWebsite=website;
        logger.info("User login from domain::="+userWebsite);
        try {
            return onFindForObject((AdPublisher) getSession().createCriteria(AdPublisher.class)
                    .add(Restrictions.eq("user.id", user.getId()))
                    .add(Restrictions.eq("website", userWebsite))
                    .uniqueResult());
        } catch (HibernateException e) {
            throw new ObjectNotFoundException("user:=" + user.getId() + " and website:=" + website, AdPublisher.class.getCanonicalName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdPublisher get(final String apiKey) {

        try {
            return onFindForObject((AdPublisher) getSession().createCriteria(AdPublisher.class)
                    .add(Restrictions.eq("apiKey", apiKey))
                    .uniqueResult());
        } catch (HibernateException e) {
            throw new ObjectNotFoundException("apiKey:=" + apiKey + " was not found.", AdPublisher.class.getCanonicalName());
        }
    }


}
