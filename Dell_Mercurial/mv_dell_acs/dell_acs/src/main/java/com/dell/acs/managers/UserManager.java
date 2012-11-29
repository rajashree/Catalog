/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.UserNotFoundException;
import com.dell.acs.persistence.domain.User;
import com.dell.acs.persistence.domain.UserRole;
import com.sourcen.core.managers.Manager;
import com.sourcen.core.util.beans.ServiceFilterBean;

import java.util.List;

/**
 * @author Navin Raj
 * @version 1/31/12 11:37 AM
 */
public interface UserManager extends Manager {

    /**
     * Return an User by unique ID
     *
     * @param id - Long - The unique identifier of a User
     * @return {@link User}
     * @throws UserNotFoundException
     */
    public User getUser(Long id) throws UserNotFoundException;

    /**
     * Return an User by userName
     *
     * @param username String - unique username of an User
     * @return {@link User}
     * @throws UserNotFoundException
     */
    public User getUser(String username) throws UserNotFoundException;


    /**
     * Return an User by email
     * @param email String - email address of an User
     * @return {@link User}
     * @throws UserNotFoundException
     */
    public User getUserByEmail(String email) throws UserNotFoundException;


    /**
     * Return an User by User's facebook identifier
     *
     * @param facebookId String - facebook ID
     * @return {@link User}
     */
    public User getFacebookUser(String facebookId);

    /**
     * Creates an User within Content Server
     *
     * @param user {@link User} The user object needs to have pre-populated
     *             mandatory fields - username, firstName, lastName,
     *             email, password
     * @return {@link User}
     */
    User createUser(User user);

    /**
     * Update the User information.
     *
     * @param dellUser - {@link User} The update is restricted - username,
     *                 firstName, lastName, email, password
     * @return {@link User}
     */
    User updateUser(User dellUser);

    /**
     * @param roleName
     * @return
     */
    UserRole getRole(String roleName);

    /**
     * Fetches all the Users in the system
     * @param filterBean - The filterBean is an ValueObject that can be used to build
     *                   criteria
     *
     * @return a list of Users
     */
    List<User> getUsers(ServiceFilterBean filterBean);

    /**
     * Fetches Users from the system with a specified range
     *
     * @param start     -
     * @param maxResult
     * @return
     */
    List<User> getUsers(Integer start, Integer maxResult);

    /**
     * Get all the Roles which are defined in the system
     *
     * @return {@link UserRole} - Collection of <code>UserRole</code>
     */
    List<UserRole> getAllRoles();


}
