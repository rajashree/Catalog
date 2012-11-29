package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.D3Link;
import com.dell.dw.persistence.repository.D3LinkRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 5/30/12
 * Time: 7:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class D3LinkRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, D3Link>
        implements D3LinkRepository {

    public D3LinkRepositoryImpl() {
        super(D3Link.class);
    }
}