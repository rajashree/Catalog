/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */


package com.sourcen.core.config.util.spring;

import com.sourcen.core.config.ConfigurationServiceEvent;
import com.sourcen.core.event.spring.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

import java.util.regex.Pattern;

/**
 * A event listener class that is processed on ConfigurationEvents and resets the application settings based on the
 * properties.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 * @since 1.01
 */

@EventListener
public class SpringBeanPropertyConfigurer implements ApplicationContextAware, ConfigurationServiceEvent.PropertyUpdated {

    private static final Logger log = LoggerFactory.getLogger(SpringBeanPropertyConfigurer.class);

    private ConfigurableListableBeanFactory beanFactory;

    public SpringBeanPropertyConfigurer() {
    }


    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof ConfigurableApplicationContext) {
            this.beanFactory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        }
    }


    public void onPropertyUpdated(final ConfigurationServiceEvent evt) {

        Assert.notNull(this.beanFactory, "The beanFactory for the SpringBeanPlaceholderListener is null");

        final String key = evt.getPropertyName();
        final String value = evt.getPropertyValue().toString();

        // check if spring bean property was modified.
        if (key.startsWith("spring.beans.")) {
            String beanName = key.substring(13);

            beanName = beanName.substring(0, beanName.indexOf('.'));
            final String beanPrefix = "spring.beans.".concat(beanName) + '.';

            if (this.beanFactory.containsBeanDefinition(beanName)) {
                final BeanDefinition definition = this.beanFactory.getBeanDefinition(beanName);
                final String prop = Pattern.compile(beanPrefix, Pattern.LITERAL).split(key)[1];

                if (SpringBeanPropertyConfigurer.log.isDebugEnabled()) {
                    SpringBeanPropertyConfigurer.log.debug("setting property key:=" + key + " value:=" + value + " for beanName:=" + beanName + " isSingleton:="
                            + definition.isSingleton());
                }

                if (definition.isSingleton()) {
                    try {
                        final BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this.beanFactory.getBean(beanName));
                        bw.setPropertyValue(prop, value);
                    } catch (final Exception e) {
                        SpringBeanPropertyConfigurer.log.warn("Unable to set property:" + prop + " on bean:" + beanName + " with value:" + value, e);
                    }
                } else {
                    definition.getPropertyValues().addPropertyValue(new PropertyValue(prop, value));
                }
            } else if (SpringBeanPropertyConfigurer.log.isDebugEnabled()) {
                SpringBeanPropertyConfigurer.log.debug("the property " + key + " was set, but didn't find any spring beans to apply it on.");
            }
        }
    }

}
