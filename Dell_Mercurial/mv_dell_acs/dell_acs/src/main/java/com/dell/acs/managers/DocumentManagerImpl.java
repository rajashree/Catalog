package com.dell.acs.managers;


import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.Document;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.repository.DocumentRepository;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.sourcen.core.util.FileSystem;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.hibernate.NonUniqueResultException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA. User: Mahalakshmi Date: 4/26/12 Time: 3:04 PM To change this template use File | Settings |
 * File Templates.
 */
@Service
public class DocumentManagerImpl implements DocumentManager {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(DocumentManagerImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Collection<Document> getDocuments() {

        Collection<Document> documentList = documentRepository.getAll();
        return documentList;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Document> getDocumentByRetailerSiteID(final Long retailerSiteID) {
        return documentRepository.getDocumentByRetailerSiteID(retailerSiteID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Document> getLatestDocuments(final Long retailerSiteID) {
        return documentRepository.getLatestDocuments(retailerSiteID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean doesDocumentExists(Document document) {
        String name = document.getName();
        Long siteID = document.getRetailerSite().getId();
        boolean exists = false;
        Document exampleObj = new Document();
        exampleObj.setName(name);
        RetailerSite rs = new RetailerSite();
        rs.setId(siteID);
        exampleObj.setRetailerSite(rs);
        List<Document> exampleObjs = documentRepository.getByExample(exampleObj);
        if (exampleObj != null) {
            // Check if the same object is being updated.
            // If same object then return false
            // else return true
            if (document.getId() != null && exampleObj.getId().longValue() != document.getId().longValue()) {
                exists = true;
            }
        }
        return exists;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false,
            rollbackFor = EntityExistsException.class)
    public Document saveDocument(final Document document) throws EntityExistsException {
        try {
            if (document.getId() != null) {
                document.setModifiedDate(new Date());
                documentRepository.update(document);
                logger.debug("Updated document - " + document.getId() + " :: " + document.getName());
            } else {
                document.setCreationDate(new Date());
                document.setModifiedDate(new Date());
                document.setStatus(EntityConstants.Status.PUBLISHED.getId());
                documentRepository.insert(document);
                logger.debug("Saved new document - " + document.getName());
            }
        } catch (Exception ex) {
            if (ex instanceof ConstraintViolationException) {
                throw new EntityExistsException(ex.getMessage());
            }
        }
        return document;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Document getDocument(final Long documentID) throws EntityNotFoundException {
        return documentRepository.get(documentID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void deleteDocument(final Long documentID) throws EntityNotFoundException {
        Document document = documentRepository.deleteDocument(documentID);
        if (document == null) {
            throw new EntityNotFoundException("Unable to find the Document with ID:" + documentID);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBaseCDNPathForDocument(final Document document) {
        RetailerSite retailerSite = document.getRetailerSite(); //retailerSiteRepository.get(document.getRetailerSite().getId());
        Retailer retailer = retailerSite.getRetailer();

        String documentsCDNBasePath = StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR + "cdn"
                + StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR + retailer.getId()
                + "_" + retailer.getName()
                + StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR + retailerSite.getId() + "_" + retailerSite.getSiteName()
                + StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR + "document"
                + StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR + document.getId()
                + StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR;
        File baseFolder = null;
        // This block is to ensure that the directories in CDN in created before performing any operation on the CDN folder.
        try {
            // Create the folders if doesn't exists in 'documentsCDNBasePath' CDN path
            baseFolder = FileSystem.getDefault().getDirectory(documentsCDNBasePath, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return documentsCDNBasePath;

    }


    @Override
    public List<Map<String, String>> getFilteredDocuments(Map<String, Object> paramsMap) {
        List<Map<String, String>> filteredDocuments = new ArrayList<Map<String, String>>();
        // Prepare the documents data map
        for (Document document : documentRepository.filterDocuments(paramsMap)) {
            Map<String, String> documentData = new HashMap<String, String>();
            documentData.put("id", String.valueOf(document.getId()));
            documentData.put("title", document.getName());
            // Build an output data map based on the document type
            if(document.getType() == EntityConstants.Entities.DOCUMENT.getId()){
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

                documentData.put("document", document.getDocument());
                // Format the date in valid format
                if (document.getStartDate() != null) {
                    documentData.put("startDate", formatter.format(document.getStartDate()));
                } else {
                    documentData.put("startDate", "");
                }

                if (document.getEndDate() != null) {
                    documentData.put("endDate", formatter.format(document.getEndDate()));
                } else {
                    documentData.put("endDate", "");
                }
            }else if(document.getType() == EntityConstants.Entities.IMAGE.getId()
                    || document.getType() == EntityConstants.Entities.LINK.getId()
                    || document.getType() == EntityConstants.Entities.VIDEO.getId() ){
                documentData.put("url", document.getUrl());
                //documentData.put("desc", document.getDescription());
            }
            if(document.getType() == EntityConstants.Entities.VIDEO.getId()){
                documentData.put("image", document.getImage());
            }
            filteredDocuments.add(documentData);
        }
        return filteredDocuments;
    }


    @Override
    @Transactional(readOnly = true)
    public String getDocumentNameByID(final Long documentId, Integer type) {
       String documentName=documentRepository.getDocumentNameByID(documentId, type);
       if(documentName==null){
           logger.error("Unable to get the document , document not exist");
       }
        return documentName;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkNameExistence(final String documentName) throws NonUniqueResultException {
        return documentRepository.checkNameAvailability(documentName);
    }

    @Override
    @Transactional(readOnly = true)
    public Document getDocument(Long documentID, Integer type) throws EntityNotFoundException {
        Document document = documentRepository.getDocument(documentID, type);
        //Get the type name so that exception is thrown with a proper message this will help in web service calls
        String typeName = EntityConstants.Entities.getById(type).name();
        if (document == null) {
            throw new EntityNotFoundException(typeName + " not found with ID:= " + documentID);
        }
        return document;
    }


    @Override
    @Transactional(readOnly = true)
    public Collection<Document> getDocuments(final Long retailerSiteID, final Integer type, final ServiceFilterBean filterBean) {
        Collection<Document> articles = documentRepository.getDocuments(retailerSiteID, type, filterBean);
        if (articles.size() > 0) {
            return articles;
        }
        //Get the name of the Content for logging purpose
        String typeName = EntityConstants.Entities.getById(type).name();
        logger.info("No " + typeName + "(S) found for retailerSiteID - " + retailerSiteID);
        return Collections.emptyList();
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Document> getDocuments(final String retailerSiteName, final Integer type, final ServiceFilterBean filterBean) {
        Long retailerSiteID = this.retailerSiteRepository.getByName(retailerSiteName).getId();
        return getDocuments(retailerSiteID, type, filterBean);
    }

    /**
     * DocumentRepository autowiring by name.
     */
    @Autowired
    private DocumentRepository documentRepository;

    /**
     * RetailerSiteRepository autowiring by name.
     */
    @Autowired
    private RetailerSiteRepository retailerSiteRepository;
}
