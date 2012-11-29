package com.dell.dw.persistence.repository;

import com.dell.dw.persistence.domain.GAWebPropertyProfile;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 5/28/12
 * Time: 3:45 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GAWebPropertyProfileRepository  extends IdentifiableEntityRepository<Long, GAWebPropertyProfile> {
    GAWebPropertyProfile getUnprocessedProfile();

    List<GAWebPropertyProfile> getUnprocessedProfiles();

    Date getLastDownloadedDate(Long webPropertyProfileId);

    void updateLastDownloadedDate(Long webPropertyProfileId, Date lastDownloadedDate);

    GAWebPropertyProfile getWebPropertyProfile(Long webPropertyProfileId, String webPropertyId, Long accountId);

    Date getInitializationDate(Long webPropertyProfileId);

    GAWebPropertyProfile getWebPropertyProfile(String webPropertyId);

    List<GAWebPropertyProfile> getActiveProfiles();

}
