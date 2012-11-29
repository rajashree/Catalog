/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v2.rest;

import com.dell.acs.UserNotFoundException;
import com.dell.acs.content.EntityConstants;
import com.dell.acs.managers.*;
import com.dell.acs.persistence.domain.*;
import com.dell.acs.web.ws.v2.CurationService;
import com.sourcen.core.config.ConfigurationServiceImpl;
import com.sourcen.core.util.beans.ServiceFilterBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebService;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Samee K.S
 * @author : sameeks $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */

/**
 * Service class which implements all Curation related services.
 */
@WebService(name = "CurationService")
@RequestMapping("/api/v2/rest/CurationService")
public class CurationServiceImpl extends WebServiceImpl implements CurationService {

    private static List<Integer> availableDocTypes = new ArrayList<Integer>(Arrays.asList(2000, 2001, 2002, 2003, 2004));
    private static List<Integer> availableFeedTypes = new ArrayList<Integer>(Arrays.asList(100,101,102,103,200,201,202,300,400,401,402));



    @Autowired
    private CurationManager curationManager;

    @Autowired
    private CurationContentManager curationContentManager;


    @Autowired
    private CurationCacheManager curationCacheManager;

    @Autowired
    private TaxonomyManager taxonomyManager;

    @Autowired
    private DocumentManager documentManager;

    @Override
    @RequestMapping(value = "createCuration", method = RequestMethod.POST)
    public Curation createCuration(HttpServletRequest request,
                                   @RequestParam String name,
                                   @RequestParam String desc,
                                   @RequestParam Object site) {
        Assert.notNull(name, "Required parameter 'name' is missing.");
        Assert.notNull(desc, "Required parameter 'desc' is missing.");
        Assert.notNull(site, "Required parameter 'site' for retailer site is missing. Can be either id or name.");

        RetailerSite retailerSite = retailerManager.getRetailerSite(site);
        Assert.notNull(retailerSite, "Retailer Site '" + site.toString() + "' not found.");

        Curation curation = new Curation(name, desc, retailerSite, true);
        User user = getUser();
        if(user == null){
            logger.info("Unable to load the user from security context.");
            try {
                user = userManager.getUser(5L);
            } catch (UserNotFoundException e) {
                logger.info("Unable to find user");
            }
        }
        curation.setCreatedBy(user);
        curation.setModifiedBy(user);
        curation.setCreatedDate(new Date());
        curation.setModifiedDate(new Date());
        curation = curationManager.saveCuration(curation);
        return curation;
    }

    @Override
    @RequestMapping(value = "updateCuration", method = RequestMethod.POST)
    public Curation updateCuration(HttpServletRequest request,
                                   @RequestParam Long curationID,
                                   @RequestParam String name,
                                   @RequestParam String desc) {

        // Long userID = getUserFromRequest(request).getId();
        Assert.notNull(curationID, "Curation ID can't be null");

        // User user = userManager.getUser(userID);
        Curation curation = curationManager.getCuration(curationID);
        if(StringUtils.isNotEmpty(name)){
            curation.setName(name);
        }
        if(StringUtils.isNotEmpty(desc)){
            curation.setDescription(desc);
        }
        curation.setModifiedBy(getUser());
        curation.setModifiedDate(new Date());
        curationManager.saveCuration(curation);
        return curation;
    }

    @Override
    @RequestMapping(value = "getCuration", method = RequestMethod.GET)
    public Curation getCuration(@RequestParam Long curationID) {
        //Assert.notNull(curationID, "Required parameter 'curationID' is missing.");
        JSONObject response = new JSONObject();
        Curation curation = curationManager.getCuration(curationID);
        if(curation == null){
            throw new EntityNotFoundException("Curation with id '" + curationID + "' not found.");
        }
        return curation;
    }

    @Override
    @RequestMapping(value = "deleteCuration", method = RequestMethod.POST)
    public void deleteCuration(@RequestParam Long curationID) {
        curationManager.deleteCuration(curationID);
    }

