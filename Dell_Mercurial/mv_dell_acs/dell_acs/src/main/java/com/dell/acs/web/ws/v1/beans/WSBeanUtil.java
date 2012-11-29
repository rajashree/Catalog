/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.beans;

import com.dell.acs.managers.ContentFilterBean;
import com.dell.acs.persistence.domain.AdPublisher;
import com.dell.acs.persistence.domain.Campaign;
import com.dell.acs.persistence.domain.CampaignCategory;
import com.dell.acs.persistence.domain.CampaignItem;
import com.dell.acs.persistence.domain.Document;
import com.dell.acs.persistence.domain.Event;
import com.dell.acs.persistence.domain.Product;
import com.dell.acs.persistence.domain.ProductImage;
import com.dell.acs.persistence.domain.ProductReview;
import com.dell.acs.persistence.domain.ProductSlider;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.domain.TaxonomyCategory;
import com.sourcen.core.persistence.domain.Entity;
import com.sourcen.core.util.beans.BeanConverter;
import com.sourcen.core.util.beans.MappingContext;
import com.sourcen.core.util.collections.PropertiesProvider;
import com.sourcen.core.web.ws.beans.WSProperty;
import com.sourcen.core.web.ws.beans.base.WSBean;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public final class WSBeanUtil {

    private static final BeanConverter<Entity, WSBean> BEAN_CONVERTER = new BeanConverter<Entity, WSBean>(Entity.class, WSBean.class, new WSPropertyValueConverter());

    static {
        BEAN_CONVERTER.registerAlias(AdPublisher.class, WSAdPublisher.class);
        BEAN_CONVERTER.registerAlias(Product.class, WSProduct.class);
        BEAN_CONVERTER.registerAlias(ProductSlider.class, WSProductSlider.class);
        BEAN_CONVERTER.registerAlias(ProductImage.class, WSProductImage.class);
        BEAN_CONVERTER.registerAlias(ProductReview.class, WSProductReview.class);
        BEAN_CONVERTER.registerAlias(Retailer.class, WSRetailer.class);
        BEAN_CONVERTER.registerAlias(RetailerSite.class, WSRetailerSite.class);
        BEAN_CONVERTER.registerAlias(CampaignItem.class, WSCampaignItem.class);
        BEAN_CONVERTER.registerAlias(Campaign.class, WSCampaign.class);
        BEAN_CONVERTER.registerAlias(CampaignCategory.class, WSCampaignCategory.class);
        BEAN_CONVERTER.registerAlias(ProductReview.class, WSProductReview.class);
        BEAN_CONVERTER.registerAlias(TaxonomyCategory.class, WSTaxonomyCategory.class);
        BEAN_CONVERTER.registerAlias(Event.class, WSEvent.class);
        BEAN_CONVERTER.registerAlias(Document.class, WSDocument.class);


        // reverse mapping
        BEAN_CONVERTER.registerAlias(WSProductFilter.class, ContentFilterBean.class);
    }

    public static <T extends WSBean> T convert(Entity input, T output) {
        return convert(input, output, new MappingContext());
    }

    public static <T extends WSBean> T convert(Entity input, T output, MappingContext mappingContext) {
        return BEAN_CONVERTER.convert(input, output, mappingContext);
    }

    public static <T extends WSBean> Collection<T> convert(Collection<? extends Entity> input, Class outputClass) {
        return convert(input, outputClass, new MappingContext());
    }

    public static <T extends WSBean> Collection<T> convert(Collection<? extends Entity> input, Class outputClass, MappingContext mappingContext) {
        return BEAN_CONVERTER.convert(input, outputClass, mappingContext);
    }

    private static final class WSPropertyValueConverter implements BeanConverter.ValueConverter {
        @Override
        public Object convert(final Object value) {
            if (value instanceof PropertiesProvider) {
                PropertiesProvider propertiesProvider = (PropertiesProvider) value;
                Set<String> keys = propertiesProvider.keySet();
                Collection<WSProperty> finalProperties = new LinkedHashSet<WSProperty>();
                for (String key : keys) {
                    if (!key.startsWith("__")) { // expose public properties.
                        finalProperties.add(new WSProperty(key, propertiesProvider.getProperty(key)));
                    }
                }
                return finalProperties;
            }
            return value;
        }
    }


}
