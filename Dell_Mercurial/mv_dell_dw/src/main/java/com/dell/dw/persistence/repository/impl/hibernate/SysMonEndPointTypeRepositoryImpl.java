package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.Order;
import com.dell.dw.persistence.domain.SysMonEndPoint;
import com.dell.dw.persistence.domain.SysMonEndPointType;
import com.dell.dw.persistence.domain.SysMonEndPointTypeProperty;
import com.dell.dw.persistence.repository.SysMonEndPointTypeRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.PropertiesAwareRepositoryImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 8/1/12
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class SysMonEndPointTypeRepositoryImpl extends PropertiesAwareRepositoryImpl<SysMonEndPointType> implements SysMonEndPointTypeRepository {

    public SysMonEndPointTypeRepositoryImpl() {
        super(SysMonEndPointType.class, SysMonEndPointTypeProperty.class);
    }


}
