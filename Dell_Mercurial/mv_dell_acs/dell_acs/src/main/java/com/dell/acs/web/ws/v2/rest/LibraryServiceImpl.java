/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v2.rest;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.managers.DocumentManager;
import com.dell.acs.managers.TagManager;
import com.dell.acs.persistence.domain.Document;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.domain.User;
import com.dell.acs.web.ws.v2.LibraryService;
import com.sourcen.core.InvalidArgumentException;
import com.sourcen.core.config.ConfigurationServiceImpl;
import com.sourcen.core.util.DateUtils;
import com.sourcen.core.util.FileSystem;
import com.sourcen.core.util.FileUtils;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.WebUtils;
import com.sourcen.core.util.beans.DocumentFilterBean;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.jws.WebService;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.WebServiceException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 @author Sandeep Heggi
 @author $LastChangedBy: Sandeep $ */
@WebService
@RequestMapping("/api/v2/rest/LibraryService")
public class LibraryServiceImpl extends WebServiceImpl implements LibraryService {

    private String cdnPrefix = ConfigurationServiceImpl.getInstance().getProperty("filesystem.cdnPrefix", "");

    public static final Collection<String> allowedImageFileExtensions =
            // Image file extension.
            Arrays.asList("jpg", "gif", "png");

    /**
     {@inheritDoc}
     */
    @Override
    @RequestMapping("getDocumentsBySite")
    public Collection<Document> getDocumentsBySite(@ModelAttribute ServiceFilterBean filter,
                                                   @RequestParam(required = true) Object site)
            throws EntityNotFoundException {

        RetailerSite retailerSite = retailerManager.getRetailerSite(site);
        //Type of the content is document type
        Integer documentType = EntityConstants.Entities.DOCUMENT.getId();
        Collection<Document> documents = documentManager.getDocuments(retailerSite.getId(), documentType, filter);
        for (Document document : documents) {
            setTagForContentType(document, documentType, document.getId());
            if (document.getImage() != null) {
                document.setImage(cdnPrefix + document.getImage());
            }
            document.setDocument(cdnPrefix + document.getDocument());
        }
        return documents;
    }

    /**
     {@inheritDoc}
     */
    @Override
    @RequestMapping("getArticlesBySite")
    public Collection<Document> getArticlesBySite(@ModelAttribute ServiceFilterBean filter,
                                                  @RequestParam(required = true) Object site)
            throws EntityNotFoundException {

        RetailerSite retailerSite = retailerManager.getRetailerSite(site);
        //Type of the content is Article type
        Integer articleType = EntityConstants.Entities.ARTICLE.getId();
        Collection<Document> articles = documentManager.getDocuments(retailerSite.getId(), articleType, filter);
        for (Document document : articles) {
            setTagForContentType(document, articleType, document.getId());
        }
        return articles;
    }


    /**
     {@inheritDoc}
     */
    @Override
    @RequestMapping("getVideosBySite")
    public Collection<Document> getVideosBySite(@ModelAttribute ServiceFilterBean filter,
                                                @RequestParam(required = true) Object site)
            throws EntityNotFoundException {

        RetailerSite retailerSite = retailerManager.getRetailerSite(site);
        //Type of the content is Video type
        Integer videoType = EntityConstants.Entities.VIDEO.getId();
        Collection<Document> videos = documentManager.getDocuments(retailerSite.getId(), videoType, filter);
        for (Document document : videos) {
            setTagForContentType(document, videoType, document.getId());
            if (document.getImage() != null) {
                document.setImage(cdnPrefix + document.getImage());
            }
        }
        return videos;
    }

