package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.D3LinkTrackerMetrics;
import com.dell.dw.persistence.repository.D3LinkTrackerMetricsRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 5/30/12
 * Time: 7:57 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class D3LinkTrackerTrackerMetricsRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, D3LinkTrackerMetrics>
        implements D3LinkTrackerMetricsRepository {

    public D3LinkTrackerTrackerMetricsRepositoryImpl() {
        super(D3LinkTrackerMetrics.class);
    }
}