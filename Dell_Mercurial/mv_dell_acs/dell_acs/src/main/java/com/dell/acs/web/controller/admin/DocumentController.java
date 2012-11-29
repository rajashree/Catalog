package com.dell.acs.web.controller.admin;

import com.dell.acs.CampaignItemNotFoundException;
import com.dell.acs.DocumentAlreadyExistsException;
import com.dell.acs.content.EntityConstants;
import com.dell.acs.managers.CampaignManager;
import com.dell.acs.managers.DocumentManager;
import com.dell.acs.managers.RetailerManager;
import com.dell.acs.managers.TagManager;
import com.dell.acs.persistence.domain.CampaignItem;
import com.dell.acs.persistence.domain.Document;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.validator.spring.CustomDefaultBindingErrorProcessor;
import com.dell.acs.web.controller.BaseDellController;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.util.Assert;
import com.sourcen.core.util.FileSystem;
import com.sourcen.core.util.FileUtils;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.WebUtils;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dell.acs.web.crumbs.AdminCrumb;


/**
 Created by IntelliJ IDEA. User: Mahalakshmi Date: 4/26/12 Time: 3:03 PM To change this template use File | Settings |
 File Templates.
 */

@Controller
@SuppressWarnings("all")
public class DocumentController extends BaseDellController {

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
     Check the file extension corrected or not.

     @param fileName , store the uploaded file
     @return , response.
     */
    @RequestMapping(value = "admin/document/ajaxResponse.do", method = RequestMethod.GET)
    public
    @ResponseBody
    String ajaxResultForImageFormat(@RequestParam String fileName, @RequestParam String fileType) {
        logger.info("File Name===>" + fileName);
        String ajaxResponseStatus = "rejected";
        if (FileUtils.isFileExtensionAllowed(fileName, fileType)) {
            ajaxResponseStatus = fileType;
        } else {
            ajaxResponseStatus = fileType + "Rejected";
        }

        return ajaxResponseStatus;
    }

