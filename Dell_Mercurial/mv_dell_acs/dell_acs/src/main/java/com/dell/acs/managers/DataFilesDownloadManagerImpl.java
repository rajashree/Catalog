/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.ACSException;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.repository.DataFileRepository;
import com.dell.acs.persistence.repository.ProductRepository;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.dell.acs.stats.CountMinMaxStat;
import com.dell.acs.stats.CountStatMutator;
import com.dell.acs.stats.StatUtil;
import com.dell.acs.stats.TimerStat;
import com.dell.acs.stats.TimerStatMutator;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.util.FileSystem;
import com.sourcen.core.util.FileUtils;
import com.sourcen.core.util.ZipUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.vfs2.FileNotFolderException;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.hibernate.NonUniqueObjectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Sandeep
 * @author $LastChangedBy: sandeep $
 * @version $Revision: 3610 $, $Date:: 2012-06-25 15:52#$
 */
@Service
public class DataFilesDownloadManagerImpl implements DataFilesDownloadManager, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(DataFilesDownloadManagerImpl.class);


    /**
     * Status to check whether the downloading is in progress from different providers.
     */
    private static final AtomicBoolean downloadingFicstarFiles = new AtomicBoolean(false);

    private static final AtomicBoolean downloadingCJFiles = new AtomicBoolean(false);

    private static final AtomicBoolean downloadingGoogleFiles = new AtomicBoolean(false);

    private static final AtomicBoolean downloadingMerchantFiles = new AtomicBoolean(false);

    /**
     * {@inheritDoc}
     */
    private FileDownLoadStatus FILE_DOWNLOAD_STATUS = DataFilesDownloadManager.FileDownLoadStatus.FILE_DOWNLOAD_STARTED;


    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isDownloadingFiles(String providerName) {
        if (providerName.equals("ficstar")) {
            return downloadingFicstarFiles.get();
        }
        if (providerName.equals("cj")) {
            return downloadingCJFiles.get();
        }
        if (providerName.equals("google")) {
            return downloadingGoogleFiles.get();
        }
        if (providerName.equals("merchant")) {
            return downloadingMerchantFiles.get();
        }
        return false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void downloadDataFiles(String providerName) {
        CountStatMutator downloadCount = null;
        CountStatMutator fileCount = null;
        TimerStatMutator downloadTiming = null;
        TimerStatMutator downloadFileTiming = null;
        StandardFileSystemManager fileSystemManager = null;
        try {
            downloadCount = (CountStatMutator) StatUtil.getInstance()
                    .getStat(CountMinMaxStat.class, "DownloadDataFiles.DownloadCount");
            fileCount = (CountStatMutator) StatUtil.getInstance()
                    .getStat(CountMinMaxStat.class, "DownloadDataFiles.FileCount");
            downloadTiming = (TimerStatMutator) StatUtil.getInstance().getStat(TimerStat.class, "DownloadDataFiles.DownloadTiming");
        	downloadTiming.start();
            downloadFileTiming = (TimerStatMutator) StatUtil.getInstance().getStat(TimerStat.class, "DownloadDataFiles.DownloadFileTiming");

            //Set the downloading as true for current provier
            this.setDownloadingTrue(providerName);

            logger.info("Downloading data files job started for " + providerName.toUpperCase() + " provider");

            Set<String> availableFiles = new HashSet<String>();

            String sftpHostname =
                    configurationService.getProperty(DataFilesDownloadManager.class, providerName + ".sftpHostname");
            String sftpUsername =
                    configurationService.getProperty(DataFilesDownloadManager.class, providerName + ".sftpUsername");
            String sftpPassword =
                    configurationService.getProperty(DataFilesDownloadManager.class, providerName + ".sftpPassword");
            String sftpLocation =
                    configurationService.getProperty(DataFilesDownloadManager.class, providerName + ".sftpLocation");
            String sftpPortNumber =
                    configurationService.getProperty(DataFilesDownloadManager.class, providerName + ".sftpPortNumber");
            if (sftpHostname == null || sftpLocation == null || sftpPassword == null || sftpUsername == null ||
                    sftpPortNumber == null) {
                throw new ACSException(
                        "Unalbe to find FTP configurations for the provider/retailerSite:= " +
                                providerName.toUpperCase());
            }
            String sftpURI = "sftp://" + sftpUsername + ":" + sftpPassword + "@" + sftpHostname + ":" + sftpPortNumber
                    + "/" + sftpLocation;

            //create filesystem options for accessing sftp files
            FileSystemOptions fileSystemOptions = new FileSystemOptions();
            SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(fileSystemOptions, "no");
            SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(fileSystemOptions, true);

            //Boolean property to check whether to download only the latest feeds for retailers(Default is true).
            Boolean includeOnlyLatestFeeds = configurationService
                    .getBooleanProperty(DataFilesDownloadManager.class, "includeOnlyLatestFeeds", true);

            if (providerName.equalsIgnoreCase("cj") || providerName.equalsIgnoreCase("google")) {
                includeOnlyLatestFeeds = false;
            }

            try {
                fileSystemManager = new StandardFileSystemManager();
                if (fileSystemManager == null) {
                    throw new Exception("Unable to get file system manager");
                }
                fileSystemManager.init();
                FileObject serverFileList = null;
                try {
                    serverFileList = fileSystemManager.resolveFile(sftpURI, fileSystemOptions);
                } catch (FileSystemException fse) {
                    setDownloadingFalse(providerName);
                    logger.error(fse.getMessage());
                    return;
                }
                FileObject[] filesInSftpLocation = serverFileList.getChildren();

                // download only 2 file per retailerSite(1-feed+1-image).
                Map<String, FileObject> latestRetailerSiteFiles = new HashMap<String, FileObject>(10);
            	downloadCount.incBy(latestRetailerSiteFiles.size());
                for (FileObject child : filesInSftpLocation) {
                    if (!includeOnlyLatestFeeds) {
                        availableFiles.add(child.getName().getBaseName());
                        continue;
                    }
                    if (child.getName().getExtension().equalsIgnoreCase("csv")) {
                        availableFiles.add(child.getName().getBaseName());
                    } else if (child.getName().getExtension().equalsIgnoreCase("zip")) {
                        // if its zip, check that its the latest for the retailer.
                        String fileName = child.getName().getBaseName();
                        String probableRetailerSiteName = null;
                        if (fileName.indexOf("_feed") > 0) {
                            probableRetailerSiteName = fileName.substring(0, fileName.indexOf("_feed") + 5);
                        } else if (fileName.indexOf("_images") > 0) {
                            probableRetailerSiteName = fileName.substring(0, fileName.indexOf("_images") + 7);
                        } else {
                            logger.warn("Invalid zip file(Neither a feed nor an image) :=" + fileName);
                            continue;
                        }
                        if (!latestRetailerSiteFiles.containsKey(probableRetailerSiteName)) {
                            latestRetailerSiteFiles.put(probableRetailerSiteName, child);
                        } else {
                            FileObject oldFileObject = latestRetailerSiteFiles.get(probableRetailerSiteName);
                            if (oldFileObject.getContent().getLastModifiedTime()
                                    < child.getContent().getLastModifiedTime()) {
                                latestRetailerSiteFiles.put(probableRetailerSiteName, child);
                            }
                        }
                    }
                }
                if (includeOnlyLatestFeeds) {
                    for (FileObject imageFile : latestRetailerSiteFiles.values()) {
                        availableFiles.add(imageFile.getName().getBaseName());
                    }
                    logger.info("Found " + filesInSftpLocation.length + " files in FTP but downloading only "
                            + availableFiles.size() + " files that are latest for a retailer.");
                }
            } catch (FileNotFolderException fnfe) {
                logger.error(fnfe.getMessage(), fnfe);
                setDownloadingFalse(providerName);
                return;
            } catch (FileSystemException e) {
                logger.error(e.getMessage(), e);
                setDownloadingFalse(providerName);
                return;
            }
            logger.info("Connection to FTP server was successful for provider." + providerName.toUpperCase());

            Set<String> filesToProcess = new TreeSet<String>(new Comparator<String>() {
                @Override
                public int compare(final String o1, final String o2) {
                    if (o1 != null && o1.contains("product")) {
                        return -1;
                    }
                    if (o1 != null && o1.contains("image")) {
                        return 1;
                    }
                    return 2;
                }
            });

            if (availableFiles.isEmpty()) {
                logger.info("no files to download. terminating process.");
                setDownloadingFalse(providerName);
                return;
            }

            List<DataFile> dataFiles = dataFileRepository.getDataFilesWithSrcPath(availableFiles);
            if (dataFiles.isEmpty()) {
                filesToProcess.addAll(availableFiles);
            } else {
                // Prepare already processed feeds list
                Set<String> feedOperationsProcessed = new HashSet<String>(dataFiles.size());
                for (DataFile dataFile : dataFiles) {
                    feedOperationsProcessed.add(dataFile.getSrcFile());
                }

                // Filter the processed feeds from the available feeds from FTP
                for (String filename : availableFiles) {
                    if (!feedOperationsProcessed.contains(filename)) {
                        String extension = FilenameUtils.getExtension(filename);
                        if (extension.equalsIgnoreCase("zip") || extension.equalsIgnoreCase("gz")
                                || extension.equalsIgnoreCase("csv")) {
                            filesToProcess.add(filename);
                        }
                    }
                }
            }
            logger.info("Number of files to be downloaded from FTP for " + providerName.toUpperCase() + " := " +
                    filesToProcess.size());
        	fileCount.incBy(filesToProcess.size());
            String filesystem = configurationService.getProperty("filesystem.dataFiles.directory");
            String tmpDir = filesystem + configurationService.getProperty("filesystem.dataFiles.temp");
            RetailerSite retailerSite = null;
            for (String downloadFilename : filesToProcess) {
        		downloadFileTiming.start();
                File tmpFile = null;
                FileObject remoteFile = null;
                FileObject localFile = null;
                String probableRetailerSiteName = null;
                try {
                    logger.info("Downloading the file " + downloadFilename + " from SFTP Server");
                    this.FILE_DOWNLOAD_STATUS = DataFilesDownloadManagerImpl.FileDownLoadStatus.FILE_DOWNLOADING;
                    remoteFile = fileSystemManager.resolveFile(sftpURI + downloadFilename, fileSystemOptions);
                    if (!remoteFile.exists()) {
                        logger.error("File not found := " + downloadFilename);
                    } else {
                        if (FilenameUtils.isExtension(downloadFilename, "gz")) {
                            if (!downloadFilename.contains("-")) {
                                throw new RuntimeException("Unable to determine the siteName from the file :="
                                        + downloadFilename);
                            }
                            //do not generate the timestamp for the gz files
                            tmpFile = this.generateTempFile(downloadFilename, false);
                            probableRetailerSiteName = downloadFilename.substring(0, downloadFilename.indexOf("-"));
                            retailerSite = this.retailerSiteRepository.getByName(probableRetailerSiteName);
                        } else {
                            if (!downloadFilename.contains("_")) {
                                throw new RuntimeException("Unable to determine the siteName from the file :="
                                        + downloadFilename);
                            }
                            tmpFile = this.generateTempFile(downloadFilename);
                            probableRetailerSiteName = downloadFilename.substring(0, downloadFilename.indexOf("_"));
                            retailerSite = this.retailerSiteRepository.getByName(probableRetailerSiteName);
                        }
                        if (!configurationService.getBooleanProperty(DataFilesDownloadManager.class,
                                retailerSite.getSiteName() + ".enabled", true)) {
                            throw new RuntimeException(
                                    "download for " + retailerSite.getSiteName() + " has been disabled");
                        }
                        localFile = fileSystemManager.resolveFile(tmpFile.getAbsolutePath());
                        //download the remote file
                        localFile.copyFrom(remoteFile, Selectors.SELECT_SELF);
                        logger.info("Download Complete - File :=" + downloadFilename + " is downloaded at " + tmpDir);
                        this.FILE_DOWNLOAD_STATUS =
                                DataFilesDownloadManagerImpl.FileDownLoadStatus.FILE_DOWN_LOAD_SUCESSFULL;
                        this.addFileIntoBatch(retailerSite, downloadFilename, Source.FTP, tmpFile);
                        DataFilesDownloadCache.setDownloadFile(retailerSite.getSiteName(), downloadFilename,
                                FileDownLoadStatus.FILE_DOWN_LOAD_SUCESSFULL);
                    }
                } catch (EntityNotFoundException enfe) {
                    logger.error("Unable to find retailerSite - " + probableRetailerSiteName);
                    this.FILE_DOWNLOAD_STATUS = DataFilesDownloadManagerImpl.FileDownLoadStatus.FILE_DOWN_LOAD_ERROR;
                } catch (NonUniqueObjectException nuoe) {
                    logger.error("We found more than 1 retailer site with the name :=" + probableRetailerSiteName
                            + " please fix this in the admin console.");
                    this.FILE_DOWNLOAD_STATUS = DataFilesDownloadManagerImpl.FileDownLoadStatus.FILE_DOWN_LOAD_ERROR;
                } catch (Exception e) {
                    logger.error("Unable to download - " + downloadFilename + " - " + e.getMessage(), e);
                    this.FILE_DOWNLOAD_STATUS = DataFilesDownloadManagerImpl.FileDownLoadStatus.FILE_DOWN_LOAD_ERROR;
                } finally {
                    if (retailerSite != null) {
                    	DataFilesDownloadCache
                                .setDownloadFile(retailerSite.getSiteName(), downloadFilename,
                                        this.FILE_DOWNLOAD_STATUS);
                    }
                    FileUtils.deleteQuietly(tmpFile);
                    if (localFile != null) {
                        localFile.close();
                    }
                    if (remoteFile != null) {
                        remoteFile.close();
                    }
            		downloadFileTiming.stop();
                }
            }
        } catch (Exception e) {
            setDownloadingFalse(providerName);
            logger.error("error while downloading files", e);
            this.FILE_DOWNLOAD_STATUS = FileDownLoadStatus.FILE_DOWN_LOAD_FAIL;
        } finally {
            if (fileSystemManager != null) {
                fileSystemManager.close();
            }
            if (FILE_DOWNLOAD_STATUS == DataFilesDownloadManagerImpl.FileDownLoadStatus.FILE_DOWN_LOAD_ERROR) {
                FILE_DOWNLOAD_STATUS = DataFilesDownloadManagerImpl.FileDownLoadStatus.FILE_DOWN_LOAD_FAIL;
                logger.error("FILE_DOWNLOAD_STATUS :-> FILE_DOWN_LOAD_FAIL");
            }

            if (downloadTiming != null) {
            	downloadTiming.stop();
            	downloadTiming.apply();
            }
            if (downloadCount != null) {
            	downloadCount.apply();
            }
            if (fileCount != null) {
            	fileCount.apply();
            }
            if (downloadFileTiming != null) {
            	downloadFileTiming.apply();
            }
        }

        setDownloadingFalse(providerName);
        logger.info("FILE_DOWNLOAD_STATUS " + this.FILE_DOWNLOAD_STATUS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File generateTempFile(final String originalFilename) throws IOException {
        File tempDir = configurationService.getFileSystem().getTempDirectory();
        String timestampFilename = FileUtils.generateTimestampFilename(originalFilename);
        File tempFile = new File(tempDir, timestampFilename);
        if (tempFile.createNewFile() && tempFile.canRead()) {
            return tempFile;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File generateTempFile(final String originalFilename, boolean generateTimeStamp) throws IOException {
        if (generateTimeStamp) {
            return this.generateTempFile(originalFilename);
        }
        File tempDir = configurationService.getFileSystem().getTempDirectory();
        File tempFile = new File(tempDir, originalFilename);
        if (tempFile.createNewFile() && tempFile.canRead()) {
            return tempFile;
        }
        return null;
    }

    /**
     {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Collection<DataFile> addFileIntoBatch(final RetailerSite retailerSite, final String srcFile,
                                                 final Source fileSource, File tempFile) throws IOException {

        String tempFileName = tempFile.getName();

        Collection<DataFile> resultFiles = new ArrayList<DataFile>();
        FileSystem fileSystem = configurationService.getFileSystem();

        String retailerSiteCDNDir = FileSystemUtil.getPath(retailerSite, "cdn");
        String retailerSiteFeedsDir = FileSystemUtil.getPath(retailerSite, "feeds");

        File retailerSiteDataDir = fileSystem.getDirectory(retailerSiteFeedsDir, true);
        fileSystem.getDirectory(retailerSiteCDNDir, true);

        if (FilenameUtils.isExtension(tempFileName, "gz")) {
            File newTmpFile = ZipUtils.extractGZipFile(tempFile, tempFile.getParentFile().getAbsolutePath());
            tempFile.delete();
            tempFile = newTmpFile;
            tempFileName = tempFile.getName();
        }

        if (FilenameUtils.isExtension(tempFileName, new String[]{"csv", "txt", "tab"})) {
            DataImportManager.ImportType importType = null;
            if (FilenameUtils.isExtension(tempFileName, "txt")) {
                importType = DataImportManager.ImportType.products;
            } else {
                importType = DataImportManager.ImportType.getTypeFromFilename(tempFileName);
            }
            if (!importType.equals(DataImportManager.ImportType.UNKNOWN)) {
                String destFilename = importType + "-" + FileUtils.generateTimestamp() + "." +
                        FilenameUtils.getExtension(tempFileName);
                File finalFile = new File(retailerSiteDataDir, destFilename);
                FileUtils.copyFile(tempFile, finalFile);

                DataFile dataFile = new DataFile(retailerSite, srcFile,
                        (retailerSiteFeedsDir + destFilename),
                        DataImportManager.FileStatus.IN_QUEUE, importType.getTableName(), importType.getPriority());

                dataFileRepository.insert(dataFile);
                resultFiles.add(dataFile);
                FileUtils.deleteQuietly(tempFile);
            } else {
                logger.warn("Uploaded csv was not in standard format");
            }
        }

        if (FilenameUtils.isExtension(tempFileName, "zip")) {
            File extractedLocation = new File(tempFile.getParentFile(), FilenameUtils.getBaseName(tempFile.getName())
                    + "_extracted");
            if (tempFileName.contains("images")) {
                try {
                    Integer imageZipStatus = DataImportManager.FileStatus.IMAGES_IMPORTED;
                    logger.info("Found a image zip:= " + tempFileName);
                    DataImportManager.ImportType
                            importType = DataImportManager.ImportType.getTypeFromFilename(tempFileName);
                    String retailerSiteCDNImageDir = FileSystem.getSimpleFilename(retailerSiteCDNDir + "images/");
                    File retailerSiteCDNImageDirectory = fileSystem.getDirectory(retailerSiteCDNImageDir, true);
                    try {
                        Collection<File> files = ZipUtils.extractZipFileWithStructure(tempFile, retailerSiteCDNImageDirectory.getAbsolutePath());
                        for (File file : files) {
                            // get base name;
                            String xtn = FilenameUtils.getExtension(file.getName());
                            String baseName = FilenameUtils.getBaseName(file.getName());
                            File fileTiny = new File(retailerSiteCDNImageDirectory, baseName + "_size_tiny." + xtn);
                            File fileSmall = new File(retailerSiteCDNImageDirectory, baseName + "_size_small." + xtn);
                            File fileLarge = new File(retailerSiteCDNImageDirectory, baseName + "_size_large." + xtn);
                            File fileMedium = new File(retailerSiteCDNImageDirectory, baseName + "_size_medium." + xtn);
                            if (fileTiny.exists()) {
                                fileTiny.delete();
                            }
                            if (fileLarge.exists()) {
                                fileLarge.delete();
                            }
                            if (fileSmall.exists()) {
                                fileSmall.delete();
                            }
                            if (fileMedium.exists()) {
                                fileMedium.delete();
                            }
                        }
                    } catch (Exception e) {
                        logger.error("Unable to extract images:= " + e.getMessage(), e);
                        imageZipStatus = DataImportManager.FileStatus.ERROR_EXTRACTING;
                    }
                    DataFile dataFile = new DataFile(retailerSite, srcFile, retailerSiteCDNImageDir,
                            imageZipStatus, importType.getTableName(),
                            importType.getPriority());
                    dataFileRepository.insert(dataFile);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            } else {
                try {
                    logger.info("Found a feeds zip:= " + tempFileName);
                    if (!extractedLocation.mkdirs()) {
                        logger.error("Unable to create directory:= " + extractedLocation.getAbsolutePath());
                    }
                    Collection<File> extractedFiles = ZipUtils.extractZipFileWithStructure(tempFile,
                            extractedLocation.getAbsolutePath(), new FileFilter(){
                        @Override
                        public boolean accept(File pathname) {
                            return FilenameUtils.getExtension(pathname.getAbsolutePath()).equals("csv");
                        }
                    });

                    for (File file : extractedFiles) {

                        if (FilenameUtils.isExtension(file.getName(), "csv") ||
                                FilenameUtils.isExtension(file.getName(), "txt")) {

                            DataImportManager.ImportType
                                    importType = DataImportManager.ImportType.getTypeFromFilename(file.getName());

                            if (!importType.equals(DataImportManager.ImportType.UNKNOWN)) {

                                String destFilename = retailerSiteFeedsDir + importType + "-"
                                        + FileUtils.generateTimestamp() + ".csv";
                                File finalFile = fileSystem.getFile(destFilename, true, true);
                                FileUtils.copyFile(file, finalFile);
                                logger.info("Copied feed file - " + FilenameUtils.getName(destFilename) + " to "
                                        + retailerSiteFeedsDir);

                                DataFile dataFile = new DataFile(retailerSite, srcFile,
                                        destFilename,
                                        DataImportManager.FileStatus.IN_QUEUE, importType.getTableName(),
                                        importType.getPriority());

                                dataFileRepository.insert(dataFile);
                                resultFiles.add(dataFile);
                            } else {
                                logger.info("Ignoring file as we cannot determine the fileType :="
                                        + file.getAbsolutePath());
                            }
                        } else {
                            logger.info("Ignoring file as its not a csv :=" + file.getAbsolutePath());
                        }
                    }
                } catch (IOException ioe) {
                    logger.error("Unable to extract zip file:=" + tempFileName, ioe);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
            try {
                FileUtils.deleteDirectory(extractedLocation);
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
            try {
                FileUtils.deleteQuietly(tempFile);
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }
        return resultFiles;
    }

    /**
     * Sets the downloading status for different providers to FALSE.
     *
     * @param providerName
     * @return
     */
    private boolean setDownloadingFalse(String providerName) {
        if (providerName.equals("ficstar")) {
            downloadingFicstarFiles.set(false);
        }
        if (providerName.equals("cj")) {
            downloadingCJFiles.set(false);
        }
        if (providerName.equals("google")) {
            downloadingGoogleFiles.set(false);
        }
        if (providerName.equals("merchant")) {
            downloadingMerchantFiles.set(false);
        }
        return true;
    }


    /**                                                        `
     * Sets the downloading status for different providers to TRUE.
     *
     * @param providerName
     * @return
     */

    private boolean setDownloadingTrue(String providerName) {
        if (providerName.equals("ficstar")) {
            downloadingFicstarFiles.compareAndSet(false, true);
        }
        if (providerName.equals("cj")) {
            downloadingCJFiles.compareAndSet(false, true);
        }
        if (providerName.equals("google")) {
            downloadingGoogleFiles.compareAndSet(false, true);
        }
        if (providerName.equals("merchant")) {
            downloadingMerchantFiles.compareAndSet(false, true);
        }
        return false;
    }

    @Override
    public FileDownLoadStatus getFileDownLoadStatus() {
        return this.FILE_DOWNLOAD_STATUS;
    }

    //
    // IoC
    //

    /**
     * ApplicationContext bean injection.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * DataFileRepository bean injection.
     */
    @Autowired
    private DataFileRepository dataFileRepository;

    /**
     * RetailerSiteRepository bean injection.
     */
    @Autowired
    private RetailerSiteRepository retailerSiteRepository;

    /**
     * ConfigurationService bean injection.
     */
    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Setter for applicationContext
     */
    public void setApplicationContext(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Setter for retailerSiteRepository
     */
    public void setRetailerSiteRepository(final RetailerSiteRepository retailerSiteRepository) {
        this.retailerSiteRepository = retailerSiteRepository;
    }

    /**
     * Setter for configurationService
     */
    public void setConfigurationService(final ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    /**
     * Setter for dataFileRepository
     */
    public void setDataFileRepository(final DataFileRepository dataFileRepository) {
        this.dataFileRepository = dataFileRepository;
    }

    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
