package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.GAAccount;
import com.dell.dw.persistence.repository.GAAccountRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Devashree
 * Date: 6/13/12
 * Time: 12:17 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class GAAccountRepositoryImpl  extends IdentifiableEntityRepositoryImpl<Long, GAAccount>
        implements GAAccountRepository {

    public GAAccountRepositoryImpl() {
        super(GAAccount.class);
    }
}