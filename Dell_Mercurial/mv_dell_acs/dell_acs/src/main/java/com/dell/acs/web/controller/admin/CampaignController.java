/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.controller.admin;

import com.dell.acs.CampaignAlreadyExistsException;
import com.dell.acs.CampaignItemNotFoundException;
import com.dell.acs.CampaignNotFoundException;
import com.dell.acs.PixelTrackingException;
import com.dell.acs.managers.*;
import com.dell.acs.managers.model.CampaignItemData;
import com.dell.acs.persistence.domain.*;
import com.dell.acs.persistence.repository.CampaignRepository;
import com.dell.acs.persistence.repository.ProductRepository;
import com.dell.acs.web.controller.BaseDellController;
import com.dell.acs.web.controller.formbeans.CampaignItemBean;
import com.dell.acs.web.crumbs.AdminCrumb;
import com.sourcen.core.util.FileSystem;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.WebUtils;
import com.sourcen.core.util.collections.PropertiesProvider;
import net.sf.json.JSONArray;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author Samee K.S
 * @author $LastChangedBy: sameeks $
 * @version $Revision: 3504 $, $Date:: 2012-06-21 17:43:22#$
 */

@SuppressWarnings("all")
@Controller
public final class CampaignController extends BaseDellController {

    @Autowired
    private CampaignManager campaignManager;

    @Autowired
    private TaxonomyManager taxonomyManager;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductManager productManager;

    @Autowired
    private EventManager eventManager;

    @Autowired
    private DocumentManager documentManager;

    @Autowired
    private RetailerManager retailerManager;
    
