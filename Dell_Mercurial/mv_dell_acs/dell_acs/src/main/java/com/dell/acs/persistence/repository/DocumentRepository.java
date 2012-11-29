/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository;

import com.dell.acs.persistence.domain.Document;
import com.dell.acs.persistence.domain.DocumentProperty;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.hibernate.NonUniqueResultException;

import java.util.Collection;
import java.util.Map;


/**
 * @author Samee K.S
 * @author sameeks: svnName $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */

public interface DocumentRepository extends IdentifiableEntityRepository<Long, Document> {

    /**
     * Method to retrieve the list of Document's using retailer siteID
     *
     * @param retailerSiteID
     * @return -Return the list of Document's with matching siteID
     * @see com.dell.acs.persistence.domain.RetailerSite
     */
    Collection<Document> getDocumentByRetailerSiteID(Long retailerSiteID);

    @Deprecated
    Collection<Document> getLatestDocuments(Long siteID);

    /**
     * Retrieve the filtered documents.
     *
     * @param paramsMap
     * @return Collection of documents
     */
    Collection<Document> filterDocuments(Map<String, Object> paramsMap);

    /**
     Retrieve the published document.
     @param documentId, store the document id.
     @param type , store the document type.
     @return  published document name.
     */
    @Deprecated
    String getDocumentNameByID(Long documentId, Integer type);

    @Deprecated
    Document updateProperty(Long documentPropertyId, String name, String value);


    //New methods introduced in Sprint 4

    /**
     * Retrieves the documents(only published) on the basis of RetailerSite ID and Content Type.
     *
     * @param retailerSiteID - Long - site ID for which documents need to be retrieved
     * @param type           - Integer - Refer {@link com.dell.acs.content.EntityConstants}
     * @return Collection of {@link Document}
     */
    Collection<Document> getDocuments(Long retailerSiteID, Integer type, ServiceFilterBean filterBean);

    /**
     * Check the name existence , so that duplicate entry not allowed.
     * It checks during ajax call.
     * @param documentName , store the document name.
     * @return boolean status , according to existence status.
     */
    @Deprecated
    boolean checkNameAvailability(String documentName);

    /**
     * Retrieves the published document with the given type and ID.
     *
     * @param documentID - Long - documentID
     * @param type       - Integer - Refer {@link com.dell.acs.content.EntityConstants}
     * @return Object of {@link Document}
     */
    Document getDocument(Long documentID, Integer type);

    /**
     * Deletes the document i.e., sets the status of the document to deleted.
     *
     * @param documentID - Long - documentID which needs to be deleted
     */
    Document deleteDocument(Long documentID);


}
