/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.controller.admin;

import com.dell.acs.CampaignCategoryNotFoundException;
import com.dell.acs.CampaignItemNotFoundException;
import com.dell.acs.CampaignNotFoundException;
import com.dell.acs.managers.CampaignManager;
import com.dell.acs.managers.DocumentManager;
import com.dell.acs.managers.RetailerManager;
import com.dell.acs.persistence.domain.*;
import com.dell.acs.persistence.repository.ProductRepository;
import com.dell.acs.web.controller.BaseDellController;
import com.sourcen.core.util.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;

/**
 * @author Samee K.S
 * @author $LastChangedBy: sameeks $
 * @version $Revision: 3715 $, $Date:: 2012-06-28 09:23:55#$
 */

@SuppressWarnings("all")
@RequestMapping(value = "admin/campaign/category")
@Controller
public final class CampaignCategoryController extends BaseDellController {

    private Set<Long> processedCategoriesList = new HashSet<Long>();

    @Autowired
    private CampaignManager campaignManager;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DocumentManager documentManager;

    @Autowired
    private RetailerManager retailerManager;

    @RequestMapping(value = "save-category-tree.json", method = RequestMethod.POST)
    public ModelAndView saveCategoryTreeData(@RequestParam(value = "treeData", required = true) final String treeData,
                                             @RequestParam(value = "campaignID", required = false) final Long campaignID)
            throws CampaignNotFoundException {

        Campaign campaign = campaignManager.getCampaign(campaignID, false);
        campaign.getProperties().setProperty(CampaignManager.CAMPAIGN_CATEGORY_TREE_JSON_DATA, treeData);
        campaignManager.updateCampaign(campaign);

        return new ModelAndView();
    }

    @RequestMapping(value = "category-actions.json", method = RequestMethod.POST)
    public ModelAndView itemCategoryHandler(
            @RequestParam(value = "name", required = false) final String name,
            @RequestParam(value = "id", required = false) final Long id,
            @RequestParam(value = "position", required = false) final Integer position,
            @RequestParam(value = "parentID", required = false) final Long parentID,
            @RequestParam(value = "oldParentID", required = false) final Long oldParentID,
            @RequestParam(value = "thumbnail", required = false) final InputStream thumbnail,

            @RequestParam(value = "moved", required = false) final String moved,
            @RequestParam(value = "referenceNodeID", required = false) final Long referenceNodeID,
            @RequestParam(value = "itemID", required = false) final Long itemID,
            @RequestParam(value = "campaignID", required = false) final Long campaignID,
            @RequestParam(value = "action", required = false) final String action)
            throws CampaignCategoryNotFoundException, CampaignItemNotFoundException, DataIntegrityViolationException {
        ModelAndView mv = new ModelAndView();
        CampaignCategory category = null;
        Map dataMap = new HashMap();
        try {
            if (action.equalsIgnoreCase("create")) {
                // Create category
                category = createCategory(name, parentID, position, campaignID);
                dataMap.put("id", category.getId());
                dataMap.put("parentID", parentID);
            } else if (action.equalsIgnoreCase("remove")) {
                deleteCategory(id, parentID);
            } else if (action.equalsIgnoreCase("rename")) {
                // id - CategoryID
                category = campaignManager.getCategory(id);
                category.setName(name);
                campaignManager.updateCampaignCategory(category);
                logger.debug("Category " + id + " name  " + name + " was updated successfully.");
            } else if (action.equalsIgnoreCase("move")) {
                // Moving categories to a diff category
                Long categoryID = itemID; // Source Category which is being moved under a new parent category
                Long targetCategoryID = id; // Target Category
                category = moveCategory(targetCategoryID, categoryID, campaignID);
            }
            //Item re-ordering - Related changes
            else if (action.equalsIgnoreCase("moveItem")) {
                Long categoryID = parentID;
                moveItem(id, referenceNodeID, categoryID, moved, campaignID);
            } else if (action.equalsIgnoreCase("addItem")) {
                Long srcCategoryID = oldParentID;
                Long targetCategoryID = parentID;

                addItemToCategory(itemID, srcCategoryID, targetCategoryID, moved, referenceNodeID);
            }
        } catch (Exception ex) {
            logger.info("Error while process the request - ", ex);
            if (ex instanceof DataIntegrityViolationException) {
                logger.info("Constraint violation");
                throw new RuntimeException("This item is already associated with a category");
            }else{
                logger.info("Failed to perform the action - " + action, ex);
                throw new RuntimeException(ex.getMessage());
            }

        }
        return mv.addObject("data", dataMap);
    }