    @Override
    @RequestMapping(value = "getRetailerSiteCurations", method = RequestMethod.GET)
    public Collection<Curation> getCurations(@ModelAttribute ServiceFilterBean filter, @RequestParam Object site) {
        RetailerSite retailerSite = retailerManager.getRetailerSite(site);
        Collection<Curation> curations = curationManager.getAllCurationByRetailerSite(retailerSite.getId());
        if(curations.size() == 0){
            logger.info("No curation found for the retailer site - " + site.toString());
            return Collections.emptyList();
        }
        return curations;
    }


    @Override
    @RequestMapping(value = "createCategory", method = RequestMethod.POST)
    public TaxonomyCategory createCategory(@RequestParam Long curationID,
                                     @RequestParam String name,
                                     @RequestParam(required = false) Long parentID,
                                     @RequestParam(required = false) Integer position) {
        Taxonomy taxonomy = taxonomyManager.getCurationTaxonomy(curationID);
        TaxonomyCategory category = taxonomyManager.createCategory(taxonomy, name, position, parentID);
        return category;
    }

    @Override
    @RequestMapping(value = "updateCategory", method = RequestMethod.POST)
    public TaxonomyCategory updateCategory(@RequestParam Long categoryID,
                                           @RequestParam(required = false) String name,
                                           @RequestParam(required = false) Long parentID,
                                           @RequestParam(required = false) Integer position) {
        Assert.notNull(categoryID, "curationID can't be NULL");
        Assert.notNull(name, "name can't be NULL");
        TaxonomyCategory category = taxonomyManager.getTaxonomyCategory(categoryID);
        if(StringUtils.isNotEmpty(name)){
            category.setName(name);
        }
        if(parentID != null && parentID > 0){
            category.setParent(taxonomyManager.getTaxonomyCategory(parentID));
        }
        if(position != null){
            category.setPosition(position);
        }
        category = taxonomyManager.updateCategory(categoryID, name, parentID, position);
        return category;
    }

    @Override
    @RequestMapping(value = "getSubCategories", method = RequestMethod.GET)
    public JSONObject getSubCategories(@RequestParam Long parentID) {
        JSONArray children = new JSONArray();
        TaxonomyCategory parent = taxonomyManager.getTaxonomyCategory(parentID);
        JSONObject response = convertToJSON(parent);
        Collection<TaxonomyCategory> categoryEntities = taxonomyManager.getCategories(parentID);
        for(TaxonomyCategory category : categoryEntities){
            children.add(convertToJSON(category));
        }
        response.accumulate("children", children);
        return response;
    }


    @Override
    @RequestMapping(value = "deleteCategory", method = RequestMethod.POST)
    public void deleteCategory(@RequestParam final Long curationID, @RequestParam final Long categoryID) {
        // Delete the category and its associated content
        taxonomyManager.deleteCurationCategory(curationID, categoryID);
    }

    @Override
    @RequestMapping(value = "getCategory", method = RequestMethod.GET)
    public TaxonomyCategory getCategory(@RequestParam final Long curationID, @RequestParam final Long categoryID)  {

        TaxonomyCategory category = taxonomyManager.getCurationCategory(curationID, categoryID);
        if(category == null ){
            throw new EntityNotFoundException("Category '" + categoryID + "' for curation '" + curationID + "' not found.");
        }
        return category;
    }

    @Override
    @RequestMapping(value = "getCategories", method = RequestMethod.GET)
    public JSONArray getCategories(@RequestParam final Long curationID,
                                   @RequestParam(required = false) final boolean onlyRoot,
                                   @RequestParam(required = false) final boolean hiearchical) {

        JSONArray categories = new JSONArray();
        Collection<TaxonomyCategory> categoryEntities = taxonomyManager.getCategories(curationID, onlyRoot);
        if(hiearchical == true && ! onlyRoot){
            categories = convertListDataToCategoryTreeJSON(categoryEntities, 0);
        }else{
            for(TaxonomyCategory category : categoryEntities){
                categories.add(convertToJSON(category));
            }
        }
        processedCategoriesList.clear();
        return categories;
    }

    @Override
    @RequestMapping(value = "getCacheContent", method = RequestMethod.GET)
    public CurationCache getCacheContent(@RequestParam Long cacheID) {
        CurationCache cache = curationCacheManager.getCache(cacheID);
        return cache;
    }

