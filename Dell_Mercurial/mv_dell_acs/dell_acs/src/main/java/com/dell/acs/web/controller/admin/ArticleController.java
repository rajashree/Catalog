package com.dell.acs.web.controller.admin;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.managers.DocumentManager;
import com.dell.acs.managers.TagManager;
import com.dell.acs.persistence.domain.Document;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.validator.spring.CustomDefaultBindingErrorProcessor;
import com.dell.acs.web.controller.BaseDellController;
import com.sourcen.core.util.WebUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.dell.acs.web.crumbs.AdminCrumb;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.text.SimpleDateFormat;


/**
 * @author Sandeep Heggi
 * @author $LastChangedBy: Sandeep $
 */

@Controller
public class ArticleController extends BaseDellController {

    private void addCrumbs(HttpServletRequest request,
                           Model model,
                           String crumbText,
                           RetailerSite retailerSite) {

        Retailer retailer = retailerSite.getRetailer();
        model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME,
                new AdminCrumb(request.getContextPath())
                        .libraryArticle(retailer.getName(),
                                retailer.getId(),
                                retailerSite.getSiteName(), retailerSite.getId())
                        .render(crumbText));
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

    @RequestMapping(value = AdminCrumb.URL_ARTICLE_ADD, method = RequestMethod.GET)
    public void initArticleAddForm(HttpServletRequest request, Model model,
                                   @RequestParam(value = "siteID", required = true) Long siteID) {
        Document article = new Document();
        //Get the retailer site to add the article for this particular retailer site
        RetailerSite retailerSite = retailerManager.getRetailerSite(siteID);
        article.setRetailerSite(retailerSite);
        model.addAttribute("article", article);
        addCrumbs(request, model, AdminCrumb.TEXT_ARTICLE_ADD, retailerSite);
    }

    @RequestMapping(value = AdminCrumb.URL_ARTICLE_ADD, method = RequestMethod.POST)
    public ModelAndView saveArticle(HttpServletRequest request, Model model,
                                    @ModelAttribute("article") Document article,
                                    BindingResult bindingResult,
                                    final Errors errors)
            throws EntityExistsException {

        Long retailerSiteID = null;

        if (article != null) {
            retailerSiteID = article.getRetailerSite().getId();
            if (retailerSiteID != null) {
                article.setRetailerSite(retailerManager.getRetailerSite(retailerSiteID));
            }


            //Validation for title and description
            if (!StringUtils.isEmpty(article.getName())) {
                if (StringUtils.isEmpty(article.getName().trim())) {
                    bindingResult.addError(new FieldError("document", "name", null, true,
                            new String[]{"NoSpace.title"}, null, ""));
                }
            } else {
                bindingResult.addError(new FieldError("document", "name", null, true,
                        new String[]{"NotEmpty.title"}, null, ""));
            }
            if (!StringUtils.isEmpty(article.getBody())) {
                if (StringUtils.isEmpty(article.getBody().trim())) {
                    bindingResult.addError(new FieldError("document", "body", null, true,
                            new String[]{"NoSpace.body"}, null, ""));
                }
            } else {
                bindingResult.addError(new FieldError("document", "body", null, true,
                        new String[]{"NotEmpty.body"}, null, ""));
            }
        }
        if (!bindingResult.hasErrors()) {
            //Save the article
            try {
                article = documentManager.saveDocument(article);
            } catch (EntityExistsException e) {
                logger.error(e.getMessage());
            }
            /* Saving Tag for Article, after video object is loaded*/
            String tag= WebUtils.getParameter(request, "TagValue", "");
            if(com.sourcen.core.util.StringUtils.isNotEmpty(tag))
            {
                logger.debug("Saving Tag for ARTICLE "+article.getId()+" is "+tag);
                tagManager.saveTags(tag, EntityConstants.Entities.getByValue("ARTICLE").getId(), article.getId());
            }
            /*end*/
            model.addAttribute("article", article);
            logger.info("Successfully created article - " + article.getName());

            String redirectUrl =
                    new AdminCrumb(request.getContextPath())
                            .toAbsolute(AdminCrumb.linkById(AdminCrumb.URL_ARTICLE_EDIT, article.getId()));
            return new ModelAndView(new RedirectView(redirectUrl));
        }

        model.addAttribute("errors", errors);
        addCrumbs(request, model, AdminCrumb.TEXT_ARTICLE_ADD,
                retailerManager.getRetailerSite(article.getRetailerSite().getId()));

        String viewName = new AdminCrumb(request.getContextPath())
                .toView(AdminCrumb.URL_ARTICLE_ADD);
        return new ModelAndView(viewName);
    }

