package com.dell.acs.web.crumbs;

public class AdminCrumb {

    private static final String LIST = "list.do";

    private static final String ADD = "add.do";

    private static final String EDIT = "edit.do";

    private static final String VIEW = "view.do";

    private static final String MANAGEITEMS = "manage-items.do";

    private static final String MANAGECATEGORIES = "manage-categories.do";

    private static final String MANAGETRACKING = "manage-tracking.do";

    private static final String DATAIMPORT = "dataImport.do";

    private static final String SITEITEMS = "site-items.do";

    private static final String CLONEITEM = "clone-item.do";

    private static final String MANAGEPROPERTIES = "manage-properties.do";

    private static final String BASE = "/admin/";

    private static final String RETAILER = "retailers/";

    private static final String RETAILERSITE = "retailerSites/";

    private static final String LIBRARY = "library/";

    private static final String DOCUMENT = "document/";

    private static final String IMAGES = "images/";

    private static final String LINKS = "link/";

    private static final String VIDEOS = "videos/";

    private static final String COUPON = "coupons/";

    private static final String EVENT = "events/";

    private static final String ARTICLE = "article/";

    private static final String CAMPAIGN = "campaign/";

    private static final String TRACKING = "tracking/";

    private static final String PROPERTIES = "entities/properties/";

    private static final String USERS = "account/users/";

    private static final String USER = "account/user/";

    public static final String USERS_Change_Password = BASE + USERS + "changepassword.do";

    public static final String USERS_Change_Password_URL = "Change Password";

    private static final String MONITOR = "monitoring/";

    private static final String METRICS = "metrics/";

    public static final String MODEL_ATTRIBUTE_NAME = "navCrumbs";

    public static final String URL_HOME = BASE + "index.do";

    public static final String URL_RETAILER_LIST = BASE + RETAILER + LIST;

    public static final String URL_RETAILER_ADD = BASE + RETAILER + ADD;

    public static final String URL_RETAILER_EDIT = BASE + RETAILER + EDIT;

    public static final String URL_RETAILERSITE_LIST = BASE + RETAILERSITE + LIST;

    public static final String URL_RETAILERSITE_ADD = BASE + RETAILERSITE + ADD;

    public static final String URL_RETAILERSITE_EDIT = BASE + RETAILERSITE + EDIT;

    public static final String URL_RETAILERSITE_DATAIMPORT = BASE + RETAILERSITE + DATAIMPORT;

    public static final String URL_RETAILERSITE_SITEITEMS = BASE + RETAILERSITE + SITEITEMS;

    public static final String URL_LIBRARY_VIEW = BASE + LIBRARY + VIEW;

    public static final String URL_LIBRARY_FRAGMENT_DOCUMENT = "documents";

    public static final String URL_LIBRARY_FRAGMENT_COUPON = "coupons";

    public static final String URL_LIBRARY_FRAGMENT_EVENT = "events";

    public static final String URL_LIBRARY_FRAGMENT_ARTICLE = "articles";

    public static final String URL_DOCUMENT = BASE + DOCUMENT;

    public static final String URL_DOCUMENT_ADD = BASE + DOCUMENT + ADD;

    public static final String URL_DOCUMENT_EDIT = BASE + DOCUMENT + EDIT;

    public static final String URL_VIDEO = BASE + VIDEOS;

    public static final String URL_VIDEO_ADD = BASE + VIDEOS + ADD;

    public static final String URL_VIDEO_EDIT = BASE + VIDEOS + EDIT;

    public static final String URL_LINK = BASE + LINKS;

    public static final String URL_LINK_ADD = BASE + LINKS + ADD;

    public static final String URL_LINK_EDIT = BASE + LINKS + EDIT;

    public static final String URL_IMAGE = BASE + IMAGES;

    public static final String URL_IMAGE_ADD = BASE + IMAGES + ADD;

    public static final String URL_IMAGE_EDIT = BASE + IMAGES + EDIT;

    public static final String URL_COUPON_ADD = BASE + COUPON + ADD;

    public static final String URL_COUPON_EDIT = BASE + COUPON + EDIT;

    public static final String URL_EVENT_ADD = BASE + EVENT + ADD;

    public static final String URL_EVENT_EDIT = BASE + EVENT + EDIT;

