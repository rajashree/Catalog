/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository;

import com.dell.acs.persistence.domain.CampaignCategory;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

public interface CampaignCategoryRepository extends IdentifiableEntityRepository<Long, CampaignCategory> {
    Integer getMaxCategoryPosition(Long id, Long campaignID);
}
