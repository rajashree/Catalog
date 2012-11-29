package com.dell.acs.jobs;

import com.dell.acs.managers.DataExportManager;
import com.dell.acs.managers.FileSystemUtil;
import com.dell.acs.managers.RetailerManager;
import com.dell.acs.persistence.domain.DataExportFile;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.stats.CountStat;
import com.dell.acs.stats.CountStatMutator;
import com.dell.acs.stats.StatUtil;
import com.dell.acs.stats.TimerStat;
import com.dell.acs.stats.TimerStatMutator;
import com.sourcen.core.jobs.AbstractJob;
import com.sourcen.core.util.FileSystem;
import com.sourcen.core.util.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.vfs2.FileSystemManager;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Adarsh
 * Date: 4/3/12
 * Time: 3:20 PM
 */
public class UploadDataFilesJob extends AbstractJob {

    private static final Logger logger = LoggerFactory.getLogger(UploadDataFilesJob.class);
    private static FileSystemManager fileSystemManager = null;
    private File generatedFeedsDir = null;
    private String sftpUri = null;
    FileSystem fileSystem = null;


    @Override
    protected void executeJob(JobExecutionContext context) {
    	CountStatMutator count = null;
    	try {
        	count = (CountStatMutator)StatUtil.getInstance().getStat(CountStat.class, "UploadDataFilesJob.Count");
        	count.inc();
        	count.apply();
            this.fileSystem = this.configurationService.getFileSystem();
            Collection<DataExportFile> files = this.dataExportManager.getFilesByStatus(DataExportManager.FileStatus.IN_QUEUE);

            for (DataExportFile file : files) {
                logger.info("File to upload   :  " + file.getFilePath());
                process(file);
            }

        } catch (Exception e) {
            logger.error("Uploading canonical feed failed " + e.getMessage(), e);
    	} finally {
        	if (count != null) {
        		count.clear();
        		count.dec();
        		count.apply();
        	}
    	}
    }

    /**
     * Process the DataExportFile which are ready for FTP uploading.
     *
     * @param dataExportFile - DataExportFile which is available for upload.
     * @throws IOException
     */
    protected void process(DataExportFile dataExportFile) throws IOException {
        RetailerSite retailerSite = dataExportFile.getRetailerSite();
        String retailerSiteConfigDir = FileSystemUtil.getConfigDir(retailerSite);
        File ftpConfigFile = configurationService.getFileSystem().getFile(retailerSiteConfigDir + "/ftp.properties", false, false);

        logger.info("ftpConfigFile         " + ftpConfigFile.exists() + "   " + ftpConfigFile.canRead());
        if (ftpConfigFile.exists() || ftpConfigFile.canRead()) {
            logger.info(" FTP properties for " + retailerSite.getSiteName() + " found!!! ");
            Properties ftpProperties = new Properties();
            ftpProperties.load(new FileInputStream(ftpConfigFile));

            if (isFTPConfiguredCompletely(ftpProperties)) {
                updateExportedFileStatus(dataExportFile, DataExportManager.FileStatus.PROCESSING);
                uploadToFTP(fileSystem, dataExportFile, ftpProperties);
            } else {
                logger.warn(" FTP details not configured completely for " + retailerSite.getSiteName() + ". Please configure correctly.");
            }

        } else {
            logger.info(" FTP properties for " + retailerSite.getSiteName() + " NOT  found!!! ");
        }

    }