    public static final String URL_ARTICLE_ADD = BASE + ARTICLE + ADD;

    public static final String URL_ARTICLE_EDIT = BASE + ARTICLE + EDIT;

    public static final String URL_CAMPAIGN_LIST = BASE + CAMPAIGN + LIST;

    public static final String URL_CAMPAIGN_ADD = BASE + CAMPAIGN + ADD;

    public static final String URL_CAMPAIGN_EDIT = BASE + CAMPAIGN + EDIT;

    public static final String URL_CAMPAIGN_MANAGEITEMS = BASE + CAMPAIGN + MANAGEITEMS;

    public static final String URL_CAMPAIGN_MANAGECATEGORIES = BASE + CAMPAIGN + MANAGECATEGORIES;

    public static final String URL_CAMPAIGN_CLONEITEM = BASE + CAMPAIGN + CLONEITEM;

    public static final String URL_PROPERTIES_MANAGEPROPERTIES = BASE + PROPERTIES + MANAGEPROPERTIES;

    public static final String URL_TRACKING_MANAGETRACKING = BASE + TRACKING + MANAGETRACKING;

    public static final String URL_USER_LIST = BASE + USERS + LIST;

    public static final String URL_USER_EDIT_PARTIAL = BASE + USER;

    public static final String URL_MONITOR_HOME = BASE + MONITOR + "dataImportProcesses.do";

    public static final String URL_METRICS_HOME = BASE + MONITOR + METRICS + "apiMetrics.do";

    private static final String TEXT_DOCUMENT_ACTION = " Document";

    private static final String TEXT_IMAGE_ACTION = " Image";

    private static final String TEXT_VIDEO_ACTION = " Videos";

    private static final String TEXT_LINK_ACTION = " Link";

    private static final String TEXT_COUPON_ACTION = " Coupon";

    private static final String TEXT_EVENT_ACTION = " Event";

    private static final String TEXT_ARTICLE_ACTION = " Article";

    private static final String TEXT_CAMPAIGN_ACTION = " Campaign";

    private static final String TEXT_USER_ACTION = " User";

    public static final String TEXT_MANAGEITEMS = "Manage Items";

    public static final String TEXT_MANAGECATEGORIES = "Manage Categories";

    public static final String TEXT_CLONEITEM = "Clone Item";

    public static final String TEXT_MANAGEPROPERTIES = "Manage Properties";

    public static final String TEXT_DATAIMPORT = "Jobs";

    public static final String TEXT_HOME = "Home";

    public static final String TEXT_RETAILER = "Retailers";

    public static final String TEXT_RETAILERSITE = "Retailer Sites";

    public static final String TEXT_LIBRARY = "Library";

    public static final String TEXT_CAMPAIGN = "Campaigns";

    public static final String TEXT_TRACKING = "Pixel Tracking";

    public static final String TEXT_USER = "Users";

    public static final String TEXT_MONITOR = "Monitoring";

    public static final String TEXT_METRICS = "Metrics";

    public static final String TEXT_API_METRICS = "APIMetrics";

    public static final String TEXT_ADD = "Add";

    public static final String TEXT_DOCUMENT_ADD = TEXT_ADD + TEXT_DOCUMENT_ACTION;

    public static final String TEXT_IMAGE_ADD = TEXT_ADD + TEXT_IMAGE_ACTION;

    public static final String TEXT_VIDEO_ADD = TEXT_ADD + TEXT_VIDEO_ACTION;

    public static final String TEXT_LINK_ADD = TEXT_ADD + TEXT_LINK_ACTION;

    public static final String TEXT_COUPON_ADD = TEXT_ADD + TEXT_COUPON_ACTION;

    public static final String TEXT_EVENT_ADD = TEXT_ADD + TEXT_EVENT_ACTION;

    public static final String TEXT_ARTICLE_ADD = TEXT_ADD + TEXT_ARTICLE_ACTION;

    public static final String TEXT_CAMPAIGN_ADD = TEXT_ADD + TEXT_CAMPAIGN_ACTION;

    public static final String TEXT_EDIT = "Edit";

    public static final String TEXT_DOCUMENT_EDIT = TEXT_EDIT + TEXT_DOCUMENT_ACTION;

