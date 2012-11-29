package com.sourcen.core.event.spring;/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.context.annotation.ScopeMetadataResolver;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * This class provides runtime spring scope resolution for all EventListener.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class EventListenerScopeMetadataResolver implements ScopeMetadataResolver {

    //
    // =================================================================================================================
    // == Class Fields ==
    // =================================================================================================================
    //

    /**
     * the annotation type to search in the event listeners if it has a scope specified.
     */
    private final Class<? extends Annotation> scopeAnnotationType = Scope.class;

    /**
     * the default scope for proxy mode.
     */
    private final ScopedProxyMode defaultProxyMode;

    //
    // =================================================================================================================
    // == Constructors ==
    // =================================================================================================================
    //

    /**
     * default constructor.
     */
    public EventListenerScopeMetadataResolver() {
        this.defaultProxyMode = ScopedProxyMode.NO;
    }

    /**
     * initialize with a scoped proxy mode.
     *
     * @param defaultProxyMode to be set.
     */
    public EventListenerScopeMetadataResolver(final ScopedProxyMode defaultProxyMode) {
        Assert.notNull(defaultProxyMode, "'defaultProxyMode' must not be null");
        this.defaultProxyMode = defaultProxyMode;
    }

    //
    // =================================================================================================================
    // == Class Methods ==
    // =================================================================================================================
    //

    @Override
    public final ScopeMetadata resolveScopeMetadata(final BeanDefinition definition) {
        final ScopeMetadata metadata = new ScopeMetadata();
        if (definition instanceof AnnotatedBeanDefinition) {
            final AnnotatedBeanDefinition annDef = (AnnotatedBeanDefinition) definition;
            final Map<String, Object> attributes = annDef.getMetadata().getAnnotationAttributes(this.scopeAnnotationType.getName());
            if (attributes != null) {
                metadata.setScopeName((String) attributes.get("value"));
                ScopedProxyMode proxyMode = (ScopedProxyMode) attributes.get("proxyMode");
                if (proxyMode == null || proxyMode == ScopedProxyMode.DEFAULT) {
                    proxyMode = this.defaultProxyMode;
                }
                metadata.setScopedProxyMode(proxyMode);
            } else {
                metadata.setScopeName(BeanDefinition.SCOPE_PROTOTYPE);
            }
        }
        return metadata;
    }

}
