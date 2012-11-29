/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository;

import com.dell.acs.persistence.domain.UserRole;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2704 $, $Date:: 2012-05-29 10:23:47#$
 */
public interface UserRoleRepository extends IdentifiableEntityRepository<Long, UserRole> {

    UserRole getRoleByName(String roleName);

}

