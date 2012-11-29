/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */

package com.sourcen.core.event.spring;

import com.sourcen.core.event.EventDispatcher;
import com.sourcen.core.event.registrar.BeanFactory;
import com.sourcen.core.event.registrar.BeanFactoryInitializedEvent;
import com.sourcen.core.event.registrar.EventRegistrarForBeans;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class SpringEventRegistry implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        BeanFactoryInitializedEvent event = new BeanFactoryInitializedEvent();
        event.setBeanFactory(new SpringBeanFactoryWrapper(context));
        EventDispatcher.dispatch(event);

        if (!(context instanceof ConfigurableApplicationContext)) {
            throw new IllegalArgumentException("The injected applictionContext is not configurable. " +
                    "We require ConfigurableApplicationContext to be injected but got:=" + context.getClass());

        }

        ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) context).getBeanFactory();
        // once this is loaded, lets start processing all the non-singleton events.

        // register singletons as objects.
        for (String singletonBeanName : beanFactory.getSingletonNames()) {
            EventListener annotation = beanFactory.findAnnotationOnBean(singletonBeanName, EventListener.class);
            if (annotation != null) {
                EventRegistrarForBeans.getInstance().add(singletonBeanName);
            }
        }

        // register other beans
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            EventListener annotation = beanFactory.findAnnotationOnBean(beanName, EventListener.class);
            if (annotation != null) {
                EventRegistrarForBeans.getInstance().add(beanName);
            }
        }

    }

    private static final class SpringBeanFactoryWrapper implements BeanFactory {

        private ApplicationContext applicationContext;

        public SpringBeanFactoryWrapper(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }

        @Override
        public Object getBean(String name) {
            return applicationContext.getBean(name);
        }

        @Override
        public Class getType(String beanName) {
            return applicationContext.getType(beanName);
        }
    }

}