    /**
     * Helper method used while ordering the items within the category
     *
     * @throws CampaignCategoryNotFoundException
     *
     */
    private void moveItem(Long itemID, Long referenceItemID, Long categoryID, String moved, Long campaignID)
            throws CampaignCategoryNotFoundException, CampaignItemNotFoundException {
        CampaignItem item = campaignManager.getCampaignItem(itemID, false);
        Integer priority = campaignManager.adjustItemPriorityBasedOnPosition(categoryID, referenceItemID, moved);
        item.setPriority(priority);
        campaignManager.updateCampaignItem(item);
    }

    /**
     * Helper method to create a Campaign Category
     * @throws CampaignCategoryNotFoundException
     */
    private CampaignCategory createCategory(String name, Long parentID, Integer position, Long campaignID)
            throws CampaignCategoryNotFoundException {
        CampaignCategory category = new CampaignCategory();
        category.setName(name);

        if (parentID > 0) {
            // Max allowed depth for a category restriction should only be applied to the
            // sub-categories and NOT for the ROOT level categories
            int maxAllowedDepth = configurationService.getIntegerProperty(CampaignManager.CAMPAIGN_CATEGORY_MAX_DEPTH, 6);
            // We have to verify with the depth and NOT with the position
            category.setParent(campaignManager.getCategory(parentID));
            int depth = getCategoryDepth(category.getParent());
            if (depth >= maxAllowedDepth) {
                throw new RuntimeException("Max depth for category allowed is - " + maxAllowedDepth);
            }
        }
        category.setPosition(position);
        if (campaignID != null && campaignID > 0) {
            try {
                category.setCampaign(campaignManager.getCampaign(campaignID, false));
            } catch (CampaignNotFoundException e) {
                logger.error("Campaign not found.", e);
            }
        }
        campaignManager.saveCategory(category);

        if (category.getParent() == null) {
            logger.debug("Created child: " + category.getName() + " under ROOT category");
        } else {
            logger.debug("Created child: " + category.getName() + " under parent category - " + category.getParent().getName());
        }
        return category;
    }

    /**
     * Helper method to delete a CampaignCategory OR an item associated with a category
     *
     * @throws CampaignItemNotFoundException
     * @throws CampaignCategoryNotFoundException
     *                                       if(parentID == null) then  id = categoryID [ category remove ]
     *                                       if(parentID != null) then id = campaignItemID [ removal of category item ]
     */
    private void deleteCategory(Long id, Long parentID) throws CampaignItemNotFoundException,
            CampaignCategoryNotFoundException {
        if (parentID != null) {
            CampaignItem campaignItem = campaignManager.getCampaignItem(id, false);
            if (campaignItem != null) {
                logger.info("Remove the item from category - " + parentID);
                CampaignCategory parentCategory = campaignManager.getCategory(parentID);
                Collection<CampaignItem> parentCategoryItems = parentCategory.getItems();
                parentCategoryItems.remove(campaignItem);
                parentCategory.setItems(parentCategoryItems);
                campaignManager.saveCategory(parentCategory);
            } else {
                logger.info("Unable to load the campaignItem");
            }
        } else {
            campaignManager.deleteCategory(id);
            logger.debug("Category deleted  - " + id);
        }
    }

