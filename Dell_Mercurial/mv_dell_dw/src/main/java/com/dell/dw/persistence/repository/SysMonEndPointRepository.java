package com.dell.dw.persistence.repository;

import com.dell.dw.persistence.domain.SysMonEndPoint;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 8/1/12
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SysMonEndPointRepository extends IdentifiableEntityRepository<Long, SysMonEndPoint> {
    public List<SysMonEndPoint> getEndPoints(Long endPointTypeId);

    public SysMonEndPoint getEndPointByName(String endPointName);



}
