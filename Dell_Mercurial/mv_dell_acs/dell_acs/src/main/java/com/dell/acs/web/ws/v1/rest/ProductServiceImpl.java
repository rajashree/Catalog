/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.rest;

import com.dell.acs.managers.ContentFilterBean;
import com.dell.acs.managers.ProductManager;
import com.dell.acs.managers.RecommendationManager;
import com.dell.acs.managers.TaxonomyManager;
import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.ProductReview;
import com.dell.acs.persistence.repository.model.ProductSearchModel;
import com.dell.acs.web.ws.v1.ProductService;
import com.dell.acs.web.ws.v1.beans.WSBeanUtil;
import com.dell.acs.web.ws.v1.beans.WSProduct;
import com.dell.acs.web.ws.v1.beans.WSProductReview;
import com.dell.acs.web.ws.v1.beans.WSProductSearchResult;
import com.dell.acs.web.ws.v1.beans.WSSearchHeader;
import com.dell.acs.web.ws.v1.beans.WSSearchRetailerSummary;
import com.sourcen.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/*import com.dell.acs.web.ws.v1.beans.WSCategoryProduct;*/

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: sandeep $
 * @version $Revision: 3776 $, $Date:: 2012-07-03 08:04:19#$
 */
@WebService
@RequestMapping("/api/v1/rest/ProductService")
public class ProductServiceImpl extends WebServiceImpl implements ProductService {

    @Override
    @Deprecated
    @RequestMapping("getProductRecommendations")
    public Collection<WSProduct> getProductRecommendations(@ModelAttribute ContentFilterBean filter) {
        //st<WSCategoryProduct> wsCategoryProductList = new ArrayList<WSCategoryProduct>();
        Collection<Product> products = recommendationManager.getActiveRecommendedProducts(filter);

        /*Map<Long, WSCategoryProduct> categoryMap = new HashMap<Long, WSCategoryProduct>();

        for (Product product : products) {

            TaxonomyCategory category = product.getCategory();

            //add product to category
            //category.addProduct(product);
            List<TaxonomyCategory> categoryList = taxonomyManager.getTree(category);

            //adding the leaf category
            categoryList.add(category);
            int size = categoryList.size();
            for(int i=0;i<size;i++){
                WSCategoryProduct wsCategoryProduct = new WSCategoryProduct();
                wsCategoryProduct.setName(categoryList.get(i).getName());
                if(i==size-1){
                  WSProduct wsProduct = WSBeanUtil.convert(product, new WSProduct());
                  wsCategoryProduct.addWSProduct(wsProduct);
                }
                try {
                    categoryMap.put(categoryList.get(i).getId(),wsCategoryProduct);
                }catch(Exception e){
                    e.printStackTrace();
                }
                *//*if (category.getId() == taxonomyCategory.getId()) {
                    taxonomyCategory.addProduct(product);
                }
                try {
                    categoryMap.put(taxonomyCategory.getId(), taxonomyCategory);
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }*//*


            }


            //wsCategoryProduct.setCategoryList(categoryList);
            //wsCategoryProduct.setProduct(product);
            //wsCategoryProductList.add(wsCategoryProduct);
        }*/

        return WSBeanUtil.convert(products, WSProduct.class);
    }

    @Override
    @RequestMapping("getProducts")
    public Collection<WSProduct> getProducts(@RequestParam(required = true) String searchTerm,
                                             @RequestParam(required = false) String merchant,
                                             @RequestParam(required = false, defaultValue = "0") Integer start,
                                             @RequestParam(required = false, defaultValue = "15") Integer maxProducts,
                                             @RequestParam(required = false) String filter,
                                             @RequestParam(required = false) String orderBy) {

        //Value for maxLimit is fetched from appliction properties.
        int maxLimit = configurationService.getIntegerProperty("dell.webservice.products.search.maxLimit", 100);
        if (maxProducts != null && maxProducts > maxLimit) {
            maxProducts = maxLimit;
        }

        //If start is not passed assigning it to 0
        if (start == null) {
            start = 0;
        }

        if (filter != null) {
            //Trim filter
            filter = filter.trim();

            //Converting filter to Lower Case
            filter = filter.toLowerCase();
        }
        Collection<Product> products = productManager.getActiveSearchedProducts(searchTerm,
                merchant,
                start,
                maxProducts,
                filter,
                orderBy);

        // http://jira.marketvine.com/browse/CS-381
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        if (request.getParameter("outputFormat") == null) {
            request.setAttribute("outputFormat", "Dell");
        }
//        RequestContextHolder.getRequestAttributes().getAttribute()
        return WSBeanUtil.convert(products, WSProduct.class);
    }

    @Override
    @RequestMapping("getProduct")
    public WSProduct getProduct(@RequestParam(required = true) Long productId) throws Exception {
        Product product = productManager.getProduct(productId);
        return WSBeanUtil.convert(product, new WSProduct());
    }

    @Override
    @RequestMapping("updateProductSales")
    public Integer updateProductSales(@RequestParam(required = true) Long productId) throws Exception {
        Product product = productManager.getProduct(productId);
        Integer salesCount = 0;
        if (product != null) {
            salesCount = product.getProperties().getIntegerProperty("salescount", 0);
            salesCount++;
            product.getProperties().setProperty("salescount", salesCount);
            productManager.update(product);
        }
        return salesCount;
    }