    /**
     * Helper method to re-order or arrange the categories in a particular order.
     *
     * @param targetParentID - Target categoryID
     * @param categoryID     - Moving category categoryID
     * @param campaignID     - Target category CampaignID
     * @return
     * @throws CampaignCategoryNotFoundException
     *
     */
    private CampaignCategory moveCategory(Long targetParentID, Long categoryID, Long campaignID)
            throws CampaignCategoryNotFoundException {
        // Moving categories to a diff category
        // targetParentID - Target category - new parentID
        // categoryID - ID of category which is being dragged under X Category
        CampaignCategory category = campaignManager.getCategory(categoryID);
        if (targetParentID > 0) {
            CampaignCategory parent = campaignManager.getCategory(targetParentID);
            // Check for MAX_DEPTH_ALLOWED
            Integer parentCurrentDepth = getCategoryDepth(parent);
            Integer srcCategoryDepth = getCategoryDepth(category);
            Integer maxAllowedDepth = configurationService.getIntegerProperty(
                    CampaignManager.CAMPAIGN_CATEGORY_MAX_DEPTH, 6);
            // After adding category the depth should still be less than MAX_ALLOWED_DEPTH
            if ((parentCurrentDepth + srcCategoryDepth) > maxAllowedDepth) {
                logger.info("This action exceeds the MAX_DEPTH allowed");
                throw new RuntimeException("Exceeds the maximum allowed depth for a category");
            }
            category.setParent(parent);
            // Update the position as well
            // Set the position as current (MAX_POSITION + 1 ) for items with the parentID id
            category.setPosition(campaignManager.getMaxCategoryPosition(targetParentID, campaignID));
        }
        campaignManager.saveCategory(category);
        logger.debug("Update the parent category. Child: " + category.getName() + " Parent : " + category.getParent().getName());
        return category;
    }

    private Integer getCategoryDepth(CampaignCategory parent) {
        int depth = 1;
        while (parent.getParent() != null) {
            depth++;
            parent = parent.getParent();
        }

        return depth;
    }

    /**
     * Helper method to move the items under a category
     *
     * @param itemID           - Item ( product, videos or white-paper) ID
     * @param srcCategoryID    - Parent category from which the item is being dragged from
     * @param targetCategoryID - New parent category to which the item is being dropped
     * @return
     * @throws CampaignItemNotFoundException
     * @throws CampaignCategoryNotFoundException
     *
     */
    private CampaignCategory addItemToCategory(Long itemID, Long srcCategoryID, Long targetCategoryID, String moved,
                                               Long referenceItemID)
            throws CampaignItemNotFoundException, CampaignNotFoundException, CampaignCategoryNotFoundException {
        // itemID - Item id which is dropped to the category
        // srcCategoryID - dragNodeParentID
        // targetCategoryID - id - dropNodeParentID
        CampaignItem campaignItem = campaignManager.getCampaignItem(itemID);
        CampaignCategory category = null;
        if (campaignItem != null) {
            // dragNodeParentID ( srcCategoryID ) - parameter is sent only when the drag of items is in between the categories tree
            // If dragNodeParentID value is NOT null the remove the item from srcCategoryID
            if (srcCategoryID != null) {
                CampaignCategory sourceCategory = campaignManager.getCategory(srcCategoryID);
                logger.info("Removing the item "+ campaignItem.getItemID() + " from category '" + sourceCategory.getName());
                // remove the item from source category and update the source category
                // and then add the item to new category and update
                // RESET THE SOURCE CATEGORY ITEMS
                Collection<CampaignItem> srcItems = sourceCategory.getItems();
                srcItems.remove(campaignItem);
                sourceCategory.setItems(srcItems);
                //campaignManager.saveCategory(sourceCategory);
                updateCategory(sourceCategory);
            }
            // UPDATE THE DESTINATION CATEGORY ITEMS
            category = campaignManager.getCategory(targetCategoryID);
            int priority = 0;


            if (category != null) {
                logger.info("Adding item " + itemID +" to category '" + category.getName() + "'");
                Collection<CampaignItem> items = new HashSet<CampaignItem>();
                items = category.getItems();
                BeanPropertyValueEqualsPredicate predicate = new BeanPropertyValueEqualsPredicate("itemID", itemID);
                Object obj = CollectionUtils.find(items, predicate);
                if (obj == null) {
                    // Update the item priority
                    if (moved.equalsIgnoreCase("last")) {
                        priority = campaignManager.getMaxItemPriority(targetCategoryID);
                    } else if (moved.equalsIgnoreCase("before") || moved.equalsIgnoreCase("after")) {
                        // Updates the priority of other items and returns the priority value for the new item
                        priority = campaignManager.adjustItemPriorityBasedOnPosition(targetCategoryID, referenceItemID, moved);
                    }
                    campaignItem.setPriority(priority);
                    items.add(campaignItem);
                } else {
                    logger.info("Do an update !!!!");
                }
                campaignManager.saveCategory(category);


            } else {
                logger.info("Unable to load the category");
            }
        }
        return category;
    }

