package com.dell.dw.persistence.repository;

import com.dell.dw.persistence.domain.SysMonServerHealthMetrics;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 5/28/12
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SysMonServerHealthMetricsRepository extends IdentifiableEntityRepository<Long, SysMonServerHealthMetrics> {
    public Collection<SysMonServerHealthMetrics> getServerMetrics(Long id);
}

