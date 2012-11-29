package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.OTGLead;
import com.dell.dw.persistence.repository.OTGRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 6/26/12
 * Time: 12:38 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class OTGRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, OTGLead> implements OTGRepository {

    public OTGRepositoryImpl() {
        super(OTGLead.class);
    }

}
