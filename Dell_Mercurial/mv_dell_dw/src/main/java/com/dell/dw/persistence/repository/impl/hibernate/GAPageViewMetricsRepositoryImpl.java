package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.managers.dataimport.util.DateUtils;
import com.dell.dw.persistence.domain.GAPageViewDimension;
import com.dell.dw.persistence.domain.GAPageViewMetrics;
import com.dell.dw.persistence.domain.GAWebPropertyProfile;
import com.dell.dw.persistence.repository.GAPageViewMetricsRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.hql.internal.ast.QuerySyntaxException;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 7/11/12
 * Time: 6:31 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class GAPageViewMetricsRepositoryImpl  extends IdentifiableEntityRepositoryImpl<Long, GAPageViewMetrics>
        implements GAPageViewMetricsRepository {

    public GAPageViewMetricsRepositoryImpl() {
        super(GAPageViewMetrics.class);
    }

    /**
     *
     * @param profileId
     * @param date
     */
    @Override
    public void removeTillDate(Long profileId, Date date) {
        try {
            Query query = getSession().getNamedQuery("GAPageViewMetrics.deleteTillDate");
            query.setParameter("profileId", profileId);
            query.setParameter("date", date);
            int rowCount = query.executeUpdate();
        } catch (QuerySyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param dimensionId
     * @param date
     * @return
     */
    @Override
    public List<GAPageViewMetrics> getPageViewMetricsTillDate(Long dimensionId, Date date) {
        Criteria criteria = getSession().createCriteria(GAPageViewMetrics.class)
                .add(Restrictions.eq("pageViewDimension.id", dimensionId))
                .add(Restrictions.lt("date", date));
        if(criteria.list().isEmpty()) {
            return null;
        }
        return criteria.list();
    }

    @Override
    public Object[] getTodayPageViews(Long profileId) {
        Object[] todayPageViews = null;
        Date today = new Date();
        try {
            String hql = "SELECT SUM(m.pageViews), (CASE SUM(m.pageLoadSample) WHEN 0 THEN 0 ELSE (SUM(m.pageLoadTime)/SUM(m.pageLoadSample)) END) from " +
                    "GAPageViewMetrics m, GAPageViewDimension d, GAWebPropertyProfile p " +
                    "where m.pageViewDimension.id = d.id and d.gaWebPropertyProfile.id = p.id " +
                    "and m.date like '"+DateUtils.getFormattedDate(today,"yyyy-MM-dd")+"%' and p.id = " + profileId + " " +
                    "group by p.id";
            Query query = getSession().createQuery(hql);
            todayPageViews = (Object[]) query.uniqueResult();
        } catch (QuerySyntaxException e) {
            logger.error(e.getMessage());
        }
        return todayPageViews;
    }

    @Override
    public List<Long> getDailyPageViews(Long profileId) {
        List<Long> list = null;
        try {
            String hql = "SELECT SUM(m.pageViews) from " +
                    "GAPageViewMetrics m, GAPageViewDimension d, GAWebPropertyProfile p " +
                    "where m.pageViewDimension.id = d.id and d.gaWebPropertyProfile.id = p.id and p.id = " + profileId + " " +
                    "group by p.id, year(m.date), month(m.date), day(m.date)";
            Query query = getSession().createQuery(hql);
            list = query.list();
        } catch (QuerySyntaxException e) {
            logger.error(e.getMessage());
        }
        return list;
    }

    @Override
    public Object getDailyAvgLoadTime(Long profileId) {
        Object value = null;
        //(CASE SUM(m.pageLoadSample) WHEN 0 THEN 0 ELSE (SUM(m.pageLoadTime)/SUM(m.pageLoadSample)) END)
        try {
            String hql = "SELECT (CASE SUM(m.pageLoadSample) WHEN 0 THEN 0 ELSE (SUM(m.pageLoadTime)/SUM(m.pageLoadSample)) END) from " +
                    "GAPageViewMetrics m, GAPageViewDimension d, GAWebPropertyProfile p " +
                    "where m.pageViewDimension.id = d.id and d.gaWebPropertyProfile.id = p.id and p.id = " + profileId + " " +
                    "group by p.id";
            Query query = getSession().createQuery(hql);
            value = query.uniqueResult();
        } catch (QuerySyntaxException e) {
            logger.error(e.getMessage());
        }
        return value;
    }

}
