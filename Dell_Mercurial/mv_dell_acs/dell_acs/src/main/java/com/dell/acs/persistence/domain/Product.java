/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.PropertiesAwareEntity;
import com.sourcen.core.persistence.domain.constructs.jpa.PropertiesAware;
import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;
import com.sourcen.core.util.beans.Scope;
import com.sourcen.core.util.beans.Scopes;
import com.sourcen.core.util.collections.PropertiesProvider;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Collection;
import java.util.Date;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 3431 $, $Date:: 2012-06-20 14:14:11#$
 */
//sfisk - CS-380
@Table(name = "t_product")
@org.hibernate.annotations.Table(appliesTo = "t_product",
        indexes = {@Index(name = "idx_weight", columnNames = {"weight"})})
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
// field scopes
@Scopes({
        @Scope(name = "id", fields = {"id"}),
        @Scope(name = "search", fields = {"id", "title", "images","listPrice"}),
                @Scope(name = "default", fields = {"id","retailer","title","sliders","images","description","price","rating","reviews",
                        "reviewsLink","infoLink","listPrice","hasListPriceRange","hasPriceRange","hasVariations","estimatedShipDate",
                        "facebookLikes","plusOneGoogle","tweets","promotional","shippingPromotion","buyLink","flashLink","specifications",
                        "url","siteName","productId","sku","webPartNumber","newProduct","bestSeller","clearanceTag","saleTag","priceCutTag",
                        "tempPriceCutTag","updateDateTime"}),

                @Scope(name = "minimal", fields = {"id", "title","description", "rating", "url", "listPrice", "price", "images","siteName"})
        })
