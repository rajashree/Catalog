package com.dell.acs.managers;

import com.dell.acs.persistence.domain.APIKeyActivity;
import com.dell.acs.persistence.repository.APIKeyActivityRepository;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;


/**
 * @author Sandeep Heggi
 * @author $LastChangedBy: Sandeep $
 */
@Service
public class APIKeyActivityManagerImpl implements APIKeyActivityManager {

    private static final Logger logger = LoggerFactory.getLogger(APIKeyActivityManagerImpl.class);

    @Override
    @Transactional
    public APIKeyActivity saveMetrics(APIKeyActivity apiKeyActivity) {
        apiKeyActivityRepository.insert(apiKeyActivity);
        if (apiKeyActivity.getId() == null) {
            logger.error("Unable to save the Metrics for:= " + apiKeyActivity.getRequestURL());
        } else {
            logger.info("The request '" + apiKeyActivity.getRequestURL() + "' has been successfully tracked");
        }
        return apiKeyActivity;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<APIKeyActivity> getByUsername(String username, ServiceFilterBean filter) {
        // username, apikey, filter
        return apiKeyActivityRepository.getAPIKeyActivities(null, username, filter);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<APIKeyActivity> getAPIKeyActivityByUserAccessKey(String apiKey, ServiceFilterBean filter) {
        // username, apikey, filter
        return apiKeyActivityRepository.getAPIKeyActivities(apiKey, null, filter);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getAPIKeyActivityCount(String apiKey, String username) {
        // username, apikey
        return apiKeyActivityRepository.getAPIKeyActivitiesCount(apiKey, username);
    }

    @Autowired
    private APIKeyActivityRepository apiKeyActivityRepository;

}
