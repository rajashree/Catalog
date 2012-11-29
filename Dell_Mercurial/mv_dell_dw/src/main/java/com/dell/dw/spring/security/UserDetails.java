/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.dw.spring.security;

import com.dell.dw.persistence.domain.User;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy$
 * @version $Revision$, $Date::                     $
 */
public interface UserDetails extends org.springframework.security.core.userdetails.UserDetails {

    boolean isAnonymous();

    User getUser();

}
