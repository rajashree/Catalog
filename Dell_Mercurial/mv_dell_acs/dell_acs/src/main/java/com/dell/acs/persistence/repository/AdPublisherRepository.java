/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository;

import com.dell.acs.persistence.domain.AdPublisher;
import com.dell.acs.persistence.domain.User;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

/**
 * @author Navin Raj Kumar G.S., Ashish
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2704 $, $Date:: 2012-05-29 10:23:47#$
 */
public interface AdPublisherRepository extends IdentifiableEntityRepository<Long, AdPublisher> {

    /**
     * retrieve the adPublisher object on the basis of website value.
     *
     * @param user    , store the user information
     * @param website , store the domain of website ,
     *                through which facebook user login.
     *
     * @return return the AdPublisher object.
     */
    AdPublisher get(User user, String website);

    /**
     * retrieve the AdPublisher Object using the API key value of Facebook User.
     *
     * @param apiKey
     *
     * @return
     */
    AdPublisher get(String apiKey);

}
