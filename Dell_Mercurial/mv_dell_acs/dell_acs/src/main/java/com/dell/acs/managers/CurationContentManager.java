package com.dell.acs.managers;

/**
 * @author Adarsh kumar
 * @author $LastChangedBy: Adarsh $
 * @version $Revision: 3707 $, $Date:: 2012-07-19 11:37 AM#$
 */

import com.dell.acs.CurationContentAlreadyExistsException;
import com.dell.acs.CurationContentException;
import com.dell.acs.CurationContentNotFoundException;
import com.dell.acs.persistence.domain.CurationContent;
import com.dell.acs.persistence.domain.User;
import com.sourcen.core.managers.Manager;
import com.sourcen.core.util.beans.ServiceFilterBean;

import javax.persistence.EntityExistsException;
import java.util.Collection;
/*
*   CurationContentManager provides the set of Services
 *  which is used for the Curation Content Management
 *  its consist of method's which are providing
 *  assistance for C.U.R.D operation on the
 *  Curation Content by using Curation Content Repository
* */

public interface CurationContentManager extends Manager {

    /**
     *  Method to create a Curated content mapping entry in t_curation_content table
     * @param contentData
     * @return - returns newly content object
     * @throws CurationContentException
     * @throws CurationContentAlreadyExistsException
     */
    CurationContent createContent(CurationContent contentData) throws CurationContentException, CurationContentAlreadyExistsException;

    /**
     * Method to update the Curated content mapping entry in t_curation_content table
     * @param contentData
     * @return  - returns updated content object
     * @throws CurationContentException
     */
    CurationContent updateContent(CurationContent contentData) throws CurationContentException;

    /**
     * Method to delete the curation, category and content mapping entry
     * @param contentID
     * @throws CurationContentException
     */
    void deleteContent(Long contentID) throws CurationContentException;

    /**
     * Method to delete the curation, category and content mapping entry
     * @param curationContent
     * @throws CurationContentException
     */
    void deleteContent(CurationContent curationContent) throws CurationContentException;

    /**
     * Method to retrieve a contet using ID
     * @param contentID
     * @return - returns content object
     * @throws CurationContentNotFoundException
     * @throws CurationContentException
     */
    CurationContent getContent(Long contentID) throws CurationContentNotFoundException, CurationContentException;

    /**
     * Method to retrieve all contents associated to a particular curation and category
     * @param curationID - Curation ID
     * @param categoryID - Category id
     * @return - return list of all associated content objects
     */
    Collection<CurationContent> getContents(ServiceFilterBean filter, Long curationID, Long categoryID);

    /**
     * Method to add a curated content item to a category
     * @param curationID
     * @param contentID
     * @param categoryID
     * @param type
     * @param position
     * @param user
     * @return
     * @throws CurationContentAlreadyExistsException
     */
    CurationContent addContent(Long curationID, Long contentID, Long categoryID, Integer type, Integer position, User user)
            throws EntityExistsException;


    void updateContentPosition(Long contentID, Integer position);

    /**
     * Set the curated item as favourite
     * @param contentID
     */
    void updateFavouriteStatus(Long contentID, boolean status);

    void updateStickyStatus(Long contentID, boolean status);

    /**
     * Method to return a paginated content objects
     * @param start
     * @param pageSize
     * @return - collection of content objects
     * @throws CurationContentException
     * @throws CurationContentNotFoundException
     */
    Collection<CurationContent> getContents(ServiceFilterBean filter)
            throws CurationContentException, CurationContentNotFoundException;

    /**
     * Method to return a paginated content objects based on the criteria set by columnName and columnValue data
     * @param columnName - Name of column in t_curation_content
     * @param columnValue - the value which is to be compared with the columnName
     * @param limit -
     * @return - collection of content objects
     * @throws CurationContentException
     * @throws CurationContentNotFoundException
     */
    Collection<CurationContent> getContents(ServiceFilterBean filter, String columnName, Object columnValue)
            throws CurationContentException, CurationContentNotFoundException;


    boolean isContentAssociated(CurationContent content) throws EntityExistsException;

    /**
     * Method to check if the content object is marked as sticky
     * @param contentID
     * @return
     * @throws CurationContentException
     * @throws CurationContentNotFoundException
     */
    boolean isContentSticky(Long contentID) throws CurationContentException, CurationContentNotFoundException;

    /**
     * Method to check if the content object is marked as favourite
     * @param contentID
     * @return
     * @throws CurationContentException
     * @throws CurationContentNotFoundException
     */
    boolean isContentFavorite(Long contentID) throws CurationContentException, CurationContentNotFoundException;

}