    public static final String TEXT_IMAGE_EDIT = TEXT_EDIT + TEXT_IMAGE_ACTION;

    public static final String TEXT_VIDEO_EDIT = TEXT_EDIT + TEXT_VIDEO_ACTION;

    public static final String TEXT_LINK_EDIT = TEXT_EDIT + TEXT_LINK_ACTION;

    public static final String TEXT_COUPON_EDIT = TEXT_EDIT + TEXT_COUPON_ACTION;

    public static final String TEXT_EVENT_EDIT = TEXT_EDIT + TEXT_EVENT_ACTION;

    public static final String TEXT_ARTICLE_EDIT = TEXT_EDIT + TEXT_ARTICLE_ACTION;

    public static final String TEXT_CAMPAIGN_EDIT = TEXT_EDIT + TEXT_CAMPAIGN_ACTION;

    public static final String TEXT_USER_EDIT = TEXT_EDIT + TEXT_USER_ACTION;

    public static final String TEXT_TRACKING_MARKETVINE = TEXT_TRACKING + " (MarketVine)";

    public static final String TEXT_TRACKING_HASOFFERS = TEXT_TRACKING + " (HasOffers)";

    public static final String TEXT_TRACKING_LINKTRACKER = TEXT_TRACKING + " (Link Tracker)";

    public static final String TEXT_TRACKING_INTEGRATED = TEXT_TRACKING + " (Integrated)";


    private final String separator = " > ";

    private String contextPath;

    private String crumb = "";

    // helpers

    private String assembleCrumb(String required, String optional) {
        String crumb = required;
        if (optional != null) {
            crumb += " (" + optional + ")";
        }
        return crumb;
    }

    private AdminCrumb addLink(String text, String link) {
        if (this.crumb.length() > 0) {
            this.crumb += this.separator;
        }

        if (link != null) {
            this.crumb += "<a href='" + this.contextPath + link + "'>";
        }

        this.crumb += text;

        if (link != null) {
            this.crumb += "</a>";
        }

        return this;
    }

    public String toAbsolute(String url) {
        return this.contextPath + url;
    }

    public String toView(String url) {
        // assumes viewName is url - ".do" extension
        if (url.endsWith(".do")) {
            url = url.substring(0, url.length() - ".do".length());
        }
        return url;
    }

    public static String linkById(String url, Long id) {
        return String.format((url + "?id=%d"), id);
    }

    public static String linkByCampaignId(String url, Long id) {
        return String.format((url + "?campaignID=%d"), id);
    }

    public static String linkBySiteId(String url, Long id, String fragment) {
        if (fragment.length() > 0) {
            fragment = "#" + fragment;
        }
        return String.format((url + "?siteID=%d%s"), id, fragment);
    }

    public static String linkBySiteId(String url, Long id) {
        return linkBySiteId(url, id, "");
    }

    public static String linkByRetailerId(String url, Long id) {
        return String.format((url + "?retailerId=%d"), id);
    }

    // structural -- Campaigns...

    public AdminCrumb campaign(String retailerName,
                               Long retailerId,
                               String retailerSiteName,
                               Long retailerSiteId,
                               String campaignName) {

        String crumb = TEXT_CAMPAIGN;
        if (campaignName != null) {
            crumb += " (" + campaignName + ")";
        }

        this.retailerSite(retailerName, retailerId, retailerSiteName);
        this.addLink(crumb, AdminCrumb.linkBySiteId(URL_CAMPAIGN_LIST, retailerSiteId));
        return this;
    }


    public AdminCrumb campaign(String retailerName,
                               Long retailerId,
                               String retailerSiteName,
                               Long retailerSiteId) {
        return this.campaign(retailerName, retailerId, retailerSiteName, retailerSiteId, null);
    }

    // ...Manage Items

    public AdminCrumb campaignManageItems(String retailerName,
                                          Long retailerId,
                                          String retailerSiteName,
                                          Long retailerSiteId,
                                          String campaignName,
                                          Long campaignId) {

        this.campaign(retailerName, retailerId, retailerSiteName, retailerSiteId, campaignName);
        this.addLink(TEXT_CAMPAIGN_EDIT, AdminCrumb.linkById(URL_CAMPAIGN_EDIT, campaignId));
        return this;
    }

