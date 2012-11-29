/**
 * 
 */
package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.persistence.domain.DataFileStatisticSummary;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.repository.DataFileStatisticSummaryRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * @author Shawn_Fisk
 * 
 */
@Repository
public class DataFileStatisticSummaryRepositoryImpl extends
		IdentifiableEntityRepositoryImpl<Long, DataFileStatisticSummary> implements
        DataFileStatisticSummaryRepository {

	/**
	 * Constructor
	 */
	public DataFileStatisticSummaryRepositoryImpl() {
		super(DataFileStatisticSummary.class);
	}

    @Override
    public Collection<DataFileStatisticSummary> getSummaryByRetailerSite(Session session, RetailerSite rs) {
        Criteria criteria = session.createCriteria(DataFileStatisticSummary.class);
        criteria.add(Restrictions.eq("retailerSite_id", rs.getId()));

        return criteria.list();
    }
}