    @Deprecated
    @RequestMapping(value = "/admin/document/checkNameAvailability.do", method = RequestMethod.GET)
    public
    @ResponseBody
    String checkDocumentNameAvailability(
            @RequestParam String name, @RequestParam String status, @RequestParam String id) {
        boolean nameExistenceStatus = false;
        Long documentId = 0L;
        if (!id.equals("null")) {
            documentId = Long.parseLong(id);
        }
        if (status.equals("update")) {
            String updateDocumentName = null;
            if (documentId != 0) {
                updateDocumentName = documentManager.getDocumentNameByID(documentId, 2000);
                if (!name.equals(updateDocumentName)) {
                    try {
                        nameExistenceStatus = documentManager.checkNameExistence(name);
                    } catch (NonUniqueResultException nure) {
                        logger.error("Unique result not found");
                        nameExistenceStatus = true;
                    }
                }
            } else {
                logger.error(
                        "Unable to make an ajax call , because there is no document for document id::" + documentId);
            }
        } else {
            try {
                nameExistenceStatus = documentManager.checkNameExistence(name);
            } catch (NonUniqueResultException nure) {
                logger.error("Unique result not found");
                nameExistenceStatus = true;
            }
        }
        if (nameExistenceStatus)

        {
            return "found";
        } else

        {
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

    /**
     Method to initialize the add Document form.

     @param model - Model object for the document form
     */
    @RequestMapping(value = AdminCrumb.URL_DOCUMENT_ADD, method = RequestMethod.GET)
    public void initDocumentAddForm(HttpServletRequest request, Model model,
                                    @RequestParam(value = "siteID", required = true) Long siteID,
                                    @RequestParam(value = "flag", required = false) Boolean flag) {

        Document document = new Document();
        RetailerSite retailerSite = retailerManager.getRetailerSite(siteID);
        document.setRetailerSite(retailerSite);
        model.addAttribute("document", document);
        if (flag == null) {
            model.addAttribute("show", new Boolean(false));
        }
        addCrumbs(request, model, AdminCrumb.TEXT_DOCUMENT_ADD, retailerSite);
    }


    /**
     Method to save Document.

     @param model    - Document model attribute
     @param document - Model object
     @param binding  - Webbinding object
     @return - Redirect URL
     */
    @RequestMapping(value = AdminCrumb.URL_DOCUMENT_ADD, method = RequestMethod.POST)
    public ModelAndView saveDocument(HttpServletRequest request, Model model,
                                     @ModelAttribute("document") Document document,
                                     BindingResult binding,
                                     Errors errors,
                                     @RequestParam(value = "thumbnailFile", required = false) MultipartFile file,
                                     @RequestParam(value = "documentFile", required = false) MultipartFile docfile)
            throws EntityExistsException, IOException, MultipartException {

        boolean imageFileExtensionNotMatched = false;
        boolean docFileExtensionNotMatched = false;

        RetailerSite retailerSite = null;

        if (document != null) {
            retailerSite = retailerManager.getRetailerSite(document.getRetailerSite().getId());
            document.setRetailerSite(retailerSite);
        }

        // Check whether file has choosen or not.
        if (StringUtils.isEmpty(docfile.getOriginalFilename())) {
            binding.addError(new FieldError("document", "documentFile", null, true,
                    new String[]{"documentFile.null.error"}, null, ""));
            model.addAttribute("docStatus", new Boolean(true));
        }

        if (!StringUtils.isEmpty(document.getName())) {
            if (StringUtils.isEmpty(document.getName().trim())) {
                binding.addError(new FieldError("document", "name", null, true,
                        new String[]{"NoSpace.name"}, null, ""));
            }
        } else {
            binding.addError(new FieldError("document", "name", null, true,
                    new String[]{"NotEmpty.name"}, null, ""));
        }
        if (!StringUtils.isEmpty(document.getDescription())) {
            if (StringUtils.isEmpty(document.getDescription().trim())) {
                binding.addError(new FieldError("document", "description", null, true,
                        new String[]{"NoSpace.description"}, null, ""));
            }
        } else {
            binding.addError(new FieldError("document", "description", null, true,
                    new String[]{"NotEmpty.description"}, null, ""));
        }

        if (!binding.hasErrors()) {
            document = documentManager.saveDocument(document);

            /* Saving the document tag to database, Upon getting the document object */
            String tag = WebUtils.getParameter(request, "TagValue", "");
            if (StringUtils.isNotEmpty(tag)) {
                logger.debug("Tag for documentID " + document.getId() + " is " + tag);
                tagManager.saveTags(tag, EntityConstants.Entities.DOCUMENT.getId(), document.getId());
            }
            /*End*/

            MultiValueMap<String, MultipartFile> multipartFileMultiValueMap =
                    ((DefaultMultipartHttpServletRequest) request).getMultiFileMap();
            logger.info("Saved  Document[Without the Image]==>" + document);

            if (multipartFileMultiValueMap.containsKey("thumbnailFile")) {
                file = multipartFileMultiValueMap.getFirst("thumbnailFile");
                if (file != null && !file.isEmpty()) {
                    /*allowedImageFileExtensions
                            .contains(FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase())*/
                    if (FileUtils.isFileExtensionAllowed(file.getOriginalFilename(), "image")) {
                        Assert.notNull(document, "Trying to set the image property for null entity");
                        String baseCDNPathForDocument =
                                documentManager.getBaseCDNPathForDocument(document);
                        FileSystem fileSystem = FileSystem.getDefault();
                        String imageFileName = "thumbnailFile".concat(".png"); // Its only PNG for now
                        String finalFileName = baseCDNPathForDocument.concat(imageFileName);
                        logger.info(finalFileName);
                        boolean exists = new File(finalFileName).exists();
                        if (exists) {
                            new File(finalFileName).delete();
                        }
                        File imageFile = fileSystem.getFile(finalFileName, true, true);
                        file.transferTo(imageFile);
                        logger.info(" image saved in - " + imageFile.getAbsolutePath());
                        //Sandeep - JIRA-CS-483
//                        document.getProperties()
//                                .setProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY, finalFileName);
                        document.setImage(finalFileName);
//                        logger.info("Property value==>" + document.getProperties());
                        logger.info("Image file := " + document.getImage());

                    } else {
                        imageFileExtensionNotMatched = true;
                        model.addAttribute("imageFileExtensionNotMatched", imageFileExtensionNotMatched);
                    }
                }
            }

            if (multipartFileMultiValueMap.containsKey("documentFile")) {
                docfile = multipartFileMultiValueMap.getFirst("documentFile");
                if (docfile != null && !docfile.isEmpty()) {
                    if (FileUtils.isFileExtensionAllowed(docfile.getOriginalFilename(), "document")) {

                        Assert.notNull(document, "Trying to set the document for null entity");
                        String baseCDNPathForDocument =
                                documentManager.getBaseCDNPathForDocument(document);
                        FileSystem fileSystem = FileSystem.getDefault();
                        String finalFileName = baseCDNPathForDocument.concat(docfile.getOriginalFilename());
                        logger.info(finalFileName);
                        boolean exists = new File(finalFileName).exists();
                        if (exists) {
                            new File(finalFileName).delete();
                        }
                        File documentFile = fileSystem.getFile(finalFileName, true, true);
                        docfile.transferTo(documentFile);
                        logger.info(" Document saved in - " + documentFile.getAbsolutePath());
                        document.setDocument(finalFileName);
                    } else {
                        docFileExtensionNotMatched = true;
                        model.addAttribute("docFileExtensionNotMatched", docFileExtensionNotMatched);
                        model.addAttribute("docFlag", false);
                    }
                }
            }
            document = documentManager.saveDocument(document);

            //documentBean = FormBeanConverter.convert(documentBean, documentBean);

            logger.info("Property value" + document.getProperties());
            model.addAttribute("document", document);
            logger.info("Successfully created document - " + document.getName());

            String redirectUrl =
                    new AdminCrumb(request.getContextPath())
                            .toAbsolute(AdminCrumb.linkById(AdminCrumb.URL_DOCUMENT_EDIT, document.getId()));
            return new ModelAndView(new RedirectView(redirectUrl));

        }

        model.addAttribute("show", new Boolean(true));
        model.addAttribute("errors", errors);

        addCrumbs(request, model, AdminCrumb.TEXT_DOCUMENT_ADD,
                retailerManager.getRetailerSite(document.getRetailerSite().getId()));

        String viewName = new AdminCrumb(request.getContextPath())
                .toView(AdminCrumb.URL_DOCUMENT_ADD);
        return new ModelAndView(viewName);
    }

    /**
     Edit document form initializer method.

     @param id    - Document id
     @param model - Model attribute for edit document form
     */
    @RequestMapping(value = AdminCrumb.URL_DOCUMENT_EDIT, method = {RequestMethod.GET})
    public void initDocumentEditForm(
            @RequestParam Long id,
            @RequestParam(required = false) String flag,
            @RequestParam(required = false) String docFlag,
            HttpServletRequest request, Model model) throws IOException {

        logger.info("load id :=" + id);
        boolean imageFileUnsupportedFormat = false;
        boolean docFileUnsupportedFormat = false;
        try {
            Document document = documentManager.getDocument(id);

            /*Populating existing tags of the Docuement*/
            String tags = this.tagManager
                    .getTagsAsString(EntityConstants.Entities.DOCUMENT.getId(), document.getId());
            model.addAttribute("tags", tags);
            /*End*/


            if (document != null) {
//      https://jira.marketvine.com/browse/CS-450 - Handled the case where edit pages of content
//                types were corrupting the data of other content types
                Integer type = document.getType();
                if (type == EntityConstants.Entities.DOCUMENT.getId()) {
                    document.setId(id);
                    //Check  document existence or  permission problem in database
                    if (!StringUtils.isEmpty(document.getDocument())) {
                        //Check  document existence or  permission problem.
                        boolean chkDocFileExistenceStatus = new File(configurationService.getProperty("filesystem") +
                                document.getDocument().substring(1, document.getDocument().length())).exists();

                        if (chkDocFileExistenceStatus) {
                            model.addAttribute("docFileExistenceStatus", "true");
                        } else {
                            model.addAttribute("docFileExistenceStatus", "false");
                        }
                    } else {
                        model.addAttribute("docFileExistenceStatus", "false");
                    }
                    //Check whether image file is exist or having permission to read or not.
                    //Sandeep - JIRA-CS-483
//                logger.info(
//                        " Image File Location " + document.getProperties().getProperty("dell.document.thumbnail"));
//                File file = new File(configurationService.getProperty("filesystem") +
//                        document.getProperties().getProperty("dell.document.thumbnail"));
                    logger.info(
                            " Image File Location " + document.getImage());
                    File file = new File(configurationService.getProperty("filesystem") + document.getImage());

                    boolean exists = file.exists();

                    if (exists) {
                        // It returns false if File or directory does not exist
                        logger.info("Image file exist");
                        /**
                         flag check for reseting the image status.
                         */
                        if (flag != null && flag.equals("true")) {
                            //model.addAttribute("imageExistOrAccessStatus", "true");
                            //Sandeep - JIRA-CS-483
//                        document.getProperties().setProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY, "");
                            try {
                                documentManager.saveDocument(document);
                            } catch (Exception e) {
                                logger.debug("Duplicate Entry for document id=" + document.getId());
                            }
                            file.delete();
                            logger.info("File Name: " + file.getName() + ", flag Status" + flag);

                            model.addAttribute("resetImageStatus", "true");
                            model.addAttribute("imageStatus", "false");
                        } else {
                            logger.info("File Name: " + file.getName() + ", flag Status" + flag);
                            //model.addAttribute("imageExistOrAccessStatus", "true");
                            if ((String) request.getParameter("imageFileExtensionNotMatched") == null) {
                                model.addAttribute("resetImageStatus", "false");
                            } else {
                                model.addAttribute("resetImageStatus", "true");
                            }
                            /* model.addAttribute("imageExtensionMatchStatus", "false");*/
                            if (file.getName().indexOf('.') == -1) {
                                if ((String) request.getParameter("imageFileExtensionNotMatched") == null) {
                                    model.addAttribute("uploadStatus", "false");
                                } else {
                                    model.addAttribute("uploadStatus", "true");
                                }
                            }
                            model.addAttribute("imageStatus", "true");
                        }
                    } else {
                        // It returns true if File or directory exists
                        logger.debug("Image file not exist");
                        logger.info("Image file----- :" + file.getName());
                        if ((String) request.getParameter("imageFileExtensionNotMatched") != null) {
                            imageFileUnsupportedFormat =
                                    Boolean.parseBoolean((String) request.getParameter("imageFileExtensionNotMatched"));
                            model.addAttribute("imageFileExtensionNotMatched", imageFileUnsupportedFormat);

                        }
                        model.addAttribute("imageStatus", "false");
                        model.addAttribute("resetImageStatus", "false");
                        model.addAttribute("imageExtensionMatchStatus", "false");
                    }
                    if ((String) request.getParameter("docFileExtensionNotMatched") != null) {
                        docFileUnsupportedFormat =
                                Boolean.parseBoolean((String) request.getParameter("docFileExtensionNotMatched"));
                        model.addAttribute("docFileExtensionNotMatched", docFileUnsupportedFormat);
                        if (docFlag.equals("false")) {
                            model.addAttribute("docFlag", "false");
                        }

                    }
                    model.addAttribute("document", document);
                    model.addAttribute("invalidType", false);
                    model.addAttribute("docName", FilenameUtils.getName(document.getDocument()));
                } else {
                    model.addAttribute("invalidType", true);
                    String contentTypeName = EntityConstants.Entities.getById(type).getValue();
                    model.addAttribute("contentType", contentTypeName);
                    logger.warn("User trying to load DOCUMENT with ID:= " + document.getId() + " which is of type "
                            + contentTypeName);
                }
            }
            addCrumbs(request, model, AdminCrumb.TEXT_DOCUMENT_EDIT,
                    retailerManager.getRetailerSite(document.getRetailerSite().getId()));

        } catch (EntityNotFoundException cnEx) {
            logger.info("Unable to load the document");
            throw new RuntimeException("Document not found.");
        }
    }

    /**
     Method to handle the Edit form submit.

     @param model          - Model attribute for Document Edit form
     @param documentUpdate - binded Document object
     @param binding        - Webdata binding object
     @return return - redirect string
     */

    @RequestMapping(value = AdminCrumb.URL_DOCUMENT_EDIT, method = RequestMethod.POST)
    public ModelAndView updateDocument(Model model,
                                       @ModelAttribute("document")
                                       Document documentUpdate, BindingResult binding, Errors errors,
                                       @RequestParam(value = "thumbnailFile", required = false) MultipartFile file,
                                       @RequestParam(value = "documentFile", required = false) MultipartFile docfile,
                                       @RequestParam(required = false)
                                       String reset_img_flag, HttpServletRequest request,
                                       @RequestParam(value = "id", required = false) Long id) throws
            EntityNotFoundException {


        String imageResetflag = reset_img_flag;
        boolean docExtensionMatched = false;
        boolean docFileExtensionNotMatched = false;
        String finalImageFileName = null;
        String finalDocFileName = null;
        Document documentEntity = documentManager.getDocument(id, 2000);
        FileSystem fileSystem = FileSystem.getDefault();

        /* Updating the document tag to database, Upon getting the document object */

        String tagsExisting = tagManager
                .getTagsAsString(EntityConstants.Entities.DOCUMENT.getId(), documentEntity.getId());
        String tags = WebUtils.getParameter(request, "TagValue", "");

        if (!tagsExisting.equals(tags)) {
            tagManager.saveTags(tags, EntityConstants.Entities.DOCUMENT.getId(), documentEntity.getId());
        }
        /* END */

        // Check whether file has choosen or not.
        if (StringUtils.isEmpty(docfile.getOriginalFilename())) {
            if (StringUtils.isEmpty(documentEntity.getDocument())) {
                binding.addError(new FieldError("document", "documentFile", null, true,
                        new String[]{"documentFile.null.error"}, null, ""));
                model.addAttribute("docStatus", new Boolean(true));
            }
        }

        if (!StringUtils.isEmpty(documentUpdate.getName())) {
            if (StringUtils.isEmpty(documentUpdate.getName().trim())) {
                binding.addError(new FieldError("document", "name", null, true,
                        new String[]{"NoSpace.name"}, null, ""));
            } else {
                documentEntity.setName(documentUpdate.getName());
            }
        } else {
            binding.addError(new FieldError("document", "name", null, true,
                    new String[]{"NotEmpty.name"}, null, ""));
        }
        if (!StringUtils.isEmpty(documentUpdate.getDescription())) {
            if (StringUtils.isEmpty(documentUpdate.getDescription().trim())) {
                binding.addError(new FieldError("document", "description", null, true,
                        new String[]{"NoSpace.description"}, null, ""));
            } else {
                documentEntity.setDescription(documentUpdate.getDescription());
            }
        } else {
            binding.addError(new FieldError("document", "description", null, true,
                    new String[]{"NotEmpty.description"}, null, ""));
        }

        logger.info("Updating the ->" + documentEntity.getName() + " document for id(" + documentEntity.getId() + ")");

        MultiValueMap<String, MultipartFile> multipartFileMultiValueMap =
                ((DefaultMultipartHttpServletRequest) request).getMultiFileMap();
        try {


            if (!StringUtils.isEmpty(file.getOriginalFilename())) {
                if (multipartFileMultiValueMap.containsKey("thumbnailFile")) {
                    file = multipartFileMultiValueMap.getFirst("thumbnailFile");
                    if (file != null && !file.isEmpty()) {
                        if (FileUtils.isFileExtensionAllowed(file.getOriginalFilename(), "image")) {

                            String baseCDNPathForDocument =
                                    documentManager.getBaseCDNPathForDocument(documentEntity);
                            fileSystem = FileSystem.getDefault();
                            String imageFileName = "thumbnailFile".concat(".png"); // Its only PNG for now
                            finalImageFileName = baseCDNPathForDocument.concat(imageFileName);
                            logger.info(finalImageFileName);
                            boolean exists = new File(finalImageFileName).exists();
                            if (exists) {
                                new File(finalImageFileName).delete();
                            }

                        } else {
                            docExtensionMatched = true;
                            model.addAttribute("imageFileExtensionNotMatched", docExtensionMatched);
                        }

                    }
                }
            } else {
                //Sandeep - JIRA-CS-483
//                documentUpdate.getProperties().setProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY,
//                        documentEntity.getProperties().getProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY));
                documentUpdate.setImage(documentEntity.getImage());
            }

            if (multipartFileMultiValueMap.containsKey("documentFile")) {
                docfile = multipartFileMultiValueMap.getFirst("documentFile");
                if (docfile != null && !docfile.isEmpty()) {
                    if (FileUtils.isFileExtensionAllowed(docfile.getOriginalFilename(), "document")) {
                        String baseCDNPathForDocument =
                                documentManager.getBaseCDNPathForDocument(documentEntity);
                        finalDocFileName = baseCDNPathForDocument.concat(docfile.getOriginalFilename());
                        logger.info("Uploaded document name==> " + finalDocFileName);

                        // If the uploaded file already exist, then the delete the exist one and update the latest one in cdn directory
                        boolean exists =
                                new File(configurationService.getProperty("filesystem") + finalDocFileName)
                                        .exists();
                        if (exists) {
                            new File(configurationService.getProperty("filesystem") + finalDocFileName).delete();
                        }

                        // If the uploaded file is different from the existing one , then the delete the exist one and update the latest one in cdn directory .
                        boolean chkDocFileExistenceStatus =
                                new File(configurationService.getProperty("filesystem") +
                                        documentUpdate.getDocument())
                                        .exists();
                        if (chkDocFileExistenceStatus) {
                            new File(configurationService.getProperty("filesystem") + documentUpdate.getDocument())
                                    .delete();
                        }
                    } else {
                        docFileExtensionNotMatched = true;
                        model.addAttribute("docFileExtensionNotMatched", docFileExtensionNotMatched);
                        model.addAttribute("docFlag", false);
                    }

                } else {
                    documentUpdate.setDocument(documentEntity.getDocument());
                }
            }

        } catch (MultipartException mpEx) {
            logger.error("Unable to update the document. " + mpEx);
        }


        documentEntity.setAuthor(documentUpdate.getAuthor());
        documentEntity.setSource(documentUpdate.getSource());
        documentEntity.setAbstractText(documentUpdate.getAbstractText());


        if (!binding.hasErrors()) {
            if (imageResetflag != null) {
                if (imageResetflag.equals("true")) {
                    //Sandeep - JIRA-CS-483
//                    File imageFile = new File(configurationService.getProperty("filesystem")
//                            .substring(0, (configurationService.getProperty("filesystem")).length() - 1) +
//                            documentEntity.getProperties()
//                                    .getProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY));
                    File imageFile = new File(configurationService.getProperty("filesystem")
                            .substring(0, (configurationService.getProperty("filesystem")).length() - 1) +
                            documentEntity.getImage());
                    if (imageFile.exists()) {
                        imageFile.delete();
                    }
                }
            }

            try {
                if (!StringUtils.isEmpty(finalImageFileName)) {

                    //Sandeep - JIRA-CS-483
//                    documentEntity.getProperties()
//                            .setProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY,
//                                    finalImageFileName);
                    documentEntity.setImage(finalImageFileName);

                    File imageFile = fileSystem.getFile(finalImageFileName, true, true);
                    file.transferTo(imageFile);
                    logger.info(" image saved in - " + imageFile.getAbsolutePath());
                }
                if (!StringUtils.isEmpty(finalDocFileName)) {

                    File documentFile = fileSystem.getFile(finalDocFileName, true, true);
                    docfile.transferTo(documentFile);
                    logger.info(" Document saved in - " + documentFile.getAbsolutePath());
                    //Set property for 'image'
//                    logger.info("Property value" + documentEntity.getProperties());
                    documentEntity.setDocument(finalDocFileName);
                }
                documentEntity.setStartDate(documentUpdate.getStartDate());
                documentEntity.setEndDate(documentUpdate.getEndDate());
                documentEntity.setPublishDate(documentUpdate.getPublishDate());
                documentEntity = documentManager.saveDocument(documentEntity);
                model.addAttribute("document", documentEntity);

            } catch (EntityExistsException e) {
                logger.error("Document with this name already exists.");
            } catch (IOException ioExcep) {
                logger.error("Unable to save the uploaded files.", ioExcep);
            }

            String redirectUrl = new AdminCrumb(request.getContextPath())
                    .toAbsolute(AdminCrumb.linkById(AdminCrumb.URL_DOCUMENT_EDIT, documentUpdate.getId()));
            return new ModelAndView(new RedirectView(redirectUrl));
        }

        // Check the reset image status and image status
        if (imageResetflag != null) {
            if (imageResetflag.equals("true")) {

                model.addAttribute("resetImageStatus", "true");
                model.addAttribute("imageStatus", "false");

            } else {
                model.addAttribute("imageStatus", "true");
            }

        } else {
            model.addAttribute("imageStatus", "true");
        }
        if (!StringUtils.isEmpty(finalImageFileName)) {
            //Sandeep - JIRA-CS-483
//            documentEntity.getProperties()
//                    .setProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY, finalImageFileName);
            documentEntity.setImage(finalImageFileName);
        }
        // Check the document status
        if (!StringUtils.isEmpty(documentUpdate.getDocument())) {
            //Check  document existence or  permission problem.
            boolean chkDocFileExistenceStatus = new File(configurationService.getProperty("filesystem") +
                    documentUpdate.getDocument().substring(1, documentUpdate.getDocument().length())).exists();
            if (chkDocFileExistenceStatus) {
                model.addAttribute("docFileExistenceStatus", "true");
            } else {
                model.addAttribute("docFileExistenceStatus", "false");
            }
        } else {
            model.addAttribute("docFileExistenceStatus", "false");
        }
        if (!docExtensionMatched) {
            model.addAttribute("docImageExtensionStatus", new Boolean(true));
        }

        if (docFileExtensionNotMatched) {
            model.addAttribute("docFileExtensionNotMatched", new Boolean(true));
        }

        model.addAttribute("show", new Boolean(true));
        model.addAttribute("docName", FilenameUtils.getName(documentUpdate.getDocument()));
        model.addAttribute("errors", errors);

        addCrumbs(request, model, AdminCrumb.TEXT_DOCUMENT_EDIT,
                retailerManager.getRetailerSite(documentUpdate.getRetailerSite().getId()));
        String viewName = new AdminCrumb(request.getContextPath())
                .toView(AdminCrumb.URL_DOCUMENT_EDIT);
        return new ModelAndView(viewName);
    }

    /**
     Method to delete the Document by id.

     @param id    - id of document to delete
     @param model - model attribute for model
     @return - return the redirect string
     */
    // MWE - not modified for crumbs
    @Deprecated
    @RequestMapping(value = "admin/document/delete.json", method = RequestMethod.POST)
    public ModelAndView deleteDocument(
            @RequestParam final Long id, final Model model,
            @RequestParam(value = "deleteDocStatus", required = true) boolean deleteDocStatus) {

        logger.info("load id :=" + id);

        ModelAndView modelAndView = new ModelAndView("jsonView");

        /*
          Start: Collecting the campaign name for used document in campaign_items.
         */

        String startBracket = "[";

        Map<Long, String> campaignItemIdAndCampaignNameMap = new HashMap<Long, String>();
        Map<Long, Map<Long, String>> campaignItemMap = new HashMap<Long, Map<Long, String>>();
        List<CampaignItem> associatedCampaignItemListForDocument =
                campaignManager.getCampaignsDetailsByDocumentId(id);
        logger.info("Associated camapign list:= " + associatedCampaignItemListForDocument);
        if (associatedCampaignItemListForDocument != null && associatedCampaignItemListForDocument.size() > 0) {
            Iterator iterator = associatedCampaignItemListForDocument.iterator();
            while (iterator.hasNext()) {
                CampaignItem campaignItem = (CampaignItem) iterator.next();
                campaignItemIdAndCampaignNameMap
                        .put(campaignItem.getCampaign().getId(), campaignItem.getCampaign().getName());
                campaignItemMap.put(campaignItem.getId(), campaignItemIdAndCampaignNameMap);
            }
            logger.info("Associated Campaign Name " + "[" +
                    StringUtils.join(campaignItemIdAndCampaignNameMap.values(), ",") + "]");
            modelAndView.addObject("success", true);
            modelAndView
                    .addObject("campaignNameList",
                            "[" + StringUtils.join(campaignItemIdAndCampaignNameMap.values(), ",") + "]");
        }
        logger.info("Document deleting process should start..." + deleteDocStatus);
        try {
            if (deleteDocStatus) {
                logger.info("Document deleting process should start..." + deleteDocStatus);
                // delete the document directory from cdn directory.
                Document document = documentManager.getDocument(id);

                if (document != null) {

                    FileSystem fileSystem = FileSystem.getDefault();
                    File checkImageFileExistence = null;
                    File checkDocumentFileExistence = null;
                    // Delete the thumnail image file if exists
                    //Sandeep - JIRA-CS-483
//                    if (document.getProperties().getProperty(
//                            DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY) != null) {
                    if (document.getImage() != null) {
//                        checkImageFileExistence = new File(
//                                configurationService.getProperty("filesystem") +
//                                        document.getProperties().getProperty(
//                                                DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY));
                        checkImageFileExistence = new File(
                                configurationService.getProperty("filesystem") +
                                        document.getImage());
                        if (checkImageFileExistence.exists()) {
                            if (checkImageFileExistence.delete()) {
                                logger.info("Document image file has been deleted from document cdn directory");
                            } else {
                                logger.debug(
                                        "Unable to delete the image file ,either document image file doesn't exist or permission issue.");
                            }
                        } else {
                            logger.debug(
                                    "Unable to delete the image file ,either document cdn directory or image file doesn't exist or permission issue.");
                        }
                    }


                    // Delete the uploaded document file if exists
                    try {
                        if (new File(configurationService.getProperty("filesystem") + document.getDocument())
                                .exists()) {
                            checkDocumentFileExistence = fileSystem.getFile(document.getDocument());
                            if (checkDocumentFileExistence.delete()) {
                                logger.info("Document uploaded file has been deleted from document cdn directory");
                            } else {
                                logger.debug(
                                        "Unable to delete the file ,either document uploaded file doesn't exist or permission issue.");
                            }
                        } else {
                            logger.debug(
                                    "Unable to delete the uploaded file ,either document uploaded file or document cdn directory doesn't exist or permission issue.");
                        }
                    } catch (IOException ioex) {
                        logger.debug(
                                "Unable to delete the file ,either document uploaded file doesn't exist or permission issue.");
                    }

                    // Delete the this document CDN directory - name <document_id>
                    try {
                        String cdnPathDirectory = documentManager.getBaseCDNPathForDocument(document);
                        File checkDocumentDirectoryExistorNot = FileSystem.getDefault()
                                .getFile(cdnPathDirectory.substring(0, cdnPathDirectory.lastIndexOf("/")));
                        if (checkDocumentDirectoryExistorNot.isDirectory()) {
                            if (checkDocumentDirectoryExistorNot.list().length == 0) {
                                checkDocumentDirectoryExistorNot.delete();
                            } else {
                                // If any files exists other than the thumbnail and document file will be deleted
                                String files[] = checkDocumentDirectoryExistorNot.list();
                                for (String temp : files) {
                                    //construct the file structure
                                    File fileDelete = new File(checkDocumentDirectoryExistorNot, temp);
                                    fileDelete.delete();
                                }
                                if (checkDocumentDirectoryExistorNot.list().length == 0) {
                                    checkDocumentDirectoryExistorNot.delete();
                                }
                            }
                            checkDocumentDirectoryExistorNot.delete();
                        }
                    } catch (IOException e) {
                        logger.error("Unable to delete the document directory " + document.getId() +
                                " ,may be either directory not exist or permission issue.");
                    }
                }
                // delete the document and it's property from database
                documentManager.deleteDocument(id);
                //delete the associted campaign item and product with the document.

                for (Map.Entry<Long, Map<Long, String>> mapEntry : campaignItemMap.entrySet()) {
                    for (Map.Entry<Long, String> maps : mapEntry.getValue().entrySet()) {

                        try {
                            /*campaignManager.deleteAssociatedCampaignItem(mapEntry.getKey());*/
                            campaignManager.deleteCampaignItem(mapEntry.getKey(), maps.getKey());
                        } catch (CampaignItemNotFoundException e) {
                            modelAndView.addObject("success", false);
                            logger.debug("No document associated with the campign_item id: " + mapEntry.getKey());
                        }
                    }
                }
                logger.info("Document information deleted from cdn directory and database....");
            } else {
                modelAndView.addObject("success", true);
                logger.error("Please provide the confirmation to delete the document");
            }
        } catch (EntityNotFoundException e) {
            logger.error("Unable to load the document from repository. ID  " + id, e);
            modelAndView.addObject("success", false);
        }
        return modelAndView;
    }

    private Document getDocument(Long id) throws EntityNotFoundException {
        return documentManager.getDocument(id);
    }

    /**
     Setter for configurationService
     */
    public void setConfigurationService(final ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public void setCampaignManager(final CampaignManager campaignManager) {
        this.campaignManager = campaignManager;
    }

    /**
     DocumentManager autowiring by name.
     */
    @Autowired
    private DocumentManager documentManager;

    /**
     RetailerManager autowiring by name.
     */
    @Autowired
    private RetailerManager retailerManager;

    /**
     ConfigurationService data injection.
     */
    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private CampaignManager campaignManager;

    @Autowired
    private TagManager tagManager;
}
