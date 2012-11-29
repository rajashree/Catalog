/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.dataimport.ficstar;

import com.dell.acs.managers.ProductManagerImpl;
import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.domain.Taxonomy;
import com.dell.acs.persistence.domain.TaxonomyCategory;
import com.dell.acs.persistence.domain.User;
import com.dell.acs.persistence.repository.ProductRepository;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.dell.acs.persistence.repository.TaxonomyCategoryRepository;
import com.dell.acs.persistence.repository.TaxonomyRepository;
import com.dell.acs.persistence.repository.UserRepository;
import com.sourcen.dataimport.service.support.hibernate.BeanProcessorAdapter;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: sandeep $
 * @version $Revision: 3711 $, $Date:: 2012-06-28 08:05:51#$
 */

/**
 * {@inheritDoc}
 */
public class ProductBeanProcessor extends BeanProcessorAdapter {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supportsBean(final Class clazz) {
        return Product.class.equals(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object preProcessBeforePersist(final Object bean, final Map<String, Object> row) {


        Product product = (Product) bean;

        Assert.notNull(row.get("productId"), "Product Id is null");
        Assert.notNull(row.get("productId").toString().trim().length() == 0 ? null :
                row.get("productId"), "Product Id is null");

        RetailerSite retailerSite = retailerSiteRepository.getByName(product.getSiteName());
        product.setRetailerSite(retailerSite);
        product.setRetailer(retailerSite.getRetailer());
        User user = userRepository.get("admin");
        product.setCreatedBy(user);
        product.setModifiedBy(user);
        TaxonomyCategory root = null;
        final Taxonomy taxonomy = taxonomyRepository.getTaxonomy(product.getRetailerSite(), "product");

        String[] productCategories = new String[6];
        searchAndInsert(row, "category1", productCategories, 0);
        searchAndInsert(row, "category2", productCategories, 1);
        searchAndInsert(row, "category3", productCategories, 2);
        searchAndInsert(row, "category4", productCategories, 3);
        searchAndInsert(row, "category5", productCategories, 4);
        searchAndInsert(row, "category6", productCategories, 5);

        if (taxonomy != null) {
            root = taxonomyCategoryRepository.getRootCategory(taxonomy);
        }

        TaxonomyCategory parent = root;
        for (int i = 0; i < productCategories.length; i++) {

            if (parent == null) {
                throw new RuntimeException("parent category cannot be null.");
            }
            String categoryName = productCategories[i];

            if (categoryName != null) {
                TaxonomyCategory category = taxonomyCategoryRepository.getCategory(taxonomy, parent, categoryName);
                if (category == null) {
                    category = new TaxonomyCategory(categoryName);
                    category.setTaxonomy(taxonomy);
                    category.setParent(parent);
                    category.setDepth(parent.getDepth() + 1);
                    taxonomyCategoryRepository.insert(category);
                }

                // re assign parent for loop
                parent = category;
            } else {
                break;
            }
        }
        if (parent != null) {
            product.setCategory(parent);
        } else {
            throw new RuntimeException("Unable to set category for product :=" + product.getProductId());
        }
        product.setCreatedDate(new Date());
        product.setModifiedDate(new Date());
        // Todo later change when new feed have the inforlink url value
        product.setInfoLink(product.getUrl());

        // handle properties.
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            if (entry.getValue() != null) {
                if (entry.getKey().startsWith("properties.")) {
                    String key = entry.getKey().substring(11);
                    product.getProperties().setProperty(key, entry.getValue().toString());
                }
            }
        }

        //unescaping url,buyLink,reviewsLink,infoLink and flashLink
        product.setUrl((String) unEscapeUrl(row.get("url")));

        Object object = row.get("buyLink");
        if (object != null && object.toString().trim().length() > 0) {
            product.setBuyLink((String) unEscapeUrl(row.get("buyLink")));
        } else {
            product.setBuyLink(product.getUrl());
        }

        product.setReviewsLink((String) unEscapeUrl(row.get("reviewsLink")));
        product.setInfoLink((String) unEscapeUrl(row.get("infoLink")));
        product.setFlashLink((String) unEscapeUrl(row.get("flashLink")));

        // compute product weight
        product.setWeight(ProductManagerImpl.computeProductWeight(product));

        return product;
    }

    private Object unEscapeUrl(Object object) {
        if (object != null) {
            return StringEscapeUtils.unescapeHtml(object.toString().trim());
        }
        return object;
    }

    private void searchAndInsert(final Map<String, Object> row, final String categoryKey,
                                 final String[] productCategories, final int i) {
        if (row.get(categoryKey) != null) {
            productCategories[i] = row.get(categoryKey).toString().trim();
            if (productCategories[i].length() == 0) {
                productCategories[i] = null;
            }
        }
    }

    @Override
    public Object postProcessAfterPersist(final Object bean, final Map<String, Object> row) {
        Product product = (Product) bean;
        if (product.getProperties().size() > 0) {
            productRepository.updateProperties((Product) bean);
        }
        return bean;
    }

    /**
     * properties and setter() for value injections
     */
    protected ProductRepository productRepository;

    /**
     * properties and setter() for value injections
     */
    protected RetailerSiteRepository retailerSiteRepository;

    public void setRetailerSiteRepository(final RetailerSiteRepository retailerSiteRepository) {
        this.retailerSiteRepository = retailerSiteRepository;
    }

    private TaxonomyCategoryRepository taxonomyCategoryRepository;

    public void setTaxonomyCategoryRepository(TaxonomyCategoryRepository taxonomyCategoryRepository) {
        this.taxonomyCategoryRepository = taxonomyCategoryRepository;
    }

    private UserRepository userRepository;

    public void setUserRepository(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private TaxonomyRepository taxonomyRepository;

    public void setTaxonomyRepository(TaxonomyRepository taxonomyRepository) {
        this.taxonomyRepository = taxonomyRepository;
    }

    public void setProductRepository(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
