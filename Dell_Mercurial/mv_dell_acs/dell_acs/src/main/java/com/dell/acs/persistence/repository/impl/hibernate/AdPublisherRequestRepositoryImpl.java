/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.persistence.domain.AdPublisherRequest;
import com.dell.acs.persistence.repository.AdPublisherRequestRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Chethan
 * Date: 2/29/12
 * Time: 8:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class AdPublisherRequestRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, AdPublisherRequest>
        implements AdPublisherRequestRepository {

    /**
     * Constructor.
     */
    public AdPublisherRequestRepositoryImpl() {
        super(AdPublisherRequest.class);
    }
}
