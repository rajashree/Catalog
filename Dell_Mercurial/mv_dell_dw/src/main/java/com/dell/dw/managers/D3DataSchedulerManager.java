package com.dell.dw.managers;

import com.dell.dw.persistence.domain.DataSchedulerBatch;
import com.dell.dw.persistence.domain.Retailer;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: rajashreem $
 * @version $Revision: 2808 $, $Date:: 2012-06-01 14:31:00#$
 */
public interface D3DataSchedulerManager extends DataSchedulerManager {

    /**
     * Checks whether the download is in progress.
     *
     * @return returns the current value of downloadingFiles
     */
    Boolean isDownloadingFiles();

    /**
     * Downloads the new files from the SFTP server which are not downloaded.
     */
    void downloadDataFiles();

    /**
     * Generates the temp file with the given filename.
     *
     * @param originalFilename
     * @return generated temp file
     * @throws java.io.IOException
     */
    File generateTempFile(String originalFilename) throws IOException;


    FileDownLoadStatus getFileDownLoadStatus();

    /**
     Adds the files into the database checking the file types(zip,gzip,csv).

     @param retailer
     @param srcFile
     @param fileSource
     @param tempFile
     @return Collection of dataFiles which are inserted into the database
     @throws java.io.IOException
     */
    Collection<DataSchedulerBatch> addFileIntoBatch(Retailer retailer, String srcFile, Source fileSource, File tempFile)
            throws IOException;


    /**
     Source of file.
     */
    public static enum Source {
        FTP, USER_UPLOAD
    }


    public static enum FileDownLoadStatus {

        FILE_DOWNLOAD_STARTED(0), FILE_DOWNLOADING(1), FILE_DOWN_LOAD_ERROR(2), FILE_DOWN_LOAD_SUCESSFULL(3),
        FILE_DOWN_LOAD_PAUSE(4), FILE_DOWN_LOAD_FAIL(5), FILE_ALREADY_DOWN_LOAD(6);

        private int value;

        FileDownLoadStatus(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }
}