    @RequestMapping(value = AdminCrumb.URL_ARTICLE_EDIT, method = RequestMethod.GET)
    public void initArticleEditForm(@RequestParam Long id, Model model,
                                    final HttpServletRequest request) throws EntityNotFoundException {

        logger.info("Loading Article with ID :=" + id);
//      https://jira.marketvine.com/browse/CS-450 - Handled the case where edit pages of content
//                types were corrupting the data of other content types
        Document article = documentManager.getDocument(id);
        RetailerSite retailerSite = article.getRetailerSite();
        Integer type = article.getType();
        if (type == EntityConstants.Entities.ARTICLE.getId()) {
            model.addAttribute("article", article);
            model.addAttribute("invalidType", false);
        } else {
            model.addAttribute("invalidType", true);
            String contentTypeName = EntityConstants.Entities.getById(type).getValue();
            model.addAttribute("contentType", contentTypeName);
            logger.warn("User trying to load ARTICLE with ID:= " + article.getId() + " which is of type "
                    + contentTypeName);
        }
            /*Populating existing tags of the article*/
            String tags=this.tagManager.getTagsAsString(EntityConstants.Entities.getByValue("ARTICLE").getId(), article.getId());
            model.addAttribute("tags", tags);
            /*End*/
        addCrumbs(request, model, AdminCrumb.TEXT_ARTICLE_EDIT, retailerSite);
    }


    @RequestMapping(value = AdminCrumb.URL_ARTICLE_EDIT, method = RequestMethod.POST)
    public ModelAndView updateArticle(HttpServletRequest request, Model model,
                                      @ModelAttribute("article") Document article,
                                      BindingResult bindingResult,
                                      final Errors errors)
            throws EntityExistsException, EntityNotFoundException {

        Long retailerSiteID = null;

        if (article != null) {

            retailerSiteID = article.getRetailerSite().getId();
            if (retailerSiteID != null) {
                article.setRetailerSite(retailerManager.getRetailerSite(retailerSiteID));
            }

            /*Updating the Tag of article*/
            String tagsExisting=tagManager.getTagsAsString(EntityConstants.Entities.getByValue("ARTICLE").getId(), article.getId());
            String tags=WebUtils.getParameter(request,"TagValue","");
            if(!tagsExisting.equals(tags))
            {
                logger.debug("Updating Tag for Article "+article.getId()+" is "+tags);
                tagManager.saveTags(tags,EntityConstants.Entities.getByValue("ARTICLE").getId(), article.getId());
            }
            /* End */

            //Validation for the title and description fields
            if (!StringUtils.isEmpty(article.getName())) {
                if (StringUtils.isEmpty(article.getName().trim())) {
                    bindingResult.addError(new FieldError("document", "name", null, true,
                            new String[]{"NoSpace.title"}, null, ""));
                }
            } else {
                bindingResult.addError(new FieldError("document", "name", null, true,
                        new String[]{"NotEmpty.title"}, null, ""));
            }
            if (!StringUtils.isEmpty(article.getBody())) {
                if (StringUtils.isEmpty(article.getBody().trim())) {
                    bindingResult.addError(new FieldError("document", "body", null, true,
                            new String[]{"NoSpace.body"}, null, ""));
                }
            } else {
                bindingResult.addError(new FieldError("document", "body", null, true,
                        new String[]{"NotEmpty.body"}, null, ""));
            }

            Long articleID = article.getId();
            logger.info("Updating the article with id :=" + articleID);

            Document articleObj = documentManager.getDocument(articleID);

            if (!bindingResult.hasErrors()) {

                //Set the new values for article fields
                articleObj.setName(article.getName());
                articleObj.setBody(article.getBody());
                articleObj.setDescription(article.getDescription());
                articleObj.setAuthor(article.getAuthor());
                articleObj.setSource(article.getSource());
                articleObj.setAbstractText(article.getAbstractText());
                articleObj.setPublishDate(article.getPublishDate());
                //Save the article
                documentManager.saveDocument(articleObj);

                String redirectUrl = new AdminCrumb(request.getContextPath())
                        .toAbsolute(AdminCrumb.linkById(AdminCrumb.URL_ARTICLE_EDIT, article.getId()));
                return new ModelAndView(new RedirectView(redirectUrl));
            }

            //Add the errors and redirect to the edit page
            model.addAttribute("errors", errors);
            addCrumbs(request, model, AdminCrumb.TEXT_ARTICLE_EDIT,
                    retailerManager.getRetailerSite(article.getRetailerSite().getId()));
        }
        model.addAttribute("articles", documentManager.getDocuments(retailerSiteID,
                EntityConstants.Entities.ARTICLE.getId(), null));
        String viewName = new AdminCrumb(request.getContextPath())
                .toView(AdminCrumb.URL_ARTICLE_EDIT);
        return new ModelAndView(viewName);
    }

    // MWE - not modified from crumbs
    @RequestMapping(value = "admin/article/delete.json", method = RequestMethod.POST)
    public ModelAndView deleteArticle(@RequestParam final Long id) {

        ModelAndView modelAndView = new ModelAndView();
        try {
            documentManager.deleteDocument(id);
            modelAndView.addObject("success", true);
        } catch (EntityNotFoundException exception) {
            logger.warn("Unable to find the article with ID := " + id);
            modelAndView.addObject("success", false);
            modelAndView.addObject("message", "Unable to find the Article with ID:= " + id);
        }
        return modelAndView;
    }

    @Autowired
    private DocumentManager documentManager;
    @Autowired
    private TagManager tagManager;

}


