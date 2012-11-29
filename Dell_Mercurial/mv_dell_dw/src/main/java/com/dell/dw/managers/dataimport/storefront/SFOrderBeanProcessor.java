package com.dell.dw.managers.dataimport.storefront;

import com.dell.dw.persistence.domain.SFOrder;
import com.dell.dw.persistence.domain.Store;
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
public class SFOrderBeanProcessor extends BeanProcessorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SFOrderBeanProcessor.class);


    @Override
    public boolean supportsBean(final Class clazz) {
        return SFOrder.class.equals(clazz);
    }

    @Override
    public Map<String, Object> preProcessBeanValues(final Map<String, Object> row) {
        if (row.get("store") == null) {
            throw new IllegalArgumentException("Store is null");
        }
        Store store = storeRepository.get((Long) row.get("store"));
        row.put("store", store);
        org.springframework.util.Assert.notNull(store, "Unable to find store:=" + row.get("store"));
        return row;
    }


    @Override
    public Object preProcessBeforePersist(final Object bean, Map<String, Object> row) {
        Store store = (Store)row.get("store");
        if(store != null){
            store.setStoreOwnerName(row.get("storeOwnerName").toString());
            store.setStoreOwnerId(row.get("storeOwnerId").toString());
            ((SFOrder)bean).setStore(store);
        }
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
