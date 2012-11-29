/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository;

import com.dell.acs.persistence.domain.APIKeyActivity;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;
import com.sourcen.core.util.beans.ServiceFilterBean;

import java.util.Collection;


/**
 * Repository to access API Activity related information from the DB.
 *
 * @author Sandeep Heggi
 * @author $lastChangedBy : sandeep
 */

public interface APIKeyActivityRepository extends IdentifiableEntityRepository<Long, APIKeyActivity> {

    /**
     * Get the API key metrics based on the given user name or apiKey.
     * <p/>
     * Example:
     * 1) if you want to filter based on apiKey then - getAPIKeyActivities(apiKey,null,filter)
     * 2) if you want to filter based on username then - getAPIKeyActivities(null,username,filter)
     *
     * @param username - String - user name for which API Key metrics needs to be retrieved
     * @param apiKey   - String - apiKey for which details need to be fetched out
     * @return Collection of {@link APIKeyActivity}
     */
    Collection<APIKeyActivity> getAPIKeyActivities(String apiKey, String username, ServiceFilterBean filter);


    /**
     * Get the Count of number of records.
     * <p/>
     * Example:
     * 1) if you want to count based on apiKey then - getAPIKeyActivities(apiKey,null,filter)
     * 2) if you want to count based on username then - getAPIKeyActivities(null,username,filter)
     *
     * @param username - String - user name for which API Key metrics needs to be retrieved
     * @param apiKey   - String - apiKey for which details need to be fetched out
     * @return Integer - no of records in the database
     */
    public Integer getAPIKeyActivitiesCount(String apiKey, String username);

}
