package com.dell.acs.managers;


import com.dell.acs.persistence.domain.APIKeyActivity;
import com.sourcen.core.managers.Manager;
import com.sourcen.core.util.beans.ServiceFilterBean;

import java.util.Collection;

/**
 * This class is used to manage the API Accesses
 *
 * @author Sandeep Heggi
 * @author $lastChangedBy : sandeep
 */

public interface APIKeyActivityManager extends Manager {


    /**
     * Save the API Key metrics.
     *
     * @param apiKeyActivity - refer {@link APIKeyActivity}
     * @return Object of {@link APIKeyActivity}- Saved apiKeyActivity
     */

    APIKeyActivity saveMetrics(APIKeyActivity apiKeyActivity);

    /**
     * Get the api key access details for a particular user given username.
     *
     * @param username - String - username for which api activities need to retrieved
     * @return Collection of {@link APIKeyActivity}
     */
    Collection<APIKeyActivity> getByUsername(String username, ServiceFilterBean filter);


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
    public Integer getAPIKeyActivityCount(String apiKey, String username);

    /**
     * Retrieves the API Keys access details based on the Access key.
     *
     * @param apiKey - String - accessKey for which details need to be fetched
     * @return Collection of {@link APIKeyActivity}
     */
    Collection<APIKeyActivity> getAPIKeyActivityByUserAccessKey(String apiKey, ServiceFilterBean filter);

}