public class Product extends IdentifiableEntityModel<Long> implements PropertiesAwareEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Retailer.class, optional = true)
    @Fetch(FetchMode.JOIN)
    @Scope(name = "id")
    private Retailer retailer;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = TaxonomyCategory.class, optional = true)
    @Fetch(FetchMode.SELECT)
    private TaxonomyCategory category;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = true)
    @Fetch(FetchMode.JOIN)
    @Scope(name = "id")
    private RetailerSite retailerSite;

    @Column(length = 255, nullable = false)
    private String siteName;

    @Column(length = 255, nullable = false)
    private String productId;

    @Column(length = 1000, nullable = true)
    private String url;

    @Column(length = 255, nullable = true)
    private String sku;

    @Column(length = 255, nullable = true)
    private String webPartNumber;

    @Column(length = 255, nullable = true)
    private String title;

    @Column(nullable = true)
    private Boolean clearanceTag;

    @Column(nullable = true)
    private Boolean saleTag;

    @Column(nullable = true)
    private Boolean priceCutTag;

    @Column(nullable = true)
    private Boolean tempPriceCutTag;

    @Column(nullable = true)
    private Boolean newProduct;

    @Column(nullable = true)
    private Boolean bestSeller;

    @Column(length = 4000, nullable = true)
    private String description;

    @Column(nullable = true)
    private Float stars;

    @Column(nullable = true)
    private Integer reviews;

    @Column(length = 1000, nullable = true)
    private String reviewsLink;

    @Column(length = 1000, nullable = true)
    private String infoLink;

    @Column(nullable = true)
    private Float listPrice;

    @Column(nullable = true)
    private Boolean hasListPriceRange;

    @Column(nullable = true)
    private Float price;

    @Column(nullable = true)
    private Boolean hasPriceRange;

    @Column(nullable = true)
    private Boolean hasVariations;

    @Column(length = 255, nullable = true)
    private String estimatedShipDate;

    @Column(nullable = true)
    private Integer facebookLikes;

    @Column(nullable = true)
    private Integer plusOneGoogle;

    @Column(nullable = true)
    private Integer tweets;

    @Column(length = 4000, nullable = true)
    private String promotional;

    @Column(length = 4000, nullable = true)
    private String shippingPromotion;

    @Column(length = 1000, nullable = true)
    private String buyLink;

    @Column(length = 1000, nullable = true)
    private String flashLink;

    @Column(length = 4000, nullable = true)
    private String specifications;

    @Column(nullable = true)
    private Boolean enabled;

    @Column(nullable = true)
    private Integer availability;

    // adding weight column, this can be null for non computed fields.
    // https://jira.marketvine.com/browse/CS-248
    @Column(nullable = true)
    private Float weight;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            targetEntity = ProductReview.class,
            mappedBy = "product")
    @Fetch(FetchMode.SELECT)
    private Collection<ProductReview> productReviews;

    public static enum Availability {
        OUT_OF_STOCK(0), IN_STOCK(1), AVAILABLE_FOR_ORDER(2), PREORDER(3);

        private int value;

        Availability(final int value) {
            this.value = value;
        }

        @Deprecated
        public int getValue() {
            return value;
        }

        public int getDbValue() {
            return value;
        }
    }

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updateDateTime;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class, optional = true)
    @Fetch(FetchMode.SELECT)
    @Scope(name = "id")
    private User createdBy;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)

    private Date createdDate;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class, optional = true)
    @Fetch(FetchMode.SELECT)
    @Scope(name = "id")
    private User modifiedBy;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @Column(nullable = true)
    private String searchItemHash;

    @OneToMany(fetch = FetchType.LAZY,
/*            cascade = CascadeType.ALL,*/
            targetEntity = ProductImage.class,
            mappedBy = "product")
    @Fetch(FetchMode.SELECT)
    private Collection<ProductImage> images;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            targetEntity = ProductSlider.class,
            mappedBy = "sourceProduct")
    @Fetch(FetchMode.SELECT)
    @Scope(name = "minimal")
    private Collection<ProductSlider> sliders;

    @Embedded
    private PropertiesAware properties;

    @Override
    public PropertiesProvider getProperties() {
        if (properties == null) {
            properties = new PropertiesAware();
        }
        return properties.getProperties();
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(final Float weight) {
        this.weight = weight;
    }

    public Collection<ProductSlider> getSliders() {
        return sliders;
    }

    public void setSliders(Collection<ProductSlider> sliders) {
        this.sliders = sliders;
    }

    public Collection<ProductImage> getImages() {
        return images;
    }

    public void setImages(Collection<ProductImage> images) {
        this.images = images;
    }

    public TaxonomyCategory getCategory() {
        return category;
    }

    public void setCategory(TaxonomyCategory category) {
        this.category = category;
    }

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }

    public RetailerSite getRetailerSite() {
        return retailerSite;
    }

    public void setRetailerSite(RetailerSite retailerSite) {
        this.retailerSite = retailerSite;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getWebPartNumber() {
        return webPartNumber;
    }

    public void setWebPartNumber(String webPartNumber) {
        this.webPartNumber = webPartNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getClearanceTag() {
        return clearanceTag;
    }

    public void setClearanceTag(Boolean clearanceTag) {
        this.clearanceTag = clearanceTag;
    }

    public Boolean getSaleTag() {
        return saleTag;
    }

    public void setSaleTag(Boolean saleTag) {
        this.saleTag = saleTag;
    }

    public Boolean getPriceCutTag() {
        return priceCutTag;
    }

    public void setPriceCutTag(Boolean priceCutTag) {
        this.priceCutTag = priceCutTag;
    }

    public Boolean getTempPriceCutTag() {
        return tempPriceCutTag;
    }

    public void setTempPriceCutTag(Boolean tempPriceCutTag) {
        this.tempPriceCutTag = tempPriceCutTag;
    }

    public Boolean getNewProduct() {
        return newProduct;
    }

    public void setNewProduct(Boolean newProduct) {
        this.newProduct = newProduct;
    }

    public Boolean getBestSeller() {
        return bestSeller;
    }

    public void setBestSeller(Boolean bestSeller) {
        this.bestSeller = bestSeller;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getStars() {
        return stars;
    }

    public void setStars(Float stars) {
        this.stars = stars;
    }

    public Integer getReviews() {
        return reviews;
    }

    public void setReviews(Integer reviews) {
        this.reviews = reviews;
    }

    public String getInfoLink() {
        return infoLink;
    }

    public void setInfoLink(String infoLink) {
        this.infoLink = infoLink;
    }

    public String getReviewsLink() {
        return reviewsLink;
    }

    public void setReviewsLink(String reviewsLink) {
        this.reviewsLink = reviewsLink;
    }

    public Float getListPrice() {
        return listPrice;
    }

    public void setListPrice(Float listPrice) {
        this.listPrice = listPrice;
    }

    public Boolean getHasListPriceRange() {
        return hasListPriceRange;
    }

    public void setHasListPriceRange(Boolean hasListPriceRange) {
        this.hasListPriceRange = hasListPriceRange;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Boolean getHasPriceRange() {
        return hasPriceRange;
    }

    public void setHasPriceRange(Boolean hasPriceRange) {
        this.hasPriceRange = hasPriceRange;
    }

    public Boolean getHasVariations() {
        return hasVariations;
    }

    public void setHasVariations(Boolean hasVariations) {
        this.hasVariations = hasVariations;
    }

    public String getEstimatedShipDate() {
        return estimatedShipDate;
    }

    public void setEstimatedShipDate(String estimatedShipDate) {
        this.estimatedShipDate = estimatedShipDate;
    }

    public Integer getFacebookLikes() {
        return facebookLikes;
    }

    public void setFacebookLikes(Integer facebookLikes) {
        this.facebookLikes = facebookLikes;
    }

    public Integer getPlusOneGoogle() {
        return plusOneGoogle;
    }

    public void setPlusOneGoogle(Integer plusOneGoogle) {
        this.plusOneGoogle = plusOneGoogle;
    }

    public Integer getTweets() {
        return tweets;
    }

    public void setTweets(Integer tweets) {
        this.tweets = tweets;
    }

    public String getPromotional() {
        return promotional;
    }

    public void setPromotional(String promotional) {
        this.promotional = promotional;
    }

    public String getShippingPromotion() {
        return shippingPromotion;
    }

    public void setShippingPromotion(String shippingPromotion) {
        this.shippingPromotion = shippingPromotion;
    }

    public String getBuyLink() {
        return buyLink;
    }

    public void setBuyLink(String buyLink) {
        this.buyLink = buyLink;
    }

    public String getFlashLink() {
        return flashLink;
    }

    public void setFlashLink(String flashLink) {
        this.flashLink = flashLink;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public Date getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(Date updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        Date modifiedDate1 = modifiedDate;
        return modifiedDate1;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getAvailability() {
        return availability;
    }

    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

    public Collection<ProductReview> getProductReviews() {
        return productReviews;
    }

    public void setProductReviews(Collection<ProductReview> productReviews) {
        this.productReviews = productReviews;
    }

    public String getSearchItemHash() {
        return searchItemHash;
    }

    public void setSearchItemHash(String searchItemHash) {
        this.searchItemHash = searchItemHash;
    }
}
