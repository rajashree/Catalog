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

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

@Controller
public final class ManageController extends BaseDellController {

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
    

    @RequestMapping(value = "admin/campaign/manage", method = RequestMethod.GET)
    public ModelAndView manageCategories(HttpServletRequest request, Model model, 
    									 @RequestParam Long campaignID) 
    											 throws CampaignNotFoundException {
        boolean redirect = false;
        Campaign campaign = campaignManager.getCampaign(campaignID, false);
        List<CampaignItemBean> products = new ArrayList<CampaignItemBean>();
        List<CampaignItemBean> events = new ArrayList<CampaignItemBean>();
        List<CampaignItemBean> documents = new ArrayList<CampaignItemBean>();

        for (CampaignItemData data : campaignManager.getCampaignItems(campaignID)) {
            if (data.getItemType().equalsIgnoreCase(CampaignItem.Type.PRODUCT.name())) {
                products.add(new CampaignItemBean(data));
            } else if (data.getItemType().equalsIgnoreCase(CampaignItem.Type.EVENT.name())) {
                events.add(new CampaignItemBean(data));
            } else if (data.getItemType().equalsIgnoreCase(CampaignItem.Type.DOCUMENT.name())) {
                documents.add(new CampaignItemBean(data));
            }

        }
        if (campaign == null) {
            redirect = true;
        } else {
            model.addAttribute("campaign", campaign);
            model.addAttribute("items", products);
            model.addAttribute("events", events);
            model.addAttribute("documents", documents);
        }
        
//		addCrumbsForManageItems(request, model, AdminCrumb.TEXT_MANAGECATEGORIES, campaign);
        return new ModelAndView();
    }
    
    private Comparator<CampaignCategory> comparator = new Comparator<CampaignCategory>() {
    	
    	private Long parent(CampaignCategory cc) {
    		CampaignCategory parent = cc.getParent();
    		return (parent == null ? -1L : parent.getId());
    	}
    	
    	private int position(CampaignCategory cc) {
    		return cc.getPosition();
    	}
    	
        @Override
        public int compare(CampaignCategory cc1, CampaignCategory cc2) {
        	Long idCompare = parent(cc1) - parent(cc2);
        	if (idCompare > 0) 
        		return 1;
        	else if (idCompare < 0)
        		return -1;
 
        	return (position(cc1) - position(cc2));
        }
    };
    
    static class Conversion {
    	
    	static final String ITEM_PARENT_TYPE = "category";
    	
    	static private JSONObject common(String type,
	    							     String title,
	    							     Long id) {
	    		
    		JSONObject result = new JSONObject();
    		
            result.accumulate("type",  type);
            result.accumulate("title", title);
            result.accumulate("id", id);
            
            return result;	    		
    	}
    	
    	static private JSONObject product(CampaignItem campaignItem,
    							   		  Long parentID) {
    		if (campaignItem.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.PRODUCT.name())) {
    			Product product = campaignItem.getProduct();
    	        if (product != null) 
    	        	return common("product", product.getTitle(), product.getId());
    		}
    		return null;
    	}
    	
    	static private JSONObject event(CampaignItem campaignItem,
				   				 		Long parentID) {
    		
    		if (campaignItem.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.EVENT.name())) {
	            Event event = campaignItem.getEvent();
	            if (event != null) 
	            	return common("event", event.getName(), event.getId());
	        }
    		return null;
    	}
    	
    	static private JSONObject document(CampaignItem campaignItem,
					   					   Long parentID) {

    		if (campaignItem.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.DOCUMENT.name())) {
                Document document = campaignItem.getDocument();
                if(document != null) 
                	return common("document", document.getName(), document.getId());
    		}
    		return null;
    	}
    	
    	static public JSONObject convertItem(CampaignItem campaignItem,
    							  		 	 Long parentID) {
    		JSONObject result;
    		
    		result = product(campaignItem, parentID);
    		if (result == null)
    			result = event(campaignItem, parentID);
    		if (result == null)
    			result = document(campaignItem, parentID);
    		
    		return result;
    	}
    
    	static public JSONObject convertCategory(CampaignCategory category) {
    		return common("container", category.getName(), category.getId());
    	}    	
    } 	    
    
    private JSONObject convertCategory(CampaignCategory category) {
        JSONObject result = Conversion.convertCategory(category);
        JSONArray children = new JSONArray();
        
        // handle items
        if (category.getItems().size() > 0) {
            LinkedList<CampaignItem> sortedItems = new LinkedList<CampaignItem>(category.getItems());
            PropertyComparator.sort(sortedItems, new MutableSortDefinition("priority", false, true));
                                
            for (CampaignItem campaignItem : sortedItems) {
                JSONObject item = Conversion.convertItem(campaignItem, category.getId());
                if (item != null)
                    children.add(item);
            }
        }
        
        // handle descendant categories
        if (category.getChildren().size() > 0) {
            // Sort by position
            LinkedList<CampaignCategory> orderedChildren = new LinkedList<CampaignCategory>(category.getChildren());
            Collections.sort(orderedChildren, this.comparator);
           
            for (CampaignCategory childCategory : orderedChildren) 
                children.add(convertCategory(childCategory));
        }               
        
        result.accumulate("children", children);
        return result;
    }
    
    @RequestMapping(value = "/admin/campaign/manage/tree.json", method = RequestMethod.GET)
    public ResponseEntity<String> getCampaignCategories(@RequestParam Long campaignID) 
            throws CampaignNotFoundException, CampaignItemNotFoundException {
        
        Campaign campaign = campaignManager.getCampaign(campaignID);
        List<CampaignCategory> categories = new ArrayList<CampaignCategory>(campaign.getCategories());
        Collections.sort(categories, this.comparator);
                    
        JSONArray tree = new JSONArray();
        for (CampaignCategory childCategory : categories) 
            tree.add(convertCategory(childCategory));

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(tree.toString(), responseHeaders, HttpStatus.OK);
    }
}