    // ...Clone Item, Item Properties

    public AdminCrumb campaignManageItemAction(String retailerName,
                                               Long retailerId,
                                               String retailerSiteName,
                                               Long retailerSiteId,
                                               String campaignName,
                                               Long campaignId) {

        this.campaignManageItems(retailerName, retailerId,
                retailerSiteName, retailerSiteId,
                campaignName, campaignId);
        this.addLink(TEXT_MANAGEITEMS, AdminCrumb.linkByCampaignId(URL_CAMPAIGN_MANAGEITEMS, campaignId));
        return this;
    }

    // structural -- Library...

    private AdminCrumb library(String retailerName, Long retailerId,
                               String retailerSiteName, String fragment,Long retailerSiteId) {
        this.retailerSite(retailerName, retailerId, retailerSiteName);
        this.addLink(TEXT_LIBRARY, AdminCrumb.linkBySiteId(URL_LIBRARY_VIEW, retailerSiteId, fragment));
        return this;
    }

    // ...articles

    public AdminCrumb libraryArticle(String retailerName, Long retailerId, String retailerSiteName,Long retailerSiteId) {
        return this.library(retailerName, retailerId, retailerSiteName, URL_LIBRARY_FRAGMENT_ARTICLE,retailerSiteId);
    }

    // ...events

    public AdminCrumb libraryEvent(String retailerName, Long retailerId, String retailerSiteName,Long retailerSiteId) {
        return this.library(retailerName, retailerId, retailerSiteName, URL_LIBRARY_FRAGMENT_EVENT,retailerSiteId);
    }

    // ...coupons

    public AdminCrumb libraryCoupon(String retailerName, Long retailerId, String retailerSiteName,Long retailerSiteId) {
        return this.library(retailerName, retailerId, retailerSiteName, URL_LIBRARY_FRAGMENT_COUPON,retailerSiteId);
    }

    // ...documents

    public AdminCrumb libraryDocument(String retailerName, Long retailerId, String retailerSiteName , Long retailerSiteId) {
        return this.library(retailerName, retailerId, retailerSiteName, URL_LIBRARY_FRAGMENT_DOCUMENT,retailerSiteId);
    }

    // structural -- retailerSite

    public AdminCrumb retailerSite(String retailerName, Long retailerId, String retailerSiteName) {
        String crumb = TEXT_RETAILERSITE;
        if (retailerSiteName != null) {
            crumb += " (" + retailerSiteName + ")";
        }

        this.retailer(retailerName);
        this.addLink(crumb, AdminCrumb.linkByRetailerId(URL_RETAILERSITE_LIST, retailerId));
        return this;
    }

    public AdminCrumb retailerSite(String retailerName, Long retailerId) {
        return this.retailerSite(retailerName, retailerId, null);
    }

    // structural -- retailer

    public AdminCrumb retailer(String retailerName) {
        this.home();
        this.addLink(assembleCrumb(TEXT_RETAILER, retailerName), URL_RETAILER_LIST);
        return this;
    }

    public AdminCrumb retailer() {
        return this.retailer(null);
    }

    // structural -- monitoring

    public AdminCrumb monitoring(String retailerSiteName) {
        this.home();
        this.addLink(assembleCrumb(TEXT_MONITOR, retailerSiteName), URL_MONITOR_HOME);
        return this;
    }

    public AdminCrumb metrics(String userName) {
        this.home();
        this.addLink(assembleCrumb(TEXT_METRICS, userName), URL_METRICS_HOME);
        return this;
    }


    // structural -- users

    public AdminCrumb user(String userName) {
        this.home();
        this.addLink(assembleCrumb(TEXT_USER, userName), URL_USER_LIST);
        return this;
    }

    // structural -- home

    public AdminCrumb home() {
        this.addLink(TEXT_HOME, URL_HOME);
        return this;
    }

    // misc object

    public String render(String currentPageText) {
        if (currentPageText != null) {
            this.addLink(currentPageText, null);
        }
        return "<div id='breadcrumb'>" + this.crumb + "</div>";
    }

    public String render() {
        return this.render(null);
    }

    public AdminCrumb(String contextPath) {
        this.contextPath = contextPath;
    }
}