    /**
     {@inheritDoc}
     */
    @Override
    @RequestMapping("getImagesBySite")
    public Collection<Document> getImagesBySite(@ModelAttribute ServiceFilterBean filter,
                                                @RequestParam(required = true) Object site)
            throws EntityNotFoundException {

        RetailerSite retailerSite = retailerManager.getRetailerSite(site);
        //Type of the content is Image type
        Integer imageType = EntityConstants.Entities.IMAGE.getId();
        Collection<Document> images = documentManager.getDocuments(retailerSite.getId(), imageType, filter);
        for (Document document : images) {
            setTagForContentType(document, imageType, document.getId());
            if (document.getImage() != null) {
                document.setImage(cdnPrefix + document.getImage());
            }
            if (document.getUrl() != null) {
                document.setUrl(cdnPrefix + document.getUrl());
            }
        }

        return images;
    }

    /**
     {@inheritDoc}
     */
    @Override
    @RequestMapping("getLinksBySite")
    public Collection<Document> getLinksBySite(@ModelAttribute ServiceFilterBean filter,
                                               @RequestParam(required = true) Object site)
            throws EntityNotFoundException {

        RetailerSite retailerSite = retailerManager.getRetailerSite(site);
        //Type of the content is Link type
        Integer linkType = EntityConstants.Entities.LINK.getId();
        Collection<Document> links = documentManager.getDocuments(retailerSite.getId(), linkType, filter);
        for (Document document : links) {
            setTagForContentType(document, linkType, document.getId());
        }
        return links;
    }


    //Services for getting the Content type details  by ID(s)

    /**
     {@inheritDoc}
     */
    @Override
    @RequestMapping("getDocumentDetails")
    public Collection<Document> getDocumentDetails(@RequestParam(required = true) String documentID)
            throws EntityNotFoundException {
        //Content type will be document
        Integer documentType = EntityConstants.Entities.DOCUMENT.getId();
        Collection<Document> documents = this.getDocumentsByContentType(documentID, documentType);
        for (Document document : documents) {
            setTagForContentType(document, documentType, document.getId());
            if (document.getImage() != null) {
                document.setImage(cdnPrefix + document.getImage());
            }
            document.setDocument(cdnPrefix + document.getDocument());
        }
        return documents;
    }

    /**
     {@inheritDoc}
     */
    @Override
    @RequestMapping("getArticleDetails")
    public Collection<Document> getArticleDetails(@RequestParam(required = true) String articleID)
            throws EntityNotFoundException {
        Integer articleType = EntityConstants.Entities.ARTICLE.getId();
        Collection<Document> articles=this.getDocumentsByContentType(articleID, articleType);
        for (Document document : articles) {
            setTagForContentType(document, articleType, document.getId());
        }
        return articles;
    }

    /**
     {@inheritDoc}
     */
    @Override
    @RequestMapping("getVideoDetails")
    public Collection<Document> getVideoDetails(@RequestParam(required = true) String videoID)
            throws EntityNotFoundException {
        Integer videoType = EntityConstants.Entities.VIDEO.getId();
        Collection<Document> videos = this.getDocumentsByContentType(videoID, videoType);
        for (Document document : videos) {
            setTagForContentType(document, videoType, document.getId());
            if (document.getImage() != null) {
                document.setImage(cdnPrefix + document.getImage());
            }
        }
        return videos;
    }

    /**
     {@inheritDoc}
     */
    @Override
    @RequestMapping("getImageDetails")
    public Collection<Document> getImageDetails(@RequestParam(required = true) String imageID)
            throws EntityNotFoundException {
        Integer imageType = EntityConstants.Entities.IMAGE.getId();
        Collection<Document> images = this.getDocumentsByContentType(imageID, imageType);
        for (Document document : images) {
            setTagForContentType(document, imageType, document.getId());
            if (document.getImage() != null) {
                document.setImage(cdnPrefix + document.getImage());
            }
            if (document.getUrl() != null) {
                document.setUrl(cdnPrefix + document.getUrl());
            }
        }
        return images;
    }

    /**
     {@inheritDoc}
     */
    @Override
    @RequestMapping("getLinkDetails")
    public Collection<Document> getLinkDetails(@RequestParam(required = true) String linkID)
            throws EntityNotFoundException {
        Integer linkType = EntityConstants.Entities.LINK.getId();
        Collection<Document> links=this.getDocumentsByContentType(linkID, linkType);
        for (Document document : links) {
            setTagForContentType(document, linkType, document.getId());
        }
        return links;
    }

