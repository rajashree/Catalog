/**
 * 
 */
package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.DataFileStatistic;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.repository.DataFileStatisticRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Shawn_Fisk
 * 
 */
@Repository
public class DataFileStatisticRepositoryImpl extends
		IdentifiableEntityRepositoryImpl<Long, DataFileStatistic> implements
		DataFileStatisticRepository {

	/**
	 * Constructor
	 */
	public DataFileStatisticRepositoryImpl() {
		super(DataFileStatistic.class);
	}

	/*
	 * (non-Javadoc)
	 * @see com.dell.acs.persistence.repository.DataFileStatisticRepository#create(org.hibernate.Session, com.dell.acs.persistence.domain.DataFile, int)
	 */
	@Override
	public DataFileStatistic create(Session session, DataFile dataFile, int row) {
		return new DataFileStatistic(dataFile, row);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<DataFileStatistic> getByDataFile(Session session, DataFile dataFile) {
		Criteria criteria = session.createCriteria(DataFileStatistic.class);
		criteria.add(Restrictions.eq("dataFile_id", dataFile.getId()));
		
		return criteria.list();
	}

	@Override
	public DataFileStatistic getByDataFileAndRow(Session session,
			DataFile dataFile, int row) {
		Criteria criteria = session.createCriteria(DataFileStatistic.class);
		criteria.add(Restrictions.eq("dataFile_id", dataFile.getId()));
		criteria.add(Restrictions.eq("row", row));
		
		List<?> result = criteria.list();
		
		if (result.isEmpty()) {
			return null;
		} else {
			return (DataFileStatistic) result.iterator().next();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<DataFileStatistic> getByRetailerSite(Session session,
                                                           RetailerSite rs, List<Long> skipStatsDataFileIds) {
		Criteria criteria = session.createCriteria(DataFileStatistic.class);
		criteria.add(Restrictions.eq("retailerSite_id", rs.getId()));
        if (skipStatsDataFileIds.size() > 0) {
            criteria.add(Restrictions.not(Restrictions.in("dataFile_id", skipStatsDataFileIds)));
        }

        return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<DataFileStatistic> getByRetailerSiteAndHost(
            Session session, RetailerSite rs, String host, List<Long> skipStatsDataFileIds) {
		Criteria criteria = session.createCriteria(DataFileStatistic.class);
		criteria.add(Restrictions.eq("retailerSite_id", rs.getId()));
		String[] fieldsWithHost = { "imageHost", "importHost", "transferHost", "transferSliderHost", "validationHost" };
		LinkedList<Criterion> searchCriterions = new LinkedList<Criterion>();
		for(String fieldWithHost : fieldsWithHost) {
			searchCriterions.add(Restrictions.eq(fieldWithHost, host));
		}
        // finally create the master searchTerm criterion.
        Criterion masterSearchTermCriterion = searchCriterions.removeLast();
        for (Criterion criterion : searchCriterions) {
            masterSearchTermCriterion = Restrictions.or(masterSearchTermCriterion, criterion);
        }
        criteria.add(masterSearchTermCriterion);
        criteria.add(Restrictions.not(Restrictions.in("dataFile_id", skipStatsDataFileIds)));

        return criteria.list();
	}

}
