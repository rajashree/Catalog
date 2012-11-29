package com.dell.acs.web.ws.v2;


/**
 * @author Adarsh kumar
 * @author $LastChangedBy: Adarsh $
 * @version $Revision: 3707 $, $Date:: 2012-07-18 4:21 PM#$
 */

import com.dell.acs.CurationCacheException;
import com.dell.acs.CurationCacheNotFoundException;
import com.dell.acs.persistence.domain.CurationCache;
import com.sourcen.core.util.beans.ServiceFilterBean;
import com.sourcen.core.web.ws.WebService;

import java.util.Collection;

/**
 * CuratedCacheService offers set of functionality
 * for Curation Cache Management by using Curation
 * Cache Manager's and Repository
 */
public interface CurationCacheService extends WebService {

    /**
     * getCurationCache() is for selection of the CurationCache
     * form database on the basic of Curation Id ,Curation Source Id and Gui Id
     *
     * @param curationCacheId  Its the Id value of the CurationCache
     * @param curationSourceId Its the CurationSource Id value
     * @param guId             Its the Gui Id value
     * @return Its return the CurationCache Entity Object
     * @throws com.dell.acs.CurationCacheNotFoundException
     *
     * @throws com.dell.acs.CurationCacheException
     *
     */
    CurationCache getCache(Long curationCacheId, Long curationSourceId, String guId)
            throws CurationCacheNotFoundException, CurationCacheException;


    /**
     * getCurationCache() provide the functionality of selection of the
     * CurationCache object from the Database in paginated manner by using
     * CurationCache Repository
     *
     * @param filter accept the Service Filter Bean object which has generic
     *               properties for fetching Data
     * @return List object having CurationCache Entity object
     * @throws CurationCacheNotFoundException
     * @throws CurationCacheException
     */
    Collection<CurationCache> getCache(ServiceFilterBean filter)
            throws CurationCacheNotFoundException, CurationCacheException;

    /**
     * getCurationCacheById() provide the functionality of selection of the
     * curation cache object from database on the basis of id
     *
     * @param curationCacheID value is the base for selection
     * @return CurationCache object of persistence state
     * @throws CurationCacheNotFoundException
     * @throws CurationCacheException
     */
    CurationCache getCache(Long curationCacheID)
            throws CurationCacheNotFoundException, CurationCacheException;

    /**
     * getCurationCacheBySource() provide the functionality for selection of the
     * curation Cache object from database on the basic of the source
     *
     * @param filter           accept the Service Filter Bean object which has generic
     *                         properties for fetching Data
     * @param curationSourceID accepts the Long Type curation Source Id
     * @return Collection of Curation Cache  object having Persistence state
     * @throws CurationCacheNotFoundException
     * @throws CurationCacheException
     */
    Collection<CurationCache> getCurationCacheBySource(ServiceFilterBean filter, Long curationSourceID)
            throws CurationCacheNotFoundException, CurationCacheException;


    /**
     * getCurationCacheByMatch() provide the functionality for selection of the
     * curation Cache object from database on the basic of the guid
     *
     * @param filter     accept the Service Filter Bean object which has generic
     *                   properties for fetching Data
     * @param columnName name of the Column which is base for selection of
     *                   Curation Cache
     * @param value      accept the Object type value for the Column which is provides for
     *                   selection base for Curation Cache object
     * @return Collection of Curation Cache  object having Persistence state
     * @throws CurationCacheNotFoundException
     * @throws CurationCacheException
     */
    Collection<CurationCache> getCurationCacheByMatch(ServiceFilterBean filter, String columnName, Object value)
            throws CurationCacheNotFoundException, CurationCacheException;
}
