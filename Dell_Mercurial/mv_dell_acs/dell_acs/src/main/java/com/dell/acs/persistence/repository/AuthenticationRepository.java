package com.dell.acs.persistence.repository;

import com.dell.acs.AuthenticationKeyNotFoundException;
import com.dell.acs.persistence.domain.AuthenticationKey;

import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import java.util.List;

/**
 * @author Vivek
 * @version 1.1
 */
public interface AuthenticationRepository extends IdentifiableEntityRepository<Long, AuthenticationKey> {

    /**
     * Returns the list of AuthenticationKey for an User
     * @param userId
     * @return
     */
    List<AuthenticationKey> getAuthenticationKeys(long userId);

    /**
     * Return an AuthenticationKey for a specified accessKey
     * @param accessKey
     * @return AuthenticationKey
     * @throws AuthenticationKeyNotFoundException
     */
    AuthenticationKey getAuthenticationKey(String accessKey) throws AuthenticationKeyNotFoundException;

}

