package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.SysMonServerHealthMetrics;
import com.dell.dw.persistence.repository.SysMonServerHealthMetricsRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 5/28/12
 * Time: 7:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class SysMonServerHealthMetricsRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, SysMonServerHealthMetrics>
        implements SysMonServerHealthMetricsRepository {

    public SysMonServerHealthMetricsRepositoryImpl() {
        super(SysMonServerHealthMetrics.class);
    }

    @Override
    public Collection<SysMonServerHealthMetrics> getServerMetrics(Long id) {
        Criteria criteria = getSession().createCriteria(SysMonServerHealthMetrics.class)
                .add(Restrictions.eq("server.id", id))
                .addOrder(Order.desc("updateDate"))
                .setMaxResults(1);
        List result = criteria.list();
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }
}
