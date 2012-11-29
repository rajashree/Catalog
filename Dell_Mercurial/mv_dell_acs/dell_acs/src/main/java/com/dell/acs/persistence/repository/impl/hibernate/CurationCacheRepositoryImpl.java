package com.dell.acs.persistence.repository.impl.hibernate;


/**
 * @author Adarsh kumar
 * @author $LastChangedBy: Adarsh $
 * @version $Revision: 2704 $, $Date:: 2012-07-18 10:23:47#$
 */

import com.dell.acs.CurationCacheException;
import com.dell.acs.CurationCacheNotFoundException;
import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.CurationCache;
import com.dell.acs.persistence.domain.CurationCacheProeprty;
import com.dell.acs.persistence.repository.CurationCacheRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.PropertiesAwareRepositoryImpl;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityExistsException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * CurationCacheRepositoryImpl Class provide the implementation for
 * CurationCacheRepository interface which have
 * Curation Cache Management Functionality Declaration
 */

@Repository
public class CurationCacheRepositoryImpl extends PropertiesAwareRepositoryImpl<CurationCache>
        implements CurationCacheRepository {

    private static final Logger logger =  Logger.getLogger(CurationCacheRepositoryImpl.class);

    public CurationCacheRepositoryImpl() {
        super(CurationCache.class, CurationCacheProeprty.class);
        logger.debug("CurationCacheRepositoryImpl instantiate ");
    }

    @Override
    public CurationCache searchCurationCacheById(CurationCache curationCache) {
        logger.debug("Request for searchCurationCacheById()");
        CurationCache curationCacheObject = get(curationCache.getId());
        if (curationCacheObject != null
                && curationCacheObject.getStatus() == EntityConstants.Status.PUBLISHED.getId()) {
            return curationCacheObject;
        }
        return null;
    }

    @Override
    public CurationCache saveCache(CurationCache curationCache) throws EntityExistsException {
        logger.debug("Request for insetCurationCache()");
        if (cacheExist(curationCache)) {
            throw new EntityExistsException("Socurce cache object is already cached in 't_curation_cache' table" + curationCache.getId());
        }
        try {
            insert(curationCache);
        } catch (Exception e) {
            logger.error("Unable to save the cache object.", e);
            throw new RuntimeException(e.getMessage());
        }
        return curationCache;
    }

    @Override
    public CurationCache updateCache(CurationCache curationCache)
            throws CurationCacheNotFoundException, CurationCacheException {
        logger.debug("Request for updateCurationCacheById()");
        if (cacheExist(curationCache)) {
            try {
                update(curationCache);
            } catch (Exception e) {
                throw new CurationCacheException(e.getMessage());
            }
        } else {
            throw new CurationCacheNotFoundException("Curation Cache Not Found for id " + curationCache.getId());
        }
        return curationCache;
    }


    @Override
    public boolean deleteCache(CurationCache curationCache)
            throws CurationCacheNotFoundException, CurationCacheException {
        logger.debug("Request for deleteCurationCache()");
        if (cacheExist(curationCache)) {
            try {
                super.remove(curationCache);
            } catch (Exception e) {
                throw new CurationCacheException(e.getMessage());
            }
        } else {
            throw new CurationCacheNotFoundException("Curation Cache Not Found for id " + curationCache.getId());
        }
        return true;
    }


    @Override
    public List<CurationCache> getCaches(ServiceFilterBean filter) {
        Criteria criteria = getSession().createCriteria(CurationCache.class);
        criteria.add(Restrictions.eq("status", EntityConstants.Status.PUBLISHED.getId()));
        applyGenericCriteria(criteria, filter);
        return criteria.list();
    }


    @Override
    public Collection<CurationCache> getCaches(ServiceFilterBean filter, Long sourceID) {
        Criteria criteria = getSession().createCriteria(CurationCache.class);
        applyGenericCriteria(criteria, filter);
        criteria.add(Restrictions.eq("status", EntityConstants.Status.PUBLISHED.getId()));
        criteria.add(Restrictions.eq("curationSource.id", sourceID));
        return criteria.list();
    }

    @Override
    public CurationCache getCache(Long cacheID, Long sourceID, String guid) {
        Criteria criteria = getSession().createCriteria(CurationCache.class);

        if(cacheID != null){
            criteria.add(Restrictions.eq("id", cacheID));
        }
        if(StringUtils.isNotEmpty(guid)){
            criteria.add(Restrictions.eq("guid", guid));
        }
        if(sourceID != null){
            criteria.add(Restrictions.eq("curationSource.id", sourceID));
        }
        CurationCache cache = (CurationCache)criteria.uniqueResult();
        if(cache != null){
            return cache;
        }
        logger.info("Unable to find the cache object with id " + cacheID +  ", source " + sourceID + " guid " + guid);
        return null;
    }

    @Override
    public CurationCache getCache(CurationCache curationCache) {
        if (cacheExist(curationCache)) {
            CurationCache curationCacheObject = get(curationCache.getId());
            if (curationCacheObject != null
                    && curationCacheObject.getStatus() == EntityConstants.Status.PUBLISHED.getId()) {
                return curationCacheObject;
            }
        }
        return null;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public boolean cacheExist(CurationCache curationCache)
            throws CurationCacheNotFoundException, CurationCacheException {
        logger.debug("Request for curationCacheExist()");
        Criteria criteria = getSession().createCriteria(CurationCache.class);
        criteria.add(
                Restrictions.and(
                        Restrictions.eq("curationSource.id", curationCache.getCurationSource().getId()),
                        Restrictions.like("guid", curationCache.getGuid())
                )
        );
        List<CurationCache> list = null;
        try {
            list = criteria.list();
        } catch (Exception e) {
            throw new CurationCacheException(e.getMessage());
        }
        if (list == null) {
            throw new CurationCacheNotFoundException("Curation Cache Not Found for id " + curationCache.getId());
        }
        if (list.size() > 0) {
            return true;
        }
        return false;
    }


    @Override
    public Collection<CurationCache> getCaches(ServiceFilterBean filter, String columnName, Object columnValue) throws RuntimeException{
        final Criteria criteria = getSession().createCriteria(CurationCache.class);
        criteria.add(Restrictions.eq(columnName, columnValue));
        applyGenericCriteria(criteria, filter);
        List<CurationCache> list = null;
        try {
            list = criteria.list();
        } catch (Exception e) {
            logger.error("Unable to load the cache items for column " + columnValue + " with  value " + columnValue, e);
            e.printStackTrace();
        }
        if (list != null && list.size() > 0) {
            return list;
        }
        logger.info("No cache object found for column " + columnValue + " with value " + columnValue);
        return Collections.emptyList();

    }

}
