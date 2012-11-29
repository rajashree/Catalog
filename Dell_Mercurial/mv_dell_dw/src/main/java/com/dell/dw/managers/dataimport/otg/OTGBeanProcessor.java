package com.dell.dw.managers.dataimport.otg;

import com.dell.dw.persistence.domain.Order;
import com.dell.dw.persistence.repository.StoreRepository;
import com.sourcen.dataimport.service.support.hibernate.BeanProcessorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 5/30/12
 * Time: 3:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class OTGBeanProcessor extends BeanProcessorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(OTGBeanProcessor.class);


    @Override
    public boolean supportsBean(final Class clazz) {
        return Order.class.equals(clazz);
    }

    @Override
    public Object preProcessBeforePersist(final Object bean, Map<String, Object> row) {
       return bean;
    }

    @Autowired
    StoreRepository storeRepository;

    public StoreRepository getStoreRepository() {
        return storeRepository;
    }

    public void setStoreRepository(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }
}
