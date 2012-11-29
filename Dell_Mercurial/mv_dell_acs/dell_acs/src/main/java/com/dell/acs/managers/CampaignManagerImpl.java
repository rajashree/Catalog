/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import au.com.bytecode.opencsv.CSVReader;
import com.dell.acs.*;
import com.dell.acs.managers.model.CampaignItemData;
import com.dell.acs.persistence.domain.*;
import com.dell.acs.persistence.repository.*;
import com.google.common.base.Joiner;
import com.sourcen.core.util.FileSystem;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.collections.PropertiesProvider;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.io.*;
import java.util.*;

/**
 * @author Samee K.S
 */

@Service
public class CampaignManagerImpl implements CampaignManager {

    private static final Logger log = Logger.getLogger(CampaignManagerImpl.class);

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CampaignItemRepository campaignItemRepository;

    @Autowired
    private CampaignCategoryRepository campaignCategoryRepository;

    @Autowired
    private RetailerSiteRepository retailerSiteRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductManager productManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false,
            rollbackFor = CampaignAlreadyExistsException.class)
    public Campaign saveCampaign(Campaign campaign) throws CampaignAlreadyExistsException {
        try{
            if (campaign.getId() != null) {
                campaign.setModifiedDate(new Date());
                campaignRepository.update(campaign);
                log.debug("Updated campaign - " + campaign.getId() + " :: " + campaign.getName());
            } else {
                campaign.setCreationDate(new Date());
                campaign.setModifiedDate(new Date());
                campaignRepository.insert(campaign);
                log.debug("Saved new campaign - " + campaign.getName());
            }
        }catch (Exception ex){
            if(ex  instanceof ConstraintViolationException){
                log.debug("Failed to create the campaign. Campaign by this name already exists.");
                throw new EntityExistsException("Campaign by this name already associated to the retailer site.");
            }
        }
        return campaign;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Campaign getCampaign(Long campaignID) throws CampaignNotFoundException {
        // Need this method because am populating the products list in this method.
        // repository will not help me to get the products since its based on the itemType
        // so have to fetch it explicitly
        return campaignRepository.getCampaign(campaignID);
    }


    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Campaign getCampaign(Long campaignID, Boolean loadReferences) throws CampaignNotFoundException {
        if (loadReferences) {
            return getCampaign(campaignID);
        } else {
            return campaignRepository.get(campaignID);
        }
    }

    @Override
    @Transactional(rollbackFor = CampaignNotFoundException.class)
    public void deleteCampaign(Long campaignID) throws CampaignNotFoundException {
        campaignRepository.remove(campaignID);//campaignRepository.get(campaignID));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Campaign> getCampaigns() {
        return campaignRepository.getCampaigns();
    }


    @Override
    @Transactional(readOnly = true)
    public List<Campaign> getCampaignByRetailerSiteID(Long retailerSiteID) {
        return campaignRepository.getCampaignsBySite(retailerSiteID);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Campaign> getCampaignsByRetailerSite(Collection<Long> retailerSiteIDList) {
        List<Campaign> campaigns = new LinkedList<Campaign>();
        for (Long retailerSite : retailerSiteIDList) {
            if (retailerSite != null) {
                campaigns.addAll(campaignRepository.getCampaignsBySite(retailerSite));
            }
        }
        return campaigns;
    }

    @Override
    @Transactional(readOnly = true)
    public CampaignItem getCampaignItem(Long campaignItemID) throws CampaignItemNotFoundException {
        CampaignItem item = campaignItemRepository.get(campaignItemID);
        if (item == null) {
            throw new CampaignItemNotFoundException("Unable to load the campaignItem - " + campaignItemID);
        }
        if (item.getItemType().getValue().equalsIgnoreCase(CampaignItem.Type.PRODUCT.getValue())) {
            Product prod = item.getProduct();
            if (prod != null) {
                item.setProduct(prod);
            } else {
                //checking if product is null and disabled
                try {
                    prod = productManager.getProduct(item.getItemID());
                } catch (Exception e) {
                    e.printStackTrace();
                    prod = null;
                }
                item.setProduct(prod);
            }
        }
        return item;
    }

    @Override
    @Transactional(readOnly = true)
    public CampaignItem getCampaignItem(Long campaignItemID, Boolean loadReferences) throws
            CampaignItemNotFoundException {
        if (loadReferences) {
            return getCampaignItem(campaignItemID);
        } else {
            return campaignItemRepository.get(campaignItemID);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<CampaignItemData> getCampaignItems(Long campaignID) {
        Collection<CampaignItemData> itemBeans = new ArrayList<CampaignItemData>();
        String[] ignoreProps = {"itemType", "category", "properties"};

        for (CampaignItem item : campaignItemRepository.getItems(campaignID)) {
            CampaignItemData bean = new CampaignItemData();
            BeanUtils.copyProperties(item, bean, ignoreProps);
            bean.setItemType(item.getItemType().getValue());
            // Load the reference entity ( Document, Product and Event ) related data
            bean = loadCampaignItemReference(bean, item);
            if(bean != null){
                itemBeans.add(bean);
            }else{
                log.debug("Reference entity item NOT FOUND or Title is empty. So excluding the item - " + item.getId());
            }
        }
        return itemBeans;
    }

    // Helper method to to update the CampaignItem - ( Product, Event or Document entity associated to Campaign)
    private CampaignItemData loadCampaignItemReference(CampaignItemData bean, CampaignItem item){
        PropertiesProvider itemProps = null;
        if (bean.getItemType().equalsIgnoreCase(CampaignItem.Type.PRODUCT.getValue())) {
            Product prod = item.getProduct();
            if (prod != null) {
                itemProps = item.getProperties();
                if (itemProps.hasProperty(CAMPAIGN_ITEM_CLONED_PROP)) {
                    bean.getProperties().put("cloned", itemProps.getBooleanProperty(CAMPAIGN_ITEM_CLONED_PROP));
                }
                if (StringUtils.isNotEmpty(prod.getTitle())) {
                    bean.setItemName(prod.getTitle());
                } else {
                    log.debug("ERROR: PRODUCT has INVALID TITLE id - " + item.getItemID());
                    return null;
                }
                bean.setPrice(prod.getPrice());
                bean.setListPrice(prod.getListPrice());

                if (itemProps.hasProperty(CampaignManager.CAMPAIGN_ITEM_REVIEWS_PROPERTY)) {
                    bean.getProperties()
                            .put("reviews", itemProps.getProperty(CampaignManager.CAMPAIGN_ITEM_REVIEWS_PROPERTY));
                }
                bean.setStars(prod.getStars());
                bean.setReviews(prod.getReviews());
            }else{
                log.debug("ERROR: PRODUCT NOT FOUND id - " + item.getItemID());
                return null;
            }
        }else if (bean.getItemType().equalsIgnoreCase(CampaignItem.Type.EVENT.getValue())) {
            Event event = item.getEvent();
            if (event != null) {
                itemProps = item.getEvent().getProperties();
                if (StringUtils.isNotEmpty(event.getName())) {
                    bean.setItemName(event.getName());
                } else {
                    log.debug("ERROR: EVENT has INVALID TITLE id - " + item.getItemID());
                    return null;
                }

                bean.setLocation(event.getLocation());

                if (itemProps.hasProperty(EventManager.EVENT_STARS_PROPERTIES)) {
                    bean.setStars(Float.valueOf(itemProps.getProperty(EventManager.EVENT_STARS_PROPERTIES)));
                } else {
                    bean.setStars(0F);
                }

                if (itemProps.hasProperty(EventManager.EVENT_RATINGS_PROPERTIES)) {
                    bean.getProperties()
                            .put("ratings", itemProps.getProperty(EventManager.EVENT_RATINGS_PROPERTIES));
                }

                if (itemProps.hasProperty(EventManager.EVENT_LIST_PRICE_PROPERTIES)) {
                    bean.setListPrice(
                            Float.valueOf(itemProps.getProperty(EventManager.EVENT_LIST_PRICE_PROPERTIES)));
                } else {
                    bean.setListPrice(0F);
                }

                if (itemProps.hasProperty(EventManager.EVENT_PRICE_PROPERTIES)) {
                    bean.setPrice(Float.valueOf(itemProps.getProperty(EventManager.EVENT_PRICE_PROPERTIES)));
                } else {
                    bean.setPrice(0F);
                }
                bean.setReviews(0);
            } else{
                log.debug("ERROR: EVENT NOT FOUND id - " + item.getItemID());
                return null;
            }
        }else if (bean.getItemType().equalsIgnoreCase(CampaignItem.Type.DOCUMENT.getValue())) {
            Document document = item.getDocument();
            if (document != null) {
                if(StringUtils.isEmpty(document.getName())){
                    log.debug("ERROR: DOCUMENT has INVALID TITLE id - " + item.getItemID());
                    return null;
                }
                bean.setItemName(document.getName());
                bean.getProperties().put("startDate", document.getStartDate());
                bean.getProperties().put("endDate", document.getEndDate());
                bean.getProperties().put("document", document.getDocument());
                bean.getProperties().put("docName", FilenameUtils.getName(document.getDocument()));
            }else{
                log.debug("ERROR: DOCUMENT NOT FOUND id - " + item.getItemID());
                return null;
            }
        }else if (bean.getItemType().equalsIgnoreCase(CampaignItem.Type.IMAGE.getValue())) {
            Document document = item.getDocument();
            if (document != null) {
                if(StringUtils.isEmpty(document.getName())){
                    log.debug("ERROR: DOCUMENT has INVALID TITLE id - " + item.getItemID());
                    return null;
                }
                bean.setItemName(document.getName());
                bean.setUrl(document.getUrl());
                bean.setDesc(document.getDescription());

            }else{
                log.debug("ERROR: IMAGE NOT FOUND id - " + item.getItemID());
                return null;
            }
        }else if (bean.getItemType().equalsIgnoreCase(CampaignItem.Type.VIDEO.getValue())) {
            Document document = item.getDocument();
            if (document != null) {
                if(StringUtils.isEmpty(document.getName())){
                    log.debug("ERROR: DOCUMENT has INVALID TITLE id - " + item.getItemID());
                    return null;
                }
                bean.setItemName(document.getName());
                bean.setImage(document.getImage());
                bean.setUrl(document.getUrl());
                bean.setDesc(document.getDescription());
            }else{
                log.debug("ERROR: VIDEO NOT FOUND id - " + item.getItemID());
                return null;
            }
        }else if (bean.getItemType().equalsIgnoreCase(CampaignItem.Type.LINK.getValue())) {
            Document document = item.getDocument();
            if (document != null) {
                if(StringUtils.isEmpty(document.getName())){
                    log.debug("ERROR: DOCUMENT has INVALID TITLE id - " + item.getItemID());
                    return null;
                }
                bean.setItemName(document.getName());
                bean.setUrl(document.getUrl());
                bean.setDesc(document.getDescription());
            }else{
                log.debug("ERROR: DOCUMENT NOT FOUND id - " + item.getItemID());
                return null;
            }
        }
        return bean;
    }


    @Override
    @Transactional(readOnly = false)
    public void deleteCampaignItem(Long itemID, Long campaignID) throws CampaignItemNotFoundException {
        // Remove the item association from category
        CampaignItem item = campaignItemRepository.get(itemID);
        Set<CampaignCategory> campaignCategories = campaignRepository.get(campaignID).getCategories();
        for (CampaignCategory category : campaignCategories) {
            if (category.getItems().contains(item)) {
                category.getItems().remove(item);
                campaignCategoryRepository.update(category);
            }
        }
        campaignItemRepository.remove(itemID);
        // Fix: EXTERNALINTERACTIVEADS-394. Whenever an item (product) is deleted we check if its a cloned item
        // if its a cloned item then we set the product as enabled = false so that it doesn't appear in search results
        // in all other services like Product service, Merchant service and Product services.
        if (item.getProperties().hasProperty(CAMPAIGN_ITEM_CLONED_PROP)
                && item.getProperties().getBooleanProperty(CAMPAIGN_ITEM_CLONED_PROP)) {
            // For cloned product set disabled = true so that its not searchable in the product search
            //checking if production is null or disabled
            Product prod = null;
            try {
                prod = productManager.getProduct(item.getItemID());
                prod.setEnabled(false);
                productManager.update(prod);
            } catch (Exception e) {
                log.info("Unable to load the product " + item.getItemID() + " for campaign " + campaignID);
                e.printStackTrace();
                prod = null;
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CampaignCategory getCategory(Long id) { //String name, Long pid){
        return campaignCategoryRepository.get(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getMaxCategoryPosition(Long id, Long campaignID) throws CampaignCategoryNotFoundException {
        return campaignCategoryRepository.getMaxCategoryPosition(id, campaignID);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getMaxItemPriority(Long campaignID) throws CampaignNotFoundException {
        return campaignItemRepository.getMaxItemPriority(campaignID);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Integer adjustItemPriorityBasedOnPosition(Long categoryID, Long referenceItemID, String position) {
        return campaignItemRepository.adjustItemPriorityBasedOnPosition(categoryID, referenceItemID, position);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = DuplicateKeyException.class)
    public CampaignCategory saveCategory(CampaignCategory category) { //String name, Long pid){
        if (category.getId() != null) {
            campaignCategoryRepository.update(category);
        } else {
            campaignCategoryRepository.insert(category);
        }
        return category;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CampaignItem saveCampaignItem(CampaignItem item) { //String name, Long pid){
        try {
            if (item.getId() != null) {
                item.setModifiedDate(new Date());
                campaignItemRepository.update(item);
            } else {
                item.setCreationDate(new Date());
                item.setModifiedDate(new Date());
                campaignItemRepository.insert(item);
            }
        } catch (DataIntegrityViolationException dex) {

        }
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteCategory(Long id) {
        campaignCategoryRepository.remove(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public File downloadCampaignItems(Long cid) {
        return generateCSVFileObject(campaignItemRepository.getItems(cid), cid);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateCampaignItemLinks(Long campaignID, MultipartFile uploadFile, String pixelTrackingType)
            throws CampaignItemNotFoundException, CampaignNotFoundException, IOException, PixelTrackingException {
        Map<String, Map<Long, String>> parsedDataMap = parseUploadedCsvFileToMap(uploadFile.getInputStream(),
                campaignID, pixelTrackingType);
        campaignItemRepository.updateCampaignItemProperties(campaignID, parsedDataMap);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Collection<Campaign> getCampaignsBySiteandType(Long retailerSiteID, String campaignType) {
        return campaignRepository.getCampaignsBySiteandType(retailerSiteID, campaignType);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Campaign> getCampaignsBySite(Long retailerSiteID) {
        return campaignRepository.getCampaignsBySite(retailerSiteID);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Campaign> getActiveCampaignsBySiteName(String retailerSiteName, String campaignType)
    							throws CampaignForInactiveRetailerException {
        return campaignRepository.getActiveCampaignsBySiteName(retailerSiteName, campaignType);
    }


    @Override
    @Transactional
    public void updateCampaign(Campaign campaign) {
        campaign.setModifiedDate(new Date());
        campaignRepository.update(campaign);
    }

    @Override
    @Transactional
    public void updateCampaignItem(CampaignItem campaignItem) {
        campaignItem.setModifiedDate(new Date());
        campaignItemRepository.update(campaignItem);
    }

    @Override
    @Transactional
    public void updateCampaignCategory(CampaignCategory campaignCategory) {
        campaignCategoryRepository.update(campaignCategory);
    }

    @Override
    public String getBaseCDNPathForCampaign(Campaign campaign) throws CampaignNotFoundException, IOException {
        RetailerSite rs = retailerSiteRepository.get(campaign.getRetailerSite().getId());
        Retailer r = rs.getRetailer();
        String campaignCDNBasePath = StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR + "cdn"
                + StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR + r.getId()
                + "_" + r.getName()
                + StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR + rs.getId() + "_" + rs.getSiteName()
                + StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR + "campaign"
                + StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR + campaign.getId()
                + StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR;
        return campaignCDNBasePath;
    }

    // Uploaded file columns - cid, id, product_id, info_url, lt_url, Link_tracker_campagin_ID
    private Map<String, Map<Long, String>> parseUploadedCsvFileToMap(InputStream csvFileIS, Long campaignID, String pixelTrackingType)
            throws FileNotFoundException, UnsupportedEncodingException, PixelTrackingException {
        // <campaignItemID>, [LT_CID, LT_URL]

        // MAP<(LT_CID>), Map(<CI_ID>, <LT_URL>)
        Map<String, Map<Long, String>> uploadLinkTrackerData = new HashMap<String, Map<Long, String>>();

        // [lt_cid, [itemID, LT_URL]]

        Map<Long, List<String>> uploadedLinkTrackerInfo = new HashMap<Long, List<String>>();
        CSVReader reader = null;
        // Read headers
        char separator = ',';
        char quotechar = '\'';
        String[] headers = new String[0];
        try {
            reader = new CSVReader(new InputStreamReader(csvFileIS, "UTF-8"), separator, quotechar);
            headers = reader.readNext();
        } catch (IOException e) {

        }
        for (int i = 0; i < headers.length; i++) {
            headers[i] = StringUtils.getSimpleString(headers[i].trim()).intern();
        }
        //String row[] = null;
        int i = 0;
        Integer rowIndex = 1; // skip reading the CSV headers
        int headersCount = 7; // Its always fixed
        /*try {
            row = reader.readNext();
        } catch (IOException e) {
        }*/
        // ROW - campaign_id - 0 , campaign_item_id - 1, product_id - 2, fic_product_id - 3  ,info_url - 4, lt_campagin_id - 5, lt_url - 6
        //Note: We do not have any use on the Fic_ProdID - 3. So we do not need to parse it.
        Long campaign_id, campaign_item_id, product_id, ca;
        String info_url = "", lt_url = "", lt_campagin_id;
        try {
            List<String[]> allCampaignItems = reader.readAll();

            for (String[] csvRow : allCampaignItems) {
                campaign_id = (StringUtils.isNotEmpty(csvRow[0])) ? Long.parseLong(csvRow[0].trim()) : -1L;
                campaign_item_id = (StringUtils.isNotEmpty(csvRow[1])) ? Long.parseLong(csvRow[1].trim()) : -1L;
                product_id = (StringUtils.isNotEmpty(csvRow[2])) ? Long.parseLong(csvRow[2].trim()) : -1L;

                // Validations
                // Check for invalid campaignID
                if (campaign_id != campaignID.longValue()) {
                    log.debug("Invalid campaign data found. So skipping the record - " + rowIndex++);
                    throw new PixelTrackingException("Invalid CampaignID found. Trying to update wrong campaign.");
                }
                // Check for invalid campaignItemID
                else if (campaign_item_id <= 0 || campaign_item_id == null) {
                    log.debug("Invalid campaignItem id data found. So skipping the record - " + rowIndex++);
                }
                // Check for invalid productID
                else if (product_id <= 0 || product_id == null) {
                    log.debug("Invalid Product id found. So skipping the record - " + rowIndex++);
                } else {
                    info_url = (StringUtils.isNotEmpty(csvRow[4])) ? csvRow[4].trim() : "";
                    // Link tracker related info
                    lt_campagin_id = (StringUtils.isNotEmpty(csvRow[5])) ? csvRow[5].trim() : "";
                    lt_url = (StringUtils.isNotEmpty(csvRow[6])) ? csvRow[6].trim() : "";
                    log.debug(" lt_campagin_id :::  " + lt_campagin_id);
                    log.debug(" lt_url :::  " + lt_url);
                    //Check the pixel-tracker URL format
                    validatePixelTrackingURL(lt_url, pixelTrackingType);
                    // infoMap - campaignItemID, LT_URL
                    Map infoMap = new HashMap();
                    infoMap.put(campaign_item_id, lt_url);
                    // uploadLinkTrackerData - LT_CID, infoMap
                    //Vivek - Changing the key from LT_CID to Item_ID + LT_CID. Need to do this because an item can have multiple LT_CID .
                    uploadLinkTrackerData.put(campaign_item_id + "." + lt_campagin_id, infoMap);
                    log.debug("Updated the map - " + uploadedLinkTrackerInfo.size() + " Record - " + rowIndex);

                }


            }
        } catch (IOException e) {
            log.error("Unable to read the uploaded csv file.", e);
        }
        return uploadLinkTrackerData;
    }

    /**
     * Validation for cross updating of LinkTracker with MarketVine pixels.
     *
     * @param url
     * @param pixelTrackerType
     * @throws PixelTrackingException
     */
    protected void validatePixelTrackingURL(String url, String pixelTrackerType) throws PixelTrackingException {
        if (pixelTrackerType.equalsIgnoreCase("marketvine")) {
            if (url.contains("DURL=")) {
                throw new PixelTrackingException("DURL-Found in MarketVine pixel.");

            }
            if (!url.contains("store.marketvine.com")) {
                throw new PixelTrackingException("Incorrect-MV pixel.");
            }
        } else if (pixelTrackerType.equalsIgnoreCase("linktracker")) {
            if (!url.contains("DURL=")) {
                throw new PixelTrackingException("DURL-NotFound in LinkTracker pixel.");
            }
        }
    }

    // Helper method to generate the campaign items csv file
    private File generateCSVFileObject(List<CampaignItem> items, Long campaignID) {
        BufferedWriter bw = null;
        File file = null;
        try {
            // <campaign_cdn_path>/download/download_campaign_items.csv
            String filePath = getBaseCDNPathForCampaign(getCampaign(campaignID))
                    .concat("download").concat(StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR)
                    .concat("download_campaign_items.csv");
            // If exists delete the old and create a new file.
            File tmp = FileSystem.defaultFileSystem.getFile(filePath, false, false);
            if (tmp != null) {
                if (tmp.exists()) {
                    // Delete the old file
                    if(tmp.delete()){
                        log.info("Old generated csv file was deleted successfully !");
                    }else{
                        log.info("Unable to delete the old generated csv file was deleted successfully !");
                    }
                }
            }
            boolean canRead = true;
            // Create a new file ( if exists - its deleted before creating a new )
            file = FileSystem.defaultFileSystem.getFile(filePath, true, true);
            canRead = file.canRead();
            if (canRead) {
                String csvRowFormat = "%s, %s, %s, %s, %s, %s, %s";
                file = FileSystem.defaultFileSystem.getFile(filePath, true, true);
                bw = new BufferedWriter(new FileWriter(file, true));
                // Write the header csv header -  cid	 product_id	 info_url	 lt_url
                // campaign_id, campaign_item_id, product_id, info_url, lt_campagin_id, lt_url
                bw.write(Joiner.on(",")
                        .join(new String[]{"campaign_id", "campaign_item_id", "product_id", "fic_product_id",
                                "info_url", "lt_campagin_id", "lt_url"}));
                bw.newLine();
                // Iterate ove the list and collect data to write into a csv file
                int recordCounter = 0;
                for (CampaignItem item : items) {
                    String infoLink = "", type = "";
                    //Object[] arr = (Object[]) items.get(i);
                    Long id = item.getId();
                    Long itemID = item.getItemID();
                    type = item.getItemType().name();
                    if (type.equalsIgnoreCase(CampaignItem.Type.PRODUCT.getValue())) {
                        Product prod = productRepository.get(itemID);
                        // Order of info_link fetch - info, review, buy
                        if (StringUtils.isNotEmpty(prod.getUrl())) {
                            infoLink = prod.getUrl();
                        } else if (StringUtils.isNotEmpty(prod.getReviewsLink())) {
                            infoLink = prod.getReviewsLink();
                        } else if (StringUtils.isNotEmpty(prod.getBuyLink())) {
                            infoLink = prod.getBuyLink();
                        }
                        // csv record for a product
                        // prodRecords.add(Joiner.on(",").join(new String[]{cid.toString(), prod.getProductId(), infoLink, ""}));
                        // String record = Joiner.on(",").join(new String[]{campaignID.toString(), prod.getProductId(), , ""});
                        String record =
                                String.format(csvRowFormat, campaignID.toString(), id, itemID, prod.getProductId(),
                                        infoLink, "", "");
                        log.debug("Processing - " + (recordCounter++) + "\n" + record);
                        bw.write(record);
                        bw.newLine();
                    } else if (type.equalsIgnoreCase(CampaignItem.Type.VIDEO.getValue())) {
                        // NOT SUPPORTED
                    } else if (type.equalsIgnoreCase(CampaignItem.Type.DOCUMENT.getValue())) {
                        // NOT SUPPORTED
                    }
                    // reset the data
                    type = "";
                    infoLink = "";
                }
                bw.close();
            }
        } catch (Exception e) {
            log.info("Error while write the data to csv file creating", e);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                log.info("Error while closing the buffered write instance - ", e);
            }
        }
        if (file != null) {
            log.info("File is downloaded @ - " + file.getAbsolutePath());
        }
        return file;
    }

    //Getter to return Campaign Item's Curated Reviews
    @Transactional(readOnly = true)
    public Collection<ProductReview> getCampaignItemReviews(CampaignItem campaignItem) throws
            ProductReviewNotFoundException {
        List<ProductReview> productReviews = new ArrayList<ProductReview>();
        if (campaignItem.getProperties().hasProperty(CampaignManager.CAMPAIGN_ITEM_REVIEWS_PROPERTY)) {
            String reviewProperty =
                    campaignItem.getProperties().getProperty(CampaignManager.CAMPAIGN_ITEM_REVIEWS_PROPERTY);
            List<Long> reviewIds = StringUtils.asLongList(reviewProperty);
            for (Long reviewId : reviewIds) {
                ProductReview productReview = productManager.getProductReview(reviewId);
                productReviews.add(productReview);
            }
        }
        return productReviews;
    }

    @Override
    public boolean checkDocumentAssociatedToCampaign(final Long itemID) {
        if (campaignRepository.checkCampaignForDoc(itemID)) {
            return true;
        }
        return false;
    }

    @Override
    public List<CampaignItem> getCampaignsDetailsByDocumentId(final Long documentID) {
        List<CampaignItem> associatedCampaignDetailsList = campaignRepository.getDocumentAssociatedCampaigns(documentID);
        if (associatedCampaignDetailsList.size() > 0) {
            log.info("Associated Campaign Detail List==>"+associatedCampaignDetailsList+" Found");
            return associatedCampaignDetailsList;
        } else {
            log.debug("No any campaign associated with document");
        }
        return associatedCampaignDetailsList;
    }

    @Override
    @Transactional(readOnly = true)
    public Campaign getCampaignForService(Long campaignID) throws EntityNotFoundException {
        Campaign campaign = this.campaignRepository.get(campaignID);
        if(campaign == null){
            throw new EntityNotFoundException("Requested campaign - "+campaignID+" does not exist.");
        }
        return campaign;
    }
}