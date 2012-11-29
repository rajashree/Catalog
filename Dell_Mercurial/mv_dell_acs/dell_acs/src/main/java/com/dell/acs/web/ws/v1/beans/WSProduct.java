/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserve   d.
 */

package com.dell.acs.web.ws.v1.beans;

import com.sourcen.core.util.beans.FieldMapping;
import com.sourcen.core.util.beans.Scope;
import com.sourcen.core.util.beans.ScopeAware;
import com.sourcen.core.util.beans.Scopes;
import com.sourcen.core.web.ws.beans.base.WSPropertiesAwareBeanModel;

import java.util.Collection;
import java.util.Date;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: vivekk $
 * @version $Revision: 3036 $, $Date:: 2012-06-08 11:33:16#$
 */
@Scopes({
        @Scope(name = "search", fields = {"id", "title", "images"}),
        @Scope(name = "minimal", fields = {"id", "title","description", "rating", "url", "listPrice", "price", "images","siteName"}),
        @Scope(name = "WSProductSlider.targetProduct", fields = {"id", "title", "price", "url"})
})
public class WSProduct extends WSPropertiesAwareBeanModel implements ScopeAware {


    Long id;
    WSRetailer retailer;
    //@FieldMapping("title")
    String title;
    Collection<WSProductSlider> sliders;
    Collection<WSProductImage> images;
    String description;
    Float price;
    @FieldMapping("stars")
    Float rating;
    Integer reviews;
    String reviewsLink;
    String infoLink;
    Float listPrice;
    Boolean hasListPriceRange;
    Boolean hasPriceRange;
    Boolean hasVariations;
    String estimatedShipDate;
    Integer facebookLikes;
    Integer plusOneGoogle;
    Integer tweets;
    String promotional;
    String shippingPromotion;
    String buyLink;
    String flashLink;
    String specifications;
    String url;
    String siteName;
    String productId;
    String sku;
    String webPartNumber;
    Boolean newProduct;
    Boolean bestSeller;
    Boolean clearanceTag;
    Boolean saleTag;
    Boolean priceCutTag;
    Boolean tempPriceCutTag;
    Date updateDateTime;
    Collection<WSProductReview> productReviews;
    // private WSTaxonomyCategory category;
    //Flag used in transformers to determine whether to show Product Categories or not
    Boolean showCategory = true;
    Boolean enabled;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WSRetailer getRetailer() {
        return retailer;
    }

    public void setRetailer(WSRetailer retailer) {
        this.retailer = retailer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Collection<WSProductSlider> getSliders() {
        return sliders;
    }

    public void setSliders(Collection<WSProductSlider> sliders) {
        this.sliders = sliders;
    }

    public Collection<WSProductImage> getImages() {
        return images;
    }

    public void setImages(Collection<WSProductImage> images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Integer getReviews() {
        return reviews;
    }

    public void setReviews(Integer reviews) {
        this.reviews = reviews;
    }

    public String getReviewsLink() {
        return reviewsLink;
    }

    public void setReviewsLink(String reviewsLink) {
        this.reviewsLink = reviewsLink;
    }

    public String getInfoLink() {
        return infoLink;
    }

    public void setInfoLink(String infoLink) {
        this.infoLink = infoLink;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Date getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(Date updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public Collection<WSProductReview> getProductReviews() {
        return productReviews;
    }

    public void setProductReviews(Collection<WSProductReview> productReviews) {
        this.productReviews = productReviews;
    }

    /* public WSTaxonomyCategory getCategory() {
        return category;
    }

    public void setCategory(WSTaxonomyCategory category) {
        this.category = category;
    }*/

    public Boolean getShowCategory() {
        return showCategory;
    }

    public void setShowCategory(Boolean showCategory) {
        this.showCategory = showCategory;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "WSProduct{" +
                "id=" + id +
                ", retailer=" + retailer +
                ", title='" + title + '\'' +
                ", sliders=" + sliders +
                ", images=" + images +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", rating=" + rating +
                ", reviews=" + reviews +
                ", reviewsLink='" + reviewsLink + '\'' +
                ", infoLink='" + infoLink + '\'' +
                ", listPrice=" + listPrice +
                ", hasListPriceRange=" + hasListPriceRange +
                ", hasPriceRange=" + hasPriceRange +
                ", hasVariations=" + hasVariations +
                ", estimatedShipDate='" + estimatedShipDate + '\'' +
                ", facebookLikes=" + facebookLikes +
                ", plusOneGoogle=" + plusOneGoogle +
                ", tweets=" + tweets +
                ", promotional='" + promotional + '\'' +
                ", shippingPromotion='" + shippingPromotion + '\'' +
                ", buyLink='" + buyLink + '\'' +
                ", flashLink='" + flashLink + '\'' +
                ", specifications='" + specifications + '\'' +
                ", url='" + url + '\'' +
                ", siteName='" + siteName + '\'' +
                ", productId='" + productId + '\'' +
                ", sku='" + sku + '\'' +
                ", webPartNumber='" + webPartNumber + '\'' +
                ", newProduct=" + newProduct +
                ", bestSeller=" + bestSeller +
                ", clearanceTag=" + clearanceTag +
                ", saleTag=" + saleTag +
                ", priceCutTag=" + priceCutTag +
                ", tempPriceCutTag=" + tempPriceCutTag +
                ", updateDateTime=" + updateDateTime +
                ", productReviews=" + productReviews +
                ", showCategory=" + showCategory +
                ", enabled=" + enabled +
                '}';
    }
}
