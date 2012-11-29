/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.UserNotFoundException;

import com.dell.acs.persistence.domain.User;
import com.dell.acs.persistence.domain.UserRole;
import com.dell.acs.persistence.repository.UserRepository;
import com.dell.acs.persistence.repository.UserRoleRepository;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 572 $, $Date:: 2012-03-08 14:56:09#$
 */
@Service
public class UserManagerImpl implements UserManager {

    private static final Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);


    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<User> getUsers(Integer start, Integer maxResult) {
        return userRepository.getAll(start, maxResult);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<User> getUsers(ServiceFilterBean filterBean) {
        return this.userRepository.getAllUsers(filterBean);
    }


    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public User getUser(Long id) throws UserNotFoundException {
        User user = userRepository.get(id);
        if (user == null) {
            throw new UserNotFoundException("User by ID '"+id+"' does not exist");
        }
        return user;
    }


    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public User getUser(String username) throws UserNotFoundException {
        User user = userRepository.get(username);
        if (user == null) {
            throw new UserNotFoundException("User by username '"+username+"' does not exist");
        }
        return user;
    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {
        User user = this.userRepository.getEmail(email);
        if(user == null) {
            throw new UserNotFoundException("User by email '"+email+"' does not exist");
        }
        return user;
    }

    /*Check whether user exist or not */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public User getFacebookUser(String facebookId) {
        return userRepository.getFacebookUser(facebookId);
    }

    @Override
    @Transactional
    public User createUser(User user) {
        userRepository.insert(user);
        return  user;
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        userRepository.update(user);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public UserRole getRole(String roleName) {
        return userRoleRepository.getRoleByName(roleName);
    }

    @Override
    public List<UserRole> getAllRoles() {
        return userRoleRepository.getAll();
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
