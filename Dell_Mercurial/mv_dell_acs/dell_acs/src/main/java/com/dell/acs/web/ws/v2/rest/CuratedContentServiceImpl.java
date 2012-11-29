package com.dell.acs.web.ws.v2.rest;

/*
 * @author Adarsh kumar
 * @author $LastChangedBy: Adarsh $
 * @version $Revision: 3707 $, $Date:: 2012-07-18 4:21 PM#$
 */


import com.dell.acs.CurationContentAlreadyExistsException;
import com.dell.acs.CurationContentException;
import com.dell.acs.CurationContentNotFoundException;
import com.dell.acs.content.EntityConstants;
import com.dell.acs.managers.CurationCacheManager;
import com.dell.acs.managers.CurationContentManager;
import com.dell.acs.managers.CurationManager;
import com.dell.acs.managers.CurationSourceManager;
import com.dell.acs.persistence.domain.CurationContent;
import com.dell.acs.persistence.domain.CurationSourceType;
import com.dell.acs.web.ws.v2.CuratedContentService;
import com.sourcen.core.util.Assert;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebService;
import java.util.Collection;

/*   CuratedContentServiceImpl class provide implementation for the set
*   of services declaration CuratedContentService
*
*/

@WebService
@RequestMapping("/api/v2/rest/CurationContentService")
public class CuratedContentServiceImpl extends WebServiceImpl implements CuratedContentService {
    private static final Logger logger = Logger.getLogger(CuratedContentServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "createCurationContent", method = RequestMethod.POST)
    public CurationContent createCurationContent(@RequestParam(required = true) Long curationID
            , @RequestParam(required = true) Long curationCacheId
            , @RequestParam(required = true) Long categoryID
            , @RequestParam(required = true) String type)
            throws CurationContentAlreadyExistsException, CurationContentException {
       /* Assert.notNull(curationCacheId, "curationCacheId Argument in createCurationContent is Null");
        Assert.notNull(categoryID, "categoryID Argument in createCurationContent is Null");
        Assert.notNull(type, "type Argument in createCurationContent is Null");
        CurationContent curationContent = new CurationContent();
        curationContent.setCuration(curationManager.getCuration(curationID));
        curationContent.setCategoryID(categoryID);
        curationContent.setCacheContent(curationCacheManager.getCache(curationCacheId));
        curationContent.setType(EntityConstants.CurationSourceType.valueOf(type).getId());
        return curationContentManager.createContent(curationContent);*/
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "getCurationContent", method = RequestMethod.GET)
    public Collection<CurationContent> getCurationContent(@ModelAttribute ServiceFilterBean filter)
            throws CurationContentNotFoundException, CurationContentException {
        return curationContentManager.getContents(filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "getCurationContentByType", method = RequestMethod.GET)
    public Collection<CurationContent> getCurationContentByType(@ModelAttribute ServiceFilterBean filter
            , @RequestParam(required = true) String type)
            throws CurationContentNotFoundException, CurationContentException {
        Assert.notNull(type, "Type Argument in getCurationContentByType is Null");
        return curationContentManager.getContents(filter, "type", CurationSourceType.valueOf(type));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "getCurationContentByCategory", method = RequestMethod.GET)
    public Collection<CurationContent> getCurationContentByCategory(@ModelAttribute ServiceFilterBean filter
            , @RequestParam(required = true) Long taxonomyID)
            throws CurationContentNotFoundException, CurationContentException {
        Assert.notNull(taxonomyID, "taxonomyID Argument in getCurationContentByCategory is Null");
        return curationContentManager.getContents(filter, "taxonomyCategoryId", taxonomyID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "removeCurationContent", method = RequestMethod.POST)
    public int removeCurationContent(@RequestParam(required = true) Long curationContentID)
            throws CurationContentNotFoundException, CurationContentException {
        Assert.notNull(curationContentID, "curationContentID Argument in removeCurationContent is Null");
        try {
            curationContentManager.deleteContent(curationContentID);
        } catch (Exception e) {
            return 1;
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "addFavorite", method = RequestMethod.POST)
    public int addFavorite(@RequestParam(required = true) Long curationContentID)
            throws CurationContentNotFoundException, CurationContentException {
        Assert.notNull(curationContentID, "curationContentID Argument in addFavorite is Null");
        CurationContent curationContent = curationContentManager.getContent(curationContentID);
        curationContent.setFavorite(true);
        if (curationContentManager.updateContent(curationContent) == null) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "removeFavorite", method = RequestMethod.POST)
    public int removeFavorite(@RequestParam(required = true) Long curationContentID)
            throws CurationContentNotFoundException, CurationContentException {
        Assert.notNull(curationContentID, "curationContentID Argument in removeFavorite is Null");
        CurationContent curationContent = curationContentManager.getContent(curationContentID);
        curationContent.setFavorite(false);
        if (curationContentManager.updateContent(curationContent) == null) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "addSticky", method = RequestMethod.POST)
    public int addSticky(@RequestParam(required = true) Long curationContentID)
            throws CurationContentNotFoundException, CurationContentException {
        Assert.notNull(curationContentID, "curationContentID Argument in addSticky is Null");
        CurationContent curationContent = curationContentManager.getContent(curationContentID);
        curationContent.setSticky(true);
        if (curationContentManager.updateContent(curationContent) == null) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping(value = "removeSticky", method = RequestMethod.POST)
    public int removeSticky(@RequestParam(required = true) Long curationContentID)
            throws CurationContentNotFoundException, CurationContentException {
        Assert.notNull(curationContentID, "curationContentID Argument in removeSticky is Null");
        CurationContent curationContent = curationContentManager.getContent(curationContentID);
        curationContent.setSticky(false);
        if (curationContentManager.updateContent(curationContent) == null) {
            return 1;
        } else {
            return 0;
        }
    }


    /*Spring Dependency Injection*/
    @Autowired
    private CurationManager curationManager;

    public void setCurationManager(CurationManager curationManager) {
        this.curationManager = curationManager;
    }

    @Autowired
    private CurationSourceManager curationSourceManager;

    public void setCurationSourceManager(CurationSourceManager curationSourceManager) {
        this.curationSourceManager = curationSourceManager;
    }

    @Autowired
    private CurationCacheManager curationCacheManager;

    public void setCurationCacheManager(CurationCacheManager curationCacheManager) {
        this.curationCacheManager = curationCacheManager;
    }

    @Autowired
    private CurationContentManager curationContentManager;

    public void setCurationContentManager(CurationContentManager curationContentManager) {
        this.curationContentManager = curationContentManager;
    }
}
