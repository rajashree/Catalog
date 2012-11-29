/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.CampaignForInactiveRetailerException;
import com.dell.acs.CampaignItemNotFoundException;
import com.dell.acs.CampaignNotFoundException;
import com.dell.acs.managers.ContentFilterBean;
import com.dell.acs.persistence.domain.Campaign;
import com.dell.acs.persistence.domain.CampaignCategory;
import com.dell.acs.persistence.domain.CampaignItem;
import com.dell.acs.persistence.domain.CampaignProperty;
import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.repository.CampaignItemRepository;
import com.dell.acs.persistence.repository.CampaignRepository;
import com.dell.acs.persistence.repository.DocumentRepository;
import com.dell.acs.persistence.repository.EventRepository;
import com.dell.acs.persistence.repository.ProductImageRepository;
import com.dell.acs.persistence.repository.ProductRepository;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.PropertiesAwareRepositoryImpl;
import com.sourcen.core.util.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 @author Samee K.S
 @author $LastChangedBy: ashish $
 @version $Revision: 3769 $, $Date:: 2012-07-02 15:44:51#$ */
@Repository
public class CampaignRepositoryImpl extends PropertiesAwareRepositoryImpl<Campaign>
        implements CampaignRepository {

    /**
     Logger instance for CampaignRepositoryImpl.java class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(CampaignRepositoryImpl.class);

    //IOC
    @Autowired
    private RetailerSiteRepository retailerSiteRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private CampaignItemRepository campaignItemRepository;

    public CampaignRepositoryImpl() {
        super(Campaign.class, CampaignProperty.class);
    }

    @Autowired
    private ProductImageRepository productImageRepository;

    /**
     {@inheritDoc}
     */
    @Override
    public Campaign updateProperty(Long campaignId, String name, String value) throws CampaignItemNotFoundException {
        Campaign item = get(campaignId);
        if (item == null) {
            throw new CampaignItemNotFoundException("Campaign item not found.");
        } else {
            item.getProperties().setProperty(name, value);
            update(item);
        }
        return item;
    }

    /**
     @inheritdoc
     */
    @Override
    public Campaign getCampaign(Long id) throws CampaignNotFoundException {
        Campaign campaign = get(id);
        if (campaign != null) {
            for (CampaignCategory category : campaign.getCategories(true)) {
                for (CampaignItem item : category.getItems()) {
                    // To explicitly load the properties
                    item = campaignItemRepository.get(item.getId());
                    if (item.getItemType().getValue() == CampaignItem.Type.PRODUCT.getValue()) {
                        Product prod = null;
                        if (item.getProduct() == null) {
                            prod = productRepository.get(item.getItemID());
                        }
                        // prod.setImages(productImageRepository.getProductImages(prod));
                        //prod.setSliders(productSliderRepository.getProductSlidersBySourceProduct(prod));
                        //prod.setProductReviews(productReviewRepository.getProductReviews(prod));
                        item.setProduct(prod);
                        campaign.getProducts().add(prod);
                    } else if (item.getItemType().getValue() == CampaignItem.Type.EVENT.getValue()) {
                        item.setEvent(eventRepository.get(item.getItemID()));
                    } else if (item.getItemType().getValue() == CampaignItem.Type.DOCUMENT.getValue()) {
                        item.setDocument(documentRepository.get(item.getItemID()));
                    } else if (item.getItemType().getValue() == CampaignItem.Type.VIDEO.getValue()) {
                        // NOT SUPPORTED
                    }
                }
            }
        } else {
            LOG.info("Unable to load the campaign id: " + id);
            throw new CampaignNotFoundException("Campaign not found");
        }
        return campaign;
        //return null;
    }

    @Override
    public boolean checkCampaignForDoc(final Long itemID) {
        Object obj =
                getSession().createCriteria(CampaignItem.class).add(Restrictions.eq("itemID", itemID));
        if (obj != null) {
            return true;
        }
        return false;
    }

    /**
     @inheritdoc
     */
    @Override
    public List<CampaignItem> getDocumentAssociatedCampaigns(final Long documentID) {

        Criteria criteria = getSession().createCriteria(CampaignItem.class, "ci");
        criteria.add(Restrictions.eq("ci.itemID", documentID));
        List<CampaignItem> campaignItemList=new ArrayList<CampaignItem>();
        campaignItemList = criteria.list();
        if(campaignItemList.size() > 0){
            logger.info("Associated campaign list FOUND.");
            return campaignItemList;
        }else{
            logger.debug("No any campaign associated with document");
        }
        return campaignItemList;
    }


    /**
     @inheritdoc
     */
    @Override
    public List<Campaign> getCampaigns() {
        Campaign ex = getCampaignExampleObject();
        List<Campaign> campaigns = getByExample(ex);
        return campaigns;
    }

    /**
     @inheritdoc
     */
    @Override
    public List<CampaignItem> searchItems(String searchTerm) {
        ContentFilterBean contentFilterBean = new ContentFilterBean();
        List<CampaignItem> items = new LinkedList<CampaignItem>();
        if (StringUtils.isNumeric(searchTerm)) {
            LOG.info("Search for products with ID - " + searchTerm);
            //set ProductId
            //contentFilterBean.setProductIDs(searchTerm);
        } else {
            LOG.info("Search for products with Name  - " + searchTerm);
            //set Product Title
            //contentFilterBean.setSearchTerms(searchTerm);
        }
        contentFilterBean.setMaxProducts(30);
        contentFilterBean.setSearchTerms(searchTerm);
        Collection<Product> products = productRepository.getActiveProducts(contentFilterBean);

        for (Product product : products) {
            CampaignItem campaignItem = new CampaignItem();
            campaignItem.setItemID(product.getId());
            campaignItem.setProduct(product);
            campaignItem.setItemType(CampaignItem.Type.PRODUCT);
            items.add(campaignItem);
        }
        return items;
    }

    /**
     Helper method to creating criteria object for Campaign class

     @returns - Campaign criteria object
     */
    private Campaign getCampaignExampleObject() {
        Campaign exampleObj = new Campaign();
        // TODO: Needs a better approach to check for permissions
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
            if (authorities != null && !authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                exampleObj.setEnabled(true);
            }
        }
        return exampleObj;
    }


    /**
     @inheritdoc
     */
    @Override
    public List<Campaign> getCampaignsBySite(Long retailerSiteID) {
        try {
            Criteria campaignCriteria = getSession().createCriteria(Campaign.class);
            campaignCriteria.add(Restrictions.eq("retailerSite.id", retailerSiteID));

            List<Campaign> campaignOList = campaignCriteria.list();

            for (Campaign c : campaignOList) {
                for (CampaignItem item : c.getItems()) {
                    if (item.getItemType().getValue() == CampaignItem.Type.PRODUCT.getValue()) {
                        c.getProducts().add(productRepository.get(item.getItemID()));
                    } else if (item.getItemType().getValue() == CampaignItem.Type.DOCUMENT.getValue()) {
                        // NOT SUPPORTED
                    } else if (item.getItemType().getValue() == CampaignItem.Type.VIDEO.getValue()) {
                        // NOT SUPPORTED
                    }
                }
            }
            return campaignOList;
        } catch (Exception e) {
            logger.warn("CampaignRepositoryImpl " + e.getMessage());
        }
        return null;
    }

    @Override
    @Deprecated
    public Collection<Campaign> getCampaignsBySiteandType(Long retailerSiteID, String campaignType) {
        LinkedList<Campaign> campaigns = new LinkedList<Campaign>();
        try {
            List<Object> campaignOList = getSession().createCriteria(Campaign.class)
                    .add(Restrictions.eq("retailerSite.id", retailerSiteID))
                    .add(Restrictions.eq("type", Campaign.Type.getByValue(campaignType)))
                    .list();
            if (campaignOList != null) {
                for (Object o : campaignOList) {
                    // To load the properties
                    Campaign c = getCampaign(((Campaign) o).getId());
                    // Can't cast a PersistentSet collection to a List object. work-arround is by creating a new
                    // object with collection
                    Collections.sort(new ArrayList(c.getItems()), new CampaignItemComparator());
                    for (CampaignItem item : c.getItems()) {
                        if (item.getItemType().getValue() == CampaignItem.Type.PRODUCT.getValue()) {
                            c.getProducts().add(productRepository.get(item.getItemID()));
                        } else if (item.getItemType().getValue() == CampaignItem.Type.DOCUMENT.getValue()) {
                            // NOT SUPPORTED
                        } else if (item.getItemType().getValue() == CampaignItem.Type.VIDEO.getValue()) {
                            // NOT SUPPORTED
                        }
                    }
                    campaigns.add(c);
                }
                return campaigns;
            }
        } catch (Exception e) {
            logger.warn("CampaignRepositoryImpl " + e.getMessage());
        }
        return null;
    }

    /**
     @inheritdoc
     */
    @Override
    public Campaign getCampaignBySite(RetailerSite retailerSite) {
        try {
            Campaign ex = new Campaign();
            RetailerSite site = new RetailerSite();
            site.setId(retailerSite.getId());
            ex.setRetailerSite(site);
            ex = getUniqueByExample(ex);
            return ex;
        } catch (Exception e) {
            logger.warn("CampaignRepositoryImpl " + e.getMessage());
        }
        return null;
    }

    /**
     Comparator class to sort the campaign items based on the priority
     */
    class CampaignItemComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            int item1Priority = ((CampaignItem) o1).getPriority();
            int item2Priority = ((CampaignItem) o2).getPriority();

            if (item1Priority > item2Priority) {
                return 1;
            } else if (item1Priority < item2Priority) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    @Override
    public Collection<Campaign> getActiveCampaignsBySiteName(String retailerSiteName, String campaignType)
    								throws CampaignForInactiveRetailerException 
    {
        List<Campaign> campaigns = new LinkedList<Campaign>();
        //Get RetailerSite by Name
        RetailerSite retailerSite = retailerSiteRepository.getByName(retailerSiteName);
        // verify owning retailer is "active" (too many queries -- must be optimized later)
        if ((retailerSite.getActive() == false) ||
                (retailerSite.getRetailer().getActive() == false))
            throw new CampaignForInactiveRetailerException("Campaign's retailer / retailersite is inactive");

        Criteria campaignCriteria = getSession().createCriteria(Campaign.class);
        campaignCriteria.add(Restrictions.eq("retailerSite.id", retailerSite.getId()));
        campaignCriteria.add(Restrictions.eq("type", Campaign.Type.getByValue(campaignType)));
        campaignCriteria.add(Restrictions.eq("enabled", true));
        campaignCriteria.addOrder(Order.desc("endDate"));

        campaigns = campaignCriteria.list();

        return campaigns;
    }


}

