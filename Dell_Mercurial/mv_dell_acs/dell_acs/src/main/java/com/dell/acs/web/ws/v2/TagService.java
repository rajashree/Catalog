/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v2;

import com.dell.acs.persistence.domain.Tag;
import com.sourcen.core.util.beans.ServiceFilterBean;
import com.sourcen.core.web.ws.WebService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Samee K.S
 * @author : sameeks $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */

public interface TagService extends WebService {
    /*
        1) Create
        2) Tagging a content
        3) Get All tags - paginated
        4) Get content associated tags - paginated
        5) Search tags - min 3 chars - paginated
        6) Get tag associated contents
    */

    /**
     * Service to create a tag
     * @param tag
     * @return
     */
    Tag createTag(String tag);

    /**
     * Service method to associate a content(s) with a tag
     * @param tag - Name of tag
     * @param entityType - type of entity @see EntityConstants.Entities
     * @param entityID - The entity(s) which needs to be associated with a tag. If more than
     *                  one item is passed, the request should follow the format entityID=11-12-13
     */
    void addTag(String tag, String entityType, String entityID);

    Collection<Tag> getTags(Object site, ServiceFilterBean filter);

    Collection<Tag> getTags(ServiceFilterBean filter);

    Collection<Tag> searchTags(String tag, ServiceFilterBean filter);

    Map<String, List> getContents(Object tag, ServiceFilterBean filter);
}
