package com.dell.acs.web.controller.admin;

import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.web.controller.BaseDellController;
import com.dell.acs.web.crumbs.AdminCrumb;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.dell.acs.web.crumbs.AdminCrumb;

/**
 * Controller which is used for DataImport process monitoring.
 *
 */
@Controller
public class DataImportProcessMonitorController extends BaseDellController {

	private void addCrumbs(HttpServletRequest request, Model model) {
		model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME, 
				new AdminCrumb(request.getContextPath())
						      .home()
							  .render(AdminCrumb.TEXT_MONITOR));  
	}
	
    /**
     * List all the RetailerSites
     * @param model
     */
    @RequestMapping(value = "/admin/monitoring/dataImportProcesses.do", method = RequestMethod.GET)
    public void getAllRetailers(HttpServletRequest request, Model model) {
		Collection<RetailerSite> retailerSiteList = retailerManager.getAllActiveRetailerSites();
		List<RetailerSite> resultList = new ArrayList<RetailerSite>();
		
		// ensure active RetailerSite's parent Retailer is active also...
		for (RetailerSite retailerSite : retailerSiteList) {
			Retailer retailer = retailerSite.getRetailer();
			if ((retailer != null) && (retailer.getActive() == true))
				resultList.add(retailerSite);				
		}
		
        model.addAttribute("retailerSiteList", resultList);
		addCrumbs(request, model);        
    }

    /**
     * Gets all the Data Files for a specific RetailerSite
     * @param id - RetailerSiteId
     * @param model
     */
    @RequestMapping(value = "/admin/monitoring/processPerRetailer.do", method = RequestMethod.POST)
    public void getDataFile(@RequestParam(value = "retailerSiteChoosed", required = false) String id, 
							HttpServletRequest request, Model model) {

        long retailerSiteID = Long.parseLong(id);
        logger.info("Selected retailer site id" + retailerSiteID);
        RetailerSite retailerSite = retailerManager.getRetailerSite(retailerSiteID);
        Collection<DataFile> dataFiles = dataImportManager.getDataFiles(retailerSite);

        String importType = retailerManager.getRetailerSite(retailerSiteID).getProperties()
                .getProperty("retailerSite.dataImportType.name");
        logger.info("Import type==>" + importType);
        model.addAttribute("importType", importType);
        model.addAttribute("dataFiles", dataFiles);
		model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME, 
							new AdminCrumb(request.getContextPath())
									      .monitoring(retailerSite.getSiteName())
										  .render(AdminCrumb.TEXT_MONITOR));  
    }

    /**
     * Displays all the Active Feed jobs which are running on the Feed Server.
     * @param model
     */
    @RequestMapping(value = "/admin/monitoring/activeProcesses", method = RequestMethod.GET)
    public void getActiveDataImportProcesses(HttpServletRequest request, Model model) {
        Collection<DataFile> dataFiles = dataImportManager.getAllDataFilesInProcessingStatus();
        if(dataFiles != null)
            logger.debug("Total active data imports in the system ::: "+dataFiles.size());

        model.addAttribute("dataFiles",dataFiles);
		addCrumbs(request, model);     
    }

}