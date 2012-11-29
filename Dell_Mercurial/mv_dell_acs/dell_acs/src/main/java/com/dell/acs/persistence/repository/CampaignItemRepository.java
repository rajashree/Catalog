/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository;


import com.dell.acs.persistence.domain.CampaignItem;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import java.util.List;
import java.util.Map;

/**
 * @author Samee K.S
 * @author $LastChangedBy: sameeks $
 * @version $Revision: 3113 $, $Date:: 2012-06-12 12:08:21#$
 */
public interface CampaignItemRepository extends IdentifiableEntityRepository<Long, CampaignItem> {

    List<CampaignItem> getItems(Long campaignID);

    void updateCampaignItemProperties(Long campaignID, Map<String, Map<Long, String>> parsedMap);

    Integer getMaxItemPriority(Long campaignID);

    Integer adjustItemPriorityBasedOnPosition(Long categoryID, Long referenceItemID, String position);
}
