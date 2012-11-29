package com.dell.dw.managers.dataimport.storefront;

import com.dell.dw.persistence.domain.SFOrder;
import com.dell.dw.persistence.domain.SFOrderItem;
import com.dell.dw.persistence.repository.SFOrderRepository;
import com.sourcen.dataimport.service.support.hibernate.BeanProcessorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 5/30/12
 * Time: 3:06 PM
 * To change this template use File | Settings | File Templates.
 */
public final class SFOrderItemBeanProcessor extends BeanProcessorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SFOrderItemBeanProcessor.class);


    @Override
    public boolean supportsBean(final Class clazz) {
        return (SFOrderItem.class.equals(clazz));
    }

    @Override
    public Map<String, Object> preProcessBeanValues(final Map<String, Object> row) {
        if (row.get("order") == null) {
            throw new IllegalArgumentException("order is null");
        }
        SFOrder order= sfOrderRepository.get((Long) row.get("order"));
        row.put("order",order);
        Assert.notNull(order, "Unable to find Order for orderId:=" + row.get("order"));
        return row;
    }



    @Override
    public Object preProcessBeforePersist(final Object bean, Map<String, Object> row) {
        return bean;
    }


    @Qualifier("sfOrderRepositoryImpl")
    @Autowired
    SFOrderRepository sfOrderRepository;

    public SFOrderRepository getSfOrderRepository() {
        return sfOrderRepository;
    }

    public void setSfOrderRepository(SFOrderRepository sfOrderRepository) {
        this.sfOrderRepository = sfOrderRepository;
    }
}
