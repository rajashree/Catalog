/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.persistence.domain.AdPublisher;
import com.dell.acs.persistence.domain.User;
import com.dell.acs.persistence.repository.AdPublisherRepository;
import com.dell.acs.persistence.repository.AdPublisherRequestRepository;
import com.dell.acs.persistence.repository.UserRoleRepository;
import com.dell.acs.persistence.repository.impl.hibernate.UserRoleRepositoryImpl;
import com.sourcen.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;

/**
 * Author: Ashish
 * Date: 3/1/12
 * Last Modified by : Navin Raj
 * To change this template use File | Settings | File Templates.
 */
@Service
public class AdPublisherManagerImpl implements AdPublisherManager {

    /*Logger Class.*/
    private static final Logger LOG = LoggerFactory.getLogger(AdPublisherManagerImpl.class);

    /**
     * default constructor.
     */
    public AdPublisherManagerImpl() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public AdPublisher createAdPublisher(final User user, final String website) {
        AdPublisher adPublisher = new AdPublisher();
        adPublisher.setUser(user);
        adPublisher.setWebsite(website);
        String apiKey = StringUtils.MD5Hash(user.getId() + "." + website + System.currentTimeMillis());
        adPublisher.setApiKey(apiKey);
        adPublisherRepository.insert(adPublisher);
        return adPublisher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Collection<AdPublisher> getAdPublishers(final User user) {
        AdPublisher example = new AdPublisher();
        example.setUser(user);
        return adPublisherRepository.getByExample(example);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public AdPublisher getAdPublisher(final User user, final String website) {
        return adPublisherRepository.get(user, website);
    }

    @Override
    @Transactional(readOnly = true)
    public AdPublisher getAdPublisher(final Long adPublisherId) {
        return adPublisherRepository.get(adPublisherId);
    }

    @Override
    @Transactional(readOnly = true)
    public AdPublisher getAdPublisher(final String apiKey) {
        AdPublisher adPublisher = adPublisherRepository.get(apiKey);
        if (adPublisher == null) {
            throw new EntityNotFoundException("Unable to find publisher for given Facebook API Key.");
        }
        return adPublisher;
    }

    @Override
    @Transactional
    public void updateAdPublisher(final AdPublisher adPublisher) {
        adPublisherRepository.update(adPublisher);
    }

    /**
     * reference for UserRoleRepositoryImpl.
     */
    @Autowired
    private UserRoleRepository userRoleRepository;


    /**
     * reference for AdPublisherRepository.
     */
    @Autowired
    private AdPublisherRepository adPublisherRepository;

    /**
     * reference for AdPublisherRequestRepository.
     */

    @Autowired
    private AdPublisherRequestRepository adPublisherRequestRepository;

    //
    // getters / iOC
    //

    public void setUserRoleRepository(final UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public void setAdPublisherRepository(final AdPublisherRepository adPublisherRepository) {
        this.adPublisherRepository = adPublisherRepository;
    }

    public void setAdPublisherRequestRepository(final AdPublisherRequestRepository adPublisherRequestRepository) {
        this.adPublisherRequestRepository = adPublisherRequestRepository;
    }

}
