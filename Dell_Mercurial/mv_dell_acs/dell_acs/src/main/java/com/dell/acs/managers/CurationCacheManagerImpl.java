package com.dell.acs.managers;


/**
 * @author Adarsh kumar
 * @author $LastChangedBy: Adarsh $
 * @version $Revision: 2704 $, $Date:: 2012-07-18 10:23:47#$
 */

import com.dell.acs.CurationCacheAlreadyExistsException;
import com.dell.acs.CurationCacheException;
import com.dell.acs.CurationCacheNotFoundException;
import com.dell.acs.persistence.domain.CurationCache;
import com.dell.acs.persistence.domain.CurationSource;
import com.dell.acs.persistence.repository.CurationCacheRepository;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Collection;

/**
 * CurationCacheManagerImpl provide implementation for
 * CurationCacheManager which have Set of functionality
 * declaration for Curation Cache Management
 */

@Service
public class CurationCacheManagerImpl implements CurationCacheManager {

    private static final Logger logger = Logger.getLogger(CurationCacheManagerImpl.class);

    @Autowired
    private CurationCacheRepository curationCacheRepository;

    public CurationCacheManagerImpl() { }

    @Override
    @Transactional
    public CurationCache saveCache(CurationCache curationCache)
            throws EntityExistsException, CurationCacheException {
        logger.debug("Request for createCurationCache()");
        return this.curationCacheRepository.saveCache(curationCache);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteCache(Long cacheID) throws EntityNotFoundException {
        logger.debug("Request for deleteCurationCache()");
        curationCacheRepository.remove(cacheID);
    }


    @Override
    @Transactional(readOnly = true)
    public Collection<CurationCache> getCaches(ServiceFilterBean filter, Long sourceID) {
        return curationCacheRepository.getCaches(filter,sourceID);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<CurationCache> getCaches(final ServiceFilterBean filter, final String columnName, final Object object)
            throws CurationCacheNotFoundException, CurationCacheException {
        final Collection<CurationCache> curationCache = curationCacheRepository.getCaches(filter, columnName, object);
        return curationCache;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean cacheExist(CurationCache curationCache)
            throws CurationCacheNotFoundException, CurationCacheException {
        logger.debug("Request for cacheExist()");
        return this.curationCacheRepository.cacheExist(curationCache);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CurationCache updateCache(CurationCache curationCache)
            throws CurationCacheNotFoundException, CurationCacheException {
        logger.debug("Request for updateCurationCache()");
        curationCache = this.curationCacheRepository.updateCache(curationCache);
        return curationCache;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CurationCache getCache(Long cacheID, Long sourceID, String guid) throws EntityNotFoundException {
        logger.debug("Request for selectCurationCache()");
        CurationCache curationCache = new CurationCache();
        curationCache.setId(cacheID);
        CurationSource curationSource = new CurationSource();
        curationSource.setId(sourceID);
        curationCache.setCurationSource(curationSource);
        curationCache.setGuid(guid);
        curationCache = this.curationCacheRepository.getCache(curationCache);
        return curationCache;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Collection<CurationCache> getCaches(ServiceFilterBean filter) {
        logger.debug("Request for selectCurationCacheBy() Paginated ");
        Collection<CurationCache> curationCacheList = this.curationCacheRepository.getCaches(filter);
        return curationCacheList;
    }

    @Override
    @Transactional(readOnly = true)
    public CurationCache getCache(Long cacheID) throws EntityNotFoundException {
        return this.curationCacheRepository.get(cacheID);
    }


}
