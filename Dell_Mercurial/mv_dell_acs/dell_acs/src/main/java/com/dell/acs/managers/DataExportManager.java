package com.dell.acs.managers;

import com.dell.acs.persistence.domain.DataExportFile;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.RetailerSite;
import com.sourcen.core.managers.Manager;

import java.util.Collection;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: vivekk $
 * @version $Revision: 2798 $, $Date:: 2012-06-01 07:10:48#$
 */
public interface DataExportManager extends Manager {

    Boolean isExportingFiles();

    DataFile getFilePendingForExport(RetailerSite retailerSite);

    void processFileForExport(DataFile dataFile);

    /**
     * this is just a quickFix
     *
     * @param retailerSite
     */
    void executeJobForRetailerSite(RetailerSite retailerSite);

    public static class FileStatus {
        public static final Integer IN_QUEUE = 0;
        public static final Integer PROCESSING = 1;
        public static final Integer DONE = 2;
        public static final Integer ERROR_READ = -1000;
        public static final Integer ERROR_EXTRACTING = -1001;
        public static final Integer ERROR_PARSING = -1002;

    }

    Collection<DataExportFile> getFilesByStatus(Integer exportStatus);

    void updateExportedFileStatus(DataExportFile dataExportFile);


}

