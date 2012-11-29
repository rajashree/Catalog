package com.dell.acs.web.controller.admin;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.managers.DocumentManager;
import com.dell.acs.managers.RetailerManager;
import com.dell.acs.managers.TagManager;
import com.dell.acs.persistence.domain.Document;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.validator.spring.CustomDefaultBindingErrorProcessor;
import com.dell.acs.web.controller.BaseDellController;
import com.dell.acs.web.crumbs.AdminCrumb;
import com.sourcen.core.util.Assert;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.WebUtils;
import com.sourcen.core.web.controller.BaseController;
import org.hibernate.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

/**
 * @author Ashish
 * @author $LastChangedBy: Ashish $
 * @version $Revision: 1595 $, $Date:: 7/25/12 3:10 PM#$
 */
@Controller
public class LinkController extends BaseDellController {

    private void addCrumbs(HttpServletRequest request,
                           Model model,
                           String crumbText,
                           RetailerSite retailerSite) {

        Retailer retailer = retailerSite.getRetailer();
        model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME,
                new AdminCrumb(request.getContextPath())
                        .libraryDocument(retailer.getName(),
                                retailer.getId(),
                                retailerSite.getSiteName(), retailerSite.getId())
                        .render(crumbText));
    }

    @Deprecated
    @RequestMapping(value = "/admin/link/checkNameAvailability.do", method = RequestMethod.GET)
    public
    @ResponseBody
    String checkDocumentNameAvailability(
            @RequestParam String name, @RequestParam String status, @RequestParam String id) {
        boolean nameExistenceStatus = false;
        Long linkId = 0L;
        if (!id.equals("null")) {
            linkId = Long.parseLong(id);
        }
        if (status.equals("update")) {
            String updateLinkName = null;
            if (linkId != 0) {
                updateLinkName = documentManager.getDocumentNameByID(linkId, 2004);
                if (!name.equals(updateLinkName)) {
                    try {
                        nameExistenceStatus = documentManager.checkNameExistence(name);
                    } catch (NonUniqueResultException nure) {
                        logger.error("Unique result not found");
                        nameExistenceStatus = true;
                    }
                }
            } else {
                logger.error("Unable to make an ajax call , because there is no document for document id::" + linkId);
            }
        } else {
            try {
                nameExistenceStatus = documentManager.checkNameExistence(name);

            } catch (NonUniqueResultException nure) {
                logger.error("Unique result not found");
                nameExistenceStatus = true;
            }

        }

        if (nameExistenceStatus) {
            return "found";
        } else {
            return "notFound";
        }
    }

    /*
     {@inheritDoc}
      Bind our custom class for default binding error processor.
    */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm");

        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                dateFormat, false));
        binder.setBindingErrorProcessor(new CustomDefaultBindingErrorProcessor());
    }

    @RequestMapping(value = AdminCrumb.URL_LINK_ADD, method = RequestMethod.GET)
    public void addLinkPage(HttpServletRequest request,
                            final Model model,
                            @RequestParam(value = "siteID", required = false) final Long siteID) {

        if (siteID != null) {
            Document document = new Document();
            RetailerSite retailerSite = retailerManager.getRetailerSite(siteID);
            if (retailerSite != null) {
                document.setRetailerSite(retailerSite);
                model.addAttribute("retailerSite", retailerSite);
            } else {
                model.addAttribute("error", "Unable to process the request");
            }
            model.addAttribute("document", document);
            addCrumbs(request, model, AdminCrumb.TEXT_LINK_ADD, retailerSite);
        }

    }


    @RequestMapping(value = AdminCrumb.URL_LINK_ADD, method = RequestMethod.POST)
    public ModelAndView saveLinkPage(final Model model,
                                     final HttpServletRequest request,
                                     @ModelAttribute(value = "document") Document linkContent,
                                     BindingResult bindingResult,
                                     @RequestParam(value = "siteID", required = false) final Long siteID) throws
            EntityExistsException {

        RetailerSite retailerSite = null;
        if (linkContent != null) {
            linkContent.setRetailerSite(retailerManager.getRetailerSite(linkContent.getRetailerSite().getId()));
        }


        // Check the validation for Iamge input fields.
        validateFields(linkContent, bindingResult);


        if (!bindingResult.hasErrors()) {
            logger.info("No Validation error occurs");
            try {
                linkContent = documentManager.saveDocument(linkContent);
            } catch (EntityExistsException e) {
                logger.error("Duplicate Entry for link:=> " + linkContent.getName());
            }
            /*  Saving Tag for Video, after link object is loaded*/
            String tag= WebUtils.getParameter(request, "TagValue", "");
            if(StringUtils.isNotEmpty(tag))
            {
                logger.debug("Saving Tag for documentID "+linkContent.getId()+" is "+tag);
                tagManager.saveTags(tag, EntityConstants.Entities.LINK.getId(), linkContent.getId());
            }

            /*End*/

            String redirectUrl =
                    new AdminCrumb(request.getContextPath())
                            .toAbsolute(
                                    AdminCrumb.linkById(AdminCrumb.URL_LINK_EDIT, linkContent.getId()));
            return new ModelAndView(new RedirectView(redirectUrl));
        }


        logger.info("Validation error occurs");

        addCrumbs(request, model, AdminCrumb.TEXT_LINK_ADD,
                retailerManager.getRetailerSite(linkContent.getRetailerSite().getId()));

        String viewName = new AdminCrumb(request.getContextPath())
                .toView(AdminCrumb.URL_LINK_ADD);

        return new ModelAndView(viewName);
    }


    @RequestMapping(value = AdminCrumb.URL_LINK_EDIT, method = RequestMethod.GET)
    public void editLinkPage(final HttpServletRequest request,
                             Model model,
                             @RequestParam Long id) throws EntityNotFoundException {
        Document linkContent = documentManager.getDocument(id);

        /*Populating existing tags of the Video*/
        String tags=this.tagManager.getTagsAsString(EntityConstants.Entities.LINK.getId(), linkContent.getId());
        model.addAttribute("tags", tags);
        /*End*/

        Assert.notNull(linkContent, "Link Content Not Found");
        RetailerSite retailerSite = linkContent.getRetailerSite();
        Integer type = linkContent.getType();
        if (type == EntityConstants.Entities.LINK.getId()) {
            if (retailerSite != null) {
                model.addAttribute("retailerSite", retailerSite);
            } else {
                model.addAttribute("error", "Unable to process the request");
            }
            model.addAttribute("document", linkContent);
            model.addAttribute("invalidType", false);
        } else {
            model.addAttribute("invalidType", true);
            String contentTypeName = EntityConstants.Entities.getById(type).getValue();
            model.addAttribute("contentType", contentTypeName);
            logger.warn("User trying to load LINK with ID:= " + linkContent.getId() + " which is of type "
                    + contentTypeName);
        }
        addCrumbs(request, model, AdminCrumb.TEXT_LINK_EDIT, retailerSite);

    }

    @RequestMapping(value = AdminCrumb.URL_LINK_EDIT, method = RequestMethod.POST)
    public ModelAndView updateLinkPage(final Model model,
                                       final HttpServletRequest request,
                                       @ModelAttribute(value = "document") Document linkContent,
                                       BindingResult bindingResult,
                                       @RequestParam(value = "id", required = false) Long id) throws
            EntityExistsException, EntityNotFoundException {

        RetailerSite retailerSite = null;
        Assert.notNull(id, "No Link Record found for link id:=>" + id);
        logger.info("Link Record found");
        Document dbLinkContent = documentManager.getDocument(id);

        // Check the validation for Iamge input fields.
        validateFields(linkContent, bindingResult);

        /*Updating the Tag of link*/
        String tagsExisting=tagManager.getTagsAsString(EntityConstants.Entities.LINK.getId(), dbLinkContent.getId());
        String tags=WebUtils.getParameter(request,"TagValue","");
        if(!tagsExisting.equals(tags))
        {
            logger.debug("Updating Tag for LINK "+dbLinkContent.getId()+" is "+tags);
            tagManager.saveTags(tags,EntityConstants.Entities.LINK.getId(), dbLinkContent.getId());
        }
        /* End */

        if (!bindingResult.hasErrors()) {
            logger.info("No Validation error occurs");
            dbLinkContent.setName(linkContent.getName());
            dbLinkContent.setDescription(linkContent.getDescription());
            dbLinkContent.setUrl(linkContent.getUrl());
            dbLinkContent.setAuthor(linkContent.getAuthor());
            dbLinkContent.setSource(linkContent.getSource());
            dbLinkContent.setAbstractText(linkContent.getAbstractText());
            dbLinkContent.setPublishDate(linkContent.getPublishDate());

            linkContent = documentManager.saveDocument(dbLinkContent);
            String redirectUrl =
                    new AdminCrumb(request.getContextPath())
                            .toAbsolute(
                                    AdminCrumb.linkById(AdminCrumb.URL_LINK_EDIT, linkContent.getId()));

            return new ModelAndView(new RedirectView(redirectUrl));
        }

        logger.info("Validation error occurs");
        linkContent.setName(dbLinkContent.getName());
        linkContent.setDescription(dbLinkContent.getDescription());
        linkContent.setUrl(dbLinkContent.getUrl());
        addCrumbs(request, model, AdminCrumb.TEXT_LINK_EDIT,
                retailerManager.getRetailerSite(linkContent.getRetailerSite().getId()));

        String viewName = new AdminCrumb(request.getContextPath())
                .toView(AdminCrumb.URL_LINK_EDIT);

        return new ModelAndView(viewName);
    }

    public void validateFields(Document entity, BindingResult bindingResult) {


        if (!StringUtils.isEmpty(entity.getName())) {
            if (StringUtils.isEmpty(entity.getName().trim())) {
                bindingResult.addError(new FieldError("document", "name", null, true,
                        new String[]{"NoSpace.name"}, null, ""));
            }
        } else {
            bindingResult.addError(new FieldError("document", "name", null, true,
                    new String[]{"NotEmpty.title"}, null, ""));
        }

        if (StringUtils.isEmpty(entity.getUrl())) {
            bindingResult.addError(new FieldError("document", "url", null, true,
                    new String[]{"NotEmpty.url"}, null, ""));
        } else

        {
            if (StringUtils.isEmpty(entity.getUrl().trim())) {
                bindingResult.addError(new FieldError("document", "url", null, true,
                        new String[]{"Url.format.error"}, null, ""));
            } else {
                if (!Pattern.matches(
                        "(http?|https|www)[:]*[.]*[//]*[www]*[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]",
                        entity.getUrl())) {

                    bindingResult.addError(new FieldError("document", "url", null, true,
                            new String[]{"Url.format.error"}, null, ""));


                }
            }
        }/* else {
            bindingResult.addError(new FieldError("document", "url", null, true,
                    new String[]{"NotEmpty.url"}, null, "Link can't be empty"));
        }*/

    }


    @Autowired
    private DocumentManager documentManager;

    public void setDocumentManager(final DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    @Autowired
    private RetailerManager retailerManager;

    public void setRetailerManager(final RetailerManager retailerManager) {
        this.retailerManager = retailerManager;
    }
    @Autowired
    private TagManager tagManager;


}
