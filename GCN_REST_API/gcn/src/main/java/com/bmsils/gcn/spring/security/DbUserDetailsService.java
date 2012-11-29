/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.spring.security;

import com.bmsils.gcn.managers.UserManager;
import com.bmsils.gcn.persistence.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/23/12
 * Time: 3:03 PM
 * DbUserDetailsService to store user specific data
 */

@Component
public class DbUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(DbUserDetailsService.class);

    /**
     * loadUserByUsername loads the userdetails and also adds the Granted Authorities
     * @param username
     * @return
     * @throws UsernameNotFoundException
     * @throws DataAccessException
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {

        try {
            User user = userManager.getUser(username);
            Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            if(user.isAdmin()){
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }else{
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }

            UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUserGCN(),user.getPassword(),authorities);
            return userDetails;
        } catch (Exception e) {
            logger.error("Unable to find record with username:=" + username, e);
        }
        return null;
    }

    /**
     * Dependency Injection of various Spring beans follows
     */
    @Autowired
    private UserManager userManager;
    

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}
