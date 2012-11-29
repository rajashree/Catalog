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
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.util.Assert;
import com.sourcen.core.util.FileSystem;
import com.sourcen.core.util.FileUtils;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.WebUtils;
import com.sourcen.core.web.controller.BaseController;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * @author Ashish
 * @author $LastChangedBy: Ashish $
 * @version $Revision: 1595 $, $Date:: 7/25/12 1:43 PM#$
 */
@Controller

public class ImageController extends BaseDellController {

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

    /**
     * Check the file extension corrected or not.
     *
     * @param fileName , store the uploaded file
     * @return , response.
     */
    @RequestMapping(value = "/admin/images/ajaxResponse.do", method = RequestMethod.GET)
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

    @Deprecated
    @RequestMapping(value = "/admin/images/checkNameAvailability.do", method = RequestMethod.GET)
    public
    @ResponseBody
    String checkDocumentNameAvailability(
            @RequestParam String name, @RequestParam String status, @RequestParam String id) {
        boolean nameExistenceStatus = false;
        Long imageId = 0L;
        if (!id.equals("null")) {
            imageId = Long.parseLong(id);
        }
        if (status.equals("update")) {
            String updateImageName = null;
            if (imageId != 0) {
                updateImageName = documentManager.getDocumentNameByID(imageId, 2001);
                if (!name.equals(updateImageName)) {
                    try {
                        nameExistenceStatus = documentManager.checkNameExistence(name);
                    } catch (NonUniqueResultException nure) {
                        logger.error("Unique result not found");
                        nameExistenceStatus = true;
                    }
                }
            } else {
                logger.error("Unable to make an ajax call , because there is no document for image id::" + imageId);
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

    @RequestMapping(value = AdminCrumb.URL_IMAGE_ADD, method = RequestMethod.GET)
    public void addImagePage(HttpServletRequest request,
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
            addCrumbs(request, model, AdminCrumb.TEXT_IMAGE_ADD, retailerSite);
        }

    }

    @RequestMapping(value = AdminCrumb.URL_IMAGE_ADD, method = RequestMethod.POST)
    public ModelAndView saveImagePage(final Model model,
                                      final HttpServletRequest request,
                                      @ModelAttribute(value = "document") Document imageContent,
                                      BindingResult bindingResult,
                                      @RequestParam(value = "thumbnailFile", required = false) MultipartFile file,
                                      Errors errors,
                                      @RequestParam(value = "siteID", required = false) final Long siteID) throws
            IOException, EntityExistsException {

        RetailerSite retailerSite = null;
        boolean imageFileExtensionNotMatched = false;
        if (imageContent != null) {
            imageContent.setRetailerSite(retailerManager.getRetailerSite(imageContent.getRetailerSite().getId()));
        }
        // Image Mandatory validation.

        if (StringUtils.isEmpty(file.getOriginalFilename())) {
            bindingResult.addError(new FieldError("document", "thumbnailFile", null, true,
                    new String[]{"NotEmpty.thumbnailFile"}, null, ""));
        } else {
            if (!FileUtils.isFileExtensionAllowed(file.getOriginalFilename(), "image")) {
                //model.addAttribute("imageFileExtensionNotMatched",new Boolean(true));
                bindingResult.addError(new FieldError("document", "thumbnailFile", null, true,
                        new String[]{"thumbnailFile.extension.error"}, null, ""));
            }
        }

        // Check the validation for Iamge input fields.
        validateFields(imageContent, bindingResult, file, model);


        if (!bindingResult.hasErrors()) {
            logger.info("No Validation error occurs");

            imageContent = documentManager.saveDocument(imageContent);
            if (imageContent.getId() != null) {

                /* Saving the image tag to database, Upon getting the image object */
                String tag= WebUtils.getParameter(request, "TagValue", "");
                if(StringUtils.isNotEmpty(tag))
                {
                 logger.debug("Tag for imageID "+imageContent.getId()+" is "+tag);
                 tagManager.saveTags(tag, EntityConstants.Entities.IMAGE.getId(), imageContent.getId());
                }
                /* End*/

                MultiValueMap<String, MultipartFile> multipartFileMultiValueMap =
                        ((DefaultMultipartHttpServletRequest) request).getMultiFileMap();

                logger.info("Saved  Image Content[Without the Image]==>" + imageContent);

                String finalFileName = null;
                if (multipartFileMultiValueMap.containsKey("thumbnailFile")) {
                    file = multipartFileMultiValueMap.getFirst("thumbnailFile");
                    if (file != null && !file.isEmpty()) {
                        if (FileUtils.isFileExtensionAllowed(file.getOriginalFilename(), "image")) {
                            Assert.notNull(imageContent, "Trying to set the image property for null entity");
                            String baseCDNPathForDocument =
                                    documentManager.getBaseCDNPathForDocument(imageContent);
                            FileSystem fileSystem = FileSystem.getDefault();
                            String imageFileName = "thumbnailFile".concat(".png"); // Its only PNG for now
                            finalFileName = baseCDNPathForDocument.concat(imageFileName);
                            logger.info(finalFileName);
                            boolean exists = new File(finalFileName).exists();
                            if (exists) {
                                new File(finalFileName).delete();
                            }
                            File imageFile = fileSystem.getFile(finalFileName, true, true);
                            Assert.notNull(imageFile, "Image File Not Found");
                            file.transferTo(imageFile);
                            logger.info(" image saved in - " + imageFile.getAbsolutePath());
                            ///Sandeep - JIRA - CS-483
//                            imageContent.getProperties()
//                                    .setProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY, finalFileName);
                            imageContent.setImage(finalFileName);
//                            logger.info("Property value==>" + imageContent.getProperties());

                            logger.info("Image Location" + imageContent.getImage());

                        } else {
                            imageFileExtensionNotMatched = true;
                            model.addAttribute("imageFileExtensionNotMatched", imageFileExtensionNotMatched);
                        }
                    }
                }
                if (finalFileName != null) {
                    imageContent.setUrl(finalFileName);
                }
                imageContent = documentManager.saveDocument(imageContent);
                String redirectUrl =
                        new AdminCrumb(request.getContextPath())
                                .toAbsolute(
                                        AdminCrumb.linkById(AdminCrumb.URL_IMAGE_EDIT, imageContent.getId()));
                return new ModelAndView(new RedirectView(redirectUrl));
            } else {
                Long imageContentID = imageContent.getId();
                Assert.notNull(imageContentID, "Unable to save the image content type");

            }
        }
        logger.info("No Validation error occurs");
        model.addAttribute("errors", errors);

        addCrumbs(request, model, AdminCrumb.TEXT_IMAGE_ADD,
                retailerManager.getRetailerSite(imageContent.getRetailerSite().getId()));

        String viewName = new AdminCrumb(request.getContextPath())
                .toView(AdminCrumb.URL_IMAGE_ADD);

        return new ModelAndView(viewName);
    }


    @RequestMapping(value = AdminCrumb.URL_IMAGE_EDIT, method = RequestMethod.GET)
    public void editImagePage(HttpServletRequest request,
                              final Model model,
                              @RequestParam Long id,
                              @RequestParam(value = "siteID", required = false) final Long siteID) throws
            EntityNotFoundException {

        FileSystem fileSystem = FileSystem.getDefault();
        Document imageContent = documentManager.getDocument(id);
//        https://jira.marketvine.com/browse/CS-450 - Handled the case where edit pages of content
//                types were corrupting the data of other content types
        RetailerSite retailerSite = imageContent.getRetailerSite();
        Integer type = imageContent.getType();
        if (type == EntityConstants.Entities.IMAGE.getId()) {
            Assert.notNull(imageContent, "Image Content Not Found");
            //Sandeep - JIRA-CS-483
//        String finalImageFileName =
//                imageContent.getProperties().getProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY);
            String finalImageFileName = imageContent.getImage();

            /* Populating the Existing image tags based on image ID */
            String tags=this.tagManager.getTagsAsString(EntityConstants.Entities.IMAGE.getId(), imageContent.getId());
            model.addAttribute("tags", tags);
            /**/

            if (finalImageFileName != null) {
                File file = new File(configurationService.getProperty("filesystem") + finalImageFileName);
                if (!file.exists()) {
                    model.addAttribute("imageNotExist", true);
                }
            }

            if (retailerSite != null) {
                model.addAttribute("retailerSite", retailerSite);
            } else {
                model.addAttribute("error", "Unable to process the request");
            }
            if (!StringUtils.isEmpty(request.getParameter("imageFileExtensionMatched"))) {
                if (request.getParameter("imageFileExtensionMatched").equals("false")) {
                    model.addAttribute("imageFileExtensionMatched", false);
                }
            }
            if (!StringUtils.isEmpty(request.getParameter("resetFlag"))) {
                if (request.getParameter("resetFlag").equals("false")) {
                    model.addAttribute("resetFlag", false);
                } else {
                    if (!StringUtils.isEmpty(request.getParameter("newFileFound"))) {
                        if (request.getParameter("newFileFound").equals("false")) {
                            if (finalImageFileName != null) {

                                try {
                                    File file = fileSystem.getFile(finalImageFileName, true, true);
                                    if (file.exists()) {
                                        file.delete();
                                    }
                                } catch (IOException e) {
                                    logger.error("File Not found in cdn directory");
                                }
                                model.addAttribute("resetFlag", true);

                            } else {
                                logger.warn("Image File key-value pair not found");
                            }
                        } else {
                            model.addAttribute("resetFlag", false);
                        }
                    }
                    model.addAttribute("resetFlag", true);
                }
            }
            if (!StringUtils.isEmpty(request.getParameter("uploadStatus"))) {
                if (request.getParameter("uploadStatus").equals("false")) {
                    model.addAttribute("uploadStatus", false);
                } else {
                    model.addAttribute("uploadStatus", true);
                }
            }

            model.addAttribute("document", imageContent);
            model.addAttribute("invalidType", false);
        } else {
            model.addAttribute("invalidType", true);
            String contentTypeName = EntityConstants.Entities.getById(type).getValue();
            model.addAttribute("contentType", contentTypeName);
            logger.warn("User trying to load IMAGE with ID:= " + imageContent.getId() + " which is of type "
                    + contentTypeName);
        }

        addCrumbs(request, model, AdminCrumb.TEXT_IMAGE_EDIT, retailerSite);


    }

    @RequestMapping(value = AdminCrumb.URL_IMAGE_EDIT, method = RequestMethod.POST)
    public ModelAndView updateImagePage(final Model model,
                                        final HttpServletRequest request,
                                        @ModelAttribute(value = "document") Document imageContent,
                                        BindingResult bindingResult,
                                        @RequestParam(value = "thumbnailFile", required = false) MultipartFile file,
                                        Errors errors,
                                        @RequestParam(value = "id", required = false) Long id,
                                        @RequestParam(value = "reset_img_flag", required = false)
                                        String reset_img_flag) throws
            IOException, EntityExistsException, EntityNotFoundException {

        RetailerSite retailerSite = null;
        boolean imageFileExtensionNotMatched = false;
        Assert.notNull(id, "No Image Record found for image id:=>" + id);
        logger.info("Image Record found");
        Document dbImageContent = documentManager.getDocument(id);

        /* Updating the image tag to database, After getting the image object */
        String tagsExisting=tagManager.getTagsAsString(EntityConstants.Entities.IMAGE.getId(), dbImageContent.getId());
        String tags=WebUtils.getParameter(request,"TagValue","");
        if(!tagsExisting.equals(tags))
        {
        tagManager.saveTags(tags,EntityConstants.Entities.IMAGE.getId(), dbImageContent.getId());
        }

        boolean resetImage = false;
        if (reset_img_flag != null) {
            if (reset_img_flag.equals("true")) {
                resetImage = true;
            }
        }

        if (resetImage) {
            // Image Mandatory validation.

            if (StringUtils.isEmpty(file.getOriginalFilename())) {
                bindingResult.addError(new FieldError("document", "thumbnailFile", null, true,
                        new String[]{"NotEmpty.thumbnailFile"}, null, ""));
            } else {
                if (!FileUtils.isFileExtensionAllowed(file.getOriginalFilename(), "image")) {
                    //model.addAttribute("imageFileExtensionNotMatched",new Boolean(true));
                    bindingResult.addError(new FieldError("document", "thumbnailFile", null, true,
                            new String[]{"thumbnailFile.extension.error"}, null, ""));
                }
            }
        }

        // Check the validation for Iamge input fields.
        validateFields(imageContent, bindingResult, file, model);

        if (!bindingResult.hasErrors()) {
            logger.info("No Validation error occurs");

            // Image Saving with validation regarding file extension , file existence.

            MultiValueMap<String, MultipartFile> multipartFileMultiValueMap =
                    ((DefaultMultipartHttpServletRequest) request).getMultiFileMap();

            logger.info("Saved  Image Content[Without the Image]==>" + imageContent);

            String finalFileName = null;
            if (!StringUtils.isEmpty(file.getOriginalFilename())) {

                if (multipartFileMultiValueMap.containsKey("thumbnailFile")) {
                    file = multipartFileMultiValueMap.getFirst("thumbnailFile");
                    if (file != null && !file.isEmpty()) {
                        if (FileUtils.isFileExtensionAllowed(file.getOriginalFilename(), "image")) {
                            Assert.notNull(imageContent, "Trying to set the image property for null entity");
                            String baseCDNPathForDocument =
                                    documentManager.getBaseCDNPathForDocument(dbImageContent);
                            FileSystem fileSystem = FileSystem.getDefault();
                            String imageFileName = "thumbnailFile".concat(".png"); // Its only PNG for now
                            finalFileName = baseCDNPathForDocument.concat(imageFileName);
                            logger.info(finalFileName);
                            boolean exists = new File(finalFileName).exists();
                            if (exists) {
                                new File(finalFileName).delete();
                            }
                            File imageFile = fileSystem.getFile(finalFileName, true, true);
                            Assert.notNull(imageFile, "Image File Not Found");
                            file.transferTo(imageFile);
                            logger.info(" image saved in - " + imageFile.getAbsolutePath());
                            //Sandeep - JIRA-CS-483
//                            dbImageContent.getProperties()
//                                    .setProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY, finalFileName);
                            dbImageContent.setImage(finalFileName);
                            dbImageContent.setUrl(finalFileName);
//                            logger.info("Property value==>" + imageContent.getProperties());

                        } else {
                            model.addAttribute("imageFileExtensionMatched", false);
                        }
                    } else {
                        //Sandeep - JIRA-CS-483
//                        imageContent.getProperties().setProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY,
//                                dbImageContent.getProperties()
//                                        .getProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY));
                        imageContent.setImage(dbImageContent.getImage());
                        model.addAttribute("uploadStatus", false);
                    }
                }
            } else {
                //Sandeep - JIRA-CS-483
//                imageContent.getProperties().setProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY,
//                        dbImageContent.getProperties().getProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY));
                imageContent.setImage(dbImageContent.getImage());
                if (resetImage) {
//                    String finalImageFileName =
//                            imageContent.getProperties().getProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY);
                    String finalImageFileName = imageContent.getImage();
                    if (finalImageFileName != null) {

                        try {
                            //Sandeep - JIRA-CS-483
//                            File imageFile = new File(configurationService.getProperty("filesystem")
//                                    .substring(0, (configurationService.getProperty("filesystem")).length() - 1) +
//                                    dbImageContent.getProperties()
//                                            .getProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY));
                            File imageFile = new File(configurationService.getProperty("filesystem")
                                    .substring(0, (configurationService.getProperty("filesystem")).length() - 1) +
                                    dbImageContent.getImage());

                            if (imageFile.exists()) {
                                imageFile.delete();
                                dbImageContent.setUrl(null);
                            }
                            dbImageContent.setImage(null);
//                            dbImageContent.getProperties()
//                                    .setProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY, null);
                        } catch (Exception e) {
                            logger.error("File Not found in cdn directory");
                        }

                    } else {
                        logger.warn("Image File key-value pair not found");
                    }
                }
            }


            dbImageContent.setName(imageContent.getName());
            if (finalFileName != null) {
                imageContent.setUrl(finalFileName);
            }
            dbImageContent.setDescription(imageContent.getDescription());
            dbImageContent.setAuthor(imageContent.getAuthor());
            dbImageContent.setSource(imageContent.getSource());
            dbImageContent.setAbstractText(imageContent.getAbstractText());
            dbImageContent.setPublishDate(imageContent.getPublishDate());
            imageContent = documentManager.saveDocument(dbImageContent);
            String redirectUrl =
                    new AdminCrumb(request.getContextPath())
                            .toAbsolute(
                                    AdminCrumb.linkById(AdminCrumb.URL_IMAGE_EDIT, imageContent.getId()));
            return new ModelAndView(new RedirectView(redirectUrl));
        }/* else {
//            imageContent.getProperties().setProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY,
//                    dbImageContent.getProperties().getProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY));
            imageContent.setImage(dbImageContent.getImage());
        }
*/
        logger.info("Validation error occurs");
        model.addAttribute("errors", errors);
        imageContent.setUrl(dbImageContent.getUrl());
        if (!resetImage) {
            imageContent.setImage(dbImageContent.getImage());
        } else {
            imageContent.setImage(null);
        }
        addCrumbs(request, model, AdminCrumb.TEXT_IMAGE_EDIT,
                retailerManager.getRetailerSite(imageContent.getRetailerSite().getId()));

        String viewName = new AdminCrumb(request.getContextPath())
                .toView(AdminCrumb.URL_IMAGE_EDIT);

        return new ModelAndView(viewName);
    }

    public void validateFields(Document entity, BindingResult bindingResult, MultipartFile file, Model model) {


        if (!StringUtils.isEmpty(entity.getName())) {
            if (StringUtils.isEmpty(entity.getName().trim())) {
                bindingResult.addError(new FieldError("document", "name", null, true,
                        new String[]{"NoSpace.image.name"}, null, ""));
            }
        } else {
            bindingResult.addError(new FieldError("document", "name", null, true,
                    new String[]{"NotEmpty.title"}, null, ""));
        }
    }

    @Autowired
    private RetailerManager retailerManager;

    public void setRetailerManager(final RetailerManager retailerManager) {
        this.retailerManager = retailerManager;
    }

    @Autowired
    private DocumentManager documentManager;



    @Autowired
    private TagManager tagManager;

    public void setDocumentManager(final DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    /**
     * ConfigurationService data injection.
     */
    @Autowired
    private ConfigurationService configurationService;
}
