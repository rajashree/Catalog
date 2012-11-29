/**
 * 
 */
package com.dell.acs.persistence.repository.impl.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dell.acs.managers.DataImportManager;
import com.dell.acs.managers.DataImportManager.ImportType;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.repository.DataImportDataFileRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;

/**
 * @author Shawn_Fisk
 * 
 */
@Repository
public class DataImportDataFileRepositoryImpl extends
		IdentifiableEntityRepositoryImpl<Long, DataFile> implements
		DataImportDataFileRepository {

	/**
	 * 
	 */
	public DataImportDataFileRepositoryImpl() {
		super(DataFile.class);
	}

	@Override
	public boolean isRetailerSiteStillProcessing(RetailerSite retailerSite,
			ImportType importType) {
		boolean result = true;
		Session session = this.getSession();
		Criteria dataFileCriteria = session.createCriteria(DataFile.class);
		dataFileCriteria.add(Restrictions.not(Restrictions.eq("status",
				DataImportManager.FileStatus.TRANSFER_DONE)));
		if (importType != ImportType.ALL) {
			dataFileCriteria.add(Restrictions.eq("importType",
					importType.getTableName()));
		} else {
			dataFileCriteria.add(Restrictions.in("importType",
					new String[] { ImportType.images.getTableName(),
							ImportType.products.getTableName(),
							ImportType.reviews.getTableName(),
							ImportType.sliders.getTableName() }));
		}
		dataFileCriteria.add(Restrictions.eq("retailerSite", retailerSite));
		dataFileCriteria.setProjection(Projections.rowCount());

		@SuppressWarnings("unchecked")
		List<Long> rss = (List<Long>) dataFileCriteria.list();

		if (rss != null) {
			result = (rss.iterator().next() > 0);
		} else {
			result = false;
		}

		return result;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public boolean hasProcessed(String srcFile) {
		boolean result = true;
		Session session = this.getSession();
		Criteria dataFileCriteria = session.createCriteria(DataFile.class);
		dataFileCriteria.add(Restrictions.eq("srcFile", srcFile));
		dataFileCriteria.setProjection(Projections.id());

		@SuppressWarnings("unchecked")
		List<Long> rss = (List<Long>) dataFileCriteria.list();

		if (rss != null) {
			result = rss.iterator().hasNext();
		} else {
			result = false;
		}

		return result;
	}
}
