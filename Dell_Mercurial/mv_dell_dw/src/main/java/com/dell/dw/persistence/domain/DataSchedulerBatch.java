package com.dell.dw.persistence.domain;

import com.sourcen.core.persistence.domain.constructs.StatusAware;
import com.sourcen.core.persistence.domain.constructs.ThreadLockAware;
import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 5/15/12
 * Time: 12:42 PM
 * To change this template use File | Settings | File Templates.
 */

@NamedQueries({
        @NamedQuery(
                name="DataSchedulerBatch.getInitialBatches",
                query = "select dsb from DataSchedulerBatch dsb where " +
                        "dsb.startDate = (select min(dsb1.startDate) from DataSchedulerBatch dsb1 where dsb1.referenceId = dsb.referenceId and dsb1.endpoint = dsb.endpoint) " +
                        "and dsb.referenceId = :referenceId and dsb.endpoint = :endPoint"
        )
})

@Table(name = "data_scheduler_batch")
@Entity
public class DataSchedulerBatch extends IdentifiableEntityModel<Long>
        implements StatusAware<Integer>, ThreadLockAware {

    @ManyToOne
    private DataSource dataSource;

    // Property ID in case of Google Analytics, CustomerId in case of D3
    @Column(nullable = false)
    private Long referenceId;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(nullable = false)
    private Long startIndex;

    @Column(nullable = false)
    private Long maxResults;


    //
    // values could be
    // https://www.google.com/analytics/feeds/data?dimensions=ga:pageTitle,ga:pagePath&metrics=ga:pageviews
    // or sftp://downloader@d3systems.com:22:/feeds/2012-01-01.csv
    //
    @Column(length = 2000)
    private String endpoint;

    @Column(length = 3, nullable = false)
    private Integer priority;

    @Column(nullable = false)
    private Integer status;

    @Column(nullable = true)
    private String lockedThread;

    @ManyToOne
    private Retailer retailer;

    @Column(nullable = true)
    private String srcFile;

    @Column(nullable = true)
    private String filePath;

    @Column(nullable = true)
    private Date lastProcessedDate;

    public DataSchedulerBatch() {
    }

    public DataSchedulerBatch(DataSource dataSource, Long referenceId, Date startDate, Date endDate, Long startIndex, Long maxResults, String endpoint, Integer priority, Integer status, Retailer retailer, String filePath) {
        this.dataSource = dataSource;
        this.referenceId = referenceId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startIndex = startIndex;
        this.maxResults = maxResults;
        this.endpoint = endpoint;
        this.priority = priority;
        this.status = status;
        this.retailer = retailer;
        this.filePath = filePath;
    }

    public DataSchedulerBatch(DataSource dataSource, Long referenceId, Date startDate, Date endDate, Long startIndex, Long maxResults, String endpoint, Integer priority, Integer status, Retailer retailer, String srcFile, String filePath) {
        this.dataSource = dataSource;
        this.referenceId = referenceId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startIndex = startIndex;
        this.maxResults = maxResults;
        this.endpoint = endpoint;
        this.priority = priority;
        this.status = status;
        this.retailer = retailer;
        this.srcFile = srcFile;
        this.filePath = filePath;
    }

    public static final class Status {
        public static final Integer IN_QUEUE = 0;
        public static final Integer PROCESSING = 1;
        public static final Integer DONE = 2;
        public static final Integer ERROR_READ = 3;
        public static final Integer ERROR_EXTRACTING = 4;
        public static final Integer ERROR_PARSING = 5;
        public static final Integer ERROR_WRITE = 6;
    }

    public static final class EndPoint {
        public static final String GA_EVENTS = "GA_Events";
        public static final String GA_PAGE_VIEWS = "GA_PageViews";
        public static final String D3_LINKTRACKER_METRICS = "D3_Linktracker_Metrics";
        public static final String D3_REVENUE_METRICS = "D3_Revenue_Metrics";
        public static final String OTG = "OTG";
        public static final String STORES = "Stores";
        public static final String SF_ORDERS = "SF_Orders";
        public static final String SF_ORDER_ITEMS = "SF_Order_Items";
        public static final String ORDERS = "Orders";
        public static final String ORDER_ITEMS = "Order_Items";
    }

    public static final class Priority {
        public static final Integer UNKNOWN = 0;
        public static final Integer STORES = 1;
        public static final Integer SF_ORDERS = 2;
        public static final Integer SF_ORDERITEMS = 3;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(final Long referenceId) {
        this.referenceId = referenceId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    public Long getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(final Long startIndex) {
        this.startIndex = startIndex;
    }

    public Long getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(final Long maxResults) {
        this.maxResults = maxResults;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(final Integer status) {
        this.status = status;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(final String endpoint) {
        this.endpoint = endpoint;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getLockedThread() {
        return lockedThread;
    }

    public void setLockedThread(final String lockedThread) {
        this.lockedThread = lockedThread;
    }

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }

    public String getSrcFile() {
        return srcFile;
    }

    public void setSrcFile(String srcFile) {
        this.srcFile = srcFile;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getLastProcessedDate() {
        return lastProcessedDate;
    }

    public void setLastProcessedDate(Date lastProcessedDate) {
        this.lastProcessedDate = lastProcessedDate;
    }
}
