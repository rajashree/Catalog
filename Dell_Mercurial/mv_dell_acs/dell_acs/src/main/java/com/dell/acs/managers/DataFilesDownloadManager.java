/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.RetailerSite;
import com.sourcen.core.managers.Manager;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 @author Sandeep
 @author $LastChangedBy: sandeep $
 @version $Revision: 3168 $, $Date:: 2012-06-14 12:45#$ */
public interface DataFilesDownloadManager extends Manager {


    /**
     * Checks whether the download is in progress.
     *
     * @return returns the current value of downloadingFiles
     */
    Boolean isDownloadingFiles(String providerName);

    /**
     * Downloads the new files from the SFTP server which are not downloaded.
     */
    void downloadDataFiles(String providerName);

    /**
     * Generates the temp file with the given filename.
     *
     * @param originalFilename
     * @return generated temp file
     * @throws IOException
     */
    File generateTempFile(String originalFilename) throws IOException;

    /**
     * Generates the temp file with the given filename with an option to generate timestamp.
     *
     * @param originalFilename
     * @return generated temp file
     * @throws IOException
     */
    File generateTempFile(String originalFilename, boolean generateTimeStamp) throws IOException;

    FileDownLoadStatus getFileDownLoadStatus();

    /**
     Adds the files into the database checking the file types(zip,gzip,csv).

     @param retailerSite
     @param srcFile
     @param fileSource
     @param tempFile
     @return Collection of dataFiles which are inserted into the database
     @throws java.io.IOException
     */
    Collection<DataFile> addFileIntoBatch(RetailerSite retailerSite, String srcFile, Source fileSource, File tempFile)
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
