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
import com.sourcen.core.util.beans.ServiceFilterBean;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Collection;

/**
 * CurationCacheManager provides the set of Services
 * which is used for the CurationCache Management
 * its consist of method's which are providing
 * assistance for C.U.R.D operation on the
 * CurationCache
 */
public interface CurationCacheManager {

    /**
     * createCurationCache() create new CurationCache
     * before creating its check for if CurationCache exist or not
     * if exist throws Exception
     *
     * @param curationCache object as parameter
     * @return CurationCache Entity object
     * @throws CurationCacheAlreadyExistsException
     *
     * @throws CurationCacheException
     */
    CurationCache saveCache(CurationCache curationCache)
            throws EntityExistsException, CurationCacheException;


    /**
     * deleteCurationCache() is for deletion of the CurationCache object
     * before performing delete operation its is checking for its existence
     * on the bases of Curation Id ,Curation Source Id and Gui Id
     *
     * @param curationCacheId value as the criteria for deletion
     * @throws CurationCacheNotFoundException
     * @throws CurationCacheException
     */
    void deleteCache(Long cacheID) throws EntityNotFoundException;


    CurationCache updateCache(CurationCache curationCache) throws EntityNotFoundException, EntityExistsException;

    /**
     * getCurationCache() is for selection of the CurationCache
     * form database on the basic of Curation Id ,Curation Source Id and Gui Id
     *
     * @param curationCacheId  Its the Id value of the CurationCache
     * @param curationSourceId Its the CurationSource Id value
     * @param guId             Its the Gui Id value
     * @return Its return the CurationCache Entity Object
     * @throws CurationCacheNotFoundException
     * @throws CurationCacheException
     */
    CurationCache getCache(Long cacheID, Long sourceID, String guid) throws CurationCacheNotFoundException, CurationCacheException;


    CurationCache getCache(Long cacheID) throws EntityNotFoundException;


    /**
     * getCurationCacheAll() provide the functionality of selection of the
     * CurationCache object from the Database in paginated manner by using
     * CurationCache Repository
     *
     * @param start    starting row number for fetching data
     * @param pageSize ending row number for fetching data
     * @return List object having CurationCache Entity object
     * @throws CurationCacheNotFoundException
     * @throws CurationCacheException
     */
    Collection<CurationCache> getCaches(ServiceFilterBean filter) throws CurationCacheNotFoundException, CurationCacheException;


    Collection<CurationCache> getCaches(ServiceFilterBean filter, Long sourceID);

    Collection<CurationCache> getCaches(ServiceFilterBean filter, String columnName, Object object);

    /**
     * cacheExist() checks for the existence of the CurationCache object
     * in the database on the bases of Curation Id ,Curation Soruce Id and Gui Id
     *
     * @param curationCache object as parameter
     * @return boolean if its exist then return true and if not then false
     * @throws CurationCacheNotFoundException
     * @throws CurationCacheException
     */
    boolean cacheExist(CurationCache curationCache);

}
