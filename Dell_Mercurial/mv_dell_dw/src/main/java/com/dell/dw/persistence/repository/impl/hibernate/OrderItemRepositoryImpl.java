package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.OrderItem;
import com.dell.dw.persistence.repository.OrderItemRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 6/26/12
 * Time: 12:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class OrderItemRepositoryImpl  extends IdentifiableEntityRepositoryImpl<Long, OrderItem> implements OrderItemRepository {

    public OrderItemRepositoryImpl() {
        super(OrderItem.class);
    }

}

