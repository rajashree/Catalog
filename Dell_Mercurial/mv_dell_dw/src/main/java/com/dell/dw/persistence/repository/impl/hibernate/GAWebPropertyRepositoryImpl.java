package com.dell.dw.persistence.repository.impl.hibernate;

import com.dell.dw.persistence.domain.GAWebProperty;
import com.dell.dw.persistence.repository.GAWebPropertyRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.RepositoryImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Devashree
 * Date: 6/13/12
 * Time: 12:47 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class GAWebPropertyRepositoryImpl  extends RepositoryImpl<GAWebProperty> implements GAWebPropertyRepository {

    public GAWebPropertyRepositoryImpl() {
        super(GAWebProperty.class);
    }

    public GAWebProperty getById(String campaignId){
        Criteria criteria = getSession().createCriteria(GAWebProperty.class)
                .add(Restrictions.eq("id", campaignId));
        GAWebProperty obj = (GAWebProperty) criteria.uniqueResult();
        return obj;
    }
}
