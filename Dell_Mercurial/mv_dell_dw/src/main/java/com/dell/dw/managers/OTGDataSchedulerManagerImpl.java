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
import org.apache.commons.vfs2.impl.DefaultFileSystemManager;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.http.HttpFileObject;
import org.apache.commons.vfs2.provider.http.HttpFileProvider;
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
@Service(value = "otgDataSchedulerManagerImpl")
public class OTGDataSchedulerManagerImpl implements OTGDataSchedulerManager, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(OTGDataSchedulerManagerImpl.class);


    /**
     Boolean value to check whether the downloading is in progress.
     */
    private static final AtomicBoolean downloadingFiles = new AtomicBoolean(false);

    private static AtomicInteger otgDataSchedulerJobRetryCount = new AtomicInteger(1);


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

        DefaultFileSystemManager fileSystemManager = null;

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
            String otgReportsLocation = configurationService.getProperty(OTGDataSchedulerManager.class, "otgReportsLocation");
            File tmpFile = null;
            HttpFileObject httpFileObject = null;
            FileObject localFile = null;
            FileObject remoteFile = null;

            try {
                fileSystemManager = new DefaultFileSystemManager();
                fileSystemManager.init();
                fileSystemManager.addProvider("http", new HttpFileProvider());
                logger.info("Downloading the file from OTG Reports Location");
                this.FILE_DOWNLOAD_STATUS = FileDownLoadStatus.FILE_DOWNLOADING;
                StandardFileSystemManager obj = new StandardFileSystemManager();
                obj.init();
                httpFileObject = (HttpFileObject) obj.resolveFile(otgReportsLocation);
                tmpFile = this.generateTempFile("otg_leads.csv");
                //download the remote file
                FileUtils.copyInputStreamToFile(httpFileObject.getInputStream(), tmpFile);
                localFile = obj.resolveFile(tmpFile.getAbsolutePath());

                logger.info("Download Complete - File :=" + localFile.getName().getBaseName());
                this.FILE_DOWNLOAD_STATUS = FileDownLoadStatus.FILE_DOWN_LOAD_SUCESSFULL;
                this.addFileIntoBatch(retailer, "otg_leads.csv", Source.HTTP, tmpFile);


            } catch (FileSystemException e) {
                logger.error(e.getMessage(), e);
                downloadingFiles.set(false);
                return;
            } catch (ObjectNotFoundException e) {
                logger.error("Unable to download - " + localFile.getName().getBaseName() + " - " + e.getMessage(), e);
                this.FILE_DOWNLOAD_STATUS = FileDownLoadStatus.FILE_DOWN_LOAD_ERROR;
            } catch (NonUniqueObjectException e) {
                logger.error("Unable to download - " + localFile.getName().getBaseName() + " - " + e.getMessage(), e);
                this.FILE_DOWNLOAD_STATUS = FileDownLoadStatus.FILE_DOWN_LOAD_ERROR;
            } catch (Exception e) {
                logger.error("Unable to download - " + localFile.getName().getBaseName() + " - " + e.getMessage(), e);
                this.FILE_DOWNLOAD_STATUS = FileDownLoadStatus.FILE_DOWN_LOAD_ERROR;
            } finally {
                FileUtils.deleteQuietly(tmpFile);
                if (localFile != null) {
                    localFile.close();
                }
                if (httpFileObject != null) {
                    httpFileObject.close();
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
                if(otgDataSchedulerJobRetryCount.get() <= ConfigurationServiceImpl.getInstance().getIntegerProperty("com.dell.dw.managers.OTGDataSchedulerManager.retryCount").intValue() ){
                    downloadingFiles.set(false);
                    otgDataSchedulerJobRetryCount.addAndGet(1);
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
        String timestampFilename = FilenameUtils.getBaseName(originalFilename) + "_" + FileUtils.generateTimestamp() + "." + FilenameUtils.getExtension(originalFilename);
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

        DataSource ds = dataSourceRepository.get(DataSource.DSConstants.OTG);
        String tempFileName = tempFile.getName();
        Collection<DataSchedulerBatch> resultFiles = new ArrayList<DataSchedulerBatch>();

        if (FilenameUtils.isExtension(tempFileName, new String[]{"csv"})) {

            String destFilename = FilenameUtils.getBaseName(srcFile) + "_" + FileUtils.generateTimestamp() + "." + FilenameUtils.getExtension(srcFile);
            File finalFile = new File(configurationService.getFileSystem().getDirectory("otg",true), destFilename);
            FileUtils.copyFile(tempFile, finalFile);
            DataSchedulerBatch dataSchedulerBatch =null;
            dataSchedulerBatch = new DataSchedulerBatch(ds,retailer.getId(), new Date(), new Date(), 0L,0L,DataSchedulerBatch.EndPoint.OTG,DataSchedulerBatch.Priority.UNKNOWN, DataSchedulerBatch.Status.IN_QUEUE,retailer,srcFile, "/otg/"+destFilename) ;

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

