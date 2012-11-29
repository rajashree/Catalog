package com.dell.acs.web.controller.admin;

import com.dell.acs.CampaignNotFoundException;
import com.dell.acs.content.EntityConstants;
import com.dell.acs.managers.CampaignManager;
import com.dell.acs.managers.RetailerManager;
import com.dell.acs.managers.model.CampaignItemData;
import com.dell.acs.persistence.domain.Campaign;
import com.dell.acs.persistence.domain.CampaignItem;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.web.controller.BaseDellController;
import com.dell.acs.web.controller.formbeans.CampaignItemBean;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.collections.PropertiesProvider;
import com.sourcen.core.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.dell.acs.web.crumbs.AdminCrumb;

/**
 * Created by IntelliJ IDEA.
 * User: vivek
 * Date: 6/2/12
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("all")
@Controller
public class PixelTrackingController extends BaseDellController {

    @Autowired
    private RetailerManager retailerManager;

    @Autowired
    private CampaignManager campaignManager;
    
	private void addCrumbs(HttpServletRequest request, 
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
    

    @RequestMapping(value = "admin/tracking/list.do", method = RequestMethod.GET)
    public ModelAndView getAvailablePixelTracking(Model model) {
        logger.info("Load all the pixel tracking systems currently supported");
        model.addAttribute("pixelTrackers", EntityConstants.EnumPixelTracker.values());
        return new ModelAndView();
    }


    @RequestMapping(value = AdminCrumb.URL_TRACKING_MANAGETRACKING, method = RequestMethod.GET)
    public ModelAndView manageItems(Model model, 
    								@RequestParam Long siteID, 
    								@RequestParam Long campaignID) 
    										throws CampaignNotFoundException {

        String pixelTrackerType = null;
        RetailerSite retailerSite = retailerManager.getRetailerSite(siteID);
        PropertiesProvider retailerProps = retailerSite.getProperties();
        if (retailerProps.hasProperty(RetailerManager.RETAILER_SITE_PIXEL_TRACKER_NAME_PROPERTY_KEY)) {
            pixelTrackerType = retailerProps.getProperty(RetailerManager.RETAILER_SITE_PIXEL_TRACKER_NAME_PROPERTY_KEY);
            model.addAttribute("pixelTracker", pixelTrackerType);
            logger.info(" Pixel Tracking Type :::   " + pixelTrackerType);
            if (!StringUtils.isEmpty(pixelTrackerType)) {
            	String redirectUrl = null;
            	
                if (pixelTrackerType.equalsIgnoreCase("linktracker")) {
                    redirectUrl = "manage-linktracker.do?siteID=" + siteID + "&campaignID=" + campaignID;
                } else if (pixelTrackerType.equalsIgnoreCase("marketvine")) {
                    redirectUrl = "manage-marketvine.do?siteID=" + siteID + "&campaignID=" + campaignID;
                } else if (pixelTrackerType.equalsIgnoreCase("hasoffers")) {
                    redirectUrl = "manage-hasoffers.do?siteID=" + siteID + "&campaignID=" + campaignID;
                } else if (pixelTrackerType.equalsIgnoreCase("integratedwithfeed")) {
                    redirectUrl = "manage-integrated.do?siteID=" + siteID + "&campaignID=" + campaignID;
                }

                if (redirectUrl != null)
                	return new ModelAndView(new RedirectView(redirectUrl));
            }
        } 
        
        logger.info(" No Pixel Tracking configured for the Retailer Site  " + retailerSite.getSiteName());
        model.addAttribute("pixelTrackerConfigError",true);
        model.addAttribute("siteID",siteID);
        return new ModelAndView();
    }

    @RequestMapping(value = "admin/tracking/manage-linktracker.do", method = RequestMethod.GET)
    public ModelAndView manageLinkTrackerPixel(HttpServletRequest request, Model model, 
    										   @RequestParam Long siteID, @RequestParam Long campaignID) 
    												   throws CampaignNotFoundException {

        logger.info("LinkTracker PixelTracking is chosen ");
        loadCampaign(request, model, siteID, campaignID, AdminCrumb.TEXT_TRACKING_LINKTRACKER);
        return new ModelAndView();
    }


    @RequestMapping(value = "admin/tracking/manage-marketvine.do", method = RequestMethod.GET)
    public ModelAndView manageMarketVinePixel(HttpServletRequest request, Model model, 
    										  @RequestParam Long siteID, @RequestParam Long campaignID) 
    												  throws CampaignNotFoundException {

        logger.info("MarketVine PixelTracking is chosen ");
        loadCampaign(request, model, siteID, campaignID, AdminCrumb.TEXT_TRACKING_MARKETVINE);
        return new ModelAndView();
    }


    @RequestMapping(value = "admin/tracking/manage-hasoffers.do", method = RequestMethod.GET)
    public ModelAndView manageHasOffersPixel(HttpServletRequest request, Model model, 
    										 @RequestParam Long siteID, @RequestParam Long campaignID) 
    												 throws CampaignNotFoundException {

        logger.info("HasOffers Pixel integration coming soon :)");
        loadCampaign(request, model, siteID, campaignID, AdminCrumb.TEXT_TRACKING_HASOFFERS);

        return new ModelAndView();
    }

    @RequestMapping(value = "admin/tracking/manage-integrated.do", method = RequestMethod.GET)
    public ModelAndView manageIntegrated(HttpServletRequest request, Model model, 
    									 @RequestParam Long siteID, @RequestParam Long campaignID) 
    											 throws CampaignNotFoundException {

        logger.info("Integrated with feed. Nothing to be customized");

        return new ModelAndView();
    }

    protected void loadCampaign(HttpServletRequest request, Model model, 
    							Long siteID, Long campaignID,
    							String crumbText) 
    									throws CampaignNotFoundException {
        Campaign campaign = campaignManager.getCampaign(campaignID);
        Collection<CampaignItemData> dataItems = campaignManager.getCampaignItems(campaignID);
        Collection<CampaignItemBean> items = new ArrayList<CampaignItemBean>();
        for(CampaignItemData data : dataItems) {
        	items.add(new CampaignItemBean(data));
        }

        List<CampaignItemBean> products = new ArrayList<CampaignItemBean>();
        List<CampaignItemBean> events = new ArrayList<CampaignItemBean>();
        List<CampaignItemBean> documents = new ArrayList<CampaignItemBean>();
        if (campaign == null) {
            String redirectUrl = "campaign/list.do?siteID=" + siteID;
            new ModelAndView(new RedirectView(redirectUrl));
        } 

    
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
            }
        }
        model.addAttribute("products", products);
        model.addAttribute("events", events);
        model.addAttribute("documents", documents);
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
        
        addCrumbs(request, model, crumbText, campaign);
    }
}
