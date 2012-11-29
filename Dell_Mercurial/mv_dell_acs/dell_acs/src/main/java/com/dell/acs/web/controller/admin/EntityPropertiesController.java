/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.controller.admin;

import com.dell.acs.managers.CampaignManager;
import com.dell.acs.managers.ProductManager;
import com.dell.acs.persistence.domain.Campaign;
import com.dell.acs.persistence.domain.CampaignCategory;
import com.dell.acs.persistence.domain.CampaignItem;
import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.web.controller.BaseDellController;
import com.dell.acs.web.crumbs.AdminCrumb;
import com.sourcen.core.ObjectNotFoundException;
import com.sourcen.core.persistence.domain.PropertiesAwareEntity;
import com.sourcen.core.util.collections.PropertiesProvider;
import org.apache.commons.httpclient.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author Sandeep Heggi.
 * @author $LastChangedBy: sameeks $
 * @version $Revision: 3422 $, $Date:: 2012-06-20 13:45:46#$
 */

@Controller
public final class EntityPropertiesController extends BaseDellController {

    @Autowired
    private CampaignManager campaignManager;

    @Autowired
    private ProductManager productManager;

    public abstract class Variation {

        protected String type;
        protected CampaignManager campaignManager;

        public Variation(String type,
                         CampaignManager campaignManager) {
            this.type = type;
            this.campaignManager = campaignManager;
        }

        public String getType() {
            return this.type;
        }

        abstract public PropertiesAwareEntity<?> get(Long id) throws Exception;

        abstract public void update(PropertiesAwareEntity<?> entity);

        public String startTimeFormat() {
            return CampaignManager.CAMPAIGN_ITEM_START_TIME_PROP;
        }

        public String endTimeFormat() {
            return CampaignManager.CAMPAIGN_ITEM_END_TIME_PROP;
        }

        public String crumbs(HttpServletRequest request,
                             PropertiesAwareEntity<?> entity) {
            return new AdminCrumb(request.getContextPath()).render("crumbs not implemented");
        }
    }

    class CampaignVariation extends Variation {

        public CampaignVariation(String type, CampaignManager campaignManager) {
            super(type, campaignManager);
        }

        public PropertiesAwareEntity<?> get(Long id) throws Exception {
            return campaignManager.getCampaign(id);
        }

        public void update(PropertiesAwareEntity<?> entity) {
            campaignManager.updateCampaign((Campaign) entity);
        }

        @Override
        public String crumbs(HttpServletRequest request,
                             PropertiesAwareEntity<?> entity) {

            Campaign campaign = (Campaign) entity;

            RetailerSite retailerSite = campaign.getRetailerSite();
            Retailer retailer = retailerSite.getRetailer();
            return new AdminCrumb(request.getContextPath())
                    .campaignManageItems(retailer.getName(),
                            retailer.getId(),
                            retailerSite.getSiteName(),
                            retailerSite.getId(),
                            campaign.getName(),
                            campaign.getId())
                    .render(AdminCrumb.TEXT_MANAGEPROPERTIES);
        }
    }

    class CampaignItemVariation extends Variation {

        public CampaignItemVariation(String type, CampaignManager campaignManager) {
            super(type, campaignManager);
        }

        public PropertiesAwareEntity<?> get(Long id) throws Exception {
            return campaignManager.getCampaignItem(id);
        }

        public void update(PropertiesAwareEntity<?> entity) {
            campaignManager.updateCampaignItem((CampaignItem) entity);
        }

        @Override
        public String crumbs(HttpServletRequest request,
                             PropertiesAwareEntity<?> entity) {

            CampaignItem campaignItem = (CampaignItem) entity;
            Campaign campaign = campaignItem.getCampaign();

            RetailerSite retailerSite = campaign.getRetailerSite();
            Retailer retailer = retailerSite.getRetailer();
            return new AdminCrumb(request.getContextPath())
                    .campaignManageItemAction(retailer.getName(),
                            retailer.getId(),
                            retailerSite.getSiteName(),
                            retailerSite.getId(),
                            campaign.getName(),
                            campaign.getId())
                    .render(AdminCrumb.TEXT_MANAGEPROPERTIES);
        }
    }

    // MWE - not fully implemented (no apparent UI)
    class CategoryVariation extends Variation {

        public CategoryVariation(String type, CampaignManager campaignManager) {
            super(type, campaignManager);
        }

        public PropertiesAwareEntity<?> get(Long id) throws Exception {
            return campaignManager.getCategory(id);
        }

        public void update(PropertiesAwareEntity<?> entity) {
            campaignManager.updateCampaignCategory((CampaignCategory) entity);
        }
    }

    // MWE - not fully implemented (no apparent UI)
    class ProductVariation extends Variation {

        private ProductManager productManager;

        public ProductVariation(String type,
                                CampaignManager campaignManager,
                                ProductManager productManager) {
            super(type, campaignManager);
            this.productManager = productManager;
        }

        public void setProductManager(ProductManager productManager) {
            this.productManager = productManager;
        }

        public PropertiesAwareEntity<?> get(Long id) throws Exception {
            return productManager.getProduct(id);
        }

        public void update(PropertiesAwareEntity<?> entity) {
            productManager.update((Product) entity);
        }
    }

    class VariationFactory {
        public Variation create(String type) throws ObjectNotFoundException {
            if (type.equalsIgnoreCase("campaign")) {
                return new CampaignVariation("campaign", campaignManager);
            }
            if (type.equalsIgnoreCase("campaignItem")) {
                return new CampaignItemVariation("campaignItem", campaignManager);
            }
            if (type.equalsIgnoreCase("category")) {
                return new CategoryVariation("category", campaignManager);
            }
            if (type.equalsIgnoreCase("product")) {
                return new ProductVariation("product", campaignManager, productManager);
            }
            throw new ObjectNotFoundException(type + " not found");
        }
    }

