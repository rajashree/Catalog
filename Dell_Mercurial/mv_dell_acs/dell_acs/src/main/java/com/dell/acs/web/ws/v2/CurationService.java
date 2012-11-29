/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v2;

import com.dell.acs.persistence.domain.Curation;
import com.dell.acs.persistence.domain.CurationCache;
import com.dell.acs.persistence.domain.CurationContent;
import com.dell.acs.persistence.domain.TaxonomyCategory;
import com.sourcen.core.util.beans.ServiceFilterBean;
import com.sourcen.core.web.ws.WebService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * @author Samee K.S
 * @author : sameeks $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */

public interface CurationService extends WebService {

    /**
     * Service to create new Curation
     * @param name - Name of the Curation
     * @param desc - Description of the Curation
     * @param site - Retailer Site for which the curation is being created
     * @return - Curation object with the generated ID
     */
    Curation createCuration(HttpServletRequest request, String name, String desc, Object site);

    /**
     * Service to update an existing Curation
     * @param curationID - ID of curation for which an update is required
     * @param name - Name of the Curation
     * @param desc - Description of the Curation
     * @return - Curation object with updated values
     */
    Curation updateCuration(HttpServletRequest request, Long curationID, String name, String desc);

    /**
     * Service to delete a curation
     * @param curationID - Curation id which needs to be deleted
     */
    void deleteCuration(Long curationID);


    Curation getCuration(Long curationID);

    /**
     * Servcie method to fetch the Curation specific to a RetailerSite based on siteName
     * @param site - Get Curations for the Retailer Site
     * @return - List of Curation objects
     */
    Collection<Curation> getCurations(ServiceFilterBean filter, Object site);

    /* Curation content related services */

    Collection<CurationCache> getSourceContents(ServiceFilterBean filter, Long sourceID);

    /**
     * Service to add a content source for curation
     * @param curationID - Curation to which the content is to be added
     * @param cacheID - ID of content which needs to be added to a Curation with matching curationID
     * @param categoryID - ID of category for which content needs to be added
     * @param type - type of content which is being added
     * @return - Returns the associated CurationContent object
     */
    CurationContent addCurationItem(Long curationID, Long cacheID, Long categoryID, Integer type, Integer position) throws EntityExistsException;

    void updateItemPosition(Long contentID, Integer position);

    /**
     * Service to remove the CurationContent and Curation association
     * @param curationID - Curation ID
     * @param contentID - Content ID
     * @return - Returns the status if item(s) were removed successfully.
     */
    String removeCurationItem(Long curationID, String contentID);

    /**
     * Service to mark a CurationContent as favourite
     * @param contentID - The curated item(s) which needs to be marked as favorite. If more than
     *                  one item is passed, the request should look like contentID=11-12-13
     * @return String - Returns the status if item(s) were marked as Favorite successfully.
     */
    String markFavourite(String contentID);

    /**
     * Service to mark a CurationContent as favourite
     * @param contentID - The curated item(s) which needs to unfavorite. If more than
     *                  one item is passed, the request should look like contentID=11-12-13
     * @return String - Returns the status if item(s) were unmarked as Favorite successfully.
     */
    String undoFavourite(String contentID);

    /**
     * Mark Item(s) as Sticky
     * @param contentID - The curated item(s) which needs to be marked as sticky. If more than
     *                  one item is passed, the request should look like contentID=11-12-13
     * @return String - Returns the status if item(s) were marked as sticky successfully.
     */
    String markSticky(String contentID);

    /**
     * Un-mark Item(s) as Sticky
     * @param contentID - The curated item(s) which needs to be unmarked as sticky. If more than
     *                  one item is passed, the request should look like contentID=11-12-13
     * @return String - Returns the status if item(s) were marked as sticky successfully.
     */
    String undoSticky(String contentID);


    /**
     * Service method to fetch the curated contents related to a category( Curation Category )
     * @param categoryID - Curation category id for which the content is to be returned
     * @return - List of content objects
     */
    Collection<CurationContent> getCategoryContent(@ModelAttribute ServiceFilterBean filter, @RequestParam Long categoryID);

    /***************************** Curation Categories related services ************************************/

    /**
     * Service to create a Curation category
     * @param curationID -  category to which this category is associated
     * @param name - name of category
     * @param parentID - parent category ID (optional)
     * @param position - category position (optional)
     * @return - returns newly created category object
     */
    TaxonomyCategory createCategory(Long curationID, String name, Long parentID, Integer position);

    /**
     * Service to update the category object
     * @param categoryID - Category ID
     * @param name - updated name of category ( optional)
     * @param parentID - new parentID for category ( optional)
     * @param position - updated name of category ( optional)
     * @return -  returns updated category object
     */
    TaxonomyCategory updateCategory(Long categoryID, String name, Long parentID, Integer position);

    /**
     * Service to delete the category associated to a curation
     * @param curationID - ID of curation to which this category is associated
     * @param categoryID - category ID which has to be deleted
     */
    void deleteCategory(final Long curationID, final Long categoryID);

    /**
     * Service to fetch the category details
     * @param curationID - ID of curation to which this category is associated
     * @param categoryID - category ID
     * @return
     */
    TaxonomyCategory getCategory(final Long curationID, final Long categoryID);

    /**
     * Service to fetch the categories associated to a parent category
     * @param parentID - parent categoryID
     * @return - JSON object with the child categories information
     */
    JSONObject getSubCategories(Long parentID);

    /**
     * Service to return the JSON array of categories associated to a curation
     * @param curationID -
     * @param topLevel - true ( to fetch the ROOT level) & false ( fetch all ) categories
     * @param hiearchical - true to fetch the categories result in hiearchical manner
     * @return - returns JSONArray of categories
     */
    JSONArray getCategories(final Long curationID, final boolean topLevel, final boolean hiearchical);

    /**
     * Service to fetch the favourite content for a curation
     * @param curationID - ID of curation
     * @return list of content object which are marked as FAVOURITE
     */
    Collection<CurationContent> getCurationFavourites(@ModelAttribute ServiceFilterBean filter, Long curationID);

    /**
     * Service to fetch the favourite content for a curation and a category
     * @param curationID - ID of curation
     * @param categoryID - ID of category
     * @return list of content object which are marked as FAVOURITE
     */
    Collection<CurationContent> getCurationFavourites(@ModelAttribute ServiceFilterBean filter, Long curationID, Long categoryID);

    CurationCache getCacheContent(@RequestParam Long cacheID);

    CurationContent getContent(@RequestParam Long contentID);
}
