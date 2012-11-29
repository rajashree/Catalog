package com.dell.acs.web.ws.v2;

import com.dell.acs.ProductDisabledException;
import com.dell.acs.ProductNotFoundException;
import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.ProductReview;
import com.sourcen.core.util.beans.ServiceFilterBean;
import com.sourcen.core.web.ws.WebService;

import javax.xml.ws.WebServiceException;
import java.util.Collection;

/**
 *
 * User: vivek
 * Date: 7/20/12
 * Time: 1:07 PM
 *
 */
public interface ProductService extends WebService {

    /**
     * Get the Product Details for the specified product(s)
     * Note: Using v1 of ProductManager
     * @param productID - "1-2-3" returns Product details of products with Id = 1, Id = 2 & Id = 3;
     * @return Collection of {@link Product }
     *
     */
    public Collection<Product> getProductDetails(String productID) throws ProductNotFoundException, ProductDisabledException;

    /**
     * Gets reviews associated with a specific product
     * @param filter - The variable defined in the {@link ServiceFilterBean} can be over-ridden by
     * REQUEST parameters.
     * @param productID - Long - the product ID
     * @return Collection of {@link ProductReview}
     * @throws ProductNotFoundException, ProductDisabledException
     */
    public Collection<ProductReview> getProductReviews(ServiceFilterBean filter, Long productID) throws ProductNotFoundException, ProductDisabledException;

    /**
     * Search products against all Merchants (RetailerSites), by default. If a merchant(RetailerSite) name is specified then we
     * restrict the search for the products is restricted to a specific RetailerSite
     * @param filter variable defined in the {@link ServiceFilterBean} can be over-ridden by
     * REQUEST parameters
     * @param merchantName - String merchantName, which essentially is RetailerSite
     * @return Collection of {@link Product}
     * @throws WebServiceException - Exception is thrown, if the parameters 'q' & 'searchFields' are not set on the REQUEST.
     */
    public Collection<Product> searchProducts(ServiceFilterBean filter, String merchantName) throws WebServiceException;

    /**
     * TODO: Do we need to implement this on v2
     * @param productId
     * @return
     * @throws Exception
     */
    public Integer updateProductSales(Long productId) throws Exception;
}
