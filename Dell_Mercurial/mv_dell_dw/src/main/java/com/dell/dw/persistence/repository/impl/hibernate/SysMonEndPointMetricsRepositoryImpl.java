package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.SysMonEndPointMetrics;
import com.dell.dw.persistence.repository.SysMonEndPointMetricsRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 8/1/12
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class SysMonEndPointMetricsRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, SysMonEndPointMetrics> implements SysMonEndPointMetricsRepository {

    public SysMonEndPointMetricsRepositoryImpl() {
        super(SysMonEndPointMetrics.class);
    }

    @Override
    public Collection<SysMonEndPointMetrics> getEndPointMetrices(Long endpointId) {
        Criteria criteria = getSession().createCriteria(SysMonEndPointMetrics.class)
                .add(Restrictions.eq("endPoint.id", endpointId));
        List result = criteria.list();
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }
}
