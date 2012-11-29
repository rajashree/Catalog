package com.dell.acs.persistence.repository;

/**
 * @author Adarsh kumar
 * @author $LastChangedBy: Adarsh $
 * @version $Revision: 2704 $, $Date:: 2012-07-18 10:23:47#$
 */

import com.dell.acs.CurationCacheAlreadyExistsException;
import com.dell.acs.CurationCacheException;
import com.dell.acs.CurationCacheNotFoundException;
import com.dell.acs.persistence.domain.CurationCache;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;
import com.sourcen.core.util.beans.ServiceFilterBean;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Collection;

/**
 * CurationCacheRepository provides the set of Services
 * which is used for the CurationCache Management
 * its consist of method's which are providing
 * assistance for C.U.R.D operation on the Database by using Hibernate
 */
public interface CurationCacheRepository extends IdentifiableEntityRepository<Long, CurationCache> {

    /**
     * Method to persist the content Cache object
     * @param curationCache
     * @return
     * @throws CurationCacheAlreadyExistsException
     * @throws CurationCacheException
     */
    CurationCache saveCache(CurationCache curationCache) throws EntityExistsException;


    /**
     * updateCurationCacheById()provide the functionality of updating of the
     * CurationCache object into the database
     *
     * @param curationCache CurationCache Entity Object as the parameter
     * @return CurationCache Entity Object as the return object having persistence state
     * @throws CurationCacheNotFoundException
     * @throws CurationCacheException
     */
    CurationCache updateCache(CurationCache curationCache) throws EntityNotFoundException;


    /**
     * deleteCurationCache() provide the functionality for deletion of the
     * CurationCache object form the database
     *
     * @param curationCache object as the parameter for getting the id value
     * @return boolean value if the deletion is successful then return true
     *         otherwise false
     * @throws CurationCacheNotFoundException
     * @throws CurationCacheException
     */
    boolean deleteCache(CurationCache curationCache) throws EntityNotFoundException;


    /**
     * getCurationCache() is for retrieval of CurationCache object on the
     * basic of the column name and value of that column which is in form of object
     *
     * @param columnName name of the column which is user for selection
     * @param object     value of the Column which is used for selection
     * @param limit      size for selection
     * @return return the list of the Data Object
     * @throws CurationCacheNotFoundException
     * @throws CurationCacheException
     */
    Collection<CurationCache> getCaches(ServiceFilterBean filter, String columnName, Object object);

    /**
     * getCurationCache() provide the functionality of selection of the
     * CurationCache object from the Database its uses base as the curationCacheId
     * ,curationSourceId and guId for selection
     *
     * @param curationCache object as the parameter for getting the id value
     * @return CurationCache Entity Object as the return object having persistence state
     * @throws CurationCacheNotFoundException
     * @throws CurationCacheException
     */
    CurationCache getCache(Long cacheID, Long sourceID, String guid) throws EntityNotFoundException;


    CurationCache getCache(CurationCache curationCache) throws CurationCacheNotFoundException, CurationCacheException;

    /**
     * searchCurationCacheById() provide the functionality of selection of the
     * CurationCache object from the Database
     *
     * @param curationCache object as the parameter for getting the id value
     * @return CurationCache Entity Object as the return object having persistence state
     * @throws CurationCacheNotFoundException
     * @throws CurationCacheException
     */
    CurationCache searchCurationCacheById(CurationCache curationCache) throws CurationCacheNotFoundException, CurationCacheException;

    /**
     * getCurationCache() provide the functionality of selection of the
     * CurationCache object from the Database in paginated manner
     *
     * @param start    starting row number for fetching data
     * @param pageSize ending row number for fetching data
     * @return List object having curationCache object
     * @throws CurationCacheNotFoundException
     * @throws CurationCacheException
     */
    Collection<CurationCache> getCaches(ServiceFilterBean filter);

    Collection<CurationCache> getCaches(ServiceFilterBean filter, Long sourceID);

    /**
     * curationCacheExist() provide the functionality of checking the
     * existence of the CurationCache object in the database on the based of the
     * Curation Id ,Curation Source Id and the Guid
     *
     * @param curationCache accepts CurationCache Object as the parameter
     * @return boolean value if the object exist int he database otherwise false
     * @throws CurationCacheNotFoundException
     * @throws CurationCacheException
     */
    boolean cacheExist(CurationCache curationCache)
            throws CurationCacheNotFoundException, CurationCacheException;

}
