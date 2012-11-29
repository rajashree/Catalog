/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.controller.formbeans;

import com.dell.acs.persistence.domain.Document;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.sourcen.core.persistence.domain.Entity;
import com.sourcen.core.util.beans.BeanConverter;
import com.sourcen.core.util.beans.MappingContext;
import com.sourcen.core.util.collections.PropertiesProvider;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 @author Navin Raj Kumar G.S.
 @author $LastChangedBy: ashish $
 @version $Revision: 3250 $, $Date:: 2012-06-18 08:09:42#$ */
public class FormBeanConverter {

    private static final BeanConverter<Entity, FormBean> ENTITY_TO_BEAN_CONVERTER =
            new BeanConverter<Entity, FormBean>(Entity.class, FormBean.class, new FormBeanPropertyConverter());

    private static final BeanConverter<FormBean, Entity> BEAN_TO_ENTITY_CONVERTER =
            new BeanConverter<FormBean, Entity>(FormBean.class, Entity.class, new FormBeanPropertyConverter());


    static {
        BEAN_TO_ENTITY_CONVERTER.registerAlias(RetailerBean.class, Retailer.class);
        BEAN_TO_ENTITY_CONVERTER.registerAlias(RetailerSiteBean.class, RetailerSite.class);
        //BEAN_TO_ENTITY_CONVERTER.registerAlias(EventBean.class, Event.class);
        BEAN_TO_ENTITY_CONVERTER.registerAlias(DocumentBean.class, Document.class);
    }


    // Entity to FormBean
    public static <T extends FormBean> T convert(Entity input, T output) {
        return convert(input, output, new MappingContext());
    }

    public static <T extends FormBean> T convert(Entity input, T output, MappingContext mappingContext) {
        return ENTITY_TO_BEAN_CONVERTER.convert(input, output, mappingContext);
    }

    public static <T extends FormBean> Collection<T> convertToBeans(Collection<? extends Entity> input, Class outputClass) {
        return convertToBeans(input, outputClass, new MappingContext());
    }

    public static <T extends FormBean> Collection<T> convertToBeans(Collection<? extends Entity> input, Class outputClass, MappingContext mappingContext) {
        return ENTITY_TO_BEAN_CONVERTER.convert(input, outputClass, mappingContext);
    }


    // FormBean to Entity
    public static <T extends Entity> T convert(FormBean input, T output) {
        return convert(input, output, new MappingContext("all"));
    }

    public static <T extends Entity> T convert(FormBean input, T output, MappingContext mappingContext) {
        return BEAN_TO_ENTITY_CONVERTER.convert(input, output, mappingContext);
    }

    public static <T extends Entity> Collection<T> convertToEntities(Collection<? extends FormBean> input, Class outputClass) {
        return convertToEntities(input, outputClass, new MappingContext("all"));
    }

    public static <T extends Entity> Collection<T> convertToEntities(Collection<? extends FormBean> input, Class outputClass, MappingContext mappingContext) {
        return BEAN_TO_ENTITY_CONVERTER.convert(input, outputClass, mappingContext);
    }


    // FormBean to Entity.
//
//    public static <T extends Entity> T convert(FormBean input, T output) {
//        return convert(input, output, new MappingContext());
//    }
//
//    public static <T extends Entity> T convert(FormBean input, T output, MappingContext mappingContext) {
//        return BEAN_TO_ENTITY_CONVERTER.convert(input, output, mappingContext);
//    }
//
//    public static <T extends Entity> Collection<T> convert(Collection<? extends FormBean> input, Class outputClass) {
//        return convert(input, outputClass, new MappingContext());
//    }
//
//    public static <T extends Entity> Collection<T> convert(Collection<? extends FormBean> input, Class outputClass, MappingContext mappingContext) {
//        return BEAN_TO_ENTITY_CONVERTER.convert(input, outputClass, mappingContext);
//    }


    private static final class FormBeanPropertyConverter implements BeanConverter.ValueConverter {

        @Override
        public Object convert(final Object value) {
            if (value instanceof PropertiesProvider) {
                PropertiesProvider propertiesProvider = (PropertiesProvider) value;
                Set<String> keys = propertiesProvider.keySet();
                Collection<FormBeanProperty> finalProperties = new LinkedHashSet<FormBeanProperty>();
                for (String key : keys) {
                    if (!key.startsWith("__")) { // expose public properties.
                        finalProperties.add(new FormBeanProperty(key, propertiesProvider.getProperty(key)));
                    }
                }
                return finalProperties;
            }
            return value;
        }
    }


}
