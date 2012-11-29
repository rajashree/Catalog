/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.CampaignNotFoundException;
import com.dell.acs.managers.CampaignManager;
import com.dell.acs.managers.ContentFilterBean;
import com.dell.acs.managers.ProductManagerImpl;
import com.dell.acs.managers.RetailerManager;
import com.dell.acs.persistence.domain.*;
import com.dell.acs.persistence.repository.ProductRepository;
import com.dell.acs.persistence.repository.TaxonomyCategoryRepository;
import com.dell.acs.persistence.repository.TaxonomyRepository;
import com.dell.acs.persistence.repository.model.ProductSearchModel;
import com.sourcen.core.persistence.repository.impl.hibernate.PropertiesAwareRepositoryImpl;
import com.sourcen.core.util.ArrayUtils;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: sandeep $
 * @version $Revision: 3740 $, $Date:: 2012-06-29 13:50:45#$
 */
@Repository
public class ProductRepositoryImpl extends PropertiesAwareRepositoryImpl<Product> implements ProductRepository {

    static final float DEFAULT_STAR_RATING = 3.0f;
    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ProductRepositoryImpl.class);

    /**
     * CampaignManager Bean Injection.
     */
    @Autowired
    private CampaignManager campaignManager;

    @Autowired
    private RetailerManager retailerManager;

    @Autowired
    private TaxonomyRepository taxonomyRepository;

    @Autowired
    private TaxonomyCategoryRepository taxonomyCategoryRepository;

    /**
     * Constructor.
     */
    public ProductRepositoryImpl() {
        super(Product.class, ProductProperty.class);
    }

    public Product getActive(Long id) {

        try {
            Product product = get(id);
            if (product != null) {
                if (product.getRetailer().getActive() && product.getRetailerSite().getActive()) {
                    return product;
                } else {
                    logger.info("product with ID:={} is inactive due to retailer/retailerSite", id);
                }

            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    @Override
    public Product getBySiteNameAndProductId(String siteName, String productId) {
        try {
            Object o = getSession().createCriteria(Product.class).
                    add(Restrictions.eq("siteName", siteName)).add(Restrictions.eq("productId", productId)).uniqueResult();
            if (o != null) {
                return (Product) o;
            }
        } catch (Exception e) {
            logger.warn("ProductRepositoryImpl " + e.getMessage());
        }
        return null;
    }

    @Override
    public Product saveOrUpdate(Product product) {
        super.getSession().saveOrUpdate(product);
        return product;
    }

    @Override
    public List<Map<String, Object>> getAllCampaignItemProductReviews(Long productID, List<Long> selectedReviews) {
        return getAllCampaignItemProductReviews(productID, selectedReviews, DEFAULT_STAR_RATING);
    }

    @Override
    public List<Map<String, Object>> getAllCampaignItemProductReviews(Long productID, List<Long> selectedReviews, float minRating) {
        List<Object> selected = new ArrayList<Object>();
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        Criteria selectedReviewCriteria = getSession().createCriteria(ProductReview.class);
        // Get the selected reviews first
        if (selectedReviews.size() > 0) {
            selectedReviewCriteria.add(Restrictions.in("id", selectedReviews));
            selectedReviewCriteria.addOrder(Order.desc("computedWeight"));
            selected = selectedReviewCriteria.list();
        }
        // Get the 10 more reviews based on the computedWeight
        Criteria reviewCriteria = getSession().createCriteria(ProductReview.class);
        reviewCriteria.add(Restrictions.eq("product.id", productID));
        reviewCriteria.add(Restrictions.ge("stars", minRating));
        if (selectedReviews.size() > 0) {
            reviewCriteria.add(Restrictions.not(Restrictions.in("id", selectedReviews)));
        }
        reviewCriteria.addOrder(Order.desc("computedWeight"));
        // Merge both results
        selected.addAll(reviewCriteria.list());
        for (Object obj : selected) {
            results.add(convertProductReviewToMap((ProductReview) obj));
        }
        logger.info("Reviews for product ID -" + productID + " are " + results.size());
        return results;
    }

    private Map<String, Object> convertProductReviewToMap(ProductReview productReview) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("id", productReview.getId());
        dataMap.put("review", productReview.getReview());
        dataMap.put("title", productReview.getTitle());
        return dataMap;
    }

    /**
     * TODO - optimize for performance. @ NavinRAJ
     *
     * @param contentFilterBean
     * @return
     */
    @Override
    public Collection<Product> getActiveProducts(ContentFilterBean contentFilterBean) {
        boolean flag = false;
        String searchTerm = contentFilterBean.getSearchTerms();
        String productTitle = contentFilterBean.getProductTitle();
        String productIDs = contentFilterBean.getProductIDs();
        String productCategories = contentFilterBean.getProductCategories();
        Long retailerID = contentFilterBean.getAdvertiserID();
        int maxProducts = contentFilterBean.getMaxProducts();
        String orderBy = contentFilterBean.getOrderBy();
        maxProducts = (maxProducts == 0 ? 100 : maxProducts);
        // limit to 1000 records. we should handle pagination some other time.
        if (maxProducts > 1000) {
            maxProducts = 1000;
        }
        Criteria recommendedProductCriteria = getSession().createCriteria(Product.class);
        Criterion mainCriterion = null;
        Criterion searchTermCriterion;
        Criterion productIdCriterion;
        Criterion productTitleCriterion;
        Criterion retailerIDCriterion;
        Criterion productCategoriesCriterion;

        //Gender Criteria
        /* if(gender!=null){
            List genderList = new ArrayList();
            //genderList.add(gender);
            //Criteria genderCriteria =
            (Criteria)Restrictions.or(Restrictions.sqlRestriction()in("category1", genderList)
            ,Restrictions.in("category1", genderList);
            genderCriterion =
            Restrictions.sqlRestriction(gender + " in (?)", "category%", Hibernate.STRING);
            if(flag){
                recommenProductCriteria.add(Restrictions.or(mainCriterion, genderCriterion));
            }else{
                mainCriterion = genderCriterion;
                recommenProductCriteria.add(mainCriterion);
                flag = true;
            }
        }*/

        //searchTerm Criteria
        // fix for https://jira.marketvine.com/browse/CS-249
        if (searchTerm != null) {

            if (searchTerm.contains(" ")) {
                // ok, we have multiple words.

                // add the searchTerm first
                Criterion combinedORCriterion = Restrictions.or(Restrictions.ilike("title", "%" + searchTerm + "%"), Restrictions.ilike("description", "%" + searchTerm + "%"));

                // now do the splits
                for (String term : searchTerm.split(" ")) {
                    term = "%" + term + "%";
                    combinedORCriterion = Restrictions.or(combinedORCriterion, Restrictions.ilike("title", term));
                    combinedORCriterion = Restrictions.or(combinedORCriterion, Restrictions.ilike("description", term));
                }
                searchTermCriterion = combinedORCriterion;
            } else {
                // no space, just add title description and be done with.
                searchTermCriterion = Restrictions.or(Restrictions.ilike("title", "%" + searchTerm + "%"), Restrictions.ilike("description", "%" + searchTerm + "%"));

            }

            mainCriterion = searchTermCriterion;
            recommendedProductCriteria.add(mainCriterion);
            flag = true;
        }

        // add orderBy clause
        // https://jira.marketvine.com/browse/CS-248
        if (orderBy != null && !orderBy.contains("merchant")) {
            // lets restrict the order by to the fields we have.
            String[] orderByParts = orderBy.split("-");
            boolean isAsc = true;
            if (orderByParts.length > 1) {
                orderBy = orderByParts[0];
                isAsc = (orderByParts[1] != null && orderByParts[1].equalsIgnoreCase("asc"));
            }

            String[] availableFields = sessionFactory.getClassMetadata(Product.class).getPropertyNames();
            if (ArrayUtils.indexOf(availableFields, orderBy) > -1) {
                // this is a field we know...
                if (isAsc) {
                    recommendedProductCriteria.addOrder(Order.asc(orderBy));
                } else {
                    recommendedProductCriteria.addOrder(Order.desc(orderBy));
                }
            }
        }

        //Product Criteria
        if (productIDs != null) {
            String[] paramproductIdList = productIDs.split(",");
            List<Long> productIdList = new LinkedList<Long>();
            //Converting List of Strings Values to List of Long Values
            for (String productIdString : paramproductIdList) {
                productIdString = productIdString.trim();
                if (StringUtils.isNumeric(productIdString)) {
                    productIdList.add(Long.parseLong(productIdString));
                } else {
                    LOG.info("Unable to parse the product ID " + productIdString);
                }
            }

            productIdCriterion = Restrictions.in("id", productIdList);
            if (flag) {
                recommendedProductCriteria.add(Restrictions.or(mainCriterion, productIdCriterion));
            } else {
                mainCriterion = productIdCriterion;
                recommendedProductCriteria.add(mainCriterion);
                flag = true;
            }
        }

        //Product Title
        if (productTitle != null) {
            productTitleCriterion = Restrictions.eq("title", productTitle);
            if (flag) {
                recommendedProductCriteria.add(Restrictions.or(mainCriterion, productTitleCriterion));
            } else {
                mainCriterion = productTitleCriterion;
                recommendedProductCriteria.add(mainCriterion);
                flag = true;
            }
        }

        //Retailer Id
        if (retailerID != null && retailerID > 0) {
            retailerIDCriterion = Restrictions.eq("retailer.id", Long.valueOf(retailerID));
            if (flag) {
                recommendedProductCriteria.add(Restrictions.or(mainCriterion, retailerIDCriterion));
            } else {
                mainCriterion = retailerIDCriterion;
                recommendedProductCriteria.add(mainCriterion);
                flag = true;
            }
        }

        //Product Categories
        if (productCategories != null) {
            String[] productCategoriesList = productCategories.split(",");
            Criterion category12Creterion = Restrictions.or(Restrictions.in("category1", productCategoriesList), Restrictions.in("category2", productCategoriesList));
            Criterion category34Creterion = Restrictions.or(Restrictions.in("category3", productCategoriesList), Restrictions.in("category4", productCategoriesList));
            Criterion category56Creterion = Restrictions.or(Restrictions.in("category5", productCategoriesList), Restrictions.in("category6", productCategoriesList));
            Criterion category1to4Creterion = Restrictions.or(category12Creterion, category34Creterion);
            productCategoriesCriterion = Restrictions.or(category1to4Creterion, category56Creterion);
            if (flag) {
                recommendedProductCriteria.add(Restrictions.or(mainCriterion, productCategoriesCriterion));
            } else {
                mainCriterion = productCategoriesCriterion;
                recommendedProductCriteria.add(mainCriterion);
                flag = true;
            }
        }

        // default limit
        //TODO: the maxProducts is per Category
        recommendedProductCriteria.setMaxResults(maxProducts);

        // Filter the disabled products - EXTERNALINTERACTIVEADS-394
        recommendedProductCriteria.add(Restrictions.or(Restrictions.isNull("enabled"), Restrictions.eq("enabled", true)));

        // additional filtering to ensure return products are for active Retailers and RetailerSites
        recommendedProductCriteria.createCriteria("retailerSite").add(Restrictions.eq("active", true));
        recommendedProductCriteria.createCriteria("retailer").add(Restrictions.eq("active", true));

        // Fetch all recommended products matching the criteria
        List<Product> products = recommendedProductCriteria.list();

        //Fetch the retailerSites for the matching criteria to load the campaign products
        Set<Long> retailerSites = new HashSet<Long>();
        LOG.debug("Fetching product sites for Campaign");
        for (Product product : products) {
            retailerSites.add(product.getRetailerSite().getId());
        }

        List<Product> campaignRelatedProducts = new ArrayList<Product>();
        Collection<Campaign> recommendedCampaigns = campaignManager.getCampaignsByRetailerSite(retailerSites);
        LOG.debug("Fetching products for Campaign");
        for (Campaign campaign : recommendedCampaigns) {
            campaignRelatedProducts.addAll(campaign.getProducts());
        }

        LOG.debug("Filtering the products");
        List<Product> filteredProducts = new LinkedList<Product>();
        // To filter the duplicate products
        Set<Product> finalProds = new HashSet<Product>();
        finalProds.addAll(campaignRelatedProducts);
        finalProds.addAll(products);

        filteredProducts.addAll(finalProds);

        // Fetch a sublist of max products that needs to be returned
        if (filteredProducts.size() > 0) {
            // check the max products size and add the rest of the products to campaint Products
            if (filteredProducts.size() > maxProducts) {
                LOG.debug("Product Results > " + filteredProducts.size() + " greater than MAX_RESULT - " + maxProducts);
                filteredProducts = filteredProducts.subList(0, maxProducts);
            }
        } else {
            LOG.debug("No Products matching the criteria. Returning EMPTY_LIST");
            //there are no products matching the criteria
            filteredProducts = Collections.emptyList();
        }
        //        TESTING
        //        for(Product prod: filteredProducts){
        //            LOG.debug("Product ID >> " + prod.getId());
        //        }
        return filteredProducts;
    }

    @Override
    public Collection<Product> getProducts(String productIds) {

        String productIDs = productIds;
        Criteria recommendedProductCriteria = getSession().createCriteria(Product.class);
        Criterion productIdCriterion;

        //Product Criteria
        if (productIDs != null) {
            String[] paramproductIdList = productIDs.split(",");
            List<Long> productIdList = new LinkedList<Long>();
            //Converting List of Strings Values to List of Long Values
            for (String productIdString : paramproductIdList) {
                productIdString = productIdString.trim();
                if (StringUtils.isNumeric(productIdString)) {
                    productIdList.add(Long.parseLong(productIdString));
                } else {
                    LOG.info("Unable to parse the product ID " + productIdString);
                }
            }
            productIdCriterion = Restrictions.in("id", productIdList);
            recommendedProductCriteria.add(productIdCriterion);
        }

        // Filter the disabled products - EXTERNALINTERACTIVEADS-394
        recommendedProductCriteria.add(Restrictions.or(Restrictions.isNull("enabled"), Restrictions.eq("enabled", true)));

        // Fetch all products matching the criteria
        List<Product> products = recommendedProductCriteria.list();
        return products;
    }

    @Override
    // TODO: Change the param to Long once we have the Taxanomy api ready
    public Map<String, String> getCategories(Long parentID, String name, Long campaignID) {
        Map<String, String> categories = new HashMap<String, String>();
        try {
            Campaign campaign = campaignManager.getCampaign(campaignID, false);
            Taxonomy taxonomy = taxonomyRepository.getTaxonomy(campaign.getRetailerSite().getId(), "product");
            TaxonomyCategory taxonomyCategory = null;
            if (parentID == null || parentID == 0) {
                taxonomyCategory = taxonomyCategoryRepository.getRootCategory(taxonomy);
            } else {
                // taxonomyCategory = taxonomyCategoryRepository.getCategory()
                // Fetch the categories with parentID
                taxonomyCategory = taxonomyCategoryRepository.get(parentID);
            }
            for (TaxonomyCategory category : taxonomyCategory.getChildren()) {
                categories.put(category.getId().toString(), category.getName());
            }
        } catch (CampaignNotFoundException e) {
            logger.error("Unable to load the campaign id - " + campaignID);
        }
        return categories;
    }

    /**
     * Retrun the products which are under the specified category list
     *
     * @param searchTerm - Keyword to search in title
     * @param categories - List of categories from which the products needs to be filtered
     * @param siteID     - Retailer site ID
     * @return - List of filtered Products in a MAP representation
     */
    @Override
    public List<Map<String, String>> getFilteredProducts(String searchTerm, Collection<Long> categories, Long siteID) {
        Criteria criteria = getSession().createCriteria(Product.class);
        // Projections
        Projection idProjection = Projections.property("id").as("id");
        Projection titleProjection = Projections.property("title").as("title");
        Projection reviewsProjection = Projections.property("reviews").as("reviews");
        Projection starsProjection = Projections.property("stars").as("stars");
        Projection priceProjection = Projections.property("price").as("price");
        Projection listPriceProjection = Projections.property("listPrice").as("listPrice");
        criteria.setProjection(Projections.projectionList().add(idProjection).add(reviewsProjection).add(starsProjection).add(priceProjection).add(listPriceProjection).add(titleProjection));
        // Restrictions
        Criterion categoriesCriterion = null;
        Criterion searchTermCriterion = null;

        if (categories.size() > 0) {
            categoriesCriterion = Restrictions.in("category.id", categories);
        }
        if (StringUtils.isNotEmpty(searchTerm)) {
            searchTermCriterion = Restrictions.like("title", "%" + searchTerm + "%");
        }

        //fixme: Needs a  better approach
        if (categoriesCriterion != null && searchTermCriterion != null) {
            criteria.add(Restrictions.and(categoriesCriterion, searchTermCriterion));
        } else if (categoriesCriterion != null) {
            criteria.add(categoriesCriterion);
        } else if (searchTermCriterion != null) {
            criteria.add(searchTermCriterion);
        }

        criteria.add(Restrictions.eq("retailerSite.id", siteID));
        // Filter the disabled products - EXTERNALINTERACTIVEADS-394
        criteria.add(Restrictions.or(Restrictions.isNull("enabled"), Restrictions.eq("enabled", true)));

        criteria.addOrder(Order.desc("stars"));
        return mapProductToListMap(criteria.list());
    }

    /**
     * Retrun the products for a specific category
     *
     * @param categoryID - ID for which the products that should be returns
     * @return -
     */
    @Override
    public List<Map<String, String>> getFilteredItems(Long categoryID) {

        Criteria criteria = getSession().createCriteria(Product.class);
        // Projections
        Projection idProjection = Projections.property("id").as("id");
        Projection titleProjection = Projections.property("title").as("title");
        Projection reviewsProjection = Projections.property("reviews").as("reviews");
        Projection starsProjection = Projections.property("stars").as("stars");
        Projection priceProjection = Projections.property("price").as("price");
        Projection listPriceProjection = Projections.property("listPrice").as("listPrice");
        criteria.setProjection(Projections.projectionList().add(idProjection).add(reviewsProjection).add(starsProjection).add(priceProjection).add(listPriceProjection).add(titleProjection));
        // Restrictions
        criteria.add(Restrictions.eq("category.id", categoryID));
        criteria.addOrder(Order.desc("stars"));

        // Filter the disabled products - EXTERNALINTERACTIVEADS-394
        criteria.add(Restrictions.or(Restrictions.isNull("enabled"), Restrictions.eq("enabled", true)));

        return mapProductToListMap(criteria.list());
    }

    private List mapProductToListMap(List products) {
        List results = new ArrayList();
        //        id  - "1"
        //        reviews - "11"
        //        stars - "2.6"
        //        title - "Latitude ST Tablet"
        boolean skip = false;
        for (int i = 0; i < products.size(); i++) {
            skip = false;
            Map<String, String> dataMap = new HashMap<String, String>();
            Object[] arr = (Object[]) products.get(i);
            if (arr[0] != null) {
                dataMap.put("id", arr[0].toString());

                // If the product has a valid id then proceed with processing
                if (arr[1] != null) {
                    dataMap.put("reviews", arr[1].toString());
                } else {
                    dataMap.put("reviews", "0");
                }

                if (arr[2] != null) {
                    dataMap.put("stars", arr[2].toString());
                } else {
                    dataMap.put("stars", "0");
                }

                if (arr[3] != null) {
                    dataMap.put("price", arr[3].toString());
                } else {
                    dataMap.put("price", "0");
                }

                if (arr[4] != null) {
                    dataMap.put("listPrice", arr[4].toString());
                } else {
                    dataMap.put("listPrice", "0");
                }

                if (arr[5] != null) {
                    dataMap.put("title", arr[5].toString());
                } else {
                    skip = true;
                }

            } else {
                skip = true;
            }

            if (!skip) {
                results.add(dataMap);
            }

        }
        return results;
    }

    @Override
    public Collection<Product> getActiveSearchedProducts(String searchTerm, String merchantName,
                                                         int start, int maxProducts,
                                                         String filter, String orderBy) {


        // fix for https://jira.marketvine.com/browse/CS-248
        // fix for https://jira.marketvine.com/browse/CS-249

        // fixed by navinr
        // we could have searchTerm as empty to enable backward compatibility.
        // Assert.isTrue(searchTerm != null && !searchTerm.isEmpty(), "searchTerm is empty.");

        Criteria criteria = getSession().createCriteria(Product.class);
        if (searchTerm != null && !searchTerm.isEmpty()) {
            String[] searchColumnNames;
            if (filter == null) {
                searchColumnNames = new String[]{"title", "description", "specifications"};
            } else {
                searchColumnNames = filter.split(",");
            }

            LinkedList<Criterion> searchCriterions = new LinkedList<Criterion>();

            for (String searchColumn : searchColumnNames) {
                searchCriterions.add(Restrictions.ilike(searchColumn, "%" + searchTerm + "%"));
            }

            if (searchTerm.contains(" ")) {
                // ok, we have multiple words.
                // now do the splits

                for (String term : searchTerm.split(" ")) {
                    term = "%" + term + "%";
                    // we can enable multi-column split word search in the future.
                    //for (String searchColumn : searchColumnNames) {
                    searchCriterions.add(Restrictions.ilike("title", term));
                    //}
                }
            }

            // finally create the master searchTerm criterion.
            Criterion masterSearchTermCriterion = searchCriterions.removeLast();
            for (Criterion criterion : searchCriterions) {
                masterSearchTermCriterion = Restrictions.or(masterSearchTermCriterion, criterion);
            }
            criteria.add(masterSearchTermCriterion);
        }

        if (!StringUtils.isEmpty(merchantName)) {
            RetailerSite retailerSite = retailerManager.getRetailerSitebyName(merchantName);
            criteria.add(Restrictions.eq("retailerSite.id", retailerSite.getId()));
        }

        maxProducts = (maxProducts > 0 && maxProducts < 1000 ? maxProducts : 10);

        criteria.setFirstResult(start);
        criteria.setMaxResults(maxProducts);

        // Filter the disabled products - EXTERNALINTERACTIVEADS-394
        criteria.add(Restrictions.or(Restrictions.isNull("enabled"), Restrictions.eq("enabled", true)));

        // add orderBy clause
        // https://jira.marketvine.com/browse/CS-248
        if (orderBy != null && !orderBy.contains("merchant")) {
            // lets restrict the order by to the fields we have.
            String[] orderByParts = orderBy.split("-");
            boolean isAsc = true;
            if (orderByParts.length > 1) {
                orderBy = orderByParts[0];
                isAsc = (orderByParts[1] != null && orderByParts[1].equalsIgnoreCase("asc"));
            }

            String[] availableFields = sessionFactory.getClassMetadata(Product.class).getPropertyNames();
            if (ArrayUtils.indexOf(availableFields, orderBy) > -1) {
                // this is a field we know...
                if (isAsc) {
                    criteria.addOrder(Order.asc(orderBy));
                } else {
                    criteria.addOrder(Order.desc(orderBy));
                }
            }
        }

        // additional filtering to ensure return products are for active Retailers and RetailerSites
        criteria.createCriteria("retailerSite").add(Restrictions.eq("active", true));
        criteria.createCriteria("retailer").add(Restrictions.eq("active", true));

        return criteria.list();


        //searchTerm Criteria
        //        if (searchTerm != null) {
        //            Criteria searchProductCriteria = getSession().createCriteria(Product.class);
        //            maxProducts = (maxProducts != 0 ? maxProducts : 5);
        //            if (filter == null) {
        //                Criterion criterion1 = Restrictions.or(Restrictions.like("title", "%" + searchTerm + "%"),
        //                        Restrictions.like("description", "%" + searchTerm + "%"));
        //
        //                searchProductCriteria.add(Restrictions.or(criterion1,
        //                        Restrictions.like("specifications", "%" + searchTerm + "%")));
        //
        //                // Add metchangeName criteria if NOT NULL
        //                if (!StringUtils.isEmpty(merchantName)) {
        //                    RetailerSite retailerSite = retailerManager.getRetailerSitebyName(merchantName);
        //                    searchProductCriteria.add(Restrictions.eq("retailerSite.id", retailerSite.getId()));
        //                }
        //            } else {
        //                searchProductCriteria.add(Restrictions.like(filter, "%" + searchTerm + "%"));
        //            }
        //            searchProductCriteria.setFirstResult(start);
        //            searchProductCriteria.setMaxResults(maxProducts);
        //
        //            // Filter the disabled products - EXTERNALINTERACTIVEADS-394
        //            searchProductCriteria.add(Restrictions.or(Restrictions.isNull("enabled"),
        //                    Restrictions.eq("enabled", true)));
        //
        //            return searchProductCriteria.list();
        //        } else {
        //            throw new RuntimeException();
        //        }
    }

    @Override
    public long getProductReviewRating(final Product product) {
        long rating = 0L;
        final Object o = getSession().createCriteria(ProductReview.class).add(Restrictions.eq("product.id", product.getId())).uniqueResult();
        if (o != null) {
            ProductReview productReview = (ProductReview) o;
            rating = productReview.getComputedWeight().longValue();
        } else {
            rating = 0L;
        }
        return rating;
    }

    @Override
    public List<ProductReview> getTopProductReview(final Product product, final int numOfReviews) {
        List list = null;
        try {
            list = getSession().createCriteria(ProductReview.class).add(Restrictions.eq("product.id", product.getId())).addOrder(Order.desc("computedWeight")).setMaxResults(numOfReviews).list();
        } catch (Exception e) {
            logger.warn("ProductRepositoryImpl  getTopProductReview() " + e.getMessage());
            throw new RuntimeException("Unable to Retrieval Product Review ", e);
        }
        if (list != null) {
            return list;
        } else {
            throw new RuntimeException("Empty Product Review List ");
        }
    }

    @Override
    public List<Product> getProductsForRetailerSite(final RetailerSite retailerSite) {
        return getSession().createCriteria(Product.class).add(Restrictions.eq("retailerSite.id", retailerSite.getId())).list();
    }

    @Override
    public Collection<Product> getProductForRetailerSite(Long retailerSiteID, int start, int maxProducts) {
        Criteria productCriteria = getSession().createCriteria(Product.class);

        productCriteria.add(Restrictions.eq("retailerSite.id", retailerSiteID));
        productCriteria.setFirstResult(start);
        // To get all products if "maxProducts" value is set to 0
        if (maxProducts > 0) {
            //Setting MaxProduct, if maxProducts is zero, then we set maxproducts to 5
            maxProducts = (maxProducts != 0 ? maxProducts : 5);
            productCriteria.setMaxResults(maxProducts);
        }
        // Filter the disabled products - EXTERNALINTERACTIVEADS-394
        productCriteria.add(Restrictions.or(Restrictions.isNull("enabled"), Restrictions.eq("enabled", true)));

        // Filter for those owned by active retailers
        productCriteria.createCriteria("retailer").add(Restrictions.eq("active", true));

        return productCriteria.list();
    }

    @Override
    public Collection<Product> getAllProductForRetailerSite(Long retailerSiteID, int start, int maxProducts) {
        Criteria productCriteria = getSession().createCriteria(Product.class);

        productCriteria.add(Restrictions.eq("retailerSite.id", retailerSiteID));
        productCriteria.setFirstResult(start);
        // To get all products if "maxProducts" value is set to 0
        if (maxProducts > 0) {
            //Setting MaxProduct, if maxProducts is zero, then we set maxproducts to 5
            maxProducts = (maxProducts != 0 ? maxProducts : 5);
            productCriteria.setMaxResults(maxProducts);
        }
        //Commited the below code to support enabling/disabling products.
        // Filter the disabled products - EXTERNALINTERACTIVEADS-394
        //productCriteria.add(Restrictions.or(Restrictions.isNull("enabled"), Restrictions.eq("enabled", true)));

        // Filter for those owned by active retailers
        productCriteria.createCriteria("retailer").add(Restrictions.eq("active", true));

        return productCriteria.list();
    }

    @Override
    public Collection<Product> getProductForRetailerSite(RetailerSite retailerSite, int start, int maxProducts) {
        if (retailerSite != null) {
            return getProductForRetailerSite(retailerSite.getId(), start, maxProducts);
        } else {
            throw new RuntimeException();
        }

    }

    @Override
    public Integer getTotalProductCountForRetailerSite(RetailerSite retailerSite) {
        return getSession().createCriteria(Product.class).add(Restrictions.eq("retailerSite.id", retailerSite.getId())).list().size();
    }

    @Override
    public Collection<ProductSearchModel> getSearchedProductSiteNames(String searchTerm, String merchantName,
                                                                      Integer start, Integer maxProducts,
                                                                      String filter) {

        //searchTerm Criteria
        if (searchTerm != null) {

            Criteria searchProductCriteria = getSession().createCriteria(Product.class);
            Criteria searchRetailerCriteria = searchProductCriteria.setFetchMode("retailer", FetchMode.JOIN);

            applyGenericCriteria(searchProductCriteria, null);

            maxProducts = (maxProducts != 0 ? maxProducts : 5);
            if (filter == null) {
                Criterion criterion1 = Restrictions.or(Restrictions.like("title", "%" + searchTerm + "%"), Restrictions.like("description", "%" + searchTerm + "%"));

                searchProductCriteria.add(Restrictions.or(criterion1, Restrictions.like("specifications", "%" + searchTerm + "%")));

                // Add metchangeName criteria if NOT NULL
                if (!StringUtils.isEmpty(merchantName)) {
                    RetailerSite retailerSite = retailerManager.getRetailerSitebyName(merchantName);
                    searchProductCriteria.add(Restrictions.eq("retailerSite.id", retailerSite.getId()));
                }
            } else {
                searchProductCriteria.add(Restrictions.like(filter, "%" + searchTerm + "%"));
            }

            // Filter the disabled products - EXTERNALINTERACTIVEADS-394
            searchProductCriteria.add(Restrictions.or(Restrictions.isNull("enabled"), Restrictions.eq("enabled", true)));
            ProjectionList projectionList = Projections.projectionList();
            projectionList.add(Projections.rowCount());
            projectionList.add(Projections.groupProperty("siteName"));
            searchRetailerCriteria.setProjection(projectionList);

            List<Object[]> retailerInfos = searchRetailerCriteria.list();
            Collection<ProductSearchModel> result = new ArrayList<ProductSearchModel>(retailerInfos.size());

            for (Object[] retailerInfo : retailerInfos) {
                ProductSearchModel psm = new ProductSearchModel();
                psm.setTotalCount(((Long) retailerInfo[0]).intValue());
                psm.setSiteName((String) retailerInfo[1]);
                result.add(psm);
            }

            return result;
        } else {
            throw new RuntimeException();
        }
    }

    // feature fix for https://jira.marketvine.com/browse/CS-248
    // I am not using scrollable resultset as MySQL connector just fackes it and loads the entire resultSet in memory.
    @Override
    @SuppressWarnings("unchecked")
    public void computeMissingProductWeights(final int limitOfBatch) {
        Session session = getSession();
        Collection<Product> products = session.createCriteria(Product.class).add(Restrictions.isNull("weight")).setFirstResult(0).setMaxResults(limitOfBatch).setReadOnly(false).setLockMode(LockMode.PESSIMISTIC_WRITE).setFetchSize(limitOfBatch).list();

        for (Product product : products) {
            float computedWeight = ProductManagerImpl.computeProductWeight(product);
            product.setWeight(computedWeight);
            session.merge(product);
            onUpdate(product);
        }
    }

    @Transactional
    public Long getMissingProductWeightsCount() {
        final Criteria criteria = getSession().createCriteria(entityClass);
        criteria.add(Restrictions.isNull("weight"));
        criteria.setProjection(Projections.rowCount()).uniqueResult();
        return (Long) criteria.uniqueResult();

    }

    @Override
    public Collection<Product> getProducts(ServiceFilterBean filterBean, String merchantName) {
        final Criteria criteria = getSession().createCriteria(Product.class);
        //apply Generic Criteria
        applyGenericCriteria(criteria, filterBean);

        if (!StringUtils.isEmpty(merchantName)) {
            RetailerSite retailerSite = retailerManager.getRetailerSitebyName(merchantName);
            criteria.add(Restrictions.eq("retailerSite.id", retailerSite.getId()));
        }
        // Filter the disabled products - EXTERNALINTERACTIVEADS-394
        criteria.add(Restrictions.or(Restrictions.isNull("enabled"), Restrictions.eq("enabled", true)));
        //Active Retailer & RetailerSite
        criteria.createCriteria("retailerSite").add(Restrictions.eq("active", true));
        criteria.createCriteria("retailer").add(Restrictions.eq("active", true));

        return criteria.list();
    }

    @Override
    public Collection<Product> getProducts(String columnName, Object columnValue, Integer start, Integer maxProducts){
        final Criteria criteria = getSession().createCriteria(Product.class);
        criteria.add(Restrictions.isNull(columnName));
        criteria.setFirstResult(start);
        criteria.setMaxResults(maxProducts);
        return (Collection<Product>) criteria.list();
    }

    @Override
    public Collection<ProductReview> getTopProductReview(Product product, ServiceFilterBean filterBean) {
        Criteria criteria = getSession().createCriteria(ProductReview.class);
        //Apply Generic Criteria
        applyGenericCriteria(criteria, filterBean);
        criteria.add(Restrictions.eq("product.id", product.getId())).addOrder(Order.desc("computedWeight"));
        return criteria.list();
    }
}
