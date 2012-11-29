package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.Retailer;
import com.dell.dw.persistence.repository.RetailerRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 5/28/12
 * Time: 7:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class RetailerRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, Retailer>
        implements RetailerRepository {

    public RetailerRepositoryImpl() {
        super(Retailer.class);
    }

    @Override
    public Retailer getByRetailerId(String retailerId) {
        Criteria criteria = getSession().createCriteria(Retailer.class)
                .add(Restrictions.eq("retailerId", retailerId));
        Retailer obj = (Retailer) criteria.uniqueResult();
        return obj;
    }
}
