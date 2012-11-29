/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.dw.spring.security;

import com.dell.dw.managers.UserManager;
import com.dell.dw.persistence.domain.User;
import com.dell.dw.persistence.domain.UserRole;
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

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy$
 * @version $Revision$, $Date::                     $
 */
public class DbUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(DbUserDetailsService.class);

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {

        try {
            User bean = userManager.getUser(username);
            System.out.println(bean);
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
