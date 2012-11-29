/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.persistence.domain.User;
import com.dell.acs.persistence.domain.UserProperty;
import com.dell.acs.persistence.repository.UserRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.PropertiesAwareRepositoryImpl;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2704 $, $Date:: 2012-05-29 10:23:47#$
 */
@Repository
public class UserRepositoryImpl extends PropertiesAwareRepositoryImpl<User> implements UserRepository {

    public UserRepositoryImpl() {
        super(User.class, UserProperty.class);
    }

    @Override
    @Transactional(readOnly = true)
    public User get(String username) {
        User example = new User();
        example.setUsername(username);
        return getUniqueByExample(example);
    }

    @Override
    public User getFacebookUser(String facebookId) {
        User example = new User();
        example.setFacebookId(facebookId);
        return getUniqueByExample(example);
    }

    @Override
    public User getEmail(String email) {
        User user = new User();
        user.setEmail(email);
        return getUniqueByExample(user);
    }

    @Override
    public List<User> getAllUsers(ServiceFilterBean filterBean) {
        if (filterBean != null) {
            Criteria criteria = getSession().createCriteria(entityClass);
            applyGenericCriteria(criteria, filterBean);
            return criteria.list();
        } else {
            return super.getAll();
        }
    }
}
