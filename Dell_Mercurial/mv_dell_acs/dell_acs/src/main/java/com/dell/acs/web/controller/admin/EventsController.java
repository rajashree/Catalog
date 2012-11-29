/*
* Copyright (c) Sourcen Inc. 2004-2012
* All rights reserved.
*/

package com.dell.acs.web.controller.admin;


import com.dell.acs.EventNotFoundException;
import com.dell.acs.content.EntityConstants;
import com.dell.acs.managers.EventManager;
import com.dell.acs.managers.RetailerManager;
import com.dell.acs.managers.TagManager;
import com.dell.acs.persistence.domain.Event;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.validator.spring.CustomDefaultBindingErrorProcessor;
import com.dell.acs.web.controller.BaseDellController;
import com.dell.acs.web.crumbs.AdminCrumb;
import com.sourcen.core.util.Assert;
import com.sourcen.core.util.FileSystem;
import com.sourcen.core.util.FileUtils;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.WebUtils;
import org.hibernate.NonUniqueResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 @author Ashish
 @author $LastChangedBy: ashish $
 @version $Revision: 2741 $, $Date: 2012-04-27 */
@SuppressWarnings("all")
@Controller
public class EventsController extends BaseDellController {

    /**
     Logger variable.
     */
    private Logger logger = LoggerFactory.getLogger(EventsController.class);

    /**
     Ajax Functionality
     */

    /**
     Ajax functionality for deleting or updating the event optional images.

     @param imageKey    , store the optional image key
     @param imageValue, store the optional image value
     @param flag        , tells about the deleting or updating status of ajax call.
     @param id          , store the event id.
     @param request     , request refernce.
     @return
     */
    @RequestMapping(value = "/admin/events/removeOrUpdateOptionalImageKey.do", method = RequestMethod.GET)
    public
    @ResponseBody
    String removeOrUpdateOptionalImageKey(
            @RequestParam(value = "imageKey", required = false) String imageKey,
            @RequestParam(value = "imageValue", required = false) String imageValue,
            @RequestParam(value = "flag", required = false) String flag,
            @RequestParam(value = "id", required = true) Long id,
            @RequestParam(value = "retailerSiteId", required = true) Long retailerSiteId,
            HttpServletRequest request) {
        FileSystem fileSystem = FileSystem.getDefault();
        if (flag != null) {
            Event event = null;
            try {
                event = eventManager.getEvent(id, retailerSiteId);
            } catch (Exception e) {
                logger.error("No Event found for retailer site id: " + retailerSiteId);
                logger.error(e.getMessage(), e);
            }
            String totalOptionalImageNo = event.getProperties().getProperty("dell.event.total.deleted.optional.image");
            int noOfOptionalImageDeletd =
                    Integer.parseInt(event.getProperties().getProperty("dell.event.total.deleted.optional.image"));
            if (totalOptionalImageNo != null) {
                noOfOptionalImageDeletd = Integer.parseInt(totalOptionalImageNo);
            }
            if (flag.equals("remove")) {
                event.getProperties().setProperty(imageKey, null);
                logger.info("Image having key:=" + imageKey + ", with value:=" + imageValue +
                        " is removed successfully");
                File file = null;
                try {
                    file = fileSystem.getFile(imageValue, true, true);
                } catch (IOException e) {
                    logger.debug("Optional Image :==>" + imageValue + " Not Found.");
                    logger.error(e.getMessage(), e);
                }
                if (file.exists()) {
                    file.delete();
                }
                noOfOptionalImageDeletd++;
                event.getProperties()
                        .setProperty("dell.event.total.deleted.optional.image", noOfOptionalImageDeletd);


                try {
                    eventManager.saveEvent(event);
                } catch (EntityExistsException e) {
                    logger.error(e.getMessage(), e);
                }


                return "deleted";
            } else if (flag.equals("update")) {
                event.getProperties().setProperty(imageKey, "/images/no_image.png");
                logger.info("Successfully updated the event key:==>" + imageKey +
                        ", with the value=/images/no_image.png");
                try {
                    eventManager.saveEvent(event);
                } catch (EntityExistsException e) {
                    logger.error(e.getMessage(), e);
                }
                return "updated";
            }
        }

        logger.info("You didn't choosed any option of delete or update");
        return "errors";
    }

    @Deprecated
    @RequestMapping(value = "/admin/events/checkNameAvailability.do", method = RequestMethod.GET)
    public
    @ResponseBody
    String checkEventNameAvailability(@RequestParam String name, @RequestParam String status, @RequestParam String id) {
        boolean nameExistenceStatus = false;
        Long eventId = 0L;
        if (!id.equals("null")) {
            eventId = Long.parseLong(id);
        }
        if (status.equals("update")) {
            String updateEventName = null;
            if (eventId != 0) {
                updateEventName = eventManager.getEventNameByID(eventId);
                if (!name.equals(updateEventName)) {
                    try {
                        nameExistenceStatus = eventManager.checkNameAvailability(name);
                    } catch (NonUniqueResultException nure) {
                        logger.error("Unique result not found");
                        nameExistenceStatus = true;
                    }
                }
            } else {
                logger.error("Unable to make an ajax call , because there is no event for event id::" + eventId);
            }
        } else {
            try {
                nameExistenceStatus = eventManager.checkNameAvailability(name);
            } catch (NonUniqueResultException nure) {
                logger.error("Unique result not found");
                nameExistenceStatus = true;
            }
        }
        if (nameExistenceStatus)

        {
            return "found";
        } else {
            return "notFound";
        }
    }

    /**
     Check the file extension corrected or not.

     @param fileName , store the uploaded file
     @return , response.
     */
    @RequestMapping(value = "admin/events/ajaxResponse.do", method = RequestMethod.GET)
    public
    @ResponseBody
    String ajaxResultForImageFormat(@RequestParam String fileName) {
        logger.info("File Name===>" + fileName);
        if (FileUtils.isFileExtensionAllowed(fileName, "image")) {
            return "File Selected";
        } else {
            return "File has unsupported format, please upload another file";
        }

    }

