package com.dell.dw.persistence.repository;

import com.dell.dw.persistence.domain.Order;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 6/26/12
 * Time: 12:37 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OrderRepository extends IdentifiableEntityRepository<Long, Order> {

    Order getByOrderId(String orderId);

    Collection<Object[]> getTotalOrdersByDate();
}