    @Override
    @RequestMapping(value = "getContent", method = RequestMethod.GET)
    public CurationContent getContent(@RequestParam Long contentID) {
        CurationContent content = curationContentManager.getContent(contentID);

        if (availableDocTypes.contains(content.getType())) {
            Document document = this.documentManager.getDocument(content.getCacheContent());
            logger.info(" Document Name :: " + document.getType());
            handleCDNPaths(document);

            content.setLibrary(document);
        } else if (availableFeedTypes.contains(content.getType())) {
            CurationCache cache = this.curationCacheManager.getCache(content.getCacheContent());
            logger.info(" Cache Name ::  "+cache.getBody());
            content.setFeed(cache);
        }
        return content;
    }

    /**
     * Helper method for creating CDN friendly URLs on the JSON response
     * @param document
     */
    protected void handleCDNPaths(Document document) {
        if (document.getType().intValue() == EntityConstants.Entities.DOCUMENT.getId()) {
            document.setDocument(getCDNURL(document.getDocument()));
            document.setImage(getCDNURL(document.getImage()));
        } else if (document.getType().intValue() == EntityConstants.Entities.IMAGE.getId()) {
            document.setUrl(getCDNURL(document.getUrl()));
            document.setImage(getCDNURL(document.getImage()));
        } else if (document.getType().intValue() == EntityConstants.Entities.VIDEO.getId()) {
            document.setImage(getCDNURL(document.getImage()));

        }
    }

    /**
     * Returns CDN base URL
     * @param contentURL
     * @return
     */
    private String getCDNURL(String contentURL) {
        String cdnPrefix = ConfigurationServiceImpl.getInstance().getProperty("filesystem.cdnPrefix", "");
        if(com.sourcen.core.util.StringUtils.isNotEmpty(contentURL)){
            return cdnPrefix.trim().concat(contentURL);
        }
        return contentURL;
    }



    @Override
    @RequestMapping(value = "getCategoryContent", method = RequestMethod.GET)
    public Collection<CurationContent> getCategoryContent(@ModelAttribute ServiceFilterBean filter,
                                                          @RequestParam Long categoryID) {
        // Content might be of type - CurationContent, Document( document, image, link, article etc..)
        // Query the - CurationContent table which is a mapping table to fetch all
        Collection<CurationContent> contents = curationContentManager.getContents(filter, "categoryID", categoryID);

        if (contents != null && contents.size() != 0) {
            Iterator<CurationContent> itr = contents.iterator();
            while (itr.hasNext()) {
                CurationContent content = itr.next();
                Long itemID = content.getCacheContent();
                if (availableDocTypes.contains(content.getType())) {


                    try {
                        Document document = this.documentManager.getDocument(itemID);
                        logger.info(" Document Name :: " + document.getName());
                        handleCDNPaths(document);
                        content.setLibrary(document);
                        //If the Curated Content is of CONTENT TYPE 'Document' i.e. IMAGE, ARTICLE, VIDEO, LINK, DOC then
                        //the CacheContent has to be null. Else the JSON response would have data on Cache Content.
                        content.setFeed(null);
                    } catch (EntityNotFoundException enFx) {
                        logger.warn("Curated Document content is missing." + enFx.getMessage());
                    }

                } else if (availableFeedTypes.contains(content.getType())) {
                       CurationCache cache = this.curationCacheManager.getCache(itemID);
                        logger.info(" Cache Name ::  "+cache.getBody());
                        content.setFeed(cache);
                }

            }


            return contents;
        }
        logger.info("No content found for category - " + categoryID);
        return Collections.emptyList();
    }

    /************************************** CONTENT RELATED SERVICES **************************************/


    @Override
    @RequestMapping(value = "addItem", method = RequestMethod.POST)
    public CurationContent addCurationItem(@RequestParam Long curationID,
                                           @RequestParam Long itemID,
                                           @RequestParam Long categoryID,
                                           @RequestParam Integer type,
                                           @RequestParam Integer position) throws EntityExistsException {
        return curationContentManager.addContent(curationID, itemID, categoryID, type,position, getUser());
    }

