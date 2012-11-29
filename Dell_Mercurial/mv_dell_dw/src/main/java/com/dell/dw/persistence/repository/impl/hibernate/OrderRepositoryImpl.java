package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.Order;
import com.dell.dw.persistence.repository.OrderRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 6/26/12
 * Time: 12:38 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class OrderRepositoryImpl  extends IdentifiableEntityRepositoryImpl<Long, Order> implements OrderRepository {

    public OrderRepositoryImpl() {
        super(Order.class);
    }

    @Override
    public Order getByOrderId(String orderId) {
        Criteria criteria = getSession().createCriteria(Order.class)
                .add(Restrictions.eq("orderId", orderId));
        Order obj = (Order) criteria.uniqueResult();
        return obj;
    }

    @Override
    public Collection<Object[]> getTotalOrdersByDate() {
        HashMap<String, Long> result = new HashMap<String, Long>();
        Criteria criteria = getSession().createCriteria(Order.class)
                .setProjection(
                        Projections.projectionList()
                                .add(Projections.rowCount())
                                .add(Projections.sqlGroupProjection("CONVERT(CHAR(10), transactionDate, 101) as transactionDate", "CONVERT(CHAR(10), transactionDate, 101)", new String[] { "transactionDate" }, new Type[] { StandardBasicTypes.STRING })))
                .add(Restrictions.ne("orderCancelled", true));
         return criteria.list();
    }

}