    /**
     * Returns the latest shopped count for a given product.
     *
     * @param productId - the productID
     *
     * @return - Integer value of the shoppedCount. Return 0, if there is value.
     * @throws Exception
     */
    @Override
    @RequestMapping("getProductShoppedCount")
    public Integer getProductShoppedCount(@RequestParam(required = true) Long productId) throws Exception {
        Product product = productManager.getProduct(productId);
        Integer salesCount = 0;
        if (product != null) {
            salesCount = product.getProperties().getIntegerProperty("salescount", 0);
        }
        return salesCount;
    }

    @Override
    @RequestMapping("getPagedProductReviews")
    /* EXTERNALINTERACTIVEADS-352 :: product id, page number and page size */
    public Collection<WSProductReview> getPagedProductReviews(@RequestParam(required = true) Long productId,
                                                              @RequestParam(required = true) Integer pageSize,
                                                              @RequestParam(required = true) Integer pageNumber) throws Exception {
        Collection<ProductReview> productReviews = productManager.getProductReviews(productId, pageNumber, pageSize);
        return WSBeanUtil.convert(productReviews, WSProductReview.class);
    }

    @Override
    @RequestMapping("getProductDetail")
    public WSProduct getProductDetail(@RequestParam(required = true) Long productId) throws Exception {
        /**
         * CS-397:All APIs should not show up disabled products
         */
        Product product = productManager.getProduct(productId);
        return WSBeanUtil.convert(product, new WSProduct());
    }

    @Override
    @RequestMapping("getProductDetails")
    public Collection<WSProduct> getProductDetails(@RequestParam(required = true) String productId) throws Exception {
        String[] ids = StringUtils.split(productId, "-");
        logger.debug("Get Product details for IDs :::  " + productId);
        Collection<Product> products = new ArrayList<Product>();
        if (ids.length > 0) {
            for (String id : ids) {
                Product product = this.productManager.getProduct(Long.valueOf(id));
                if (product != null)
                    products.add(product);
            }//for each ID
        }

        if (products.size() > 0) {
            return WSBeanUtil.convert(products, WSProduct.class);
        }
        return Collections.emptyList();
    }

    /*
      * (non-Javadoc)
      *
      * @see
      * com.dell.acs.web.ws.v1a.ProductService#getProductSearch(java.lang.String,
      * java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String)
      */
    @Override
    @RequestMapping("searchProducts")
    public WSProductSearchResult searchProducts(@RequestParam(required = true) String searchTerm,
                                                @RequestParam(required = false) String merchant,
                                                @RequestParam(required = false, defaultValue = "0") Integer start,
                                                @RequestParam(required = false, defaultValue = "15")
                                                Integer maxProducts, @RequestParam(required = false) String filter,
                                                @RequestParam(required = false) String orderBy) {
        //Value for maxLimit is fetched from appliction properties.
        int maxLimit = configurationService.getIntegerProperty("dell.webservice.products.search.maxLimit", 100);
        if (maxProducts != null && maxProducts > maxLimit) {
            maxProducts = maxLimit;
        }

        //If start is not passed assigning it to 0
        if (start == null) {
            start = 0;
        }

        if (filter != null) {
            //Trim filter
            filter = filter.trim();

            //Converting filter to Lower Case
            filter = filter.toLowerCase();
        }
        WSSearchHeader header = new WSSearchHeader();
        Collection<ProductSearchModel> psms = productManager.getSearchedProductSiteNames(searchTerm,
                merchant,
                start,
                maxProducts,
                filter);
        int totalCount = 0;
        Collection<WSSearchRetailerSummary> retailerSummaries = new ArrayList<WSSearchRetailerSummary>(psms.size());
        for (ProductSearchModel psm : psms) {
            totalCount += psm.getTotalCount();
            WSSearchRetailerSummary retailerSummary = new WSSearchRetailerSummary();
            retailerSummary.setSiteName(psm.getSiteName());
            retailerSummary.setTotalCount(psm.getTotalCount());
            retailerSummaries.add(retailerSummary);
        }
        header.setTotalCount(totalCount);
        header.setRetailers(retailerSummaries);

        Collection<Product> products = productManager.getActiveSearchedProducts(searchTerm,
                merchant,
                start,
                maxProducts,
                filter,
                orderBy);
        WSProductSearchResult psr = new WSProductSearchResult();
        psr.setHeader(header);
        Collection<WSProduct> wsProducts = WSBeanUtil.convert(products, WSProduct.class);
        psr.setProducts(wsProducts);

        return psr;
    }

    @Override
    @RequestMapping("updateProductStatus")
    public WSProduct updateProductStatus(@RequestParam(required = true) Long productId,
                                       @RequestParam(required = true) boolean status) throws Exception {
        Product product = productManager.getEntireProduct(productId);
        product.setEnabled(status);
        productManager.update(product);
        WSProduct wsProduct = WSBeanUtil.convert(product,new WSProduct());
        return wsProduct;
    }

    //
    // IoC
    //
    @Autowired
    private RecommendationManager recommendationManager;

    public void setRecommendationManager(final RecommendationManager recommendationManager) {
        this.recommendationManager = recommendationManager;
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
