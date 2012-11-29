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
 * @version $Revision: 1595 $, $Date:: 7/25/12 7:30 PM#$
 */
@Controller
public class VideoController extends BaseDellController {


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
    @RequestMapping(value = "admin/videos/ajaxResponse.do", method = RequestMethod.GET)
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
    @RequestMapping(value = "admin/videos/checkNameAvailability.do", method = RequestMethod.GET)
    public
    @ResponseBody
    String checkDocumentNameAvailability(
            @RequestParam String name, @RequestParam String status, @RequestParam String id) {
        boolean nameExistenceStatus = false;
        Long videoId = 0L;
        if (!id.equals("null")) {
            videoId = Long.parseLong(id);
        }
        if (status.equals("update")) {
            String updateVideoName = null;
            if (videoId != 0) {
                updateVideoName = documentManager.getDocumentNameByID(videoId, 2003);
                if (!name.equals(updateVideoName)) {
                    try {
                        nameExistenceStatus = documentManager.checkNameExistence(name);
                    } catch (NonUniqueResultException nure) {
                        logger.error("Unique result not found");
                        nameExistenceStatus = true;
                    }
                }
            } else {
                logger.error("Unable to make an ajax call , because there is no document for video id::" + videoId);
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

    @RequestMapping(value = AdminCrumb.URL_VIDEO_ADD, method = RequestMethod.GET)
    public void addVideoPage(HttpServletRequest request,
                             final Model model,
                             @RequestParam(value = "siteID", required = false) final Long siteID) {

        if (siteID != null) {
            Document videoContent = new Document();
            RetailerSite retailerSite = retailerManager.getRetailerSite(siteID);
            if (retailerSite != null) {
                videoContent.setRetailerSite(retailerSite);
                model.addAttribute("retailerSite", retailerSite);
            } else {
                model.addAttribute("error", "Unable to process the request");
            }
            model.addAttribute("document", videoContent);
            addCrumbs(request, model, AdminCrumb.TEXT_VIDEO_ADD, retailerSite);
        }

    }

    @RequestMapping(value = AdminCrumb.URL_VIDEO_ADD, method = RequestMethod.POST)
    public ModelAndView saveVideoPage(final Model model,
                                      final HttpServletRequest request,
                                      @ModelAttribute(value = "document") Document videoContent,
                                      BindingResult bindingResult,
                                      @RequestParam(value = "thumbnailFile", required = false) MultipartFile file,
                                      Errors errors,
                                      @RequestParam(value = "siteID", required = false) final Long siteID) throws
            IOException, EntityExistsException {

        RetailerSite retailerSite = null;
        boolean imageFileExtensionNotMatched = false;
        if (videoContent != null) {
            videoContent.setRetailerSite(retailerManager.getRetailerSite(videoContent.getRetailerSite().getId()));
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

        // Check the validation for Image input fields.
        validateFields(videoContent, bindingResult, file, model);


        if (!bindingResult.hasErrors()) {
            logger.info("No Validation error occurs");

            videoContent = documentManager.saveDocument(videoContent);

            /* Saving Tag for Video, after video object is loaded*/
            String tag= WebUtils.getParameter(request, "TagValue", "");
            if(StringUtils.isNotEmpty(tag))
            {
             logger.debug("Saving Tag for documentID "+videoContent.getId()+" is "+tag);
             tagManager.saveTags(tag, EntityConstants.Entities.VIDEO.getId(), videoContent.getId());
            }
            /*end*/

            if (videoContent.getId() != null) {

                MultiValueMap<String, MultipartFile> multipartFileMultiValueMap =
                        ((DefaultMultipartHttpServletRequest) request).getMultiFileMap();

                logger.info("Saved  Image Content[Without the Image]==>" + videoContent);

                String finalFileName = null;
                if (multipartFileMultiValueMap.containsKey("thumbnailFile")) {
                    file = multipartFileMultiValueMap.getFirst("thumbnailFile");
                    if (file != null && !file.isEmpty()) {
                        if (FileUtils.isFileExtensionAllowed(file.getOriginalFilename(), "image")) {
                            Assert.notNull(videoContent, "Trying to set the image property for null entity");
                            String baseCDNPathForDocument =
                                    documentManager.getBaseCDNPathForDocument(videoContent);
                            FileSystem fileSystem = FileSystem.getDefault();
                            String imageFileName = "videoThumbnailFile".concat(".png"); // Its only PNG for now
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
//                            videoContent.getProperties()
//                                    .setProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY, finalFileName);
                            videoContent.setImage(finalFileName);
//                            logger.info("Property value==>" + videoContent.getProperties());

                            logger.info("Image Location :" + videoContent.getProperties());

                        } else {
                            imageFileExtensionNotMatched = true;
                            model.addAttribute("imageFileExtensionNotMatched", imageFileExtensionNotMatched);
                        }
                    }
                }
/*                if (finalFileName != null) {
                    videoContent.setUrl(finalFileName);
                }*/
                videoContent = documentManager.saveDocument(videoContent);
                String redirectUrl =
                        new AdminCrumb(request.getContextPath())
                                .toAbsolute(
                                        AdminCrumb.linkById(AdminCrumb.URL_VIDEO_EDIT, videoContent.getId()));
                return new ModelAndView(new RedirectView(redirectUrl));
            } else {
                Long videoContentID = videoContent.getId();
                Assert.notNull(videoContentID, "Unable to save the video content type");

            }
        }
        logger.info("Validation error occurs");
        model.addAttribute("errors", errors);

        addCrumbs(request, model, AdminCrumb.TEXT_VIDEO_ADD,
                retailerManager.getRetailerSite(videoContent.getRetailerSite().getId()));

        String viewName = new AdminCrumb(request.getContextPath())
                .toView(AdminCrumb.URL_VIDEO_ADD);

        return new ModelAndView(viewName);
    }

    @RequestMapping(value = AdminCrumb.URL_VIDEO_EDIT, method = RequestMethod.GET)
    public void editVideoPage(@RequestParam Long id, Model model,
                              final HttpServletRequest request) throws
            EntityNotFoundException, IOException {

        FileSystem fileSystem = FileSystem.getDefault();
        Document videoContent = documentManager.getDocument(id);
//      https://jira.marketvine.com/browse/CS-450 - Handled the case where edit pages of content
            /*Populating existing tags of the Video*/
            String tags=this.tagManager.getTagsAsString(EntityConstants.Entities.VIDEO.getId(), videoContent.getId());
            model.addAttribute("tags", tags);
            /*End*/
        RetailerSite retailerSite = videoContent.getRetailerSite();
        Integer type = videoContent.getType();
        if (type == EntityConstants.Entities.VIDEO.getId()) {
            Assert.notNull(videoContent, "Image Content Not Found");
//        String finalImageFileName =
//                videoContent.getProperties().getProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY);
            String finalImageFileName = videoContent.getImage();

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
            model.addAttribute("document", videoContent);
        } else {
            model.addAttribute("invalidType", true);
            String contentTypeName = EntityConstants.Entities.getById(type).getValue();
            model.addAttribute("contentType", contentTypeName);
            logger.warn("User trying to load VIDEO with ID:= " + videoContent.getId() + " which is of type "
                    + contentTypeName);
        }
        addCrumbs(request, model, AdminCrumb.TEXT_VIDEO_EDIT, retailerSite);


    }


    @RequestMapping(value = AdminCrumb.URL_VIDEO_EDIT, method = RequestMethod.POST)
    public ModelAndView updateImagePage
            (
                    final Model model,
                    final HttpServletRequest request,
                    @ModelAttribute(value = "document") Document videoContent,
                    BindingResult bindingResult,
                    @RequestParam(value = "thumbnailFile", required = false) MultipartFile file,
                    Errors errors,
                    @RequestParam(value = "id", required = false) Long id,
                    @RequestParam(value = "reset_img_flag", required = false)
                    String reset_img_flag) throws
            IOException, EntityExistsException, EntityNotFoundException {

        RetailerSite retailerSite = null;
        boolean imageFileExtensionMatched = true;
        FileSystem fileSystem = FileSystem.getDefault();
        Assert.notNull(id, "No Image Record found for image id:=>" + id);
        logger.info("Image Record found");
        Document dbVideoContent = documentManager.getDocument(id);
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
        /*Updating the Tag of video*/
        String tagsExisting=tagManager.getTagsAsString(EntityConstants.Entities.VIDEO.getId(), dbVideoContent.getId());
        String tags=WebUtils.getParameter(request,"TagValue","");
        if(!tagsExisting.equals(tags))
        {
            logger.debug("Updating Tag for VIDEO "+dbVideoContent.getId()+" is "+tags);
            tagManager.saveTags(tags,EntityConstants.Entities.VIDEO.getId(), dbVideoContent.getId());
        }
        /* End */
        validateFields(videoContent, bindingResult, file, model);

        if (!bindingResult.hasErrors()) {
            logger.info("No Validation error occurs");

            MultiValueMap<String, MultipartFile> multipartFileMultiValueMap =
                    ((DefaultMultipartHttpServletRequest) request).getMultiFileMap();

            logger.info("Saved  Image Content[Without the Image]==>" + videoContent);

            String finalFileName = null;
            if (!StringUtils.isEmpty(file.getOriginalFilename())) {

                if (multipartFileMultiValueMap.containsKey("thumbnailFile")) {
                    file = multipartFileMultiValueMap.getFirst("thumbnailFile");
                    if (file != null && !file.isEmpty()) {
                        if (FileUtils.isFileExtensionAllowed(file.getOriginalFilename(), "image")) {
                            Assert.notNull(videoContent, "Trying to set the image property for null entity");
                            String baseCDNPathForDocument =
                                    documentManager.getBaseCDNPathForDocument(dbVideoContent);
                            String imageFileName = "videoThumbnailFile".concat(".png"); // Its only PNG for now
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
//                            dbVideoContent.getProperties()
//                                    .setProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY, finalFileName);
                            dbVideoContent.setImage(finalFileName);
//                            logger.info("Property value==>" + videoContent.getProperties());
                            model.addAttribute("newFileFound", true);
                            resetImage = false;
                            model.addAttribute("resetFlag", resetImage);

                        } else {
                            imageFileExtensionMatched = false;
                            model.addAttribute("imageFileExtensionMatched", imageFileExtensionMatched);
                        }
                    } else {
//                        videoContent.getProperties().setProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY,
//                                dbVideoContent.getProperties()
//                                        .getProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY));
                        videoContent.setImage(dbVideoContent.getImage());
                        model.addAttribute("uploadStatus", false);
                    }
                }
            } else {
//                videoContent.getProperties().setProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY,
//                        dbVideoContent.getProperties().getProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY));
                videoContent.setImage(dbVideoContent.getImage());
                if (resetImage) {
//                    String finalImageFileName =
//                            videoContent.getProperties().getProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY);
                    String finalImageFileName = videoContent.getImage();
                    if (finalImageFileName != null) {

                        try {
//                            File imageFile = new File(configurationService.getProperty("filesystem") +
//                                    dbVideoContent.getProperties()
//                                            .getProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY));
                            File imageFile = new File(configurationService.getProperty("filesystem") +
                                    dbVideoContent.getImage());
                            if (imageFile.exists()) {
                                imageFile.delete();
                            }
                            dbVideoContent.setImage(null);
//                            dbVideoContent.getProperties()
//                                    .setProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY, null);
                        } catch (Exception e) {
                            logger.error("File Not found in cdn directory");
                        }

                    } else {
                        logger.warn("Image File key-value pair not found");
                    }
                }
            }


            dbVideoContent.setName(videoContent.getName());
            dbVideoContent.setUrl(videoContent.getUrl());
            dbVideoContent.setDescription(videoContent.getDescription());
            dbVideoContent.setAuthor(videoContent.getAuthor());
            dbVideoContent.setSource(videoContent.getSource());
            dbVideoContent.setAbstractText(videoContent.getAbstractText());
            dbVideoContent.setPublishDate(videoContent.getPublishDate());

            videoContent = documentManager.saveDocument(dbVideoContent);
            String redirectUrl =
                    new AdminCrumb(request.getContextPath())
                            .toAbsolute(
                                    AdminCrumb.linkById(AdminCrumb.URL_VIDEO_EDIT, videoContent.getId()));
            return new ModelAndView(new RedirectView(redirectUrl));
        }/* else {
//            videoContent.getProperties().setProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY,
//                    dbVideoContent.getProperties().getProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY));
            videoContent.setImage(dbVideoContent.getImage());
        }*/

        logger.info("Validation error occurs");
        model.addAttribute("errors", errors);
        videoContent.setUrl(dbVideoContent.getUrl());
        if (!resetImage) {
            videoContent.setImage(dbVideoContent.getImage());
        } else {
            videoContent.setImage(null);
        }
        addCrumbs(request, model, AdminCrumb.TEXT_VIDEO_EDIT,
                retailerManager.getRetailerSite(videoContent.getRetailerSite().getId()));

        String viewName = new AdminCrumb(request.getContextPath())
                .toView(AdminCrumb.URL_VIDEO_EDIT);

        return new ModelAndView(viewName);
    }


    public void validateFields(Document entity, BindingResult bindingResult, MultipartFile file, Model model) {

        if (!StringUtils.isEmpty(entity.getName()))

        {
            if (StringUtils.isEmpty(entity.getName().trim())) {
                bindingResult.addError(new FieldError("document", "name", null, true,
                        new String[]{"NoSpace.name"}, null, ""));
            }
        } else

        {
            bindingResult.addError(new FieldError("document", "name", null, true,
                    new String[]{"NotEmpty.title"}, null, ""));
        }

        if (StringUtils.isEmpty(entity.getUrl()))

        {
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
        }


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
    private ConfigurationService configurationService;
    @Autowired
    private TagManager tagManager;


}