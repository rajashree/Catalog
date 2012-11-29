/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.AuthenticationKeyNotFoundException;
import com.dell.acs.persistence.domain.AuthenticationKey;
import com.dell.acs.persistence.domain.User;
import com.sourcen.core.managers.Manager;

import java.util.List;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: Sandeep Heggi $
 */
public interface AuthenticationManager extends Manager, org.springframework.security.authentication.AuthenticationManager  {

    public static enum Status  {
        DISABLED,ENABLED
    }

    /**
     * Returns a list of AuthenticationKeys held by an User
     * @param userId - Long User's id
     * @return List of AuthenticationKeys
     */
    List<AuthenticationKey> getAuthenticationKeys(long userId);

    /**
     * Returns an AuthenticationKey if the accessKey is found
     * @param accessKey - String
     * @return {@see AuthenticationKey}
     * @throws AuthenticationKeyNotFoundException
     */
    AuthenticationKey getAuthenticationKey(String accessKey) throws AuthenticationKeyNotFoundException;

    /**
     * Refactored the existing removeAuthenticationKey() to modifyAuthenticationKeyStatus().
     * This was carried to support enabling of Auth Keys after being revoked
     * @param accessKey
     * @param status
     * @return
     * @throws AuthenticationKeyNotFoundException
     */
    AuthenticationKey modifyAuthenticationKeyStatus(String accessKey, int status) throws AuthenticationKeyNotFoundException;

    /**
     * Saves an AuthenticationKey
     * @param user The User for which the key is to be saved for.
     * @return Successfully saved AuthenticationKey
     */
    AuthenticationKey saveAuthenticationKey(User user);

    /**
     * Return an AuthenticationKey by unique identifier
     * @param authKeyID Long authKey
     * @return AuthenticationKey
     */
    AuthenticationKey getByID(Long authKeyID);
}
