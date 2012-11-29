package com.dell.dw.managers.dataimport.order;

import com.dell.dw.persistence.domain.*;
import com.dell.dw.persistence.repository.LeadRepository;
import com.dell.dw.persistence.repository.OrderRepository;
import com.dell.dw.persistence.repository.RetailerRepository;
import com.dell.dw.persistence.repository.StoreRepository;
import com.sourcen.dataimport.service.support.hibernate.BeanProcessorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 5/30/12
 * Time: 3:06 PM
 * To change this template use File | Settings | File Templates.
 */
public final class OrderItemBeanProcessor extends BeanProcessorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(OrderItemBeanProcessor.class);

    @Override
    public boolean supportsBean(final Class clazz) {
        return (OrderItem.class.equals(clazz));
    }


    @Override
    public Object preProcessBeforePersist(final Object bean, Map<String, Object> row) {
        Object orderItem =  bean;
        try {

            Assert.notNull(row.get("orderOrderId"), "Order Id is null");
            Assert.notNull(row.get("orderSiteId"), "Site Id is null");
            Assert.notNull(row.get("leadId"), "Lead Id is null");
            Assert.notNull(row.get("leadEntityId"),"Entity Id is null");

            String orderId = row.get("orderOrderId").toString();
            String orderSiteId = row.get("orderSiteId").toString();
            String orderCity = row.get("orderCity").toString();
            String orderState = row.get("orderState").toString();
            String orderCountry = row.get("orderCountry").toString();
            String orderTransactionId = row.get("orderTransactionId").toString();
            Date orderTransactionDate = (Date)row.get("orderTransactionDate");
            String orderStatus = row.get("orderStatus").toString();
            Boolean orderOrderCancelled = (Boolean)row.get("orderOrderCancelled");
            String orderCancelReason = row.get("orderCancelReason").toString();
            Double orderOriginalSaleAmount = (Double)row.get("orderOriginalSaleAmount");
            Double orderOriginalCommissionAmount = (Double)row.get("orderOriginalCommissionAmount");
            Double orderFinalSaleAmount = (Double)row.get("orderFinalSaleAmount");
            Double orderFinalCommissionAmount = (Double)row.get("orderFinalCommissionAmount");
            Double orderTotalDiscountAmountToDate = (Double)row.get("orderTotalDiscountAmountToDate");
            Double orderTotalCancellationAmountToDate = (Double)row.get("orderTotalCancellationAmountToDate");

            String leadId = row.get("leadId").toString();
            String leadSiteId = row.get("leadSiteId").toString();
            String leadEntityId = row.get("leadEntityId").toString();
            String leadRedirectUrl = row.get("leadRedirectUrl").toString();
            int leadMaxOrderCount = (Integer)row.get("leadMaxOrderCount");
            Date leadCreationDate = (Date)row.get("leadCreationDate");
            Date leadExpirationDate = (Date)row.get("leadExpirationDate");

            Retailer retailer = null;
            retailer= (Retailer) retailerRepository.getByRetailerId(orderSiteId);

            if(retailer == null){
                retailer = new Retailer(orderSiteId);
                retailerRepository.insert(retailer);
            }

            Lead lead = null;
            lead= (Lead) leadRepository.getByLeadId(leadId);

            Store store = null;
            store = (Store) storeRepository.getByStoreId(leadEntityId);

            if(store == null){
                store = new Store(leadEntityId,retailer);
                storeRepository.insert(store);
            }

            if(lead == null){
                lead = new Lead(leadId,retailer,store,leadRedirectUrl,leadMaxOrderCount,leadCreationDate,leadExpirationDate);
                leadRepository.insert(lead);
            }

            Order order = null;
            order = (Order) orderRepository.getByOrderId(orderId);

            if(order == null){
                order = new Order(orderId,lead,retailer,orderCity,orderState,orderCountry,orderTransactionId,orderTransactionDate,orderStatus,orderOrderCancelled,orderCancelReason,orderOriginalSaleAmount,orderOriginalCommissionAmount,orderFinalSaleAmount,orderFinalCommissionAmount,orderTotalDiscountAmountToDate,orderTotalCancellationAmountToDate);

                orderRepository.insert(order);
            }

            ((OrderItem)bean).setOrder(order);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }

        return bean;
    }



    @Autowired
    OrderRepository orderRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    RetailerRepository retailerRepository;

    @Autowired
    LeadRepository leadRepository;

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public RetailerRepository getRetailerRepository() {
        return retailerRepository;
    }

    public void setRetailerRepository(RetailerRepository retailerRepository) {
        this.retailerRepository = retailerRepository;
    }

    public StoreRepository getStoreRepository() {
        return storeRepository;
    }

    public void setStoreRepository(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public LeadRepository getLeadRepository() {
        return leadRepository;
    }

    public void setLeadRepository(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }
}
