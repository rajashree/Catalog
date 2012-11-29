/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.spring.security;

import com.dell.acs.persistence.domain.User;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 572 $, $Date:: 2012-03-08 10:26:09#$
 */
public interface UserDetails extends org.springframework.security.core.userdetails.UserDetails {

    boolean isAnonymous();

    User getUser();

}