    private void updateCategory(CampaignCategory category) {
        campaignManager.saveCategory(category);
    }

    /**
     * Method to fetch the
     *
     * @param campaignID
     * @return
     * @throws com.dell.acs.CampaignNotFoundException
     *
     */
    @RequestMapping(value = "campaign-categories.json", method = RequestMethod.GET)
    public ResponseEntity<String> getCampaignCategories(@RequestParam Long campaignID) throws CampaignNotFoundException, CampaignItemNotFoundException {
        Campaign campaign = campaignManager.getCampaign(campaignID);
        String catJSON = "";
//        if(campaign != null && !campaign.getProperties().hasProperty(CampaignManager.CAMPAIGN_CATEGORY_TREE_JSON_DATA)){
//            logger.info("Category JSON property exists. So loading the data from the property.");
//            catJSON = campaign.getProperties().getProperty(CampaignManager.CAMPAIGN_CATEGORY_TREE_JSON_DATA);
//        }else{
//        JSONArray.fromObject(campaign.getProperties().getProperty(CampaignManager.CAMPAIGN_CATEGORY_TREE_JSON_DATA).toString());
//        logger.info("Category JSON property DOESN'T exists. So loading the data from DB.");

        List<CampaignCategory> list = new ArrayList<CampaignCategory>(campaign.getCategories());

        /*
            UPDATE campaign_items SET priority = priority + 1 WHERE id in ( SELECT items_id FROM campaign_category_campaign_items where campaign_category_id = 78 ) and priority >= 6;
            SELECT id, itemID, priority FROM Campaign_Items where id in ( SELECT items_id FROM campaign_category_campaign_items where campaign_category_id = 78 ) order by priority;
            SELECT * FROM campaign_category c;
         */
        // Sort based on the parentID and position to fetch the parents first and its order of creation.
        Collections.sort(list, new Comparator<CampaignCategory>() {
            @Override
            public int compare(CampaignCategory o1, CampaignCategory o2) {
                int compareResult;
                Long parentID1 = (o1.getParent() != null) ? o1.getParent().getId() : -1L;
                Long parentID2 = (o2.getParent() != null) ? o2.getParent().getId() : -1L;

                if (parentID1 > parentID2)
                    compareResult = 1;
                else if (parentID1 < parentID2)
                    compareResult = -1;
                else
                    compareResult = 0;

                // Apply the position sorting only for root parent children nodes
                if (parentID1 == parentID2) {
                    Long position1 = (o1.getPosition() != null) ? o1.getPosition() : -1L;
                    Long position2 = (o2.getPosition() != null) ? o2.getPosition() : -1L;

                    if (position1 < position2) {
                        compareResult = -1;
                    } else if (position2 < position1) {
                        compareResult = 1;
                    }
                }

                return compareResult;
            }
        });
        processedCategoriesList.clear();
        JSONArray categoryTreeJSON = convertListDataToCategoryTreeJSON(list, 0);
        catJSON = categoryTreeJSON.toString();
        // }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(catJSON, responseHeaders, HttpStatus.OK);
    }

