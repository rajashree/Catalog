/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository;

import com.dell.acs.persistence.domain.User;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;
import com.sourcen.core.util.beans.ServiceFilterBean;

import java.util.List;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2704 $, $Date:: 2012-05-29 10:23:47#$
 */
public interface UserRepository extends IdentifiableEntityRepository<Long, User> {

    public User get(String username);

    User getFacebookUser(String facebookId);

    /**
     * Return <code>User</code> which matches an User's email address.
     * @param email - String - email address of an user
     * @return {@link User}
     */
    public User getEmail(String email);

    public List<User> getAllUsers(ServiceFilterBean filterBean);
}
