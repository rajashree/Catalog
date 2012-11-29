package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.SysMonEndPoint;
import com.dell.dw.persistence.domain.SysMonEndPointProperty;
import com.dell.dw.persistence.repository.SysMonEndPointRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.PropertiesAwareRepositoryImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 8/1/12
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class SysMonEndPointRepositoryImpl extends PropertiesAwareRepositoryImpl<SysMonEndPoint> implements SysMonEndPointRepository {

    public SysMonEndPointRepositoryImpl() {
        super(SysMonEndPoint.class, SysMonEndPointProperty.class);
    }

    @Override
    public List<SysMonEndPoint> getEndPoints(Long endPointTypeId) {
        Criteria criteria = getSession().createCriteria(SysMonEndPoint.class)
                .add(Restrictions.eq("endPointType.id", endPointTypeId));
        List result = criteria.list();
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    @Override
    public SysMonEndPoint getEndPointByName(String endPointName){
         Criteria criteria = getSession().createCriteria(SysMonEndPoint.class)
                .add(Restrictions.eq("endpointName", endPointName));
        return (SysMonEndPoint)criteria.uniqueResult();

    }

}