    @Override
    @RequestMapping(value = "updateItemPosition", method = RequestMethod.POST)
    public void updateItemPosition(@RequestParam Long contentID, @RequestParam Integer position) {
        Assert.isTrue(position > -1, "Position should be >= 0");
        Assert.isTrue(contentID > 0, "contentID value is invalid");
        curationContentManager.updateContentPosition(contentID, position);
    }

    @Override
    @RequestMapping(value = "removeItem", method = RequestMethod.POST)
    public String removeCurationItem(@RequestParam Long curationID, @RequestParam String contentID) {
        String status = "Successfully removed %d %s.";
        String[] ids = contentID.split("-");
        if (ids != null && ids.length > 0) {
            for (String id : ids) {
                curationContentManager.deleteContent(Long.valueOf(id));
            }
        }
        if(ids != null && ids.length == 1){
            status = String.format(status,1,"item");
        } else if(ids.length > 1){
            status = String.format(status,ids.length,"items");
        }
        return status;
    }

    @Override
    @RequestMapping(value = "markAsFavourite", method = RequestMethod.POST)
    public String markFavourite(@RequestParam String contentID) {
        String status = "Successfully marked %d %s as favorite.";
        String[] ids = contentID.split("-");
        if (ids != null && ids.length > 0) {
            for (String id : ids) {
            curationContentManager.updateFavouriteStatus(Long.valueOf(id), true);
            }
        }
        if(ids != null && ids.length == 1){
            status = String.format(status,1,"item");
        } else if(ids.length > 1){
            status = String.format(status,ids.length,"items");
        }
        return status;
    }

    @Override
    @RequestMapping(value = "undoFavourite", method = RequestMethod.POST)
    public String undoFavourite(@RequestParam String contentID) {
        String status = "Successfully marked %d %s as unfavorite.";
        String[] ids = contentID.split("-");
        if (ids != null && ids.length > 0) {
            for (String id : ids) {
            curationContentManager.updateFavouriteStatus(Long.valueOf(id), false);
            }
        }
        if(ids != null && ids.length == 1){
            status = String.format(status,1,"item");
        } else if(ids.length > 1){
            status = String.format(status,ids.length,"items");
        }
        return status;

    }

    @Override
    @RequestMapping(value = "markSticky", method = RequestMethod.POST)
    public String markSticky(String contentID) {
        String status = "Successfully marked %d %s as sticky.";
        String[] ids = contentID.split("-");
        if (ids != null && ids.length > 0) {
            for (String id : ids) {
                curationContentManager.updateStickyStatus(Long.valueOf(id), true);
            }
        }
        if(ids != null && ids.length == 1){
            status = String.format(status,1,"item");
        } else if(ids.length > 1){
            status = String.format(status,ids.length,"items");
        }
        return status;
    }

    @Override
    @RequestMapping(value = "undoSticky", method = RequestMethod.POST)
    public String undoSticky(String contentID) {
        String status = "Successfully marked %d %s as non-sticky.";
        String[] ids = contentID.split("-");
        if (ids != null && ids.length > 0) {
            for (String id : ids) {
                curationContentManager.updateStickyStatus(Long.valueOf(id), false);
            }
        }
        if(ids != null && ids.length == 1){
            status = String.format(status,1,"item");
        } else if(ids.length > 1){
            status = String.format(status,ids.length,"items");
        }
        return status;

    }

    @Override
    @RequestMapping(value = "getSourceContents", method = RequestMethod.GET)
    public Collection<CurationCache> getSourceContents(@ModelAttribute ServiceFilterBean filter,
                                                       @RequestParam Long sourceID){
        com.sourcen.core.util.Assert.notNull(sourceID, "curationSourceID Argument in getCurationCacheBySource is Null");
        return curationCacheManager.getCaches(filter, sourceID);
    }

    @Override
    @RequestMapping(value = "getFavourites", method = RequestMethod.GET)
    public Collection<CurationContent> getCurationFavourites(@ModelAttribute ServiceFilterBean filter,
                                                             @RequestParam Long curationID) {
        return null;
    }

