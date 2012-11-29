package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.AuthenticationKeyNotFoundException;
import com.dell.acs.persistence.domain.AuthenticationKey;
import com.dell.acs.persistence.domain.AuthenticationKeysProperty;
import com.dell.acs.persistence.repository.AuthenticationRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.PropertiesAwareRepositoryImpl;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing Authentication Keys
 * @author Vivek
 * @version 1.1
 */
@Repository
public class AuthenticationRepositoryImpl  extends PropertiesAwareRepositoryImpl<AuthenticationKey> implements AuthenticationRepository {

    private static final Logger logger = Logger.getLogger(AuthenticationRepositoryImpl.class);

    public AuthenticationRepositoryImpl() {
        super(AuthenticationKey.class, AuthenticationKeysProperty.class);
    }

    @Override
    public AuthenticationKey getAuthenticationKey(String accessKey) throws AuthenticationKeyNotFoundException {
        Criteria criteria = getSession().createCriteria(AuthenticationKey.class);
        criteria.add(Restrictions.eq("accessKey",accessKey));
        Object result = criteria.uniqueResult();
        if( result != null)
            return (AuthenticationKey) result;
        else
            throw new AuthenticationKeyNotFoundException("Access key not found - "+accessKey);

    }

    @Override
    public List<AuthenticationKey> getAuthenticationKeys(long userId) {
        Criteria criteria = getSession().createCriteria(AuthenticationKey.class);
        criteria.add(Restrictions.eq("user.id",userId));
        return criteria.list();
    }

}
