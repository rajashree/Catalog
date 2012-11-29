package com.dell.acs.web.ws.v2.rest;

import com.dell.acs.ProductDisabledException;
import com.dell.acs.ProductNotFoundException;
import com.dell.acs.managers.ProductManager;
import com.dell.acs.managers.TaxonomyManager;
import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.ProductReview;
import com.dell.acs.web.ws.v2.ProductService;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebService;
import javax.xml.ws.WebServiceException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Version2 of the Product Service which allows users to
 * 1) Get Product(s) Details by ProductID
 * 2) Search Products by query across defined searchFields.
 * Where searchFields are the names which need to map the Product Table Columns
 * 3) Get Product Reviews for a specified ProductID.
 *
 */
@Component(value = "ProductServiceV2")
@WebService
@RequestMapping("/api/v2/rest/ProductService")
public class ProductServiceImpl extends WebServiceImpl implements ProductService {

    public static final Logger logger = Logger.getLogger(ProductServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    @RequestMapping("getProductDetails")
    public Collection<Product> getProductDetails(@RequestParam(required = true) String productID) throws ProductNotFoundException, ProductDisabledException {
        String[] ids = StringUtils.split(productID, "-");
        Collection<Product> products = new ArrayList<Product>();

        logger.debug("Get Product details for IDs :::  " + productID);
        //Fix for https://jira.marketvine.com/browse/CS-530, where the product was not being added
        //to the collection before returning.
        if( ids.length == 1){
            products.add(this.productManager.getProduct(Long.valueOf(ids[0])));
        }

        if (ids.length > 1) {

            for (String id : ids) {
                Product product = null;
                try {
                    product = this.productManager.getProduct(Long.valueOf(id));
                } catch (Exception e) {
                    logger.error("Product with ID:=" + id + " is either not null or disabled", e);
                    e.printStackTrace();
                }
                if (product != null)
                    products.add(product);
            }//for each ID
        }


        if (products.size() > 0) {
            return products;
        }

        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    @RequestMapping("getProductReviews")
    public Collection<ProductReview> getProductReviews(@ModelAttribute ServiceFilterBean filter, @RequestParam(required = true) Long productID) throws ProductNotFoundException, ProductDisabledException {
        return productManager.getProductReviews(productID, filter);
    }

    /**
     * {@inheritDoc}
     * @param filter
     * @param merchantName - String merchantName, which essentially is RetailerSite
     * @return
     */
    @Override
    @RequestMapping("searchProducts")
    public Collection<Product> searchProducts(@ModelAttribute ServiceFilterBean filter,
                                              @RequestParam(required = false) String merchantName) {
        if (StringUtils.isEmpty(filter.getQ())) {
            throw new WebServiceException("Required parameter 'q' is not present");
        }
        if (StringUtils.isEmpty(filter.getSearchFields())) {
            throw new WebServiceException("Required parameter 'searchFields' is not present.");
        }

        logger.info("Search in Products for   :::     " + filter.getQ() + "  across the Product columns    :::  " + filter.getSearchFields());
        return this.productManager.getProducts(filter, merchantName);

    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public Integer updateProductSales(Long productId) throws Exception {
        return null;
    }


    @Autowired
    private ProductManager productManager;

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    @Autowired
    private TaxonomyManager taxonomyManager;

    public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
        this.taxonomyManager = taxonomyManager;
    }

}
