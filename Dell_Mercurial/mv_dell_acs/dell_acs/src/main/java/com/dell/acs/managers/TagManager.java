/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.persistence.domain.Tag;
import com.sourcen.core.util.beans.ServiceFilterBean;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;

/**
 * @author Samee K.S
 * @author : svnName $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */

public interface TagManager {

    Tag createTag(String name);

    Tag createTag(String name, Object site);

    /**
     * Load the tag by id
     * @param id - id of tag
     * @return - Tag object if tag found by name 'name' and null if not found
     */
    Tag getTag(Long id);

    /**
     * Load the tag by name
     *
     * @param name - name of tag
     * @return - Tag object if tag found by name 'name' and null if not found
     */
    Tag getTag(String name) throws EntityNotFoundException;

    /**
     * Returns all tags associated in the system
     * @return - List of Tags
     */
    Collection<Tag> getTags(ServiceFilterBean filter);

    /**
     * Returns retailer site specific tags list
     * @param site - accepts both ID and name as Retailer Site
     * @return - List of tags specific to a retailer site
     */
    Collection<Tag> getTags(Object site, ServiceFilterBean filter);

    /**
     * Method to retrieve the contents associated with the specified tag
     * @param tag
     * @return
     */
    Collection<Object> getContent(String tag);

    /**
     * Method to retrieve global tags based on the search term
     * @param searchTerm
     * @return
     */
    Collection<Tag> findTags(String searchTerm, ServiceFilterBean filter);

    Collection<Tag> getRetailerSiteTags(String searchTerm, Object site, ServiceFilterBean filter);

    /* Entity specific methods */

    //void saveTags(List<Object> tags, int entityType, Long entityID);

    void saveTags(String tags, int entityType, Long entityID);

    Collection<Tag> getTags(int entityType, Long entityID, ServiceFilterBean filter);

    String getTagsAsString(int entityType, Long entityID);

}
