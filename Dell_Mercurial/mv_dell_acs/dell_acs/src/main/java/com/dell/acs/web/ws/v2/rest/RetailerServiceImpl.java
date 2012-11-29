package com.dell.acs.web.ws.v2.rest;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.managers.RetailerManager;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.web.ws.v2.RetailerService;
import com.sourcen.core.util.Assert;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.collections.PropertiesProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * Version 2 Implementation of see {@link RetailerService }
 * @author Vivek Kondur
 * @version 1.0
 */
@Component(value = "RetailerServiceV2")
@WebService
@RequestMapping("/api/v2/rest/RetailerService")
public class RetailerServiceImpl extends WebServiceImpl implements RetailerService {

    @Override
    @RequestMapping(value = "createRetailer", method = RequestMethod.POST)
    public Retailer createRetailer(@RequestParam(required = true)String name,@RequestParam(required = true) String description) {

        Assert.hasText(name, "Request parameter 'name' must have text; it must not be null, empty, or blank.");
        Assert.hasText(description, "Request parameter 'description'  must have text; it must not be null, empty, or blank.");

        Retailer newRetailer = new Retailer();
        newRetailer.setName(name);
        newRetailer.setDescription(description);

        return this.retailerManager.createRetailer(newRetailer);
    }

    @Override
    @RequestMapping(value = "createRetailerSite", method = RequestMethod.POST)
    public RetailerSite createRetailerSite(@RequestParam( required = true) Object retailer, @RequestParam( required = true)
                                           String name,@RequestParam( required = true) String url,
                                           @RequestParam( required = true) String logoURL, String pixelTracker, String dataImport) {

        Assert.notNull(retailer, "Request paramter 'retailer' must have either retailer ID or retailer name. ");
        Assert.hasText(name, "Request parameter 'name' must have text; it must not be null, empty, or blank.");
        Assert.hasText(url, "Request parameter 'url' must have text; it must not be null, empty, or blank.");
        Assert.hasText(logoURL, "Request parameter 'logoURL' must have text; it must not be null, empty, or blank.");

        Retailer activeRetailer = this.retailerManager.getActiveRetailer(retailer);
        RetailerSite site = new RetailerSite();
        site.setSiteName(name);
        site.setActive(true);
        site.setSiteUrl(url);
        site.setLogoUri(logoURL);
        site.setRetailer(activeRetailer);
        //Setting the following defaults
        // 1) PixelTracker to LinkTracker
        // 2) DataImportType to Ficstar
        int trackerIntValue = EntityConstants.EnumPixelTracker.LINKTRACKER.intValue();
        String trackerName = EntityConstants.EnumPixelTracker.LINKTRACKER.getTrackerName();
        int importIntValue = EntityConstants.EnumDataImport.FICSTAR.getValue();
        String importName =  EntityConstants.EnumDataImport.FICSTAR.getDataImportType();

        if( StringUtils.isNotEmpty(pixelTracker) && StringUtils.isNotEmpty(dataImport)){

            EntityConstants.EnumPixelTracker pixel = EntityConstants.EnumPixelTracker.valueOf(pixelTracker);
            EntityConstants.EnumDataImport data = EntityConstants.EnumDataImport.valueOf(dataImport);

            trackerIntValue = pixel.intValue();
            trackerName = pixel.getTrackerName();
            importIntValue = data.getValue();
            importName = data.getDataImportType();
        }
        //PixelTracker
        site.getProperties().setProperty(RetailerManager.RETAILER_SITE_PIXEL_TRACKER_ID_PROPERTY_KEY, trackerIntValue );
        site.getProperties().setProperty(RetailerManager.RETAILER_SITE_PIXEL_TRACKER_NAME_PROPERTY_KEY, trackerName);
        //DataImportType
        site.getProperties().setProperty(RetailerManager.RETAILER_SITE_DATA_IMPORT_TYPE_ID_PROPERTY_KEY, importIntValue );
        site.getProperties().setProperty(RetailerManager.RETAILER_SITE_DATA_IMPORT_TYPE_NAME_PROPERTY_KEY, importName );


        return this.retailerManager.createRetailerSite(site);
    }

    @Override
    @RequestMapping("getRetailers")
    public Collection<Retailer> getRetailers() {
        return this.retailerManager.getRetailers();
    }

    @Override
    @RequestMapping("getRetailerSites")
    public Collection<RetailerSite> getRetailerSites(@RequestParam (required = true) Object retailer) {
        Retailer retailerObj = this.retailerManager.getActiveRetailer(retailer);
        return this.retailerManager.getRetailerSites(retailerObj);
    }

    @Override
    @RequestMapping("getRetailer")
    public Retailer getRetailer(@RequestParam( required = true) Object retailer) {
        return this.retailerManager.getActiveRetailer(retailer);
    }

    @Override
    @RequestMapping("getRetailerSite")
    public RetailerSite getRetailerSite(@RequestParam(required = true) Object site) {
        return this.retailerManager.getRetailerSite(site);
    }

    @Override
    @RequestMapping("getPixelTrackers")
    public EntityConstants.EnumPixelTracker[] getPixelTrackers() {
        return EntityConstants.EnumPixelTracker.values();
    }

    @Override
    @RequestMapping("getDataImportTypes")
    public EntityConstants.EnumDataImport[] getDataImportTypes() {
        return EntityConstants.EnumDataImport.values();
    }

    private RetailerManager retailerManager;

    @Autowired
    public void setRetailerManager(RetailerManager retailerManager) {
        this.retailerManager = retailerManager;
    }

    @Override
    public Retailer updateRetailer(Object retailerId, String name, String description) {
        return null;
    }

    @Override
    public RetailerSite updateRetailerSite(Object retailerSite, String name, String url, String logoURL, String pixelTracker, String dataImport) {
        return null;
    }

    @Override
    public RetailerSite deActivateRetailerSite(Object retailerSite) {
        return null;
    }

}