	private void addCrumbs(HttpServletRequest request, 
						   Model model, 
						   String crumbText,
						   RetailerSite retailerSite) {
		
		Retailer retailer = retailerSite.getRetailer();
		model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME, 
							new AdminCrumb(request.getContextPath())
									      .campaign(retailer.getName(),
									  				retailer.getId(),
									  			    retailerSite.getSiteName(),
									  			    retailerSite.getId())
										  .render(crumbText));  	
	}
	
	private void addCrumbsForManageItems(HttpServletRequest request, 
									     Model model, 
									     String crumbText,
									     Campaign campaign) {
	
		RetailerSite retailerSite = campaign.getRetailerSite();
		Retailer retailer = retailerSite.getRetailer();
		model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME, 
								new AdminCrumb(request.getContextPath())
										      .campaignManageItems(retailer.getName(),
									    		  			  retailer.getId(),
									    		  			  retailerSite.getSiteName(),
									    		  		      retailerSite.getId(),
									    		  		      campaign.getName(),
									    		  		      campaign.getId())
											  .render(crumbText));		
	}    

	private void addCrumbsForCloneItem(HttpServletRequest request, 
									   Model model, 
									   String crumbText,
									   Campaign campaign) {
		
		RetailerSite retailerSite = campaign.getRetailerSite();
		Retailer retailer = retailerSite.getRetailer();
		model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME, 
		new AdminCrumb(request.getContextPath())
						.campaignManageItemAction(retailer.getName(),
										   retailer.getId(),
										   retailerSite.getSiteName(),
									       retailerSite.getId(),
									       campaign.getName(),
									       campaign.getId())
						.render(crumbText));		
	}    
	
    @RequestMapping(value = "admin/campaign/getItems.do", method = RequestMethod.POST)
    public ModelAndView getItems(HttpServletRequest request, @RequestParam final Long campaignID)
            throws CampaignNotFoundException, CampaignAlreadyExistsException {
        Map model = new HashMap<String, String>();
        String redirectUrl = request.getRequestURI();
        if (campaignID > 0) {
            Campaign c = campaignManager.getCampaign(campaignID, false);
            // Set the campaign items and save
            c.setItems(getCampaignItemsFromRequest(c, request));
            c.setModifiedBy(getUser());
            campaignManager.saveCampaign(c);
            model.put("message", "Successfully updated items for campaign");
            model.put("success", true);
        } else {
            model.put("message", "Unable to load the campaign");
            model.put("success", true);
        }
        // On successful save redirect to edit campaign page
        redirectUrl = "";
        return new ModelAndView(new RedirectView(redirectUrl));
    }

    /**
     * Method used to save the items selected from the search filter
     */
    @RequestMapping(value = "admin/campaign/saveItems.json", method = RequestMethod.POST)
    public ModelAndView saveItems(Model model, HttpServletRequest request, @RequestParam final Long campaignID)
            throws CampaignNotFoundException, CampaignAlreadyExistsException {
        Map modelMap = new HashMap<String, String>();
        if (campaignID > 0) {
            Campaign c = campaignManager.getCampaign(campaignID, false);
            // Set the campaign items and save
            Set<CampaignItem> items = c.getItems();
            for (CampaignItem newItem : getCampaignItemsFromRequest(c, request)) {
                // Duplicate check is already performed in the getCampaignItemsFromRequest method. So we just add items
                // to the campaign.
                items.add(newItem);
            }
            c.setModifiedBy(getUser());
            campaignManager.saveCampaign(c);
            modelMap.put("success", true);
            // On successful save of items redirect to campaign edit page
        } else {
            modelMap.put("message", "Unable to load the campaign");
            modelMap.put("success", false);
            // manage-items.do?campaignID=3
            // If no valid campaign found redirect to same page with an error.
            // Please choose a valid campaign to add items
            model.addAttribute("error", "Unable to load the campaign. Please choose a valid campaign to add items");
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to filter the products in manage-items page and add to campaigns
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "admin/campaign/searchItems.json", method = RequestMethod.GET)
    public ModelAndView getFilteredItems(HttpServletRequest request, @RequestParam(required = true) String itemType) {
        Map model = new HashMap<String, String>();
        Map paramsMap = new HashMap<String, Object>();
        Date startDate, endDate;
        String keyword, location;
        List<Map<String, String>> items = Collections.emptyList();
        Long siteID = WebUtils.getParameter(request, "siteID", -1L);
        paramsMap.put("siteID", siteID);
        paramsMap.put("itemType", itemType);
        if (itemType.equalsIgnoreCase(CampaignItem.Type.PRODUCT.name())) {
            Long categoryID = WebUtils.getParameter(request, "categoryID", -1L);
            Collection<Long> categories = new HashSet<Long>(StringUtils.asLongList(WebUtils.getParameter(request, "categories", "")));
            String searchTerm = WebUtils.getParameter(request, "searchTerm", "");
            // Get all sub-categories for all last selected category and append to the category criteria list
            // Fix: EXTERNALINTERACTIVEADS-435
            if (categoryID > 0) {
                categories.addAll(taxonomyManager.getRecursiveSubCategories(categoryID, categories));
            }
            items = productManager.getFilteredProducts(searchTerm, categories, siteID);
        } else if (itemType.equalsIgnoreCase(CampaignItem.Type.EVENT.name())) {
            keyword = WebUtils.getParameter(request, "keyword", "");
            location = WebUtils.getParameter(request, "location", "");
            startDate = WebUtils.getParameter(request, "startDate");
            endDate = WebUtils.getParameter(request, "endDate");
            // Set the parameters
            if (StringUtils.isNotEmpty(keyword)) {
                paramsMap.put("keyword", keyword);
            }
            if (startDate != null) {
                paramsMap.put("startDate", startDate);
            }
            if (endDate != null) {
                paramsMap.put("endDate", endDate);
            }
            if (StringUtils.isNotEmpty(location)) {
                paramsMap.put("location", location);
            }
            items = eventManager.getFilteredEvents(paramsMap);
        } else if( itemType.equalsIgnoreCase(CampaignItem.Type.DOCUMENT.name()) ||
                itemType.equalsIgnoreCase(CampaignItem.Type.VIDEO.name()) ||
                itemType.equalsIgnoreCase(CampaignItem.Type.IMAGE.name()) ||
                itemType.equalsIgnoreCase(CampaignItem.Type.LINK.name())){
                keyword = WebUtils.getParameter(request, "keyword", "");
                startDate = WebUtils.getParameter(request, "startDate");
                endDate = WebUtils.getParameter(request, "endDate");
                // Set the parameters
                if (StringUtils.isNotEmpty(keyword)) {
                    paramsMap.put("keyword", keyword);
                }
                if (startDate != null) {
                    paramsMap.put("startDate", startDate);
                }
                if (endDate != null) {
                    paramsMap.put("endDate", endDate);
                }
                items = documentManager.getFilteredDocuments(paramsMap);
        }
        model.put("data", items);
        return new ModelAndView("jsonView", model);
    }


    @RequestMapping(value = "admin/campaign/reviews.json", method = RequestMethod.GET)
    public ModelAndView getReviews(HttpServletRequest request) throws CampaignItemNotFoundException {
        Map modelMap = new HashMap();
        Long campaignItemID = WebUtils.getParameter(request, "campaignItemID", -1L);
        Long itemID = WebUtils.getParameter(request, "itemID", -1L);
        Long itemType = WebUtils.getParameter(request, "itemType", -1L);
        float minStars = WebUtils.getParameter(request, "minStars", 3.0f);
        if (itemID > 0) {
            List<Map<String, Object>> reviews = new ArrayList<Map<String, Object>>();
            reviews = productManager.getAllCampaignItemProductReviews(campaignItemID, itemID, minStars);
            modelMap.put("data", JSONArray.fromObject(reviews));
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
        }
        return new ModelAndView("jsonView", modelMap);
    }
    
    /**
     * Method which return the list of all enabled and non-expired campaign.
     *
     * @param model    -
     */
    @RequestMapping(value = AdminCrumb.URL_CAMPAIGN_LIST, method = RequestMethod.GET)
    public final void getCampaigns(HttpServletRequest request, Model model,
                                   @RequestParam(value = "siteID", required = true) Long siteID) throws Exception {
    	
    	RetailerSite retailerSite = retailerManager.getRetailerSite(siteID);

        if(retailerSite == null){
            throw new EntityNotFoundException("The requested retailer site " + siteID + " not found.");
        }

	    model.addAttribute("retailerSite", retailerSite);
	    model.addAttribute("campaigns", campaignManager.getCampaignByRetailerSiteID(siteID));

	    Retailer retailer = retailerSite.getRetailer();
		model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME, 
			   	   new AdminCrumb(request.getContextPath())
		   					      .retailerSite(retailer.getName(),
   					    		  				retailer.getId(),
   					    		  			    retailerSite.getSiteName())
		   					      .render(AdminCrumb.TEXT_CAMPAIGN)); 		
    }

    /**
     * Method to delete the campaign by id.
     *
     * @param id    - id of campaign to delete
     * @param model - model attribute for model
     * @return - return the redirect string
     */
    // MWE -- not modified for crumbs
    @RequestMapping(value = "admin/campaign/delete.json", method = RequestMethod.POST)
    public ModelAndView deleteCampaign(@RequestParam final Long id, final Model model) {
        logger.info("load id :=" + id);
        // fixme: Delete is not working because we have campaign_id as NOT NULL for campaign_category table.
        // But logically its correct. All categories should be associated to a Campaign.
        // ERROR 22-May-12 06:46:00 SqlExceptionHelper:logExceptions:144 Column 'campaign_id' cannot be null
        // ERROR 22-May-12 06:46:00 BatchingBatch:performExecution:119 HHH000315: Exception executing batch [Column 'campaign_id' cannot be null]
        try {
            campaignManager.deleteCampaign(id);
        } catch (CampaignNotFoundException e) {
            logger.error("Campaign not found.", e);
            return new ModelAndView("jsonView").addObject("success", false);
        }
        return new ModelAndView("jsonView").addObject("success", true);
    }


    /**
     * Edit campaign form initializer method.
     *
     * @param id    - Campaign id
     * @param model - Model attribute for edit campaign form
     */
    @RequestMapping(value = AdminCrumb.URL_CAMPAIGN_EDIT, method = RequestMethod.GET)
    public void initCampaignEditForm(HttpServletRequest request, Model model,
    								 @RequestParam(value = "id", required = true) Long id) {
        logger.info("load id :=" + id);
        Campaign campaign;
        try {
            campaign = campaignManager.getCampaign(id, false);
        } catch (CampaignNotFoundException cnEx) {
        	throw new RuntimeException("Campaign not found.");
        }
        
        campaign.setId(id);
        campaign.setPackageType(campaign.getProperties().getBooleanProperty(
                CampaignManager.CAMPAIGN_PACKAGE_TYPE_PROPERTY, false));
        model.addAttribute("campaign", campaign);

        RetailerSite retailerSite = campaign.getRetailerSite();
        addCrumbs(request, model, AdminCrumb.TEXT_CAMPAIGN_EDIT, retailerSite);        
    }
    
    /**
     * Method to handle the Edit form submit.
     *
     * @param model      - Model attribute for Campaign Edit form
     * @param campaign   - binded Campaign object
     * @param binding    - Webdata binding object
     * @return return - redirect string
     */
    @RequestMapping(value = AdminCrumb.URL_CAMPAIGN_EDIT, method = RequestMethod.POST)
    public ModelAndView updateCampaign(@ModelAttribute Campaign campaign,
                                       @RequestParam(value = "thumbnailFile", required = false) MultipartFile file,
                                       BindingResult binding, 
                                       HttpServletRequest request, Model model) 
                                    		   throws CampaignNotFoundException {
        if (!binding.hasErrors()) {
            logger.debug("Update the campaign id :=" + campaign.getId());
            Campaign campaignObj = campaignManager.getCampaign(campaign.getId(), false);
            String finalFileName = "";
            try {
                if (((DefaultMultipartHttpServletRequest) request).getMultiFileMap().containsKey("thumbnailFile")) {
                    file = ((DefaultMultipartHttpServletRequest) request).getMultiFileMap().getFirst("thumbnailFile");
                    if (file != null && !file.isEmpty()) {
                        String baseCDNPathForCampaign = campaignManager.getBaseCDNPathForCampaign(campaign);
                        FileSystem fileSystem = FileSystem.getDefault();
                        String thumbnailFileName = "thumbnail".concat(".png"); // Its only PNG for now
                        finalFileName = baseCDNPathForCampaign.concat(thumbnailFileName);
                        logger.info(finalFileName);
                        boolean exists = new File(finalFileName).exists();
                        if (exists) {
                            new File(finalFileName).delete();
                        }
                        File thumbnailFile = fileSystem.getFile(finalFileName, true, true);
                        file.transferTo(thumbnailFile);
                        logger.info("Thumbnail image saved in - " + thumbnailFile.getAbsolutePath());
                        //Set property for 'thumbnail'
                        campaignObj.getProperties().setProperty(CampaignManager.CAMPAIGN_THUMBNAIL_PROPERTY, finalFileName);
                    }
                }

                campaignObj.setName(campaign.getName());
                campaignObj.setStartDate(campaign.getStartDate());
                campaignObj.setEndDate(campaign.getEndDate());
                campaignObj.setEnabled(campaign.getEnabled());
                if (campaign.getType() == null) {
                    campaign.setType(Campaign.Type.HOTDEALS);
                }
                campaignObj.setType(campaign.getType());
                campaignObj.setModifiedBy(getUser());

                // Update campaign related properties

                //Set property 'packageType' property is the Campaign is of Package Type
                boolean isPackageTypeCampaign = WebUtils.getParameter(request, "packageType", false);
                campaignObj.getProperties().setProperty(CampaignManager.CAMPAIGN_PACKAGE_TYPE_PROPERTY, isPackageTypeCampaign);
                boolean resetThumbnail = WebUtils.getBooleanParameter(request, "resetThumbnail");
                if (resetThumbnail) {
                    campaignObj.getProperties().setProperty(CampaignManager.CAMPAIGN_THUMBNAIL_PROPERTY, null);
                    campaignObj.getProperties().setProperty(CampaignManager.CAMPAIGN_THUMBNAIL_TYPE_PROPERTY, null);
                }

                campaignManager.saveCampaign(campaignObj);

            } catch (CampaignAlreadyExistsException acsEx) {
                // Add a error message and return to same page
                logger.error("Duplicate name for a campaign");
            } // Add a error message and return to same page
            catch (IOException e) {
                logger.error("Error while reading the thumbnail file.", e);
            }
        } else {
            // Add a error message and redirect to same page
            logger.error("Binding error - " + binding.getAllErrors());
        }
        
      
        String redirectUrl = 
            	new AdminCrumb(request.getContextPath())
		       	.toAbsolute(AdminCrumb.linkById(AdminCrumb.URL_CAMPAIGN_EDIT, campaign.getId()));
        return new ModelAndView(new RedirectView(redirectUrl));            	
    }

    private Campaign getCampaign(Long id) throws CampaignNotFoundException {
        return campaignManager.getCampaign(id);
    }

    @RequestMapping(value = AdminCrumb.URL_CAMPAIGN_CLONEITEM, method = RequestMethod.GET)
    public void initCloneItemForm(@RequestParam(required = false) Long id,
                                  @RequestParam(required = false) Long productID,
                                  @RequestParam(required = false) Long campaignID, 
                                  HttpServletRequest request, Model model)
            throws CampaignNotFoundException, CampaignItemNotFoundException {
        Map<String, String> cloneData = new HashMap<String, String>();
        Product cloningProduct = null;
        try {
            cloningProduct = productManager.getProduct(productID);
        } catch (Exception e) {
            logger.error("Product with id:=" + productID + " doesn't exist or is disabled", e);
        }
        if (cloningProduct != null) {
            if (cloningProduct.getProductId().startsWith("CL-")) {
                cloneData.put("productID", cloningProduct.getProductId());
            } else {
                cloneData.put("productID", "AUTO-GENERATED");
            }
            cloneData.put("title", cloningProduct.getTitle());
            cloneData.put("description", cloningProduct.getDescription());
            cloneData.put("specifications", cloningProduct.getSpecifications());
            // Fix: CS-306
            cloneData.put("price", (cloningProduct.getPrice() != null) ? String.valueOf(cloningProduct.getPrice()) : "0.0");
            cloneData.put("listPrice", (cloningProduct.getListPrice() != null) ? String.valueOf(cloningProduct.getListPrice()) : "0.0");
            cloneData.put("promotional", cloningProduct.getPromotional());
            cloneData.put("shippingPromotion", cloningProduct.getShippingPromotion());
            cloneData.put("availability", (cloningProduct.getAvailability() != null) ? String.valueOf(cloningProduct.getAvailability()) : "1");

            model.addAttribute("cloneObject", cloneData);

            Campaign campaign = campaignManager.getCampaign(campaignID, false);
            model.addAttribute("campaign", campaign);
            addCrumbsForCloneItem(request, model, AdminCrumb.TEXT_CLONEITEM, campaign);
        }
    }

    @RequestMapping(value = AdminCrumb.URL_CAMPAIGN_CLONEITEM, method = RequestMethod.POST)
    public ModelAndView saveCloneItem(HttpServletRequest request, Model model)
            throws CampaignNotFoundException, CampaignAlreadyExistsException, CampaignItemNotFoundException {

        Long campaignItemID = WebUtils.getParameter(request, "id", -1L);
        Long originalProductID = WebUtils.getParameter(request, "originalProductID", -1L);
        //Long productID = WebUtils.getParameter(request, "productID", -1L);
        Long campaignID = WebUtils.getParameter(request, "campaignID", -1L);
        Boolean isUpdate = false;

        if (campaignID > 0) {
            CampaignItem item = null;
            if (originalProductID > 0 && campaignItemID > 0) {
                item = campaignManager.getCampaignItem(campaignItemID);
                Product originalProduct = null;
                try {
                    originalProduct = productManager.getProduct(originalProductID);
                } catch (Exception e) {
                    logger.error("Product with id:=" + originalProductID + " doesn't exist or is disabled", e);
                }
                Product productClone = null;
                if (originalProduct != null) {
                    if (item.getProperties().hasProperty(CampaignManager.CAMPAIGN_ITEM_CLONED_PROP)
                            && item.getProperties().getBooleanProperty(CampaignManager.CAMPAIGN_ITEM_CLONED_PROP)) {
                        productClone = originalProduct;
                    } else {
                        // All these data should go as a new product
                        try {
                            productClone = (Product) BeanUtils.cloneBean(originalProduct);
                        } catch (IllegalAccessException e) {
                            logger.error("Error while cloning a Product entity ID " + originalProduct.getId(), e);
                        } catch (InstantiationException e) {
                            logger.error("Error while cloning a Product entity ID " + originalProduct.getId(), e);
                        } catch (InvocationTargetException e) {
                            logger.error("Error while cloning a Product entity ID " + originalProduct.getId(), e);
                        } catch (NoSuchMethodException e) {
                            logger.error("Error while cloning a Product entity ID " + originalProduct.getId(), e);
                        }
                        // logger.info("Cloned product successfully !!!");
                    }

                    // Product Name / Title
                    String title = WebUtils.getParameter(request, "title", originalProduct.getTitle());
                    productClone.setTitle(title);

                    // Product Description
                    String description = WebUtils.getParameter(request, "description", originalProduct.getDescription());
                    productClone.setDescription(description);

                    // Specification
                    String specifications = WebUtils.getParameter(request, "specifications", originalProduct.getSpecifications());
                    productClone.setSpecifications(specifications);

                    // Market Value
                    String price = String.valueOf(WebUtils.getParameter(request, "price", originalProduct.getPrice()));
                    productClone.setPrice(Float.valueOf(price));

                    // List Price
                    String listPrice = String.valueOf(WebUtils.getParameter(request, "listPrice", originalProduct.getListPrice()));
                    productClone.setListPrice(Float.valueOf(listPrice));

                    // TBD
                    // Categories - All 6 Category field  (Need to discuss with Navin to create a taxonomy )

                    // Availability - Alter the columnType from boolean to String
                    productClone.setAvailability(Integer.valueOf(WebUtils.getParameter(request, "availability", -1)));

                    // Promotional Text field 1
                    String promotional = WebUtils.getParameter(request, "promotional", originalProduct.getPromotional());
                    productClone.setPromotional(promotional);

                    // Promotional Text field 2
                    String shippingPromotion = WebUtils.getParameter(request, "shippingPromotion", originalProduct.getShippingPromotion());
                    productClone.setShippingPromotion(shippingPromotion);

                    // Declaration of campaign item
                    CampaignItem clonedItem = null;

                    // Based on the item cloned property we can decide weather to insert/update the product
                    if (item.getProperties().hasProperty(CampaignManager.CAMPAIGN_ITEM_CLONED_PROP) &&
                            item.getProperties().getBooleanProperty(CampaignManager.CAMPAIGN_ITEM_CLONED_PROP)) {
                        productClone.setModifiedBy(getUser());
                        productClone.setModifiedDate(new Date());
                    } else {
                        productClone.setId(null);
                        productClone.setCreatedBy(getUser());
                        productClone.setCreatedDate(new Date());
                        productClone.setModifiedBy(getUser());
                        productClone.setModifiedDate(new Date());
                        productClone.setVersion(0L);

                        // Clone images, sliders and reviews only at creation
                        // Update the image references to the new product
                        if (productClone.getImages().size() > 0) {
                            List<ProductImage> images = new ArrayList<ProductImage>();
                            for (ProductImage image : originalProduct.getImages()) {
                                ProductImage clonedImage = null;
                                try {
                                    clonedImage = (ProductImage) BeanUtils.cloneBean(image);
                                    clonedImage.setId(null);
                                    clonedImage.setVersion(0L);
                                    clonedImage.setProduct(productClone);
                                    images.add(clonedImage);
                                } catch (IllegalAccessException e) {
                                    logger.error("Error while cloning a ProductImage entity ID " + image.getId(), e);
                                } catch (InstantiationException e) {
                                    logger.error("Error while cloning a ProductImage entity ID " + image.getId(), e);
                                } catch (InvocationTargetException e) {
                                    logger.error("Error while cloning a ProductImage entity ID " + image.getId(), e);
                                } catch (NoSuchMethodException e) {
                                    logger.error("Error while cloning a ProductImage entity ID " + image.getId(), e);
                                }

                            }
                            productClone.setImages(images);
                        } else {
                            productClone.setImages((Collection) Collections.emptyList());
                        }

                        // Update the review references to the new product
                        if (productClone.getProductReviews().size() > 0) {
                            List<ProductReview> reviews = new ArrayList<ProductReview>();
                            for (ProductReview review : originalProduct.getProductReviews()) {
                                ProductReview clonedReview = null;
                                try {
                                    clonedReview = (ProductReview) BeanUtils.cloneBean(review);
                                    clonedReview.setId(null);
                                    clonedReview.setVersion(0L);
                                    clonedReview.setProduct(productClone);
                                    reviews.add(clonedReview);
                                } catch (IllegalAccessException e) {
                                    logger.error("Error while cloning a ProductReview entity ID: " + review.getId(), e);
                                } catch (InstantiationException e) {
                                    logger.error("Error while cloning a ProductReview entity ID: " + review.getId(), e);
                                } catch (InvocationTargetException e) {
                                    logger.error("Error while cloning a ProductReview entity ID: " + review.getId(), e);
                                } catch (NoSuchMethodException e) {
                                    logger.error("Error while cloning a ProductReview entity ID: " + review.getId(), e);
                                }

                            }
                            productClone.setProductReviews(reviews);
                        } else {
                            productClone.setProductReviews((Collection) Collections.emptyList());
                        }

                        // Update the slider references to the new product
                        if (productClone.getSliders().size() > 0) {
                            List<ProductSlider> sliders = new ArrayList<ProductSlider>();
                            for (ProductSlider slider : originalProduct.getSliders()) {
                                // Chethan : when u clone a product the product is nothing but the sourceProduct in the slider..
                                // so.. u have to make an another entry in the db with the cloneproductid as the
                                // sourceproductid and targetproductid  remains the same..
                                ProductSlider clonedSlider = null;
                                try {
                                    clonedSlider = (ProductSlider) BeanUtils.cloneBean(slider);
                                    clonedSlider.setId(null);
                                    clonedSlider.setSourceProduct(productClone);
                                    sliders.add(clonedSlider);
                                } catch (IllegalAccessException e) {
                                    logger.error("Error while cloning a ProductSlider entity ID: " + slider.getId(), e);
                                } catch (InstantiationException e) {
                                    logger.error("Error while cloning a ProductSlider entity ID: " + slider.getId(), e);
                                } catch (InvocationTargetException e) {
                                    logger.error("Error while cloning a ProductSlider entity ID: " + slider.getId(), e);
                                } catch (NoSuchMethodException e) {
                                    logger.error("Error while cloning a ProductSlider entity ID: " + slider.getId(), e);
                                }


                            }
                            // Set new sliders to the cloned product
                            productClone.setSliders(sliders);
                        } else {
                            productClone.setSliders((Collection) Collections.emptyList());
                        }

                        // Save or Update the cloned product
                        productClone = productManager.saveOrUpdate(productClone);

                        // Update the productID - CL-<currentProductID>-<id>
                        String newProductID = String.format(CampaignManager.CLONED_PRODUCT_ID_FORMAT,
                                productClone.getProductId(), productClone.getId());

                        productClone.setProductId(newProductID);
                    }


                    // To update the changes properties and productID if its while creating a cloned product
                    productManager.update(productClone);

                    // Update the campaign details
                    PropertiesProvider itemProperties = item.getProperties();
                    if (item.getProperties().hasProperty(CampaignManager.CAMPAIGN_ITEM_CLONED_PROP)
                            && item.getProperties().getBooleanProperty(CampaignManager.CAMPAIGN_ITEM_CLONED_PROP)) {
                        clonedItem = item;
                        clonedItem.setModifiedBy(getUser());
                    } else {
                        clonedItem = new CampaignItem(productClone.getId(), CampaignItem.Type.PRODUCT);
                        clonedItem.setCampaign(item.getCampaign());
                        clonedItem.setCreatedBy(getUser());
                        clonedItem.setModifiedBy(getUser());
                        clonedItem.setPriority(0);
                        clonedItem.setVersion(0L);
                        // Set item CLONED = TRUE
                        clonedItem.getProperties().setProperty(CampaignManager.CAMPAIGN_ITEM_CLONED_PROP, true);
                    }
                    // Update the properties
                    campaignManager.saveCampaignItem(clonedItem);
                }
            }
        }
        
        String redirectUrl = 
            	new AdminCrumb(request.getContextPath())
		       	.toAbsolute(AdminCrumb.linkByCampaignId(AdminCrumb.URL_CAMPAIGN_MANAGEITEMS, campaignID));
        return new ModelAndView(new RedirectView(redirectUrl));              
    }
	
    /**
     * Method to initialize the add Campaign form.
     *
     * @param model  - Model object for the campaign form
     * @param siteID - SiteID for which the campaign should be binded with.
     */
    @RequestMapping(value = AdminCrumb.URL_CAMPAIGN_ADD, method = RequestMethod.GET)
    public void initCampaignAddForm(HttpServletRequest request, Model model, 	
    							    @RequestParam(value = "siteID", required = true) Long siteID) {

    	RetailerSite retailerSite = retailerManager.getRetailerSite(siteID);
        if(retailerSite == null){
            throw new EntityNotFoundException("The requested retailer site " + siteID + " not found.");
        }
    	Campaign campaign = new Campaign();
    	
        campaign.setRetailerSite(retailerSite);
        model.addAttribute("campaign", campaign);
        addCrumbs(request, model, AdminCrumb.TEXT_CAMPAIGN_ADD, retailerSite);
       
    }

    /**
     * Method to save Campaign.
     *
     * @param model      - Campaign model attribute
     * @param campaign   - Model object
     * @param binding    - Webbinding object
     * @return - Redirect URL
     */
    @RequestMapping(value = AdminCrumb.URL_CAMPAIGN_ADD, method = RequestMethod.POST)
    public ModelAndView saveCampaign(HttpServletRequest request, 
    							     Model model,
                                     @ModelAttribute Campaign campaign, 
                                     BindingResult binding,
                                     @RequestParam(value = "thumbnailFile", required = false) MultipartFile file) 
                                    		 throws CampaignNotFoundException, CampaignAlreadyExistsException {

    	RetailerSite retailerSite = null;
        if (campaign != null && campaign.getRetailerSite() != null && campaign.getRetailerSite().getId() != null) {
            retailerSite = retailerManager.getRetailerSite(campaign.getRetailerSite().getId());
            if(retailerSite == null){
                throw new EntityNotFoundException("The requested retailer site " + campaign.getRetailerSite().getId() + " not found.");
            }
            campaign.setRetailerSite(retailerSite);
        }

        if (!binding.hasErrors()) {

            // By default will have the type as HOTDEALS
            campaign.setType(Campaign.Type.HOTDEALS);

            campaign.setCreatedBy(getUser());
            campaign.setModifiedBy(getUser());
            // Set property 'packageType' property is the Campaign is of Package Type
            if (WebUtils.hasParameter(request, "packageType") && WebUtils.getParameter(request, "packageType", false)) {
                campaign.getProperties().setProperty(CampaignManager.CAMPAIGN_PACKAGE_TYPE_PROPERTY, true);
            }
            // Upon successful save we have to create and update the campaign image
            campaign = campaignManager.saveCampaign(campaign);

            // Save the campaign first. Since the campaignID is required for the CDN path construction
            String finalFileName = "";
            if (((DefaultMultipartHttpServletRequest) request).getMultiFileMap().containsKey("thumbnailFile")) {
                file = ((DefaultMultipartHttpServletRequest) request).getMultiFileMap().getFirst("thumbnailFile");
                if (file != null && !file.isEmpty()) {
                    String baseCDNPathForCampaign = "";
                    File thumbnailFile = null;
                    try {
                        baseCDNPathForCampaign = campaignManager.getBaseCDNPathForCampaign(campaign);
                        FileSystem fileSystem = FileSystem.getDefault();
                        String thumbnailFileName = "thumbnail".concat(".png"); // Its only PNG for now
                        finalFileName = baseCDNPathForCampaign.concat(thumbnailFileName);
                        logger.info(finalFileName);
                        boolean exists = new File(finalFileName).exists();
                        if (exists) {
                            new File(finalFileName).delete();
                        }
                        thumbnailFile = fileSystem.getFile(finalFileName, true, true);
                        file.transferTo(thumbnailFile);
                        logger.info("Thumbnail image saved in - " + thumbnailFile.getAbsolutePath());
                    } catch (IOException ioEx) {
                        logger.error("Error: ", ioEx);
                        if (thumbnailFile == null) {
                            logger.error("Unable to save the thumbnail image in - " + baseCDNPathForCampaign);
                        }

                    }

                    //Set property for 'thumbnail'
                    campaign.getProperties().setProperty(CampaignManager.CAMPAIGN_THUMBNAIL_PROPERTY, finalFileName);
                }
            }

            campaign = campaignManager.saveCampaign(campaign);

            model.addAttribute("campaign", campaign);
            logger.info("Successfully created campaign - " + campaign.getName());
            
            // After successful save redirect to campaign edit page. With a success message
            
            String redirectUrl = 
                	new AdminCrumb(request.getContextPath())
    		       	.toAbsolute(AdminCrumb.linkById(AdminCrumb.URL_CAMPAIGN_EDIT, campaign.getId()));
            return new ModelAndView(new RedirectView(redirectUrl));              
        }
        
        String returnURL = "Unable to save the campaign.";
        if (retailerSite != null) {
            returnURL = returnURL.concat("Please try again <a href='add.do?siteID=")
                    .concat(retailerSite.getId().toString()
                            .concat("'>here</a>"));
        }
        throw new RuntimeException(returnURL);
    }

    /**
     * Method used to update the campaignItem related properties.
     *
     * @param request
     * @param campaignItemId
     * @param name
     * @param value
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "admin/campaign/setItemProperty.json", method = RequestMethod.POST)
    public ModelAndView setCampaignItemProperty(HttpServletRequest request,
                                                @RequestParam(value = "campaignItemId", required = true) Long campaignItemId,
                                                @RequestParam(value = "name", required = false) String name,
                                                @RequestParam(value = "value", required = true) String value)
            throws CampaignItemNotFoundException {
        ModelAndView mv = new ModelAndView("jsonView");
        CampaignItem campaignItem = campaignManager.getCampaignItem(campaignItemId, false);
        if (campaignItem != null) {
            if (value.length() == 0) {
                value = null;
            }
            campaignItem.getProperties().setProperty(name, value);
            campaignManager.updateCampaignItem(campaignItem);
            mv.addObject("success", true);
        }
        return mv;
    }

    @RequestMapping(value = "admin/campaign/downloadLinks.do")
    public ModelAndView downloadCampaignItems(HttpServletResponse response, @RequestParam final Long campaignID) {
        // Read the campaign items and convert it to a csv file to download
        try {
            File file = campaignManager.downloadCampaignItems(campaignID);
            if (file != null) {
                Campaign campaign = campaignManager.getCampaign(campaignID, false);
                response.setContentType("text/csv");
                response.setHeader("Content-Disposition", "attachment;filename=download_campaign_items.csv");
                FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
                response.setStatus(HttpServletResponse.SC_OK);
                // Set a campaign property for download flag
                campaign.getProperties().setProperty(CampaignManager.CAMPAIGN_LT_LINKS_DOWNLOADED_PROP, true);
                campaign.setModifiedBy(getUser());
                campaignManager.saveCampaign(campaign);
            }
        } catch (IOException e) {
            logger.error("Error while downloading the campaign items as a csv file.", e);
        } catch (CampaignNotFoundException e) {
            logger.error("Failed to download the links. Invalid campaignID - " + campaignID, e);
        } catch (CampaignAlreadyExistsException e) {
            // This exception is not thrown from this operation
            logger.error("Campaign already exists.", e);
        }
        return null;
    }


    @RequestMapping(value = "admin/campaign/uploadCampaignItems.json")
    public ModelAndView uploadCampaignItems(HttpServletResponse response, HttpServletRequest request,
                                            @RequestParam final Long campaignID,
                                            @RequestParam final String pixelTrackerType,
                                            @RequestParam(value = "uploadFile", required = false) MultipartFile file)
            throws IOException, PixelTrackingException {
        ModelAndView mv = new ModelAndView();
        try {
            logger.info("-----------------------------------------   pixelTrackerType :::   " + pixelTrackerType + " --------------          campaignID  :::  " + campaignID);

            if (campaignID != null && campaignID > 0) {

                // This is achieved at the repository and will throw CampaignNotFoundException
                if (((DefaultMultipartHttpServletRequest) request).getMultiFileMap().containsKey("uploadFile")) {
                    file = ((DefaultMultipartHttpServletRequest) request).getMultiFileMap().getFirst("uploadFile");

                    if (!file.isEmpty()) {
                        Campaign campaign = campaignManager.getCampaign(campaignID, false);


                        campaignManager.updateCampaignItemLinks(campaignID, file, pixelTrackerType);
                        mv.addObject("Successfully updated the pixel tracking.");
                        // Remove the downloaded status property and insert the uploaded status property
                        campaign.getProperties().setProperty(CampaignManager.CAMPAIGN_LT_LINKS_DOWNLOADED_PROP, null);
                        // Add uploaded property
                        campaign.getProperties().setProperty(CampaignManager.CAMPAIGN_LT_LINKS_UPLOADED_PROP, true);
                        campaign.setModifiedBy(getUser());

                        // Update the properties
                        campaignManager.saveCampaign(campaign);
                    } else {
                        logger.info("NO file was uploaded");
                    }
                }
            }
        } catch (CampaignItemNotFoundException e) {
            logger.error("Unable to fine the campaign item. ", e);
        } catch (CampaignNotFoundException e) {
            logger.error("Unable to fine the campaign", e);
        } catch (CampaignAlreadyExistsException e) {
            logger.error("Campaign already exists", e);
        }
        return mv;
    }


    /**
     * Method used to update the campaign related properties. Currently being used to update campaign_item priority
     *
     * @return - Success string
     */
    @RequestMapping(value = "admin/campaign/deleteItem.json", method = RequestMethod.POST)
    public ModelAndView deleteItem(HttpServletRequest request,
                                   @RequestParam(value = "itemID", required = true) Long itemID,
                                   @RequestParam(value = "campaignID", required = true) Long campaignID) throws CampaignNotFoundException {
        try {
            if ((itemID != null && itemID > 0) && (campaignID != null && campaignID > 0)) {
                campaignManager.deleteCampaignItem(itemID, campaignID);
            }
        } catch (CampaignItemNotFoundException e) {
            logger.error("Unable to find the campaign item to perform the operation. ID - " + itemID);
            return new ModelAndView("jsonView").addObject("success", false);
        }

        return new ModelAndView("jsonView").addObject("success", true);
    }

    /**
     * Methods serves the data for Categories drop down in Campaign item selection popup filter panel
     *
     * @return MV object with list of categories
     */
    @RequestMapping(value = "admin/campaign/item-categories.json", method = RequestMethod.GET)
    public ModelAndView getCategories(@RequestParam(value = "parentCategoryID", required = false) final Long parentCategoryID,
                                      @RequestParam(value = "campaignID", required = false) final Long campaignID,
                                      @RequestParam(value = "name", required = false) final String name) {
        logger.debug("Fetch sub-categories for parent " + parentCategoryID);
        Map<String, String> categories = productRepository.getCategories(parentCategoryID, name, campaignID);

        Map model = new HashMap<String, String>();
        if (categories.size() > 0) {
            logger.debug(categories.size() + " - categories found for parent category " + parentCategoryID);
            model.put("data", categories);
        } else {
            logger.debug("No Subcategories found for parent category " + parentCategoryID);
            model.put("data", Collections.<Object>emptyList());
        }
        return new ModelAndView("jsonView", model);
    }

    @RequestMapping(value = AdminCrumb.URL_CAMPAIGN_MANAGEITEMS, method = RequestMethod.GET)
    public ModelAndView manageItems(HttpServletRequest request, Model model, 
    								@RequestParam Long campaignID) 
    										throws CampaignNotFoundException {
        boolean redirect = false;
        Campaign campaign = campaignManager.getCampaign(campaignID, false);
        Collection<CampaignItemData> dataItems = campaignManager.getCampaignItems(campaignID);
        Collection<CampaignItemBean> items = new ArrayList<CampaignItemBean>();
        for(CampaignItemData data : dataItems) {
        	items.add(new CampaignItemBean(data));
        }

        List<CampaignItemBean> products = new ArrayList<CampaignItemBean>();
        List<CampaignItemBean> events = new ArrayList<CampaignItemBean>();
        List<CampaignItemBean> documents = new ArrayList<CampaignItemBean>();
        List<CampaignItemBean> links = new ArrayList<CampaignItemBean>();
        List<CampaignItemBean> videos = new ArrayList<CampaignItemBean>();
        List<CampaignItemBean> images = new ArrayList<CampaignItemBean>();
        if (campaign == null) {
            redirect = true;
        } else {
            // Breadcrumb data
            model.addAttribute("campaign", campaign);

            // List the campaign items
            model.addAttribute("items", items);

            /* Segregate the item types */
            for (CampaignItemBean bean : items) {
                if (bean.getItemType().equalsIgnoreCase(CampaignItem.Type.PRODUCT.name())) {
                    products.add(bean);
                } else if (bean.getItemType().equalsIgnoreCase(CampaignItem.Type.EVENT.name())) {
                    events.add(bean);
                } else if (bean.getItemType().equalsIgnoreCase(CampaignItem.Type.DOCUMENT.name())) {
                    documents.add(bean);
                }  else if (bean.getItemType().equalsIgnoreCase(CampaignItem.Type.VIDEO.name())) {
                    videos.add(bean);
                } else if (bean.getItemType().equalsIgnoreCase(CampaignItem.Type.LINK.name())) {
                    links.add(bean);
                } else if (bean.getItemType().equalsIgnoreCase(CampaignItem.Type.IMAGE.name())) {
                    images.add(bean);
                }
            }
            // TBD: Change here to extend the support for new content types
            model.addAttribute("products", products);
            model.addAttribute("events", events);
            model.addAttribute("documents", documents);
            model.addAttribute("links", links);
            model.addAttribute("videos", videos);
            model.addAttribute("images", images);
            String status = "";
            PropertiesProvider props = campaign.getProperties();
            if (props.hasProperty(CampaignManager.CAMPAIGN_LT_LINKS_DOWNLOADED_PROP)
                    && props.getBooleanProperty(CampaignManager.CAMPAIGN_LT_LINKS_DOWNLOADED_PROP)) {
                status = "downloaded";
            } else if (props.hasProperty(CampaignManager.CAMPAIGN_LT_LINKS_UPLOADED_PROP)
                    && props.getBooleanProperty(CampaignManager.CAMPAIGN_LT_LINKS_UPLOADED_PROP)) {
                status = "uploaded";
            }
            model.addAttribute("status", status);

            addCrumbsForManageItems(request, model, AdminCrumb.TEXT_MANAGEITEMS, campaign);
        }
        return new ModelAndView();
    }

    @RequestMapping(value = AdminCrumb.URL_CAMPAIGN_MANAGECATEGORIES, method = RequestMethod.GET)
    public ModelAndView manageCategories(HttpServletRequest request, Model model, 
    									 @RequestParam Long campaignID) 
    											 throws CampaignNotFoundException {
        boolean redirect = false;
        Campaign campaign = campaignManager.getCampaign(campaignID, false);
        List<CampaignItemBean> products = new ArrayList<CampaignItemBean>();
        List<CampaignItemBean> events = new ArrayList<CampaignItemBean>();
        List<CampaignItemBean> documents = new ArrayList<CampaignItemBean>();
        List<CampaignItemBean> links = new ArrayList<CampaignItemBean>();
        List<CampaignItemBean> videos = new ArrayList<CampaignItemBean>();
        List<CampaignItemBean> images = new ArrayList<CampaignItemBean>();

        for (CampaignItemData data : campaignManager.getCampaignItems(campaignID)) {
            if (data.getItemType().equalsIgnoreCase(CampaignItem.Type.PRODUCT.name())) {
                products.add(new CampaignItemBean(data));
            } else if (data.getItemType().equalsIgnoreCase(CampaignItem.Type.EVENT.name())) {
                events.add(new CampaignItemBean(data));
            } else if (data.getItemType().equalsIgnoreCase(CampaignItem.Type.DOCUMENT.name())) {
                documents.add(new CampaignItemBean(data));
            }  else if (data.getItemType().equalsIgnoreCase(CampaignItem.Type.VIDEO.name())) {
                videos.add(new CampaignItemBean(data));
            } else if (data.getItemType().equalsIgnoreCase(CampaignItem.Type.LINK.name())) {
                links.add(new CampaignItemBean(data));
            } else if (data.getItemType().equalsIgnoreCase(CampaignItem.Type.IMAGE.name())) {
                images.add(new CampaignItemBean(data));
            }
        }
        if (campaign == null) {
            redirect = true;
        } else {
            model.addAttribute("campaign", campaign);
            model.addAttribute("items", products);
            model.addAttribute("events", events);
            model.addAttribute("documents", documents);
            model.addAttribute("links", links);
            model.addAttribute("videos", videos);
            model.addAttribute("images", images);
        }
        
		addCrumbsForManageItems(request, model, AdminCrumb.TEXT_MANAGECATEGORIES, campaign);
        return new ModelAndView();
    }

    private List<Long> removeAlreadyAssociatedItemsFromList(Set<CampaignItem> items, List<Long> productIDs, CampaignItem.Type type) {
        List<Long> filteredList = new ArrayList<Long>();
        int index = 0;
        boolean exists = false;
        for (Long productID : productIDs) {
            // Reset the exists flag
            exists = false;
            // Check if UI selected productID exist in the items list
            for (CampaignItem item : items) {
                // item.getItemID() == productID - always returning false
                // logger.info("Product ID : " + productIDs.get(0) + " ItemID - " + item.getItemID());
                if (productID == item.getItemID().longValue() && type == item.getItemType()) {
                    logger.info("Product ID - " + item.getItemID().longValue() + " is already associated with the campaign so skipping the addition.");
                    exists = true;
                }
            }
            if (!exists) {
                logger.info("Add new item to items list > " + productID);
                filteredList.add(productID);
            }
        }
        return filteredList;
    }

    // Helper method to get the Campaign item details from request object and filter if any exists products are
    // already mapped
    private Set<CampaignItem> getCampaignItemsFromRequest(Campaign campaign, HttpServletRequest request)
            throws CampaignNotFoundException {
        Set<CampaignItem> campaignItems = new HashSet<CampaignItem>();
        Set<CampaignItem> existingItems = campaign.getItems();
        // Set product items
        if (request.getParameterMap().containsKey("products") && request.getParameterMap().get("products") != null) {
            List<Long> prodIDList = StringUtils.asLongList(request.getParameter("products"));
            // Get the filter list. Duplicates and already associated items will be filtered
            // This will filter the items with same productID selected from the UI.
            prodIDList = removeAlreadyAssociatedItemsFromList(existingItems, prodIDList, CampaignItem.Type.PRODUCT);
            for (Long productID : prodIDList) {
                CampaignItem item = new CampaignItem(productID, CampaignItem.Type.PRODUCT);
                item.setCampaign(campaign);
                // Can't fetch the MAX Priority based on campaign its should be based on the Category
                //item.setPriority(campaignManager.getMaxItemPriority(campaign.getId()));
                item.setCreatedBy(getUser());
                item.setModifiedBy(getUser());
                item.setCreationDate(new Date());
                item.setModifiedDate(new Date());
                campaignItems.add(item);
            }
        }

        if (request.getParameterMap().containsKey("events") && request.getParameterMap().get("events") != null) {
            List<Long> eventList = StringUtils.asLongList(request.getParameter("events"));
            // Get the filter list. Duplicates and already associated items will be filtered
            // This will filter the items with same productID selected from the UI.
            eventList = removeAlreadyAssociatedItemsFromList(existingItems, eventList, CampaignItem.Type.EVENT);
            for (Long eventID : eventList) {
                CampaignItem item = new CampaignItem(eventID, CampaignItem.Type.EVENT);
                item.setCampaign(campaign);
                // Can't fetch the MAX Priority based on campaign its should be based on the Category
                //  item.setPriority(campaignManager.getMaxItemPriority(campaign.getId()));
                item.setCreatedBy(getUser());
                item.setModifiedBy(getUser());
                item.setCreationDate(new Date());
                item.setModifiedDate(new Date());
                campaignItems.add(item);
            }
        }

        if (request.getParameterMap().containsKey("documents") && request.getParameterMap().get("documents") != null) {
            List<Long> documents = StringUtils.asLongList(request.getParameter("documents"));
            // Get the filter list. Duplicates and already associated items will be filtered
            // This will filter the items with same productID selected from the UI.
            documents = removeAlreadyAssociatedItemsFromList(existingItems, documents, CampaignItem.Type.DOCUMENT);
            for (Long documentID : documents) {
                CampaignItem item = new CampaignItem(documentID, CampaignItem.Type.DOCUMENT);
                item.setCampaign(campaign);
                // Can't fetch the MAX Priority based on campaign its should be based on the Category
                //item.setPriority(campaignManager.getMaxItemPriority(campaign.getId()));
                item.setCreatedBy(getUser());
                item.setModifiedBy(getUser());
                item.setCreationDate(new Date());
                item.setModifiedDate(new Date());
                campaignItems.add(item);
            }
        }
        if (request.getParameterMap().containsKey("videos") && request.getParameterMap().get("videos") != null) {
            List<Long> videos = StringUtils.asLongList(request.getParameter("videos"));
            // Get the filter list. Duplicates and already associated items will be filtered
            // This will filter the items with same productID selected from the UI.
            videos = removeAlreadyAssociatedItemsFromList(existingItems, videos, CampaignItem.Type.VIDEO);
            for (Long documentID : videos) {
                CampaignItem item = new CampaignItem(documentID, CampaignItem.Type.VIDEO);
                item.setCampaign(campaign);
                // Can't fetch the MAX Priority based on campaign its should be based on the Category
                //item.setPriority(campaignManager.getMaxItemPriority(campaign.getId()));
                item.setCreatedBy(getUser());
                item.setModifiedBy(getUser());
                item.setCreationDate(new Date());
                item.setModifiedDate(new Date());
                campaignItems.add(item);
            }
        }
        if (request.getParameterMap().containsKey("links") && request.getParameterMap().get("links") != null) {
            List<Long> links = StringUtils.asLongList(request.getParameter("links"));
            // Get the filter list. Duplicates and already associated items will be filtered
            // This will filter the items with same productID selected from the UI.
            links = removeAlreadyAssociatedItemsFromList(existingItems, links, CampaignItem.Type.LINK);
            for (Long documentID : links) {
                CampaignItem item = new CampaignItem(documentID, CampaignItem.Type.LINK);
                item.setCampaign(campaign);
                // Can't fetch the MAX Priority based on campaign its should be based on the Category
                //item.setPriority(campaignManager.getMaxItemPriority(campaign.getId()));
                item.setCreatedBy(getUser());
                item.setModifiedBy(getUser());
                item.setCreationDate(new Date());
                item.setModifiedDate(new Date());
                campaignItems.add(item);
            }
        }
        if (request.getParameterMap().containsKey("images") && request.getParameterMap().get("images") != null) {
            List<Long> images = StringUtils.asLongList(request.getParameter("images"));
            // Get the filter list. Duplicates and already associated items will be filtered
            // This will filter the items with same productID selected from the UI.
            images = removeAlreadyAssociatedItemsFromList(existingItems, images, CampaignItem.Type.IMAGE);
            for (Long documentID : images) {
                CampaignItem item = new CampaignItem(documentID, CampaignItem.Type.IMAGE);
                item.setCampaign(campaign);
                // Can't fetch the MAX Priority based on campaign its should be based on the Category
                //item.setPriority(campaignManager.getMaxItemPriority(campaign.getId()));
                item.setCreatedBy(getUser());
                item.setModifiedBy(getUser());
                item.setCreationDate(new Date());
                item.setModifiedDate(new Date());
                campaignItems.add(item);
            }
        }

        return campaignItems;
    }

}
