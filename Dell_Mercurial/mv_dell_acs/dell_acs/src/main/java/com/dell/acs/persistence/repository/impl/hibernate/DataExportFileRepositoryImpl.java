package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.persistence.domain.DataExportFile;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.repository.DataExportFileRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 4/8/12
 * Time: 12:41 PM
 */
@Repository
public class DataExportFileRepositoryImpl extends IdentifiableEntityRepositoryImpl<Long, DataExportFile> implements DataExportFileRepository {

    public static final Logger log = LoggerFactory.getLogger(DataExportFileRepositoryImpl.class);

    /**
     * Default Constructor
     */
    public DataExportFileRepositoryImpl() {
        super(DataExportFile.class);
    }

    /**
     * Get pending files for export by RetailerSite
     *
     * @param retailerSite for which the data files to be returned
     *
     * @return Collection of data export files
     */
    @Override
    public Collection<DataExportFile> getPendingFilesByRetailer(RetailerSite retailerSite) {
        Criteria criteria = getSession().createCriteria(DataExportFile.class);
        criteria.add(Restrictions.eq("retailerSite.id", retailerSite.getId()));
        return (List<DataExportFile>) criteria.list();
    }

    /**
     * Get all files based on exportStatus flag
     *
     * @param exportStatus the status of export
     *
     * @return Collection of data export files
     */
    @Override
    public Collection<DataExportFile> getFilesByStatus(Integer exportStatus) {
        Criteria criteria = getSession().createCriteria(DataExportFile.class);
        //criteria.add(Restrictions.eq("retailerSite.id",6));
        criteria.add(Restrictions.eq("exportStatus", exportStatus));
        log.info(" Criteria ::::     " + exportStatus + "                    " + criteria.list());
        return (List<DataExportFile>) criteria.list();
    }
}
