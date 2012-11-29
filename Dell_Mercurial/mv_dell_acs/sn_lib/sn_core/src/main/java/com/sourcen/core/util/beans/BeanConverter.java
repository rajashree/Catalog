/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util.beans;

import com.sourcen.core.persistence.domain.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2947 $, $Date:: 2012-06-06 09:31:23#$
 */
public class BeanConverter<I, O> {

    private Class inputSuperclass;

    private Class outputSuperclass;

    private ValueConverter converter;

    private static final Logger logger = LoggerFactory.getLogger(BeanConverter.class);

    public BeanConverter(final Class<I> inputSuperclass, final Class<O> outputSuperclass,
            final ValueConverter converter) {
        this.inputSuperclass = inputSuperclass;
        this.outputSuperclass = outputSuperclass;
        this.converter = converter;
    }

    public <T extends O> T convert(I input, T output, MappingContext mappingContext) {
        return convert(input, output,mappingContext, null);
    }

    public <T extends O> T convert(I input, T output, MappingContext mappingContext, Object parent) {
        if (mappingContext == null) {
            mappingContext = new MappingContext();
        }
        if (canConvert(input)) {
            copyValues(input, output, mappingContext, parent);
            return output;
        }
        return null;
    }

    public <T extends O> Collection<T> convert(Collection<? extends I> input, Class outputClass,
                                               final MappingContext mappingContext) {
        return convert(input, outputClass, mappingContext, null);
    }
    public <T extends O> Collection<T> convert(Collection<? extends I> input, Class outputClass,
            final MappingContext mappingContext, Object parent) {
        if (!outputSuperclass.isAssignableFrom(outputClass)) {
            throw new RuntimeException(outputClass + " must be of type: " + outputSuperclass);
        }
        if (input.isEmpty()) {
            return Collections.emptyList();
        }
        Assert.notNull(mappingContext);
        Collection<O> items;
        if(input instanceof Set){
            items = new HashSet<O>(input.size());
        }  else{
            items = new ArrayList<O>(input.size());
        }

        if (!input.isEmpty()) {
            try {
                for (I inputItem : input) {
                    O output = (O) outputClass.newInstance();
                    convert(inputItem, output, mappingContext, parent);
                    items.add(output);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return (Collection<T>) items;
        }
        return Collections.emptyList();
    }

    private static final Map<Class, Map<String, PropertyDescriptor[]>> pdCache = new ConcurrentHashMap<Class, Map<String, PropertyDescriptor[]>>();

    private static final Map<Class, Map<String, String[]>> scopeCache = new ConcurrentHashMap<Class, Map<String, String[]>>();

    public static String[] getFieldsForScope(Class<?> clazz, String scopeName) {
        clazz = ClassUtils.getUserClass(clazz);
        Map<String, String[]> scopeFields = scopeCache.get(clazz);
        if (scopeFields == null) {
            // cache it
            scopeFields = new ConcurrentHashMap<String, String[]>(5);

            Scopes scopes = clazz.getAnnotation(Scopes.class);
            Scope singleScope = clazz.getAnnotation(Scope.class);
            Collection<Scope> allScopes = new HashSet<Scope>();
            if (scopes != null) {
                Collections.addAll(allScopes, scopes.value());
            }
            if (singleScope != null) {
                allScopes.add(singleScope);
            }

            if (allScopes.isEmpty()) {
                // add default scope.
                if (ScopeAware.class.isAssignableFrom(clazz)) {
                    final Set<String> fields = new HashSet<String>();
                    ReflectionUtils.doWithFields(clazz, new ReflectionUtils.FieldCallback() {
                        @Override
                        public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
                            Class<?> fieldType = field.getType();

                            if (ClassUtils.isPrimitiveOrWrapper(fieldType) || fieldType.equals(String.class) ||
                                    Date.class.isAssignableFrom(fieldType) ||
                                    ScopeAware.class.isAssignableFrom(fieldType)) {
                                fields.add(field.getName());
                            }
                        }
                    });
                    String[] simpleFields = new String[fields.size()];
                    fields.toArray(simpleFields);
                    scopeFields.put("default", simpleFields);
                    if(fields.contains("id")){
                        scopeFields.put("id", new String[]{"id"});
                    }
                }
            } else {
                for (Scope scope : allScopes) {
                    scopeFields.put(scope.name(), scope.fields());
                }
                if (ScopeAware.class.isAssignableFrom(clazz) && !scopeFields.containsKey("id")) {
                    scopeFields.put("id", new String[]{"id"});
                }
            }
            scopeCache.put(clazz, scopeFields);
        }
        return scopeFields.get(scopeName);
    }

    private void copyValues(final Object source, final Object target, final MappingContext mappingContext, Object parent) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        if(parent == null){
            parent = new Object[]{source, target, null};
        }

        if(!parent.getClass().isArray()){
            parent = new Object[]{parent, target, null};
        }

        Class<?> actualEditable = target.getClass();
        if(ClassUtils.isCglibProxyClass(actualEditable)){
            actualEditable = actualEditable.getSuperclass();
        }
        Map<String, PropertyDescriptor[]> classCache = pdCache.get(actualEditable);
        if (classCache == null) {
            classCache = new ConcurrentHashMap<String, PropertyDescriptor[]>();
            PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
            Scopes scopes = actualEditable.getAnnotation(Scopes.class);
            Scope singleScope = actualEditable.getAnnotation(Scope.class);
            Collection<Scope> allScopes = new HashSet<Scope>();
            if (scopes != null) {
                Collections.addAll(allScopes, scopes.value());
            }
            if (singleScope != null) {
                allScopes.add(singleScope);
            }

            classCache.put("all", targetPds);

            // check if ID scope is possible.
            try{
                PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(actualEditable,"id");
                if(pd != null){
                    classCache.put("id", new PropertyDescriptor[]{pd});
                }
            } catch (Throwable e){
                // ignore
            }

            if (!allScopes.isEmpty()) {
                for (Scope scope : allScopes) {
                    Collection<PropertyDescriptor> finalPds = new HashSet<PropertyDescriptor>();
                    Collection<String> selectedFieldNames = Arrays.asList(scope.fields());
                    for (PropertyDescriptor pd : targetPds) {
                        if (selectedFieldNames.contains(pd.getName())) {
                            finalPds.add(pd);
                        }
                    }
                    PropertyDescriptor[] finalPdsArray = new PropertyDescriptor[finalPds.size()];
                    finalPds.toArray(finalPdsArray);
                    classCache.put(scope.name(), finalPdsArray);
                }
            }

            // check if default scope exists? if not add one.
            // now set the default to only include simple fields.
            if(!classCache.containsKey("default")){
                final Set<PropertyDescriptor> defaultFields = new HashSet<PropertyDescriptor>();
                for(PropertyDescriptor pd : targetPds){
                    Class<?> fieldType = pd.getPropertyType();
                    if (ClassUtils.isPrimitiveOrWrapper(fieldType) || fieldType.equals(String.class) ||
                            Date.class.isAssignableFrom(fieldType) ||
                            ScopeAware.class.isAssignableFrom(fieldType)) {
                        defaultFields.add(pd);
                    }
                }
                PropertyDescriptor[] simpleFields = new PropertyDescriptor[defaultFields.size()];
                defaultFields.toArray(simpleFields);
                classCache.put("default", simpleFields);
            }

            pdCache.put(actualEditable, classCache);
        }

        PropertyDescriptor[] targetPds;

        // check if target classes have scope..
        // mappingContext.getScope() > targetClass.scopes > sourceClass.scopes
        String fieldScopeName = null;
        Object parentTarget = ((Object[]) parent)[1];
        if (parentTarget != null) {
            Class<?> parentTargetClass = parentTarget.getClass();
            if(ClassUtils.isCglibProxyClass(parentTargetClass)){
                parentTargetClass = parentTargetClass.getSuperclass();
            }
            Map<String, String> parentScopes = mappingContext.getStackForClass(parentTargetClass);
            if (parentScopes != null && !parentScopes.isEmpty()) {
                Field field = (Field)((Object[]) parent)[2];
                if(field != null){
                    String key = actualEditable.getCanonicalName() + "." + field.getName();
                    if (parentScopes.containsKey(key)) {
                        fieldScopeName = parentScopes.get(key);
                    }
                }
            }
        }
        String scope = null;
        if(fieldScopeName != null){
            scope = fieldScopeName;
        } else {
            scope = mappingContext.getScope();
        }

        if(scope == null){
            // damn.. scope is null. should we use the defaultScope.
            if(mappingContext.getProperties().getBooleanProperty("useDefaultScope",true)){
                scope = "default";
            }
        }

        if (scope != null) {
            targetPds = pdCache.get(actualEditable).get(scope);
            if (targetPds == null) {
                // we didnt find any for the scope, lets get the default scope.
                targetPds = pdCache.get(actualEditable).get("default");
            }
        } else {
            targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        }
        if (targetPds == null) {
            throw new IllegalArgumentException("target property descriptors is null. something is very wrong.");
        }

        for (PropertyDescriptor targetPd : targetPds) {
            try {
                if (targetPd.getWriteMethod() == null) {
                    continue;
                }
                Object value = null;
                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                // check for FieldMapping.
                Field field = ReflectionUtils.findField(actualEditable, targetPd.getName());
                if (field != null) {
                    if (Modifier.isTransient(field.getModifiers())) {
                        logger.debug("skipping field from conversion because its transient.");
                        continue;
                    }
                    FieldMapping annotation = field.getAnnotation(FieldMapping.class);
                    if (annotation != null) {
                        String mapping = annotation.value();
                        String[] depth = mapping.split("\\.");
                        try {
                            Object depthValue = source;
                            for (String depthNode : depth) {

                                Assert.isTrue(depthNode != null && depthNode.trim().length() > 0,
                                        "@Fieldmapping has a invalid value for mapping:=" + mapping + " on field:=" +
                                                field.toString());

                                if (depthValue == null) {
                                    logger.info(
                                            "while mapping @FieldMaping fields, we found a null value for mapping:=" +
                                                    mapping + " on field:=" + field.toString());
                                    break;
                                }


                                PropertyDescriptor pd = BeanUtils
                                        .getPropertyDescriptor(depthValue.getClass(), depthNode);
                                if (pd == null) {
                                    logger.warn(" unable to find PD in class:=" + source.getClass() + ", depthNode:=" +
                                            depthNode);
                                }
                                if (pd.getReadMethod() != null) {
                                    if (depthValue == null) {
                                        logger.info("Unable to find value for " + mapping + " in " + source);
                                        break;
                                    }
                                    depthValue = pd.getReadMethod().invoke(depthValue);
                                }
                            }
                            if (depthValue != null) {
                                value = depthValue;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (sourcePd != null && sourcePd.getReadMethod() != null) {
                        Method readMethod = sourcePd.getReadMethod();
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        value = readMethod.invoke(source);
                    }
                    Scope fieldScope = field.getAnnotation(Scope.class);
                    if (fieldScope != null) {
                        Class fieldType = field.getType();
                        String key = null;
                        if(Collection.class.isAssignableFrom(fieldType)){
                            // get the generic type
                            Type[] collctionTypes = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
                            if(collctionTypes.length >0){
                                key = ((Class) collctionTypes[0]).getCanonicalName();
                            }
                        }else if(Map.class.isAssignableFrom(fieldType)){
                            Type[] collctionTypes = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
                            if(collctionTypes.length >1){ // has to be! this is a Map<K,V> doh!
                                key = ((Class) collctionTypes[1]).getCanonicalName();
                            }
                        } else{
                            key = field.getType().getCanonicalName();
                        }

                        if(key != null){
                            key = key +"."+field.getName();
                            mappingContext.getStackForClass(actualEditable).put(key, fieldScope.name());
                        }
                    }
                }

                if (value != null) {
                    if (inputSuperclass.isAssignableFrom(value.getClass())) {
                        Class converter = getAlias(value.getClass());
                        if (converter == null) {
                            converter = getAlias(value.getClass().getSuperclass());
                        }
                        if (converter == null) {
                            throw new RuntimeException("Unable to convert :" + value.getClass() + " into a WSBean.");
                        }
                        O bean = (O) converter.newInstance();
                        value = convert((I) value, bean, mappingContext, new Object[]{source, target, field});
                    } else if (value instanceof Collection) {
                        Collection collectionValues = (Collection) value;
                        if (!collectionValues.isEmpty()) {
                            Object collectionItem = collectionValues.iterator().next();
                            if (inputSuperclass.isAssignableFrom(collectionItem.getClass())) {
                                // must convert collection.
                                Class collectionClass = getAlias(collectionItem.getClass());
                                if (collectionClass == null) {
                                    throw new RuntimeException(
                                            "unable to determine alias for type:=" + collectionItem.getClass());
                                }
                                value = convert((Collection<I>) collectionValues, collectionClass, mappingContext, new Object[]{source,target, field});
                            }
                        } else {
                            // patched by Navin
                            if (value instanceof Set) {
                                value = Collections.emptySet();
                            } else {
                                value = Collections.emptyList();
                            }
                        }
                    } else if (converter != null) {
                        value = converter.convert(value);
                    }
                }
                Method writeMethod = targetPd.getWriteMethod();
                if (writeMethod != null) {
                    if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                        writeMethod.setAccessible(true);
                    }
                    try {
                        writeMethod.invoke(target, value);
                    } catch (Exception e) {
                        logger.error("Unable to set value for method :=" + writeMethod + " with value:=" + value);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private final Map<Class, Class> BEAN_ALIAS = new ConcurrentHashMap<Class, Class>();

    private boolean canConvert(final Object source) {
        if (source == null) {
            return false;
        }

        Class clazz = source.getClass();

        if ((source instanceof Entity) && autoRegisterEntities && !BEAN_ALIAS.containsKey(clazz)) {
            BEAN_ALIAS.put(clazz, clazz);
            return true;
        }

        if (BEAN_ALIAS.containsKey(clazz)) {
            return true;
        }
        clazz = clazz.getSuperclass();
        if (clazz != null && BEAN_ALIAS.containsKey(clazz)) {
            return true;
        }
        return false;
    }

    private Class getAlias(Class source) {
        if(ClassUtils.isCglibProxyClass(source)){
            source = source.getSuperclass();
        }
        Class clazz = BEAN_ALIAS.get(source);
        if (clazz == null && Entity.class.isAssignableFrom(source) && autoRegisterEntities) {
            BEAN_ALIAS.put(source, source);
            return source;
        }
        return clazz;
    }

    public void registerAlias(final Class source, final Class destination) {
        BEAN_ALIAS.put(source, destination);
    }

    private boolean autoRegisterEntities = false;

    public void autoRegisterEntities(final boolean autoRegisterEntities) {
        this.autoRegisterEntities = autoRegisterEntities;
    }

    public static interface ValueConverter {

        Object convert(Object input);
    }

}
