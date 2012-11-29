package com.dell.dw.managers.dataimport.storefront;

import com.dell.dw.persistence.domain.Retailer;
import com.dell.dw.persistence.domain.Store;
import com.dell.dw.persistence.repository.RetailerRepository;
import com.sourcen.core.util.WebUtils;
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
public class StoreBeanProcessor extends BeanProcessorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(StoreBeanProcessor.class);


    @Override
    public boolean supportsBean(final Class clazz) {
        return Store.class.equals(clazz);
    }

    @Override
    public Object preProcessBeforePersist(final Object bean, Map<String, Object> row) {

        String merchantId = row.get("merchantId").toString();
        Retailer retailer = null;
        if(!WebUtils.isNullOrEmpty(merchantId)){
            retailer = retailerRepository.getByRetailerId(merchantId);
            if(retailer == null){
                retailer = new Retailer(row.get("merchantName").toString(),row.get("merchantId").toString(),row.get("merchantName").toString());
                retailerRepository.insert(retailer);
            }
        }
        if(retailer != null){
            ((Store)bean).setMerchant(retailer);
        }
        return bean;

    }
    @Autowired
    RetailerRepository retailerRepository;

    public RetailerRepository getRetailerRepository() {
        return retailerRepository;
    }

    public void setRetailerRepository(RetailerRepository retailerRepository) {
        this.retailerRepository = retailerRepository;
    }

}