    @Override
    @Consumes(value = MediaType.MULTIPART_FORM_DATA)
    @RequestMapping(value = "createDocument", method = RequestMethod.POST)
    public Document createDocument(@ModelAttribute DocumentFilterBean filterBean,
                                   HttpServletRequest request) throws ParseException, IOException {

        Date startDT = null;
        Date endDT = null;
        Date publishDT = null;

        String tag = WebUtils.getParameter(request, "tag", "");


        //Base parameters required for any Document type (Document, Article, Image, Video and Link)
        Assert.notNull(filterBean.getType(), "Required parameter 'type' is missing");
        Assert.hasText(filterBean.getType(), "Required parameter 'type' is missing");

        Assert.notNull(filterBean.getName(), "Required parameter 'name' is missing");
        Assert.hasText(filterBean.getName(), "Required parameter 'name' is missing");


        Assert.notNull(filterBean.getSite(), "Required parameter 'site' is missing");
        Assert.hasText(String.valueOf(filterBean.getSite()), "Required parameter 'site' is missing");

        User user = getUser();
        if (user.getUsername().equalsIgnoreCase("anonymous")) {
            logger.warn(
                    "The control should never come here. If invalid API Key is found, should be handled at AccessKeyFilter");
            throw new WebServiceException("Incorrect Authentication Information. Please use valid API Key.");
        }


        Document document = new Document();

        //https://jira.marketvine.com/browse/CS-466 - Handled the startDate and endDate for the document
        if (StringUtils.isNotEmpty(filterBean.getStartDate()) && StringUtils.isNotEmpty(filterBean.getEndDate())) {
            try {
                //Check whether date foramts for startDate and endDate
                startDT = DateUtils.getDate(filterBean.getStartDate().trim(), "yyyy-MM-dd HH:mm:ss");
                logger.info(" Start D  " + startDT);
                endDT = com.sourcen.core.util.DateUtils.getDate(filterBean.getEndDate().trim(), "yyyy-MM-dd HH:mm:ss");
                logger.info(" End D    " + endDT);
            } catch (ParseException pEx) {
                logger.error("Incorrect Date format while creation of Docuemnt  " + pEx.getMessage());
                throw new ParseException(
                        "Please provide the 'startDate' and 'endDate' in date time format 'yyyy-MM-dd HH:mm:ss' ",
                        pEx.getErrorOffset());
            }
        }
        if (StringUtils.isNotEmpty(filterBean.getPublishDate())) {
            try {
                //Check whether date foramts for startDate and endDate
                publishDT = DateUtils.getDate(filterBean.getPublishDate().trim(), "yyyy-MM-dd HH:mm:ss");
                logger.info(" publish date    " + publishDT);
            } catch (ParseException pEx) {
                logger.error("Incorrect Date format while creation of Docuemnt  " + pEx.getMessage());
                throw new ParseException(
                        "Please provide the 'publishDate' in date time format 'yyyy-MM-dd HH:mm:ss' ",
                        pEx.getErrorOffset());
            }
        }
        RetailerSite retailerSite = retailerManager.getRetailerSite(filterBean.getSite());

/*        *//*Common field values*//*
        document.setName(filterBean.getName());

        document.setAuthor(filterBean.getAuthor());
        document.setSource(filterBean.getSource());
        document.setAbstractText(filterBean.getAbstractText());
        document.setRetailerSite(retailerSite);

        //https://jira.marketvine.com/browse/CS-466 - Handled startDate and endDate
        document.setStartDate(startDT);
        document.setEndDate(endDT);
        document.setPublishDate(publishDT);
        document.setCreatedBy(user);
        document.setModifiedBy(user);*/


        //Now based on the type save the document
        if (filterBean.getType().equalsIgnoreCase(EntityConstants.Entities.DOCUMENT.getValue())) {

            Assert.notNull(filterBean.getDescription(), "Required parameter 'description' is missing");
            Assert.hasText(filterBean.getDescription(), "Required parameter 'description' is missing");
            Assert.notNull(filterBean.getDocument(), "Required parameter 'document' is missing");

            //Set the retailer site and type as Document type
            document.setType(EntityConstants.Entities.DOCUMENT.getId());
            document.setDescription(filterBean.getDescription());

            documentPropertySetter(document, filterBean, retailerSite, startDT, endDT, publishDT, user);
            // Persisting the document before handling the documnets (image and uploaded doc ) which requires the
            // documentID to save in CDN directory
            document = documentManager.saveDocument(document);

            if (request instanceof DefaultMultipartHttpServletRequest) {
                MultiValueMap<String, MultipartFile> multipartFileMultiValueMap =
                        ((DefaultMultipartHttpServletRequest) request).getMultiFileMap();
                MultipartFile imageFile = null;
                MultipartFile documentFile = null;


                if (multipartFileMultiValueMap.containsKey("image")) {
                    imageFile = multipartFileMultiValueMap.getFirst("image");
                    if (imageFile != null && !imageFile.isEmpty()) {
                        if (FileUtils.isFileExtensionAllowed(imageFile.getOriginalFilename(), "image")) {
                            this.saveImageToCDN(document, imageFile);
                        } else {
                            throw new WebServiceException(
                                    "Only following image types are supported : " + FileUtils.allowedImageExtensions);
                        }

                    }
                }
                if (multipartFileMultiValueMap.containsKey("document")) {
                    documentFile = multipartFileMultiValueMap.getFirst("document");
                    if (documentFile != null && !documentFile.isEmpty()) {

                        if (FileUtils.isFileExtensionAllowed(documentFile.getOriginalFilename(), "document")) {
                            String baseCDNPathForDocument =
                                    documentManager.getBaseCDNPathForDocument(document);
                            FileSystem fileSystem = FileSystem.getDefault();
                            String finalFileName = baseCDNPathForDocument.concat(documentFile.getOriginalFilename());
                            logger.info(finalFileName);
                            File documentCDNFile = fileSystem.getFile(finalFileName, true, true);
                            documentFile.transferTo(documentCDNFile);
                            logger.info(" Document saved in - " + documentCDNFile.getAbsolutePath());
                            document.setDocument(finalFileName);
                        } else {
                            throw new WebServiceException(
                                    "Only following files are supported : " + FileUtils.allowedDocumentExtensions);
                        }
                    }
                }
            }

            Document jsonDocument = saveContentTypeWithTag(document, tag, EntityConstants.Entities.DOCUMENT.getId());
            setTagForContentType(jsonDocument, EntityConstants.Entities.DOCUMENT.getId(), jsonDocument.getId());
            //Add the CDN Prefix for image and document
            if (document.getImage() != null) {
                jsonDocument.setImage(cdnPrefix + document.getImage());
            }
            jsonDocument.setDocument(cdnPrefix + document.getDocument());
            return jsonDocument;
        } else if (filterBean.getType().equalsIgnoreCase(EntityConstants.Entities.ARTICLE.getValue())) {

            Assert.notNull(filterBean.getBody(), "Required parameter 'body' is missing");

            document.setBody(filterBean.getBody());
            document.setDescription(filterBean.getDescription());
            //Set the type of the document as Article Type
            document.setType(EntityConstants.Entities.ARTICLE.getId());
            document.setDescription(filterBean.getDescription());
            documentPropertySetter(document, filterBean, retailerSite, startDT, endDT, publishDT, user);

            document=saveContentTypeWithTag(document, tag, EntityConstants.Entities.ARTICLE.getId());
            setTagForContentType(document, EntityConstants.Entities.ARTICLE.getId(), document.getId());

            return document;

        } else if (filterBean.getType().equalsIgnoreCase(EntityConstants.Entities.VIDEO.getValue())) {

            Assert.notNull(filterBean.getUrl(), "Required parameter 'url' is missing");
            Assert.notNull(filterBean.getImage(),
                    "Required parameter 'video thumbnail' is missing for video document type");

            //Set the retailer site and type as Video type
            document.setType(EntityConstants.Entities.VIDEO.getId());
            document.setDescription(filterBean.getDescription());
            document.setUrl(filterBean.getUrl());
            document.setDescription(filterBean.getDescription());
            documentPropertySetter(document, filterBean, retailerSite, startDT, endDT, publishDT, user);

            // Persisting the video before for handling the image which requires the videoID to save in CDN directory
            document = documentManager.saveDocument(document);

            if (request instanceof DefaultMultipartHttpServletRequest) {
                MultiValueMap<String, MultipartFile> multipartFileMultiValueMap =
                        ((DefaultMultipartHttpServletRequest) request).getMultiFileMap();
                MultipartFile videoImageFile = null;

                if (multipartFileMultiValueMap.containsKey("image")) {
                    videoImageFile = multipartFileMultiValueMap.getFirst("image");
                    if (videoImageFile != null && !videoImageFile.isEmpty()) {
                        if (FileUtils.isFileExtensionAllowed(videoImageFile.getOriginalFilename(), "image")) {
                            saveImageToCDN(document, videoImageFile);
                        } else {
                            throw new WebServiceException(
                                    "Only following image types are supported : " + FileUtils.allowedImageExtensions);
                        }

                    }
                }
            }
            Document jsonVideo = saveContentTypeWithTag(document, tag, EntityConstants.Entities.VIDEO.getId());
            jsonVideo.setImage(cdnPrefix + document.getImage());
            setTagForContentType(jsonVideo, EntityConstants.Entities.VIDEO.getId(), jsonVideo.getId());
            return jsonVideo;
        } else if (filterBean.getType().equalsIgnoreCase(EntityConstants.Entities.IMAGE.getValue())) {

            Assert.notNull(filterBean.getImage(),
                    "Required parameter 'imageFile' is missing for image document type");

            //Set the retailer site and type as Image type
            document.setType(EntityConstants.Entities.IMAGE.getId());
            document.setDescription(filterBean.getDescription());
            documentPropertySetter(document, filterBean, retailerSite, startDT, endDT, publishDT, user);

            // Persisting the image before for handling the image which requires the videoID to save in CDN directory
            document = documentManager.saveDocument(document);

            if (request instanceof DefaultMultipartHttpServletRequest) {
                MultiValueMap<String, MultipartFile> multipartFileMultiValueMap =
                        ((DefaultMultipartHttpServletRequest) request).getMultiFileMap();
                MultipartFile imageFile = null;

                if (multipartFileMultiValueMap.containsKey("image")) {
                    imageFile = multipartFileMultiValueMap.getFirst("image");
                    if (imageFile != null && !imageFile.isEmpty()) {
                        //imageFile.getOriginalFilename()
                        if (FileUtils.isFileExtensionAllowed(imageFile.getOriginalFilename(), "image")) {
                            saveImageToCDN(document, imageFile);
                        } else {
                            throw new WebServiceException(
                                    "Only following image types are supported :: " + FileUtils.allowedImageExtensions);
                        }

                    }
                }
            }
            Document jsonImage = saveContentTypeWithTag(document, tag, EntityConstants.Entities.IMAGE.getId());
            jsonImage.setImage(cdnPrefix + document.getImage());
            setTagForContentType(jsonImage, EntityConstants.Entities.IMAGE.getId(), jsonImage.getId());
            return jsonImage;
        } else if (filterBean.getType().equalsIgnoreCase(EntityConstants.Entities.LINK.getValue())) {

            Assert.notNull(filterBean.getUrl(), "Required parameter 'url' is missng.");

            //Set the retailer site and type as Link type

            document.setType(EntityConstants.Entities.LINK.getId());
            document.setUrl(filterBean.getUrl());
            document.setDescription(filterBean.getDescription());
            documentPropertySetter(document, filterBean, retailerSite, startDT, endDT, publishDT, user);
            document=saveContentTypeWithTag(document, tag, EntityConstants.Entities.LINK.getId());
            setTagForContentType(document, EntityConstants.Entities.LINK.getId(), document.getId());
            return document;

        } else {
            throw new InvalidArgumentException("type", filterBean.getType());
        }

    }

