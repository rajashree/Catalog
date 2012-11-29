/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.persistence.domain.UserRole;
import com.dell.acs.persistence.repository.UserRoleRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;

import org.springframework.stereotype.Repository;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2704 $, $Date:: 2012-05-29 10:23:47#$
 */
@Repository
public class UserRoleRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, UserRole>
        implements UserRoleRepository {

    public UserRoleRepositoryImpl() {
        super(UserRole.class);
    }

    @Override
    public UserRole getRoleByName(String roleName) {
        UserRole template = new UserRole();
        template.setName(roleName);
        return super.getUniqueByExample(template);
    }
}
