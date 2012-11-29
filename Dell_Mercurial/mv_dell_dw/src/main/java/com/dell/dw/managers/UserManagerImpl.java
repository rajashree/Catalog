/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.dw.managers;

import com.dell.dw.persistence.domain.User;
import com.dell.dw.persistence.domain.UserRole;
import com.dell.dw.persistence.repository.UserRepository;
import com.dell.dw.persistence.repository.UserRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy$
 * @version $Revision$, $Date::                     $
 */
@Service
public class UserManagerImpl implements UserManager {

    private static final Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public User getUser(String username) {
        return userRepository.get(username);
    }


    @Override
    @Transactional
    public void createUser(User user) {
        userRepository.insert(user);
    }

    @Override
    @Transactional
    public void updateUser(User dellUser) {
        userRepository.update(dellUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserRole getRole(String roleName) {
        return userRoleRepository.getRoleByName(roleName);
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }
}
