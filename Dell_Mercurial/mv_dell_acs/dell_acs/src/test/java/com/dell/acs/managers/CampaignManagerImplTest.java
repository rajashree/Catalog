/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.CampaignNotFoundException;
import com.dell.acs.DellTestCase;
import com.dell.acs.managers.model.CampaignItemData;
import com.dell.acs.persistence.domain.Campaign;
import com.dell.acs.persistence.domain.CampaignCategory;
import com.dell.acs.persistence.domain.CampaignItem;
import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.repository.CampaignRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: vivekk $
 * @version $Revision: 1182 $, $Date:: 2012-04-03 08:27:24#$
 */
public class CampaignManagerImplTest extends DellTestCase {

    protected static Logger logger = LoggerFactory.getLogger(CampaignManagerImplTest.class);
    private CampaignRepository repository = applicationContext.getBean("campaignRepositoryImpl", CampaignRepository.class);
    private CampaignManager manager = applicationContext.getBean("campaignManagerImpl", CampaignManager.class);

    @Test
    public void testAddCampaign() {
        try {
            if (manager != null) {
                Campaign campaign = new Campaign();
                campaign.setName("Test Campaign");
                campaign.setStartDate(new Date());
                campaign.setEndDate(new Date());
                campaign.setEnabled(true);
                CampaignItem item1 = new CampaignItem();
                item1.setId(1L);
                item1.setItemID(37L);
                item1.setPriority(0);
                //item1.setItemName("Vostro 1440");
                item1.setItemType(CampaignItem.Type.PRODUCT);

                CampaignItem item2 = new CampaignItem();
                item2.setId(2L);
                item2.setItemID(38L);
                item2.setPriority(0);
                //item2.setItemName("Vostro 3750");
                item2.setItemType(CampaignItem.Type.PRODUCT);

                Set<CampaignItem> items = new HashSet<CampaignItem>();
                items.add(item1);
                items.add(item2);
                campaign.setItems(items);
                manager.saveCampaign(campaign);
            }
        } catch (Exception ex) {
            logger.info("Error while creating the Campaign");
        }
    }

    @Test
    public void testUpdateCampaign() {
        if (manager != null) {
            try {
                Campaign campaign = manager.getCampaign(2L);
                if (campaign != null) {
                    campaign.setName("Test Campaign - Updated");
                    Date today = new Date();
                    campaign.setStartDate(today);

                    CampaignItem item1 = new CampaignItem();
                    item1.setItemID(37L);//30
                    item1.setPriority(0);
                    //item1.setItemName("Dell Latitude E5520");
                    item1.setItemType(CampaignItem.Type.PRODUCT);
                    item1.setCampaign(campaign);

                    CampaignItem item2 = new CampaignItem();
                    item2.setItemID(38L); // 31
                    item2.setPriority(0);
                    //item2.setItemName("Ships Fast Vostro 1440");
                    item2.setItemType(CampaignItem.Type.PRODUCT);
                    item2.setCampaign(campaign);

                    Set<CampaignItem> items = new HashSet<CampaignItem>();
                    items.add(item1);
                    items.add(item2);
                    campaign.setItems(items);
                    manager.saveCampaign(campaign);
                } else {
                    logger.debug("Campaign NOT FOUND");
                }
            } catch (Exception ex) {
                logger.info("Error while creating the Campaign");
            }
        }
    }

    @Test
    public void testGetAllCampaigns() {
        for (Campaign campaign : manager.getCampaigns()) {
            logger.debug(campaign.getName());
        }
    }

    @Test
    public void testGetCampaign() throws CampaignNotFoundException {


        Campaign c = manager.getCampaign(1L);
        Set<CampaignCategory> categories = c.getCategories();


        for (CampaignCategory cat : categories) {
            // logger.debug("   "+cat.getName());
            if (cat.getItems().size() == 0) {
                logger.debug(" This is a node, we can skip from feed generation ");
            } else {
                getCategoriesForFeed(cat);
            }
            logger.debug("   " + cat.toString());
        }


    }

    protected void getCategoriesForFeed(CampaignCategory cat) {
        logger.debug(" This has products ");
        String category = cat.getName();
        if (cat.getParent() != null) {
            logger.debug(" Has a parent & we need to recursively lookup....to construct the Category list with comma delimiter ");
            StringBuilder sb = new StringBuilder();
            category = constructCategories(cat, sb);
        }
        logger.debug("Category Names  :::  " + category);
    }

    private String constructCategories(CampaignCategory category, StringBuilder sb) {

        CampaignCategory parent = category.getParent();
        //now append the current node value and move to its parent;
        // logger.debug("category    "+sb.toString());

        if (parent.getParent() == null) {
            //logger.debug("There is no parent...."+category.toString());
            sb.append(category.getName());
        } else {
            //logger.debug("We have a parent, so continue up the order....");
            sb.append(category.getName());
            sb.append(",");
            constructCategories(parent, sb);
        }
        return sb.toString();


    }

    @Test
    public void testDeleteCampaign() {
        try {
            manager.deleteCampaign(1L);
        } catch (CampaignNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCampaignBySiteID() {
        List test = new ArrayList<Long>();
        test.add(3L);
        Collection<Campaign> campaigns = manager.getCampaignByRetailerSiteID(3L);
        logger.debug(String.valueOf(campaigns.size()));
    }

    @Test
    public void testProductsHashSet() {
        Set<Product> prods = new HashSet<Product>();
        Product p1 = new Product();
        p1.setId(1L);
        Product p2 = new Product();
        p2.setId(2L);
        Product p3 = new Product();
        p3.setId(3L);
        Product p4 = new Product();
        p4.setId(1L);
        Product p5 = new Product();
        p5.setId(2L);
        prods.add(p1);
        prods.add(p2);
        prods.add(p3);
        prods.add(p4);
        prods.add(p5);
        for (Product prod : prods) {
            logger.debug(String.valueOf(prod.getId()));
        }

    }

    @Test
    public void testSearchProducts() {
    }

    @Test
    public void getCampaignItems() {

        Collection<CampaignItemData> itemBeans = manager.getCampaignItems(1L);
        logger.debug(String.valueOf(itemBeans.size()));
    }


    public static void main(String[] args) {
        applicationContext = new ClassPathXmlApplicationContext(new String[]{"/spring/applicationContext.xml"});
    }
}
