package com.dell.acs.jobs;

import com.dell.acs.managers.ProductManager;
import com.dell.acs.managers.RetailerManager;
import com.dell.acs.persistence.domain.Product;
import com.sourcen.core.jobs.AbstractJob;
import com.sourcen.core.util.StringUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Job to generate hash from Title,Price,SKU column for existing Products and insert the hash in column searchItemHash
 * @author: Chethan
 */
public class SearchItemHashJob extends AbstractJob {

    private static final Logger logger = LoggerFactory.getLogger(SearchItemHashJob.class);
    private static volatile AtomicBoolean jobRunningStatus = new AtomicBoolean(false);

    @Override
    protected void executeJob(JobExecutionContext context) {
        logger.info("Upgrade Task:: To Generate hash of (title, price, sku) fields, used for Product Search API in ProductServices");
        try {
            if (!jobRunningStatus.get()) {
                synchronized (jobRunningStatus) {
                    jobRunningStatus.set(true);
                    //Setting Max Products to as default 50
                    int start = 0;
                    int maxProducts = configurationService.getIntegerProperty("dell.jobs.searchItemHash.maxProducts", 50);
                    try {
                        final Collection<Product> products = productManager.getProducts("searchItemHash", null, start, maxProducts);
                        logger.info("Number of Products with hash as null value::" + products.size());
                        //If there are no products for hashing, Exit the Loop
                        if (products.size() <= 0) {
                            jobRunningStatus.set(false);
                            return;
                        } else {
                            Iterator productItr = products.iterator();
                            while (productItr.hasNext()) {
                                try {
                                    final Product product = (Product) productItr.next();
                                    final String title = product.getTitle();
                                    final Float price = product.getPrice();
                                    final String sku = product.getSku();
                                    if (product.getSearchItemHash() == null) {
                                        if (title != null && price != null && sku != null) {
                                            product.setSearchItemHash(StringUtils.MD5Hash(title + price + sku));
                                        }
                                    }
                                    productManager.saveOrUpdate(product);
                                } catch (Exception e) {
                                    logger.error("Error while Save or Updating Hash Values::" + e.getMessage());
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.error("Error while Retrieving Retailer/Products::" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error caused due to::" + e.getMessage());
            e.printStackTrace();
        } finally {
            // Reset the jobRunningStatus flag once done with processing all broken images
            jobRunningStatus.set(false);
        }
    }

    @Autowired
    ProductManager productManager;

    @Autowired
    RetailerManager retailerManager;

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public void setRetailerManager(RetailerManager retailerManager) {
        this.retailerManager = retailerManager;
    }


}
