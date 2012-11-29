package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.SFOrder;
import com.dell.dw.persistence.repository.SFOrderRepository;
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
@Repository(value="sfOrderRepositoryImpl")
public class SFOrderRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, SFOrder> implements SFOrderRepository {

    public SFOrderRepositoryImpl() {
        super(SFOrder.class);
    }

    @Override
    public SFOrder getByOrderId(String orderId) {
        Criteria criteria = getSession().createCriteria(SFOrder.class)
                .add(Restrictions.eq("orderId", orderId));
        SFOrder obj = (SFOrder) criteria.uniqueResult();
        return obj;
    }

    @Override
    public Collection<Object[]> getTotalOrdersByDate() {
        HashMap<String, Long> result = new HashMap<String, Long>();
        Criteria criteria = getSession().createCriteria(SFOrder.class)
                .setProjection(
                        Projections.projectionList()
                                .add(Projections.rowCount())
                                .add(Projections.sqlGroupProjection("CONVERT(CHAR(10), purchaseDate, 101) as purchaseDate", "CONVERT(CHAR(10), purchaseDate, 101)", new String[] { "purchaseDate" }, new Type[] { StandardBasicTypes.STRING })))
                .add(Restrictions.ne("orderCancelled", true));
         return criteria.list();
    }

}
