/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.spring.context;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 533 $, $Date:: 2012-03-07 05:28:35#$
 * @since 1.0
 */
public class ImplBeanNameGenerator extends AnnotationBeanNameGenerator {

    @Override
    protected String buildDefaultBeanName(BeanDefinition definition) {
        String name = super.buildDefaultBeanName(definition);
        if (name.endsWith("Impl")) {
            return name.substring(0, name.lastIndexOf("Impl"));
        }
        return name;
    }
}