    //========HELPER METHODS========//

    /**
     Saving the document with the particular tag.

     @param document   , store the document content type reference.
     @param tag        , store the tag value.
     @param entityType , store document content type.
     @return document reference.
     */
    private Document saveContentTypeWithTag(Document document, String tag, int entityType) {

        document = documentManager.saveDocument(document);
        if (document.getId() != null) {
            if (tag != null) {
                if (tag != "") {
                    tagManager.saveTags(tag, entityType, document.getId());
                } else {
                    logger.info("No Tag Found for this document content type");
                }
            }
        }
        return document;
    }

    private void saveImageToCDN(Document document, MultipartFile imageFile) throws IOException {

        String baseCDNPathForDocument =
                documentManager.getBaseCDNPathForDocument(document);
        FileSystem fileSystem = FileSystem.getDefault();
        String imageFileName = "thumbnailFile".concat(".png"); // Its only PNG for now
        String finalFileName = baseCDNPathForDocument.concat(imageFileName);
        logger.info(finalFileName);
        File imageCDNFile = fileSystem.getFile(finalFileName, true, true);
        imageFile.transferTo(imageCDNFile);
        logger.info(" Image saved in - " + imageCDNFile.getAbsolutePath());
        document.setImage(finalFileName);
    }

    private Collection<Document> getDocumentsByContentType(String documentID, Integer type)
            throws EntityNotFoundException {
        String ids[] = StringUtils.split(documentID, "-");
        Collection<Document> documents = new ArrayList<Document>();
        //Get the name of the Content type for logging purpose
        String typeName = EntityConstants.Entities.getById(type).getValue();

        logger.info("Getting the " + typeName + " details with IDs:= " + documentID);
        if (ids.length == 1) {
            documents.add(documentManager.getDocument(Long.valueOf(ids[0]), type));
        }
        if (ids.length > 1) {
            for (String id : ids) {
                Document document = null;
                try {
                    document = this.documentManager.getDocument(Long.valueOf(id), type);
                } catch (Exception ex) {
                    logger.error(typeName + " with ID := "
                            + id + " is either null or deleted");
                }
                if (document != null) {
                    documents.add(document);
                }
            }//for each ID
        }
        return documents;
    }

