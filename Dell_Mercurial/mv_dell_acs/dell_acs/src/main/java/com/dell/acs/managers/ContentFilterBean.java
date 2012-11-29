/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by IntelliJ IDEA. User: Chethan Date: 3/1/12 Time: 1:11 PM To change this template use File | Settings | File
 * Templates.
 */

public class ContentFilterBean {

    /**
     * Location:Body, Content:Zip or regional code based on IP, Requried: false.
     */
    private String zipcode;

    /**
     * Location:Body, Content:Maximum number of product per category to send, Required: true.
     */
    private int maxProducts;

    /**
     * Location:Body, Content:Number of sub-category levels (including top level category, so minimum value is 1),
     * Requried: true.
     */

    private int categoryDepth;

    /**
     * Location:Body, Content:Comma-separated values for number of categories at each category level.1, Requried: true.
     */
    private String maxCategories;

    /**
     * Location:Body, Content:Maximum number of products to include in <AlsoViewed> or <AlsoBought> nodes, Requried:
     * true.
     */
    private int maxRelated;

    /**
     * Location:Body, Content:A numeric value indicating the advertiser, Requried: true.
     */
    private long advertiserID;

    /**
     * Location:Body, Content:A numeric value indicating the category of the ad, Requried: true.
     */
    private String adCategoryID;

    /**
     * Location:Body, Content:If available, the referring site’s URL, Requried: false.
     */
    private String referralSite;

    /**
     * Location:Body, Content:If available, search terms used to find the current site, Requried: false.
     */

    private String searchTerms;

    /**
     * Location:Body, Content:A value in the form of “Width x Height” indicating the screen size, Requried: false.
     */

    private String resolution;

    /**
     * Location:Body, Content:The user ID to cross-reference with data warehouse, Requried: false.
     */

    private long userCookieID;

    /**
     * Location:Body, Content:Comma separated list of the last viewed products, Requried: false.
     */
    private String productIDs;

    /**
     * Location:Body, Content:Comma separated list of the last viewed categories, Requried: false.
     */

    private String productCategories;

    /**
     *
     */
    //Location:Body, Content:If available, the gender
    // from the current FB session, Requried: false.
    private String gender;

    /**
     * Location:Body, Content:If available, the birthday from the current FB session, Requried: false.
     */

    private Date birthday;

    /**
     * Location:Body, Content:0 (Default) - Products, Requried: false.
     */

    private int contentType;

    /**
     * Location:Body, Content:1 (Default) send recommended products; 0 – use Product_IDs as a specific list of IDs to
     * return, Requried: false.
     */

    private int recommend;

    /**
     * Location:Header, Content:Requesting machine’s IP address, Requried: false.
     */

    private String ipAddress;

    /**
     * Location:Header, Content:The browser type, Requried: false.
     */

    private String browser;

    /**
     * Location:Header, Content:Requesting OS, Requried: false.
     */

    private String operatingSystem;

    /**
     * Product Title.
     */
    private String productTitle;

    private String orderBy;

    /**
     * Default Constructor.
     */

    @Autowired
    public ContentFilterBean() {
    }

    /**
     * Constructor.
     *
     * @param zipcode
     * @param maxProducts
     * @param categoryDepth
     * @param maxCategories
     * @param maxRelated
     * @param advertiserID
     * @param adCategoryID
     * @param referralSite
     * @param searchTerms
     * @param resolution
     * @param userCookieID
     * @param productIDs
     * @param productCategories
     * @param gender
     * @param birthday
     * @param contentType
     * @param recommend
     * @param ipAddress
     * @param browser
     * @param operatingSystem
     * @param productTitle
     */
    public ContentFilterBean(String zipcode, int maxProducts, int categoryDepth, String maxCategories, int maxRelated,
                             long advertiserID, String adCategoryID, String referralSite, String searchTerms,
                             String resolution, long userCookieID, String productIDs, String productCategories,
                             String gender, Date birthday, int contentType, int recommend, String ipAddress,
                             String browser, String operatingSystem, String productTitle) {

        this.zipcode = zipcode;
        this.maxProducts = maxProducts;
        this.categoryDepth = categoryDepth;
        this.maxCategories = maxCategories;
        this.maxRelated = maxRelated;
        this.advertiserID = advertiserID;
        this.adCategoryID = adCategoryID;
        this.referralSite = referralSite;
        this.searchTerms = searchTerms;
        this.resolution = resolution;
        this.userCookieID = userCookieID;
        this.productIDs = productIDs;
        this.productCategories = productCategories;
        this.gender = gender;
        this.birthday = birthday;
        this.contentType = contentType;
        this.recommend = recommend;
        this.ipAddress = ipAddress;
        this.browser = browser;
        this.operatingSystem = operatingSystem;
        this.productTitle = productTitle;

    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public int getMaxProducts() {
        return maxProducts;
    }

    public void setMaxProducts(int maxProducts) {
        this.maxProducts = maxProducts;
    }

    public int getCategoryDepth() {
        return categoryDepth;
    }

    public void setCategoryDepth(int categoryDepth) {
        this.categoryDepth = categoryDepth;
    }

    public String getMaxCategories() {
        return maxCategories;
    }

    public void setMaxCategories(String maxCategories) {
        this.maxCategories = maxCategories;
    }

    public int getMaxRelated() {
        return maxRelated;
    }

    public void setMaxRelated(int maxRelated) {
        this.maxRelated = maxRelated;
    }

    public long getAdvertiserID() {
        return advertiserID;
    }

    public void setAdvertiserID(long advertiserID) {
        this.advertiserID = advertiserID;
    }

    public String getAdCategoryID() {
        return adCategoryID;
    }

    public void setAdCategoryID(String adCategoryID) {
        this.adCategoryID = adCategoryID;
    }

    public String getReferralSite() {
        return referralSite;
    }

    public void setReferralSite(String referralSite) {
        this.referralSite = referralSite;
    }

    public String getSearchTerms() {
        return searchTerms;
    }

    public void setSearchTerms(String searchTerms) {
        this.searchTerms = searchTerms;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public long getUserCookieID() {
        return userCookieID;
    }

    public void setUserCookieID(long userCookieID) {
        this.userCookieID = userCookieID;
    }

    public String getProductIDs() {
        return productIDs;
    }

    public void setProductIDs(String productIDs) {
        this.productIDs = productIDs;
    }

    public String getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(String productCategories) {
        this.productCategories = productCategories;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(final String orderBy) {
        this.orderBy = orderBy;
    }
}
