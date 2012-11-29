package com.dell.dw.persistence.repository.impl.hibernate;


import com.dell.dw.persistence.domain.Lead;
import com.dell.dw.persistence.repository.LeadRepository;
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
public class LeadRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, Lead>
        implements LeadRepository {

    public LeadRepositoryImpl() {
        super(Lead.class);
    }

    @Override
    public Lead getByLeadId(String leadId) {
        Criteria criteria = getSession().createCriteria(Lead.class)
                .add(Restrictions.eq("leadId", leadId));
        Lead obj = (Lead) criteria.uniqueResult();
        return obj;
    }
}
