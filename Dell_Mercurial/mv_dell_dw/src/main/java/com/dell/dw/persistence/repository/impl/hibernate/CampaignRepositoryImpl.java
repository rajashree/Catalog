package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.Campaign;
import com.dell.dw.persistence.repository.CampaignRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 5/28/12
 * Time: 7:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class CampaignRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, Campaign>
        implements CampaignRepository {

    public CampaignRepositoryImpl() {
        super(Campaign.class);
    }
}
