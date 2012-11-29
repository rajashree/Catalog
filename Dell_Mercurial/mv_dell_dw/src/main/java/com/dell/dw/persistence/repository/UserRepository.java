/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.dw.persistence.repository;


import com.dell.dw.persistence.domain.User;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy$
 * @version $Revision$, $Date::                     $
 */
public interface UserRepository extends IdentifiableEntityRepository<Long, User> {

    public User get(String username);
}
