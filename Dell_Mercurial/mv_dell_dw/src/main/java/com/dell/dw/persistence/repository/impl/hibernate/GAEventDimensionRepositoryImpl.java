package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.GAEventDimension;
import com.dell.dw.persistence.repository.GAEventDimensionRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 5/29/12
 * Time: 8:50 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class GAEventDimensionRepositoryImpl
        extends IdentifiableEntityRepositoryImpl<Long, GAEventDimension>
        implements GAEventDimensionRepository {

    public GAEventDimensionRepositoryImpl() {
        super(GAEventDimension.class);
    }

    /**
     * Get GA event dimension from db for a given Event Category, Event Action, Event Label and Profile ID
     * @param eventCategory
     * @param eventAction
     * @param eventLabel
     * @param profileId
     * @return
     */
    @Override
    public GAEventDimension getEventDimension(String eventCategory, String eventAction,
                                              String eventLabel, Long profileId) {
        Criteria criteria = getSession().createCriteria(GAEventDimension.class)
                .add(Restrictions.eq("eventCategory", eventCategory))
                .add(Restrictions.eq("eventAction", eventAction))
                .add(Restrictions.eq("eventLabel", eventLabel))
                .add(Restrictions.eq("gaWebPropertyProfile.id", profileId)).setMaxResults(1);
        if(criteria.list().isEmpty()) {
            return null;
        }
        return (GAEventDimension) criteria.list().get(0);
    }

    @Override
    public List<GAEventDimension> getEventDimensions(Long profileId) {
        Criteria criteria = getSession().createCriteria(GAEventDimension.class)
                .add(Restrictions.eq("gaWebPropertyProfile.id", profileId));
        if(criteria.list().isEmpty()) {
            return null;
        }
        return criteria.list();
    }
}
