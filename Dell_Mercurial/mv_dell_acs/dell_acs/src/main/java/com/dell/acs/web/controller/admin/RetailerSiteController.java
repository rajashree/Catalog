/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.controller.admin;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.managers.DataFilesDownloadCache;
import com.dell.acs.managers.DataFilesDownloadManager;
import com.dell.acs.managers.RetailerManager;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.web.controller.BaseDellController;
import com.dell.acs.web.crumbs.AdminCrumb;
import com.dell.acs.web.ws.v1.MerchantService;
import com.dell.acs.web.ws.v1.beans.WSProduct;
import com.sourcen.core.util.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;

/**
 @author Navin Raj Kumar G.S.
 @author $LastChangedBy: sameeks $
 @version $Revision: 3635 $, $Date:: 2012-06-26 13:05:12#$ */

@Controller
public final class RetailerSiteController extends BaseDellController {

    @Autowired
    private MerchantService merchantService;

    private void addCrumbs(HttpServletRequest request,
                           Model model,
                           String crumbText,
                           Retailer retailer,
                           RetailerSite retailerSite) {

        if (retailer == null) {
            retailer = retailerSite.getRetailer();
        }
        model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME,
                new AdminCrumb(request.getContextPath())
                        .retailerSite(retailer.getName(),
                                retailer.getId(),
                                retailerSite.getSiteName())
                        .render(crumbText));
    }

    @RequestMapping(value = AdminCrumb.URL_RETAILERSITE_LIST, method = RequestMethod.GET)
    public void get(@RequestParam Long retailerId, HttpServletRequest request, Model model) {
        Retailer retailer = retailerManager.getRetailer(retailerId);
        if (retailer != null) {
            Collection<RetailerSite> retailerSites = retailerManager.getRetailerSites(retailerId),
                    retailerActiveSites = new ArrayList<RetailerSite>(),
                    retailerInactiveSites = new ArrayList<RetailerSite>();

            // sort retailerSites by active
            for (RetailerSite retailerSite : retailerSites) {
                if (retailerSite.getActive() == true) {
                    retailerActiveSites.add(retailerSite);
                } else {
                    retailerInactiveSites.add(retailerSite);
                }
            }

            model.addAttribute("retailer", retailer);
            model.addAttribute("retailerActiveSites", retailerActiveSites);
            model.addAttribute("retailerInactiveSites", retailerInactiveSites);
            model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME,
                    new AdminCrumb(request.getContextPath())
                            .retailer(retailer.getName())
                            .render(AdminCrumb.TEXT_RETAILERSITE));
        }
    }

    @RequestMapping(value = AdminCrumb.URL_RETAILERSITE_EDIT, method = RequestMethod.GET)
    public void edit(@RequestParam Long id, HttpServletRequest request, Model model) {
        RetailerSite retailerSite = retailerManager.getRetailerSite(id);
        if (retailerSite != null) {
            Integer trackID = retailerSite.getProperties()
                    .getIntegerProperty(RetailerManager.RETAILER_SITE_PIXEL_TRACKER_ID_PROPERTY_KEY);
            Integer dataImportType = retailerSite.getProperties()
                    .getIntegerProperty(RetailerManager.RETAILER_SITE_DATA_IMPORT_TYPE_ID_PROPERTY_KEY);
            if (trackID == null) {
                trackID = EntityConstants.EnumPixelTracker.LINKTRACKER.intValue();
            }
            if (dataImportType == null) {
                dataImportType = EntityConstants.EnumDataImport.FICSTAR.getValue();
            }
            retailerSite.setEnumDataImport(EntityConstants.EnumDataImport.valueOf(dataImportType));
            retailerSite.setEnumPixelTracker(EntityConstants.EnumPixelTracker.valueOf(trackID));
            model.addAttribute("retailerSite", retailerSite);
            addCrumbs(request, model, AdminCrumb.TEXT_EDIT, null, retailerSite);
        }
    }

    @RequestMapping(value = AdminCrumb.URL_RETAILERSITE_EDIT, method = RequestMethod.POST)
    public ModelAndView edit_submit(HttpServletRequest request, Model model,
                                    @ModelAttribute RetailerSite bean, BindingResult binding) {
        if (bean.getEnumPixelTracker() == null) {
            model.addAttribute("enumPixelTrackerMissing", true);

            String redirectUrl =
                    new AdminCrumb(request.getContextPath())
                            .toAbsolute(AdminCrumb.linkById(AdminCrumb.URL_RETAILERSITE_EDIT, bean.getId()));
            return new ModelAndView(new RedirectView(redirectUrl));
        }

        if (bean.getEnumDataImport() == null) {
            model.addAttribute("enumDataImportMissing", true);

            String redirectUrl =
                    new AdminCrumb(request.getContextPath())
                            .toAbsolute(AdminCrumb.linkById(AdminCrumb.URL_RETAILERSITE_EDIT, bean.getId()));
            return new ModelAndView(new RedirectView(redirectUrl));
        }

        // Validation for site name(should contain only text and must not be null,empty, blank) , url and logo-url(should contain text in format of http,https,www).

        if (!StringUtils.isEmpty(bean.getSiteName())) {
            if (StringUtils.isEmpty(bean.getSiteName().trim())) {
                binding.addError(new FieldError("retailerSite", "siteName", null, true,
                        new String[]{"NoSpace.name"}, null, ""));
            }/* else {
                // Alpha numeric regex: [a-zA-Z0-9_]*
                if (!Pattern.matches("[a-zA-Z0-9_]*", bean.getSiteName())) {
                    binding.addError(new FieldError("retailerSite", "siteName", null, true,
                            new String[]{"regex.string.pattern"}, null, ""));
                }
            }*/
        } else {
            binding.addError(new FieldError("retailerSite", "siteName", null, true,
                    new String[]{"NotEmpty.name"}, null, ""));
        }
        if (!StringUtils.isEmpty(bean.getSiteUrl())) {
            if (!Pattern.matches(
                    "(http?|https|www)[:]*[.]*[//]*[www]*[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]",
                    bean.getSiteUrl())) {

                binding.addError(new FieldError("retailerSite", "siteUrl", null, true,
                        new String[]{"Url.format.error"}, null, ""));


            }
        } else {
            binding.addError(new FieldError("retailerSite", "siteUrl", null, true,
                    new String[]{"NotEmpty.url"}, null, ""));
        }

        if (!StringUtils.isEmpty(bean.getLogoUri())) {
            if (!Pattern.matches(
                    "(http?|https|www)[:]*[.]*[//]*[www]*[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]",
                    bean.getLogoUri())) {

                binding.addError(new FieldError("retailerSite", "logoUri", null, true,
                        new String[]{"Url.format.error"}, null, ""));


            }
        } else {
            binding.addError(new FieldError("retailerSite", "logoUri", null, true,
                    new String[]{"NotEmpty.url"}, null, ""));
        }

        RetailerSite retailerSite = retailerManager.getRetailerSite(bean.getId());


        if (!binding.hasErrors()) {
            retailerSite.setSiteName(bean.getSiteName());
            retailerSite.setSiteUrl(bean.getSiteUrl());
            retailerSite.setLogoUri(bean.getLogoUri());
            retailerSite.setActive(bean.getActive());
            retailerManager.updateRetailerSite(retailerSite);
            model.addAttribute("retailerSite", retailerSite);
            //PixelTracker
            retailerSite.getProperties().setProperty(RetailerManager.RETAILER_SITE_PIXEL_TRACKER_ID_PROPERTY_KEY,
                    bean.getEnumPixelTracker().intValue());
            retailerSite.getProperties().setProperty(RetailerManager.RETAILER_SITE_PIXEL_TRACKER_NAME_PROPERTY_KEY,
                    bean.getEnumPixelTracker().getTrackerName());
            //DataImportType
            retailerSite.getProperties().setProperty(RetailerManager.RETAILER_SITE_DATA_IMPORT_TYPE_ID_PROPERTY_KEY,
                    bean.getEnumDataImport().getValue());
            retailerSite.getProperties().setProperty(RetailerManager.RETAILER_SITE_DATA_IMPORT_TYPE_NAME_PROPERTY_KEY,
                    bean.getEnumDataImport().getDataImportType());

            retailerManager.updateRetailerSite(retailerSite);

            String redirectUrl = new AdminCrumb(request.getContextPath())
                    .toAbsolute(AdminCrumb.linkById(AdminCrumb.URL_RETAILERSITE_EDIT, bean.getId()));
            return new ModelAndView(new RedirectView(redirectUrl));
        }

        addCrumbs(request, model, AdminCrumb.TEXT_EDIT, null, retailerSite);
        String viewName = new AdminCrumb(request.getContextPath())
                .toView(AdminCrumb.URL_RETAILERSITE_EDIT);
        return new ModelAndView(viewName);
    }


    @RequestMapping(value = AdminCrumb.URL_RETAILERSITE_ADD, method = RequestMethod.GET)
    public void add(HttpServletRequest request, Model model, @RequestParam Long retailerId) {
        Retailer retailer = retailerManager.getRetailer(retailerId);
        RetailerSite retailerSite = new RetailerSite();
        retailerSite.setActive(true);
        retailerSite.setRetailer(retailer);
        model.addAttribute("retailer", retailer);
        model.addAttribute("retailerSite", retailerSite);
        addCrumbs(request, model, AdminCrumb.TEXT_ADD, retailer, retailerSite);
    }


    @RequestMapping(value = AdminCrumb.URL_RETAILERSITE_ADD, method = RequestMethod.POST)
    public ModelAndView add_submit(HttpServletRequest request, Model model,
                                   @ModelAttribute RetailerSite retailerSite, BindingResult binding) {

        // Validation for site name(should contain only text and must not be null,empty, blank) , url and logo-url(should contain text in format of http,https,www).

        if (!StringUtils.isEmpty(retailerSite.getSiteName())) {
            if (StringUtils.isEmpty(retailerSite.getSiteName().trim())) {
                binding.addError(new FieldError("retailerSite", "siteName", null, true,
                        new String[]{"NoSpace.name"}, null, ""));
            } /*else {
                // Alpha numeric regex [a-zA-Z0-9_]*
                if (!Pattern.matches("[a-zA-Z0-9_]*", retailerSite.getSiteName())) {
                    binding.addError(new FieldError("retailerSite", "siteName", null, true,
                            new String[]{"regex.string.pattern"}, null, ""));
                }
            }*/
        } else {
            binding.addError(new FieldError("retailerSite", "siteName", null, true,
                    new String[]{"NotEmpty.name"}, null, ""));
        }

        if (!StringUtils.isEmpty(retailerSite.getSiteUrl())) {
            if (!Pattern.matches(
                    "(http?|https|www)[:]*[.]*[//]*[www]*[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]",
                    retailerSite.getSiteUrl())) {

                binding.addError(new FieldError("retailerSite", "siteUrl", null, true,
                        new String[]{"Url.format.error"}, null, ""));


            }
        } else {
            binding.addError(new FieldError("retailerSite", "siteUrl", null, true,
                    new String[]{"NotEmpty.url"}, null, ""));
        }

        if (!StringUtils.isEmpty(retailerSite.getLogoUri())) {
            if (!Pattern.matches(
                    "(http?|https|www)[:]*[.]*[//]*[www]*[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]",
                    retailerSite.getLogoUri())) {

                binding.addError(new FieldError("retailerSite", "logoUri", null, true,
                        new String[]{"Url.format.error"}, null, ""));


            }
        } else {
            binding.addError(new FieldError("retailerSite", "logoUri", null, true,
                    new String[]{"NotEmpty.url"}, null, ""));
        }
        if (!binding.hasErrors()) {
            Collection<String> retailerSites = retailerManager.getSiteNames();
            for (String site : retailerSites) {
                if (retailerSite.getSiteName().equalsIgnoreCase(site)) {
                    model.addAttribute("siteName", retailerSite.getSiteName());
                    model.addAttribute("duplicate", true);

                    String redirectUrl = new AdminCrumb(request.getContextPath())
                            .toAbsolute(AdminCrumb.linkByRetailerId(AdminCrumb.URL_RETAILERSITE_ADD,
                                    retailerSite.getRetailer().getId()));
                    return new ModelAndView(new RedirectView(redirectUrl));
                }
            }

            if (retailerSite.getEnumPixelTracker() == null) {
                model.addAttribute("enumPixelTrackerMissing", true);

                String redirectUrl = new AdminCrumb(request.getContextPath())
                        .toAbsolute(AdminCrumb.linkByRetailerId(AdminCrumb.URL_RETAILERSITE_ADD,
                                retailerSite.getRetailer().getId()));
                return new ModelAndView(new RedirectView(redirectUrl));
            }

            if (retailerSite.getEnumDataImport() == null) {
                model.addAttribute("enumDataImportMissing", true);

                String redirectUrl = new AdminCrumb(request.getContextPath())
                        .toAbsolute(AdminCrumb.linkByRetailerId(AdminCrumb.URL_RETAILERSITE_ADD,
                                retailerSite.getRetailer().getId()));
                return new ModelAndView(new RedirectView(redirectUrl));
            }


            retailerSite = retailerManager.createRetailerSite(retailerSite);
            Retailer retailer = retailerManager.getRetailer(retailerSite.getRetailer().getId());
            model.addAttribute("retailer", retailer);
            model.addAttribute("retailerSite", retailerSite);

            //Save the PixelTracking ID against RetailerSite extended property
            logger.info("EnumPixelTracker for the retailer   " + retailerSite.getEnumPixelTracker().getTrackerName());
            retailerSite.getProperties().setProperty(RetailerManager.RETAILER_SITE_PIXEL_TRACKER_ID_PROPERTY_KEY,
                    retailerSite.getEnumPixelTracker().intValue());
            retailerSite.getProperties().setProperty(RetailerManager.RETAILER_SITE_PIXEL_TRACKER_NAME_PROPERTY_KEY,
                    retailerSite.getEnumPixelTracker().getTrackerName());

            //Save the DataImportType against RetailerSite extended property
            logger.info("EnumDataImporType for the retailer   " + retailerSite.getEnumDataImport().getDataImportType());
            retailerSite.getProperties().setProperty(RetailerManager.RETAILER_SITE_DATA_IMPORT_TYPE_ID_PROPERTY_KEY,
                    retailerSite.getEnumDataImport().getValue());
            retailerSite.getProperties().setProperty(RetailerManager.RETAILER_SITE_DATA_IMPORT_TYPE_NAME_PROPERTY_KEY,
                    retailerSite.getEnumDataImport().getDataImportType());

            retailerManager.updateRetailerSite(retailerSite);

            String redirectUrl = new AdminCrumb(request.getContextPath())
                    .toAbsolute(AdminCrumb.linkByRetailerId(AdminCrumb.URL_RETAILERSITE_LIST, retailer.getId()));
            return new ModelAndView(new RedirectView(redirectUrl));
        }

        addCrumbs(request, model, AdminCrumb.TEXT_ADD, null, retailerSite);

        String viewName = new AdminCrumb(request.getContextPath())
                .toView(AdminCrumb.URL_RETAILERSITE_ADD);
        return new ModelAndView(viewName);
    }

    @RequestMapping(value = AdminCrumb.URL_RETAILERSITE_DATAIMPORT, method = RequestMethod.GET)
    public void import_file(HttpServletRequest request, Model model, @RequestParam Long id) {
        RetailerSite retailerSite = retailerManager.getRetailerSite(id);
        Retailer retailer = retailerSite.getRetailer();

        model.addAttribute("retailerSite", retailerSite);
        Collection<DataFile> dataFiles = dataImportManager.getDataFiles(retailerSite);
        Collection<String> downloadedFile = dataImportManager.getDownLoadedDataFiles(retailerSite, 5);
        model.addAttribute("dataFiles", dataFiles);
        model.addAttribute("downloadedFile", downloadedFile);

        DataFileUploadItem item = new DataFileUploadItem();
        item.setRetailerSiteId(id);
        model.addAttribute("dataFileUploadItem", item);
        model.addAttribute("downloadedFileList", DataFilesDownloadCache.getDownloadFile(retailerSite.getSiteName()));
        model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME,
                new AdminCrumb(request.getContextPath())
                        .retailerSite(retailer.getName(), retailer.getId(), retailerSite.getSiteName())
                        .render(AdminCrumb.TEXT_DATAIMPORT));
    }

    @RequestMapping(value = AdminCrumb.URL_RETAILERSITE_DATAIMPORT, method = RequestMethod.POST)
    public ModelAndView import_file(HttpServletRequest request, Model model,
                                    @ModelAttribute DataFileUploadItem bean, BindingResult binding) {
        try {
            if (!binding.hasErrors() && bean.getDataFile() != null) {
                RetailerSite retailerSite = retailerManager.getRetailerSite(bean.getRetailerSiteId());
                model.addAttribute("retailerSite", retailerSite);
                Collection<DataFile> dataFiles = dataImportManager.getDataFiles(retailerSite);

                File tempFile = null;
                if (FilenameUtils.isExtension(bean.getDataFile().getOriginalFilename(), "gz")) {
                    tempFile =
                            dataFilesDownloadManager.generateTempFile(bean.getDataFile().getOriginalFilename(), false);
                } else {
                    tempFile = dataFilesDownloadManager.generateTempFile(bean.getDataFile().getOriginalFilename());
                }
                bean.getDataFile().transferTo(tempFile);
                Collection<DataFile> addedDataFiles = dataFilesDownloadManager.addFileIntoBatch(retailerSite,
                        bean.getDataFile().getOriginalFilename(), DataFilesDownloadManager.Source.USER_UPLOAD,
                        tempFile);
                dataFiles.addAll(addedDataFiles);
                model.addAttribute("dataFiles", dataFiles);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        String redirectUrl = new AdminCrumb(request.getContextPath())
                .toAbsolute(AdminCrumb.linkById(AdminCrumb.URL_RETAILERSITE_DATAIMPORT, bean.getRetailerSiteId()));
        return new ModelAndView(new RedirectView(redirectUrl));
    }


    @RequestMapping(value = AdminCrumb.URL_RETAILERSITE_SITEITEMS, method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView getRetailerSiteItems(
            @RequestParam(required = true) String merchantName, HttpServletRequest request, Model model) {
        ModelAndView mv = new ModelAndView();

        model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME,
                new AdminCrumb(request.getContextPath())
                        .render("no crumbs"));

        //Changed implementation to get all products(enabled/disabled)
        Collection<WSProduct> products = merchantService.getAllPagedProductsByMerchant(merchantName, -1, 0);
        if ((products.size() == 0 || products.size() == 1)) {
            // If the retailer doesn't exists then we add the error as a Collection item in the repository
            // so we will just log the error to differentiate between the ERROR and NO PRODUCTS
            if (products.toArray().length > 0 &&
                    products.toArray()[0] instanceof String
                    && StringUtils.contains((String) (products.toArray()[0]), "retailer site not found")) {
                logger.info("Site not found");
            } else {
                logger.info("Retailer site has NO products associated.");
            }
            mv.addObject("products", Collections.emptyList());
            mv.addObject("merchantName", merchantName);
        } else {
            mv.addObject("products", products);
        }
        return mv;
    }
}
