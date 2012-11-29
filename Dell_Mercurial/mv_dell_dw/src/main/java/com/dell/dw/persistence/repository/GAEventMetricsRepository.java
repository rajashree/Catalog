package com.dell.dw.persistence.repository;

import com.dell.dw.persistence.domain.GAEventMetrics;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 7/11/12
 * Time: 6:27 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GAEventMetricsRepository extends IdentifiableEntityRepository<Long, GAEventMetrics> {
    void removeTillDate(Long profileId, Date date);
    List<GAEventMetrics> getEventMetricsTillDate(Long dimensionId, Date date);
}
