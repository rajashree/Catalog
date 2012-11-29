package com.dell.dw.managers.dataimport.ga;

import com.dell.dw.persistence.domain.GAEventDimension;
import com.dell.dw.persistence.domain.GAEventMetrics;
import com.dell.dw.persistence.domain.GAWebPropertyProfile;
import com.dell.dw.persistence.repository.GAEventDimensionRepository;
import com.dell.dw.persistence.repository.GAWebPropertyProfileRepository;
import com.sourcen.dataimport.service.support.hibernate.BeanProcessorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 5/28/12
 * Time: 11:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class GAEventBeanProcessor extends BeanProcessorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(GAEventBeanProcessor.class);
    @Override
    public boolean supportsBean(final Class clazz) {
        return GAEventMetrics.class.equals(clazz);
    }

    /**
     *  Persist GAEventDimension if not already exists before saving GAEventMetrics
     * @param bean
     * @param row
     * @return
     */
    @Override
    public Object preProcessBeforePersist(final Object bean, Map<String, Object> row) {
        GAEventMetrics eventMetrics = (GAEventMetrics) bean;
        try {

            // Get Event Dimension if already exists
            String eventCategory =  row.get("eventCategory").toString();
            String eventAction = row.get("eventAction").toString();
            String eventLabel = row.get("eventLabel").toString();
            /*if(eventLabel.equals("pc")){
                System.out.println("lowercase");
            }*/
            Long profileId = Long.parseLong(row.get("profileId").toString());
            GAEventDimension eventDimension = gaEventDimensionRepository
                    .getEventDimension(eventCategory, eventAction, eventLabel, profileId);

            // Insert event dimension if not available
            if(eventDimension == null) {
                // Get Web Property Profile
                GAWebPropertyProfile webPropertyProfile =
                        gaWebPropertyProfileRepository.get(profileId);

                eventDimension = new GAEventDimension(webPropertyProfile,eventCategory,eventAction,eventLabel);
                gaEventDimensionRepository.insert(eventDimension);
            }

            // Set Event dimensions to the  GAEventMetrics
            eventMetrics.setEventDimension(eventDimension);

        } catch (RuntimeException e) {
           logger.error(e.getMessage(), e);
        }
        return eventMetrics;
    }

    @Autowired
    public GAEventDimensionRepository gaEventDimensionRepository;

    @Autowired
    public GAWebPropertyProfileRepository gaWebPropertyProfileRepository;

    public GAEventDimensionRepository getGaEventDimensionRepository() {
        return gaEventDimensionRepository;
    }

    public void setGaEventDimensionRepository(GAEventDimensionRepository gaEventDimensionRepository) {
        this.gaEventDimensionRepository = gaEventDimensionRepository;
    }

    public GAWebPropertyProfileRepository getGaWebPropertyProfileRepository() {
        return gaWebPropertyProfileRepository;
    }

    public void setGaWebPropertyProfileRepository(GAWebPropertyProfileRepository gaWebPropertyProfileRepository) {
        this.gaWebPropertyProfileRepository = gaWebPropertyProfileRepository;
    }
}
