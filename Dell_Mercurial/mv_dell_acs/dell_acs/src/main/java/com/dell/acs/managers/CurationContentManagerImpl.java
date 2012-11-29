package com.dell.acs.managers;

/**
 * @author Adarsh kumar
 * @author $LastChangedBy: Adarsh $
 * @version $Revision: 3707 $, $Date:: 2012-07-19 11:37 AM#$
 */

import com.dell.acs.CurationContentException;
import com.dell.acs.CurationContentNotFoundException;
import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.*;
import com.dell.acs.persistence.repository.CurationContentRepository;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.Date;

@Service
public class CurationContentManagerImpl implements CurationContentManager {

    private static final Logger logger = LoggerFactory.getLogger(CurationContentManagerImpl.class);

    @Autowired
    private CurationManager curationManager;

    @Autowired
    private CurationCacheManager curationCacheManager;

    @Autowired
    private TaxonomyManager taxonomyManager;

    public CurationContentManagerImpl() {}

    @Override
    @Transactional
    public CurationContent createContent(CurationContent curationContent)
            throws EntityExistsException {

        logger.debug("Request for saveCurationContent()");
        //create the Curation content
        curationContentRepository.createContent(curationContent);
        logger.info("Successfully created the Curation Content");
        return curationContent;
    }

    @Override
    @Transactional
    public CurationContent updateContent(CurationContent curationContent)
            throws CurationContentException {
        logger.debug("Request for updateCurationContent()");
        Assert.notNull(curationContent, "Curation Content Object Can't be Null");
        curationContentRepository.updateContent(curationContent);
        logger.info("Successfully updated the Curation Content with id := "
                + curationContent.getId());
        return curationContent;
    }

    @Override
    @Transactional
    public void deleteContent(Long contentID) throws EntityNotFoundException{
        logger.debug("Request for deleteCurationContent()");
        Assert.notNull(contentID, "Curation Content Id Can't be Null");
        CurationContent content = curationContentRepository.get(contentID);
        if(content == null){
            throw new EntityNotFoundException("Content not found.");
        }
        //content.setStatus(EntityConstants.Status.DELETED.getId());
        curationContentRepository.remove(contentID);
    }

    @Override
    @Transactional
    public void deleteContent(CurationContent curationContent) throws CurationContentException {
        curationContent.setStatus(EntityConstants.Status.DELETED.getId());
        curationContentRepository.update(curationContent);
    }


    @Override
    @Transactional(readOnly = true)
    public CurationContent getContent(Long curationContentID)
            throws CurationContentNotFoundException, CurationContentException {
        logger.debug("Request for getCurationContentById()");
        Assert.notNull(curationContentID, "Curation Content Id Can't be Null");
        CurationContent curationContent = null;
        try {
            curationContent = curationContentRepository.get(curationContentID);
        } catch (Exception e) {
            throw new CurationContentException(e.getMessage());
        }
        if (curationContent == null) {
            throw new CurationContentNotFoundException("Curation Content Not Found for Id " + curationContentID);
        }
        if (curationContent.getStatus() == EntityConstants.Status.DELETED.getId()) {
            throw new CurationContentException("Curation Content is Flag as Deleted ");
        }
        return curationContent;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<CurationContent> getContents(ServiceFilterBean filter, Long curationID, Long categoryID) {
        logger.debug("Request for getCurationContents()");
        return curationContentRepository.getContents(filter, curationID, categoryID);
    }

    @Override
    @Transactional
    public CurationContent addContent(Long curationID, Long contentID, Long categoryID,
                                           Integer type, Integer position, User user) throws EntityExistsException {

        Curation curation = curationManager.getCuration(curationID);
        Assert.notNull(curation, "Curation object not found for id - " + curationID);

        //All content Types can be supported generically  by using CurationContent's cacheContent_id. This will future
        //proof for other content types - Products, Events, Coupons, etc.
        //Currently, supporting Documents(IMAGE, ARTICLE, VIDEO, LINK, DOC), Feed (TWITTER, RSS, FACEBOOK, YOUTUBE)

        TaxonomyCategory category = taxonomyManager.getTaxonomyCategory(categoryID);
        Assert.notNull(category, "Curation category object not found for id - " + categoryID);

        CurationContent curatedContent = new CurationContent(curation, contentID, user);
        curatedContent.setStatus(EntityConstants.Status.PUBLISHED.getId());
        curatedContent.setType(EntityConstants.CurationSourceType.getById(type).getId());
        // Check if the content is already associated
        if(isContentAssociated(curatedContent)){
            logger.info("Content is already associated with category");
            throw new EntityExistsException("Content is already associated with the category");
        }else{
            curatedContent.setCategoryID(categoryID);
            curatedContent.setCreatedDate(new Date());
            curatedContent.setModifiedDate(new Date());
            curatedContent.setCreatedBy(user);
            curatedContent.setModifiedBy(user);
            curatedContent.setFavorite(false);
            curatedContent.setSticky(false);
            curatedContent.setPosition(position);
            curationContentRepository.createContent(curatedContent);
        }

        return curatedContent;
    }

    @Override
    @Transactional
    public void updateContentPosition(Long contentID, Integer position) {
        curationContentRepository.updateContentPosition(contentID, position);
    }

    @Override
    @Transactional
    public void updateFavouriteStatus(Long contentID, boolean status){
        Assert.notNull(contentID, "Required parameter 'contentID' is missing.");
        curationContentRepository.updateFavouriteStatus(contentID, status);
    }

    @Override
    @Transactional
    public void updateStickyStatus(Long contentID, boolean status){
        Assert.notNull(contentID, "Required parameter 'contentID' is missing.");
        curationContentRepository.updateStickyStatus(contentID, status);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<CurationContent> getContents(ServiceFilterBean filter)
            throws CurationContentException, CurationContentNotFoundException {
        logger.debug("Request for getCurationContents()");
        return curationContentRepository.getContents(filter);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<CurationContent> getContents(ServiceFilterBean filter, String columnName, Object columnValue)
            throws CurationContentException, CurationContentNotFoundException {
        logger.debug("Request for getCurationContents()");
        return curationContentRepository.getContents(filter, columnName, columnValue);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isContentAssociated(CurationContent content) throws EntityExistsException {
        CurationContent example = curationContentRepository.getUniqueByExample(content);
        if(example != null){
            return true;
        }
        return false;
    }


        @Override
    @Transactional(readOnly = true)
    public boolean isContentSticky(Long curationContentId) throws CurationContentException, CurationContentNotFoundException {
        Assert.notNull(curationContentId, "Curation Content Id Can't be Null");
        CurationContent curationContent = null;
        try {
            curationContent = curationContentRepository.get(curationContentId);
        } catch (Exception e) {
            throw new CurationContentException(e.getMessage());
        }
        if (curationContent == null) {
            throw new CurationContentNotFoundException("Curation Content Not Found for Id " + curationContentId);
        }
        return curationContent.isSticky();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isContentFavorite(Long curationContentId) throws CurationContentException, CurationContentNotFoundException {
        Assert.notNull(curationContentId, "Curation Content Id Can't be Null");
        CurationContent curationContent = null;
        try {
            curationContent = curationContentRepository.get(curationContentId);
        } catch (Exception e) {
            throw new CurationContentException(e.getMessage());
        }
        if (curationContent == null) {
            throw new CurationContentNotFoundException("Curation Content Not Found for Id " + curationContentId);
        }
        return curationContent.isFavorite();
    }

    @Autowired
    private CurationContentRepository curationContentRepository;
}
