/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository;

import com.dell.acs.persistence.domain.Tag;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;
import com.sourcen.core.util.beans.ServiceFilterBean;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.Set;

/**
 * @author Samee K.S
 * @author : svnName $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */

public interface TagRepository extends IdentifiableEntityRepository<Long, Tag> {

    Tag getTag(String name) throws EntityNotFoundException;

    Tag getTag(Object tagObj) throws EntityNotFoundException;

    /**
     * Method used to add a mapping entry in tag-entity mapping table
     * @param tags
     * @param entityType
     * @param entityID
     */
    void saveTags(Set<Tag> tags, int entityType, Long entityID);

    Collection<Tag> getTags(String searchTerm, boolean retailerSiteSpecific, ServiceFilterBean filter);

    Collection<Tag> getTags(Long retailerSiteID, ServiceFilterBean filter);

    Collection<Tag> getRetailerSiteTags(String searchTerm, Long retailerSiteID, ServiceFilterBean filter);

    Collection<Tag> getTags(int entityType, Long entityID, ServiceFilterBean filter);


}
