package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.GAPageViewDimension;
import com.dell.dw.persistence.repository.GAPageViewDimensionRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 5/30/12
 * Time: 1:44 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class GAPageViewDimensionRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, GAPageViewDimension>
        implements GAPageViewDimensionRepository {

    public GAPageViewDimensionRepositoryImpl() {
        super(GAPageViewDimension.class);
    }

    /**
     * Get GA Page View Dimension from db
     * @param pageTitle
     * @param pagePath
     * @param pageDepth
     * @param profileId
     * @return
     */
    @Override
    public GAPageViewDimension getPageViewDimension(String pageTitle, String pagePath,
                                                    Integer pageDepth, Long profileId) {
        Criteria criteria = getSession().createCriteria(GAPageViewDimension.class)
                .add(Restrictions.eq("pageTitle", pageTitle))
                .add(Restrictions.eq("pagePath", pagePath))
                .add(Restrictions.eq("pageDepth", pageDepth))
                .add(Restrictions.eq("gaWebPropertyProfile.id",profileId)).setMaxResults(1);

        if(criteria.list().isEmpty()) {
            return null;
        }
        return (GAPageViewDimension) criteria.list().get(0);
    }

    @Override
    public List<GAPageViewDimension> getPageViewDimensions(Long profileId) {
        Criteria criteria = getSession().createCriteria(GAPageViewDimension.class)
                .add(Restrictions.eq("gaWebPropertyProfile.id", profileId));
        if(criteria.list().isEmpty()) {
            return null;
        }
        return criteria.list();
    }
}
