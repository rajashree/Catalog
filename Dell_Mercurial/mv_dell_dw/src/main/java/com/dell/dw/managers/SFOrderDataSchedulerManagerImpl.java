package com.dell.dw.managers;

import com.dell.dw.persistence.domain.DataSchedulerBatch;
import com.dell.dw.persistence.domain.DataSource;
import com.dell.dw.persistence.domain.Retailer;
import com.dell.dw.persistence.repository.DataSchedulerBatchRepository;
import com.dell.dw.persistence.repository.DataSourceRepository;
import com.dell.dw.persistence.repository.RetailerRepository;
import com.sourcen.core.ObjectNotFoundException;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.config.ConfigurationServiceImpl;
import com.sourcen.core.util.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.hibernate.NonUniqueObjectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 6/26/12
 * Time: 11:12 AM
 * To change this template use File | Settings | File Templates.
 */
@Service(value="sfOrderDataSchedulerManagerImpl")
public class SFOrderDataSchedulerManagerImpl implements SFOrderDataSchedulerManager, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(SFOrderDataSchedulerManagerImpl.class);


    /**
     Boolean value to check whether the downloading is in progress.
     */
    private static final AtomicBoolean downloadingFiles = new AtomicBoolean(false);

    private static AtomicInteger sfOrderDataSchedulerJobRetryCount = new AtomicInteger(1);


    /**
     {@inheritDoc}
     */
    private FileDownLoadStatus FILE_DOWNLOAD_STATUS = FileDownLoadStatus.FILE_DOWNLOAD_STARTED;


    /**
     {@inheritDoc}
     */
    @Override
    public Boolean isDownloadingFiles() {
        return downloadingFiles.get();
    }


    /**
     {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void downloadDataFiles() {

        StandardFileSystemManager fileSystemManager = null;

        Retailer exampleObj = new Retailer();
        exampleObj.setName(configurationService.getProperty("app.retailer.name","dell"));
        Retailer retailer = (Retailer) retailerRepository.getUniqueByExample(exampleObj);
        try {
            if (!downloadingFiles.compareAndSet(false, true)) {
                logger.info("Files are already being downloaded.");
                this.FILE_DOWNLOAD_STATUS = FileDownLoadStatus.FILE_ALREADY_DOWN_LOAD;
                return;
            }
            logger.info("Downloading data files job started");

            Set<String> availableFiles = new HashSet<String>();



            String sfOrderReportsLocation = configurationService.getProperty(SFOrderDataSchedulerManager.class, "sfOrderReportsLocation");

            try {
                fileSystemManager = new StandardFileSystemManager();
                fileSystemManager.init();
                FileObject serverFileList = fileSystemManager.resolveFile(sfOrderReportsLocation);
                FileObject[] filesInOrderReportsLocation = serverFileList.getChildren();

                for (FileObject child : filesInOrderReportsLocation) {
                    if (child.getName().getExtension().equalsIgnoreCase("csv")) {
                        if((child.getName().getBaseName().indexOf("orders") >= 0) ||
                                (child.getName().getBaseName().indexOf("orderitems") >= 0) ||
                                (child.getName().getBaseName().indexOf("stores") >= 0)){
                            availableFiles.add(child.getName().getBaseName());
                        }
                    }
                }
            } catch (FileSystemException e) {
                logger.error(e.getMessage(), e);
                downloadingFiles.set(false);
                return;
            }
            logger.info("Number of order files available for processing := " + availableFiles.size());

            for (String downloadFilename : availableFiles) {
                File tmpFile = null;
                FileObject remoteFile = null;
                FileObject localFile = null;
                try {
                    logger.info("Downloading the file " + downloadFilename + " from Order Reports Location");
                    this.FILE_DOWNLOAD_STATUS = FileDownLoadStatus.FILE_DOWNLOADING;
                    remoteFile = fileSystemManager.resolveFile(sfOrderReportsLocation + downloadFilename);
                    if (!remoteFile.exists()) {
                        logger.error("File not found := " + downloadFilename);
                    } else {

                        tmpFile = this.generateTempFile(downloadFilename);
                        localFile = fileSystemManager.resolveFile(tmpFile.getAbsolutePath());
                        //download the remote file
                        localFile.copyFrom(remoteFile, Selectors.SELECT_SELF);
                        logger.info("Download Complete - File :=" + downloadFilename);
                        this.FILE_DOWNLOAD_STATUS = FileDownLoadStatus.FILE_DOWN_LOAD_SUCESSFULL;
                        this.addFileIntoBatch(retailer, downloadFilename, Source.FTP, tmpFile);
                    }
                } catch (ObjectNotFoundException e) {
                    logger.error("Unable to download - " + downloadFilename + " - " + e.getMessage(), e);
                    this.FILE_DOWNLOAD_STATUS = FileDownLoadStatus.FILE_DOWN_LOAD_ERROR;
                } catch (NonUniqueObjectException e) {
                    logger.error("Unable to download - " + downloadFilename + " - " + e.getMessage(), e);
                    this.FILE_DOWNLOAD_STATUS = FileDownLoadStatus.FILE_DOWN_LOAD_ERROR;
                } catch (Exception e) {
                    logger.error("Unable to download - " + downloadFilename + " - " + e.getMessage(), e);
                    this.FILE_DOWNLOAD_STATUS = FileDownLoadStatus.FILE_DOWN_LOAD_ERROR;
                } finally {
                    FileUtils.deleteQuietly(tmpFile);
                    if (localFile != null) {
                        localFile.close();
                    }
                    if (remoteFile != null) {
                        remoteFile.close();
                    }
                }
            }
        } catch (Exception e) {
            downloadingFiles.set(false);
            logger.error("error while downloading files", e);
            this.FILE_DOWNLOAD_STATUS = FileDownLoadStatus.FILE_DOWN_LOAD_FAIL;
        } finally {

            if (fileSystemManager != null) {
                fileSystemManager.close();
            }
            if (FILE_DOWNLOAD_STATUS == FileDownLoadStatus.FILE_DOWN_LOAD_ERROR) {
                FILE_DOWNLOAD_STATUS = FileDownLoadStatus.FILE_DOWN_LOAD_FAIL;
                logger.error("FILE_DOWNLOAD_STATUS :-> FILE_DOWN_LOAD_FAIL");
                if(sfOrderDataSchedulerJobRetryCount.get() <= ConfigurationServiceImpl.getInstance().getIntegerProperty("com.dell.dw.managers.SFOrderDataSchedulerManager.retryCount").intValue() ){
                    downloadingFiles.set(false);
                    sfOrderDataSchedulerJobRetryCount.addAndGet(1);
                    downloadDataFiles();
                }
            }
        }
        downloadingFiles.set(false);
        logger.info("FILE_DOWNLOAD_STATUS " + this.FILE_DOWNLOAD_STATUS);
    }


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
     {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Collection<DataSchedulerBatch> addFileIntoBatch(final Retailer retailer, final String srcFile,
                                                           final Source fileSource, File tempFile) throws IOException {

        DataSource ds = dataSourceRepository.get(DataSource.DSConstants.SF_ORDER);
        String tempFileName = tempFile.getName();
        Collection<DataSchedulerBatch> resultFiles = new ArrayList<DataSchedulerBatch>();

        if (FilenameUtils.isExtension(tempFileName, new String[]{"csv"})) {

            //String destFilename = FileUtils.generateTimestampFilename(srcFile);
            File finalFile = new File(configurationService.getFileSystem().getDirectory("sf_order",true), srcFile);
            FileUtils.copyFile(tempFile, finalFile);
            DataSchedulerBatch dataSchedulerBatch =null;
            if (srcFile.indexOf("stores") >= 0) {
                dataSchedulerBatch = new DataSchedulerBatch(ds,retailer.getId(), new Date(), new Date(), 0L,0L,DataSchedulerBatch.EndPoint.STORES, DataSchedulerBatch.Priority.STORES, DataSchedulerBatch.Status.IN_QUEUE,retailer,srcFile, "/sf_order/"+srcFile) ;
            } else if (srcFile.indexOf("orders") >= 0) {
                dataSchedulerBatch = new DataSchedulerBatch(ds,retailer.getId(), new Date(), new Date(), 0L,0L,DataSchedulerBatch.EndPoint.SF_ORDERS,DataSchedulerBatch.Priority.SF_ORDERS, DataSchedulerBatch.Status.IN_QUEUE,retailer,srcFile, "/sf_order/"+srcFile) ;
            } else if (srcFile.indexOf("orderitems") >= 0) {
                dataSchedulerBatch = new DataSchedulerBatch(ds,retailer.getId(), new Date(), new Date(), 0L,0L,DataSchedulerBatch.EndPoint.SF_ORDER_ITEMS,DataSchedulerBatch.Priority.SF_ORDERITEMS, DataSchedulerBatch.Status.IN_QUEUE,retailer,srcFile, "/sf_order/"+srcFile) ;
            }

            if(dataSchedulerBatch != null){
                dataSchedulerBatchRepository.insert(dataSchedulerBatch);
                resultFiles.add(dataSchedulerBatch);
            }
            FileUtils.deleteQuietly(tempFile);
        } else {
            logger.warn("Uploaded csv was not in standard format");
        }
        return resultFiles;
    }


    @Override
    public FileDownLoadStatus getFileDownLoadStatus() {
        return this.FILE_DOWNLOAD_STATUS;
    }

    //
    // IoC
    //

    /**
     ApplicationContext bean injection.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     DataFileRepository bean injection.
     */
    @Autowired
    private DataSchedulerBatchRepository dataSchedulerBatchRepository;

    /**
     ConfigurationService bean injection.
     */
    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    DataSourceRepository dataSourceRepository;


    public FileDownLoadStatus getFILE_DOWNLOAD_STATUS() {
        return FILE_DOWNLOAD_STATUS;
    }

    public void setFILE_DOWNLOAD_STATUS(FileDownLoadStatus FILE_DOWNLOAD_STATUS) {
        this.FILE_DOWNLOAD_STATUS = FILE_DOWNLOAD_STATUS;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public DataSchedulerBatchRepository getDataSchedulerBatchRepository() {
        return dataSchedulerBatchRepository;
    }

    public void setDataSchedulerBatchRepository(DataSchedulerBatchRepository dataSchedulerBatchRepository) {
        this.dataSchedulerBatchRepository = dataSchedulerBatchRepository;
    }

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public RetailerRepository getRetailerRepository() {
        return retailerRepository;
    }

    public void setRetailerRepository(RetailerRepository retailerRepository) {
        this.retailerRepository = retailerRepository;
    }

    public DataSourceRepository getDataSourceRepository() {
        return dataSourceRepository;
    }

    public void setDataSourceRepository(DataSourceRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }
}

