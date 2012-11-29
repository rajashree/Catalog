/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

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
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.collections.PropertiesProvider;
import com.sourcen.dataimport.definition.DataImportConfig;
import com.sourcen.dataimport.definition.Schema;
import com.sourcen.dataimport.definition.TableDefinition;
import com.sourcen.dataimport.service.DataReader;
import com.sourcen.dataimport.service.DataWriter;
import com.sourcen.dataimport.service.errors.DataExceptionHandler;
import com.sourcen.dataimport.service.support.GenericDataImportService;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: vivekk $
 * @version $Revision: 3875 $, $Date:: 2012-07-09 07:01:56#$
 */

@Service
public class DataImportManagerImpl implements DataImportManager, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(DataImportManagerImpl.class);

    private static final Collection<String> supportedImageTypes = Arrays.asList("jpg", "png", "gif", "jpeg");

    /**
     * Boolean value to check whether the image processing is in progress.
     */
    private static final AtomicBoolean processingImages = new AtomicBoolean(false);

    /**
     * Constructor
     */
    public DataImportManagerImpl() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isProcessingImages() {
        return processingImages.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    // patch for https://jira.marketvine.com/browse/CS-330
    // making locks atomic on the repository rather than manager.
    public DataFile getLatestImportedFile() {
        DataFile dataFile = dataFileRepository.getLatestImportedFile();
        if (dataFile != null) {
            DataFile lockedDataFile = dataFileRepository
                    .acquireLock(dataFile, FileStatus.IN_QUEUE, FileStatus.PROCESSING);
            // acquire lock.
            if (lockedDataFile == null) {
                logger.info("Unable to lock object from IN_QUEUE TO PROCESSING  objID:=" + dataFile + " as it was locked by " + dataFile
                        .getLockedThread());
                return null;
            }
            dataFile.getRetailerSite(); // load the lazy object for future use.
            dataFile.getRetailerSite().getRetailer();
            return lockedDataFile;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    // patch for https://jira.marketvine.com/browse/CS-330
    // making locks atomic on the repository rather than manager.
    public DataFile getLatestImportedImageFile() {
        DataFile dataFile = dataFileRepository.getLatestImageFile();
        if (dataFile != null) {
            dataFile = dataFileRepository.acquireLock(dataFile, FileStatus.IMAGES_IMPORTED, FileStatus.IMAGES_RESIZING);
            // acquire lock.
            if (dataFile == null) {
                logger.info("Unable to lock object from IN_QUEUE TO PROCESSING  objID:=" + dataFile);
                return null;
            }
            return dataFile;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processImportedFile(final DataFile dataFile) {
        CountStatMutator count = null;
        TimerStatMutator timing = null;
        try {
            count = (CountStatMutator) StatUtil.getInstance()
                    .getStat(CountMinMaxStat.class, "ProcessImportedFile.Count");
            count.inc();
            count.apply();
            timing = (TimerStatMutator) StatUtil.getInstance().getStat(TimerStat.class, "ProcessImportedFile.Timer");
            timing.start();

            String fileProcessingStatus = "";
            /* productRepository.resetUpdateFlag();*/
            if (!dataFile.getStatus().equals(FileStatus.PROCESSING)) {
                logger.warn("dataFile was not locked for processing");
                return;
            }

            String loggerName = "com.dell.acs.dataimport.dataFile.logs." + StringUtils
                    .getSimpleString(dataFile.getFilePath().replace("/feeds", ""));
            Logger dataFileLogger = LoggerFactory.getLogger(loggerName);

            //        dataFileRepository.refresh(dataFile);
//	        RetailerSite retailerSite = dataFile.getRetailerSite();
            RetailerSite retailerSite = retailerSiteRepository.getByName(dataFile.getRetailerSite().getSiteName(), true);

            dataFileLogger.info("starting to process dataFile for retailerSite :=" + retailerSite
                    .getSiteName() + " dataFile:=" + dataFile);

            Integer finalFileStatus = FileStatus.DONE;
            fileProcessingStatus = "DONE";
            try {
                //get the schema file for merchant import type
                String dataImportType = retailerSite.getProperties().
                        getProperty("retailerSite.dataImportType.name", "ficstar").toLowerCase();

                String dataImportFileLocation = null;
                if (dataImportType.equalsIgnoreCase("merchant")) {
                    //Get the schema file for merchant);
                    // should give /config/dataimport/merchant/dell/dellsmb/data_import_config.xml
                    dataImportFileLocation = "/config/dataimport/providers/merchant/" + retailerSite.getRetailer().getName() + "/" + retailerSite.getSiteName() + "/data_import_config.xml";
                } else {
                    //Get the schema file for providers (CJ, GOOGLE, FICSTAR)
                    // /config/dataimport/ficstar/data_import_config.xml
                    dataImportFileLocation = "/config/dataimport/providers/" + dataImportType + "/data_import_config.xml";
                }

                logger.info("trying to load schema file :=" + dataImportFileLocation);
                File schemaFile = configurationService.getFileSystem().getFile(dataImportFileLocation, false, true);

                DataImportConfig dataImportConfig = applicationContext.getBean("dataImportConfig", DataImportConfig.class);
                dataImportConfig.setConfigFilePath(schemaFile.getAbsolutePath());
                dataImportConfig.afterPropertiesSet();
                // now the config is ready.

                Schema schema = dataImportConfig.getSchema();
                TableDefinition tableDefinition = schema.getDefinitionByDestination(dataFile.getImportType());
                Assert.notNull(tableDefinition, "cannot find tableDefinition for :=" + dataFile
                        .getImportType() + " in schema :=" + schemaFile.getAbsolutePath());

                String csvFilePath = FilenameUtils
                        .normalize(configurationService.getFileSystem().getFileSystemAsString() + "/" + dataFile
                                .getFilePath());
                tableDefinition.setProperty("path", csvFilePath);
                tableDefinition.setProperty("relativePath", dataFile.getFilePath());
                tableDefinition.setProperty("isPathAbsolute", "true");

                // some feeds like google dont have siteName == RetailerSiteName
                tableDefinition.setProperty("columns.siteName.defaultValue", retailerSite.getSiteName());

                // now we have retailerSite specific readers and writers.
                String siteName = retailerSite.getSiteName();
                DataReader dataReader = null;
                DataWriter dataWriter = null;
                String dataImportTypeName = retailerSite.getProperties().getProperty("retailerSite.dataImportType.name");
                if (dataImportTypeName != null) {
                    if (dataImportTypeName.equalsIgnoreCase("ficstar")) {
                        // default Import Type. will use 'csvDataReader' and 'hibernateDataWriter'
                        dataImportTypeName = null;
                    } else {
                        dataImportTypeName = dataImportTypeName.toLowerCase();
                        if (dataImportTypeName.equals("merchant")) {
                            dataImportTypeName = retailerSite.getSiteName();
                        }
                    }
                }

                if (dataImportTypeName != null) {
                    if (applicationContext.containsBean(dataImportTypeName + "CsvDataReader")) {
                        dataReader = applicationContext.getBean(dataImportTypeName + "CsvDataReader", DataReader.class);
                    }
                    if (applicationContext.containsBean(dataImportTypeName + "HibernateDataWriter")) {
                        dataWriter = applicationContext
                                .getBean(dataImportTypeName + "HibernateDataWriter", DataWriter.class);
                    }
                }

                if (dataReader == null) {
                    dataReader = applicationContext.getBean("csvDataReader", DataReader.class);
                }
                if (dataWriter == null) {
                    dataWriter = applicationContext.getBean("hibernateDataWriter", DataWriter.class);
                }

                DataExceptionHandler exceptionHandler = null;
                if (applicationContext.containsBean(tableDefinition.getSourceTable() + "DataExceptionHandler")) {
                    exceptionHandler = (DataExceptionHandler) applicationContext
                            .getBean(tableDefinition.getSourceTable() + "DataExceptionHandler");
                    exceptionHandler.setLogger(dataFileLogger);
                    dataReader.setExceptionHandler(exceptionHandler);
                    dataWriter.setExceptionHandler(exceptionHandler);

                }

                dataReader.setTableDefinition(tableDefinition);
                dataWriter.setTableDefinition(tableDefinition);

                dataFileLogger.info("Importing data from the feed file " + dataFile.getFilePath());
                GenericDataImportService dataImportService = applicationContext
                        .getBean("genericDataImportService", GenericDataImportService.class);
                dataImportService.setLogger(dataFileLogger);
                dataImportService.setDataImportConfig(dataImportConfig);
                dataImportService.setDataReader(dataReader);
                dataImportService.setDataWriter(dataWriter);
                dataReader.setLogger(dataFileLogger);
                dataWriter.setLogger(dataFileLogger);
                dataReader.initialize();
                dataWriter.initialize();
                dataImportService.setExceptionHandler(exceptionHandler);
                dataImportService.run();
                if (exceptionHandler.getReaderFailedCount() > 0) {
                    finalFileStatus = FileStatus.ERROR_READ;
                }
                if (exceptionHandler.getWriterFailedCount() > 0) {
                    finalFileStatus = FileStatus.ERROR_WRITE;
                }

            } catch (RuntimeException e) {
                finalFileStatus = FileStatus.ERROR_PARSING;
                fileProcessingStatus = "ERROR_PARSING";
                if (e.getCause() != null) {
                    Class errorClass = e.getCause().getClass();
                    if (errorClass.equals(FileNotFoundException.class) || errorClass.equals(IOException.class)) {
                        finalFileStatus = FileStatus.ERROR_READ;
                        dataFileLogger.error(e.getMessage());
                    } else {
                        dataFileLogger.error(e.getMessage(), e);
                    }
                } else {
                    dataFileLogger.error(e.getMessage(), e);
                }
            } catch (IOException e) {
                finalFileStatus = FileStatus.ERROR_READ;
                fileProcessingStatus = "ERROR_READ";
                dataFileLogger.error(e.getMessage(), e);
            } catch (Exception e) {
                dataFileLogger.error(e.getMessage(), e);
                finalFileStatus = FileStatus.ERROR_PARSING;
                fileProcessingStatus = "ERROR_PARSING";
            } finally {
                /*productRepository.updateFailCounter();*/
                dataFileRepository.acquireLock(dataFile, FileStatus.PROCESSING, finalFileStatus);
            }
        } finally {
            if (timing != null) {
                timing.stop();
                timing.apply();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void processImages(final DataFile dataFile) {
        CountStatMutator count = null;
        TimerStatMutator timing = null;
        try {
            count = (CountStatMutator) StatUtil.getInstance()
                    .getStat(CountMinMaxStat.class, "ProcessImages.Count");
            count.inc();
            count.apply();
            timing = (TimerStatMutator) StatUtil.getInstance().getStat(TimerStat.class, "ProcessImages.Timer");
            timing.start();

            Integer finalFileStatus = FileStatus.IMAGES_RESIZED;
            try {
                if (!processingImages.compareAndSet(false, true)) {
                    logger.info("Images are already being processed");
                    return;
                }

                if (!dataFile.getStatus().equals(FileStatus.IMAGES_RESIZING)) {
                    logger.warn("Image was not locked for processing");
                    return;
                }
                dataFileRepository.refresh(dataFile);

                // get properties from retailerSite.
                // productimages.sizes.small.width=100
                // productimages.sizes.small.height=100
                // productimages.sizes.large.height=500

                PropertiesProvider properties = dataFile.getRetailerSite().getProperties();

                Collection<String> sizeProperties = properties.getPropertyNames("productimages.sizes", true);
                Map<String, Integer> sizes = new HashMap<String, Integer>();
                for (String sizeKey : sizeProperties) {
                    if (sizeKey.indexOf(".") > 0) {
                        String size = sizeKey.substring(0, sizeKey.indexOf("."));

                        Integer imageWidth = properties.getIntegerProperty("productimages.sizes." + size + ".width");
                        if (imageWidth == null) {
                            imageWidth = configurationService
                                    .getIntegerProperty(DataImportManager.class, size + ".width", 400);
                        }
                        sizes.put("_size_" + size, imageWidth);
                    }
                }
                if (sizes.isEmpty()) {
                    sizes.put("_size_tiny", 50);
                    sizes.put("_size_small", 100);
                    sizes.put("_size_medium", 200);
                    sizes.put("_size_large", 400);
                }
                try {
                    FileSystem fileSystem = configurationService.getFileSystem();
                    final File retailerSiteCDNImageDir = fileSystem.getDirectory(dataFile.getFilePath() + "/", true);
                    //delete the files which were resized previously
//	                logger.info("Deleting old resized images");
                    //File[] images = retailerSiteCDNImageDir.listFiles();
//	                for (File image : images) {
//	                    if (image.exists() && image.canRead()) {
//	                        String imageName = FilenameUtils.getBaseName(image.getAbsolutePath());
//	                        if (imageName.lastIndexOf("_size_") > -1) {
//	                            FileUtils.deleteQuietly(image);
//	                        }
//	                    }
//	                }

//	                logger.info("Deleted old resized images in the directory:= " + retailerSiteCDNImageDir
//	                        .getAbsolutePath());
                    //Now resize all the files freshly
                    File[] originalImages = retailerSiteCDNImageDir.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File file) {
                            // if file does not have ___size__ then return true.
                            String baseName = FilenameUtils.getBaseName(file.getAbsolutePath());
                            if (!baseName.contains("_size_")) {
                                // for original file check if there are size.
                                String xtn = FilenameUtils.getExtension(file.getName());
                                if (supportedImageTypes.contains(xtn.toLowerCase())) {
                                    boolean isResized = new File(retailerSiteCDNImageDir, baseName + "_size_tiny." + xtn).exists();
                                    return !isResized;
                                }
                            }
                            return false;
                        }
                    });
                    logger.info("Found --> " + originalImages.length + " new Images at " + retailerSiteCDNImageDir.getPath());
                    logger.info("Started resizing new images in the directory :=" + dataFile.getFilePath());
                    for (File image : originalImages) {
                        String imageName = FilenameUtils.getBaseName(image.getAbsolutePath());
                        String imageType = FilenameUtils.getExtension(image.getAbsolutePath());
                        if (!image.canRead()) {
                            logger.warn("Unable to read image :=" + image.getAbsolutePath());
                            continue;
                        }
                        if (!supportedImageTypes.contains(imageType.toLowerCase())) {
                            logger.warn("Unable to process non-image file :=" + image.getAbsolutePath());
                            continue;
                        }

                        logger.info("Resizing image " + imageName + " at " + dataFile.getFilePath());
                        try {
                            BufferedImage bufferedImage = ImageIO.read(image);

                            for (Map.Entry<String, Integer> entry : sizes.entrySet()) {
                                String size = entry.getKey();
                                Integer maxTargetSize = entry.getValue();
                                File outputFile = fileSystem
                                        .getFile(retailerSiteCDNImageDir, imageName + size + "." + imageType, true, true);
                                try {
                                    BufferedImage thumbnail = Scalr
                                            .resize(bufferedImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, maxTargetSize, Scalr.OP_ANTIALIAS);
                                    ImageIO.write(thumbnail, imageType, outputFile);

                                } catch (Exception e) {
                                    logger.info("Could not resize the image " + imageName + " making the copies of the original image " + "for all sizes ");
                                    try {
                                        FileUtils.copyFile(image, outputFile);
                                    } catch (IOException ioe) {
                                        logger.error("Unable to copy the file:= " + image.getName());
                                    }
                                }
                            }
                        } catch (Exception e) {
                            logger.info("The image could not be resized:=" + image.getAbsolutePath(), e);
                        }
                        logger.debug("Finished resizing " + imageName + " at " + dataFile.getFilePath());
                    }
                    logger.info("Finished resizing of all the images at " + dataFile.getFilePath());
                } catch (IOException e) {
                    processingImages.set(false);
                    finalFileStatus = FileStatus.ERROR_RESIZING;
                    logger.warn("Unable to resize images:= " + e.getMessage(), e);
                } catch (Exception e) {
                    processingImages.set(false);
                    finalFileStatus = FileStatus.ERROR_RESIZING;
                    logger.error("Unable to resize images:= " + e.getMessage(), e);
                }
            } catch (Exception e) {
                finalFileStatus = FileStatus.ERROR_RESIZING;
                processingImages.set(false);
                logger.error(e.getMessage(), e);
            } finally {
                dataFileRepository.acquireLock(dataFile, FileStatus.IMAGES_RESIZING, finalFileStatus);
            }
            processingImages.set(false);
        } finally {
            if (timing != null) {
                timing.stop();
                timing.apply();
            }
        }
    }

    @Override
    public Collection<String> getDownLoadedDataFiles(RetailerSite retailerSite, int maxResultSize) {
        return dataFileRepository.getDownLoadedDataFiles(retailerSite, maxResultSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void update(final DataFile dataFile) {
        dataFileRepository.update(dataFile);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<DataFile> getDataFiles(final RetailerSite retailerSite) {
        return dataFileRepository.getFiles(retailerSite);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Collection<DataFile> getAllDataFilesInProcessingStatus() {
        return dataFileRepository.getAllDataFilesInProcessingStatus();
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
