package com.dell.acs.web.ws.v2.rest;

/**
 * @author Adarsh kumar
 * @author $LastChangedBy: Adarsh $
 * @version $Revision: 3707 $, $Date:: 2012-07-18 4:21 PM#$
 */

import com.dell.acs.CurationCacheException;
import com.dell.acs.CurationCacheNotFoundException;
import com.dell.acs.managers.CurationCacheManager;
import com.dell.acs.persistence.domain.CurationCache;
import com.dell.acs.web.ws.v2.CurationCacheService;
import com.sourcen.core.util.Assert;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebService;
import java.util.Collection;

/*
 * CurationCacheServiceImpl class provide implementation for the set
*   of services declaration CurationCacheService by using the
*   Curation Cache Manager's and Repository's
*/
@Deprecated
@WebService
@RequestMapping("/api/v2/rest/CurationCacheService")
public class CurationCacheServiceImpl extends WebServiceImpl
        implements CurationCacheService {


    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "getCurationCache", method = RequestMethod.GET)
    public CurationCache getCache(
            @RequestParam(required = true) Long curationCacheId
            , @RequestParam(required = true) Long curationSourceId
            , @RequestParam(required = true) String guId)
            throws CurationCacheNotFoundException, CurationCacheException {
        Assert.notNull(curationCacheId, "curationCacheId Argument in getCurationCache is Null");
        Assert.notNull(curationCacheId, "curationSourceId Argument in getCurationCache is Null");
        Assert.notNull(curationCacheId, "guId Argument in getCurationCache is Null");
        return this.curationCacheManager.getCache(curationCacheId, curationSourceId, guId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "getCurationCache", method = RequestMethod.GET)
    public Collection<CurationCache> getCache(@ModelAttribute ServiceFilterBean filter)
            throws CurationCacheNotFoundException, CurationCacheException {
        return this.curationCacheManager.getCaches(filter);
    }

    @Override
    @RequestMapping(value = "getCurationCacheByID", method = RequestMethod.GET)
    public CurationCache getCache(@RequestParam(required = true) Long curationCacheID)
            throws CurationCacheNotFoundException, CurationCacheException {
        Assert.notNull(curationCacheID, "curationCacheID Argument in getCurationCacheByID is Null");
        return this.curationCacheManager.getCache(curationCacheID);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "getCurationCacheBySource", method = RequestMethod.GET)
    public Collection<CurationCache> getCurationCacheBySource(@ModelAttribute ServiceFilterBean filter
            , @RequestParam(required = true) Long curationSourceID)
            throws CurationCacheNotFoundException, CurationCacheException {
        Assert.notNull(curationSourceID, "curationSourceID Argument in getCurationCacheBySource is Null");
        return curationCacheManager.getCaches(filter, "curationSource.id" , curationSourceID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "getCurationCacheByMatch", method = RequestMethod.GET)
    public Collection<CurationCache> getCurationCacheByMatch(@ModelAttribute ServiceFilterBean filter
            , @RequestParam(required = true) String columnName
            , @RequestParam(required = true) Object value)
            throws CurationCacheNotFoundException, CurationCacheException {
        Assert.notNull(columnName, "columnName Argument in getCurationCacheByMatch is Null");
        Assert.notNull(value, "value Argument in getCurationCacheByMatch is Null");
        return curationCacheManager.getCaches(filter, columnName, value);
    }

    /* Spring IOC */
    @Autowired
    private CurationCacheManager curationCacheManager;

    public void setCurationCacheManager(CurationCacheManager curationCacheManager) {
        this.curationCacheManager = curationCacheManager;
    }
}

