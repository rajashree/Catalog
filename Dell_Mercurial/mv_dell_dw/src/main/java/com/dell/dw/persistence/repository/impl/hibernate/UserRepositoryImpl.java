/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.User;
import com.dell.dw.persistence.domain.UserProperty;
import com.dell.dw.persistence.repository.UserRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.PropertiesAwareRepositoryImpl;
import org.springframework.stereotype.Repository;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy$
 * @version $Revision$, $Date::                     $
 */
@Repository
public class UserRepositoryImpl extends PropertiesAwareRepositoryImpl<User> implements UserRepository {

    public UserRepositoryImpl() {
        super(User.class, UserProperty.class);
    }

    @Override
    public User get(String username) {
        User example = new User();
        example.setUsername(username);
        return getUniqueByExample(example);
    }
}
