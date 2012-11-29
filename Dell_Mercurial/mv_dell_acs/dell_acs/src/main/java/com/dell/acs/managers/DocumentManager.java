package com.dell.acs.managers;


import com.dell.acs.persistence.domain.Document;
import com.sourcen.core.managers.Manager;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.hibernate.NonUniqueResultException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA. User: Mahalakshmi Date: 4/26/12 Time: 3:03 PM To change this template use File | Settings |
 * File Templates.
 */
public interface DocumentManager extends Manager {

    /* Document related properties   */
    public static final String DOCUMENT_THUMBNAIL_PROPERTY = "dell.document.thumbnail";

    /**
     * Method to retrieve the the Document objects list
     *
     * @return - List of Document's
     */
    Collection<Document> getDocuments();


    /**
     * Method to retrieve the list of Document's using retailer siteID
     *
     * @param retailerSiteID
     * @return -Return the list of Document's with matching siteID
     * @see com.dell.acs.persistence.domain.RetailerSite
     */
    @Deprecated
    Collection<Document> getDocumentByRetailerSiteID(Long retailerSiteID);

    // Library page helper method
    @Deprecated
    Collection<Document> getLatestDocuments(Long retailerSiteID);

    public boolean doesDocumentExists(Document document);
    /* Document management related methods */


    /**
     * Method to persist the Document object
     *
     * @return - Return the document object
     */
    Document saveDocument(Document document) throws EntityExistsException;

    /**
     * Method to load the Document by ID
     *
     * @param documentID - Document ID
     * @return - Document object with ID = documentID
     */
    Document getDocument(Long documentID) throws EntityNotFoundException;

    /**
     * Method to delete a Document object by ID
     *
     * @param documentID - Document ID
     */
    void deleteDocument(Long documentID) throws EntityNotFoundException;

    /**
     * Method to get the BASECDN path for Document
     *
     * @param document
     * @return
     */
    String getBaseCDNPathForDocument(Document document);

    /**
     * To get the filtered documents.
     *
     * @param paramsMap
     * @return
     */
    List<Map<String, String>> getFilteredDocuments(Map<String, Object> paramsMap);


    // <======= New methods introduced in sprint 4 =======>

    /**
     Retrieve the published document.
     @param documentId, store the document id.
     @param type , store the document type.
     @return  published document name.
     */
    @Deprecated
    String getDocumentNameByID(Long documentId,Integer type);

    /**
     Check the name existence , so that duplicate entry not allowed.
     It checks during ajax call.
     @param documentName , store the document name.
     @return boolean status , according to existence status.
     @throws NonUniqueResultException
     */
    @Deprecated
    boolean checkNameExistence(String documentName) throws NonUniqueResultException;

    /**
     * Method to load the Document by ID and type.
     *
     * @param documentID - Long - document ID
     * @return - Document object with ID = documentID
     * @throws - EntityNotFoundException - if document with given ID and type is not found.
     */
    Document getDocument(Long documentID, Integer type) throws EntityNotFoundException;

    /**
     * retrieve the documents on the basis of RetailerSite and source Type.
     *
     * @param retailerSiteID - Long - retailer site ID
     * @param type           - Integer - Refer {@link com.dell.acs.content.EntityConstants}
     * @return Collection of {@link Document}
     */
    Collection<Document> getDocuments(Long retailerSiteID, Integer type, ServiceFilterBean filterBean);

    /**
     * retrieve the documents on the basis of RetailerSite Name and source Type.
     *
     * @param retailerSiteName - String - retailer site name
     * @param type             - Integer - Refer {@link com.dell.acs.content.EntityConstants}
     * @return Collection of {@link}
     */
    Collection<Document> getDocuments(String retailerSiteName, Integer type, ServiceFilterBean filterBean);


}