    @Override
    @RequestMapping(value = "getCategoryFavourites", method = RequestMethod.GET)
    public Collection<CurationContent> getCurationFavourites(@ModelAttribute ServiceFilterBean filter,
                                                             @RequestParam Long curationID,
                                                             @RequestParam Long categoryID) {
        return null;
    }


    /****** HELPER METHODS ******/

    private Set<Long> processedCategoriesList = new HashSet<Long>();

    private JSONArray convertListDataToCategoryTreeJSON(Collection<TaxonomyCategory> children, int counter) {
        JSONArray dataArray = new JSONArray();
        Iterator itr = children.iterator();
        while (itr.hasNext()) {
            TaxonomyCategory childCategory = (TaxonomyCategory) itr.next();
            boolean hasChildren = (childCategory.getChildren().size() > 0);
            JSONObject childJSON = convertToJSON(childCategory);
            if (hasChildren) {
                if (!processedCategoriesList.contains(childCategory.getId())) {
                    processedCategoriesList.add(childCategory.getId());
                    // Add items to category
                    //logger.info(childCategory.getName() + " - Items - " + childCategory.getItems().size());
                    if (childCategory.getChildren().size() > 0) {
                        // Add items to the category
                        JSONArray itemsArray = new JSONArray();
                        // Sort the items based on the priority
                        LinkedList<TaxonomyCategory> sortedItems = new LinkedList<TaxonomyCategory>(childCategory.getChildren());
                        PropertyComparator.sort(sortedItems, new MutableSortDefinition("position", false, true));
                        for (TaxonomyCategory category : sortedItems) {
                            JSONObject categoryObj = convertToJSON(category);
                            if (categoryObj != null) {
                                itemsArray.add(categoryObj);
                            }
                        }
                    }
                    // Sort by position
                    LinkedList orderedChildren = new LinkedList(childCategory.getChildren());
                    Collections.sort(orderedChildren, new Comparator<TaxonomyCategory>() {
                        @Override
                        public int compare(TaxonomyCategory o1, TaxonomyCategory o2) {
                            int compareResult = 0;
                            // Sort the children by position
                            int position1 = (o1.getPosition() != null) ? o1.getPosition() : 0;
                            int position2 = (o2.getPosition() != null) ? o2.getPosition() : 0;

                            if (position1 < position2) {
                                return -1;
                            } else if (position2 < position1) {
                                return 1;
                            } else {
                                return 0;
                            }

                        }
                    });
                    dataArray.add(childJSON.accumulate("children", convertListDataToCategoryTreeJSON(orderedChildren, counter)));
                }
            } else {
                if (!processedCategoriesList.contains(childCategory.getId())) {
                    processedCategoriesList.add(childCategory.getId());
                    // Add items to category
                    if (childCategory.getChildren().size() > 0) {
                        JSONArray itemsArray = new JSONArray();
                        // Sort the items based on the priority
                        LinkedList<TaxonomyCategory> sortedItems = new LinkedList<TaxonomyCategory>(childCategory.getChildren());
                        PropertyComparator.sort(sortedItems, new MutableSortDefinition("position", false, true));
                        for (TaxonomyCategory category : sortedItems) {
                            JSONObject itemJSONObject = convertToJSON(category);
                            if(itemJSONObject != null){
                                itemsArray.add(itemJSONObject);
                            }else{
                                logger.debug("Item is NOT included in the category. Reference entity is NULL.");
                                continue;
                            }
                        }
                        if(itemsArray.size() > 0){
                            childJSON.accumulate("children", itemsArray);
                        }
                    }
                    dataArray.add(childJSON);
                    continue;
                }
            }
        }
        // logger.info(dataArray.toString());
        return dataArray;
    }

    private JSONObject convertToJSON(TaxonomyCategory category){
        JSONObject categoryJSON = new JSONObject();
        categoryJSON.accumulate("id", category.getId());
        categoryJSON.accumulate("name", category.getName());
//        if(category.getParent() != null){
//            //categoryJSON.accumulate("parentID", category.getParent().getId());
//        }
        return categoryJSON;
    }
}