    /**
     * Upload the canonical feed of the RetailerSite to a pre-configured FTP location
     *
     * @param fileSystem     - WorkDir of the ContentServer, where the canonical file resides
     * @param dataExportFile - DataExportFile entity
     * @param ftpProps       - the FTP config details specific to a RetailerSite
     */
    protected void uploadToFTP(FileSystem fileSystem, DataExportFile dataExportFile, Properties ftpProps) {
    	TimerStatMutator uploadToFtpTimer = null;
    	uploadToFtpTimer = (TimerStatMutator)StatUtil.getInstance().getStat(TimerStat.class, "UploadDataFilesJob.UploadToFTPTimer");
    	if (uploadToFtpTimer != null)
    		uploadToFtpTimer.start();

        logger.info("Upload canonical feed   " + dataExportFile.getFilePath() + " to FTP. ");
        FTPClient client = connectToFTP(ftpProps);
        File txtFile = null;
        try {
            if (client != null) {


                txtFile = fileSystem.getFile(dataExportFile.getFilePath(), false, true);
                if (txtFile.exists()) {
                    logger.info("Uploading......");
                    InputStream localIS = new FileInputStream(txtFile);
                    client.appendFile(txtFile.getName(), localIS);
                }
                logger.info("Upload completed.");
                updateExportedFileStatus(dataExportFile, DataExportManager.FileStatus.DONE);

            } else {
                logger.warn("FTP connection was not available.");
                updateExportedFileStatus(dataExportFile, DataExportManager.FileStatus.IN_QUEUE);
            }
        } catch (IOException e) {
            updateExportedFileStatus(dataExportFile, DataExportManager.FileStatus.IN_QUEUE);
            logger.error("Unable to copy the file " + txtFile.getName() + "to FTP");
        } finally {
            disconnectFromFTP(client);
            if (uploadToFtpTimer != null) {
            	uploadToFtpTimer.stop();
            	uploadToFtpTimer.apply();
            }
        }
    }

    /**
     * Establish a connection to the RetailerSite FTP as per the ftp.properties
     *
     * @param ftpProperties
     * @return FTPClient object
     */
    private FTPClient connectToFTP(Properties ftpProperties) {
        FTPClient client = new FTPClient();
        final String ftpHostname = ftpProperties.getProperty("sftp.Hostname");
        final String ftpUsername = ftpProperties.getProperty("sftp.Username");
        final String ftpPassword = ftpProperties.getProperty("sftp.Password");
        final String ftpLocation = ftpProperties.getProperty("sftp.Location");
        final String ftpPortNumber = ftpProperties.getProperty("sftp.PortNumber");
        boolean login = false;

        try {
            client.connect(ftpHostname);
            // When login success the login method returns true.
            login = client.login(ftpUsername, ftpPassword);
        } catch (IOException e) {
            logger.error("Exception occurred while authenticating to FTP : " + ftpHostname);
        }

        if (login) {
            return client;
        }

        return null;
    }

    /**
     * Release the active connection to the FTP site.
     *
     * @param ftpClient
     */
    private void disconnectFromFTP(FTPClient ftpClient) {
        if (ftpClient != null) {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                logger.error("Error occurred while disconnecting from FTP");
            }
        }
    }


    /**
     * Verify if all the necessary key parameters are found in the ftp.properties file
     *
     * @param ftpProperties
     * @return true if correctly configured. false if there is any missing property
     */
    protected boolean isFTPConfiguredCompletely(Properties ftpProperties) {

        if (StringUtils.isEmpty(ftpProperties.getProperty("sftp.Hostname")))
            return false;

        if (StringUtils.isEmpty(ftpProperties.getProperty("sftp.Username")))
            return false;

        if (StringUtils.isEmpty(ftpProperties.getProperty("sftp.Password")))
            return false;

        //Location is optional as of now.
        /* if (StringUtils.isEmpty(ftpProperties.getProperty("sftp.Location")))
       return false;*/

        if (StringUtils.isEmpty(ftpProperties.getProperty("sftp.PortNumber")))
            return false;

        /* if (StringUtils.isEmpty(ftpProperties.getProperty("sftp.Enabled")))
       return false;*/

        if (StringUtils.isEmpty(ftpProperties.getProperty("sftp.MerchantId")))
            return false;

        return true;
    }

    /**
     * Upon successful FTP upload, update the exportStatus on the record to 1.
     *
     * @param exportFile - The DataExportFile for which the status needs to be updated.
     */
    protected void updateExportedFileStatus(DataExportFile exportFile, int status) {
        logger.warn("Export Status::::::"+status);
        exportFile.setExportStatus(status);
        this.dataExportManager.updateExportedFileStatus(exportFile);
    }


    @Autowired
    private DataExportManager dataExportManager;

    public void setDataExportManager(DataExportManager dataExportManager) {
        this.dataExportManager = dataExportManager;
    }

    @Autowired
    private RetailerManager retailerManager;


    public void setRetailerManager(RetailerManager retailerManager) {
        this.retailerManager = retailerManager;
    }
}
