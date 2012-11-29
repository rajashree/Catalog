/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.dw.spring.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy$
 * @version $Revision$, $Date::                     $
 */
public class UserDetailsImpl extends User implements UserDetails {

    public UserDetailsImpl(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public UserDetailsImpl(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    @Override
    public boolean isAnonymous() {
        if (getUsername().equalsIgnoreCase("anonymous")) {
            return true;
        }
        return false;
    }

    @Override
    public com.dell.dw.persistence.domain.User getUser() {
        return userBean;
    }

    private com.dell.dw.persistence.domain.User userBean;

    public void setUser(com.dell.dw.persistence.domain.User userBean) {
        this.userBean = userBean;
    }
}

