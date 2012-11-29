/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.dw.managers;

import com.dell.dw.persistence.domain.User;
import com.dell.dw.persistence.domain.UserRole;
import com.sourcen.core.managers.Manager;

/**
 * @author Navin Raj
 * @version 1/31/12 11:37 AM
 */
public interface UserManager extends Manager {

    public User getUser(String username);

    void createUser(User user);

    void updateUser(User dellUser);

    UserRole getRole(String roleName);

}
