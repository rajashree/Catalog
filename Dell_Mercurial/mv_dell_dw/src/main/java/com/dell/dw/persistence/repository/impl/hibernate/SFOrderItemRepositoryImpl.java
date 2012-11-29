package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.SFOrderItem;
import com.dell.dw.persistence.repository.SFOrderItemRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 6/26/12
 * Time: 12:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class SFOrderItemRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, SFOrderItem> implements SFOrderItemRepository {

    public SFOrderItemRepositoryImpl() {
        super(SFOrderItem.class);
    }

}

