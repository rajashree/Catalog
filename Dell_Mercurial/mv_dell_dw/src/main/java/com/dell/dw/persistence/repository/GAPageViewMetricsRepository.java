package com.dell.dw.persistence.repository;

import com.dell.dw.persistence.domain.GAPageViewMetrics;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 7/11/12
 * Time: 6:28 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GAPageViewMetricsRepository extends IdentifiableEntityRepository<Long, GAPageViewMetrics> {
    void removeTillDate(Long profileId, Date date);
    List<GAPageViewMetrics> getPageViewMetricsTillDate(Long dimensionId, Date date);
    Object[] getTodayPageViews(Long profileId);
    List<Long> getDailyPageViews(Long profileId);
    Object getDailyAvgLoadTime(Long profileId);

}
