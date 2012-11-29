package com.dell.acs.managers.model;

import com.dell.acs.persistence.domain.*;
import com.sourcen.core.persistence.domain.Entity;
import com.sourcen.core.util.beans.BeanConverter;
import com.sourcen.core.util.beans.MappingContext;
import com.sourcen.core.util.collections.PropertiesProvider;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: ashish $
 * @version $Revision: 3250 $, $Date:: 2012-06-18 08:09:42#$
 */
public class FormDataConverter {

    private static final BeanConverter<Entity, FormData> ENTITY_TO_BEAN_CONVERTER =
            new BeanConverter<Entity, FormData>(Entity.class, FormData.class, new FormBeanPropertyConverter());

    private static final BeanConverter<FormData, Entity> BEAN_TO_ENTITY_CONVERTER =
            new BeanConverter<FormData, Entity>(FormData.class, Entity.class, new FormBeanPropertyConverter());


    static {
        BEAN_TO_ENTITY_CONVERTER.registerAlias(ArticleData.class, Article.class);
        BEAN_TO_ENTITY_CONVERTER.registerAlias(RetailerSiteData.class, RetailerSite.class);
        BEAN_TO_ENTITY_CONVERTER.registerAlias(RetailerData.class, Retailer.class);
        BEAN_TO_ENTITY_CONVERTER.registerAlias(CurationData.class, Curation.class);
        BEAN_TO_ENTITY_CONVERTER.registerAlias(UserData.class, User.class);
        BEAN_TO_ENTITY_CONVERTER.registerAlias(DocumentData.class, Document.class);
        BEAN_TO_ENTITY_CONVERTER.registerAlias(UserRoleData.class, UserRole.class);
        BEAN_TO_ENTITY_CONVERTER.registerAlias(CurationSourceData.class, CurationSource.class);

        ENTITY_TO_BEAN_CONVERTER.registerAlias(Curation.class,CurationData.class);
        ENTITY_TO_BEAN_CONVERTER.registerAlias(Retailer.class, RetailerData.class);
        ENTITY_TO_BEAN_CONVERTER.registerAlias(RetailerSite.class, RetailerSiteData.class);
        ENTITY_TO_BEAN_CONVERTER.registerAlias(Document.class, DocumentData.class);
        ENTITY_TO_BEAN_CONVERTER.registerAlias(User.class, UserData.class);
        ENTITY_TO_BEAN_CONVERTER.registerAlias(UserRole.class, UserRoleData.class);
        ENTITY_TO_BEAN_CONVERTER.registerAlias(Article.class, ArticleData.class);
        ENTITY_TO_BEAN_CONVERTER.registerAlias(CurationSource.class,CurationSourceData.class);
    }


    // Entity to FormBean
    public static <T extends FormData> T convert(Entity input, T output) {
        return convert(input, output, new MappingContext());
    }

    public static <T extends FormData> T convert(Entity input, T output, MappingContext mappingContext) {
        return ENTITY_TO_BEAN_CONVERTER.convert(input, output, mappingContext);
    }

    public static <T extends FormData> Collection<T> convertToBeans(Collection<? extends Entity> input, Class outputClass) {
        return convertToBeans(input, outputClass, new MappingContext());
    }

    public static <T extends FormData> Collection<T> convertToBeans(Collection<? extends Entity> input, Class outputClass, MappingContext mappingContext) {
        return ENTITY_TO_BEAN_CONVERTER.convert(input, outputClass, mappingContext);
    }


    // FormBean to Entity
    public static <T extends Entity> T convert(FormData input, T output) {
        return convert(input, output, new MappingContext());
    }

    public static <T extends Entity> T convert(FormData input, T output, MappingContext mappingContext) {
        return BEAN_TO_ENTITY_CONVERTER.convert(input, output, mappingContext);
    }

    public static <T extends Entity> Collection<T> convertToEntities(Collection<? extends FormData> input, Class outputClass) {
        return convertToEntities(input, outputClass, new MappingContext());
    }

    public static <T extends Entity> Collection<T> convertToEntities(Collection<? extends FormData> input, Class outputClass, MappingContext mappingContext) {
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
                Collection<FormDataProperty> finalProperties = new LinkedHashSet<FormDataProperty>();
                for (String key : keys) {
                    if (!key.startsWith("__")) { // expose public properties.
                        finalProperties.add(new FormDataProperty(key, propertiesProvider.getProperty(key)));
                    }
                }
                return finalProperties;
            }
            return value;
        }
    }


}