    /**
     Set the generic properties across all  document content type.

     @param document,     store the document reference.
     @param filterBean,   store the document reference.
     @param retailerSite, store the retailer site reference.
     @param startDT,      store the start date.
     @param endDT         , store the end date.
     @param publishDT     , store the publish date.
     @param user,         store the user reference.
     */
    private void documentPropertySetter(Document document, DocumentFilterBean filterBean, RetailerSite retailerSite, Date startDT, Date endDT, Date publishDT, User user) {

        document.setName(filterBean.getName());
        document.setAuthor(filterBean.getAuthor());
        document.setSource(filterBean.getSource());
        document.setAbstractText(filterBean.getAbstractText());
        document.setRetailerSite(retailerSite);

        //https://jira.marketvine.com/browse/CS-466 - Handled startDate and endDate
        document.setStartDate(startDT);
        document.setEndDate(endDT);
        document.setPublishDate(publishDT);
        document.setCreatedBy(user);
        document.setModifiedBy(user);
    }

    public Document setTagForContentType(Document document, final int entityType, final Long entityID) {
        String tags = null;
        tags = tagManager.getTagsAsString(entityType, entityID);
        if (tags != null) {
            document.setTags(tags);
        }

        return document;
    }

    @Autowired
    private DocumentManager documentManager;

    @Autowired
    private TagManager tagManager;

}
