package com.dell.acs.persistence.repository.impl.hibernate;


import com.dell.acs.persistence.domain.APIKeyActivity;
import com.dell.acs.persistence.repository.APIKeyActivityRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
public class APIKeyActivityRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, APIKeyActivity>
        implements APIKeyActivityRepository {

    public APIKeyActivityRepositoryImpl() {
        super(APIKeyActivity.class);
    }

    @Override
    public Collection<APIKeyActivity> getAPIKeyActivities(String apiKey, String username, ServiceFilterBean filter) {
        Criteria criteria = getSession().createCriteria(APIKeyActivity.class);
        applyGenericCriteria(criteria, filter);
        if(StringUtils.isNotEmpty(apiKey)){
            criteria.add(Restrictions.eq("apiKey", apiKey));
        }else if(StringUtils.isNotEmpty(username)){
            criteria.add(Restrictions.eq("username", username));
        }
        return criteria.list();
    }

    @Override
    public Integer getAPIKeyActivitiesCount(String apiKey, String username) {
        Criteria criteria = getSession().createCriteria(APIKeyActivity.class);
        if(StringUtils.isNotEmpty(apiKey)){
            criteria.add(Restrictions.eq("apiKey", apiKey));
        }else if(StringUtils.isNotEmpty(username)){
            criteria.add(Restrictions.eq("username", username));
        }
        Integer totalResult = criteria.list().size();
        return totalResult;
    }
}