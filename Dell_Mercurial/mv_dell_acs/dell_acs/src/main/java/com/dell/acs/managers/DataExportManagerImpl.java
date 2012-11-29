package com.dell.acs.managers;

import au.com.bytecode.opencsv.CSVWriter;
import com.dell.acs.persistence.domain.*;
import com.dell.acs.persistence.repository.*;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.util.DateUtils;
import com.sourcen.core.util.FileSystem;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.UrlUtils;
import com.sourcen.core.util.collections.PropertiesProvider;
import com.sourcen.dataimport.definition.DataImportConfig;
import com.sourcen.dataimport.definition.Schema;
import com.sourcen.dataimport.definition.TableDefinition;
import com.sourcen.dataimport.service.DataReader;
import com.sourcen.dataimport.service.DataWriter;
import com.sourcen.dataimport.service.errors.DataExceptionHandler;
import com.sourcen.dataimport.service.support.GenericDataImportService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * TODO: Navin review it and let me know of the implementation. We can optimize this ;)
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 3706 $, $Date:: 2012-06-28 07:18:22#$
 */
@Service
public class DataExportManagerImpl implements DataExportManager, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(DataExportManagerImpl.class);
    private static final String CSV_EXT = ".csv";
    private static final String TMP_EXT = ".tmp";
    private static final String TXT_EXT = ".txt";


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void executeJobForRetailerSite(final RetailerSite retailerSite) {
        CSVWriter writer = null;
        try {
            logger.info(" Data export for retailerSite  -   " + retailerSite.getSiteName());
            FileSystem fileSystem = null;
            File ftpConfigFile = null;
            File exportConfigDir = null;
            try {
                fileSystem = this.configurationService.getFileSystem();
                String retailerSiteConfigDir = FileSystemUtil.getConfigDir(retailerSite);
                ftpConfigFile = this.configurationService.getFileSystem().getFile(retailerSiteConfigDir + "/ftp.properties", false, false);

            } catch (Exception e) {
                logger.warn("Unable to export data for retailerSite:=" + retailerSite.getId());
            }

            String exportDir = FileSystemUtil.getDataExportDir(retailerSite);

            // At this movement the output format is same for all exports.
            //Generate the output feed ONLY for those RetailerSites which have the ftp.properties defined.

            if (ftpConfigFile != null && (ftpConfigFile.exists() || ftpConfigFile.canRead())) {
                logger.info("Export configuration found for the retailer  ::: " + retailerSite.getSiteName());

                //Verify, if there was a recent data-import-job which was completed for the given retailerSite by looking up the outputStatus
                DataFile dataFilePending = this.getFilePendingForExport(retailerSite);
                if (dataFilePending != null && dataFilePending.getOutputStatus() == 1) {


                    //Get File Name from FTP properties "sftp.merchantid", this merchantid is the storefront merchantid mapped to conten server merchant
                    //Jira Ticket: External.Content Server CS-336
                    //String outputFilename = retailerSite.getId() + "_" + retailerSite.getSiteName() + "_" + DateUtils.getFormattedDate("yyyyMMdd_HHmmss");
                    String outputFilename = null;
                    logger.info(" FTP properties for " + retailerSite.getSiteName() + " found!!! ");
                    Properties ftpProperties = new Properties();
                    try {
                        ftpProperties.load(new FileInputStream(ftpConfigFile));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        logger.error("Could Not Load FTP Properties", e1);

                    }
                    if (isFTPConfiguredCompletely(ftpProperties)) {
                        outputFilename = ftpProperties.getProperty("sftp.MerchantId")+ "_" + DateUtils.getFormattedDate("yyyyMMdd_HHmmss");
                    /*} else {
                        logger.error(" FTP details not configured completely for " + retailerSite.getSiteName() + ". Please configure correctly.");
                        //If no configuration if found setting it as "{siteid}_{sitename}_{timestamp}"
                        outputFilename = retailerSite.getId() + "_" + retailerSite.getSiteName() + "_" + DateUtils.getFormattedDate("yyyyMMdd_HHmmss");
                    }*/
                        File textFile = this.configurationService.getFileSystem().getFile(exportDir + File.separator + outputFilename + TXT_EXT, true, true);


                        textFile.createNewFile();
                        FileWriter fileWriter = new FileWriter(textFile);
                        writer = new CSVWriter(new BufferedWriter(fileWriter), '\t', CSVWriter.NO_QUOTE_CHARACTER);

                        Integer totalProducts = productRepository.getTotalProductCountForRetailerSite(retailerSite);
                        logger.info("Number of products to be exported :::  " + totalProducts);

                        int batchSize = 200;
                        int pageCount = Double.valueOf(Math.ceil(totalProducts / 200)).intValue();
                        logger.info(" Total no. of pages ::: " + pageCount);
                        long startTime = System.currentTimeMillis();
                        boolean processProductFlat = false;
                        for (int i = 0; i <= pageCount; i++) {
                            processProducts(writer, retailerSite, i, batchSize);
                            if (i <= pageCount) {
                                processProductFlat = true;
                            }
                        }
                        long endTime = System.currentTimeMillis();
                        logger.debug(" Time taken for feed exporting        " + (endTime - startTime) / 1000);


                        if (processProductFlat) {
                            //After the TEXT has been generated, make an entry into repository, which another
                            // JOB or Notification Event Listener can pick for FTP upload.

                            //String filePath = exportDir + File.separator + outputFilename + TXT_EXT;
                            String filePath = exportDir + outputFilename + TXT_EXT;

                            DataExportFile exportFile = new DataExportFile(this.retailerSiteRepository.get(retailerSite.getId()), filePath, 0);
                            exportFile.setCreatedDate(new Date());
                            exportFile.setModifiedDate(new Date());
                            this.dataExportFileRepository.insert(exportFile);
                        }
                        //Update the outputStatus for the DataFile
                        dataFilePending.setOutputStatus(FileStatus.DONE);
                        this.dataFileRepository.update(dataFilePending);
                    }else{
                        logger.error(" FTP details not configured completely for " + retailerSite.getSiteName() + ". Please configure correctly.");
                        logger.error(" Following are the mandatory properties :" +
                                "sftp.Hostname \n" +
                                "sftp.Username\n" +
                                "sftp.Password\n" +
                                "sftp.PortNumber\n" +
                                "sftp.MerchantId  ");
                        //If no configuration if found setting it as "{siteid}_{sitename}_{timestamp}"
                        //outputFilename = retailerSite.getId() + "_" + retailerSite.getSiteName() + "_" + DateUtils.getFormattedDate("yyyyMMdd_HHmmss");
                    }
                } else {
                    logger.info("No latest feed found for the " + retailerSite.getSiteName() + ". Therefore, data export will be skipped.");

                }//dataFilePending if
            } else {
                logger.error("    DataExport job will be skipped for RetailerSite " + retailerSite.getSiteName() + ". No configuration available.");
            }
        } catch (Exception e) {
            logger.error(" DataExportManager export error :    " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        }
    }


    public void processProducts(CSVWriter writer, RetailerSite retailerSite, int start, int batchSize) {
        int startCount = start;
        if (start != 0) {
            start = (batchSize * start) + 1;
        }

        logger.debug("Processing the batch ::: " + (start) + "-" + (start + batchSize));
        Collection<Product> products = this.productRepository.getProductForRetailerSite(retailerSite, start, batchSize);
        updateExportFeed(retailerSite, products, writer);
    }


    protected void updateExportFeed(RetailerSite retailerSite, Collection<Product> products, CSVWriter writer) {
        // 1 - Id	2- Group ID	3- Promotional Text	4 - Brand	5 - ManufacturerName	6 - MPN	7- UPC	8- ISBN
        // 9 - Title	10Keywords	11 - Short Description	12 - Long Description	13- Feature / Specs
        // 14 - Google Category	15 - Merchant Product Category	16 - Link	17 - Image Url	18- Buy Link
        // 19 - Additional Image URL	20 - Warranty	21 - Condition	22 - Expiration Date	23 - Availability
        // 24 - Discount	25 - Discount type	26 - Price	27 - Sale Price	28 - In Stock	29- Sale price effective dates

        List<String[]> result = new LinkedList<String[]>();

        for (Product product : products) {

            String[] row = new String[29];
            PropertiesProvider props = product.getProperties();
            String sku = (props != null) ? product.getSku() : null;
            if (StringUtils.isEmpty(sku)) {
                sku = props.getProperty("SKU_Name");
            }
            if (StringUtils.isEmpty(sku)) {
                sku = product.getProductId();
            }
            String[] categories = getGoogleAndMerchantCategories(product);
            String googleCategory = "";
            String merchantCategory = "";
            int i = categories.length;
            if (categories != null && i > 0) {

                try {
                    googleCategory = categories[0];
                    //merchantCategory = categories[1];
                } catch (ArrayIndexOutOfBoundsException aiEx) {
                    logger.debug(" Google or Merchant Category was missing. ");
                }
            }

            String manufacturerName = (props != null) ? props.getProperty("Mfr_Name") : null;
            if (StringUtils.isEmpty(manufacturerName)) {
                manufacturerName = retailerSite.getSiteName();
            }
            String mpn = (props != null) ? props.getProperty("webPartNumber") : null;
            if (StringUtils.isEmpty(mpn)) {
                mpn = retailerSite.getId() + "_" + product.getProductId();
            }
            /*String propCategory = (props != null) ? props.getProperty("Category") : null;
            if (!StringUtils.isEmpty(propCategory)) {
                merchantCategory = propCategory;
            }*/

            String categoryHierarchy = (props != null) ? props.getProperty("Category_Hierarchy") : null;
            if (!StringUtils.isEmpty(categoryHierarchy)) {
                googleCategory = categoryHierarchy;
            }

            //Reviewed by Andy, Setting googleCategory to merchantCategory.
            merchantCategory = googleCategory;

            //RetailerName
            String retailerName = product.getRetailer().getName();
            row[0] = sku; // Primarily maps to an "sku" column. If the SKU value is NULL or Empty data then need to map to "productId".
            row[1] = (props != null) ? props.getProperty("Group_ID") : " ";// GroupID -
            row[2] = product.getPromotional();
            row[3] = "";//Brand left empty
            row[4] = manufacturerName; //TODO: ManufacturerName -  Awaiting Info from the Storefront team. Will change when the Onboarding team has a request.
            row[5] = mpn; //TODO: MPN - Since this needs to be unique, we will have the composite_key = Retailer_SiteID+FicStar_ProductID
            row[6] = (props != null) ? props.getProperty("UPC_EAN") : "";  //UPC - Blank
            row[7] = "";  //ISBN - Blank
            row[8] = product.getTitle();  // Title - Maps to "title" column
            row[9] = (props != null) ? props.getProperty("Keywords") : "";   //Keyword - Blank
            row[10] = product.getDescription(); // Maps to "description" column  (Need to get confirmation from Storefront team)
            row[11] = product.getDescription();  //Maps to "description" column  (Need to get confirmation from Storefront team)
            row[12] = ""; //Feature / Specs - Blank
            row[13] = (googleCategory != null) ? googleCategory : "";//Generate a concatenated String from all the Category columns in the following manner - Category1 > Category2 > Category3 > CAtegory4 > Category5 > Category6
            row[14] = (merchantCategory != null) ? merchantCategory : "";//The highest / last category column which is NOT NULL. For a specific product we have depth till Category3, then we need to use Category3 value. For another products if we have Category6, then we need to use Category6 val.

            //Double encode Url for dell retailer products
            if (retailerName.equalsIgnoreCase("dell")) {
                String firstUrl = null;
                String secondUrl = null;
                try {
                    firstUrl = UrlUtils.encode(product.getUrl());
                    secondUrl = UrlUtils.encode(firstUrl);
                    row[15] = secondUrl; // URL
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    logger.error("Could Not Encode Product Url::", e);
                    row[15] = "";// URL
                }
            } else {
                row[15] = product.getUrl(); // URL
            }

            row[16] = getImageURL(product);// Construct Image URL like how we do for Campaign

            //Double encode BuyLink for dell retailer products
            if (retailerName.equalsIgnoreCase("dell")) {
                String firstBuyLink = null;
                String secondBuyLink = null;
                try {
                    firstBuyLink = UrlUtils.encode(product.getBuyLink());
                    secondBuyLink = UrlUtils.encode(firstBuyLink);
                    row[17] = secondBuyLink; //Buy link blank
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    logger.error("Could Not Encode Product BuyLink::", e);
                    row[17] = ""; //Buy link blank
                }

            } else {
                row[17] = product.getBuyLink(); //Buy link blank
            }
            row[18] = "";  //Additional Image URL - Blank
            row[19] = "";  //Warranty - Blank
            row[20] = ""; //Condition - Blank
            //Setting the expiration date 1 year from now
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.YEAR, 1); // add 1 Year
            date = cal.getTime();
            row[21] = DateUtils.toISO8601DateFormat(date);  //Expiration Date -  1 year from today

            Product.Availability.AVAILABLE_FOR_ORDER.getValue();


            Integer avaliablity = 0;
            String avaliablityString = "";
            if (product.getAvailability() != null) {
                avaliablity = product.getAvailability();
                if (Product.Availability.OUT_OF_STOCK.getValue() == avaliablity) {
                    avaliablityString = "out of stock";
                }
                if (Product.Availability.IN_STOCK.getValue() == avaliablity) {
                    avaliablityString = "in stock";
                }
                if (Product.Availability.AVAILABLE_FOR_ORDER.getValue() == avaliablity) {
                    avaliablityString = "available for order";
                }
                if (Product.Availability.PREORDER.getValue() == avaliablity) {
                    avaliablityString = "preorder";
                }

            }
            row[22] = avaliablityString;
            row[23] = ""; //Discount - Blank
            row[24] = ""; //Discount type - Blank
            //Use case 1: Price = 49.99 & ListPrice = 89.99
            //Use case 2: Price = 49.99 & ListPrice = 0
            //Use case 3: Price = 0 & ListPrice = 89.99


            Float s = null;

            Float dbPrice = (product.getPrice() == null) ? 0 : product.getPrice();  //49.99  || 49.99  || 0
            Float dbListPrice = (product.getListPrice() == null) ? 0 : product.getListPrice(); //89.99 || 0  || 89.99
            // Price: 1) Get ListPrice. 2) If ListPrice == 0 then use Price
            String price = "";
            String salePrice = "";
            if (dbListPrice == 0) {
                price = dbPrice.toString();
            } else {
                price = dbListPrice.toString();
            }
            row[25] = price; //89.99
            // Sale Price: 1) Get Price. 2)  If Price is < ListPrice, then use Price else use ListPrice if NOT 0 (ZERO)
            if (dbPrice != 0) {
                if (dbPrice < dbListPrice) {
                    salePrice = dbPrice.toString();
                } else {
                    if (dbListPrice == 0)
                        salePrice = dbPrice.toString();
                }
            } else {
                salePrice = dbListPrice.toString();
            }
            row[26] = salePrice;
            row[27] = (props != null) ? props.getProperty("Qty_In_Stock") : ""; // In Stock - Blank
            row[28] = "";//Sale price effective dates - Blank

            writer.writeNext(row);


        }
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Collection<DataExportFile> getFilesByStatus(Integer exportStatus) {
        return this.dataExportFileRepository.getFilesByStatus(exportStatus);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void updateExportedFileStatus(DataExportFile dataExportFile) {
        this.dataExportFileRepository.update(dataExportFile);
    }

    /**
     * Generates custom String formats for GoogleCategories and Merchant Category.
     * GoogleCategories: A concatenated String from all the Category columns in the following manner -
     * Category1 > Category2 > Category3 > Category4 > Category5 > Category6 >
     * Merchant Category: The highest / last category column which is NOT NULL. For a specific product we have
     * depth till Category3, then we need to use Category3 value. For another
     * products if we have Category6, then we need to use Category6 val.
     *
     * @param product Product
     * @return String[] position 0 - Google Categories. Position 1 - Merchant Category
     */
    protected String[] getGoogleAndMerchantCategories(Product product) {
        List<TaxonomyCategory> category = this.taxonomyCategoryRepository.getTree(product.getCategory());
        Iterator<TaxonomyCategory> itr = category.iterator();
        StringBuilder sb = new StringBuilder();
        String name = "";
        while (itr.hasNext()) {
            name = itr.next().getName();
            if (name.equalsIgnoreCase("ROOT")) {
                /**
                 * Andy's thought:
                 * Since there were "All Products" category already coming in few Retailer Feeds, it would be duplicated in marketvine production,
                 * hence we are removing the "All Products" category, so that the categories remain the same as we get from the feeds.
                 * The code is modifed to not add the ROOT/All Products category to the existing Category.
                 */
                //name = "All Products";
                continue;
            }
            sb.append(name);
            sb.append(" > ");
        }
        String googleCategory = sb.toString();

        String currentProductCategory = product.getCategory().getName();
        //logger.info("Google Category ::  "+googleCategory);
        if (!StringUtils.isEmpty(currentProductCategory)) {
            googleCategory = googleCategory + currentProductCategory;
        }
        //logger.info("Updated Google Category ::  "+googleCategory);
        return new String[]{googleCategory, currentProductCategory};

    }

    /**
     * Returns the first ImageSrcURL if there are multiple image URLs.
     *
     * @param product
     * @return String containing the ImageSrcURL from the product_image table
     */
    protected String getImageURL(Product product) {
        Collection<ProductImage> images = this.productImageRepository.getProductImages(product);
        String imagePath = "";

        if (images != null && images.size() > 0) {

            for (ProductImage image : images) {
                //imagePath = image.getImageURL();
                imagePath = image.getSrcImageURL();
                if (!StringUtils.isEmpty(imagePath)) {
                    break;
                }
            }
        }
        //Onboarding team needs the original Image Source URL. NOT the content server CDN URL
        /*if( !StringUtils.isEmpty(imagePath) ){
            logger.info(" Image path :: "+imagePath);
             imagePath = cdnPrefix + imagePath;
        }*/
        return imagePath;
    }

    public Boolean isExportingFiles() {
        return exportingFiles.get();
    }

    @Override
    public DataFile getFilePendingForExport(RetailerSite retailerSite) {
        DataFile dataFile = dataFileRepository.getPendingFileForExport(retailerSite);

        if (dataFile != null) {
            dataFile = dataFileRepository.acquireLock(dataFile, "outputStatus", FileStatus.IN_QUEUE, FileStatus.PROCESSING);
            // acquire lock.
            if (dataFile == null) {
                logger.info("Unable to lock object from IN_QUEUE TO PROCESSING  objID:=" + dataFile);
                return null;
            }
            return dataFile;
        }
        return dataFile;
    }

    @Override
    public void processFileForExport(final DataFile dataFile) {
        if (!dataFile.getStatus().equals(FileStatus.PROCESSING)) {
            logger.warn("dataFile was not locked for export processing");
            return;
        }

        dataFileRepository.refresh(dataFile);

        Integer finalFileStatus = FileStatus.DONE;
        try {
            FileSystem fileSystem = this.configurationService.getFileSystem();
            File exportConfigDir = fileSystem.getDirectory(FileSystemUtil.getPath(dataFile.getRetailerSite(), "config/retailers") + "/exports/", true);
            String[] schemaFileNames = exportConfigDir.list(new FilenameFilter() {
                @Override
                public boolean accept(final File dir, final String name) {
                    return name.endsWith("_export_config.xml");
                }
            });
            File outputDirectory = fileSystem.getDirectory(FileSystemUtil.getPath(dataFile.getRetailerSite(), "data_export"), true);
            for (String schemaFileName : schemaFileNames) {
                try {
                    File schemaFile = fileSystem.getFile(exportConfigDir, schemaFileName);
                    String outputFilename = StringUtils.getSimpleString(schemaFileName.substring(0, schemaFileName.indexOf("_export_config.xml"))) + "-" + FilenameUtils.getBaseName(dataFile.getFilePath());

                    File outputFile = fileSystem.getFile(outputDirectory, outputFilename);
                    DataImportConfig dataImportConfig = applicationContext.getBean("dataImportConfig", DataImportConfig.class);
                    dataImportConfig.setConfigFilePath(schemaFile.getAbsolutePath());
                    dataImportConfig.afterPropertiesSet();
                    // now the config is ready.

                    Schema schema = dataImportConfig.getSchema();
                    TableDefinition tableDefinition = schema.getDefinitionBySource(dataFile.getImportType());

                    Assert.notNull(tableDefinition, "cannot find tableDefinition for :="
                            + dataFile.getImportType() + " in schema :=" + schemaFile.getAbsolutePath());

                    tableDefinition.setProperty("outputPath", outputFile.getAbsolutePath());
                    tableDefinition.setProperty("isPathAbsolute", "true");


                    DataReader dataReader = applicationContext.getBean("hibernateDataReader", DataReader.class);
                    DataWriter dataWriter = applicationContext.getBean("csvDataWriter", DataWriter.class);
                    DataExceptionHandler exceptionHandler = null;
                    if (applicationContext.containsBean(tableDefinition.getDestinationTable() + "DataExceptionHandler")) {
                        exceptionHandler = (DataExceptionHandler) applicationContext.getBean(tableDefinition.getSourceTable()
                                + "DataExceptionHandler");
                        dataReader.setExceptionHandler(exceptionHandler);
                        dataWriter.setExceptionHandler(exceptionHandler);
                    }

                    dataReader.setTableDefinition(tableDefinition);
                    dataWriter.setTableDefinition(tableDefinition);

                    logger.info("Exporting data from database to " + dataFile.getFilePath());
                    GenericDataImportService dataImportService = applicationContext.getBean("genericDataImportService",
                            GenericDataImportService.class);
                    dataImportService.setDataImportConfig(dataImportConfig);
                    dataImportService.setDataReader(dataReader);
                    dataImportService.setDataWriter(dataWriter);
                    dataReader.initialize();
                    dataWriter.initialize();
                    dataImportService.run();
                } catch (RuntimeException e) {
                    if (e.getCause() != null) {
                        Class errorClass = e.getCause().getClass();
                        if (errorClass.equals(FileNotFoundException.class) || errorClass.equals(IOException.class)) {
                            finalFileStatus = DataImportManager.FileStatus.ERROR_READ;
                            logger.error(e.getMessage());
                        } else {
                            logger.error(e.getMessage(), e);
                        }
                    } else {
                        logger.error(e.getMessage(), e);
                    }
                } catch (IOException e) {
                    finalFileStatus = DataImportManager.FileStatus.ERROR_READ;
                    logger.error(e.getMessage(), e);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    finalFileStatus = DataImportManager.FileStatus.ERROR_PARSING;
                } finally {
                    dataFileRepository.acquireLock(dataFile, DataImportManager.FileStatus.PROCESSING, finalFileStatus);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            finalFileStatus = DataImportManager.FileStatus.ERROR_PARSING;
        } finally {
            dataFileRepository.acquireLock(dataFile, DataImportManager.FileStatus.PROCESSING, finalFileStatus);
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

    private volatile AtomicBoolean exportingFiles = new AtomicBoolean(false);

    @Autowired
    private RetailerSiteRepository retailerSiteRepository;

    @Autowired
    private DataExportFileRepository dataExportFileRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private DataFileRepository dataFileRepository;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TaxonomyCategoryRepository taxonomyCategoryRepository;

    @Autowired
    private ApplicationContext applicationContext;

    public void setDataExportFileRepository(DataExportFileRepository dataExportFileRepository) {
        this.dataExportFileRepository = dataExportFileRepository;
    }

    public void setProductImageRepository(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    public void setTaxonomyCategoryRepository(TaxonomyCategoryRepository taxonomyCategoryRepository) {
        this.taxonomyCategoryRepository = taxonomyCategoryRepository;
    }

    public void setProductRepository(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public void setDataFileRepository(final DataFileRepository dataFileRepository) {
        this.dataFileRepository = dataFileRepository;
    }

    public void setExportingFiles(final AtomicBoolean exportingFiles) {
        this.exportingFiles = exportingFiles;
    }


    public void setRetailerSiteRepository(RetailerSiteRepository retailerSiteRepository) {
        this.retailerSiteRepository = retailerSiteRepository;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
