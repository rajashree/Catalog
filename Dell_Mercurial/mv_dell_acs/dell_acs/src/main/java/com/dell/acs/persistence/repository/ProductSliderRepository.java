/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository;

import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.ProductSlider;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Adarsh
 * Date: 2/29/12
 * Time: 1:07 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductSliderRepository extends IdentifiableEntityRepository<Long, ProductSlider> {

    /**
     * getBySiteNameAndProductId() method help in Retrieval of the ProductSlider object on the following bases
     *
     * @param siteName  name for the slider to search
     * @param productId for the slider to search
     *
     * @return ProductSlider object which is retrieved after searching from the db by providing the
     *         siteName and productId for the Slider
     */
    public ProductSlider getBySiteNameAndProductId(String siteName, String productId);

    /**
     * saveOrUpdate()  method help in the persisting the ProductSlider object in the database
     *
     * @param productSlider is passes as the argument for saving or updating
     *
     * @return the ProductSlider object which is in persisting state after saving in
     *         the database
     */
    public ProductSlider saveOrUpdate(ProductSlider productSlider);

    /**
     * getProductSlidersBySourceProduct() help in the getting the all the ProductSlider
     * object inside Collection object which are related to the sourceProduct which is
     * passed as the argument
     *
     * @param sourceProduct which is the criteria to get the related  ProductSlider object
     *
     * @return Collection of ProductSlider on the basic of the sourceProduct
     */
    public Collection<ProductSlider> getProductSlidersBySourceProduct(Product sourceProduct);


    /**
     * getProductSlidersByTargetProduct()  help in the getting the all the ProductSlider
     * object inside Collection object which are related to the targetProduct which is
     * passed as the argument
     *
     * @param targetProduct which is the criteria to get the related  ProductSlider object
     *
     * @return Collection of ProductSlider on the basic of the targetProduct
     */
    public Collection<ProductSlider> getProductSlidersByTargetProduct(Product targetProduct);

}