    @RequestMapping(value = AdminCrumb.URL_PROPERTIES_MANAGEPROPERTIES, method = RequestMethod.GET)
    public void manageProperties_get(HttpServletRequest request, Model model,
                                     @RequestParam(required = true) final Long id,
                                     @RequestParam(required = true) final String type,
                                     @RequestParam(required = false, defaultValue = "") String propName,
                                     @RequestParam(required = false, defaultValue = "") String propValue,
                                     @RequestParam(required = false, defaultValue = "") String action
    ) throws Exception {
        manageProperties(request, model, id, type, propName, propValue, action);
    }

    // Method used to update the startDate  and endDate for campaignItem. Expiry date for a Campaign Item
    @RequestMapping(value = "/admin/entities/properties/saveItem-properties.do", method = RequestMethod.POST)
    public ModelAndView saveItemProperties(HttpServletRequest request,
                                           @RequestParam(required = true) final Long id,
                                           @RequestParam(required = true) final String type,
                                           @RequestParam(required = true) Date startTime,
                                           @RequestParam(required = true) Date endTime) throws Exception {
        // assumed type is always "campaignItem"
        Variation variation = new VariationFactory().create(type);
        CampaignItem item = (CampaignItem) variation.get(id);
        if (item != null) {
            String startTimeKey = String.format(variation.startTimeFormat(), id);
            item.getProperties().setProperty(startTimeKey, startTime.getTime());
            //DateUtil.formatDate(new Date(startTime.getTime()), "MM/dd/yyyy hh:mm"));
            //DateUtils.getDate(startTime.toString()));
            String endTimeKey = String.format(variation.endTimeFormat(), id);
            item.getProperties().setProperty(endTimeKey, endTime.getTime());
            //DateUtil.formatDate(new Date(endTime.getTime()), "MM/dd/yyyy hh:mm"));
            //DateUtils.getDate(endTime.toString()));
            variation.update(item);
        } else {
            logger.info("Unable to load the campaign item.");
        }

        String redirectUrl =
                new AdminCrumb(request.getContextPath())
                        .toAbsolute(AdminCrumb.linkById(AdminCrumb.URL_PROPERTIES_MANAGEPROPERTIES, id));
        redirectUrl += "&type=" + variation.getType();
        return new ModelAndView(new RedirectView(redirectUrl));
    }

    @RequestMapping(value = AdminCrumb.URL_PROPERTIES_MANAGEPROPERTIES, method = RequestMethod.POST)
    public void manageProperties(HttpServletRequest request, Model model,
                                 @RequestParam(required = true) final Long id,
                                 @RequestParam(required = true) final String type,
                                 @RequestParam(required = true) String propName,
                                 @RequestParam(required = true) String propValue,
                                 @RequestParam(required = true) String action) throws Exception {

        Variation variation = new VariationFactory().create(type);
        PropertiesAwareEntity<?> entity = variation.get(id);
        //Fixed:CS-490-Missing validation to add new properties for product
        //Trimming propertyName and value to remove extra space
        propName = propName.trim();
        propValue = propValue.trim();
        if (action != null && !action.equalsIgnoreCase("") && !action.equalsIgnoreCase("null")) {
            if(action.equalsIgnoreCase("delete")){
                //Setting propValue to null, to get the property removed
                propValue = null;
                entity.getProperties().setProperty(propName, propValue);
                variation.update(entity);
            }else if ((propName != null && !propName.equalsIgnoreCase("") && !propName.equalsIgnoreCase("null")) &&
                    (propValue != null && !propValue.equalsIgnoreCase("") && !propValue.equalsIgnoreCase("null"))) {
                if (action.equalsIgnoreCase("add") && entity.getProperties().hasProperty(propName)) {
                    logger.error("Property Already Exist");
                    model.addAttribute("error", "Property Already Exist");

                } else if(action.equalsIgnoreCase("edit") && !entity.getProperties().hasProperty(propName)) {
                    logger.error("Property Name isn't Valid");
                    model.addAttribute("error", "Property Name isn't Valid");
                } else{
                    entity.getProperties().setProperty(propName, propValue);
                    variation.update(entity);
                }
            } else {
                logger.error("Invalid Property Name/Value");
                model.addAttribute("error", "Invalid Property Name/Value");
            }


        }

        PropertiesProvider entityProperties = entity.getProperties();
        String startTimeKey = String.format(variation.startTimeFormat(), id);
        String endTimeKey = String.format(variation.endTimeFormat(), id);
        String jsonKey = "campaign.category.json";
        // Check whether the startTime and endTime exists for this entity
        if (entityProperties.hasProperty(jsonKey)) {
            logger.info("Found " + jsonKey + " property : Deleting this property as it is not used for now");
            entityProperties.setProperty(jsonKey, null);
        }
        if (entityProperties.hasProperty(startTimeKey)) {
            Date startTime = new Date(Long.parseLong(entityProperties.getProperty(startTimeKey)));
            model.addAttribute("startTime", DateUtil.formatDate(startTime, "MM/dd/yyyy hh:mm"));
        } else {
            model.addAttribute("startTime", "");
        }
        if (entityProperties.hasProperty(endTimeKey)) {
            Date endTime = new Date(Long.parseLong(entityProperties.getProperty(endTimeKey)));
            model.addAttribute("endTime", DateUtil.formatDate(endTime, "MM/dd/yyyy hh:mm"));
        } else {
            model.addAttribute("endTime", "");
        }

        model.addAttribute("entity", entity);
        model.addAttribute("properties", entityProperties);
        model.addAttribute("propName", null);
        model.addAttribute("propValue", null);
        model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME, variation.crumbs(request, entity));
    }
}
