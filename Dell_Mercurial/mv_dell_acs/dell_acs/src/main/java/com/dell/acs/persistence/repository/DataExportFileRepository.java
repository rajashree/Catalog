package com.dell.acs.persistence.repository;

import com.dell.acs.persistence.domain.DataExportFile;
import com.dell.acs.persistence.domain.RetailerSite;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 4/8/12
 * Time: 12:32 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataExportFileRepository extends IdentifiableEntityRepository<Long, DataExportFile> {

    /**
     * Get pending files for export by RetailerSite
     *
     * @param retailerSite for which the data files to be returned
     *
     * @return Collection of data export files
     */
    public Collection<DataExportFile> getPendingFilesByRetailer(RetailerSite retailerSite);


    /**
     * Get all files based on exportStatus flag
     *
     * @param exportStatus the status of export
     *
     * @return Collection of data export files
     */
    public Collection<DataExportFile> getFilesByStatus(Integer exportStatus);

}
