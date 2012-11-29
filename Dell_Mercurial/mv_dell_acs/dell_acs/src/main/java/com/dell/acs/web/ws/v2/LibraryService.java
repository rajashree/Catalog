/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v2;

import com.dell.acs.persistence.domain.Document;
import com.sourcen.core.util.beans.DocumentFilterBean;
import com.sourcen.core.util.beans.ServiceFilterBean;
import com.sourcen.core.web.ws.WebService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;

/**
 * @author Sandeep Heggi
 * @author $LastChangedBy: Sandeep $
 */

/**
 * Document Web Services related to Document Content types (Document, Article, Video, Image, Link)
 */
public interface LibraryService extends WebService {

    /**
     * Get Documents for the specified retailer site (Both ID and Name are supported).
     *
     * @param filter - The variable defined in the {@link ServiceFilterBean} can be over-ridden by
     *               REQUEST parameters.
     * @param site   - String or Long - siteID OR siteName (Ex : 1 OR dell)
     * @return Collection of {@link Document}
     * @throws EntityNotFoundException -  if retailer site with given ID OR siteName is not found
     */
    Collection<Document> getDocumentsBySite(ServiceFilterBean filter, Object site) throws EntityNotFoundException;

    /**
     * Get Articles for the specified retailer site (Both ID and Name are supported).
     *
     * @param filter - The variable defined in the {@link ServiceFilterBean} can be over-ridden by
     *               REQUEST parameters.
     * @param site   - String or Long - siteID OR siteName (Ex : 1 OR dell)
     * @return Collection of {@link Document}
     * @throws EntityNotFoundException -  if retailer site with given ID OR siteName is not found
     */
    Collection<Document> getArticlesBySite(ServiceFilterBean filter, Object site) throws EntityNotFoundException;

    /**
     * Get Videos for the specified retailer site (Both ID and Name are supported).
     *
     * @param filter - The variable defined in the {@link ServiceFilterBean} can be over-ridden by
     *               REQUEST parameters.
     * @param site   - String or Long - siteID OR siteName (Ex : 1 OR dell)
     * @return Collection of {@link Document}
     * @throws EntityNotFoundException -  if retailer site with given ID OR siteName is not found
     */
    Collection<Document> getVideosBySite(ServiceFilterBean filter, Object site) throws EntityNotFoundException;

    /**
     * Get Images for the specified retailer site (Both ID and Name are supported).
     *
     * @param filter - The variable defined in the {@link ServiceFilterBean} can be over-ridden by
     *               REQUEST parameters.
     * @param site   - String or Long - siteID OR siteName (Ex : 1 OR dell)
     * @return Collection of {@link Document}
     * @throws EntityNotFoundException -  if retailer site with given ID OR siteName is not found
     */
    Collection<Document> getImagesBySite(ServiceFilterBean filter, Object site) throws EntityNotFoundException;

    /**
     * Get Links for the specified retailer site (Both ID and Name are supported).
     *
     * @param filter - The variable defined in the {@link ServiceFilterBean} can be over-ridden by
     *               REQUEST parameters.
     * @param site   - String or Long - siteID OR siteName (Ex : 1 OR dell)
     * @return Collection of {@link Document}
     * @throws EntityNotFoundException -  if retailer site with given ID OR siteName is not found
     */
    Collection<Document> getLinksBySite(ServiceFilterBean filter, Object site) throws EntityNotFoundException;


    //<========== Service Methods to retrieve the Details of the Documents of different content type ========>.

    /**
     * @param documentID - String - "1-2-3" returns Document details of products with Id = 1, Id = 2 & Id = 3;
     * @return Collection of {@link Document}
     * @throws EntityNotFoundException - if document with given ID is not found.
     */
    Collection<Document> getDocumentDetails(String documentID) throws EntityNotFoundException;

    /**
     * Gets the Article Details of given IDS
     *
     * @param articleID - String - "1-2-3" returns Article details of articles with Id = 1, Id = 2 & Id = 3;
     * @return Collection of {@link Document}
     * @throws EntityNotFoundException - if article with given ID is not found.
     */
    Collection<Document> getArticleDetails(String articleID) throws EntityNotFoundException;

    /**
     * Gets the Video Details of given IDS
     *
     * @param videoID - String - "1-2-3" returns Video details of videos with Id = 1, Id = 2 & Id = 3;
     * @return Collection of {@link Document}
     * @throws EntityNotFoundException - if Video with given ID is not found.
     */
    Collection<Document> getVideoDetails(String videoID) throws EntityNotFoundException;

    /**
     * Gets the Image Details of given IDS
     *
     * @param imageID - String - "1-2-3" returns Image details of images with Id = 1, Id = 2 & Id = 3;
     * @return Collection of {@link Document}
     * @throws EntityNotFoundException - if Image with given ID is not found.
     */
    Collection<Document> getImageDetails(String imageID) throws EntityNotFoundException;

    /**
     * Gets the Link Details of given IDS
     *
     * @param linkID - String - "1-2-3" returns Link details of links with Id = 1, Id = 2 & Id = 3;
     * @return Collection of {@link Document}
     * @throws EntityNotFoundException - if Link with given ID is not found.
     */
    Collection<Document> getLinkDetails(String linkID) throws EntityNotFoundException;

    /**
     * Service to create a Document type (Document, Article, Image, Video and Link)
     *
     * @param filterBean - refer {@link DocumentFilterBean}
     * @return Object of {@link Document}
     */
    Document createDocument(DocumentFilterBean filterBean, HttpServletRequest request) throws Exception;
}
