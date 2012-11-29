package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.GAEventMetrics;
import com.dell.dw.persistence.repository.GAEventMetricsRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.hql.internal.ast.QuerySyntaxException;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 7/11/12
 * Time: 6:31 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class GAEventMetricsRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, GAEventMetrics>
        implements GAEventMetricsRepository {
    public GAEventMetricsRepositoryImpl() {
        super(GAEventMetrics.class);
    }

    @Override
    public void removeTillDate(Long profileId, Date date) {
        try {
            Query query = getSession().getNamedQuery("GAEventMetrics.deleteTillDate");
            query.setParameter("profileId", profileId);
            query.setParameter("date", date);
            int rowCount = query.executeUpdate();
        } catch (QuerySyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<GAEventMetrics> getEventMetricsTillDate(Long dimensionId, Date date) {
        Criteria criteria = getSession().createCriteria(GAEventMetrics.class)
                .add(Restrictions.eq("eventDimension.id", dimensionId))
                .add(Restrictions.lt("date", date));
        if(criteria.list().isEmpty()) {
            return null;
        }
        return criteria.list();
    }
}
