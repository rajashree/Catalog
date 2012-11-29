/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.persistence.domain.WhitePaper;
import com.dell.acs.persistence.repository.WhitePaperRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.springframework.stereotype.Repository;

/**
 * @author Adarsh.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2704 $, $Date:: 2012-05-29 10:23:47#$
 */
@Repository
public class WhitePaperRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, WhitePaper>
        implements WhitePaperRepository {

    public WhitePaperRepositoryImpl() {
        super(WhitePaper.class);
    }

}