    @Override
    @ExceptionHandler(Exception.class)
    public
    @ResponseBody
    String handleException(Exception e, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        String message = "Invalid action";
        if (e instanceof TypeMismatchException) {
            return message;
        } else if (e.getMessage().contains("com.dell.acs")) {
            return message;
        }
        return e.getMessage();
    }


    /**
     * Helper method to convert the Category object to JSON object with a tree structure
     *
     * @param children
     * @param counter
     * @return
     */
    private JSONArray convertListDataToCategoryTreeJSON(Collection<CampaignCategory> children, int counter) throws CampaignItemNotFoundException {
        JSONArray dataArray = new JSONArray();
        Iterator itr = children.iterator();
        while (itr.hasNext()) {
            CampaignCategory childCategory = (CampaignCategory) itr.next();
            boolean hasChildren = (childCategory.getChildren().size() > 0);
            JSONObject childJSON = convertCategoryToJSON(childCategory);
            if (hasChildren) {
                if (!processedCategoriesList.contains(childCategory.getId())) {
                    processedCategoriesList.add(childCategory.getId());
                    // Add items to category
                    //logger.info(childCategory.getName() + " - Items - " + childCategory.getItems().size());
                    if (childCategory.getItems().size() > 0) {
                        // Add items to the category
                        JSONArray itemsArray = new JSONArray();
                        // Sort the items based on the priority
                        LinkedList<CampaignItem> sortedItems = new LinkedList<CampaignItem>(childCategory.getItems());
                        PropertyComparator.sort(sortedItems, new MutableSortDefinition("priority", false, true));
                        for (CampaignItem campaignItem : sortedItems) {
                            JSONObject citemObj = convertCampaignItemToJSON(campaignItem, childCategory.getId());
                            if (citemObj != null) {
                                itemsArray.add(citemObj);
                            }
                        }
                        if(itemsArray.size() > 0){
                            childJSON.accumulate("children", itemsArray);
                        }
                    }
                    // Sort by position
                    LinkedList orderedChildren = new LinkedList(childCategory.getChildren());
                    Collections.sort(orderedChildren, new Comparator<CampaignCategory>() {
                        @Override
                        public int compare(CampaignCategory o1, CampaignCategory o2) {
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
                    //logger.info(childCategory.getName() + " - Items - " + childCategory.getItems().size());
                    // Add items to category
                    if (childCategory.getItems().size() > 0) {
                        JSONArray itemsArray = new JSONArray();
                        // Sort the items based on the priority
                        LinkedList<CampaignItem> sortedItems = new LinkedList<CampaignItem>(childCategory.getItems());
                        PropertyComparator.sort(sortedItems, new MutableSortDefinition("priority", false, true));
                        for (CampaignItem campaignItem : sortedItems) {
                            JSONObject itemJSONObject = convertCampaignItemToJSON(campaignItem, childCategory.getId());
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


    private JSONObject convertCampaignItemToJSON(CampaignItem campaignItem, Long parentID) {
        JSONObject jsonItemObj = new JSONObject();
        JSONObject dataNode = new JSONObject();
        // Truncate the item title with 100 characters
        String title = "";
        String htmlTitle = "";
        JSONObject nodeAttributes = new JSONObject();
        if (campaignItem.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.PRODUCT.name())) {
            Product prod = campaignItem.getProduct();
            if(prod != null){
                title = prod.getTitle();
                htmlTitle = title.concat("  (" + prod.getId() + ") ");
                if(prod.getPrice() != null){
                    htmlTitle = htmlTitle.concat("<b>$").concat(String.valueOf(prod.getPrice()).concat("</b>"));
                }
                nodeAttributes.accumulate("nodeType", "product");
                nodeAttributes.accumulate("parentType", "category");
                nodeAttributes.accumulate("rel", "product");
                nodeAttributes.accumulate("position", campaignItem.getPriority());
            }else{
                logger.debug("Product NOT FOUND. ID : " + campaignItem.getItemID());
                return null;
            }
        } else if (campaignItem.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.EVENT.name())) {
            Event event = campaignItem.getEvent();
            if(event != null){
                title = event.getName();
                htmlTitle = title.concat("  (" + event.getId() + ") ");
                if(StringUtils.isNotEmpty(event.getLocation())){
                    htmlTitle = htmlTitle.concat("<b>").concat(String.valueOf(event.getLocation()).concat("</b>"));
                }
                nodeAttributes.accumulate("nodeType", "event");
                nodeAttributes.accumulate("parentType", "category");
                nodeAttributes.accumulate("rel", "event");
                nodeAttributes.accumulate("position", campaignItem.getPriority());
            }else{
                logger.debug("Event NOT FOUND. ID : " + campaignItem.getItemID());
                return null;
            }
        } else if ( campaignItem.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.DOCUMENT.name()) ||
                    campaignItem.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.IMAGE.name()) ||
                    campaignItem.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.VIDEO.name()) ||
                    campaignItem.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.LINK.name()) ) {
            Document document = campaignItem.getDocument();

            if(document == null){
                document = documentManager.getDocument(campaignItem.getItemID(), campaignItem.getItemType().getEntityID());
            }
            String type = campaignItem.getItemType().getValue();
            if(document != null){
                title = document.getName();
                htmlTitle = title.concat("  (" + document.getId() + ") ");
                // nodeAttributes.accumulate("nodeType", "document");
                // nodeAttributes.accumulate("rel", "document");
                nodeAttributes.accumulate("nodeType", type.toLowerCase());
                nodeAttributes.accumulate("rel", type.toLowerCase());
                nodeAttributes.accumulate("parentType", "category");
                nodeAttributes.accumulate("position", campaignItem.getPriority());
            }else{
                logger.debug("Document ( "+ type + " ) NOT FOUND. ID : " + campaignItem.getItemID());
                return null;
            }
        }

        if (StringUtils.isNotEmpty(title)) {
            dataNode.accumulate("title", StringUtils.abbreviate(htmlTitle, 100));
            jsonItemObj.accumulate("data", dataNode);
            // Hover title
            nodeAttributes.accumulate("title", title);
            nodeAttributes.accumulate("class", "leaf_node");
            nodeAttributes.accumulate("parentID", parentID);
            nodeAttributes.accumulate("id", campaignItem.getId());
            jsonItemObj.accumulate("attr", nodeAttributes);
            return jsonItemObj;
        }
        return null;

    }

    private JSONObject convertCategoryToJSON(CampaignCategory category) {
        JSONObject categoryJSON = new JSONObject();
        JSONObject dataNode = new JSONObject();
        String count = (category.getItems().size() > 0) ? (" [ ").concat(category.getItems().size() + "").concat(" ]") : "";
        // Truncate the Category name with 100 characters
        dataNode.accumulate("title", StringUtils.abbreviate(category.getName(), 100));
        categoryJSON.accumulate("data", dataNode);

        JSONObject nodeAttributes = new JSONObject();
        nodeAttributes.accumulate("nodeType", "category");
        nodeAttributes.accumulate("position", category.getPosition());
        nodeAttributes.accumulate("title", category.getName());
        nodeAttributes.accumulate("rel", "container");
        nodeAttributes.accumulate("id", category.getId().toString());
        nodeAttributes.accumulate("parentID", (category.getParent() != null) ? category.getParent().getId().toString() : -1L);
        categoryJSON.accumulate("attr", nodeAttributes);
        return categoryJSON;
    }
}
