/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.persistence.domain.CampaignCategory;
import com.dell.acs.persistence.domain.CampaignCategoryProperty;
import com.dell.acs.persistence.repository.CampaignCategoryRepository;
import com.dell.acs.persistence.repository.CampaignItemRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.PropertiesAwareRepositoryImpl;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Samee K.S
 * @author $: sameeks $
 * @version : 0 $,  :: 2012-03-07 09:56:40#$
 */

@Repository
public class CampaignCategoryRepositoryImpl extends PropertiesAwareRepositoryImpl<CampaignCategory>
        implements CampaignCategoryRepository {

    @Autowired
    private CampaignItemRepository campaignItemRepository;

    public CampaignCategoryRepositoryImpl() {
        super(CampaignCategory.class, CampaignCategoryProperty.class);
    }

    public Integer getMaxCategoryPosition(Long id, Long campaignID) {
        Query q = getSession().createQuery("SELECT MAX(position) FROM CampaignCategory  WHERE parent=:parent AND campaign.id=:cid");
        q.setLong("parent", id);
        q.setLong("cid", campaignID);
        Integer position = (Integer) q.uniqueResult();
        return (position != null) ? (position + 1) : 0;
    }

    @Deprecated
    private Integer adjustOtherCategoryPositions(Long campaignItemID, String position){
        //  0    1    2    3    4    5
        // 812, 813, 814, 824, 822, 845

        //  0    1    2    3    4    5    6
        // 812, 813, 814, 820, 824, 822, 845

        // add 820 before 824 ( position 3 ) - UPDATE campaign_item SET position = ( position + 1 ) where id = 824

        // add 820 after 824  ( position 3 ) -

        // select category from campaignCategory where categoryID = categoryID and position >= position
        // Query q = getSession().createQuery("SELECT MAX(position) FROM CampaignCategory  WHERE parent=:parent AND campaign.id=:cid");
        // q.setLong("parent", id);
        // q.setLong("cid", campaignID);
        // Integer position = (Integer)q.uniqueResult();
        // return (position != null)? (position + 1): 0;
        return 0;
    }
}