    private void addCrumbs(HttpServletRequest request,
                           Model model,
                           String crumbText,
                           RetailerSite retailerSite) {

        Retailer retailer = retailerSite.getRetailer();
        model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME,
                new AdminCrumb(request.getContextPath())
                        .libraryEvent(retailer.getName(),
                                retailer.getId(),
                                retailerSite.getSiteName(), retailerSite.getId())
                        .render(crumbText));
    }


    /**
     This will redirect the add event page.

     @param model  , Model attribute.
     @param siteID , store the retailersite id.
     */
    @RequestMapping(value = AdminCrumb.URL_EVENT_ADD, method = {RequestMethod.GET})
    public void addEventsPage(HttpServletRequest request, Model model,
                              @RequestParam(value = "siteID", required = true) final Long siteID) {
        logger.info("Event Add page is going to redirect....");

        Event event = new Event();
        RetailerSite retailerSite = retailerManager.getRetailerSite(siteID);

        model.addAttribute("retailerSite", retailerSite);
        event.setRetailerSite(retailerSite);
        model.addAttribute("event", event);
        addCrumbs(request, model, AdminCrumb.TEXT_EVENT_ADD, retailerSite);
    }

    /**
     {@inheritDoc}

     @see org.springframework.web.servlet.mvc. BaseCommandController#initBinder(javax.servlet.http.HttpServletRequest,
     org.springframework.web.bind.ServletRequestDataBinder)
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                dateFormat, false));
        binder.setBindingErrorProcessor(new CustomDefaultBindingErrorProcessor());
    }

    /**
     Validate the event model attribute and save the event if validated successfully.

     @param model         , store the model object.
     @param eventBean     , store the bean object reference value.
     @param bindingResult , store the error object , if any error found after validation.
     @param errors        , store the validations errors.
     @param file          , store file detail of hero images.
     @param optionalImage ,store the file detail of optional images.
     @param request       , store the request object.
     @return model and view reference according to binding result.
     @throws IOException
     */
    @RequestMapping(value = "/admin/events/validation-process.do", method = RequestMethod.POST)
    public ModelAndView add_submit(HttpServletRequest request, Model model,
                                   @ModelAttribute("event") Event event,
                                   final BindingResult bindingResult,
                                   final Errors errors,
                                   @RequestParam(value = "thumbnailFile", required = false) MultipartFile file,
                                   @RequestParam(value = "optionalImage", required = false)
                                   MultipartFile[] optionalImage)
            throws IOException {

        Assert.notNull(event);
        RetailerSite retailerSite = retailerManager.getRetailerSite(event.getRetailerSite().getId());
        Assert.notNull(retailerSite);
        event.setRetailerSite(retailerSite);
        // Check whether name , description and location has space value
        // also check whether location contain only character value not alphanumeric.
        if (!StringUtils.isEmpty(event.getName())) {
            if (StringUtils.isEmpty(event.getName().trim())) {
                bindingResult.addError(new FieldError("event", "name", null, true,
                        new String[]{"NoSpace.name"}, null, ""));
            }
        } else {
            bindingResult.addError(new FieldError("event", "name", null, true,
                    new String[]{"NotEmpty.name"}, null, ""));
        }
        if (!StringUtils.isEmpty(event.getDescription())) {
            if (StringUtils.isEmpty(event.getDescription().trim())) {
                bindingResult.addError(new FieldError("event", "description", null, true,
                        new String[]{"NoSpace.description"}, null, ""));
            }
        } else {
            bindingResult.addError(new FieldError("event", "description", null, true,
                    new String[]{"NotEmpty.description"}, null, ""));
        }
        if (!StringUtils.isEmpty(event.getLocation())) {
            if (StringUtils.isEmpty(event.getLocation().trim())) {
                bindingResult.addError(new FieldError("event", "location", null, true,
                        new String[]{"NoSpace.location"}, null, ""));
            } // GB - Location should accept any character.  e.g. "Austin, TX" or "Ben & Jerrys @ Parmer Lane"
            /* else {
                if (Pattern.matches("[+/-]?[0-9]+", event.getLocation())) {
                    bindingResult.addError(new FieldError("event", "location", null, true,
                            new String[]{"regex.alphanumeric.pattern"}, null,
                            "location can take alphanumeric or character only"));
                } else {
                    if (!Pattern.matches("[a-zA-Z0-9_]+", event.getLocation())) {
                        bindingResult.addError(new FieldError("event", "location", null, true,
                                new String[]{"regex.alphanumeric.pattern"}, null,
                                "location can take alphanumeric or character only"));
                    }
                }
            }*/
        } else {
            bindingResult.addError(new FieldError("event", "location", null, true,
                    new String[]{"NotEmpty.location"}, null, ""));
        }
        // Add the validation errors for event properties , if error found for properties.
        addEventPropertyErrorToBindingResult(bindingResult, request);

        if (!bindingResult.hasErrors()) {
            logger.info("No Validation error occurs");
            //Event event = new Event();

            // conver bean to entity.
            //event = FormBeanConverter.convert(eventBean, event);

            // set the value of event property.
            entityPropertySetter(event, file, request);

            event.setRetailerSite(retailerSite);

            // set default values


            // set the default value for total deleted optional image from event.
            event.getProperties().setProperty("dell.event.total.deleted.optional.image", 0);

            eventManager.saveEvent(event);

            try {
                //to tag the event
                String tag = WebUtils.getParameter(request, "TagValue", "");
                if (StringUtils.isNotEmpty(tag)) {
                    logger.debug("Tag for event " + event.getId() + " is" + tag);
                    tagManager.saveTags(tag, EntityConstants.Entities.EVENT.getId(), event.getId());
                }

                // Main Image File
                if (((DefaultMultipartHttpServletRequest) request).getMultiFileMap()
                        .containsKey("thumbnailFile")) {

                    file = ((DefaultMultipartHttpServletRequest) request).getMultiFileMap()
                            .getFirst("thumbnailFile");

                    if (file != null && !file.isEmpty()) {
                        if (FileUtils.isFileExtensionAllowed(file.getOriginalFilename(), "image")) {
                            String baseCDNPathForDocument = eventManager.getBaseCDNPathForEvent(event);
                            FileSystem fileSystem = FileSystem.getDefault();
                            String imageFileName = "thumbnailFile".concat(".png"); // Its only PNG for now
                            String finalFileName = baseCDNPathForDocument.concat(imageFileName);
                            logger.info(finalFileName);
                            File finalImageFile = new File(finalFileName);
                            boolean exists = false;
                            if (finalImageFile != null) {
                                exists = finalImageFile.exists();
                            }
                            if (exists) {
                                finalImageFile.delete();
                            }
                            File imageFile = fileSystem.getFile(finalFileName, true, true);
                            file.transferTo(imageFile);
                            logger.info(" image saved in - " + imageFile.getAbsolutePath());
                            //Set property for 'image'

                            event.getProperties()
                                    .setProperty(EventManager.EVENT_THUMBNAIL_PROPERTIES, finalFileName);
                            logger.info("Property value" + event.getProperties());
                        } else {
                            model.addAttribute("imageFileExtensionNotMatched", true);
                        }
                    }

                }
                //Optional Image File

                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                MultiValueMap<String, MultipartFile> map = multipartRequest.getMultiFileMap();
                Map<String, String> optionalImageMap = new HashMap<String, String>();
                int i = 0;
                if (map != null) {
                    Iterator iter = map.keySet().iterator();
                    while (iter.hasNext()) {
                        String str = (String) iter.next();
                        List<MultipartFile> fileList = map.get(str);
                        for (MultipartFile mpf : fileList) {
                            // create the key-value pair for optional images.
                            if (mpf != null && !mpf.isEmpty()) {
                                propertySetterForOptionalImage(mpf, event, i, optionalImageMap);
                                i++;
                            }
                        }
                    }
                    // exculded the hero images.
                    event.getProperties().setProperty("dell.event.total.optional.image", i - 1);
                }

            } catch (Exception e) {
                logger.debug("Image file not found for events==>" + event.getName());
                logger.error(e.getMessage(), e);
            }
            //Set the message flag value for optional field as key-value

            model.addAttribute("retailerSite", retailerSite);
            event.setRetailerSite(retailerSite);
            event = eventManager.saveEvent(event);

            String redirectUrl =
                    new AdminCrumb(request.getContextPath())
                            .toAbsolute(AdminCrumb.linkById(AdminCrumb.URL_EVENT_EDIT, event.getId()));
            return new ModelAndView(new RedirectView(redirectUrl.concat("&siteId=" + event.getRetailerSite().getId())));
        }


        // if binding result have errors.
        logger.info("Validation error occurs");
        event.setRetailerSite(retailerSite);
        model.addAttribute("retailerSite", retailerSite);
        model.addAttribute("errors", errors);

        addCrumbs(request, model, AdminCrumb.TEXT_EVENT_ADD, retailerSite);

        String viewName = new AdminCrumb(request.getContextPath())
                .toView(AdminCrumb.URL_EVENT_ADD);
        return new ModelAndView(viewName);
    }

    /**
     Create the property and set it's value from optional image. And also write these optional image under cdn
     directory.

     @param file                  , store hero images.
     @param event,                event reference.
     @param optionalImageCounter, count the no of added optional images.
     @param optionalImageMap      , store the map for optional images.
     @throws IOException , ioexception.
     */
    public void propertySetterForOptionalImage(MultipartFile file, Event event, int optionalImageCounter, Map<String, String> optionalImageMap) throws
            IOException {
        try {
            if (file != null && !file.isEmpty()) {
                String baseCDNPathForDocument = eventManager.getBaseCDNPathForEvent(event);
                FileSystem fileSystem = FileSystem.getDefault();
                String finalFileName = baseCDNPathForDocument.concat(file.getOriginalFilename());
                logger.info("File location for optional image==>" + finalFileName);
                File finalOptionalImage = new File(finalFileName);
                boolean exists = false;
                if (finalOptionalImage != null) {
                    exists = new File(finalFileName).exists();
                }
                if (exists) {
                    new File(finalFileName).delete();
                }
                File imageFile = fileSystem.getFile(finalFileName, true, true);
                logger.info(" image saved in - " + imageFile.getAbsolutePath());
                if (optionalImageCounter != 0) {
                    file.transferTo(imageFile);
                    event.getProperties()
                            .setProperty("dell.event.optional.image" + optionalImageCounter, finalFileName);
                }
            }
        } catch (Exception e) {
            logger.debug("Optional Image File Not Uploaded");
        }
    }

    /**
     Validate the manadtory event properties and BindingResult after adding the errors

     @param bindingResult , hold the bind errors
     @param request       , hold the request
     @return BindingResult
     */

    public BindingResult addEventPropertyErrorToBindingResult(BindingResult bindingResult, HttpServletRequest request) {

/*      (GB) Making these optional for validation from form, so we can assert default values later
        String[] validationFields =
 
                {};

        // check the value of mandatory event properties fields , according to requirement .
        // And create the error object for that properties which violate the business logic and then added that error object with the binding result.
        for (int i = 0; i < validationFields.length; i++) {

            if (validationFields[i].equals("numberOfDays") || validationFields[i].equals("perNpeople") ||
                    validationFields[i].equals("facebookLikes") || validationFields[i].equals("twitter") ||
                    validationFields[i].equals("googlePluses") || validationFields[i].equals("ratings")) {
                try {
                    if (Integer.parseInt((String) request.getParameter(validationFields[i])) < 0) {
                        bindingResult.addError(new FieldError("event", validationFields[i], null, true,
                                new String[]{validationFields[i] + ".format.error"}, null, ""));
                    }
                    if (validationFields[i].equals("perNpeople")) {
                        if (Integer.parseInt((String) request.getParameter(validationFields[i])) < 1) {

                            bindingResult.addError(new FieldError("event", validationFields[i], null, true,
                                    new String[]{validationFields[i] + ".required.value"}, null, ""));
                        }
                    }
                } catch (Exception e) {
                    if (StringUtils.isEmpty((String) request.getParameter(validationFields[i]))) {
                        bindingResult.addError(new FieldError("event", validationFields[i], null, true,
                                new String[]{validationFields[i] + ".empty.error"}, null, ""));
                    } else {
                        bindingResult.addError(new FieldError("event", validationFields[i], null, true,
                                new String[]{validationFields[i] + ".format.error"}, null, ""));
                    }

                }
            } else {

                try {
                    float floatValue = Float.parseFloat((String) request.getParameter(validationFields[i]));
                    if (validationFields[i].equals("stars")) {
                        if (!((floatValue >= 0.0) && (floatValue <= 5.0))) {
                            if (floatValue < 0) {
                                bindingResult.addError(new FieldError("event", validationFields[i], null, true,
                                        new String[]{validationFields[i] + ".format.error"}, null, ""));
                            } else {
                                bindingResult.addError(new FieldError("event", validationFields[i], null, true,
                                        new String[]{validationFields[i] + ".range.error"}, null, ""));
                            }
                        }
                    }
                } catch (Exception e) {
                    if (StringUtils.isEmpty((String) request.getParameter(validationFields[i]))) {
                        bindingResult.addError(new FieldError("event", validationFields[i], null, true,
                                new String[]{validationFields[i] + ".empty.error"}, null, ""));
                    } else {
                        bindingResult.addError(new FieldError("event", validationFields[i], null, true,
                                new String[]{validationFields[i] + ".format.error"}, null, ""));
                    }
                }

            }
        }
*/
        String[] eventOptionalPropertiesValue =
                {"infoLink", "buyLink", "reviewLink", "minimumPeople", "classOfService", "numberOfDays", "price",
                        "listPrice", "perNpeople",
                        "facebookLikes", "twitter", "googlePluses", "ratings", "stars"};
/*        String[] eventOptionalPropertiesKeys =
                {EventManager.EVENT_INFO_LINK_PROPERTIES, EventManager.EVENT_BUY_LINK_PROPERTIES,
                        EventManager.EVENT_REVIE_LINK_PROPERTIES, EventManager.EVENT_MINIMUM_PEOPLE_PROPERTIES,
                        EventManager.EVENT_CLASS_OF_SERVICE_PROPERTIES};*/
        for (int i = 0; i < eventOptionalPropertiesValue.length; i++) {
            if (!StringUtils.isEmpty(request.getParameter(eventOptionalPropertiesValue[i]))) {
                if ((eventOptionalPropertiesValue[i].equals("infoLink") ||
                        eventOptionalPropertiesValue[i].equals("buyLink") ||
                        eventOptionalPropertiesValue[i].equals("reviewLink")) &&
                        !eventOptionalPropertiesValue[i].isEmpty()) {

                    if (!Pattern.matches(
                            "(http?|https|www)[:]*[.]*[//]*[www]*[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]",
                            (String) request.getParameter(eventOptionalPropertiesValue[i]))) {

                        bindingResult.addError(new FieldError("event", eventOptionalPropertiesValue[i], null, true,
                                new String[]{eventOptionalPropertiesValue[i] + ".format.error"}, null, ""));


                    }
                } else if (eventOptionalPropertiesValue[i].equals("minimumPeople") ||
                        eventOptionalPropertiesValue[i].equals("facebookLikes") ||
                        eventOptionalPropertiesValue[i].equals("twitter") ||
                        eventOptionalPropertiesValue[i].equals("googlePluses") ||
                        eventOptionalPropertiesValue[i].equals("ratings")) {
                    // Integer only, >=0
                    if (!Pattern
                            .matches("[+]?[0-9]+", (String) request.getParameter(eventOptionalPropertiesValue[i])) ||
                            Pattern.matches("[+]?\\d+\\.\\d+",
                                    (String) request.getParameter(eventOptionalPropertiesValue[i]))) {

                        bindingResult.addError(new FieldError("event", eventOptionalPropertiesValue[i], null, true,
                                new String[]{eventOptionalPropertiesValue[i] + ".format.error"}, null, ""));
                    }
                } else if (eventOptionalPropertiesValue[i].equals("numberOfDays") ||
                        eventOptionalPropertiesValue[i].equals("perNpeople")) {
                    // Integer only, >=1
                    if (!Pattern.matches("[+]?[1-9][0-9]*",
                            (String) request.getParameter(eventOptionalPropertiesValue[i]))) {

                        bindingResult.addError(new FieldError("event", eventOptionalPropertiesValue[i], null, true,
                                new String[]{eventOptionalPropertiesValue[i] + ".format.error"}, null, ""));
                    }
                } else if (eventOptionalPropertiesValue[i].equals("price") ||
                        eventOptionalPropertiesValue[i].equals("listPrice") ||
                        eventOptionalPropertiesValue[i].equals("stars")) {
                    // Check whether  value is null or empty.
                    if (!StringUtils.isEmpty((String) request.getParameter(eventOptionalPropertiesValue[i]))) {
                        // Check whether it contains numeric string or character string.
                        if (Pattern.matches(
                                "[A-Za-z]+",
                                (String) request.getParameter(eventOptionalPropertiesValue[i]))) {

                            bindingResult.addError(
                                    new FieldError("event", eventOptionalPropertiesValue[i], null, true,
                                            new String[]{eventOptionalPropertiesValue[i] + ".format.error"},
                                            null, ""));
                        } else {
                            // Check whether it contains positive decimal number.
                            if (!Pattern.matches(
                                    "[+]?[0-9]*",
                                    (String) request.getParameter(eventOptionalPropertiesValue[i]))) {
                                if (eventOptionalPropertiesValue[i].equals("stars")) {
                                    float floatValue =
                                            Float.parseFloat(
                                                    (String) request.getParameter(eventOptionalPropertiesValue[i]));
                                    if (!((floatValue >= 0.0) && (floatValue <= 5.0))) {
                                        bindingResult.addError(
                                                new FieldError("event", eventOptionalPropertiesValue[i], null, true,
                                                        new String[]{eventOptionalPropertiesValue[i] + ".range.error"},
                                                        null, ""));
                                    }
                                } else {
                                    if (!Pattern.matches(
                                            "[+]?\\d+\\.\\d+",
                                            (String) request.getParameter(eventOptionalPropertiesValue[i]))) {

                                        bindingResult.addError(
                                                new FieldError("event", eventOptionalPropertiesValue[i], null, true,
                                                        new String[]{eventOptionalPropertiesValue[i] + ".format.error"},
                                                        null, ""));
                                    }
                                }

                            } else {
                                // if user given the + integer number , then check the range for star input field.

                                if (eventOptionalPropertiesValue[i].equals("stars")) {
                                    float floatValue =
                                            Float.parseFloat(
                                                    (String) request.getParameter(eventOptionalPropertiesValue[i]));
                                    if (!((floatValue >= 0.0) && (floatValue <= 5.0))) {
                                        bindingResult.addError(
                                                new FieldError("event", eventOptionalPropertiesValue[i], null, true,
                                                        new String[]{eventOptionalPropertiesValue[i] + ".range.error"},
                                                        null, ""));
                                    }
                                }

                            }
                        }

                    }
                } else if (eventOptionalPropertiesValue[i].equals("classOfService")) {
                    if (Pattern
                            .matches("[+/-]?[0-9]+", (String) request.getParameter(eventOptionalPropertiesValue[i]))) {
                        bindingResult.addError(new FieldError("event", eventOptionalPropertiesValue[i], null, true,
                                new String[]{eventOptionalPropertiesValue[i] + ".format.error"}, null,
                                ""));
                    } else {
                        if (!Pattern.matches(
                                "[A-Za-z]+",
                                (String) request.getParameter(eventOptionalPropertiesValue[i]))) {

                            bindingResult.addError(
                                    new FieldError("event", eventOptionalPropertiesValue[i], null, true,
                                            new String[]{eventOptionalPropertiesValue[i] + ".format.error"},
                                            null, ""));
                        }
                    }
                }
            }
        }

        return bindingResult;
    }

    /**
     delete the particular events from event list.

     @param id     , store the event id.
     @param model  , Model attribute.
     @param siteID , store the retailersite id.
     @return return the model for list page.
     */
    // MWE -- not modified for crumbs
    @RequestMapping(value = "/admin/events/delete.do", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam final Long id,
                               final Model model,
                               @RequestParam final Long siteID) {
        logger.info("load id :=" + id);
        /*  Long site_id=Long.parseLong(siteID);*/
        try {
            eventManager.deleteEvent(id);
        } catch (EventNotFoundException e) {
            logger.info("Event Not Found Exception :" + e.getMessage());

            return new ModelAndView(new RedirectView("/admin/events"
                    + "/list.do?siteID=" + siteID, true));
        }
        return new ModelAndView(new RedirectView("/admin/events"
                + "/list.do?siteID=" + siteID, true));
    }

    /**
     Redirect the event edit page.

     @param id      , store the event id.
     @param bean    , Evenet reference.
     @param binding , BindingResult reference.
     @param model   , Model Attribute.
     @param siteID  , store the retailersite id .
     */
    @RequestMapping(value = AdminCrumb.URL_EVENT_EDIT, method = RequestMethod.GET)
    public void editEventsPage(@RequestParam final Long id,
                               @RequestParam final Long siteId,
                               HttpServletRequest request, Model model) {
        logger.info("Event Add page is going to redirect....");

        Event event = eventManager.getEvent(id, siteId);
        Map<String, String> noOfOptionalImage = eventManager.getNumberOfOptionalImage(id);
        model.addAttribute("noOfOptionalImage", noOfOptionalImage);
        logger.info("LInk: " + (String) request.getAttribute("link"));

        RetailerSite retailerSite = retailerManager.getRetailerSite(event.getRetailerSite().getId());

        String tags = tagManager.getTagsAsString(EntityConstants.Entities.EVENT.getId(), id);
        model.addAttribute("tags", tags);

        model.addAttribute("event", event);
        addCrumbs(request, model, AdminCrumb.TEXT_EVENT_EDIT, retailerSite);
    }

    /**
     Update the event list after editing the event.

     @param model   , store the updated event model.
     @param bean    , store the updated event object.
     @param binding , to check whether the event object is bind with or without error.
     @return ModelAndView Object for list view.
     */
    @RequestMapping(value = AdminCrumb.URL_EVENT_EDIT, method = RequestMethod.POST)
    public ModelAndView updateEventsPage(HttpServletRequest request, Model model,
                                         @ModelAttribute("event") Event event,
                                         final BindingResult bindingResult,
                                         final Errors errors,
                                         @RequestParam(value = "thumbnailFile", required = false) MultipartFile file,
                                         @RequestParam(value = "reset_img_flag", required = false)
                                         final String reset_img_flag,
                                         @RequestParam(value = "siteID", required = true) final Long siteID) throws
            IOException {

        FileSystem fileSystem = FileSystem.getDefault();

        Event dbEvent = eventManager.getEvent(event.getId(), siteID);
        Assert.notNull(dbEvent, "Event not found for id " + event.getId());

        event.setRetailerSite(dbEvent.getRetailerSite());


        // Check whether name , description and location has space value
        // also check whetther location contain only character value not alphanumeric.
        if (!StringUtils.isEmpty(event.getName())) {
            if (StringUtils.isEmpty(event.getName().trim())) {
                bindingResult.addError(new FieldError("event", "name", null, true,
                        new String[]{"NoSpace.name"}, null, "Please provide the valid input."));
            } else {
                dbEvent.setName(event.getName());
            }
        } else {
            bindingResult.addError(new FieldError("event", "name", null, true,
                    new String[]{"NotEmpty.name"}, null, ""));
        }
        if (!StringUtils.isEmpty(event.getDescription())) {
            if (StringUtils.isEmpty(event.getDescription().trim())) {
                bindingResult.addError(new FieldError("event", "description", null, true,
                        new String[]{"NoSpace.description"}, null, "description is mandatory"));
            } else {
                dbEvent.setDescription(event.getDescription());
            }
        } else {
            bindingResult.addError(new FieldError("event", "description", null, true,
                    new String[]{"NotEmpty.description"}, null, ""));
        }

        if (!StringUtils.isEmpty(event.getLocation())) {
            if (StringUtils.isEmpty(event.getLocation().trim())) {
                bindingResult.addError(new FieldError("event", "location", null, true,
                        new String[]{"NoSpace.location"}, null, ""));
            } else {
                dbEvent.setLocation(event.getLocation());
            }
        } else {
            bindingResult.addError(new FieldError("event", "location", null, true,
                    new String[]{"NotEmpty.location"}, null, ""));
        }

        // Update the Start and Ebd Date
        if (event.getStartDate() != null) {
            dbEvent.setStartDate(event.getStartDate());
        }
        if (event.getEndDate() != null) {
            dbEvent.setEndDate(event.getEndDate());
        }

        // Validation for properties fields of events .

        addEventPropertyErrorToBindingResult(bindingResult, request);

        if (!bindingResult.hasErrors()) {
            logger.info("No Validation error occurs");

            // It will update entity property accept the optional image property.
            event = entityPropertySetter(dbEvent, file, request);


            // update the optional image property
            //dbEvent = updateOptionalImageProperty(event, dbEvent, file, request);

            //updating tag
            String tag = WebUtils.getParameter(request, "TagValue", "");
            String dbTag =
                    tagManager.getTagsAsString(EntityConstants.Entities.getByValue("EVENT").getId(), dbEvent.getId());
            if (!dbTag.equals(tag)) {
                tagManager.saveTags(tag, EntityConstants.Entities.getByValue("EVENT").getId(), dbEvent.getId());
            }

            String baseCDNPath = eventManager.getBaseCDNPathForEvent(dbEvent);
            try {
                // Main Image File
                if (((DefaultMultipartHttpServletRequest) request).getMultiFileMap()
                        .containsKey("thumbnailFile")) {

                    file = ((DefaultMultipartHttpServletRequest) request).getMultiFileMap()
                            .getFirst("thumbnailFile");

                    if (file != null && !file.isEmpty()) {
                        if (FileUtils.isFileExtensionAllowed(file.getOriginalFilename(), "image")) {
                            String baseCDNPathForDocument = baseCDNPath;
                            String imageFileName = "thumbnailFile".concat(".png"); // Its only PNG for now
                            String finalFileName = baseCDNPathForDocument.concat(imageFileName);
                            logger.info(finalFileName);
                            File thumbnailImageFileLocation = new File(finalFileName);
                            boolean exists = false;
                            if (thumbnailImageFileLocation != null) {
                                exists = true;
                            }
                            if (exists) {
                                thumbnailImageFileLocation.delete();
                            }
                            try {
                                File imageFile = fileSystem.getFile(finalFileName, true, true);
                                file.transferTo(imageFile);
                                logger.info(" image saved in - " + imageFile.getAbsolutePath());
                            } catch (Exception e) {
                                logger.error("Image file location[ " + finalFileName + "] Not FOUND");
                            }
                            //Set property for 'image'

                            dbEvent.getProperties()
                                    .setProperty(EventManager.EVENT_THUMBNAIL_PROPERTIES, finalFileName);
                            logger.info("Property value" + event.getProperties());
                        } else {
                            logger.info("File extension not matched for hero image.");
                            model.addAttribute("imageFileExtensionNotMatched", true);
                        }
                    } else {
                        // If user want to reset the image first ,before updating that.
                        if (reset_img_flag != null) {
                            if (reset_img_flag.equals("true")) {
                                try {
                                    File imageFile = new File(configurationService.getProperty("filesystem")
                                            .substring(0,
                                                    (configurationService.getProperty("filesystem")).length() - 1) +
                                            dbEvent.getProperties()
                                                    .getProperty(EventManager.EVENT_THUMBNAIL_PROPERTIES));
                                    if (imageFile.exists()) {
                                        imageFile.delete();
                                    }
                                    model.addAttribute("notUploaded", true);
                                } catch (Exception e) {
                                    logger.error("Image not found , first upload the image.");
                                    model.addAttribute("notUploaded", true);
                                }

                            }
                        } else {

                            // If user wants to updated the image only.
                            dbEvent.getProperties()
                                    .setProperty(EventManager.EVENT_THUMBNAIL_PROPERTIES,
                                            event.getProperties().getProperty(EventManager.EVENT_THUMBNAIL_PROPERTIES));
                            model.addAttribute("unsupportedFile", true);
                        }
                    }
                }

                //Optional Image File

                // contain all the optional image keys , which had been created during creation of event.
                String[] optionalImageKeyArray = null;

                try {
                    optionalImageKeyArray = eventManager.getListAllOptionalKeyFromEventProperty(event.getId());
                } catch (Exception e) {
                    logger.debug("Optional Image Not Found:: " + e.getMessage());
                }

                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                MultiValueMap<String, MultipartFile> map = multipartRequest.getMultiFileMap();
                Map<String, String> optionalImageMap = new HashMap<String, String>();

                //Store the i(number of optional image) and deletedNoOfOptionalImage(number of uncheck or deleted optional image)
                int i = 0;
                int deletedNoOfOptionalImage = 0;

                try {
                    i = Integer.parseInt(dbEvent.getProperties().getProperty("dell.event.total.optional.image"));
                    deletedNoOfOptionalImage =
                            Integer.parseInt(
                                    dbEvent.getProperties().getProperty("dell.event.total.deleted.optional.image"));
                } catch (Exception e) {
                    logger.error(
                            "Number format exception caught for event properties dell.event.total.optional.image or dell.event.total.deleted.optional.image ");
                }

                // Update the existed optional Image.

                if (optionalImageKeyArray != null) {
                    try {
                        for (int j = 0; j < i - deletedNoOfOptionalImage; j++) {
                            if (((DefaultMultipartHttpServletRequest) request).getMultiFileMap()
                                    .containsKey(optionalImageKeyArray[j])) {
                                MultipartFile optionalFile =
                                        ((DefaultMultipartHttpServletRequest) request).getMultiFileMap()
                                                .getFirst(optionalImageKeyArray[j]);
                                if (optionalFile != null && !optionalFile.isEmpty()) {
                                    if (baseCDNPath != null) {
                                        dbEvent.getProperties()
                                                .setProperty(optionalImageKeyArray[j], baseCDNPath.concat(
                                                        optionalFile.getOriginalFilename()));
                                        File imageFile = fileSystem
                                                .getFile(baseCDNPath.concat(optionalFile.getOriginalFilename()), true,
                                                        true);
                                        try {
                                            optionalFile.transferTo(imageFile);
                                        } catch (IOException ioe) {
                                            logger.debug("Image File Not found");
                                        }
                                    }
                                }
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException aiobe) {
                        logger.error("Value of key 'dell.event.total.optional.image' [" + i +
                                "] is more than the total no of optional image [" +
                                (i - Integer.parseInt(aiobe.getMessage())) +
                                "] present in the database");
                    }

                    // Set the property for new uploaded image file.

                    try {
                        LinkedMultiValueMap linkedMultiValueMap =
                                (LinkedMultiValueMap) ((DefaultMultipartHttpServletRequest) request).getMultiFileMap();
                        Iterator it = linkedMultiValueMap.entrySet().iterator();
                        CommonsMultipartFile commonsMultipartFile = null;
                        while (it.hasNext()) {
                            Map.Entry pairs = (Map.Entry) it.next();
                            // before creating the key-value for newly updated optional image ,
                            // it check whether input value is coming from alraedy existed file input field or coming from newly created file input field.
                            // it allows only newly created file input field.
                            if (!pairs.getKey().equals("thumbnailFile")) {
                                if (!(((String) pairs.getKey()).startsWith("dell.event.optional.image"))) {

                                    logger.info("Key-Value Pair of uploaded file : " + pairs.getKey() + " = " +
                                            pairs.getValue());
                                    LinkedList commonsMultipartFileList = (LinkedList) pairs.getValue();
                                    for (Object obj : commonsMultipartFileList) {
                                        commonsMultipartFile = (CommonsMultipartFile) obj;
                                        String uploadedImageFileName = commonsMultipartFile.getOriginalFilename();
                                        if (!StringUtils.isEmpty(uploadedImageFileName)) {
                                            String newlyUploadedOptionalImage = baseCDNPath.concat(
                                                    commonsMultipartFile.getOriginalFilename());
                                            dbEvent.getProperties().setProperty("dell.event.optional.image" + (i + 1),
                                                    newlyUploadedOptionalImage);
                                            i++;
                                            File newOptionalImageFile = fileSystem
                                                    .getFile(baseCDNPath.concat(
                                                            commonsMultipartFile.getOriginalFilename()), true, true);

                                            try {
                                                commonsMultipartFile.transferTo(newOptionalImageFile);
                                                logger.info(
                                                        "New uploaded file location:==>" + newlyUploadedOptionalImage);
                                            } catch (IOException ioe) {
                                                logger.debug("Newly Addedd optional Image File Not found");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (i != 0) {
                            dbEvent.getProperties().setProperty("dell.event.total.optional.image", i);
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }

                }
            } catch (Exception e) {
                logger.debug("Image file not found for event==>" + event.getName());
            }
            event = eventManager.saveEvent(dbEvent);

            String redirectUrl = new AdminCrumb(request.getContextPath())
                    .toAbsolute(AdminCrumb.linkById(AdminCrumb.URL_EVENT_EDIT, dbEvent.getId()));
            return new ModelAndView(
                    new RedirectView(redirectUrl.concat("&siteId=" + dbEvent.getRetailerSite().getId())));

        } else {
            event.getProperties().setProperty(EventManager.EVENT_THUMBNAIL_PROPERTIES,
                    dbEvent.getProperties().getProperty(EventManager.EVENT_THUMBNAIL_PROPERTIES));
        }


        logger.info("Validation error occurs");
        //Setting property for bean class
        Map<String, String> noOfOptionalImage = eventManager.getNumberOfOptionalImage(event.getId());
        event = entityPropertySetter(event, file, request);
        model.addAttribute("errors", errors);
        model.addAttribute("noOfOptionalImage", noOfOptionalImage);

        addCrumbs(request, model, AdminCrumb.TEXT_EVENT_EDIT, event.getRetailerSite());
        String viewName = new AdminCrumb(request.getContextPath())
                .toView(AdminCrumb.URL_EVENT_EDIT);
        return new ModelAndView(viewName);
    }

    /**
     Return the event which having the property value.

     @param request
     @return
     @throws IOException
     */

    public Event entityPropertySetter(final Event event, MultipartFile file, final HttpServletRequest request) throws
            IOException {

        final int DEF_PERNPEOPLE = 1;
        final int DEF_NUMBEROFDAYS = 1;
        final int DEF_SOCIAL = 0;
        final double DEF_PRICE = 0.0;
        final double DEF_STARS = 0.0;

        // Setting the mandatory properties  of event entity
        String[] eventMandatoryPropertiesValue =
                {"numberOfDays", "price", "listPrice", "perNpeople", "facebookLikes", "twitter", "googlePluses",
                        "ratings",
                        "stars"};
        String[] eventMandatoryPropertiesKeys =
                {EventManager.EVENT_NUMBER_OF_DAYS_PROPERTIES, EventManager.EVENT_PRICE_PROPERTIES,
                        EventManager.EVENT_LIST_PRICE_PROPERTIES, EventManager.EVENT_PER_N_PEOPLE_PROPERTIES,
                        EventManager.EVENT_FACEBOOK_LIKES_PROPERTIES, EventManager.EVENT_TWITTER_PROPERTIES,
                        EventManager.EVENT_GOOGLE_PLUSES_PROPERTIES, EventManager.EVENT_RATINGS_PROPERTIES,
                        EventManager.EVENT_STARS_PROPERTIES};
        for (int i = 0; i < eventMandatoryPropertiesValue.length; i++) {
            if (!StringUtils.isEmpty(request.getParameter(eventMandatoryPropertiesValue[i]))) {

                if (eventMandatoryPropertiesValue[i].equals("price") ||
                        eventMandatoryPropertiesValue[i].equals("listPrice") ||
                        eventMandatoryPropertiesValue[i].equals("stars")) {
                    Float floatValue = WebUtils.getParameter(request, eventMandatoryPropertiesValue[i], 0.0f);
                    event.getProperties().setProperty(eventMandatoryPropertiesKeys[i], floatValue);
                } else {
                    event.getProperties().setProperty(eventMandatoryPropertiesKeys[i],
                            request.getParameter(eventMandatoryPropertiesValue[i]));
                }
            } else if (eventMandatoryPropertiesKeys[i] == EventManager.EVENT_NUMBER_OF_DAYS_PROPERTIES) {
                event.getProperties().setProperty(eventMandatoryPropertiesKeys[i], DEF_NUMBEROFDAYS);
            } else if (eventMandatoryPropertiesKeys[i] == EventManager.EVENT_PER_N_PEOPLE_PROPERTIES) {
                event.getProperties().setProperty(eventMandatoryPropertiesKeys[i], DEF_PERNPEOPLE);
            } else if ((eventMandatoryPropertiesKeys[i] == EventManager.EVENT_PRICE_PROPERTIES) ||
                    (eventMandatoryPropertiesKeys[i] == EventManager.EVENT_LIST_PRICE_PROPERTIES)) {
                event.getProperties().setProperty(eventMandatoryPropertiesKeys[i], DEF_PRICE);
            } else if ((eventMandatoryPropertiesKeys[i] == EventManager.EVENT_FACEBOOK_LIKES_PROPERTIES) ||
                    (eventMandatoryPropertiesKeys[i] == EventManager.EVENT_TWITTER_PROPERTIES) ||
                    (eventMandatoryPropertiesKeys[i] == EventManager.EVENT_GOOGLE_PLUSES_PROPERTIES) ||
                    (eventMandatoryPropertiesKeys[i] == EventManager.EVENT_RATINGS_PROPERTIES)) {
                event.getProperties().setProperty(eventMandatoryPropertiesKeys[i], DEF_SOCIAL);
            } else if (eventMandatoryPropertiesKeys[i] == EventManager.EVENT_STARS_PROPERTIES) {
                event.getProperties().setProperty(eventMandatoryPropertiesKeys[i], DEF_STARS);
            }
        }

        // Setting Optional properties of event entity.

        String[] eventOptionalPropertiesValue =
                {"infoLink", "buyLink", "reviewLink", "minimumPeople", "classOfService"};
        String[] eventOptionalPropertiesKeys =
                {EventManager.EVENT_INFO_LINK_PROPERTIES, EventManager.EVENT_BUY_LINK_PROPERTIES,
                        EventManager.EVENT_REVIE_LINK_PROPERTIES, EventManager.EVENT_MINIMUM_PEOPLE_PROPERTIES,
                        EventManager.EVENT_CLASS_OF_SERVICE_PROPERTIES};
        for (int i = 0; i < eventOptionalPropertiesValue.length; i++) {
            if (!StringUtils.isEmpty(request.getParameter(eventOptionalPropertiesValue[i]))) {
                if (eventOptionalPropertiesValue[i].equals("infoLink") ||
                        eventOptionalPropertiesValue[i].equals("buyLink") ||
                        eventOptionalPropertiesValue[i].equals("reviewLink")) {

                    if (Pattern.matches(
                            "(http?|https|www)[:]*[.]*[//]*[www]*[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]",
                            (String) request.getParameter(eventOptionalPropertiesValue[i]))) {

                        event.getProperties().setProperty(eventOptionalPropertiesKeys[i],
                                request.getParameter(eventOptionalPropertiesValue[i]));
                    }
                } else {
                    if (eventOptionalPropertiesValue[i].equals("minimumPeople")) {

                        if (Pattern.matches("[+]?[0-9]+",
                                (String) request.getParameter(eventOptionalPropertiesValue[i]))) {
                            event.getProperties().setProperty(eventOptionalPropertiesKeys[i],
                                    request.getParameter(eventOptionalPropertiesValue[i]));

                        }
                    } else {
                        if (Pattern.matches(
                                "[A-Za-z]+",
                                (String) request.getParameter(eventOptionalPropertiesValue[i]))) {
                            event.getProperties().setProperty(eventOptionalPropertiesKeys[i],
                                    request.getParameter(eventOptionalPropertiesValue[i]));

                        }
                    }
                }
            }
        }
        return event;
    }

    /**
     EventManager Bean.
     */
    @Autowired
    private EventManager eventManager;

    /**
     Retailer Manager bean.
     */
    @Autowired
    private RetailerManager retailerManager;

    /**
     Tag Manager bean
     */
    @Autowired
    private TagManager tagManager;

    /**
     EventManager bean injector.

     @param eventManager , store the EventManager bean object
     */
    public void setEventManager(final EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void setRetailerManager(final RetailerManager retailerManager) {
        this.retailerManager = retailerManager;
    }
}
