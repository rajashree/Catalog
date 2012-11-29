package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.GAWebPropertyProfile;
import com.dell.dw.persistence.repository.GAWebPropertyProfileRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 5/28/12
 * Time: 3:36 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class GAWebPropertyProfileRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, GAWebPropertyProfile>
        implements GAWebPropertyProfileRepository {

    public GAWebPropertyProfileRepositoryImpl() {
        super(GAWebPropertyProfile.class);
    }

    /**
     *
     * @return
     */
    @Override
    public GAWebPropertyProfile getUnprocessedProfile() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Get All unprocessed google analytics profiles
     * @return
     */
    @Override
    public List<GAWebPropertyProfile> getUnprocessedProfiles() {
        Criteria criteria = getSession().createCriteria(GAWebPropertyProfile.class)
                .createAlias("gaWebProperty","gaWebProperty")
                .add(Restrictions.eq("gaWebProperty.active", true))
                .add(Restrictions.ne("status", GAWebPropertyProfile.Status.PROCESSING));
        List result = criteria.list();
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    /**
     * Get last downloaded date of google analytics profile
     * @param webPropertyProfileId
     * @return
     */
    @Override
    public Date getLastDownloadedDate(Long webPropertyProfileId) {
        Criteria criteria = getSession().createCriteria(GAWebPropertyProfile.class)
                .add(Restrictions.eq("id", webPropertyProfileId));

        GAWebPropertyProfile result = (GAWebPropertyProfile)criteria.uniqueResult();
        if (result != null) {
            if(result.getLastDownloadedDate() != null){
                return result.getLastDownloadedDate();
            }else{
                return result.getInitializationDate();
            }
        }
        return null;
    }

    /**
     * Update last downloaded date
     * @param webPropertyProfileId
     * @param lastDownloadedDate
     */
    @Override
    public void updateLastDownloadedDate(Long webPropertyProfileId, Date lastDownloadedDate) {
        Criteria criteria = getSession().createCriteria(GAWebPropertyProfile.class)
                .add(Restrictions.eq("id", webPropertyProfileId)) ;
        GAWebPropertyProfile result = (GAWebPropertyProfile)criteria.uniqueResult();
        if (result != null) {
            result.setLastDownloadedDate(lastDownloadedDate);
            super.getSession().saveOrUpdate(result);
        }
    }

    /**
     *
     * @param webPropertyProfileId
     * @param webPropertyId
     * @param accountId
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public GAWebPropertyProfile getWebPropertyProfile(Long webPropertyProfileId, String webPropertyId, Long accountId) {
        Criteria criteria = getSession().createCriteria(GAWebPropertyProfile.class)
                .createAlias("gaWebProperty.gaAccount","gaAccount")
                .add(Restrictions.eq("id", webPropertyProfileId))
                .add(Restrictions.eq("gaWebProperty.id", webPropertyId))
                .add(Restrictions.eq("gaAccount.id", accountId));
        GAWebPropertyProfile result = (GAWebPropertyProfile) criteria.uniqueResult();
        return  result;
    }

    /**
     * Get initialization date of google analytics profile
     * @param webPropertyProfileId
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public Date getInitializationDate(Long webPropertyProfileId) {
        Criteria criteria = getSession().createCriteria(GAWebPropertyProfile.class)
                .add(Restrictions.eq("id", webPropertyProfileId));

        GAWebPropertyProfile result = (GAWebPropertyProfile)criteria.uniqueResult();
        if (result != null) {
             return result.getInitializationDate();
        }
        return null;
    }

    @Override
    public GAWebPropertyProfile getWebPropertyProfile(String webPropertyId) {
        Criteria criteria = getSession().createCriteria(GAWebPropertyProfile.class)
                .add(Restrictions.eq("gaWebProperty.id", webPropertyId));
        return  (GAWebPropertyProfile)criteria.uniqueResult();
    }

    @Override
    public List<GAWebPropertyProfile> getActiveProfiles() {
        Criteria criteria = getSession().createCriteria(GAWebPropertyProfile.class)
                        .add(Restrictions.eq("gaWebProperty.active", true));
        List<GAWebPropertyProfile> list = criteria.list();
        if(list.isEmpty()) {
            return null;
        }
        return list;
    }
}