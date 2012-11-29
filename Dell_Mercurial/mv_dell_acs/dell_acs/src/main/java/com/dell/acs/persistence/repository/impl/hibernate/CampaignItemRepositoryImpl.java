/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.managers.CampaignManager;
import com.dell.acs.persistence.domain.*;
import com.dell.acs.persistence.repository.*;
import com.sourcen.core.persistence.repository.impl.hibernate.PropertiesAwareRepositoryImpl;
import org.apache.commons.beanutils.BeanToPropertyValueTransformer;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Samee K.S
 * @author $LastChangedBy: sameeks $
 * @version $Revision: 3715 $, $Date:: 2012-06-28 09:23:55#$
 */
@Repository
public class CampaignItemRepositoryImpl extends PropertiesAwareRepositoryImpl<CampaignItem>
        implements CampaignItemRepository {

    public CampaignItemRepositoryImpl() {
        super(CampaignItem.class, CampaignItemProperty.class);
    }


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private CampaignCategoryRepository campaignCategoryRepository;

    @Override
    public void updateCampaignItemProperties(Long campaignID, Map<String, Map<Long, String>> parsedDataMap) {
        // dell.linkTracker.url.<lt_cid> = lturl
        // <lt_cid>, [ campaignItemID, lt_url ]
        //TODO: Not sure why we need map within a map. Samee or Navin explain?
        for (Map.Entry<String, Map<Long, String>> entry : parsedDataMap.entrySet()) {
            // [ campaignItemID, lt_url ]
            Long itemID = -1L;
            String lt_url = "";
            for (Map.Entry<Long, String> itemEntry : entry.getValue().entrySet()) {
                itemID = itemEntry.getKey();
                lt_url = itemEntry.getValue();
            }
            // item_id.lt_cid
            String lt_cid = entry.getKey();
            CampaignItem item = get(itemID);
            // Update the property for campaign item
            String campaignIDLTID_KEY = CampaignManager.CAMPAIGN_ITEM_LT_PROP.concat(lt_cid);
            item.getProperties().setProperty(campaignIDLTID_KEY, lt_url);
            update(item);
            logger.info(itemID + " Property : " + campaignIDLTID_KEY + " Value : " + lt_url);   // [ campaignItemID, lt_url ]
        }
    }

    @Override
    public Integer getMaxItemPriority(Long categoryID) {
        // SELECT id, itemID, priority FROM Campaign_Items where id in ( SELECT items_id FROM campaign_category_campaign_items where campaign_category_id = 74 );
        CampaignCategory category = campaignCategoryRepository.get(categoryID);
        // Fetch only ID from items collection
        BeanToPropertyValueTransformer transformer = new BeanToPropertyValueTransformer( "id" );
        // transform the Collection
        Collection<Long> categoryItemIds = CollectionUtils.collect(category.getItems(), transformer);

        Criteria criteria = getSession().createCriteria(CampaignItem.class);
        Integer priority = null;
        if(categoryItemIds.size() > 0){
            criteria.add(Restrictions.in("id", categoryItemIds));
            criteria.setProjection(Projections.max("priority"));
            priority = (Integer)criteria.uniqueResult();
        }
        return (priority != null) ? (priority + 1) : 0;
    }


    @Override
    public Integer adjustItemPriorityBasedOnPosition(Long categoryID, Long referenceItemID, String position){
        Integer referenceItemPriority = get(referenceItemID).getPriority();

        // UPDATE campaign_items SET priority = priority + 1 WHERE id in ( SELECT items_id FROM campaign_category_campaign_items where campaign_category_id = 74 ) and priority >= 6;

        StringBuffer hqlQuery = new StringBuffer();
        hqlQuery = hqlQuery.append("UPDATE CampaignItem SET priority = priority + 1 ");

        CampaignCategory category = campaignCategoryRepository.get(categoryID);

        if(category != null){

            if(category.getItems().isEmpty()){
                referenceItemPriority = 0;
            }else{
                // Fetch only ID from items collection
                BeanToPropertyValueTransformer transformer = new BeanToPropertyValueTransformer( "id" );
                // transform the Collection
                Collection<Long> categoryItemIds = CollectionUtils.collect(category.getItems(), transformer);
                if(categoryItemIds.size() > 0){
                    String items = org.springframework.util.StringUtils.collectionToDelimitedString(categoryItemIds, ",");
                    hqlQuery = hqlQuery.append(" WHERE id IN ( ").append( items ).append( ") ");
                }

                if(position.equalsIgnoreCase("before") || categoryItemIds.size() == 1){  // before >=
                    hqlQuery = hqlQuery.append(" AND priority >= ").append(referenceItemPriority);
                }else if(position.equalsIgnoreCase("after")){  // after >
                    if(categoryItemIds.size() > 1){
                        hqlQuery = hqlQuery.append(" AND " );
                    }
                    hqlQuery = hqlQuery.append(" priority > ").append(referenceItemPriority);
                }

//                if(categoryItemIds.size() > 1){
//                    hqlQuery = hqlQuery.append(" AND " );
//                }
//
//                hqlQuery = hqlQuery.append(" priority >= ").append(referenceItemPriority);

                Query query = getSession().createQuery(hqlQuery.toString());
                int rowUpdatesCounts = query.executeUpdate();
                if(rowUpdatesCounts > 0){
                    logger.info("Updated rows - " + rowUpdatesCounts);
                }
            }
        }
        return referenceItemPriority;
    }

    public Collection getCampaignItems(Long cid) {
        // Get all items from campaign_item and there respective links
        Criteria criteria = createCampaignItemCriteria();
        Projection itemID_Projection = Projections.property("itemID").as("item");
        Projection itemType_Projection = Projections.property("itemType").as("type");
        Projection id_Projection = Projections.property("id").as("id");
        criteria.setProjection(Projections.projectionList()
                .add(id_Projection)
                .add(itemID_Projection)
                .add(itemType_Projection)
        );
        criteria.add(Restrictions.eq("campaign.id", cid));
        return criteria.list();
    }

    /**
     * Helper method to creating criteria object for CampaignItem class
     *
     * @returns - Campaign criteria object
     */
    private Criteria createCampaignItemCriteria() {
        Criteria criteria = getSession().createCriteria(CampaignItem.class);
        return criteria;
    }

    @Override
    public List<CampaignItem> getItems(Long campaignID) {
        List<CampaignItem> items = new ArrayList<CampaignItem>();
        Criteria criteria = createCampaignItemCriteria();
        Projection id_Projection = Projections.property("id").as("id");
        criteria.setProjection(Projections.projectionList()
                .add(id_Projection)
        );
        criteria.add(Restrictions.eq("campaign.id", campaignID));
        Product product = null;
        Event event = null;
        Document document = null;
        for (Long id : (List<Long>) criteria.list()) {
            CampaignItem item = get(id);
            if (item.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.PRODUCT.getValue())) {
                product = productRepository.get(item.getItemID());
                if(product != null){
                    item.setProduct(product);
                    items.add(item);
                }else{
                    // removeItemAssociation(id, campaignID);
                    logger.info("Remove Product entity association for id - " + item.getId());
                }
            } else if (item.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.EVENT.getValue())) {
                event = eventRepository.get(item.getItemID());
                if(event != null){
                    item.setEvent(event);
                    items.add(item);
                }else{
                    // removeItemAssociation(id, campaignID);
                    logger.info("Remove Event entity association for id - " + item.getId());
                }
            } else if (item.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.DOCUMENT.getValue())) {
                document = documentRepository.get(item.getItemID());
                if(document != null) {
                    item.setDocument(document);
                    items.add(item);
                }else{
                    // removeItemAssociation(id, campaignID);
                    logger.info("Remove Document entity association for id - " + item.getId());
                }
            }else if (item.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.IMAGE.getValue())) {
                document = documentRepository.getDocument(item.getItemID(), EntityConstants.Entities.IMAGE.getId());
                if(document != null) {
                    item.setDocument(document);
                    items.add(item);
                }else{
                    // removeItemAssociation(id, campaignID);
                    logger.info("Remove IMAGE entity association for id - " + item.getId());
                }
            }else if (item.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.LINK.getValue())) {
                document = documentRepository.getDocument(item.getItemID(), EntityConstants.Entities.LINK.getId());
                if(document != null) {
                    item.setDocument(document);
                    items.add(item);
                }else{
                    // removeItemAssociation(id, campaignID);
                    logger.info("Remove LINK entity association for id - " + item.getId());
                }
            }else if (item.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.VIDEO.getValue())) {
                document = documentRepository.getDocument(item.getItemID(), EntityConstants.Entities.VIDEO.getId());
                if(document != null) {
                    item.setDocument(document);
                    items.add(item);
                }else{
                    // removeItemAssociation(id, campaignID);
                    logger.info("Remove VIDEO entity association for id - " + item.getId());
                }
            }
        }
        return items;
    }
}
