package com.dell.acs.web.ws.v2;

/**
 * @author Adarsh kumar
 * @author $LastChangedBy: Adarsh $
 * @version $Revision: 3707 $, $Date:: 2012-07-18 4:21 PM#$
 */

import com.dell.acs.CurationContentAlreadyExistsException;
import com.dell.acs.CurationContentException;
import com.dell.acs.CurationContentNotFoundException;
import com.dell.acs.persistence.domain.CurationContent;
import com.sourcen.core.util.beans.ServiceFilterBean;
import com.sourcen.core.web.ws.WebService;

import java.util.Collection;

/**
 * CuratedContentService offers set of functionality
 * for Curation Content Management by using Curation
 * Content Manager's and Repository
 */
public interface CuratedContentService extends WebService {

    /**
     * createCurationContent() provide the functionality for
     * creating the Curation Content by providing necessary arguments
     *
     * @param curationID      accepts the Long type argument for curation Id
     * @param curationCacheId accepts the Long type curationCache id
     * @param taxonomyID      accepts the Long type argument for taxonomy id
     * @param type            accepts the String type argument for type of source of curationContent
     * @return CurationContent persistence state Object
     * @throws CurationContentAlreadyExistsException
     *
     * @throws CurationContentException
     */
    CurationContent createCurationContent(Long curationID, Long curationCacheId, Long taxonomyID, String type)
            throws CurationContentAlreadyExistsException, CurationContentException;

    /**
     * getCurationContent() provide the functionality for
     * retrieving the Collection of Curation Content
     *
     * @param filter accepts the ServiceFileBean type argument for selection base
     * @return Collection of curationContent object having persistence state
     * @throws CurationContentNotFoundException
     *
     * @throws CurationContentException
     */
    Collection<CurationContent> getCurationContent(ServiceFilterBean filter)
            throws CurationContentNotFoundException, CurationContentException;

    /**
     * getCurationContentByType() provide the functionality for
     * retrieving the Collection of Curation Content for specified type
     *
     * @param filter accepts the ServiceFileBean type argument for selection base
     * @param type   accepts the String type argument for type of the Source
     * @return Collection of curationContent object having persistence state
     * @throws CurationContentNotFoundException
     *
     * @throws CurationContentException
     */
    Collection<CurationContent> getCurationContentByType(ServiceFilterBean filter, String type)
            throws CurationContentNotFoundException, CurationContentException;

    /**
     * getCurationContentByCategory() provide the functionality for
     * retrieving the collection of curation Content for specified taxonomy
     *
     * @param filter     accepts the ServiceFileBean type argument for selection base
     * @param taxonomyID accept the Long type argument for taxonomyId
     * @return Collection of curationContent object having persistence state
     * @throws CurationContentNotFoundException
     *
     * @throws CurationContentException
     */
    Collection<CurationContent> getCurationContentByCategory(ServiceFilterBean filter, Long taxonomyID)
            throws CurationContentNotFoundException, CurationContentException;

    /**
     * removeCurationContent() provide the functionality for
     * deleting the Curation Content
     *
     * @param curationContentID accepts the Long type Curation content Id
     *                          which is the base for deletion
     * @return the int type value having status for deletion operation
     *         0 is for successful and 1 is for failure
     * @throws CurationContentNotFoundException
     *
     * @throws CurationContentException
     */
    int removeCurationContent(Long curationContentID)
            throws CurationContentNotFoundException, CurationContentException;

    /**
     * addFavorite() provide the functionality for marking a particular
     * Curation Content as the Favorite
     *
     * @param curationContentID accepts the Long type Curation content Id
     *                          which is the base for marking as favorite
     * @return the int type value having status for marking operation
     *         0 is for successful and 1 is for failure
     * @throws CurationContentNotFoundException
     *
     * @throws CurationContentException
     */
    int addFavorite(Long curationContentID)
            throws CurationContentNotFoundException, CurationContentException;

    /**
     * removeFavorite() provide the functionality for removing the
     * marking of particular Curation Content which is Favorite
     *
     * @param curationContentID accepts the Long type Curation content Id
     *                          which is the base for removing from favorite
     * @return the int type value having status for marking operation
     *         0 is for successful and 1 is for failure
     * @throws CurationContentNotFoundException
     *
     * @throws CurationContentException
     */
    int removeFavorite(Long curationContentID)
            throws CurationContentNotFoundException, CurationContentException;

    /**
     * addSticky() provide the functionality for marking a particular
     * Curation Content as the Sticky
     *
     * @param curationContentID accepts the Long type Curation content Id
     *                          which is the base for marking as Sticky
     * @return the int type value having status for marking operation
     *         0 is for successful and 1 is for failure
     * @throws CurationContentNotFoundException
     *
     * @throws CurationContentException
     */
    int addSticky(Long curationContentID)
            throws CurationContentNotFoundException, CurationContentException;

    /**
     * removeSticky() provide the functionality for removing the
     * marking of particular Curation Content which is Sticky
     *
     * @param curationContentID accepts the Long type Curation content Id
     *                          which is the base for removing from Sticky
     * @return the int type value having status for marking operation
     *         0 is for successful and 1 is for failure
     * @throws CurationContentNotFoundException
     *
     * @throws CurationContentException
     */
    int removeSticky(Long curationContentID)
            throws CurationContentNotFoundException, CurationContentException;

}
