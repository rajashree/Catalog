/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.AuthenticationKeyNotFoundException;
import com.dell.acs.auth.AuthUtil;
import com.dell.acs.auth.MarketVineAuthentication;
import com.dell.acs.persistence.domain.AuthenticationKey;
import com.dell.acs.persistence.domain.User;
import com.dell.acs.persistence.domain.UserRole;
import com.dell.acs.persistence.repository.AuthenticationRepository;
import com.sourcen.core.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 13:56:39#$
 */
@Service
public class AuthenticationManagerImpl implements AuthenticationManager {

    private static final Logger logger = Logger.getLogger(AuthenticationManagerImpl.class);

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Autowired
    private UserManager userManager;

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<AuthenticationKey> getAuthenticationKeys(long userId) {
        return authenticationRepository.getAuthenticationKeys(userId);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public AuthenticationKey getAuthenticationKey(String accessKey) throws AuthenticationKeyNotFoundException {
        return authenticationRepository.getAuthenticationKey(accessKey);
    }


    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public AuthenticationKey modifyAuthenticationKeyStatus(String accessKey, int status) throws AuthenticationKeyNotFoundException {
        AuthenticationKey key = this.authenticationRepository.getAuthenticationKey(accessKey);
        logger.info("Revoking the following access key as requested -   "+key.getAccessKey());

        if( status == 0)
            key.setStatus(Status.DISABLED.ordinal());
        else if(status == 1)
            key.setStatus(Status.ENABLED.ordinal());

        authenticationRepository.update(key);
        return key;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public AuthenticationKey saveAuthenticationKey(User user) {
        logger.debug(" Generate and save the keys for the User ::: " + user);
        String secretKey = StringUtils.getUUID(true) + StringUtils.getUUID(true);
        String accessKey = StringUtils.getUUID(true);
        AuthenticationKey key = new AuthenticationKey();
        key.setAccessKey(accessKey);
        key.setSecretKey(secretKey);
        key.setUser(user);
        key.setStatus(AuthenticationManager.Status.ENABLED.ordinal());
        key.setCreatedBy(user);
        key.setModifiedBy(user);
        key.setCreatedDate(new Date());
        key.setModifiedDate(new Date());

        logger.debug(" Access Key ::: " + key.getAccessKey());
        logger.debug(" Secret Key ::: " + key.getSecretKey());

        authenticationRepository.insert(key);

        logger.debug(" Successfully saved!!! ");
        return key;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication token = null;
        try {
            AuthenticationKey authKey = getAuthenticationKey((String) authentication.getCredentials());
            if (authKey.getStatus() == 0) {
                logger.error("Access Key has been revoked. Please try with a valid Access Key   - " + authKey.getAccessKey());
                throw new BadCredentialsException("Access Key has been revoked. Please try with a valid Access Key.");
            }
            String secretKey = authKey.getSecretKey();
            String requestURI = (String) authentication.getDetails();
            String singedRequestData = (String) authentication.getPrincipal();
            String serverSideSignedData = AuthUtil.generateHMAC(requestURI, secretKey);
            if (serverSideSignedData.equals(singedRequestData)) {
                User user = authKey.getUser();
                logger.info("The request is from a valid User :::  " + user.getUsername());
                logger.info("User Roles  :::    " + user.getRoles());
                Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(user.getRoles().size());
                for (UserRole role : user.getRoles()) {
                    authorities.add(new SimpleGrantedAuthority(role.getName()));
                }

                token = new MarketVineAuthentication(user, authentication.getCredentials(), authorities);

            } else {
                logger.error("Authentication failed. The signatures do not match.");
                throw new BadCredentialsException("Authentication failed. The signatures do not match.");
            }

        } catch (AuthenticationKeyNotFoundException e) {
            //TODO: Need to decide the business message that needs to be returned
            logger.error("Invalid Access Key found in the header.");
            throw new BadCredentialsException("Invalid access key");
        }
        return token;
    }

    @Override
    @Transactional(readOnly = true)
    public AuthenticationKey getByID(Long authKeyID) {
        return authenticationRepository.get(authKeyID);
    }
}
