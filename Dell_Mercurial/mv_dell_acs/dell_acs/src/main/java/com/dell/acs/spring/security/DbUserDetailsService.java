/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.spring.security;

import com.dell.acs.managers.UserManager;
import com.dell.acs.persistence.domain.User;
import com.dell.acs.persistence.domain.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 1027 $, $Date:: 2012-03-29 14:57:47#$
 */
public class DbUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(DbUserDetailsService.class);

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {

        try {
            User bean = userManager.getUser(username);
            bean.getProperties().setProperty("lastLoggedInTime",null);
            userManager.updateUser(bean);
            Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(bean.getRoles().size());
            for (UserRole role : bean.getRoles()) {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            }
            UserDetailsImpl details = new UserDetailsImpl(bean.getUsername(), bean.getPassword(), bean.getEnabled(),
                    true, true, true, authorities);
            details.setUser(bean);
            return details;
        } catch (Exception e) {
            logger.error("Unable to find record with username:=" + username, e);
        }
        return null;
    }

    @Autowired
    private UserManager userManager;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}
