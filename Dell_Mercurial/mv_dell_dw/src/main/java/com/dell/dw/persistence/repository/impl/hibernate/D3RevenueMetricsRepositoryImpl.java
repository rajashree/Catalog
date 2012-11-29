package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.D3RevenueMetrics;
import com.dell.dw.persistence.repository.D3RevenueMetricsRepository;
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
public class D3RevenueMetricsRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, D3RevenueMetrics>
        implements D3RevenueMetricsRepository {

    public D3RevenueMetricsRepositoryImpl() {
        super(D3RevenueMetrics.class);
    }
}